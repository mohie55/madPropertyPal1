package ma4174h.gre.ac.uk.madpropertypal;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.util.Calendar;

public class AddViewing extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private String viewingDateString, offerExpiryDateString;
    private RadioGroup interestRadioGroup;
    private RadioButton interestCheckedRadioButton;
    private EditText offerPriceEditText, conditionsOfOfferEditText, viewingCommentsEditText;
    private Button addViewingButton, dateOfViewingButton, offerExpiryDateButton;
    private TextView viewingDateTextView, offerDateTextView, propertyIdTextView;
    private String interestStr, conditionsOfOfferStr, viewingCommentsStr;
    private int offerPrice, propertyID;
    private DatabaseHelper databaseHelper;
    private String tag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_viewing);

        initElements();

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        if (tag == "Viewing Date") {

            viewingDateString = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

            viewingDateTextView.setText(viewingDateString);


        } else if (tag == "Offer Expiry Date") {

            offerExpiryDateString = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
            offerDateTextView = findViewById(R.id.offerDateTextView);
            offerDateTextView.setText(offerExpiryDateString);


        }
    }

    public void initElements() {

        dateOfViewingButton = findViewById(R.id.dateOfViewingButton);
        viewingDateTextView = findViewById(R.id.viewingDateTextViewAV);
        offerExpiryDateButton = findViewById(R.id.offerExpiryDateButton);
        dateOfViewingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("1", "Viewing Date");
                DatePickerFragment datePicker = new DatePickerFragment();
                datePicker.setArguments(bundle);
                tag = "Viewing Date";
                datePicker.show(getSupportFragmentManager(), "Viewing Date");
            }
        });
        offerExpiryDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("1", "Offer Expiry Date");
                DatePickerFragment datePicker2 = new DatePickerFragment();
                datePicker2.setArguments(bundle);
                tag = "Offer Expiry Date";
                datePicker2.show(getSupportFragmentManager(), "Offer Expiry Date");

            }
        });

        databaseHelper = new DatabaseHelper(AddViewing.this);

        propertyID = getIntent().getIntExtra("propertyID", -1);
        propertyIdTextView = findViewById(R.id.propertyIdTextView);
        propertyIdTextView.setText("Property ID: " + propertyID);


        interestRadioGroup = findViewById(R.id.interestRadioGroup);
        interestCheckedRadioButton = findViewById(interestRadioGroup.getCheckedRadioButtonId());
        interestStr = interestCheckedRadioButton.getText().toString();

        offerPriceEditText = findViewById(R.id.offerPriceEditText);


        conditionsOfOfferEditText = findViewById(R.id.conditionsOFOfferEditText);

        viewingCommentsEditText = findViewById(R.id.viewingCommentsEditText);

        addViewingButton = findViewById(R.id.submitViewingButton);
        addViewingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveViewing();
            }
        });


    }

    public Boolean validate() {

        if (viewingDateTextView.getText().toString().isEmpty()) {
            viewingDateTextView.setError("Viewing date is required");
            Toast.makeText(this,"Please select a date",Toast.LENGTH_LONG);
            return false;
        }

        return true;
    }


    public void saveViewing() {

        if (validate()) {
            if (!offerPriceEditText.getText().toString().isEmpty()) {
                offerPrice = Integer.parseInt(offerPriceEditText.getText().toString());
            }
                if (!conditionsOfOfferEditText.getText().toString().isEmpty()) {
                    conditionsOfOfferStr = conditionsOfOfferEditText.getText().toString();

                }
                if (!viewingCommentsEditText.getText().toString().isEmpty()) {
                    viewingCommentsStr = viewingCommentsEditText.getText().toString();
                }

                long viewingID = databaseHelper.insertPropertyViewingTable(propertyID, viewingDateString, interestStr, offerPrice, offerExpiryDateString, conditionsOfOfferStr, viewingCommentsStr);

                Toast.makeText(this, "Viewing has been added", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

