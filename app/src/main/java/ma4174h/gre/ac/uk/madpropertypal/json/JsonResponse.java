package ma4174h.gre.ac.uk.madpropertypal.json;

import com.google.gson.Gson;

public class JsonResponse {

    private String uploadResponseCode, userid, message, names;
    private int number;
    Gson gson;

    public JsonResponse(String jsonResponse) {
        gson = new Gson();
        this.uploadResponseCode = gson.fromJson(jsonResponse,JsonResponse.class).uploadResponseCode;
        this.userid = gson.fromJson(jsonResponse,JsonResponse.class).userid;
        this.message = gson.fromJson(jsonResponse,JsonResponse.class).message;
        this.names = gson.fromJson(jsonResponse,JsonResponse.class).names;
        this.number = gson.fromJson(jsonResponse,JsonResponse.class).number;
    }

    @Override
    public String toString() {
        return "jsonResponse{" +
                "uploadResponseCode='" + uploadResponseCode + '\'' +
                ", userid='" + userid + '\'' +
                ", message='" + message + '\'' +
                ", names='" + names + '\'' +
                ", number=" + number +
                '}';
    }

    public String getUploadResponseCode() {
        return uploadResponseCode;
    }

    public String getUserid() {
        return userid;
    }

    public String getMessage() {
        return message;
    }

    public String getNames() {
        return names;
    }

    public int getNumber() {
        return number;
    }


}
