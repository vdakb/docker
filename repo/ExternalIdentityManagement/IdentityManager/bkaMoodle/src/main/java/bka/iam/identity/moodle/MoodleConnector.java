package bka.iam.identity.moodle;

import bka.iam.identity.moodle.model.CreateUserResponse;
import bka.iam.identity.moodle.model.ErrorResponse;
import bka.iam.identity.moodle.model.GetUsersResponse;

import com.google.gson.JsonArray;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.net.URLEncoder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.identityconnectors.framework.common.exceptions.ConfigurationException;
import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.identityconnectors.framework.common.exceptions.UnknownUidException;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.ConnectorObject;
import org.identityconnectors.framework.common.objects.ConnectorObjectBuilder;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.OperationOptions;
import org.identityconnectors.framework.common.objects.ResultsHandler;
import org.identityconnectors.framework.common.objects.SyncDeltaBuilder;
import org.identityconnectors.framework.common.objects.SyncDeltaType;
import org.identityconnectors.framework.common.objects.SyncResultsHandler;
import org.identityconnectors.framework.common.objects.SyncToken;
import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.filter.FilterTranslator;
import org.identityconnectors.framework.spi.Configuration;
import org.identityconnectors.framework.spi.Connector;
import org.identityconnectors.framework.spi.ConnectorClass;
import org.identityconnectors.framework.spi.operations.CreateOp;
import org.identityconnectors.framework.spi.operations.DeleteOp;
import org.identityconnectors.framework.spi.operations.SearchOp;
import org.identityconnectors.framework.spi.operations.SyncOp;
import org.identityconnectors.framework.spi.operations.UpdateOp;

/**
 * Nuances which I noticed during development:
 * Moodle username is subject to a policy. Only alphanumeric characters with lowercase letters, underscore (_), hyphen (-), period (.) and at symbol (@) are allowed.
 */
@ConnectorClass(configurationClass = MoodleConfiguration.class, displayNameKey = "Moodle")
public class MoodleConnector implements Connector, CreateOp, UpdateOp, DeleteOp, SearchOp<Map<String, String>>, SyncOp {
    private static final Logger logger = Logger.getLogger(MoodleConnector.class.getName());
    private static final Map<String, String> PARAMS_GETUSERS_NOFILTER = new HashMap<>();
    static {
        PARAMS_GETUSERS_NOFILTER.put("criteria[0][key]", "");
        PARAMS_GETUSERS_NOFILTER.put("criteria[0][value]", "");
    }

    private MoodleConfiguration config;
    private HttpConnection conn;
    private boolean isStopCalled = false;

    public void evalStopCondition(boolean keepProcessing) {
        if (!keepProcessing) { // Just in case we ever enter into asynchronous territory, we never let it accidentally set it back
            isStopCalled = true;
        }
    }

    @Override
    public void init(Configuration configuration) {
        logger.fine("Initialising...");
        this.config = (MoodleConfiguration) configuration;
        this.isStopCalled = false;
    }

    @Override
    public Configuration getConfiguration() {
        logger.finest("getConfiguration()...");
        return this.config;
    }

    @Override
    public void dispose() {
        logger.finer("dispose()...");
        if (this.conn != null) {
            this.conn.dispose();
        }
    }

    /**
     * @param oclass Note: oclass.is("__ACCOUNT__"): true
     * @param attrs All the data which is passed from the Process Form. e.g. attrs[n].getName() == "lastname", attrs[n].getValue()[0] == "Kolandjian"
     * @param options
     * @return
     */
    @Override
    public Uid create(ObjectClass oclass, Set<Attribute> attrs, OperationOptions options) {
        logger.fine("create()...");

        this.conn = new HttpConnection(this.config, "core_user_create_users", null);
        String body = getBodyPayload(attrs);
        this.conn.sendRequest("POST", body);
        String rawResponse = getRawResponse();

        JsonParser parser = new JsonParser(rawResponse);
        ErrorResponse error = parser.getMoodleError();

        if (error != null) {
            String errorMsg = "Error while creating a user - " + error;
            logger.info(errorMsg);
            throw new ConnectorException(errorMsg);
        }

        /*
         * EXPECTED success RESPONSE FROM MOODLE:
         * [{"id": int, "username": "string"}]
         */
        CreateUserResponse response = parser.asCreateUserReponse().get(0);
        logger.info("User created with id: " + response.getId() + ", username: " + response.getUsername());
        return buildUidForUserId(response.getId());
    }

    /**
     * Helper method created for special use-case of update() method where the uid is required by Moodle, but is not
     * part of the actual attrs set provided by ICF.
     *
     * @param attrs the set of attrs provided by ICF which are to be updated / sent to moodle
     * @param uid the user's uid
     * @return a new set of attrs combining the two
     */
    private String getBodyPayload(Set<Attribute> attrs, Uid uid) {
        Set<Attribute> combined = new HashSet<>(attrs);
        combined.add(uid);
        return getBodyPayload(combined);
    }

