package com.golfwallet.identification;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
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
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.golfwallet.R;
import com.golfwallet.main.MainActivity;
import com.golfwallet.preference.ReadWriteData;

/** \brief Activity which displays a login screen to the user
 * 	\author	Jacky CASAS
 * 	\version 1.0
 * 	\date 04.06.2014  
 */
public class LoginActivity extends Activity {

	private UserLoginTask mAuthTask = null;		///< Keep track of the login task to ensure we can cancel it if requested.

	private String mEmail;						///< Value of the username
	private String mPassword;					///< Value of the password

	// UI references.
	private EditText mEmailView;				///< Username EditText
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

		setContentView(R.layout.activity_login);

		// Set up the login form.
		mEmail = getIntent().getStringExtra("username");
		mEmailView = (EditText) findViewById(R.id.email);
		mEmailView.setText(mEmail);

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView.setText(getIntent().getStringExtra("password"));
		mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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
	 *  \brief Attempts to log in to the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
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

		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.progress_logging_in);
			showProgress(true);
			mAuthTask = new UserLoginTask();
			mAuthTask.execute(mEmail, mPassword);
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
	 *  \brief Represents an asynchronous login task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<String, String, String> {
		String username;
		String password;

		@Override
		protected String doInBackground(String... params) {
			String URL = "http://vlenfc.hevs.ch";
			String responseString = null;
			HttpResponse response;
			HttpClient httpclient = new DefaultHttpClient();

			String credentials = params[0] + ":" + params[1];
			String base64EncodedCredentials = Base64.encodeToString(
					credentials.getBytes(), Base64.NO_WRAP);

			HttpGet getRequest = new HttpGet(URL + "/api/token");
			getRequest.addHeader("Authorization", "Basic "
					+ base64EncodedCredentials);

			username = params[0];
			password = params[1];

			try {
				response = httpclient.execute(getRequest);
				StatusLine statusLine = response.getStatusLine();
				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					responseString = out.toString();
				} else {
					// Closes the connection.
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			String token = null;
			if (responseString != null) {
				try {
					JSONObject jsonObj = new JSONObject(responseString);
					token = jsonObj.getString("token");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			return token;
		}

		@Override
		protected void onPostExecute(final String token) {
			mAuthTask = null;
			showProgress(false);

			if (token != null) {
				ReadWriteData.write(getApplicationContext(), ReadWriteData.LOGIN_USERNAME, username);
				ReadWriteData.write(getApplicationContext(), ReadWriteData.LOGIN_PASSWORD, password);
				ReadWriteData.write(getApplicationContext(), ReadWriteData.LOGIN_TOKEN, token);
				
				ReadWriteData.setConnected(getApplicationContext());

				Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
				startActivity(mainIntent);
				finish();
			} else {
				mPasswordView
						.setError(getString(R.string.error_incorrect_password));
				mPasswordView.requestFocus();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}
}
