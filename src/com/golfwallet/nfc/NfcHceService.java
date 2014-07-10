package com.golfwallet.nfc;

import com.golfwallet.main.MainActivity;
import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.util.Log;

/** \brief Class NfcHceService.
 * 	\author	Jacky CASAS
 * 	\version 1.0
 * 	\date 04.06.2014  
 */
public class NfcHceService extends HostApduService {
	
	private byte[] aid = new byte[4];	///< AID of the connected device
	private byte[] success = new byte[] {
			(byte) 0x90, 0x00
			};							///< Success 2 bytes to put at the end of the response

	/**	
	 *  \brief Process a command : receive and treat the request and compute the response
	 *  \param apdu Byte array containing the request
	 *  \param extras Bundle containing information from the activity
	 *  \return A byte array containing the response
	 */
	@Override
	public byte[] processCommandApdu(byte[] apdu, Bundle extras) {
		if (selectAidApdu(apdu)) {
			Log.i("HCEDEMO", "Application selected");
			aid = MainActivity.getAID();
			return concat(aid, success);
		}
		
		aid = MainActivity.getAID();
		return concat(aid, success);
	}

	/**	
	 *  \brief Compare the first two byte to know if it is the expected message
	 *  \return True if APDU begins with 0x00 0xA4 and is longer than 2 bytes
	 */
	private boolean selectAidApdu(byte[] apdu) {
		return apdu.length >= 2 && apdu[0] == (byte)0 && apdu[1] == (byte)0xa4;
	}

	/**	
	 *  \brief Called when the service is deactivated
	 *  \param reason The reason why it is deactivated
	 */
	@Override
	public void onDeactivated(int reason) {
		Log.i("HCEDEMO", "Deactivated: " + reason);
	}
	
	/**	
	 *  \brief Concatenate two byte arrays.
	 *  \return The concatenate array
	 */
	public byte[] concat(byte[] A, byte[] B) {
	    int aLen = A.length;
	    int bLen = B.length;
	    byte[] C= new byte[aLen+bLen];
	    System.arraycopy(A, 0, C, 0, aLen);
	    System.arraycopy(B, 0, C, aLen, bLen);
	    return C;
	}
}
