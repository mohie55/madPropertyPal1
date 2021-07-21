package ma4174h.gre.ac.uk.madpropertypal;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListOfViewings extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseHelper databaseHelper;
    ArrayList<String> viewingID, viewingDate, interest, offerPrice, offerExpiryDate, conditionsOfOffer, viewingComments;
    ViewingCustomAdapter viewingCustomAdapter;
    String propertyID;

    @Override
    protected void onCreate(Bundle savedInstanceStates) {
        super.onCreate(savedInstanceStates);
        initElements();
        populateArraysWithDB();
        //custom adapter formats the layout of the list of viewings
        viewingCustomAdapter = new ViewingCustomAdapter(ListOfViewings.this,viewingID, viewingDate, interest, offerPrice, offerExpiryDate, conditionsOfOffer, viewingComments);
        recyclerView = findViewById(R.id.recyclerViewViewings);
        recyclerView.setLayoutManager(new LinearLayoutManager(ListOfViewings.this));
        recyclerView.setAdapter(viewingCustomAdapter);
    }

    private void initElements() {
        setContentView(R.layout.list_of_viewings);
        propertyID = getIntent().getStringExtra("propertyID");
        databaseHelper = new DatabaseHelper(ListOfViewings.this);
        viewingID = new ArrayList<>();
        viewingDate = new ArrayList<>();
        interest = new ArrayList<>();
        offerPrice = new ArrayList<>();
        offerExpiryDate = new ArrayList<>();
        conditionsOfOffer = new ArrayList<>();
        viewingComments = new ArrayList<>();
    }

    public void populateArraysWithDB() {
        Cursor PropertyViewingTable = databaseHelper.getPropertyViewingTable(propertyID);
        if (PropertyViewingTable.getCount() == 0) {
            Toast.makeText(this, "No Data", Toast.LENGTH_LONG).show();
        } else {
            while (PropertyViewingTable.moveToNext()) {
                viewingID.add(PropertyViewingTable.getString(0));
                //Column 2 is property 2
                viewingDate.add(PropertyViewingTable.getString(2));
                interest.add(PropertyViewingTable.getString(3));
                offerPrice.add(PropertyViewingTable.getString(4));
                offerExpiryDate.add(PropertyViewingTable.getString(5));
                conditionsOfOffer.add(PropertyViewingTable.getString(6));
                viewingComments.add(PropertyViewingTable.getString(7));
            }
        }
    }
}
