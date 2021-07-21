package ma4174h.gre.ac.uk.madpropertypal;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;

public class PropertyCustomAdapterSearch extends RecyclerView.Adapter<PropertyCustomAdapterSearch.MyViewHolder> implements Filterable  {

    private Context context;
    private ArrayList<String> propertyID, propertyType, propertyName, city, noOfBedrooms, askingPrice, postcode, description,
            size, noOfBathrooms, dateString, furnishType, leaseType, propertyTypeAll, cityAll, noOfBedroomsAll;

    //This is used to avoid out of index errors due to the binding of out of index arraylist because it was filtered;
    private int smallestArrayListSize;

    private boolean[] localAmenitiesCheckedBool;
    DatabaseHelper DBHelper;


    PropertyCustomAdapterSearch(Context context, ArrayList<String> propertyID, ArrayList<String> propertyName, ArrayList<String> propertyType, ArrayList lease_type, ArrayList city, ArrayList postcode, ArrayList noOfBedrooms,
                          ArrayList noOfBathrooms, ArrayList size, ArrayList askingPrice, ArrayList description, ArrayList dateString, ArrayList furnish_type) {

        //Data to show rows of properties and to enable editing
        this.context = context;
        this.propertyID = propertyID;
        this.propertyName = propertyName;
        this.propertyType = propertyType;
        this.leaseType = lease_type;
        this.city = city;
        this.postcode = postcode;
        this.noOfBedrooms = noOfBedrooms;
        this.noOfBathrooms = noOfBathrooms;
        this.size = size;
        this.askingPrice = askingPrice;
        this.description = description;
        this.dateString = dateString;
        this.furnishType = furnish_type;

        //Creates a copy of the list of property names to hold all the property names as the original list will be changed when filtering/searching
        this.propertyTypeAll = new ArrayList<>(propertyType);
        this.noOfBedroomsAll = new ArrayList<>(noOfBedrooms);
        this.cityAll = new ArrayList<>(city);

       // smallestArrayListSize = propertyType.size();
    }

    public Filter getFilterCity() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            ArrayList<String> filteredList = new ArrayList<>();

            if (constraint.toString().isEmpty()) {
                filteredList.addAll(cityAll);
            } else {
                for (String city : cityAll) {
                    if (city.toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filteredList.add(city);
                    }
                }

            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            if (filteredList.size() < smallestArrayListSize) {
                smallestArrayListSize = filteredList.size();
            }
            return filterResults;
        }