    /**
     * For requests to Moodle which have a body (POST requests), we need to create a payload which mimics "application/x-www-form-urlencoded".
     * Currently hardcoded for "users" parameter, however if we ever need to support more endpoints we can change that to a parameter
     *
     * @param attrs the attrs within the users object to be sent to Moodle
     * @return an array of users objects with only 1 entry, in the form of "application/x-www-form-urlencoded"
     */
    private String getBodyPayload(Set<Attribute> attrs) {
        StringBuilder payload = new StringBuilder("");
        boolean hasOne = false;
        for (Attribute attr : attrs) {
            try {
                String name = Utils.translateAttributeName(attr.getName(), this.config);
                String value = String.valueOf(
                        Utils.translateAttributeValuesOutbound(
                                attr.getValue().get(0)));
                value = URLEncoder.encode(value, "UTF-8");
                payload.append(
                        (hasOne ? "&" : "") +
                        URLEncoder.encode("users[0][" + name + "]","UTF-8")  +
                        "=" + value);

            } catch (UnsupportedEncodingException e) {
                logger.log(Level.SEVERE, "Failed to encode payload with UTF-8. This type of error should be caught in dev", e);
                throw new ConfigurationException("Failed to encode payload with UTF-8. This type of error should be caught in dev", e); // Want to keep ConnectorException reserved for a Moodle response error.
            }
            hasOne = true;
        }

        String body = payload.toString();
        return body;
    }

    @Override
    public void delete(ObjectClass objectClass, Uid uid, OperationOptions operationOptions) {
        logger.fine("delete()... objectClass: " + objectClass + ", uid: " + uid.getUidValue());

        this.conn = new HttpConnection(this.config, "core_user_delete_users", null);
        String body = "userids[0]=" + uid.getUidValue();
        this.conn.sendRequest("POST", body);
        String rawResponse = getRawResponse();

        JsonParser parser = new JsonParser(rawResponse);
        ErrorResponse error = parser.getMoodleError();
        JsonArray warning = parser.getWarning();

        if (error != null) {
            String errorMsg = "Error while deleting user - " + error;
            logger.info(errorMsg);

            // Invalid user example (where I already deleted the user): {"exception": "dml_missing_record_exception","errorcode": "invaliduser","message": "Invalid user"}
            if (error.getErrorcode().equals("invaliduser")) {
                throw new UnknownUidException(uid, objectClass);
            }
            throw new ConnectorException(errorMsg);
        }

        if (warning != null && !warning.isEmpty()) {
            // If we reached here, then Moodle must have returned something.
            String msg = "Something completely unexpected occurred. This should be caught in dev, and presents a gap in the development of the Moodle Connector. Was the version of Moodle recently changed / upgraded and now the API behaves differently?";
            logger.log(Level.SEVERE, msg);
            throw new ConfigurationException(msg);
        }

        // Note: json null response is expected from Moodle for this request (unless it's an error)
        logger.info("Successfully deleted user with id: " + uid.getUidValue());
    }

    @Override
    public Uid update(ObjectClass objectClass, Uid uid, Set<Attribute> attrs, OperationOptions operationOptions) {
        logger.fine("update()...");

        attrs = transformAttrsForEnableDisable(attrs);

        this.conn = new HttpConnection(this.config, "core_user_update_users", null);
        String body = getBodyPayload(attrs, uid);
        this.conn.sendRequest("POST", body);
        String rawResponse = getRawResponse();


        JsonParser parser = new JsonParser(rawResponse);
        ErrorResponse error = parser.getMoodleError();
        JsonArray   warning = parser.getWarning(); 

        if (error != null) {
            String errorMsg = "Error while creating a user - " + error;
            logger.info(errorMsg);
            throw new ConnectorException(errorMsg);
        }

        if (warning != null && !warning.isEmpty()) {
            // If we reached here, then Moodle must have returned something.
            String msg = "Something completely unexpected occurred. This should be caught in dev, and presents a gap in the development of the Moodle Connector. Was the version of Moodle recently changed / upgraded and now the API behaves differently?";
            logger.log(Level.SEVERE, msg);
            throw new ConfigurationException(msg);
        }

        // Note: json null response is expected from Moodle for this request (unless it's an error)
        logger.info("User with id: " + uid.getUidValue() + " updated");
        return uid;
    }

