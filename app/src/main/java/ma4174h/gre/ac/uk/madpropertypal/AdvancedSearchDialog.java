package ma4174h.gre.ac.uk.madpropertypal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatDialogFragment;

public class AdvancedSearchDialog extends AppCompatDialogFragment {

    private EditText searchCityEditText;
    private Spinner searchPropertyTypeSpinner, searchNoOfBedroomsSpinner;
    private AdvancedSearchDialogListener listener;

    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.advanced_search_dialog,null);

        builder.setView(view).setTitle("Advanced Search").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        })
                .setPositiveButton("Search", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String city = searchCityEditText.getText().toString();
                        String propertyType = searchPropertyTypeSpinner.getSelectedItem().toString();
                        String noOfBedrooms = searchNoOfBedroomsSpinner.getSelectedItem().toString();
                        listener.applyTexts(city,propertyType,noOfBedrooms);
                        /*Intent intent = new Intent(getActivity(), ListOfProperties.class);
                        intent.putExtra("city",city);
                        intent.putExtra("propertyType",propertyType);
                        intent.putExtra("noOfBedrooms",noOfBedrooms);
                        startActivity(intent);*/
                    }
                });
        searchCityEditText = view.findViewById(R.id.searchCityEditText);
        searchPropertyTypeSpinner =  view.findViewById(R.id.searchPropertyTypeSpinner);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.propertyType));
        searchPropertyTypeSpinner.setAdapter(myAdapter);

        searchNoOfBedroomsSpinner = view.findViewById(R.id.searchNoOfBedroomsSpinner);
        ArrayAdapter<String> myAdapter2 = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.noOfBedrooms));
        searchNoOfBedroomsSpinner.setAdapter(myAdapter2);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (AdvancedSearchDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement AdvancedSearchDialogListener");
        }
    }

    public interface AdvancedSearchDialogListener{
        void applyTexts(String city, String propertyType, String noOfBedrooms);
    }

}
