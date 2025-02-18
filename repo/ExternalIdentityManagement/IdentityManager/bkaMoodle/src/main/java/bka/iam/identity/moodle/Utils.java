package bka.iam.identity.moodle;

public class Utils {

    public static final Object translateAttributeValuesOutbound(Object obj) {
        // For some reason, Moodle requires JSON booleans as a number
        if (obj instanceof Boolean) {
            return (boolean)obj ? 1 : 0;
        }

        return obj;
    }

    public static final String translateAttributeName(String attr, MoodleConfiguration config) {
        if (attr.equals("__UID__")) {
            return "id";
        }
        if (attr.equals("__NAME__")) {
            return config.getNameAttr();
        }
        if (attr.equals("__ENABLE__")) {
            return "suspended";
        }
        return attr;
    }
}
