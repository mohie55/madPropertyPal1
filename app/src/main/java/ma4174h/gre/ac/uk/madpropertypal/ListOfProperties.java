package ma4174h.gre.ac.uk.madpropertypal;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListOfProperties extends AppCompatActivity  {

    RecyclerView recyclerView;
    DatabaseHelper databaseHelper;
    ArrayList<String> propertyID, propertyType, propertyName, city, noOfBedrooms, askingPrice, postcode, description, size, noOfBathrooms, dateString, furnish_type, lease_type;
    PropertyCustomAdapter propertyCustomAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceStates) {
        super.onCreate(savedInstanceStates);
        initElements();
        populateArraysWithDB();
        //custom adapter formats the layout of the list of properties
        propertyCustomAdapter = new PropertyCustomAdapter(ListOfProperties.this, propertyID, propertyName, propertyType, lease_type, city, postcode, noOfBedrooms, noOfBathrooms, size, askingPrice,
                description, dateString, furnish_type);
        recyclerView = findViewById(R.id.recyclerViewProperties);
        recyclerView.setLayoutManager(new LinearLayoutManager(ListOfProperties.this));
        //add the adapter to the recycler view
        recyclerView.setAdapter(propertyCustomAdapter);

    }

    private void initElements() {
        setContentView(R.layout.list_of_properties);

        databaseHelper = new DatabaseHelper(ListOfProperties.this);
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
        getMenuInflater().inflate(R.menu.list_of_properties_menu_bar, menu);
        MenuItem deleteItem = menu.findItem(R.id.deleteDB);
        MenuItem searchItem = menu.findItem(R.id.search_action);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Takes the text input from the search field and calls the filter method
                propertyCustomAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteDB:

                new AlertDialog.Builder(this)
                        .setTitle("Delete Entire Database")
                        .setMessage("Are you sure you want to delete the entire database?")
                        .setIcon(android.R.drawable.ic_dialog_alert)

                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                databaseHelper.deleteDB();
                                finish();
                                startActivity(getIntent());
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();

            /*case R.id.advancedSearch:
                //Go to the advanced search activity
                Intent intent = new Intent(ListOfProperties.this, ListOfPropertiesSearch.class);
                startActivity(intent);*/
        }
        return super.onOptionsItemSelected(item);
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
        propertyCustomAdapter.notifyDataSetChanged();
    }

}
