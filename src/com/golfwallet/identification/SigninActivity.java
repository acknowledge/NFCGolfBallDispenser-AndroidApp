package com.golfwallet.identification;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.golfwallet.R;
import com.golfwallet.main.MainActivity;

/** \brief Activity which displays a registration screen to the user
 * 	\author	Jacky CASAS
 * 	\version 1.0
 * 	\date 04.06.2014  
 */
public class SigninActivity extends Activity {

	private UserSigninTask mAuthTask = null;	///< Keep track of the login task to ensure we can cancel it if requested.

	// Values for email and password at the time of the login attempt.
	private String mUsername;					///< Value of the username
	private String mName;						///< Value of the name
	private String mSurname;					///< Value of the surname
	private String mPassword;					///< Value of the password

	// UI references.
	private EditText mUsernameView;				///< Username EditText
	private EditText mNameView;					///< Name EditText
	private EditText mSurnameView;				///< Surname EditText
	private EditText mPasswordView;				///< Password EditText
	private View mLoginFormView;				///< Login form View
	private View mLoginStatusView;				///< Login status View
	private TextView mLoginStatusMessageView;	///< TextView for the status messages

	/**	
	 *  \brief Method called when the activity is created
	 *  \param savedInstanceState Bundle of the last activity
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_signin);

		// Set up the signin form.
		mUsername = getIntent().getStringExtra("username");
		mUsernameView = (EditText) findViewById(R.id.username);
		mUsernameView.setText(mUsername);
		
		mName = getIntent().getStringExtra("name");
		mNameView = (EditText) findViewById(R.id.name);
		mNameView.setText(mName);
		
		mSurname = getIntent().getStringExtra("surname");
		mSurnameView = (EditText) findViewById(R.id.surname);
		mSurnameView.setText(mSurname);

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});

		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();
					}
				});
	}

	/**	
	 *  \brief
	 * 	Method called to create the option menu on the top right of the screen
	 *  \param menu Menu containing the menu items
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.signin, menu);
		return true;
	}

	/**	
	 *  \brief Attempts to sign in or register the account specified by the signin form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mUsernameView.setError(null);
		mNameView.setError(null);
		mSurnameView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mUsername = mUsernameView.getText().toString();
		mName = mNameView.getText().toString();
		mSurname = mSurnameView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 4) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid username.
		if (TextUtils.isEmpty(mUsername)) {
			mUsernameView.setError(getString(R.string.error_field_required));
			focusView = mUsernameView;
			cancel = true;
		} 

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.progress_signing_in);
			showProgress(true);
			mAuthTask = new UserSigninTask();
			//mAuthTask.execute((Void) null);
			mAuthTask.execute(mUsername, mName, mSurname, mPassword);
		}
	}

	/**	
	 *  \brief Shows the progress UI and hides the login form.
	 *  \param show Boolean to display or not the progress
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**	
	 *  \brief Represents an asynchronous registration task used 
	 * to create an account.
	 */
	public class UserSigninTask extends AsyncTask<String, Void, Boolean> {
		@Override
		protected Boolean doInBackground(String... params) {

			String URL = "http://vlenfc.hevs.ch/api/newaccount";
			HttpResponse response;
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost postRequest = new HttpPost(URL);

	        try {
	        	String json = "";
	            JSONObject jsonObject = new JSONObject();
	            try {
					jsonObject.accumulate("username", params[0]);
					if(params[1] != null && !params[1].isEmpty()) {
						jsonObject.accumulate("name", params[1]);
					} 
					if(params[2] != null && !params[2].isEmpty()) {
						jsonObject.accumulate("surname", params[2]);
					} 
					jsonObject.accumulate("password", params[3]);
				} catch (JSONException e) {
					e.printStackTrace();
				}
	            
	            json = jsonObject.toString();
	            StringEntity se = new StringEntity(json);
	            postRequest.setEntity(se);
	 
	            postRequest.setHeader("Accept", "application/json");
	            postRequest.setHeader("Content-type", "application/json");
	        	
				response = httpclient.execute(postRequest);
				StatusLine statusLine = response.getStatusLine();
	            if (statusLine.getStatusCode() == HttpStatus.SC_CREATED ||
	            		statusLine.getStatusCode() == HttpStatus.SC_OK){
	                ByteArrayOutputStream out = new ByteArrayOutputStream();
	                response.getEntity().writeTo(out);
	                out.close();
	            } else{
	                //Closes the connection.
	                response.getEntity().getContent().close();
	                throw new IOException(statusLine.getReasonPhrase());
	            }
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
	        return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			showProgress(false);

			if (success) {
				
				Intent mainIntent = new Intent(SigninActivity.this, MainActivity.class);
				startActivity(mainIntent);
				
				Toast.makeText(SigninActivity.this, "Your account is successfully creates. You can now log in to use your Golf Wallet. Your account is credited with 10 CHF. Enjoy.", Toast.LENGTH_LONG).show();
				
				finish();
			} else {
				mUsernameView.setError(getString(R.string.error_username_already_taken));
				mUsernameView.requestFocus();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}
}
