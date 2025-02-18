package bka.iam.identity.splunk;

import org.identityconnectors.framework.common.objects.filter.AbstractFilterTranslator;
import org.identityconnectors.framework.common.objects.filter.EqualsFilter;

import java.util.HashMap;
import java.util.Map;

public class SplunkFilterTranslator extends AbstractFilterTranslator<Map<String, String>> {
    @Override
    protected Map<String, String> createEqualsExpression(EqualsFilter filter, boolean not) {
        if (not) { // Nothing in the REST API is capable of this
            return super.createEqualsExpression(filter, not);
        }

        Map<String, String> params = new HashMap<>();
        params.put(
                Utils.translateAttributeName(filter.getName()),
                (String) filter.getAttribute().getValue().get(0));

        return params;
    }
}
