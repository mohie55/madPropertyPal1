package ma4174h.gre.ac.uk.madpropertypal.json;

import java.util.ArrayList;

import ma4174h.gre.ac.uk.madpropertypal.Property;

public class JsonPayload {

    private String userId = "ma4174h";
    private ArrayList<Property> detailList;


    public JsonPayload(ArrayList<Property> detailList) {
        this.detailList = detailList;
    }

    public String getUserId() {
        return userId;
    }

    public ArrayList<Property> getDetailList() {
        return detailList;
    }

    @Override
    public String toString() {
        return "jsonPayload{" +
                "userID='" + userId + '\'' +
                ", detailList=" + detailList +
                '}';
    }
}
