package bka.iam.identity.moodle.model;

import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

public class GetUsersResponse {

    private List<Map<String, Object>> users;
    private List<GetUsersResponseWarnings> warnings;

    public List<Map<String, Object>> getUsers() {
        return users;
    }

    public void setUsers(List<Map<String, Object>> users) {
        this.users = users;
    }

    public List<GetUsersResponseWarnings> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<GetUsersResponseWarnings> warnings) {
        this.warnings = warnings;
    }

    @Override
    public String toString() {
        if (users == null) {
            return super.toString();
        }

        String warnStr = "";
        if (warnings != null && warnings.size() > 0) {
            Gson gson = new Gson();
            warnStr = ", warnings: " + gson.toJson(warnings);
        }

        return "GetUsersResponse{users.size(): " + users.size() + warnStr + "}";
    }
}

class GetUsersResponseWarnings {

    private String item;
    private String warningcode;
    private String message;

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getWarningcode() {
        return warningcode;
    }

    public void setWarningcode(String warningcode) {
        this.warningcode = warningcode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Warning{item: "+item+", warningcode"+warningcode+", message"+message+"}";
    }
}