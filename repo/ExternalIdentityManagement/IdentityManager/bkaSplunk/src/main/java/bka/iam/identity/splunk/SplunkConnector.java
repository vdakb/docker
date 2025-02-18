package bka.iam.identity.splunk;

import bka.iam.identity.splunk.model.ErrorResponse;
import bka.iam.identity.splunk.model.User;
import bka.iam.identity.splunk.model.UserResponse;
import org.identityconnectors.framework.common.exceptions.ConfigurationException;
import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.identityconnectors.framework.common.objects.*;
import org.identityconnectors.framework.common.objects.filter.FilterTranslator;
import org.identityconnectors.framework.spi.Configuration;
import org.identityconnectors.framework.spi.Connector;
import org.identityconnectors.framework.spi.ConnectorClass;
import org.identityconnectors.framework.spi.operations.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

@ConnectorClass(configurationClass = SplunkConfiguration.class, displayNameKey = "Splunk")
public class SplunkConnector implements Connector, CreateOp, UpdateOp, DeleteOp, SearchOp<Map<String, String>>, SyncOp {
    private static final Logger logger = Logger.getLogger(SplunkConnector.class.getName());
    private SplunkConfiguration config;
    private HttpConnection conn;
    private boolean isStopCalled = false;

    public void evalStopCondition(boolean keepProcessing) {
        if (!keepProcessing) { // Just in case we ever enter into asynchronous territory, we never let it accidentally set it back
            isStopCalled = true;
        }
    }

    @Override
    public Configuration getConfiguration() {
        logger.finest("getConfiguration()...");
        return this.config;
    }

    @Override
    public void init(Configuration configuration) {
        logger.fine("Initialising...");
        this.config = (SplunkConfiguration) configuration;
    }

    @Override
    public void dispose() {
        logger.finer("dispose()...");
        if (this.conn != null) {
            this.conn.dispose();
        }
    }

    @Override
    public Uid create(ObjectClass objectClass, Set<Attribute> attrs, OperationOptions operationOptions) {
        logger.fine("create()...");

        this.conn = new HttpConnection(this.config, "services/authentication/users", null);

        Set<Attribute> copy = new HashSet<>();
        copy.addAll(attrs);
        copy.add(AttributeBuilder.build("roles", this.config.getBirthrightRole()));

        String body = getBodyPayload(copy);
        this.conn.sendRequest("POST", body);
        String rawResponse = getRawResponse();

        JsonParser parser = new JsonParser(rawResponse);

        if (this.conn.getResponseCode() != 201) { // HTTP 201 Created is the expected good response
            ErrorResponse error = parser.getSplunkError();
            String errorMsg = "Error while creating a user - " + error;
            logger.info(errorMsg);
            throw new ConnectorException(errorMsg);
        }

        UserResponse response = parser.as(UserResponse.class);
        Uid uid = buildUidForUserId(response.getEntry().get(0).getName());
        logger.info("Successfully created user with id: " + uid.getUidValue());
        return uid;
    }

    private String getBodyPayload(Set<Attribute> attrs) {
        StringBuilder payload = new StringBuilder("");
        boolean hasOne = false;
        for (Attribute attr : attrs) {
            if (attr.getName().equals("__CURRENT_ATTRIBUTES__")) {
                continue; // My testing shows this is only happening when adding a child form value. Therefore, it's only useful for reference and no need to add to payload
            }

            try {
                String name = Utils.translateAttributeName(attr.getName());
                for (Object valueObj : attr.getValue()) {

                    if (valueObj == null) {
                        continue; // Unsure if this is necessary
                    }

                    String value = String.valueOf(valueObj);
                    value = URLEncoder.encode(value, "UTF-8");
                    payload.append(
                            (hasOne ? "&" : "") +
                                    URLEncoder.encode(name, "UTF-8") +
                                    "=" + value);
                    hasOne = true;
                }
            } catch (UnsupportedEncodingException e) {
                logger.log(Level.SEVERE, "Failed to encode payload with UTF-8. This type of error should be caught in dev", e);
                throw new ConfigurationException("Failed to encode payload with UTF-8. This type of error should be caught in dev", e); // Want to keep ConnectorException reserved for a Moodle response error.
            }
        }

        String body = payload.toString();
        return body;
    }

