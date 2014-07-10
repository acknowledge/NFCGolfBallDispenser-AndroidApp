package com.golfwallet.nfc;

import java.util.ArrayList;
import java.util.List;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/** \brief Class IsoDepAdapter.
 * 	\author	Jacky CASAS
 * 	\version 1.0
 * 	\date 04.06.2014  
 */
public class IsoDepAdapter extends BaseAdapter {

	private LayoutInflater layoutInflater;			///< The layout inflater
	private List<String> messages = new ArrayList<String>(100);	///< A queue of messages
	private int messageCounter;						///< Counter for the message in message list

	/**	
	 *  \brief Constructor
	 *  \param layoutInfater To inflate the adapter
	 */
	public IsoDepAdapter(LayoutInflater layoutInflater) {
		this.layoutInflater = layoutInflater;
	}

	/**	
	 *  \brief Used to add a message in the message queue
	 *  \param message String containing the message
	 */
	public void addMessage(String message) {
		messageCounter++;
		messages.add("Message [" + messageCounter + "]: " + message);
		notifyDataSetChanged();
	}

	/**	
	 *  \brief Count the number of messages in the list
	 *  \return The number of messages
	 */
	@Override
	public int getCount() {
		return messages == null ? 0 : messages.size();
	}

	/**	
	 *  \brief Give a message from a position
	 *  \param position Position of the message in the list
	 *  \return The message at the requested position
	 */
	@Override
	public Object getItem(int position) {
		return messages.get(position);
	}

	/**	
	 *  \brief Give the ID of a message
	 *  \param position Position of the message
	 *  \return The ID of the message
	 */
	@Override
	public long getItemId(int position) {
		return 0;
	}

	/**	
	 *  \brief Getter for a specific view
	 *  \param position Position of the view
	 *  \param convertView Actual view
	 *  \param parent Parent of the view
	 *  \return The right view
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
		}
		TextView view = (TextView)convertView.findViewById(android.R.id.text1);
		view.setText((CharSequence)getItem(position));
		return convertView;
	}
}