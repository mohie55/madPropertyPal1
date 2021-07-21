package ma4174h.gre.ac.uk.madpropertypal;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.basgeekball.awesomevalidation.AwesomeValidation;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static com.basgeekball.awesomevalidation.ValidationStyle.TEXT_INPUT_LAYOUT;

public class AddProperty extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    EditText propertyNameEditText, cityEditText, postcodeEditText, askingPriceEditText;
    String propertyName, propertyType, leaseType, city, postcode, noOfBedrooms, noOfBathrooms, size, askingPrice, chosenDateString;
    RadioGroup leaseTypeRadioGroup;
    RadioButton leaseTypeCheckedRadioButton;
    Spinner propertyTypeSpinner, noOfBedroomsSpinner, noOfBathroomsSpinner;
    TextView sizeTextView, amenitiesTextView;
    Button amenitiesButton, submitButton;
    String[] amenitiesList;
    boolean[] localAmenitiesCheckedBool;
    ArrayList<Integer> selectedAmenities = new ArrayList<>();
    SeekBar sizeSeekBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_property);

        //Adding a back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // populating propertyTypeSpinner
        propertyTypeSpinner = (Spinner) findViewById(R.id.propertyTypeSpinner);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(AddProperty.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.propertyType));
        propertyTypeSpinner.setAdapter(myAdapter);


        //Limit spinner popup height
        try {
            //Create a popup
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(propertyTypeSpinner);

            // Set popupWindow height to 500px
            popupWindow.setHeight(500);
        }
        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
            e.printStackTrace();
        }

        // populating bedroomsSpinner
        noOfBedroomsSpinner = (Spinner) findViewById(R.id.noOfBedroomsSpinner);
        ArrayAdapter<String> myAdapter3 = new ArrayAdapter<String>(AddProperty.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.noOfBedrooms));
        noOfBedroomsSpinner.setAdapter(myAdapter3);

        // populating bathroomsSpinner
        noOfBathroomsSpinner = (Spinner) findViewById(R.id.noOfBathroomsSpinner);
        ArrayAdapter<String> myAdapter4 = new ArrayAdapter<String>(AddProperty.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.noOfBathrooms));
        noOfBathroomsSpinner.setAdapter(myAdapter4);
        //Size seekBar
        sizeSeekBar = findViewById(R.id.sizeSeekBar);
        sizeSeekBar.setProgress(100);
        sizeTextView = findViewById(R.id.sizeTextView);
        sizeTextView.setText("Size: 100m2");
        sizeSeekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);

        amenitiesButton = (Button) findViewById(R.id.amenitiesButton);
        amenitiesTextView = (TextView) findViewById(R.id.amenitiesTextView);

        amenitiesList = getResources().getStringArray(R.array.amenitiesList);
        localAmenitiesCheckedBool = new boolean[amenitiesList.length];

        //Amenities button on click listener opens a dialog with checkbox list of amenities
        amenitiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddProperty.this);
                builder.setTitle(R.string.dialogTitle);
                builder.setMultiChoiceItems(amenitiesList, localAmenitiesCheckedBool, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                        if (isChecked) {
                            if (!selectedAmenities.contains(position)) {
                                selectedAmenities.add(position);
                            }
                        } else {
                            if (selectedAmenities.contains(position)) {
                                selectedAmenities.remove((Integer) position);
                            }
                        }
                    }
                });
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.okLabel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String amenitiesString = "";
                        for (int i = 0; i < selectedAmenities.size(); i++) {
                            amenitiesString += amenitiesList[selectedAmenities.get(i)];
                            if (i != selectedAmenities.size() -1 ) {
                                amenitiesString += ", ";
                            } else {
                                amenitiesString +=".";
                            }
                        }
                        amenitiesTextView.setText(amenitiesString);
                    }
                });

                builder.setNegativeButton(R.string.dismissLabel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setNeutralButton(R.string.clearAllLabel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < localAmenitiesCheckedBool.length; i++) {
                            localAmenitiesCheckedBool[i] = false;
                            selectedAmenities.clear();
                            amenitiesTextView.setText("");
                        }
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        Button dateButton = (Button) findViewById(R.id.dateButton);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "Date Picker");

            }
        });


        //Declaring validation method and style
        final AwesomeValidation awesomeValidation = new AwesomeValidation(TEXT_INPUT_LAYOUT);

        //Adding validations for user input views
        awesomeValidation.addValidation(AddProperty.this, R.id.propertyNameTIL, "(?i)^[a-z0-9]+(?:[ -]?[a-z0-9]+)*$", R.string.errPropertyName);
        awesomeValidation.addValidation(AddProperty.this, R.id.cityTIL, "^[a-zA-Z-,]+(\\s{0,1}[a-zA-Z-, ])*$", R.string.errCity);
        awesomeValidation.addValidation(AddProperty.this, R.id.postcodeTIL, "([Gg][Ii][Rr] 0[Aa]{2})|((([A-Za-z][0-9]{1,2})|(([A-Za-z][A-Ha-hJ-Yj-y][0-9]{1,2})|(([A-Za-z][0-9][A-Za-z])|([A-Za-z][A-Ha-hJ-Yj-y][0-9][A-Za-z]?))))\\s?[0-9][A-Za-z]{2})", R.string.errPostcode);
        awesomeValidation.addValidation(AddProperty.this, R.id.askingPriceTIL, "^[0-9]+$", R.string.errAskingPrice);

        //Submit button init and onClickListener
        submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (awesomeValidation.validate() && checkSpinners()) {

                    //display user input and save if confirmed
                    getUserInput();

                }else {
                    Toast.makeText(getApplicationContext(), "Error, check you required fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //Sets the seekBar
    SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBarChangeListener, int progress, boolean fromUser) {
            // updated continuously as the user slides the thumb

            sizeTextView.setText("Size: " + progress + "m2");
            if (progress == 350) {
                sizeTextView.setText("Size: " + progress + "+" +"m2");
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // called when the user first touches the SeekBar

            //Label which displays size (method above)

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // called after the user finishes moving the SeekBar
        }

    };

    //Sets date picker
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        chosenDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        TextView dateTextView = (TextView) findViewById(R.id.dateTextView);
        dateTextView.setText(chosenDateString);
    }

    //Check spinner selection
    private boolean checkSpinners() {


        if (propertyTypeSpinner.getSelectedItemPosition() == 0 ) {
            TextView errorText = (TextView)propertyTypeSpinner.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText("Select Property Type");

        }else if  (noOfBedroomsSpinner.getSelectedItemPosition() == 0 ) {
            TextView errorText = (TextView) noOfBedroomsSpinner.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText("Select No. of Bedrooms");

        }else if (noOfBathroomsSpinner.getSelectedItemPosition() == 0){
            TextView errorText = (TextView) noOfBathroomsSpinner.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText("Select No. of Bathrooms");

        } else {
            return true;
        }
        return false;
    }

    //Get user input and call displayNextAlert method
    private void getUserInput() {

        propertyNameEditText = (EditText)findViewById(R.id.propertyNameEditText);
        propertyTypeSpinner = (Spinner)findViewById(R.id.propertyTypeSpinner);
        leaseTypeRadioGroup = (RadioGroup)findViewById(R.id.leaseTypeRadioGroup);
        leaseTypeCheckedRadioButton = (RadioButton)findViewById(leaseTypeRadioGroup.getCheckedRadioButtonId());

        cityEditText = (EditText)findViewById(R.id.cityEditText);
        postcodeEditText = (EditText)findViewById(R.id.postcodeEditText);
        noOfBedroomsSpinner = (Spinner) findViewById(R.id.noOfBedroomsSpinner);
        noOfBathroomsSpinner = (Spinner)findViewById(R.id.noOfBathroomsSpinner);
        sizeSeekBar = (SeekBar)findViewById(R.id.sizeSeekBar);
        askingPriceEditText = (EditText)findViewById(R.id.askingPriceEditText);


        propertyName = propertyNameEditText.getText().toString().trim();
        propertyType = propertyTypeSpinner.getSelectedItem().toString().trim();
        leaseType = leaseTypeCheckedRadioButton.getText().toString().trim();
        city = cityEditText.getText().toString().trim();
        postcode = postcodeEditText.getText().toString().trim();
        noOfBedrooms = noOfBedroomsSpinner.getSelectedItem().toString().trim();
        noOfBathrooms = noOfBathroomsSpinner.getSelectedItem().toString().trim();
        size = String.valueOf(sizeSeekBar.getProgress()).trim();
        askingPrice = askingPriceEditText.getText().toString().trim();

        displayNextAlert(propertyName, propertyType, leaseType, city, postcode, noOfBedrooms, noOfBathrooms, size, askingPrice);
    }

    //Saves the input to the DB
    private void saveDetails() {
        DatabaseHelper DBHelper =  new DatabaseHelper(AddProperty.this);

        EditText description = (EditText)findViewById(R.id.descriptionEditText);
        TextView dateAvailable = (TextView)findViewById(R.id.dateTextView);
        RadioGroup furnishTypeRadioGroup = (RadioGroup)findViewById(R.id.furnishTypeRadioGroup);
        RadioButton furnishTypeRadioButton = (RadioButton)findViewById(furnishTypeRadioGroup.getCheckedRadioButtonId());

        String strDescription = null;
        String strDateAvailable = null;
        String strFurnishType = null;
        if (furnishTypeRadioButton != null ) {
             strFurnishType = furnishTypeRadioButton.getText().toString().trim();
        }
        if (description != null ) {
            strDescription = description.getText().toString().trim();
        }
        if (dateAvailable != null ) {
            strDateAvailable = dateAvailable.getText().toString().trim();
        }


        long propertyID = DBHelper.insertPropertyTable(propertyName, propertyType, leaseType, city, postcode, noOfBedrooms, noOfBathrooms, Integer.parseInt(size), Integer.parseInt(askingPrice), strDescription, strDateAvailable, strFurnishType);

        DBHelper.insertLocalAmenityTable(propertyID, localAmenitiesCheckedBool);

        Toast.makeText(this, "Property has been created with id " + propertyID, Toast.LENGTH_LONG).show();
        finish();
    }

    private void displayNextAlert(String propertyName, String propertyType, String leaseType, String city, String postcode,
                                  String noOfBedrooms, String noOfBathrooms, String size, String askingPrice) {

        new AlertDialog.Builder(this).setTitle("Details entered").setMessage("Property Name: " + propertyName + "\n" + "Property Type: " + propertyType + "\n" + "Lease Type: " + leaseType + "\n" + "City: " + city + "\n" + "Postcode: " + postcode
                + "\n" + "No. of Bedrooms: "  + noOfBedrooms + "\n" + "No. of Bathrooms: " + noOfBathrooms + "\n" + "Size: " + size + "m2" + "\n" + "Asking Price: £" + askingPrice).setNeutralButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { }
        }).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveDetails();

            }
        }).show();

    }


}



