package bka.iam.identity.moodle;

import org.identityconnectors.framework.common.objects.filter.AbstractFilterTranslator;
import org.identityconnectors.framework.common.objects.filter.EqualsFilter;

import java.util.HashMap;
import java.util.Map;

public class MoodleFilterTranslator extends AbstractFilterTranslator<Map<String, String>> {
    private MoodleConfiguration config;

    public MoodleFilterTranslator(MoodleConfiguration config) {
        this.config = config;
    }

    @Override
    protected Map<String, String> createEqualsExpression(EqualsFilter filter, boolean not) {
        if (not) { // Nothing in the REST API is capable of this
            return super.createEqualsExpression(filter, not);
        }

        Map<String, String> params = new HashMap<>();
        params.put(
                Utils.translateAttributeName(filter.getName(), this.config),
                (String) filter.getAttribute().getValue().get(0));

        return params;
    }

    @Override
    protected Map<String, String> createAndExpression(Map<String, String> leftExpression, Map<String, String> rightExpression) {
        Map<String, String> combined = new HashMap<>(leftExpression);
        rightExpression.forEach((key, value) ->
                combined.merge(key, value, (oldValue, newValue) ->
                        newValue));

        return combined;
    }
}
