package bka.iam.identity.nextcloud;

import bka.iam.identity.nextcloud.model.UserDetails;
import bka.iam.identity.nextcloud.model.apitop.GenericMapResponse;
import bka.iam.identity.nextcloud.model.apitop.GroupsResponse;
import bka.iam.identity.nextcloud.model.apitop.UserResponse;
import bka.iam.identity.nextcloud.model.apitop.UsersResponse;
import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.common.exceptions.ConfigurationException;
import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.identityconnectors.framework.common.exceptions.ConnectorIOException;
import org.identityconnectors.framework.common.objects.*;
import org.identityconnectors.framework.common.objects.filter.FilterTranslator;
import org.identityconnectors.framework.spi.Configuration;
import org.identityconnectors.framework.spi.Connector;
import org.identityconnectors.framework.spi.ConnectorClass;
import org.identityconnectors.framework.spi.operations.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

@ConnectorClass(configurationClass = NextcloudConfiguration.class, displayNameKey = "Splunk")
public class NextcloudConnector implements Connector, CreateOp, UpdateOp, DeleteOp, SearchOp<Map<String, String>>, SyncOp {
    private static final Logger logger = Logger.getLogger(NextcloudConnector.class.getName());
    private NextcloudConfiguration config;
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
        this.config = (NextcloudConfiguration) configuration;
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

        this.conn = new HttpConnection(this.config, "cloud/users", null);

        String body = getBodyPayloadCreate(attrs);
        this.conn.sendRequest("POST", body);
        String rawResponse = getRawResponse();

        JsonParser parser = new JsonParser(rawResponse);

        if (parser.isError()) {
            String errorMsg = "Error while creating a user. " + parser.getError();
            logger.info(errorMsg);
            throw new ConnectorException(errorMsg);
        }