        @Override
        protected void publishResults(CharSequence constraint, FilterResults filterResults) {
            city.clear();
            city.addAll((Collection<? extends String>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public Filter getFilterPropertyType() {
        return filter2;
    }

    Filter filter2 = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            ArrayList<String> filteredList = new ArrayList<>();

            if (constraint.toString().isEmpty()) {
                filteredList.addAll(propertyTypeAll);
            } else {
                for (String propertyType : propertyTypeAll) {
                    if (propertyType.toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filteredList.add(propertyType);
                    }
                }

            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            if (filteredList.size() < smallestArrayListSize) {
                smallestArrayListSize = filteredList.size();
            }
            return filterResults;
        }


        @Override
        protected void publishResults(CharSequence constraint, FilterResults filterResults) {
            propertyType.clear();
            propertyType.addAll((Collection<? extends String>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public Filter getFilterNoOfBedrooms() {
        return filter3;
    }

    Filter filter3 = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            ArrayList<String> filteredList = new ArrayList<>();

            if (constraint.toString().isEmpty()) {
                filteredList.addAll(noOfBedroomsAll);
            } else {
                for (String noOfBedrooms : noOfBedroomsAll) {
                    if (noOfBedrooms.toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filteredList.add(noOfBedrooms);
                    }
                }

            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            if (filteredList.size() < smallestArrayListSize) {
                smallestArrayListSize = filteredList.size();
            }

            return filterResults;
        }


        @Override
        protected void publishResults(CharSequence constraint, FilterResults filterResults) {
            noOfBedrooms.clear();
            noOfBedrooms.addAll((Collection<? extends String>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    @Override
    public Filter getFilter() {
        return null;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView menuMore;
        private TextView propertyIdTextView, propertyNameTextView, cityTextView, noOfBedroomsTextView, askingPriceTextView;
        private LinearLayout linearLayoutRow;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            propertyIdTextView = itemView.findViewById(R.id.propertyIdTextViewAdvancedSearch);
            propertyNameTextView = itemView.findViewById(R.id.propertyNameTextViewAdvancedSearch);
            cityTextView = itemView.findViewById(R.id.cityTextViewAdvancedSearch);
            noOfBedroomsTextView = itemView.findViewById(R.id.noOfBedroomsTextViewAdvancedSearch);
            askingPriceTextView = itemView.findViewById(R.id.askingPriceTextViewAdvancedSearch);
            linearLayoutRow = itemView.findViewById(R.id.linearLayoutRowAdvancedSearch);
        }
    }


    @NonNull
    @Override
    public PropertyCustomAdapterSearch.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.property_row_layout_search, parent, false);
        return new MyViewHolder(view);
    }


    //Sets the text of the textViews when viewing the properties
    @Override
    public void onBindViewHolder(@NonNull PropertyCustomAdapterSearch.MyViewHolder holder, final int position) {

        //These fields are used for the row layout

        holder.propertyIdTextView.setText(String.valueOf(propertyID.get(position)));
        holder.propertyNameTextView.setText(String.valueOf(propertyName.get(position)));
        holder.cityTextView.setText(String.valueOf(city.get(position)));
        holder.noOfBedroomsTextView.setText("no. of Bedrooms: " + String.valueOf(noOfBedrooms.get(position)));
        holder.askingPriceTextView.setText("£" + String.valueOf(askingPrice.get(position)));


        //set onCLickListener when clicking row item
        holder.linearLayoutRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // when clicking the row in the ListOfProperties activity
                Intent intent3 = new Intent(context, UpdateProperty.class);
                intent3.putExtra("PropertyId", String.valueOf(propertyID.get(position)));
                intent3.putExtra("propertyName", String.valueOf(propertyName.get(position)));
                intent3.putExtra("propertyType", String.valueOf(propertyType.get(position)));
                intent3.putExtra("leaseType", String.valueOf(leaseType.get(position)));
                intent3.putExtra("city", String.valueOf(city.get(position)));
                intent3.putExtra("postcode", String.valueOf(postcode.get(position)));
                intent3.putExtra("noOfBedrooms", String.valueOf(noOfBedrooms.get(position)));
                intent3.putExtra("noOfBathrooms", String.valueOf(noOfBathrooms.get(position)));
                intent3.putExtra("size", String.valueOf(size.get(position)));
                intent3.putExtra("askingPrice", String.valueOf(askingPrice.get(position)));

                DBHelper = new DatabaseHelper(context);
                Cursor cursor = DBHelper.getLocalAmenityTable(String.valueOf(propertyID.get(position)));
                localAmenitiesCheckedBool = new boolean[13];
                int i = 0;
                while (cursor.moveToNext()) {

                    //  localAmenitiesPropertyId.add(localAmenityTableCursor.getInt(0));
                    // localAmenitiesId.add(localAmenityTableCursor.getInt(1));
                    if (cursor.getInt(2) == 0) {
                        localAmenitiesCheckedBool[i] = false;
                    } else if (cursor.getInt(2) == 1) {
                        localAmenitiesCheckedBool[i] = true;
                    }
                    i++;
                }
                intent3.putExtra("localAmenitiesBool", localAmenitiesCheckedBool);
                intent3.putExtra("description", String.valueOf(description.get(position)));
                intent3.putExtra("dateString", String.valueOf(dateString.get(position)));
                intent3.putExtra("furnishType", String.valueOf(furnishType.get(position)));

                context.startActivity(intent3);
            }
        });
    }





    @Override
    public int getItemCount() {
        int count = 0;
        if (city.size() <= propertyType.size() && city.size() <= noOfBedrooms.size()) {
            count = city.size();
            return count;
        } else if (propertyType.size() <= noOfBedrooms.size()) {
            count = propertyType.size();
            return count;
        } else if (propertyType.size() >= noOfBedrooms.size()) {
            count = noOfBedrooms.size();
            return count;
        }
        return 0;
    }


}
