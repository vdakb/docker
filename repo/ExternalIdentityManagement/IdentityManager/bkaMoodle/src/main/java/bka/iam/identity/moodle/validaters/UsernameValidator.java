package bka.iam.identity.moodle.validaters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

public class UsernameValidator {
    private static final Logger logger = Logger.getLogger(UsernameValidator.class.getName());

    public boolean validate(final HashMap<String,Object> userDetails, final HashMap<String, ArrayList<HashMap<String, String>>> entitlementDetails, final String attribute) {
        String username = (String) userDetails.get(attribute);
        logger.finest("Validating, " + attribute + ": " + username + " ...");

        boolean isLowerCase = username.toLowerCase().equals(username);
        if (!isLowerCase) {
            logger.fine("Username was found to NOT be lowercase as is required by Moodle, " + attribute + ": " + username + " ...");
        }

        return isLowerCase;
    }
}
