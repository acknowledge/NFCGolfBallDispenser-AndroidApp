package com.golfwallet.main;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.golfwallet.R;

/** \brief Permits to format a line with a transaction information in an array
 * 	\author	Jacky CASAS
 * 	\version 1.0
 * 	\date 04.06.2014  
 */
public class TransactionAdapter extends ArrayAdapter<Transaction> {
	
	/** \brief
	 * Store the two textviews of a transaction row. The ViewHolder caches the textviews.
	 */
	static class ViewHolderItem {
	    TextView dateTextView;
	    TextView amountTextView;
	}

	/**	
	 *  \brief Constructor
	 *  \param context Context of the activity
	 *  \param resource ID of a layout describing a transaction row
	 *  \param items List of all the transactions we want to display
	 */
    public TransactionAdapter(Context context, int resource, List<Transaction> items) {
        super(context, resource, items);
    }

    /**	
	 *  \brief ethod called when an element must be displayed
	 *  \param position Position of the element to display 
	 *  \param convertView View where to display the elements
	 *  \param parent Parent of the view
	 *  \return The filled view
	 */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        
        ViewHolderItem viewHolder;
        
        if (convertView == null) {
        	LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
        	convertView = inflater.inflate(R.layout.transaction_row, parent, false);
        	
        	viewHolder = new ViewHolderItem();
        	viewHolder.dateTextView = (TextView) convertView.findViewById(R.id.date);
        	viewHolder.amountTextView = (TextView) convertView.findViewById(R.id.amount);
        	
        	// store the holder with the view.
        	convertView.setTag(viewHolder);
        } else {
        	viewHolder = (ViewHolderItem) convertView.getTag();
        }
        
        Transaction p = getItem(position);
        
        if (p != null) {
        	viewHolder.dateTextView.setText("" + p.getDate());
        	viewHolder.amountTextView.setText("" + p.getSignedAmount());
        }
        
        return convertView;
    }
}
