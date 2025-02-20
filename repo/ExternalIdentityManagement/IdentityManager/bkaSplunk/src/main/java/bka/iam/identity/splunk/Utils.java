package bka.iam.identity.splunk;

public class Utils {

    public static final String translateAttributeName(String attr) {
        if (attr.equals("__UID__")) {
            return "name";
        }
        if (attr.equals("__NAME__")) {
            return "name";
        }
        if (attr.equals("__ENABLE__")) {
            return "locked-out";
        }
        return attr;
    }
}
