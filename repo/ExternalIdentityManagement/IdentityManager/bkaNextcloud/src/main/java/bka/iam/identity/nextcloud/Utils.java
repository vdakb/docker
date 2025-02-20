package bka.iam.identity.nextcloud;

import java.util.List;

public class Utils {

    public static String[] convertToStringArray(List<String> list) {
        String[] arr = new String[list.size()];
        return list.toArray(arr);
    }

    public static final String translateAttributeName(String attr) {
        if (attr.equals("__UID__")) {
            return "userid";
        }
        if (attr.equals("__NAME__")) {
            return "userid";
        }
        if (attr.equals("__PASSWORD__")) {
            return "password";
        }
        if (attr.equals("__ENABLE__")) {
            return "enabled";
        }
        return attr;
    }
}
