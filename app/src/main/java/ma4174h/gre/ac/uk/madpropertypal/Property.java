package ma4174h.gre.ac.uk.madpropertypal;


public class Property {

    private String propertyID, name, type, leaseType, city, postcode, noOfBedrooms, noOfBathrooms, size, askingPrice, description, dateAvailable, furnishType;


    public Property(String propertyID, String propertyName, String propertyType, String leaseType, String city, String postcode, String noOfBedrooms,
                    String noOfBathrooms, String size, String askingPrice, String description, String dateAvailable, String furnishType) {

        this.propertyID = propertyID;
        this.name = propertyName;
        this.type = propertyType;
        this.leaseType = leaseType;
        this.city = city;
        this.postcode = postcode;
        this.noOfBedrooms = noOfBedrooms;
        this.noOfBathrooms = noOfBathrooms;
        this.size = size;
        this.askingPrice = askingPrice;
        this.description = description;
        this.dateAvailable = dateAvailable;
        this.furnishType = furnishType;

    }
}

    /*public static final class propertyEntry implements BaseColumns {
        public static final String TABLE1_NAME = "property";
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
    }*/