    private Set<Attribute> transformAttrsForEnableDisable(Set<Attribute> attrs) {
        for (Attribute attr : attrs) {
            if (attr.getName().equals("__ENABLE__")) {
                boolean value = (boolean)attr.getValue().get(0);

                AttributeBuilder builder = new AttributeBuilder();
                builder.setName("suspended");
                builder.addValue(!value);

                Set newAttrs = new HashSet<Attribute>();
                newAttrs.add(builder.build());
                return newAttrs;
            }
        }

        return attrs;
    }

    /**
     * @param objectClass Notes: objectClass.is("__ACCOUNT__"): true
     * @param operationOptions operationOptions.getAttributesToGet() = String[]
     * @return
     */
    @Override
    public FilterTranslator<Map<String, String>> createFilterTranslator(ObjectClass objectClass, OperationOptions operationOptions) {
        return new MoodleFilterTranslator(this.config);
    }

    /**
     * @param objectClass Notes: objectClass.is("__ACCOUNT__"): true
     * @param query The native query to run. A value of null means "return every instance of the given object class"
     * @param resultsHandler Results should be returned to this handler
     * @param operationOptions Notes: operationOptions.getAttributesToGet() = String[]
     */
    @Override
    public void executeQuery(ObjectClass objectClass, Map<String, String> query, ResultsHandler resultsHandler, OperationOptions operationOptions) {
        logger.fine("executeQuery() called with objectClass: " + objectClass.toString());
        pullUsersFromMoodle(operationOptions, query, (ConnectorObject obj) -> {
            evalStopCondition(
                    resultsHandler.handle(obj));
        });
        logger.fine("Finished.");
    }

    /**
     * @param objectClass Notes: objectClass.is("__ACCOUNT__"): true
     * @param syncToken Whatever value is passed in from the schedule task. Should be null because Moodle has no changelog to support this
     * @param syncResultsHandler Results should be returned to this handler
     * @param operationOptions .getAttributesToGet() = the ReconAttrMap i.e. String["firstname", "__UID__", "email", "__NAME__", "lastname"]
     */
    @Override
    public void sync(ObjectClass objectClass, SyncToken syncToken, SyncResultsHandler syncResultsHandler, OperationOptions operationOptions) {
        logger.fine("sync() called with objectClass: " + objectClass.toString() + ", syncToken: " + (syncToken == null ? "null" : String.valueOf(syncToken.getValue())));

        SyncDeltaBuilder sdBuilder = new SyncDeltaBuilder();
        sdBuilder.setDeltaType(SyncDeltaType.CREATE_OR_UPDATE);
        pullUsersFromMoodle(operationOptions, null, (ConnectorObject obj) -> {
            sdBuilder.setToken(new SyncToken(obj.getUid().getUidValue()));
            sdBuilder.setObject(obj);

            evalStopCondition(
                    syncResultsHandler.handle(sdBuilder.build()));
        });
        logger.fine("Finished.");
    }

    private void pullUsersFromMoodle(OperationOptions operationOptions, Map<String, String> query, Consumer<ConnectorObject> callback) {
        if (this.config.isUseBatchingForRecon() && query != null) { // If query is present, then we must force use of the "core_user_get_users" api
            pullUsersFromMoodleBatch(operationOptions, callback);

        }else {
            pullUsersFromMoodleNoBatch(operationOptions, query, callback);
        }

        if (isStopCalled) {
            logger.info("Stop condition detected. Recon may not have been able to complete.");
        }
    }

    private void pullUsersFromMoodleBatch(OperationOptions operationOptions, Consumer<ConnectorObject> callback) {
        long upperLimit = 1000;

        Map<String, String> params = new HashMap<>();
        params.put("field","id");

        for (int i = 1; i < upperLimit && !isStopCalled; i++) {
            params.put("values[0]", String.valueOf(i));
            this.conn = new HttpConnection(this.config, "core_user_get_users_by_field", params);
            this.conn.sendRequest("GET", null);

            String rawResponse = getRawResponse();
            JsonParser parser = new JsonParser(rawResponse);
            ErrorResponse error = parser.getMoodleError();

            if (error != null) {
                /*
                 * If error details is as follows, this means the user used to exist but is deleted. {
                 *     "exception": "dml_missing_record_exception",
                 *     "errorcode": "invaliduser",
                 *     "message": "Invalid user",
                 * }
                 * Interestingly, the response will actually be successful if an id was requested which never existed before.
                 */
                if (error.getErrorcode().equals("invaliduser")) { // This means a user is deleted and once existed before.
                    logger.fine("Deleted user detected for id: " + i + ".");
                    continue;

                }else {
                    String errorMsg = "Error while retrieving data - " + error;
                    logger.info(errorMsg);
                    throw new ConnectorException(errorMsg); // Do not gracefully continue in this unexpected outcome
                }
            }

            /*
             * Assumptions that make this code valid are:
             * - id's auto-increment.
             * - deleted users ALWAYS respond with an error (rather than an empty user)
             */
            if (parser.isEmptyArray()) {
                // End reached
                logger.info("Upper limit of Moodle ID's detected as: " + i);
                break;
            }

            List<Map<String, Object>> users = parser.asGetUsersByFieldResponse();

            for (Map<String, Object> user : users) {
                if (isStopCalled) {
                    break; // Logging in calling function
                }
                ConnectorObject obj = createConnectorObjectForUser(user, operationOptions.getAttributesToGet());
                callback.accept(obj);
            }
        }
    }

