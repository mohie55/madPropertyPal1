package ma4174h.gre.ac.uk.madpropertypal;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static com.basgeekball.awesomevalidation.ValidationStyle.TEXT_INPUT_LAYOUT;

public class UpdateProperty extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private String propertyID, propertyType, propertyName, city, noOfBedrooms, askingPrice, postcode, description, size, noOfBathrooms, chosenDateString, furnishType, leaseType;
    boolean[] localAmenitiesCheckedBool;
    EditText propertyNameEditText, cityEditText, postcodeEditText, askingPriceEditText, descriptionEditText;
    RadioGroup leaseTypeRadioGroup, furnishTypeRadioGroup;
    RadioButton leaseTypeCheckedRadioButton;
    Spinner propertyTypeSpinner, noOfBedroomsSpinner, noOfBathroomsSpinner;
    TextView sizeTextView, dateTextView, amenitiesTextView;
    Button amenitiesButton, dateButton, updateButton;
    String[] amenitiesList;
    ArrayList<Integer> selectedAmenities = new ArrayList<>();
    SeekBar sizeSeekBar;
    AwesomeValidation awesomeValidation;
    DatabaseHelper DBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntentData();
        initViews();
        initOnCLickListeners();
        loadSavedLocalMAmenities();
        setIntentData();

    }

    public void initViews() {
        setContentView(R.layout.update_property);
        propertyNameEditText = findViewById(R.id.propertyNameEditTextUpdate);
        cityEditText = findViewById(R.id.cityEditTextUpdate);
        postcodeEditText = findViewById(R.id.postcodeEditTextUpdate);
        askingPriceEditText = findViewById(R.id.askingPriceEditTextUpdate);
        propertyTypeSpinner = findViewById(R.id.propertyTypeSpinnerUpdate);
        leaseTypeRadioGroup = findViewById(R.id.leaseTypeRadioGroupUpdate);
        noOfBedroomsSpinner = findViewById(R.id.noOfBedroomsSpinnerUpdate);
        noOfBathroomsSpinner = findViewById(R.id.noOfBathroomsSpinnerUpdate);
        amenitiesButton = findViewById(R.id.amenitiesButtonUpdate);
        descriptionEditText = findViewById(R.id.descriptionEditTextUpdate);
        dateTextView = findViewById(R.id.dateTextViewUpdate);
        dateButton = findViewById(R.id.dateButtonUpdate);
        furnishTypeRadioGroup = findViewById(R.id.furnishTypeRadioGroupUpdate);
        updateButton = findViewById(R.id.updateButton);
        sizeSeekBar = findViewById(R.id.sizeSeekBarUpdate);
        sizeTextView = findViewById(R.id.sizeTextViewUpdate);
        amenitiesTextView = findViewById(R.id.amenitiesTextViewUpdate);


        //Declaring validation method and style
        final AwesomeValidation awesomeValidation = new AwesomeValidation(TEXT_INPUT_LAYOUT);

        //Adding validations for user input views
        awesomeValidation.addValidation(UpdateProperty.this, R.id.propertyNameTILUpdate, "(?i)^[a-z0-9]+(?:[ -]?[a-z0-9]+)*$", R.string.errPropertyName);
        awesomeValidation.addValidation(UpdateProperty.this, R.id.cityTILUpdate, "^[a-zA-Z-,]+(\\s{0,1}[a-zA-Z-, ])*$", R.string.errCity);
        awesomeValidation.addValidation(UpdateProperty.this, R.id.postcodeTILUpdate, "([Gg][Ii][Rr] 0[Aa]{2})|((([A-Za-z][0-9]{1,2})|(([A-Za-z][A-Ha-hJ-Yj-y][0-9]{1,2})|(([A-Za-z][0-9][A-Za-z])|([A-Za-z][A-Ha-hJ-Yj-y][0-9][A-Za-z]?))))\\s?[0-9][A-Za-z]{2})", R.string.errPostcode);
        awesomeValidation.addValidation(UpdateProperty.this, R.id.askingPriceTILUpdate, "^[0-9]+$", R.string.errAskingPrice);

        //database helper
         DBHelper =  new DatabaseHelper(UpdateProperty.this);
    }

    public void getIntentData() {
        if (getIntent().hasExtra("PropertyId") && getIntent().hasExtra("propertyName") && getIntent().hasExtra("propertyType") && getIntent().hasExtra("leaseType") && getIntent().hasExtra("city")
                && getIntent().hasExtra("postcode") && getIntent().hasExtra("noOfBedrooms") && getIntent().hasExtra("noOfBathrooms") && getIntent().hasExtra("size") && getIntent().hasExtra("askingPrice")
                && getIntent().hasExtra("localAmenitiesBool") && getIntent().hasExtra("description") && getIntent().hasExtra("dateString") && getIntent().hasExtra("furnishType")) {

            propertyID = getIntent().getStringExtra("PropertyId");
            propertyName = getIntent().getStringExtra("propertyName");
            propertyType = getIntent().getStringExtra("propertyType");
            leaseType = getIntent().getStringExtra("leaseType");
            city = getIntent().getStringExtra("city");
            postcode = getIntent().getStringExtra("postcode");
            noOfBedrooms = getIntent().getStringExtra("noOfBedrooms");
            noOfBathrooms = getIntent().getStringExtra("noOfBathrooms");
            size = getIntent().getStringExtra("size");
            askingPrice = getIntent().getStringExtra("askingPrice");
            localAmenitiesCheckedBool = getIntent().getBooleanArrayExtra(("localAmenitiesBool"));
            description = getIntent().getStringExtra("description");
            chosenDateString = getIntent().getStringExtra("dateString");
            furnishType = getIntent().getStringExtra("furnishType");

        } else {
            Toast.makeText(this, "No data. ", Toast.LENGTH_SHORT);
        }
    }

    public void setIntentData() {
        propertyNameEditText.setText(propertyName);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.propertyType, android.R.layout.simple_list_item_1);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        propertyTypeSpinner.setAdapter(adapter);
        if (propertyType != null) {
            int spinnerPosition = adapter.getPosition(propertyType);
            propertyTypeSpinner.setSelection(spinnerPosition);
        }

        //Set radio button to the saved selection
        RadioButton leaseTypeRadio1 = findViewById(R.id.radio1Update);
        RadioButton leaseTypeRadio2 = findViewById(R.id.radio2Update);
        RadioButton leaseTypeRadio3 = findViewById(R.id.radio3Update);
        RadioButton leaseTypeRadio4 = findViewById(R.id.radio4Update);
        String strRadio1 = leaseTypeRadio1.getText().toString();
        String strRadio2 = leaseTypeRadio2.getText().toString();
        String strRadio3 = leaseTypeRadio3.getText().toString();
        String strRadio4 = leaseTypeRadio4.getText().toString();

        if (strRadio1.equals(leaseType)) {
            leaseTypeRadio1.setChecked(true);
        }
        else if (strRadio2.equals(leaseType)) {
            leaseTypeRadio2.setChecked(true);
        }
        else if (strRadio3.equals(leaseType)) {
            leaseTypeRadio3.setChecked(true);
        }
        else if (strRadio4.equals(leaseType)) {
            leaseTypeRadio4.setChecked(true);
        }

        cityEditText.setText(city);
        postcodeEditText.setText(postcode);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.noOfBedrooms, android.R.layout.simple_list_item_1);
        adapter2.setDropDownViewResource(android.R.layout.simple_list_item_1);
        noOfBedroomsSpinner.setAdapter(adapter2);
        if (noOfBedrooms != null) {
            int spinnerPosition = adapter2.getPosition(noOfBedrooms);
            noOfBedroomsSpinner.setSelection(spinnerPosition);
        }
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.noOfBathrooms, android.R.layout.simple_list_item_1);
        adapter3.setDropDownViewResource(android.R.layout.simple_list_item_1);
        noOfBathroomsSpinner.setAdapter(adapter3);
        if (noOfBathrooms != null) {
            int spinnerPosition = adapter3.getPosition(noOfBathrooms);
            noOfBathroomsSpinner.setSelection(spinnerPosition);
        }
        sizeSeekBar.setOnSeekBarChangeListener(setSeekBarListener());
        sizeSeekBar.setProgress(Integer.parseInt(size));
        sizeTextView.setText("Size: " + size + "m2");
        askingPriceEditText.setText(askingPrice);
        descriptionEditText.setText(description);
        dateTextView.setText(chosenDateString);

        //Finding the value that matches the right radio button then checking/selecting it
            RadioButton furnishTypeRadio5 = findViewById(R.id.radio5Update);
            RadioButton furnishTypeRadio6 = findViewById(R.id.radio6Update);
            RadioButton furnishTypeRadio7 = findViewById(R.id.radio7Update);
            String strRadio5 = furnishTypeRadio5.getText().toString();
            String strRadio6 = furnishTypeRadio6.getText().toString();
            String strRadio7 = furnishTypeRadio7.getText().toString();

            if (strRadio5.equals(furnishType)) {
                furnishTypeRadio5.setChecked(true);
            }
            else if (strRadio6.equals(furnishType)) {
                furnishTypeRadio6.setChecked(true);
            }
            else if (strRadio7.equals(furnishType)) {
                furnishTypeRadio7.setChecked(true);
            }
    }

    //Sets the seekBarChangeListener which updates the seekBar value as it moves
    public SeekBar.OnSeekBarChangeListener setSeekBarListener() {
        SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBarChangeListener, int progress, boolean fromUser) {
                // updated continuously as the user slides the thumb

                sizeTextView.setText("Size: " + progress + "m2");
                if (progress == 350) {
                    sizeTextView.setText("Size: " + progress + "+" + "m2");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // called when the user first touches the SeekBar

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // called after the user finishes moving the SeekBar
            }

        };

        return onSeekBarChangeListener;
    }

    //Amenities button on click listener
    public void initOnCLickListeners() {
        //Local Amenities
        amenitiesList = getResources().getStringArray(R.array.amenitiesList);
        amenitiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProperty.this);
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
                        saveAmenities();
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

        // date picker on click listener
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "Date Picker");
            }
        });

        //when clicking update validate input and update data
        updateButton.setOnClickListener(new View.OnClickListener() {
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
    public void loadSavedLocalMAmenities() {
        for (int i = 0; i < localAmenitiesCheckedBool.length; i++) {
            if (localAmenitiesCheckedBool[i]) {
                selectedAmenities.add(i);
            }
        }
        saveAmenities();
    }
    //Get user input and call displayNextAlert method
    private void getUserInput() {

        propertyNameEditText = (EditText)findViewById(R.id.propertyNameEditTextUpdate);
        propertyTypeSpinner = (Spinner)findViewById(R.id.propertyTypeSpinnerUpdate);
        leaseTypeRadioGroup = (RadioGroup)findViewById(R.id.leaseTypeRadioGroupUpdate);
        leaseTypeCheckedRadioButton = (RadioButton)findViewById(leaseTypeRadioGroup.getCheckedRadioButtonId());

        cityEditText = (EditText)findViewById(R.id.cityEditTextUpdate);
        postcodeEditText = (EditText)findViewById(R.id.postcodeEditTextUpdate);
        noOfBedroomsSpinner = (Spinner) findViewById(R.id.noOfBedroomsSpinnerUpdate);
        noOfBathroomsSpinner = (Spinner)findViewById(R.id.noOfBathroomsSpinnerUpdate);
        sizeSeekBar = (SeekBar)findViewById(R.id.sizeSeekBarUpdate);
        askingPriceEditText = (EditText)findViewById(R.id.askingPriceEditTextUpdate);


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
    private void displayNextAlert(String propertyName, String propertyType, String leaseType, String city, String postcode,
                                  String noOfBedrooms, String noOfBathrooms, String size, String askingPrice) {

        new AlertDialog.Builder(this).setTitle("Details entered").setMessage("Property Name: " + propertyName + "\n" + "Property Type: " + propertyType + "\n" + "Lease Type: " + leaseType + "\n" + "City: " + city + "\n" + "Postcode: " + postcode
                + "\n" + "No. of Bedrooms: "  + noOfBedrooms + "\n" + "No. of Bathrooms: " + noOfBathrooms + "\n" + "Size: " + size + "m2" + "\n" + "Asking Price: £" +
                askingPrice).setNeutralButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { }
        }).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateDetails();

            }
        }).show();

    }

    private void updateDetails() {

        EditText description = (EditText)findViewById(R.id.descriptionEditTextUpdate);
        TextView dateAvailable = (TextView)findViewById(R.id.dateTextViewUpdate);
        RadioGroup furnishTypeRadioGroup = (RadioGroup)findViewById(R.id.furnishTypeRadioGroupUpdate);
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

        DBHelper.updatePropertyTable(propertyID, propertyName, propertyType, leaseType, city, postcode, noOfBedrooms, noOfBathrooms, Integer.parseInt(size), Integer.parseInt(askingPrice), strDescription, strDateAvailable, strFurnishType);

        DBHelper.updateLocalAmenityTable(propertyID, localAmenitiesCheckedBool);

        Toast.makeText(this, "Property with id " + propertyID + " has been updated.", Toast.LENGTH_LONG).show();
    }

    public void saveAmenities() {
            String amenitiesString = "";
            for (int i = 0; i < selectedAmenities.size(); i++) {
                amenitiesString += amenitiesList[selectedAmenities.get(i)];
                if (i != selectedAmenities.size() - 1) {
                    amenitiesString += ", ";
                } else {
                    amenitiesString += ".";
                }
            }
            amenitiesTextView.setText(amenitiesString);
        }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        chosenDateString = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        TextView dateTextView = (TextView) findViewById(R.id.dateTextViewUpdate);
        dateTextView.setText(chosenDateString);
    }


    //Check spinner selection
    private boolean checkSpinners() {

        if (propertyTypeSpinner.getSelectedItemPosition() == 0) {
            TextView errorText = (TextView) propertyTypeSpinner.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText("Select Property Type");

        } else if (noOfBedroomsSpinner.getSelectedItemPosition() == 0) {
            TextView errorText = (TextView) noOfBedroomsSpinner.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText("Select No. of Bedrooms");

        } else if (noOfBathroomsSpinner.getSelectedItemPosition() == 0) {
            TextView errorText = (TextView) noOfBathroomsSpinner.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText("Select No. of Bathrooms");

        } else {
            return true;
        }
        return false;
    }
}
