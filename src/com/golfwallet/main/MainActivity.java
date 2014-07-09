package com.golfwallet.main;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.ReaderCallback;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.golfwallet.R;
import com.golfwallet.connectivity.APITest;
import com.golfwallet.connectivity.HTTPRequest;
import com.golfwallet.identification.LoginActivity;
import com.golfwallet.identification.SigninActivity;
import com.golfwallet.nfc.IsoDepAdapter;
import com.golfwallet.nfc.IsoDepTransceiver;
import com.golfwallet.nfc.IsoDepTransceiver.OnMessageReceived;
import com.golfwallet.preference.ReadWriteData;

/** \mainpage
* \section intro Introduction
* Android application for the NFC Golf Ball Dispenser.
* You can :
* * Create an account
* * Log into your account
* * Watch your last transactions
* * Know your balance and account status
* * Make a transaction using NFC
* \section overview Overview
* \subsection step1 Home view with the app installed
* ![Home view](../img/aHome.png)
* \subsection step2 In-app views
* ![Home view](../img/aViews.png)
* \section classdiagram Class Diagram
* ![Home view](../img/classDiagram.png)
* \section license License
* This software is developped by Jacky CASAS and was commissioned by Digiclever. 
* For further information about the license, please contact Digiclever (http://digiclever.com).<br /><br />
* This project was developped at HES-SO//Valais during the bachelor thesis of Jacky Casas in 2014.
*/

/** \brief
 * <b>Main activity of the app.</b>
 * <p> 
 * When the user is not logged, there is two button :
 * <ul>
 * <li>Login</li>
 * <li>Signin</li>
 * </ul>
 * </p>
 * <p>
 * And when connected, different information are shown :
 * <ul>
 * <li>User name</li>
 * <li>Balance of the user account</li>
 * <li>Account status</li>
 * <li>Date of registration</li>
 * <li>List of the last transations</li>
 * </ul>
 * </p>
 * 	\author	Jacky CASAS
 * 	\version 1.0
 * 	\date 04.06.2014  
 */
