package com.golfwallet.connectivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Base64;

/** \brief Asynchronous task used to call the different APIs
 * 	\author	Jacky CASAS
 * 	\version 1.0
 * 	\date 04.06.2014  
 */
public class HTTPRequest extends AsyncTask<String, String, String> {

	/**	
	 *  \brief Method which called the APIs.
	 *  \param uri Array of strings
	 *  \return A string corresponding to the answer.
	 */
	@Override
	protected String doInBackground(String... uri) {
		
		String URL = "http://vlenfc.hevs.ch";
		String responseString = null;
		HttpResponse response;
		HttpClient httpclient = new DefaultHttpClient();
		
		HttpGet getRequest;
		HttpPost postRequest;
		
		String credentials;
		String base64EncodedCredentials;
		
		switch(uri[0]) {
		case "/":
	        try {
	        	getRequest = new HttpGet(URL);
	            response = httpclient.execute(getRequest);
	            StatusLine statusLine = response.getStatusLine();
	            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
	                ByteArrayOutputStream out = new ByteArrayOutputStream();
	                response.getEntity().writeTo(out);
	                out.close();
	                responseString = out.toString();
	            } else{
	                //Closes the connection.
	                response.getEntity().getContent().close();
	                throw new IOException(statusLine.getReasonPhrase());
	            }
	        } catch (ClientProtocolException e) {
	        } catch (IOException e) {
	        }
			break;
		case "/api/newpassword":
			try {
				getRequest = new HttpGet(URL + uri[0] + uri[1]);
	            response = httpclient.execute(getRequest);
	            StatusLine statusLine = response.getStatusLine();
	            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
	                ByteArrayOutputStream out = new ByteArrayOutputStream();
	                response.getEntity().writeTo(out);
	                out.close();
	                responseString = out.toString();
	            } else{
	                //Closes the connection.
	                response.getEntity().getContent().close();
	                throw new IOException(statusLine.getReasonPhrase());
	            }
	        } catch (ClientProtocolException e) {
	        } catch (IOException e) {
	        }
			break;
		case "/api/users":
			
	        postRequest = new HttpPost(URL + uri[0]);

