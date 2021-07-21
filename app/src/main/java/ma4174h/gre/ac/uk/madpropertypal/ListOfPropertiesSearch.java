package ma4174h.gre.ac.uk.madpropertypal;

import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListOfPropertiesSearch extends AppCompatActivity implements AdvancedSearchDialog.AdvancedSearchDialogListener {

    RecyclerView recyclerView;
    DatabaseHelper databaseHelper;
    ArrayList<String> propertyID, propertyType, propertyName, city, noOfBedrooms, askingPrice, postcode, description, size, noOfBathrooms, dateString, furnish_type, lease_type;
    String cityFilter, propertyTypeFilter, noOfBedroomsFilter;
    PropertyCustomAdapterSearch propertyCustomAdapterSearch;


    @Override
    protected void onCreate(Bundle savedInstanceStates) {
        super.onCreate(savedInstanceStates);
        initElements();
        populateArraysWithDB();
        //custom adapter formats the layout of the list of properties
        propertyCustomAdapterSearch = new PropertyCustomAdapterSearch(ListOfPropertiesSearch.this, propertyID, propertyName, propertyType, lease_type, city, postcode, noOfBedrooms, noOfBathrooms, size, askingPrice,
                description, dateString, furnish_type);
        recyclerView = findViewById(R.id.recyclerViewProperties);
        recyclerView.setLayoutManager(new LinearLayoutManager(ListOfPropertiesSearch.this));
        //add the adapter to the recycler view
        recyclerView.setAdapter(propertyCustomAdapterSearch);

    }

    private void initElements() {
        setContentView(R.layout.list_of_properties);

        databaseHelper = new DatabaseHelper(ListOfPropertiesSearch.this);
        propertyID = new ArrayList<>();
        propertyName = new ArrayList<>();
        propertyType = new ArrayList<>();
        lease_type = new ArrayList<>();
        city = new ArrayList<>();
        postcode = new ArrayList<>();
        noOfBedrooms = new ArrayList<>();
        noOfBathrooms = new ArrayList<>();
        size = new ArrayList<>();
        askingPrice = new ArrayList<>();
        description = new ArrayList<>();
        dateString = new ArrayList<>();
        furnish_type = new ArrayList<>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_of_properties_menu_bar_search, menu);
        MenuItem advancedSearchItem = menu.findItem(R.id.advancedSearch);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.advancedSearch) {
            openSearchDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    public void openSearchDialog() {
        AdvancedSearchDialog advancedSearchDialog = new AdvancedSearchDialog();
        advancedSearchDialog.show(getSupportFragmentManager(), "Advanced Search");
    }

    public void populateArraysWithDB() {
        Cursor PropertyTableCursor = databaseHelper.getPropertyTable();

        if (PropertyTableCursor.getCount() == 0) {
            Toast.makeText(this, "No Data", Toast.LENGTH_LONG).show();
        } else {
            while (PropertyTableCursor.moveToNext()) {
                propertyID.add(PropertyTableCursor.getString(0));
                propertyName.add(PropertyTableCursor.getString(1));
                propertyType.add(PropertyTableCursor.getString(2));
                lease_type.add(PropertyTableCursor.getString(3));
                city.add(PropertyTableCursor.getString(4));
                postcode.add(PropertyTableCursor.getString(5));
                noOfBedrooms.add(PropertyTableCursor.getString(6));
                noOfBathrooms.add(PropertyTableCursor.getString(7));
                size.add(PropertyTableCursor.getString(8));
                askingPrice.add(PropertyTableCursor.getString(9));
                description.add(PropertyTableCursor.getString(10));
                dateString.add(PropertyTableCursor.getString(11));
                furnish_type.add(PropertyTableCursor.getString(12));

            }

        }
    }

    public void deletePropertyTableItem(long id) {
        databaseHelper.deletePropertyTableItem(id);
        propertyCustomAdapterSearch.notifyDataSetChanged();
    }


    @Override
    public void applyTexts(String city, String propertyType, String noOfBedrooms) {
        cityFilter = city;
        propertyTypeFilter = propertyType;
        noOfBedroomsFilter = noOfBedrooms;
        if (!city.isEmpty()) {
            propertyCustomAdapterSearch.getFilterCity().filter(city);
        } else {
            propertyCustomAdapterSearch.getFilterCity().filter("");
        }
        if (!propertyType.equals("Select Property Type")) {
            propertyCustomAdapterSearch.getFilterPropertyType().filter(propertyType);
        } else {
            propertyCustomAdapterSearch.getFilterPropertyType().filter("");
        }
        if (!noOfBedrooms.equals("Select No. of Bedrooms")) {
            propertyCustomAdapterSearch.getFilterNoOfBedrooms().filter(noOfBedrooms);
        } else {
            propertyCustomAdapterSearch.getFilterNoOfBedrooms().filter("");
        }

    }
}
