package com.golfwallet.nfc;

import java.io.IOException;
import android.nfc.tech.IsoDep;

/** \brief Class IsoDepTransceiver.
 * 	\author	Jacky CASAS
 * 	\version 1.0
 * 	\date 04.06.2014  
 */
public class IsoDepTransceiver implements Runnable {

	/**	
	 *  \brief Interface to implement when we want to receive messages
	 */
	public interface OnMessageReceived {
		void onMessage(byte[] message);
		void onError(Exception exception);
	}

	private IsoDep isoDep;						///< IsoDep
	private OnMessageReceived onMessageReceived;///< Instance of the inner interface

	/**	
	 *  \brief Constructor
	 *  \param isoDep
	 *  \param onMessageReceived
	 */
	public IsoDepTransceiver(IsoDep isoDep, OnMessageReceived onMessageReceived) {
		this.isoDep = isoDep;
		this.onMessageReceived = onMessageReceived;
	}

	private static final byte[] CLA_INS_P1_P2 = { 
		0x00, (byte)0xA4, 0x04, 0x00 
		};										///< Header of the AID
	private static final byte[] AID_ANDROID = { 
		(byte)0xF0, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06 
		};										///< AID of the Android service
	
	/**	
	 *  \brief Compute the AID APDU
	 *  \param aid Byte array with the AID
	 *  \return The ready to send APDU
	 */
	private byte[] createSelectAidApdu(byte[] aid) {
		byte[] result = new byte[6 + aid.length];
		System.arraycopy(CLA_INS_P1_P2, 0, result, 0, CLA_INS_P1_P2.length);
		result[4] = (byte)aid.length;
		System.arraycopy(aid, 0, result, 5, aid.length);
		result[result.length - 1] = 0;
		return result;
	}

	/**	
	 *  \brief Loop of execution of the transceiver
	 */
	@Override
	public void run() {
		int messageCounter = 0;
		try {
			isoDep.connect();
			byte[] response = isoDep.transceive(createSelectAidApdu(AID_ANDROID));
			while (isoDep.isConnected() && !Thread.interrupted()) {
				String message = "Message from IsoDep " + messageCounter++;
				response = isoDep.transceive(message.getBytes());
				onMessageReceived.onMessage(response);
			}
			isoDep.close();
		}
		catch (IOException e) {
			onMessageReceived.onError(e);
		}
	}
}
