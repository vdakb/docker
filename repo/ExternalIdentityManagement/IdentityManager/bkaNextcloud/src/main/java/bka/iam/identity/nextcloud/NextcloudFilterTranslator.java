package bka.iam.identity.nextcloud;

import org.identityconnectors.framework.common.objects.filter.AbstractFilterTranslator;
import org.identityconnectors.framework.common.objects.filter.ContainsIgnoreCaseFilter;
import org.identityconnectors.framework.common.objects.filter.EqualsFilter;

import java.util.HashMap;
import java.util.Map;

/**
 * See https://docs.oracle.com/en/middleware/idm/identity-governance/12.2.1.3/omdev/integrating-icf-oracle-identity-manager.html#GUID-11E89B97-D7C3-42BB-BA0C-B16382DF7E6F
 *
 * Based on what is implemented in this class, the most efficient use case for searches in the Job UI is to use the
 * following ICF filter examples:
 *
 * equals("__UID__", "joebloggs")
 * contains("email", "@example.com")
 * contains("displayname", "bloggs")
 */
public class NextcloudFilterTranslator extends AbstractFilterTranslator<Map<String, String>> {
    public static final String USER_SEARCH = "__USERSEARCH__"; // Special keyword which NextcloudConnector class will check for

    /**
     * Nextcloud REST API does not support filtering against specific fields when searching for a field value. Instead,
     * it searches against all fields (of the fields which it supports). For example, if I search for a field value
     * of "joebloggs", Nextcloud will return all users containing the string "joebloggs" within the following fields:
     *      [userid, displayname, email, (maybe more which I haven't discovered in testing...)]
     */
    @Override
    protected Map<String, String> createContainsIgnoreCaseExpression(ContainsIgnoreCaseFilter filter, boolean not) {
        String attr = Utils.translateAttributeName(filter.getName());
        switch (attr) {
            case "userid":
            case "email":
            case "displayname":

                Map<String, String> params = new HashMap<>();
                params.put(
                        USER_SEARCH,
                        (String) filter.getAttribute().getValue().get(0));

                return params;
            default:
                return super.createContainsIgnoreCaseExpression(filter, not);
        }
    }

    /**
     * Nextcloud REST API only really supports userid field for Equals condition
     */
    @Override
    protected Map<String, String> createEqualsExpression(EqualsFilter filter, boolean not) {
        if (not) { // Nothing in the REST API is capable of this
            return super.createEqualsExpression(filter, not);
        }


        if (! Utils.translateAttributeName(filter.getName()).equals("userid")) {
            return super.createEqualsExpression(filter, not);
        }

        // Therefore, only continue with custom API call if using equivalent of
        // equalTo('__UID__', 'xxxxxx')

        Map<String, String> params = new HashMap<>();
        params.put(
                Utils.translateAttributeName(filter.getName()),
                (String) filter.getAttribute().getValue().get(0));

        return params;
    }
}
