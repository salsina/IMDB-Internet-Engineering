import org.json.*;


public class ErrorHandler {
    public JSONObject success(String message) {
        JSONObject output = new JSONObject();
        output.put("success", true);
        output.put("data", message);
        return output;
    }
    public JSONObject success(JSONObject jsonObject) {
        JSONObject output = new JSONObject();
        output.put("success", true);
        output.put("data", jsonObject);
        return output;
    }

    public JSONObject fail(String message) {
        JSONObject output = new JSONObject();
        output.put("success", false);
        output.put("data", message);
        return output;
    }


}
