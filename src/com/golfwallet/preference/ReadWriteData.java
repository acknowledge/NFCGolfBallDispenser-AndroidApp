package com.golfwallet.preference;

import android.content.Context;
import android.content.SharedPreferences;

/** \brief Store et retrieve data from the SharedPreferences
 * 	\author	Jacky CASAS
 * 	\version 1.0
 * 	\date 04.06.2014  
 */
public class ReadWriteData {

	public final static String LOGIN_USERNAME = "name";		///< Constant for the username
	public final static String LOGIN_PASSWORD = "password";	///< Constant for the password
	public final static String LOGIN_TOKEN = "token";		///< Constant for the token
	public final static String CONNECTED = "connected";		///< Constant for the connection
	public final static String NFC_AID = "aid";				///< Constant for the AID

	private static final String LOGIN_FILE_NAME = ReadWriteData.class
			.getSimpleName();								///< Constant for the file
	
	/**	
	 *  \brief Write a value in the SharedPreferences.
	 *  \param context Context of the app
	 *  \param tag String corresponding with a class constant for the type of data to save
	 *  \param data String containing the data to save
	 */
	public static void write(Context context, String tag, String data) {
		/* Open file and editor */
		SharedPreferences sharedPref = context.getSharedPreferences(
				LOGIN_FILE_NAME, 0);
		SharedPreferences.Editor editor = sharedPref.edit();

		/* Put string value with a tag in the file */
		editor.putString(tag, data);
		/* Close the editor */
		editor.commit();
	}

	/**	
	 *  \brief Read a value from the SharedPreferences
	 *  \param context Context of the app
	 *  \param tag Type of data
	 *  \return The data requested
	 */
	public static String read(Context context, String tag) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				LOGIN_FILE_NAME, 0);
		return sharedPref.getString(tag, null);
	}

	/**	
	 *  \brief Remove a data from the SharedPreferences
	 * 	\param context Context of the app
	 *  \param tag Type of data
	 */
	public static void remove(Context context, String tag) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				LOGIN_FILE_NAME, 0);
		SharedPreferences.Editor editor = sharedPref.edit();

		editor.remove(tag);
		editor.commit();
	}

	/**	
	 *  \brief Clear all the datas stored in the SharedPreferences
	 *  \param context Context of the app
	 */
	public static void clearAll(Context context) {
		SharedPreferences sharedPref = context.getSharedPreferences(
				LOGIN_FILE_NAME, 0);
		SharedPreferences.Editor editor = sharedPref.edit();

		editor.clear();
		editor.commit();
	}
	
	/**	
	 *  \brief Store in the SharedPreferences that the user is connected.
	 */
	public static void setConnected(Context context) {
		SharedPreferences sharedPref = context.getSharedPreferences(LOGIN_FILE_NAME, 0);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putBoolean(CONNECTED, true);
		editor.commit();
	}
	
	/**	
	 *  \brief Method used to know if the user is connected.
	 *  \param context Context of the app
	 *  \return True if connected and False if not
	 */
	public static boolean getConnectionState(Context context) {
		SharedPreferences sharedPref = context.getSharedPreferences(LOGIN_FILE_NAME, 0);
		return sharedPref.getBoolean(CONNECTED, false);
	}
	
	/**	
	 *  \brief Disconnect the user by erasing the connected date in the SharedPreferences
	 *  \param context Context of the app
	 */
	public static void disconnect(Context context) {
		SharedPreferences sharedPref = context.getSharedPreferences(LOGIN_FILE_NAME, 0);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.remove(CONNECTED);
		editor.commit();
	}
}
