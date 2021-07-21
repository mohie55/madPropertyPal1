package ma4174h.gre.ac.uk.madpropertypal;

import android.database.Cursor;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import ma4174h.gre.ac.uk.madpropertypal.json.JsonPayload;
import ma4174h.gre.ac.uk.madpropertypal.json.JsonResponse;

public class UploadPropertyJson extends AppCompatActivity {

    private String propertyID, propertyName, propertyType, leaseType, city,
            postcode, noOfBedrooms, noOfBathrooms, size, askingPrice,
            description, dateAvailable, furnishType;
    ArrayList<Property> propertyList;
    ArrayList<String> detailsList;
    DatabaseHelper DBHelper;
    private WebView browser;
    Gson gson;
    TextView jsonResponseTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_property);
        detailsList = new ArrayList<>();
        //browser = (WebView) findViewById(R.id.webView);
        try {
            URL pageURL = new URL(getString(R.string.url));
            HttpURLConnection con = (HttpURLConnection) pageURL.openConnection();

            createPropertyObjects();


            gson = new Gson();
            JsonPayload jsonPayLoad = new JsonPayload(propertyList);
            String jsonString = gson.toJson(jsonPayLoad);
            System.out.println(jsonString);

            JsonThread myTask = new JsonThread(this, con, jsonString);
            Thread t1 = new Thread(myTask, "JSON Thread");
            t1.start();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createPropertyObjects() {
        propertyList = new ArrayList<>();
        DBHelper = new DatabaseHelper(UploadPropertyJson.this);
        Cursor PropertyTableCursor = DBHelper.getPropertyTable();

        if (PropertyTableCursor.getCount() == 0) {
            Toast.makeText(this, "No Data", Toast.LENGTH_LONG).show();
        } else {

            while (PropertyTableCursor.moveToNext()) {

                propertyID = PropertyTableCursor.getString(0);
                propertyName = PropertyTableCursor.getString(1);
                propertyType = PropertyTableCursor.getString(2);
                leaseType = PropertyTableCursor.getString(3);
                city = PropertyTableCursor.getString(4);
                postcode = PropertyTableCursor.getString(5);
                noOfBedrooms = PropertyTableCursor.getString(6);
                noOfBathrooms = PropertyTableCursor.getString(7);
                size = PropertyTableCursor.getString(8);
                askingPrice = PropertyTableCursor.getString(9);
                description = PropertyTableCursor.getString(10);
                dateAvailable = PropertyTableCursor.getString(11);
                furnishType = PropertyTableCursor.getString(12);

                Property property = new Property(propertyID, propertyName, propertyType, leaseType, city,
                        postcode, noOfBedrooms, noOfBathrooms, size, askingPrice,
                        description, dateAvailable, furnishType);

                propertyList.add(property);

            }
        }

    }

    /*//converts a class to a json string
    public String toJson(JsonPayload jsonPayload) {

        //Convert the property object to json string
        String json = gson.toJson(jsonPayload);
        return json;
    }*/


    class JsonThread implements Runnable {
        private AppCompatActivity activity;
        private HttpURLConnection con;
        private String jsonPayLoad;

        public JsonThread(AppCompatActivity activity, HttpURLConnection con, String jsonPayload) {
            this.activity = activity;
            this.con = con;
            this.jsonPayLoad = jsonPayload;
        }

        @Override
        public void run() {
            String response = "";
            if (prepareConnection()) {
                response = postJson();
            } else {
                response = "Error preparing the connection";
            }
            showResult(response);
        }


        private void showResult(String response) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    /*ObjectMapper objectMapper = new ObjectMapper();
                    try {
                       String prettyResponse = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);*/

                    JsonResponse jsonResponse = new JsonResponse(response);
                    jsonResponseTextView = findViewById(R.id.jsonResponseTextView);
                    String uploadResponseCode, userid, message, names;
                    uploadResponseCode = jsonResponse.getMessage();
                    userid = jsonResponse.getUserid();
                    message = jsonResponse.getMessage();
                    names = jsonResponse.getNames();

                    jsonResponseTextView.setText("Upload Response: " + uploadResponseCode + "." + "\nUser ID: " + userid + "\nProperty names: " + names + "." + "\nMessage: " + message + ".");
                    //System.out.println(jsonString);
                    //String page = generatePage(response);
                    // ((UploadPropertyJson)activity).browser.loadData(page, "text/html", "UTF-8");
                }
            });
        }

        private String postJson() {
            String response = "";
            try {
                String postParameters = "jsonpayload=" + URLEncoder.encode(jsonPayLoad, "UTF-8");
                con.setFixedLengthStreamingMode(postParameters.getBytes().length);
                PrintWriter out = new PrintWriter(con.getOutputStream());
                out.print(postParameters);
                out.close();
                int responseCode = con.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    response = readStream(con.getInputStream());
                } else {
                    response = "Error contacting server: " + responseCode;
                }
            } catch (Exception e) {
                response = "Error executing code";
            }
            return response;
        }

        private String readStream(InputStream in) {
            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                String nextLine = "";
                while ((nextLine = reader.readLine()) != null) {
                    sb.append(nextLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sb.toString();
        }

        private String generatePage(String content) {
            return "<html><body><p>" + content + "</p></body></html>";
        }


        private boolean prepareConnection() {
            try {
                con.setDoOutput(true);
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                return true;

            } catch (ProtocolException e) {
                e.printStackTrace();
            }
            return false;
        }
    }


}
