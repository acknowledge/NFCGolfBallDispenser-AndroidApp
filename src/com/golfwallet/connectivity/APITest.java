package com.golfwallet.connectivity;

import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.golfwallet.R;
import com.golfwallet.main.MainActivity;

/** \brief Activity used to test the different APIs
 * 	\author	Jacky CASAS
 * 	\version 1.0
 * 	\date 04.06.2014  
 */
public class APITest extends Activity {
	
	/**	
	 *  \brief Method called when the activity is created.
	 *  \param savedInstanceState Bundle containing information from previous activity
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_apitest);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	/**	
	 *  \brief Method used to create the options menu
	 *  \param menu Menu
	 *  \return True when is it correctly created.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.apitest, menu);
		return true;
	}

	/**	
	 *  \brief Method called when an option is selected.
	 *  \param item Menu item selected
	 *  \return True if the action is correctly executed.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**	
	 *  \brief A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
	    
		public PlaceholderFragment() {
		}
		
		Button b1, b2, b3, b4, b5, b6, b7, b8, b9, b10;
		String result = new String();

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_apitest,
					container, false);
			
			b1 = (Button)rootView.findViewById(R.id.button1);
			b2 = (Button)rootView.findViewById(R.id.button2);
			b3 = (Button)rootView.findViewById(R.id.button3);
			b4 = (Button)rootView.findViewById(R.id.button4);
			b5 = (Button)rootView.findViewById(R.id.button5);
			b6 = (Button)rootView.findViewById(R.id.button6);
			b7 = (Button)rootView.findViewById(R.id.button7);
			b8 = (Button)rootView.findViewById(R.id.button8);
			b9 = (Button)rootView.findViewById(R.id.button9);
			b10 = (Button)rootView.findViewById(R.id.button10);
			
			b1.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					//Toast.makeText(MainActivity.getAppContext(), "Clicked", Toast.LENGTH_SHORT).show();
					try {
						result = new HTTPRequest().execute("/").get();
						Toast.makeText(MainActivity.getAppContext(), "asdf : " + result, Toast.LENGTH_SHORT).show();
					} catch (InterruptedException | ExecutionException e) {
						e.printStackTrace();
					}
				}
			});
			
			b2.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					try {
						result = new HTTPRequest().execute("/api/newpassword", "?username=jbonvin&password=jjjj").get();
						Toast.makeText(MainActivity.getAppContext(), "asdf : " + result, Toast.LENGTH_SHORT).show();
					} catch (InterruptedException | ExecutionException e) {
						e.printStackTrace();
					}
				}
			});
			
			b3.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					try {
						result = new HTTPRequest().execute("/api/users").get();
						Toast.makeText(MainActivity.getAppContext(), "asdf : " + result, Toast.LENGTH_SHORT).show();
					} catch (InterruptedException | ExecutionException e) {
						e.printStackTrace();
					}
				}
			});
			
			b4.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					try {
						result = new HTTPRequest().execute("/api/users/jbonvin").get();
						Toast.makeText(MainActivity.getAppContext(), "asdf : " + result, Toast.LENGTH_SHORT).show();
					} catch (InterruptedException | ExecutionException e) {
						e.printStackTrace();
					}
				}
			});
			
			b5.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					String username = "jbonvin"; // or old token
					String password = "jjjj";	// or empty if token
					try {
						result = new HTTPRequest().execute("/api/token", username, password).get();
						Toast.makeText(MainActivity.getAppContext(), "token : " + result, Toast.LENGTH_SHORT).show();
					} catch (InterruptedException | ExecutionException e) {
						e.printStackTrace();
					}
				}
			});
			
			b6.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					String username = "jbonvin"; // or old token
					String password = "jjjj";	// or empty if token
					try {
						result = new HTTPRequest().execute("/api/user", username, password).get();
						Toast.makeText(MainActivity.getAppContext(), "user : " + result, Toast.LENGTH_SHORT).show();
					} catch (InterruptedException | ExecutionException e) {
						e.printStackTrace();
					}
				}
			});
			
			b7.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					String username = "jbonvin";
					String password = "jjjj";
					try {
						result = new HTTPRequest().execute("/api/deviceid", username, password, "/" + Secure.getString(MainActivity.getAppContext().getContentResolver(), Secure.ANDROID_ID)).get();
						Toast.makeText(MainActivity.getAppContext(), "deviceID : " + result, Toast.LENGTH_SHORT).show();
					} catch (InterruptedException | ExecutionException e) {
						e.printStackTrace();
					}
				}
			});
			
			b8.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					String username = "jbonvin";
					String password = "jjjj";
					try {
						result = new HTTPRequest().execute("/api/balance", username, password).get();
						Toast.makeText(MainActivity.getAppContext(), "balance : " + result, Toast.LENGTH_SHORT).show();
					} catch (InterruptedException | ExecutionException e) {
						e.printStackTrace();
					}
				}
			});
			
			b9.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					String username = "jbonvin";
					String password = "jjjj";
					try {
						result = new HTTPRequest().execute("/api/devices", username, password).get();
						Toast.makeText(MainActivity.getAppContext(), "devices : " + result, Toast.LENGTH_SHORT).show();
					} catch (InterruptedException | ExecutionException e) {
						e.printStackTrace();
					}
				}
			});
			
			b10.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					String username = "jbonvin";
					String password = "jjjj";
					try {
						result = new HTTPRequest().execute("/api/transactions", username, password).get();
						Toast.makeText(MainActivity.getAppContext(), "transactions : " + result, Toast.LENGTH_SHORT).show();
					} catch (InterruptedException | ExecutionException e) {
						e.printStackTrace();
					}
				}
			});
			
			return rootView;
		}
	}
}
