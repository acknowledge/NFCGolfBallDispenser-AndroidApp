package com.golfwallet.main;

/** \brief Class that contains constants accessible in a static way.
 * 	\author	Jacky CASAS
 * 	\version 1.0
 * 	\date 04.06.2014  
 */
public class Constants {

	// default android ID
	public static final byte[] DEF_ANDROID_ID = new byte[] { 
		(byte)0xAA, (byte) 0xAA, (byte)0xAA, (byte) 0xAA 
	};													///< Default Android ID

	// user statement
	public static final int STA_USER_ACTIVE = 1;		///< Active user statement
	public static final int STA_USER_INACTIVE = 2;		///< Inactive user statement
	public static final int STA_USER_DELETED = 3;		///< Deleted user statement

	// device status
	public static final int STA_DEVICE_ACTIVE = 1;		///< Active device status
	public static final int STA_DEVICE_LOST = 2;		///< Lost device status
	public static final int STA_DEVICE_STOLEN = 3;		///< Stolen device status
	public static final int STA_DEVICE_DELETED = 4;		///< Deleted device status
}
