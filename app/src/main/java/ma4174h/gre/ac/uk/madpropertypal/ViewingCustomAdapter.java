package ma4174h.gre.ac.uk.madpropertypal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ViewingCustomAdapter extends RecyclerView.Adapter<ViewingCustomAdapter.MyViewHolder> {

    Context context;
    //DatabaseHelper databaseHelper;
    ArrayList<String> viewingID, viewingDate, interest, offerPrice, offerExpiryDate, conditionsOfOffer, viewingComments;


    ViewingCustomAdapter(Context context, ArrayList<String> viewingID, ArrayList<String> viewingDate, ArrayList<String> interest, ArrayList<String> offerPrice, ArrayList<String> offerExpiryDate,
                                ArrayList<String> conditionsOfOffer, ArrayList<String> viewingComments) {
        //Data to show rows of properties and to enable editing
        this.context = context;
        this.viewingID = viewingID;
        this.viewingDate = viewingDate;
        this.interest = interest;
        this.offerPrice = offerPrice;
        this.offerExpiryDate = offerExpiryDate;
        this.conditionsOfOffer = conditionsOfOffer;
        this.viewingComments = viewingComments;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView viewingIdTextViewRow, viewingDateTextViewRow, interestTextViewRow, offerPriceTextViewRow, offerExpiryDateTextViewRow, conditionsOfOfferTextViewRow, viewingCommentsTextViewRow;
        private LinearLayout linearLayoutRow;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            viewingIdTextViewRow = itemView.findViewById(R.id.viewingIdTextViewRow);
            viewingDateTextViewRow = itemView.findViewById(R.id.viewingDateTextViewRow);
            interestTextViewRow = itemView.findViewById(R.id.interestTextViewRow);
            offerPriceTextViewRow = itemView.findViewById(R.id.offerPriceTextViewRow);
            offerExpiryDateTextViewRow = itemView.findViewById(R.id.offerExpiryDateTextViewRow);
            conditionsOfOfferTextViewRow = itemView.findViewById(R.id.conditionsOfOfferTextViewRow);
            viewingCommentsTextViewRow = itemView.findViewById(R.id.viewingCommentsTextViewRow);
            linearLayoutRow = itemView.findViewById(R.id.linearLayoutRowViewing);
        }
    }

    @NonNull
    @Override
    public ViewingCustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.viewing_row_layout,parent, false);
        return new MyViewHolder(view);
    }

    //Sets the text of the textViews when viewing the properties
    @Override
    public void onBindViewHolder(@NonNull ViewingCustomAdapter.MyViewHolder holder, final int position) {

        //These fields are used for the row layout
        holder.viewingIdTextViewRow.setText(String.valueOf(viewingID.get(position)));
        holder.viewingDateTextViewRow.setText("Viewing Date: " + String.valueOf(viewingDate.get(position)));
        holder.interestTextViewRow.setText("Interest: " + String.valueOf(interest.get(position)));

        //if the position of the array is null/empty then don't show anything otherwise
        // show in the textview (without verification it would show the text 'null')

        if (!String.valueOf(offerPrice.get(position)).equals("0")) {
            holder.offerPriceTextViewRow.setText("Offer Price: £" + String.valueOf(offerPrice.get(position)));
        } else {
            holder.offerPriceTextViewRow.setText("Offer Price:");
        }

        if (!String.valueOf(offerExpiryDate.get(position)).equals("null")) {
            holder.offerExpiryDateTextViewRow.setText("Offer Expiry: " + String.valueOf(offerExpiryDate.get(position)));
        } else {
            holder.offerExpiryDateTextViewRow.setText("Offer Expiry:");
        }
        if (!String.valueOf(conditionsOfOffer.get(position)).equals("null")) {
            holder.conditionsOfOfferTextViewRow.setText("Conditions of offer: " + String.valueOf(conditionsOfOffer.get(position)));
        } else {
            holder.conditionsOfOfferTextViewRow.setText("Conditions of offer:");
        }
        if (!String.valueOf(viewingComments.get(position)).equals("null")) {
            holder.viewingCommentsTextViewRow.setText("Comments: " + String.valueOf(viewingComments.get(position)));
        } else {
            holder.viewingCommentsTextViewRow.setText("Comments:");
        }

        //set onCLickListener when clicking row item
        holder.linearLayoutRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // When clicking the row in the ListOfViewings activity

            }
        });

    }

    @Override
    public int getItemCount() {
        return viewingID.size();
    }

}