    @Override
    public void delete(ObjectClass objectClass, Uid uid, OperationOptions operationOptions) {
        logger.fine("delete()... objectClass: " + objectClass + ", uid: " + uid.getUidValue());

        this.conn = new HttpConnection(this.config, "services/authentication/users/" + uid.getUidValue(), null);
        this.conn.sendRequest("DELETE", null);
        String rawResponse = getRawResponse();

        JsonParser parser = new JsonParser(rawResponse);

        if (this.conn.getResponseCode() != 200) {
            ErrorResponse error = parser.getSplunkError();
            String errorMsg = "Error while deleting a user - " + error;
            logger.info(errorMsg);
            throw new ConnectorException(errorMsg);
        }

        // When successful, returns the equivalent of a get all users response
        UserResponse response = parser.as(UserResponse.class);

        // Note: json null response is expected from Splunk for this request (unless it's an error)
        logger.info("Successfully deleted user with id: " + uid.getUidValue());
    }

    @Override
    public Uid update(ObjectClass objectClass, Uid uid, Set<Attribute> attrs, OperationOptions operationOptions) {
        logger.fine("update()...");

        attrs = transformAttrsForEnableDisable(attrs);

        this.conn = new HttpConnection(this.config, "services/authentication/users/" + uid.getUidValue(), null);

        String body = getBodyPayload(attrs);
        this.conn.sendRequest("POST", body);
        String rawResponse = getRawResponse();

        JsonParser parser = new JsonParser(rawResponse);

        if (this.conn.getResponseCode() != 200) {
            ErrorResponse error = parser.getSplunkError();
            String errorMsg = "Error while updating a user - " + error;
            logger.info(errorMsg);
            throw new ConnectorException(errorMsg);
        }

        // When successful, returns the equivalent of a get all users response
        UserResponse response = parser.as(UserResponse.class);
        logger.info("Successfully update user with id: " + uid.getUidValue());
        return uid;
    }

    //TODO: Delete this method as it doesn't work - Splunk doesn't support disable/enable?
    private Set<Attribute> transformAttrsForEnableDisable(Set<Attribute> attrs) {
        for (Attribute attr : attrs) {
            if (attr.getName().equals("__ENABLE__")) {
                boolean value = (boolean)attr.getValue().get(0);

                AttributeBuilder builder = new AttributeBuilder();
                builder.setName("locked-out");
                builder.addValue(!value);

                Set newAttrs = new HashSet<Attribute>();
                newAttrs.add(builder.build());
                return newAttrs;
            }
        }

        return attrs;
    }

    @Override
    public FilterTranslator<Map<String, String>> createFilterTranslator(ObjectClass objectClass, OperationOptions operationOptions) {
        logger.finer("createFilterTranslator()... objectClass: " + objectClass);
        return new SplunkFilterTranslator();
    }

    @Override
    public void executeQuery(ObjectClass objectClass, Map<String, String> query, ResultsHandler resultsHandler, OperationOptions operationOptions) {
        logger.fine("executeQuery() called with objectClass: " + objectClass.toString());
        if (objectClass.is("__GROUP__")
        ) {
            pullGroupsFromSplunk(operationOptions, query, 0, (ConnectorObject obj) -> {
                evalStopCondition(
                        resultsHandler.handle(obj));
            });
        }else if (objectClass.is("__ACCOUNT__")) {
            pullUsersFromSplunk(operationOptions, query, 0, (ConnectorObject obj) -> {
                evalStopCondition(
                        resultsHandler.handle(obj));
            });
        }else {
            logger.warning("Unexpected scenario. Didn't expect objectclass: " + objectClass);
        }
        logger.fine("Finished.");
    }

    @Override
    public void sync(ObjectClass objectClass, SyncToken syncToken, SyncResultsHandler syncResultsHandler, OperationOptions operationOptions) {
        logger.fine("sync() called with objectClass: " + objectClass.toString() + ", syncToken: " + (syncToken == null ? "null" : String.valueOf(syncToken.getValue())));
    }