	        try {
	        	String json = "";
	            JSONObject jsonObject = new JSONObject();
	            try {
					jsonObject.accumulate("username", "jbonvin");
					jsonObject.accumulate("password", "jjjj");
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
	            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
	                ByteArrayOutputStream out = new ByteArrayOutputStream();
	                response.getEntity().writeTo(out);
	                out.close();
	                responseString = out.toString();
	            } else{
	                //Closes the connection.
	                response.getEntity().getContent().close();
	                throw new IOException(statusLine.getReasonPhrase());
	            }
			} catch (IOException e) {
				e.printStackTrace();
			}
	        break;
		case "/api/users/jbonvin":
			getRequest = new HttpGet(URL + uri[0]);
			try {
	            response = httpclient.execute(getRequest);
	            StatusLine statusLine = response.getStatusLine();
	            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
	                ByteArrayOutputStream out = new ByteArrayOutputStream();
	                response.getEntity().writeTo(out);
	                out.close();
	                responseString = out.toString();
	            } else{
	                //Closes the connection.
	                response.getEntity().getContent().close();
	                throw new IOException(statusLine.getReasonPhrase());
	            }
	        } catch (ClientProtocolException e) {
	        } catch (IOException e) {
	        }
			break;
		case "/api/token":
			// uri[1] : username or token
			// uri[2] : password or empty
			getRequest = new HttpGet(URL + uri[0]);
			credentials = uri[1] + ":" + uri[2];  
			base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);  
			getRequest.addHeader("Authorization", "Basic " + base64EncodedCredentials);
 
			try {
				response = httpclient.execute(getRequest);
				StatusLine statusLine = response.getStatusLine();
	            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
	                ByteArrayOutputStream out = new ByteArrayOutputStream();
	                response.getEntity().writeTo(out);
	                out.close();
	                responseString = out.toString();
	                
	                // get token from JSON object
    				try {
    					JSONObject jsonObj = new JSONObject(responseString);
    					responseString = jsonObj.getString("token");
    				} catch (JSONException e) {
    					e.printStackTrace();
    				}
	    			
	            } else{
	                //Closes the connection.
	                response.getEntity().getContent().close();
	                throw new IOException(statusLine.getReasonPhrase());
	            }
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}  
			break;
		case "/api/user":
			// uri[1] : username or token
			// uri[2] : password or empty
			getRequest = new HttpGet(URL + uri[0]);
			credentials = uri[1] + ":" + uri[2];  
			base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);  
			getRequest.addHeader("Authorization", "Basic " + base64EncodedCredentials);
 
			try {
				response = httpclient.execute(getRequest);
				StatusLine statusLine = response.getStatusLine();
	            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
	                ByteArrayOutputStream out = new ByteArrayOutputStream();
	                response.getEntity().writeTo(out);
	                out.close();
	                responseString = out.toString();
	            } else{
	                //Closes the connection.
	                response.getEntity().getContent().close();
	                throw new IOException(statusLine.getReasonPhrase());
	            }
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}  
			break;
		case "/api/deviceid":
			// uri[1] : username or token
			// uri[2] : password or empty
			// uri[3] : android_id
			getRequest = new HttpGet(URL + uri[0] + uri[3]);
			credentials = uri[1] + ":" + uri[2];  
			base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);  
			getRequest.addHeader("Authorization", "Basic " + base64EncodedCredentials);
 
			try {
				response = httpclient.execute(getRequest);
				StatusLine statusLine = response.getStatusLine();
	            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
	                ByteArrayOutputStream out = new ByteArrayOutputStream();
	                response.getEntity().writeTo(out);
	                out.close();
	                responseString = out.toString();
	                
	                // get AID from JSON object
    				try {
    					JSONObject jsonObj = new JSONObject(responseString);
    					responseString = jsonObj.getString("uid");
    				} catch (JSONException e) {
    					e.printStackTrace();
    				}
	            } else{
	                //Closes the connection.
	                response.getEntity().getContent().close();
	                throw new IOException(statusLine.getReasonPhrase());
	            }
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}  
			break;
		case "/api/balance":
			// uri[1] : username or token
			// uri[2] : password or empty
			getRequest = new HttpGet(URL + uri[0]);
			credentials = uri[1] + ":" + uri[2];  
			base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);  
			getRequest.addHeader("Authorization", "Basic " + base64EncodedCredentials);
 
			try {
				response = httpclient.execute(getRequest);
				StatusLine statusLine = response.getStatusLine();
	            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
	                ByteArrayOutputStream out = new ByteArrayOutputStream();
	                response.getEntity().writeTo(out);
	                out.close();
	                responseString = out.toString();
	                
	                // get balance from JSON object
    				try {
    					JSONObject jsonObj = new JSONObject(responseString);
    					responseString = jsonObj.getString("balance");
    				} catch (JSONException e) {
    					e.printStackTrace();
    				}
	            } else{
	                //Closes the connection.
	                response.getEntity().getContent().close();
	                throw new IOException(statusLine.getReasonPhrase());
	            }
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}  
			break;
		case "/api/devices":
			// uri[1] : username or token
			// uri[2] : password or empty
			getRequest = new HttpGet(URL + uri[0]);
			credentials = uri[1] + ":" + uri[2];  
			base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);  
			getRequest.addHeader("Authorization", "Basic " + base64EncodedCredentials);
 
			try {
				response = httpclient.execute(getRequest);
				StatusLine statusLine = response.getStatusLine();
	            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
	                ByteArrayOutputStream out = new ByteArrayOutputStream();
	                response.getEntity().writeTo(out);
	                out.close();
	                responseString = out.toString();
	            } else{
	                //Closes the connection.
	                response.getEntity().getContent().close();
	                throw new IOException(statusLine.getReasonPhrase());
	            }
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}  
			break;
		case "/api/transactions":
			// uri[1] : username or token
			// uri[2] : password or empty
			getRequest = new HttpGet(URL + uri[0]);
			credentials = uri[1] + ":" + uri[2];  
			base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);  
			getRequest.addHeader("Authorization", "Basic " + base64EncodedCredentials);
 
			try {
				response = httpclient.execute(getRequest);
				StatusLine statusLine = response.getStatusLine();
	            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
	                ByteArrayOutputStream out = new ByteArrayOutputStream();
	                response.getEntity().writeTo(out);
	                out.close();
	                responseString = out.toString();
	            } else{
	                //Closes the connection.
	                response.getEntity().getContent().close();
	                throw new IOException(statusLine.getReasonPhrase());
	            }
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}  
			break;
		}        
        return responseString;
	}
	
	/**	
	 *  \brief Method called when the asynchronous doInBackground() method is over.
	 *  \param result String resulting of the asynchronous request
	 */
	@Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }
}
