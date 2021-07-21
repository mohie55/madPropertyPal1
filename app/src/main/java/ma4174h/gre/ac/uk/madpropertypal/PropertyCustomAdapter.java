package ma4174h.gre.ac.uk.madpropertypal;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;

public class PropertyCustomAdapter extends RecyclerView.Adapter<PropertyCustomAdapter.MyViewHolder> implements Filterable  {

    private Context context;
    private ArrayList<String> propertyID, propertyType, propertyName, city, noOfBedrooms, askingPrice, postcode, description,
            size, noOfBathrooms, dateString, furnishType, leaseType, propertyNameAll;
    private TextView propertyIdTextView;
    //This is used to avoid out of index errors due to the binding of out of index arraylist because it was filtered;
    private int smallestArrayListSize;

    private boolean[] localAmenitiesCheckedBool;
    DatabaseHelper DBHelper;


    PropertyCustomAdapter(Context context, ArrayList<String> propertyID, ArrayList<String> propertyName, ArrayList<String> propertyType, ArrayList lease_type, ArrayList city, ArrayList postcode, ArrayList noOfBedrooms,
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
        this.propertyNameAll = new ArrayList<>(propertyName);

    }


        @Override
        public Filter getFilter() {
            return filter;
        }

        Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            ArrayList<String> filteredList = new ArrayList<>();

                if (constraint.toString().isEmpty()) {
                    filteredList.addAll(propertyNameAll);
                } else {
                    for (String propertyName : propertyNameAll) {
                        if (propertyName.toLowerCase().contains(constraint.toString().toLowerCase())) {
                            filteredList.add(propertyName);
                        }
                    }

                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                smallestArrayListSize = filteredList.size();
                return filterResults;
        }


        @Override
        protected void publishResults(CharSequence constraint, FilterResults filterResults) {
            propertyName.clear();
            propertyName.addAll((Collection<? extends String>) filterResults.values);
            notifyDataSetChanged();
        }
    };


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView menuMore;
        private TextView propertyIdTextView, propertyNameTextView, cityTextView, noOfBedroomsTextView, askingPriceTextView;
        private LinearLayout linearLayoutRow;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            propertyIdTextView = itemView.findViewById(R.id.propertyIdTextView);
            propertyNameTextView = itemView.findViewById(R.id.propertyNameTextView);
            cityTextView = itemView.findViewById(R.id.cityTextView);
            noOfBedroomsTextView = itemView.findViewById(R.id.noOfBedroomsTextView);
            askingPriceTextView = itemView.findViewById(R.id.askingPriceTextView);
            menuMore = itemView.findViewById(R.id.menuMore);
            linearLayoutRow = itemView.findViewById(R.id.linearLayoutRowListOfProperties);
        }
    }


    @NonNull
    @Override
    public PropertyCustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.property_row_layout, parent, false);
        return new MyViewHolder(view);
    }


    //Sets the text of the textViews when viewing the properties
    @Override
    public void onBindViewHolder(@NonNull PropertyCustomAdapter.MyViewHolder holder, final int position) {


        //These fields are used for the row layout


            holder.propertyNameTextView.setText(String.valueOf(propertyName.get(position)));
            holder.cityTextView.setText(String.valueOf(city.get(position)));
            holder.noOfBedroomsTextView.setText("no. of Bedrooms: " + String.valueOf(noOfBedrooms.get(position)));
            holder.askingPriceTextView.setText("£" + String.valueOf(askingPrice.get(position)));


            //set onCLickListener when clicking row item
            holder.linearLayoutRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // when clicking the row in the ListOfProperties activity
                    String id = propertyID.get(position);
                    Intent intent2 = new Intent(context, ListOfViewings.class);
                    intent2.putExtra("propertyID",id);
                    context.startActivity(intent2);
                }
            });

            DBHelper = new DatabaseHelper(context);

            holder.menuMore.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //Get ID to use when deleting a record

                    PopupMenu popupMenu = new PopupMenu(context, v);
                    popupMenu.getMenuInflater().inflate(R.menu.menu_bar, popupMenu.getMenu());
                    popupMenu.show();
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            String id = propertyID.get(position);
                            switch (item.getItemId()) {

                                case R.id.addViewingItem:

                                    //String id = propertyID.get(position);
                                    Intent intent = new Intent(context, AddViewing.class);
                                    intent.putExtra("propertyID", Integer.parseInt(id));
                                    context.startActivity(intent);
                                    break;

                                case R.id.viewViewings:
                                    //Go to list of viewings activity to show all viewings/offers for the selected property
                                    Intent intent2 = new Intent(context, ListOfViewings.class);
                                    intent2.putExtra("propertyID",id);
                                    context.startActivity(intent2);

                                    break;

                                case R.id.editProperty:
                                    //on click navigate to update/view property details activity and pass over details

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
                                    break;

                                case R.id.deleteItem:
                                    //delete from local amenity table too..
                                    int deletedId = DBHelper.deletePropertyTableItem((long) Integer.parseInt(id));

                                    notifyItemRemoved(Integer.parseInt(id));
                                    propertyID.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemChanged(position, getItemCount());
                                    notifyDataSetChanged();

                                    Toast.makeText(context, String.valueOf(id), Toast.LENGTH_LONG);
                                    break;

                            }
                            return true;
                        }
                    });
                }
            });

        }


    @Override
    public int getItemCount() {
        return propertyName.size();
    }


}
