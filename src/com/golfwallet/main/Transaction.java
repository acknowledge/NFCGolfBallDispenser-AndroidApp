package com.golfwallet.main;

/** \brief Contains all the attributes of a transaction and make available different getters.
 * 	\author	Jacky CASAS
 * 	\version 1.0
 * 	\date 04.06.2014  
 */
public class Transaction {
	
	// transaction type
	public static final int RECHARGE = 0;	///< Constant for an account recharge
	public static final int WITHDRAWAL = 1; ///< Constant for an account withdrawal
		
	private int amount;						///< Amount of the transaction
	private int type;						///< Type of transaction : recharge or withdrawal
	private String transactionDate;			///< Date of the transaction
	private String deviceId;				///< Id of the device uses to make the transaction
	private String dispenserId;        		///< Id of the NFC reader/ball dispenser

	/**	
	 *  \brief
	 * 	Constructor
	 *  \param amount Amount of the transaction
	 *  \param type Type of transaction
	 *  \param transactionDate Date
	 *  \param deviceId Id of the device
	 *  \param dispenserId Id of the dispenser
	 */
	public Transaction(int amount, int type, String transactionDate, String deviceId, String dispenserId){
		this.amount = amount;
		this.type = type;
		this.transactionDate = transactionDate;
		this.deviceId = deviceId;
		this.dispenserId = dispenserId;
	}

	/**	
	 *  \brief Getter for the amount.
	 *  \return The amount
	 */
	public int getAmount() {
		return this.amount;
	}
	
	/**	
	 *  \brief Getter for the type.
	 *  \return The type of transaction
	 */
	public int getType() {
		return this.type;
	}
	
	/**	
	 *  \brief Getter for the date.
	 *  \return A formatted date
	 */
	public String getDate() {
		return this.transactionDate.replace(" GMT","");
	}
	
	/**	
	 *  \brief Getter for the device ID.
	 *  \return The device ID
	 */
	public String getDeviceId() {
		return this.deviceId;
	}
	
	/**	
	 *  \brief Getter for the dispenser ID.
	 *  \return The dispenser ID
	 */
	public String getDispenserId() {
		return this.dispenserId;
	}	
	
	/**	
	 *  \brief Getter for the amount.
	 *  \return A signed amount depending on the type of transaction
	 */
	public String getSignedAmount() {
		if (this.type == Transaction.RECHARGE) {
			return "+" + this.amount + " CHF";
		} else {
			return "-" + this.amount + " CHF";
		}
	}
}