    private void pullUsersFromSplunk(OperationOptions operationOptions, Map<String, String> query, long offset, Consumer<ConnectorObject> callback) {
        logger.fine("pullUsersFromSplunk()... offset: " + offset);

        String path = "services/authentication/users";
        if (query != null && query.containsKey("name")) {
            path += "/" + query.remove("name");
        }

        UserResponse response = pullFromSplunkCommon(path, query, offset);

        List<User> users = response.getEntry();
        for (User user : users) {
            callback.accept(
                createConnectorObjectForUser(user, operationOptions.getAttributesToGet()));
        }

        if (response.getPaging().hasNextPage()) {
            pullUsersFromSplunk(operationOptions, query, response.getPaging().getTotalPulled(), callback);
        }
    }

    private void pullGroupsFromSplunk(OperationOptions operationOptions, Map<String, String> query, long offset, Consumer<ConnectorObject> callback) {
        logger.fine("pullGroupsFromSplunk()... offset: " + offset);
        UserResponse response = pullFromSplunkCommon("services/authorization/roles", query, offset); // Groups come back in same data model as User

        List<User> groups = response.getEntry();
        for (User group : groups) {
            callback.accept(
                    createConnectorObjectForGroup(group.getName()));
        }

        if (response.getPaging().hasNextPage()) {
            pullGroupsFromSplunk(operationOptions, query, response.getPaging().getTotalPulled(), callback);
        }
    }

    private UserResponse pullFromSplunkCommon(String path, Map<String, String> query, long offset) {
        Map<String, String> params = new HashMap<>();
        params.put("offset", String.valueOf(offset));

        if (query != null) {
            for (Map.Entry<String, String> entry : query.entrySet()) {
                try {
                    params.put("search", URLEncoder.encode(entry.getKey() + "=\"" + entry.getValue() + "\"", "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    logger.log(Level.WARNING, "Failed to encode parameters with UTF-8. This type of error should be caught in dev", e);
                    // Gracefully continue, because why not
                }
            }
        }

        this.conn = new HttpConnection(this.config, path, params);
        this.conn.sendRequest("GET", null);
        String rawResponse = getRawResponse();
        JsonParser parser = new JsonParser(rawResponse);

        if (this.conn.getResponseCode() != 200) {
            ErrorResponse error = parser.getSplunkError();
            String errorMsg = "Error while retrieving group data - " + error;
            logger.info(errorMsg);
            throw new ConnectorException(errorMsg);
        }

        UserResponse response = parser.as(UserResponse.class); // Groups come back in same data model as User
        return response;
    }

    @Override
    public SyncToken getLatestSyncToken(ObjectClass objectClass) {
        logger.warning("getLatestSyncToken(objectClass:" + objectClass + ") was called, however it is not implemented because Moodle does not support syncTokens.");
        return null;
    }

    private final ConnectorObject createConnectorObjectForGroup(String groupName) {
        ConnectorObjectBuilder obj = new ConnectorObjectBuilder();
        obj.setObjectClass(ObjectClass.GROUP);
        obj.setName(groupName);
        obj.setUid(new Uid(groupName));
        return obj.build();
    }

    private final ConnectorObject createConnectorObjectForUser(User user, String[] attrs) {
        Uid uid = buildUidForUserId(user.getName());

        ConnectorObjectBuilder obj = new ConnectorObjectBuilder();
        obj.setObjectClass(ObjectClass.ACCOUNT);
        obj.setName(user.getName());
        obj.setUid(uid);

        for (String attr : attrs) {
            if (attr.equals("__ENABLE__")) {
                obj.addAttribute(attr, !(boolean)user.getContent().get("locked-out"));

            }else if (attr.equals("__UID__") || attr.equals("__NAME__")) {
                // Do nothing because it was already set near the start of the method
            }else if (attr.equals("roles")) {
                ArrayList<String> rolesList = (ArrayList<String>) user.getContent().get("roles");
                String[] rolesArr = new String[rolesList.size()];
                rolesArr = rolesList.toArray(rolesArr);
                obj.addAttribute(attr,
                        rolesArr);
            }else {
                obj.addAttribute(attr,
                        user.getContent().get(attr));
            }
        }

        return obj.build();
    }

    private String getRawResponse() {
        try {
            return this.conn.getResponse();

        } catch (IOException e) {
            String errorMsg = "Failed to deserialize response from Splunk";
            logger.log(Level.SEVERE, errorMsg, e);
            throw new ConnectorException(errorMsg, e);
        }
    }

    protected static final Uid buildUidForUserId(Object uid) {
        return new Uid(String.valueOf(uid));
    }
}
