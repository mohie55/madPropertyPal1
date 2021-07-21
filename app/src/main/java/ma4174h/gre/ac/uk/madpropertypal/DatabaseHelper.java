package ma4174h.gre.ac.uk.madpropertypal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;




public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "madPropertyPalDB";
    private SQLiteDatabase sqLiteDatabase;
    private Context context;


    //table 1
    private static final String TABLE1_NAME = "property";
    public static final String PROPERTY_ID_COLUMN = "property_id";
    public static final String PROPERTY_NAME_COLUMN = "property_name";
    public static final String PROPERTY_TYPE_COLUMN = "property_type";
    public static final String LEASE_TYPE_COLUMN = "lease_type";
    public static final String CITY_COLUMN = "city";
    public static final String POSTCODE_COLUMN = "postcode";
    public static final String NO_OF_BEDROOMS_COLUMN = "no_of_bedrooms";
    public static final String NO_OF_BATHROOMS_COLUMN = "no_of_bathrooms";
    public static final String SIZE_COLUMN = "size";
    public static final String ASKING_PRICE_COLUMN = "asking_price";
    public static final String DESCRIPTION_COLUMN = "description";
    public static final String DATE_AVAILABLE_COLUMN = "date_available";
    public static final String FURNISH_TYPE_COLUMN = "furnish_type";

    //table 2
    private static final String TABLE2_NAME = "local_amenity";
    public static final String LOCAL_AMENITY_ID_COLUMN = "local_amenity_id";
    public static final String LOCAL_AMENITY_CHECKED_COLUMN = "local_amenity_checked";

    //table 3
    private static final String TABLE3_NAME = "property_viewing";
    public static final String VIEWING_ID_COLUMN = "viewing_id";
    public static final String PROPERTY_ID_VIEWING_COLUMN = "property_id_viewing";
    public static final String DATE_OF_VIEWING_COLUMN = "date_of_viewing";
    public static final String INTEREST_COLUMN = "interest";
    public static final String OFFER_PRICE_COLUMN = "offer_price";
    public static final String OFFER_EXPIRY_DATE_COLUMN = "offer_expiry_date";
    public static final String CONDITION_OF_OFFER_COLUMN = "condition_of_offer";
    public static final String VIEWING_COMMENTS_COLUMN = "viewing_comments";

    //Creates the DB


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
        sqLiteDatabase = getWritableDatabase();
        this.context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE1);
        db.execSQL(CREATE_TABLE2);
        db.execSQL(CREATE_TABLE3);
    }

    public void deleteDB() {
        context.deleteDatabase(DATABASE_NAME);
    }

    //SQL to create property table
    private static final String CREATE_TABLE1 = String.format(
            "CREATE TABLE %s (" +
                    " %s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " %s TEXT, " +
                    " %s TEXT, " +
                    " %s TEXT, " +
                    " %s TEXT, " +
                    " %s TEXT, " +
                    " %s TEXT, " +
                    " %s TEXT, " +
                    " %s INTEGER, " +
                    " %s INTEGER, " +
                    " %s TEXT, " +
                    " %s TEXT, " +
                    " %s TEXT)",
            TABLE1_NAME, PROPERTY_ID_COLUMN, PROPERTY_NAME_COLUMN, PROPERTY_TYPE_COLUMN, LEASE_TYPE_COLUMN,CITY_COLUMN, POSTCODE_COLUMN,
            NO_OF_BEDROOMS_COLUMN, NO_OF_BATHROOMS_COLUMN, SIZE_COLUMN, ASKING_PRICE_COLUMN, DESCRIPTION_COLUMN, DATE_AVAILABLE_COLUMN, FURNISH_TYPE_COLUMN);

    //SQL to create local amenity table
    private static final String CREATE_TABLE2 = String.format(
            "CREATE TABLE %s (" +
                    " %s INTEGER, " +
                    " %s INTEGER, " +
                    " %s INTEGER, " +
                    "PRIMARY KEY (%s, %s)) ",
            TABLE2_NAME, PROPERTY_ID_COLUMN, LOCAL_AMENITY_ID_COLUMN, LOCAL_AMENITY_CHECKED_COLUMN, PROPERTY_ID_COLUMN, LOCAL_AMENITY_ID_COLUMN);

    //SQL to create property viewing table
    private static final String CREATE_TABLE3 = String.format(
            "CREATE TABLE %s (" +
                    " %s INTEGER PRIMARY KEY AUTOINCREMENT , " +
                    " %s INTEGER , " +
                    " %s TEXT, " +
                    " %s TEXT, " +
                    " %s INTEGER, " +
                    " %s TEXT, " +
                    " %s TEXT, " +
                    " %s TEXT)",
            TABLE3_NAME, VIEWING_ID_COLUMN, PROPERTY_ID_VIEWING_COLUMN, DATE_OF_VIEWING_COLUMN, INTEREST_COLUMN, OFFER_PRICE_COLUMN, OFFER_EXPIRY_DATE_COLUMN,CONDITION_OF_OFFER_COLUMN,VIEWING_COMMENTS_COLUMN) ;


    //Called if the version changes, to upgrade the DB.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);

        Log.v(this.getClass().getName(), DATABASE_NAME + " database upgrade to version " + newVersion + " - old data lost");
        onCreate(db);
    }

    //Insert records into property table
    public long insertPropertyTable(String property_name, String property_type, String lease_type, String city, String postcode, String no_of_bedrooms, String no_of_bathrooms, int size, int asking_price,
                                    String description, String date_available, String furnish_type) {
        ContentValues rowValues = new ContentValues();

        rowValues.put(PROPERTY_NAME_COLUMN, property_name);
        rowValues.put(PROPERTY_TYPE_COLUMN, property_type);
        rowValues.put(LEASE_TYPE_COLUMN, lease_type);
        rowValues.put(CITY_COLUMN, city);
        rowValues.put(POSTCODE_COLUMN, postcode);
        rowValues.put(NO_OF_BEDROOMS_COLUMN, no_of_bedrooms);
        rowValues.put(NO_OF_BATHROOMS_COLUMN, no_of_bathrooms);
        rowValues.put(SIZE_COLUMN, size);
        rowValues.put(ASKING_PRICE_COLUMN, asking_price);
        rowValues.put(DESCRIPTION_COLUMN, description);
        rowValues.put(DATE_AVAILABLE_COLUMN, date_available);
        rowValues.put(FURNISH_TYPE_COLUMN, furnish_type);

        return sqLiteDatabase.insertOrThrow(TABLE1_NAME, null, rowValues);

    }

    //Insert records into local amenity table
    public void insertLocalAmenityTable(long property_id, boolean[] checkedAmenities) {
        ContentValues rowValues = new ContentValues();
        int i = 0;
        for (Boolean checkedAmenity : checkedAmenities) {
            //Convert boolean to int (1,0) because sqlite can't save boolean type
            int intCheckedAmenity = (checkedAmenity) ? 1 : 0;
            rowValues.put(PROPERTY_ID_COLUMN, Math.toIntExact(property_id));
            rowValues.put(LOCAL_AMENITY_ID_COLUMN, i);
            rowValues.put(LOCAL_AMENITY_CHECKED_COLUMN, intCheckedAmenity);
            i++;
            sqLiteDatabase.insertOrThrow(TABLE2_NAME, null, rowValues);
        }
    }

    //Insert a viewing into property viewing table
    public long insertPropertyViewingTable(int propertyID, String viewingDateString, String interestStr, int offerPrice, String offerExpiryDateString, String conditionsOfOfferStr, String viewingCommentsStr) {
        ContentValues rowValues = new ContentValues();

        rowValues.put(PROPERTY_ID_VIEWING_COLUMN, propertyID);
        rowValues.put(DATE_OF_VIEWING_COLUMN, viewingDateString);
        rowValues.put(INTEREST_COLUMN, interestStr);
        rowValues.put(OFFER_PRICE_COLUMN, offerPrice);
        rowValues.put(OFFER_EXPIRY_DATE_COLUMN, offerExpiryDateString);
        rowValues.put(CONDITION_OF_OFFER_COLUMN, conditionsOfOfferStr);
        rowValues.put(VIEWING_COMMENTS_COLUMN, viewingCommentsStr);

        return sqLiteDatabase.insertOrThrow(TABLE3_NAME, null, rowValues);
    }

    //Reads property table
    public Cursor getPropertyTable() {
        Cursor cursor = null;
        if (sqLiteDatabase != null) {
            cursor = sqLiteDatabase.query(TABLE1_NAME, new String[]{PROPERTY_ID_COLUMN, PROPERTY_NAME_COLUMN, PROPERTY_TYPE_COLUMN, LEASE_TYPE_COLUMN, CITY_COLUMN, POSTCODE_COLUMN,
                    NO_OF_BEDROOMS_COLUMN, NO_OF_BATHROOMS_COLUMN, SIZE_COLUMN, ASKING_PRICE_COLUMN, DESCRIPTION_COLUMN, DATE_AVAILABLE_COLUMN, FURNISH_TYPE_COLUMN},
                    null, null, null, null, null);
        }
        return cursor;
    }


    //Reads local amenity table
    public Cursor getLocalAmenityTable(String id) {
        Cursor cursor = null;

        if (sqLiteDatabase != null) {
            cursor = sqLiteDatabase.query(TABLE2_NAME, new String[]{PROPERTY_ID_COLUMN, LOCAL_AMENITY_ID_COLUMN, LOCAL_AMENITY_CHECKED_COLUMN}, PROPERTY_ID_COLUMN + " = " + id, null, null, null, PROPERTY_ID_COLUMN);
        }
        return cursor;
    }

  /*  public Cursor getLocalAmenityTable() {
        Cursor cursor = null;

        if (sqLiteDatabase != null) {
            cursor = sqLiteDatabase.query(TABLE2_NAME, new String[]{PROPERTY_ID_COLUMN, LOCAL_AMENITY_ID_COLUMN, LOCAL_AMENITY_CHECKED_COLUMN}, null, null, null, null, PROPERTY_ID_COLUMN);
        }
        return cursor;
    }*/

    //Reads local amenity table
    public Cursor getPropertyViewingTable(String id) {
        Cursor cursor = null;
        if (sqLiteDatabase != null) {
            cursor = sqLiteDatabase.query(TABLE3_NAME, new String[]{VIEWING_ID_COLUMN, PROPERTY_ID_VIEWING_COLUMN, DATE_OF_VIEWING_COLUMN,INTEREST_COLUMN, OFFER_PRICE_COLUMN,
                    OFFER_EXPIRY_DATE_COLUMN,CONDITION_OF_OFFER_COLUMN,VIEWING_COMMENTS_COLUMN}, PROPERTY_ID_VIEWING_COLUMN + " = " + id, null, null, null, null);
        }
        return cursor;
    }
    //TABLE3_NAME, VIEWING_ID_COLUMN, PROPERTY_ID_VIEWING_COLUMN, DATE_OF_VIEWING_COLUMN, INTEREST_COLUMN, OFFER_PRICE_COLUMN, OFFER_EXPIRY_DATE_COLUMN,CONDITION_OF_OFFER_COLUMN,VIEWING_COMMENTS_COLUMN


    //Delete record from property table
    public Integer deletePropertyTableItem(long id) {

        int returnedID = sqLiteDatabase.delete(TABLE1_NAME, PROPERTY_ID_COLUMN + " = " + id, null);

        return returnedID;

    }


    public long updatePropertyTable(String propertyID, String property_name, String property_type, String lease_type, String city, String postcode, String no_of_bedrooms, String no_of_bathrooms, int size, int asking_price,
                                    String description, String date_available, String furnish_type) {
        ContentValues rowValues = new ContentValues();

        rowValues.put(PROPERTY_NAME_COLUMN, property_name);
        rowValues.put(PROPERTY_TYPE_COLUMN, property_type);
        rowValues.put(LEASE_TYPE_COLUMN, lease_type);
        rowValues.put(CITY_COLUMN, city);
        rowValues.put(POSTCODE_COLUMN, postcode);
        rowValues.put(NO_OF_BEDROOMS_COLUMN, no_of_bedrooms);
        rowValues.put(NO_OF_BATHROOMS_COLUMN, no_of_bathrooms);
        rowValues.put(SIZE_COLUMN, size);
        rowValues.put(ASKING_PRICE_COLUMN, asking_price);
        rowValues.put(DESCRIPTION_COLUMN, description);
        rowValues.put(DATE_AVAILABLE_COLUMN, date_available);
        rowValues.put(FURNISH_TYPE_COLUMN, furnish_type);

        return sqLiteDatabase.update(TABLE1_NAME, rowValues, PROPERTY_ID_COLUMN + " = ?", new String[] {propertyID});
    }

    public void updateLocalAmenityTable(String propertyID, boolean[] localAmenitiesCheckedBool) {
        ContentValues rowValues = new ContentValues();
        int i = 0;
        for (Boolean localAmenity : localAmenitiesCheckedBool) {
            //Convert boolean to int (1,0) because sqlite can't save boolean type
            int intCheckedAmenity = (localAmenity) ? 1 : 0;
           // rowValues.put(PROPERTY_ID_COLUMN, Integer.parseInt(propertyID));
           // rowValues.put(LOCAL_AMENITY_ID_COLUMN, i);
            rowValues.put(LOCAL_AMENITY_CHECKED_COLUMN, intCheckedAmenity);

            sqLiteDatabase.update(TABLE2_NAME, rowValues,PROPERTY_ID_COLUMN + " = ? AND " + LOCAL_AMENITY_ID_COLUMN + " = ?" ,new String[] {propertyID,String.valueOf(i)});
            i++;
        }
    }


}