    private static Map<String, String> queryToParamsGetUsers(Map<String, String> query) {
        Map<String, String> map = new HashMap<>();

        int i = 0;
        for (Map.Entry<String, String> entry : query.entrySet()) {
            map.put("criteria["+i+"][key]", entry.getKey());
            map.put("criteria["+i+"][value]", entry.getValue());
            i++;
        }

        return map;
    }

    private void pullUsersFromMoodleNoBatch(OperationOptions operationOptions, Map<String, String> query, Consumer<ConnectorObject> callback) {
        Map<String, String> params = query == null
                ? PARAMS_GETUSERS_NOFILTER
                : queryToParamsGetUsers(query);

        this.conn = new HttpConnection(this.config, "core_user_get_users", params);
        this.conn.sendRequest("GET", null);
        String rawResponse = getRawResponse();

        JsonParser parser = new JsonParser(rawResponse);
        ErrorResponse error = parser.getMoodleError();

        if (error != null) {
            String errorMsg = "Error while retrieving data - " + error;
            logger.info(errorMsg);
            throw new ConnectorException(errorMsg);
        }

        GetUsersResponse response = parser.as(GetUsersResponse.class);
        logger.fine("Response from server: " + response);
        List<Map<String, Object>> users = response.getUsers();

        for (Map<String, Object> user : users) {
            if (isStopCalled) {
                break; // Logging in calling function
            }
            ConnectorObject obj = createConnectorObjectForUser(user, operationOptions.getAttributesToGet());
            callback.accept(obj);
        }
    }

    private final ConnectorObject createConnectorObjectForUser(Map<String, Object> user, String[] attrs) {
        Uid uid = buildUidForUserId(user.get("id"));

        ConnectorObjectBuilder obj = new ConnectorObjectBuilder();
        obj.setObjectClass(ObjectClass.ACCOUNT);
        obj.setName(String.valueOf(user.get(this.config.getNameAttr())));
        obj.setUid(uid);

        for (String attr : attrs) {
            if (attr.equals("__ENABLE__")) {
                obj.addAttribute(attr, !(boolean)user.get("suspended"));

            }else if (attr.equals("__UID__") || attr.equals("__NAME__")) {
                // Do nothing because it was already set near the start of the method
            } else {
                obj.addAttribute(attr,
                        getFieldFromUser(user, attr));
            }
        }

        return obj.build();
    }

    /**
     * Moodle responds with custom fields listed under this element:
     * "customfields": [{
     *     "type": "text",
     *     "value": "1",
     *     "name": "Custom Field",
     *     "shortname": "CustomField1"
     *  },{...etc}]
     * @param user the user model as provided by Moodle
     * @param field the field to search for
     * @return the value for the field, or null if no such field
     */
    private static final Object getFieldFromUser(Map<String, Object> user, String field) {
        if (user.containsKey(field)) {
            return user.get(field);
        }

        if (! user.containsKey("customfields")) {
            return null;
        }

        List<Map<String, Object>> customFields = (List<Map<String, Object>>) user.get("customfields");
        for (Map<String, Object> customField : customFields) {
            String shortname = (String)customField.get("shortname");
            if (shortname.equals(field)) {
                return customField.get("value");
            }
        }

        return null;
    }

    /**
     * Retrieves the latest syncToken from the server. I wonder if OIM actually uses this method and what the use-case is
     * @param objectClass either "__ACCOUNT__" or "__GROUP__"
     * @return A token if synchronization events exist; otherwise null.
     */
    @Override
    public SyncToken getLatestSyncToken(ObjectClass objectClass) {
        logger.warning("getLatestSyncToken(objectClass:" + objectClass + ") was called, however it is not implemented because Moodle does not support syncTokens.");
        return null;
    }

    private String getRawResponse() {
        try {
            return this.conn.getResponse();

        } catch (IOException e) {
            String errorMsg = "Failed to deserialize response from Moodle";
            logger.log(Level.SEVERE, errorMsg, e);
            throw new ConnectorException(errorMsg, e);
        }
    }

    private static final Uid buildUidForUserId(Object uid) {
        return new Uid(String.valueOf(uid));
    }
}