public class MainActivity extends Activity implements OnMessageReceived,
		ReaderCallback {

	private NfcAdapter nfcAdapter;			///< NfcAdapter fot the NFC support
	private IsoDepAdapter isoDepAdapter;	///< IsoDepAdapter for the NFC support
	private static Context context = null;	///< Context of the application
	
	private TextView titleTextView;			///< TextView to display the name of the user
	private TextView balanceTextView;		///< TextView to display the account balance
	private TextView statusTextView;		///< TextView to display the account status
	private TextView dateTextView;			///< TextView to display the date of account creation
	private ListView transactionListView;	///< ListView to display last transactions
	
	private Button loginButton;				///< Button to access login activity
	private Button signinButton;			///< Button to access signin activity
	
	/**	
	 *  \brief Method to get the context of the application
	 *  \return The context of the application
	 */
	public static Context getAppContext() {
		return MainActivity.context;
	}
	
	/**	
	 *  \brief Change the connected status of a user in the application
	 * 	\param value True for connected and False to unconnected
	 */
	public void setConnected(boolean value) {
		boolean connected = ReadWriteData.getConnectionState(context);
		
		if (!connected) {
			// connection
			ReadWriteData.setConnected(context);
			Toast.makeText(MainActivity.this, "Connection",Toast.LENGTH_SHORT).show(); 
			
			Intent mainIntent = new Intent(MainActivity.this, MainActivity.class);
			startActivity(mainIntent);
			finish();
		} else {
			// disconnection
			ReadWriteData.disconnect(context);
			ReadWriteData.clearAll(context);
			Intent mainIntent = new Intent(MainActivity.this, MainActivity.class);
			startActivity(mainIntent);
			finish();
		}
	}

	/**	
	 *  \brief
	 * 	Method called when the activity is created
	 * 	\param savedInstanceState Bundle used to pass information from previous activity to this one
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		MainActivity.context = getApplicationContext();
		
		isoDepAdapter = new IsoDepAdapter(getLayoutInflater());
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);

		String username = ReadWriteData.read(context, ReadWriteData.LOGIN_USERNAME);
		String password = ReadWriteData.read(context, ReadWriteData.LOGIN_PASSWORD);
		
		if (ReadWriteData.getConnectionState(context) && username != null && password != null) {
			
			setContentView(R.layout.fragment_main);
			titleTextView = (TextView)findViewById(R.id.title);
			balanceTextView = (TextView)findViewById(R.id.balance);
			statusTextView = (TextView)findViewById(R.id.accountStatus);
			dateTextView = (TextView)findViewById(R.id.registrationDate);
			transactionListView = (ListView)findViewById(R.id.transactionList);
			
			// GET NEW TOKEN
			String token = null;
			try {
				token = new HTTPRequest().execute("/api/token", username, password).get();
				
				if (token != null) {
					ReadWriteData.write(context, ReadWriteData.LOGIN_TOKEN, token);
					saveAID();
				} else {
					Toast.makeText(MainActivity.this, "Connection issues, retry to connect.", Toast.LENGTH_SHORT).show();
					setConnected(false);
				}
				
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
				Toast.makeText(MainActivity.this, "Connection issues, retry to connect.", Toast.LENGTH_SHORT).show();
				setConnected(false);
			}
			
			// GET ACTIVITY CONTENT FROM API : USER INFORMATIONS
			String result = "error";
			try {
				if (token == null) {
					result = new HTTPRequest().execute("/api/user", username, password).get();
				} else {
					result = new HTTPRequest().execute("/api/user", token, "").get();
				}
		        
		        try {
		        	JSONObject jsonObj = new JSONObject(result);
					JSONObject user = (JSONObject) jsonObj.get("user");
		            
					if (user.has("name") && user.has("surname")) {
						titleTextView.setText("Hi, " + user.getString("name") + " " + user.getString("surname") + " !");
					} else {
						titleTextView.setText("Hi, " + user.getString("username") + " !");
					}
					
					balanceTextView.setText("balance : " + user.getInt("balance") + " CHF");
					
					switch(user.getInt("statement")) {
						case Constants.STA_USER_ACTIVE:
							statusTextView.setText("account status : active");
							break;
						case Constants.STA_USER_DELETED:
							statusTextView.setText("account status : deleted");
							break;
						case Constants.STA_USER_INACTIVE:
							statusTextView.setText("account status : inactive");
							break;
					}
					dateTextView.setText("registration date : " + user.getString("registrationDate").replace(" GMT",""));
		        } catch (JSONException e) {
		            e.printStackTrace();
		        }
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
					
			// GET ACTIVITY CONTENT FROM API : TRANSACTIONS FROM USER
			result = "error";
			try {
				if (token == null) {
					result = new HTTPRequest().execute("/api/transactions", username, password).get();
				} else {
					result = new HTTPRequest().execute("/api/transactions", token, "").get();
				}
		        
				List<Transaction> transactionList = new ArrayList<Transaction>();
		        try {
		        	JSONObject jsonObj = new JSONObject(result);
					JSONArray json = (JSONArray) jsonObj.get("transactions");
					
					Toast.makeText(MainActivity.getAppContext(), "Your " + json.length() + " last transactions", Toast.LENGTH_LONG).show();
					
		            for (int i = 0; i < json.length(); i++) {
		                JSONObject c = json.getJSONObject(i);
		                int amount = c.getInt("amount");
		                int type = c.getInt("transactionType");
		                String transactionDate = c.getString("transactionDate");
		                String deviceId = c.getString("deviceId");
		                String dispenserId = c.getString("dispenserId");
		                
		                transactionList.add(new Transaction(amount, type, transactionDate, deviceId, dispenserId));
		            }
		        } catch (JSONException e) {
		            e.printStackTrace();
		        }

		        TransactionAdapter transactionAdapter = new TransactionAdapter(this, R.layout.transaction_row, transactionList);

		        transactionListView.setAdapter(transactionAdapter);
		        
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		} else {
			setContentView(R.layout.log_layout);
			loginButton = (Button)findViewById(R.id.loginbutton);
			signinButton = (Button)findViewById(R.id.signinbutton);
			
			loginButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
					startActivity(loginIntent);
					finish();
				}
			});
			
			signinButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent signinIntent = new Intent(MainActivity.this, SigninActivity.class);
					startActivity(signinIntent);
					finish();
				}
			});
		}	
	}

	/**	
	 *  \brief
	 * 	Method which create the option menu of the top right of the app.
	 * 	\param menu Menu that contains the different items to be displayed
	 *  \return True if the menu is correctly created
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu); 

		if (ReadWriteData.getConnectionState(context)) {
			menu.findItem(R.id.action_logout).setTitle("Logout");
		} else {
			menu.findItem(R.id.action_logout).setTitle("Login");
		}
		return true;
	}

	/**	
	 *  \brief
	 * 	Method called when the user click on a menu item.
	 * 	\param item An item of the menu
	 * 	\return Say if the action of selection is made correctly or not
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Toast.makeText(MainActivity.this, "Settings clicked",
					Toast.LENGTH_SHORT).show();
			Intent apiTestIntent = new Intent(MainActivity.this, APITest.class);
			MainActivity.this.startActivity(apiTestIntent);
			return true;
		} else if (id == R.id.action_logout) {
			if (ReadWriteData.getConnectionState(context)) {
				Toast.makeText(MainActivity.this, "Bye bye !", Toast.LENGTH_SHORT).show();
				
				item.setTitle("Login");
				setConnected(false);
			} else {
				item.setTitle("Logout");
				Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
				startActivity(loginIntent);
				finish();
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**	
	 *  \brief
	 * 	Method called when the activity is brought to the front.
	 */
	@Override
	public void onResume() {
		super.onResume();
		nfcAdapter.enableReaderMode(this, this, NfcAdapter.FLAG_READER_NFC_A
				| NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK, null);
	}

	/**	
	 *  \brief
	 * 	Method called just before the activity goes on the background.
	 */
	@Override
	public void onPause() {
		super.onPause();
		nfcAdapter.disableReaderMode(this);
	}

	/**	
	 *  \brief
	 * 	Method called when the service detect a NFC card.
	 * 	\param tag Tag that the service has detected
	 */
	@Override
	public void onTagDiscovered(Tag tag) {
		IsoDep isoDep = IsoDep.get(tag);
		IsoDepTransceiver transceiver = new IsoDepTransceiver(isoDep, this);
		Thread thread = new Thread(transceiver);
		thread.start();
	}

	/**	
	 *  \brief
	 * 	Method called when we receive a message from the tag
	 * 	\param message Bytes array containing the message
	 */
	@Override
	public void onMessage(final byte[] message) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				isoDepAdapter.addMessage(new String(message));
			}
		});
	}

	/**	
	 *  \brief
	 * 	Method called when an error occured when chatting with the card.
	 * 	\param exception Exception
	 */
	@Override
	public void onError(Exception exception) {
		onMessage(exception.getMessage().getBytes());
	}
	
	/**	
	 *  \brief
	 * 	Method that fetch the AID (through API) and store it in the SharedPreferences.
	 */
	private void saveAID() {
		// AID = Application ID = unique 4 bytes identifier for each Android devices of an account
		// the AID is generated in the API
		
		String username;
		String password;
		String token = ReadWriteData.read(context, ReadWriteData.LOGIN_TOKEN);
		String result = null;
		String androidId = Secure.getString(MainActivity.getAppContext().getContentResolver(), Secure.ANDROID_ID);
		
		try {
			if (token == null) {
				username = ReadWriteData.read(context ,ReadWriteData.LOGIN_USERNAME);
				password = ReadWriteData.read(context, ReadWriteData.LOGIN_PASSWORD);
				result = new HTTPRequest().execute("/api/deviceid", username, password, "/" + androidId).get();
			} else {
				result = new HTTPRequest().execute("/api/deviceid", token, "", "/" + androidId).get();
			}
			ReadWriteData.write(context, ReadWriteData.NFC_AID, result);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			setConnected(false);
			Toast.makeText(MainActivity.getAppContext(), "An error occured, please try to connect again.", Toast.LENGTH_SHORT).show();
		}
	}
	
	/**	
	 *  \brief
	 * 	Method that returns the AID. If the user is connected, the unique AID is returned
	 *  but if not, the default AID is returned.
	 *  \return A byte array that contains the AID
	 */
	public static byte[] getAID() {
		String AID = ReadWriteData.read(context, ReadWriteData.NFC_AID);
		Toast.makeText(MainActivity.getAppContext(), "aid : " + AID, Toast.LENGTH_SHORT).show();

		if (ReadWriteData.getConnectionState(context) && AID != null) {
			String[] separatedAID = AID.split(" ");
			byte[] aid = new byte[separatedAID.length]; 
			
			for (int i = 0; i < separatedAID.length; i++) {
				aid[i] = (byte)(Integer.parseInt(separatedAID[i],16) & 0xff);
			}

			return aid;
		} else {
			return Constants.DEF_ANDROID_ID;
		}
	}
}