        UserResponse response = parser.as(UserResponse.class);
        Uid uid = buildUidForUserId(response.getOcs().getData().getId());
        logger.info("Successfully created user with id: " + uid.getUidValue());
        return uid;
    }

    private String getBodyPayloadCreate(Set<Attribute> attrs) {
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

                    if (valueObj instanceof GuardedString) {
                        GuardedString pass = (GuardedString) valueObj;
                        final String[] password = new String[1];
                        pass.access(new GuardedString.Accessor() {
                            @Override
                            public void access(char[] clearChars) {
                                password[0] = new String(clearChars); //print the password.
                            }
                        });
                        value = password[0];
                    }

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

        this.conn = new HttpConnection(this.config, "cloud/users/" + uid.getUidValue(), null);
        this.conn.sendRequest("DELETE", null);
        String rawResponse = getRawResponse();

        JsonParser parser = new JsonParser(rawResponse);

        if (parser.isError()) {
            String errorMsg = "Error while deleting a user. " + parser.getError();
            logger.info(errorMsg);
            throw new ConnectorException(errorMsg);
        }

        // When successful, returns an empty data array (so only metadata is present). Therefore nothing to parse...
        logger.info("Successfully deleted user with id: " + uid.getUidValue());
    }

    @Override
    public Uid update(ObjectClass objectClass, Uid uid, Set<Attribute> attrs, OperationOptions operationOptions) {
        logger.fine("update()... objectClass: " + objectClass);

        if (isGroupRequest(attrs)) {
            return doGroupChanges(objectClass, uid, attrs, operationOptions);
        }

        if (isEnableDisableRequest(attrs)) {
            return enableDisableUser(objectClass, uid, attrs, operationOptions);
        }

        this.conn = new HttpConnection(this.config, "cloud/users/" + uid.getUidValue(), null);

        String body = getBodyPayloadUpdate(attrs);
        this.conn.sendRequest("PUT", body);
        String rawResponse = getRawResponse();

        JsonParser parser = new JsonParser(rawResponse);

        if (parser.isError()) {
            String errorMsg = "Error while updating a user. " + parser.getError();
            logger.info(errorMsg);
            throw new ConnectorException(errorMsg);
        }

        if (didUpdateFailPostValidation(uid, attrs)) {
            String errorMsg = "Validation failed after performing an update for user: " + uid.getUidValue();
            logger.fine(errorMsg);
            throw new ConnectorIOException(errorMsg);
        }

        // When successful, no "data" is returned. Just returns "meta", with a statuscode of 100 which is already checked for in Parser.isError()
        logger.info("Successfully update user with id: " + uid.getUidValue());
        return uid;
    }

    private boolean isGroupRequest(Set<Attribute> attrs) {
        for (Attribute attr : attrs) {
            if (attr.getName().equals("__CURRENT_ATTRIBUTES__")) {
                continue; // My testing shows this is only happening when adding a child form value. Therefore, it's only useful for reference and no need to add to payload
            }

            if (attr.getName().equals("groups")) {
                return true;
            }
        }
        return false;
    }

    private Uid doGroupChanges(ObjectClass objectClass, Uid uid, Set<Attribute> attrs, OperationOptions operationOptions) {
        logger.fine("doGroupChanges()... objectClass: " + objectClass + ", uid: " + uid.getUidValue());

        String payload = getBodyPayloadUpdateGroups(attrs);
        this.conn = new HttpConnection(this.config, "cloud/users/" + uid.getUidValue() + "/groups", null);
        this.conn.sendRequest("POST", payload);
        String rawResponse = getRawResponse();

        JsonParser parser = new JsonParser(rawResponse);

        if (parser.isError()) {
            String errorMsg = "Error while making group changes on user. " + parser.getError();
            logger.info(errorMsg);
            throw new ConnectorException(errorMsg);
        }

        // When successful, no "data" is returned. Just returns "meta", with a statuscode of 100 which is already checked for in Parser.isError()
        logger.info("Successfully applied group changes to user with id: " + uid.getUidValue());
        return uid;
    }

    private String getBodyPayloadUpdateGroups(Set<Attribute> attrs) {
        StringBuilder payload = new StringBuilder("");
        boolean hasOne = false;
        for (Attribute attr : attrs) {
            String name = Utils.translateAttributeName(attr.getName());

            if (name.equals("__CURRENT_ATTRIBUTES__")) {
                continue; // My testing shows this is only happening when adding a child form value. Therefore, it's only useful for reference and no need to add to payload
            }

            if (! name.equals("groups")) {
                continue;
            }

            for (Object valueObj : attr.getValue()) {
                if (valueObj == null) {
                    continue; // Unsure if this is necessary, but sometimes my debugger shows a weird null entry at index 0
                }

                try {
                    String value = String.valueOf(valueObj);
                    if (hasOne) {
                        payload.append("&");
                    }
                    payload.append("groupid=" + URLEncoder.encode(value, "UTF-8"));
                    hasOne = true;

                } catch (UnsupportedEncodingException e) {
                    logger.log(Level.SEVERE, "Failed to encode payload with UTF-8. This type of error should be caught in dev", e);
                    throw new ConfigurationException("Failed to encode payload with UTF-8. This type of error should be caught in dev", e); // Want to keep ConnectorException reserved for a Moodle response error.
                }
            }
        }

        String body = payload.toString();
        return body;
    }

    private String getBodyPayloadUpdate(Set<Attribute> attrs) {
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
                        continue; // Unsure if this is necessary, but sometimes my debugger shows a weird null entry at index 0
                    }

                    String value = String.valueOf(valueObj);
                    if (hasOne) {
                        payload.append("&");
                    }
                    payload.append("key=" + URLEncoder.encode(name, "UTF-8"));
                    payload.append("&value=" + URLEncoder.encode(value, "UTF-8"));
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

    private boolean didUpdateFailPostValidation(Uid uid, Set<Attribute> attrs) {
        GenericMapResponse userAsMap = null;

        for (Attribute attr : attrs) {
            if (attr.getName().equals("__CURRENT_ATTRIBUTES__")) {
                continue; // My testing shows this is only happening when adding a child form value. Therefore, it's only useful for reference and no need to add to payload
            }

            String name = Utils.translateAttributeName(attr.getName());
            if (! this.config.isPostUpdateValidateField(name)) {
                continue;
            }

            if (userAsMap == null) {
                JsonParser parser = pullUser(uid.getUidValue());
                userAsMap = parser.as(GenericMapResponse.class);
            }

            for (Object valueObj : attr.getValue()) {

                if (valueObj == null) {
                    continue; // Unsure if this is necessary, but sometimes my debugger shows a weird null entry at index 0
                }

                Object apiValueObj = userAsMap.getOcs().getData().get(name);
                if (! apiValueObj.equals(valueObj)) {
                    /*
                     * Use case where this can happen:
                     * When updating a user for “phone” field, the API will attempt to parse the value. On failure, API still responds with 200 success. However, the phone field will be blank.
                     *   Examples:
                     *     07767007579 fails
                     *     +447767007579 succeeds.
                     */
                    return true;
                }
            }
        }
        return false;
    }

    private Uid enableDisableUser(ObjectClass objectClass, Uid uid, Set<Attribute> attrs, OperationOptions operationOptions) {
        logger.fine("enableDisableUser()... objectClass: " + objectClass);
        String action = isEnable(attrs) ? "enable" : "disable";
        this.conn = new HttpConnection(this.config, "cloud/users/" + uid.getUidValue() + "/" + action, null);

        this.conn.sendRequest("PUT", null);
        String rawResponse = getRawResponse();

        JsonParser parser = new JsonParser(rawResponse);

        if (parser.isError()) {
            String errorMsg = "Error while attempting "+action+" on user. " + parser.getError();
            logger.info(errorMsg);
            throw new ConnectorException(errorMsg);
        }

        // When successful, no "data" is returned. Just returns "meta", with a statuscode of 100 which is already checked for in Parser.isError()
        logger.info("Successfully " + action + "d user with id: " + uid.getUidValue());
        return uid;
    }

    private boolean isEnableDisableRequest(Set<Attribute> attrs) {
        for (Attribute attr : attrs) {
            if (attr.getName().equals("__ENABLE__")) {
                return true;
            }
        }
        return false;
    }

    private boolean isEnable(Set<Attribute> attrs) {
        for (Attribute attr : attrs) {
            if (attr.getName().equals("__ENABLE__")) {
                return (boolean)attr.getValue().get(0);
            }
        }

        String errorMsg = "Error while parsing enable/disable request.";
        logger.info(errorMsg);
        throw new ConnectorException(errorMsg);
    }

    @Override
    public FilterTranslator<Map<String, String>> createFilterTranslator(ObjectClass objectClass, OperationOptions operationOptions) {
        logger.finer("createFilterTranslator()... objectClass: " + objectClass);
        return new NextcloudFilterTranslator();
    }

    @Override
    public void executeQuery(ObjectClass objectClass, Map<String, String> query, ResultsHandler resultsHandler, OperationOptions operationOptions) {
        logger.fine("executeQuery() called with objectClass: " + objectClass.toString());
        if (objectClass.is("__GROUP__")) {
            pullGroups(operationOptions, query, 0, (ConnectorObject obj) -> {
                evalStopCondition(
                        resultsHandler.handle(obj));
            });
        }else if (objectClass.is("__ACCOUNT__")) {
            if (query != null && query.containsKey("userid")) {
                pullUser(operationOptions, query.get("userid"), (ConnectorObject obj) -> {
                    evalStopCondition(
                            resultsHandler.handle(obj));
                });
                return;
            }

            pullUsers(operationOptions, query, 0, (ConnectorObject obj) -> {
                evalStopCondition(
                        resultsHandler.handle(obj));
            });
        }else {
            logger.warning("Unexpected scenario. Didn't expect objectclass: " + objectClass);
        }
        logger.fine("Finished.");
    }

    private void pullGroups(OperationOptions operationOptions, Map<String, String> query, long offset, Consumer<ConnectorObject> callback) {
        logger.fine("pullGroupsFromSplunk()... offset: " + offset);

        Map<String, String> params = new HashMap<>();
        params.put("limit", String.valueOf(this.config.getReconPageSize()));
        params.put("offset", String.valueOf(offset));

        this.conn = new HttpConnection(this.config, "cloud/groups", params);
        this.conn.sendRequest("GET", null);
        String rawResponse = getRawResponse();

        JsonParser parser = new JsonParser(rawResponse);
        GroupsResponse groupsResponse = parser.as(GroupsResponse.class);
        List<String> groups = groupsResponse.getOcs().getData().getGroups();

        for (String group : groups) {
            callback.accept(
                    createConnectorObjectForGroup(group));
        }

        boolean hasNextPage = groups.size() > 0;
        if (hasNextPage) {
            pullGroups(operationOptions, query, offset + this.config.getReconPageSize(), callback);
        }
    }

    private void pullUser(OperationOptions operationOptions, String userid, Consumer<ConnectorObject> callback) {
        logger.fine("pullUser()... userid: " + userid);

        JsonParser parser = pullUser(userid);
        UserResponse user = parser.as(UserResponse.class);
        GenericMapResponse userAsMap = parser.as(GenericMapResponse.class);
        logger.fine("Successfully pulled user with id: " + userid);

        callback.accept(
                createConnectorObjectForUser(user.getOcs().getData(), userAsMap.getOcs().getData(), operationOptions.getAttributesToGet()));
    }

    protected JsonParser pullUser(String userid) {
        this.conn = new HttpConnection(this.config, "cloud/users/" + userid, null);
        this.conn.sendRequest("GET", null);
        String rawResponse = getRawResponse();

        JsonParser parser = new JsonParser(rawResponse);

        if (parser.isError()) {
            String errorMsg = "Error while pulling a user. " + parser.getError();
            logger.info(errorMsg);
            throw new ConnectorException(errorMsg);
        }

        return parser;
    }

    private void pullUsers(OperationOptions operationOptions, Map<String, String> query, long offset, Consumer<ConnectorObject> callback) {
        logger.fine("pullUsers()... offset: " + offset);

        Map<String, String> params = new HashMap<>();
        params.put("limit", String.valueOf(this.config.getReconPageSize()));
        params.put("offset", String.valueOf(offset));

        this.conn = new HttpConnection(this.config, "cloud/users", params);
        this.conn.sendRequest("GET", null);
        String rawResponse = getRawResponse();

        JsonParser parser = new JsonParser(rawResponse);
        UsersResponse response = parser.as(UsersResponse.class);
        List<String> users = response.getOcs().getData().getUsers();

        for (String userid : users) {
            pullUser(operationOptions, userid, callback);
        }

        boolean hasNextPage = users.size() > 0;
        if (hasNextPage) {
            pullUsers(operationOptions, query, offset + this.config.getReconPageSize(), callback);
        }
    }

    @Override
    public void sync(ObjectClass objectClass, SyncToken syncToken, SyncResultsHandler syncResultsHandler, OperationOptions operationOptions) {
        logger.fine("sync() called with objectClass: " + objectClass.toString() + ", syncToken: " + (syncToken == null ? "null" : String.valueOf(syncToken.getValue())));
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

    private final ConnectorObject createConnectorObjectForUser(UserDetails user, Map<String, Object> userAsMap, String[] attrs) {
        Uid uid = buildUidForUserId(user.getId());

        ConnectorObjectBuilder obj = new ConnectorObjectBuilder();
        obj.setObjectClass(ObjectClass.ACCOUNT);
        obj.setName(user.getId());
        obj.setUid(uid);

        for (String attr : attrs) {
            if (attr.equals("__ENABLE__")) {
                obj.addAttribute(attr, user.isEnabled());

            }else if (attr.equals("__UID__") || attr.equals("__NAME__")) {
                // Do nothing because it was already set near the start of the method
            }else if (attr.equals("groups")) {
                String[] groupsArr = Utils.convertToStringArray(user.getGroups());
                obj.addAttribute(attr, groupsArr);
            }else {
                obj.addAttribute(attr,
                        userAsMap.get(attr));
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
