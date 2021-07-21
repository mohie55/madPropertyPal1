package ma4174h.gre.ac.uk.madpropertypal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button viewPropertiesButton, advancedSearchButton, addPropertyButton, uploadPropertyDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addPropertyButton = (Button)findViewById(R.id.addpropertyButton);
        viewPropertiesButton = (Button)findViewById(R.id.viewPropertiesButton);
        advancedSearchButton = (Button)findViewById(R.id.advancedSearchButton);
        uploadPropertyDetails = (Button)findViewById(R.id.uploadPropertyDetails);

        addPropertyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            Intent intent = new Intent(MainActivity.this, AddProperty.class);
            startActivity(intent);

            }
        });

        viewPropertiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListOfProperties.class);
                startActivity(intent);
            }
        });

        advancedSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListOfPropertiesSearch.class);
                startActivity(intent);
            }
        });

        uploadPropertyDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UploadPropertyJson.class);
                startActivity(intent);
            }
        });


    }




}
