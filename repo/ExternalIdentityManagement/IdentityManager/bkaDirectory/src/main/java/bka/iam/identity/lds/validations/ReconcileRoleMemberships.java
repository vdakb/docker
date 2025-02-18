package bka.iam.identity.lds.validations;

import java.util.Set;
import java.util.Map;
import java.util.HashSet;
import java.util.HashMap;
import java.util.ArrayList;

import javax.naming.InvalidNameException;

import javax.naming.ldap.LdapName;

import Thor.API.Operations.tcLookupOperationsIntf;
import Thor.API.Operations.tcITResourceInstanceOperationsIntf;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcColumnNotFoundException;
import Thor.API.Exceptions.tcITResourceNotFoundException;
import Thor.API.Exceptions.tcInvalidLookupException;
import Thor.API.Exceptions.tcInvalidValueException;

import oracle.iam.platform.Platform;

import oracle.iam.identity.usermgmt.vo.User;

import oracle.iam.identity.rolemgmt.vo.Role;

import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.api.UserManagerConstants;

import oracle.iam.identity.rolemgmt.api.RoleManager;
import oracle.iam.identity.rolemgmt.api.RoleManagerConstants;

import oracle.iam.identity.exception.NoSuchRoleException;
import oracle.iam.identity.exception.NoSuchUserException;
import oracle.iam.identity.exception.UserLookupException;
import oracle.iam.identity.exception.RoleGrantException;
import oracle.iam.identity.exception.RoleLookupException;
import oracle.iam.identity.exception.RoleModifyException;
import oracle.iam.identity.exception.RoleGrantRevokeException;
import oracle.iam.identity.exception.ValidationFailedException;
import oracle.iam.identity.exception.SearchKeyNotUniqueException;

import bka.iam.identity.lds.Serialiser;
import bka.iam.identity.lds.ResourceConfig;
import bka.iam.identity.lds.ReconciliationPlugin;

import oracle.hst.foundation.logging.Logger;

public class ReconcileRoleMemberships extends ReconciliationPlugin {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String GM_GROUP_DN            = "distinguishedName";
  private static final String GM_GROUP_NAME          = "Group";
  private static final String GM_MEMBERS             = "Memberships";
  private static final String GM_MEMBERS_KEY         = "user";

  private static final String MAP_KEY_ITRESOURCE_KEY = "IT Resource Key";
  private static final String ITR_CONFIG_ATTR_CACHE  = "Role Membership Cache Lookup";

  private static final String USR_SCHEMA_LDAPDN      = "ldapDN";

  private static final int FAIL_SAFE_MAX_ITERATION   = 10000;

  private UserManager                        userManager;
  private RoleManager                        roleManager;
  private tcLookupOperationsIntf             lookupService;
  private tcITResourceInstanceOperationsIntf itResourceService;
  private String                             cacheLookupName = null;

  private static final Logger LOGGER = Logger.create(ReconcileRoleMemberships.class.getName());

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>ReconcileRoleMemberships</code> adpater that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ReconcileRoleMemberships() {
    // ensure inheritance
    super(LOGGER);
  }

  @SuppressWarnings("unused")
  public boolean validate(final HashMap<String,Object> identity, final HashMap<String, ArrayList<HashMap<String, String>>> entitlement, final String attribute)
    throws UserLookupException, SearchKeyNotUniqueException, InvalidNameException, Exception {

    roleManager       = Platform.getService(RoleManager.class);
    userManager       = Platform.getService(UserManager.class);
    lookupService     = Platform.getService(tcLookupOperationsIntf.class);
    itResourceService = Platform.getService(tcITResourceInstanceOperationsIntf.class);

    String groupName = (String)identity.get(GM_GROUP_NAME);
    String dn = (String)identity.get(GM_GROUP_DN);

    logger.debug("identity.toString() -> " + identity.toString());

    // by not catching errors here, and allowing to be thrown, we are ensuring
    // we do not gracefully skip by preventing further execution, and therefore
    // allowing admins to properly fix.
    String itResourceKeyString = (String)identity.get(MAP_KEY_ITRESOURCE_KEY);
    long   itResourceKey = Long.parseLong(itResourceKeyString);
    retrieveCacheLookupTableName(itResourceKey);

    this.logger.debug("Identifying memberships for group[" + groupName + "]...");

    // -- Parse OIM Name
    String oimPrefix    = participantPrefix(itResourceKey);
    String groupNameOim = oimPrefix + " " + groupName;

    // -- Validate Role Name exists in OIM
    Role role = getRole(groupNameOim);
    if (role == null) {
      return false; // Skip ReconEvent gracefully if not found
    }

    // -- Evaluate user memberships + check in OIM
    String[] users     = parseListOfUsers(groupNameOim, entitlement);
    // -- Retrieve previous user memberships for this group
    String[] lastUsers = getLastMemberships(groupNameOim, this.cacheLookupName);

    //try {
    // -- Effect the membership differences between last recon, and now
    doMemberships(role.getEntityId(), groupNameOim, users, lastUsers);
    try {
      // -- Delete old membership data
      deleteMembershipsMetadata(groupNameOim, this.cacheLookupName);
      // -- Store list in lookup, to be evaluated by event handler
      storeMemberships(groupNameOim, users, this.cacheLookupName);
    }
    catch (tcAPIException | tcInvalidLookupException e) {
      this.logger.error("Unable to storeMemberships for dn[" + dn + "]", e);
      throw e;
    }
    this.logger.debug("Memberships for group[" + groupName + "] reconciliation complete.");
    return true;
  }

  private final String participantPrefix(long itResourceKey)
    throws Exception {

    String oimPrefix = null;
    try {
      oimPrefix = participantRealm(itResourceKey)[1];
    }
    catch (Exception e) {
      // transformations will not capture thrown exceptions, so need to debug
      // visibily here
      logger.error("Unable to getParticipantPrefixs for itResourceKey[" + itResourceKey + "]", e);
      throw e;
    }
    return oimPrefix;
  }

  private final Role getRole(String groupNameOim)
    throws RoleLookupException, ValidationFailedException, RoleModifyException {
    logger.trace("Looking for Roles with OIM-Name[" + groupNameOim + "] against column[" + RoleManagerConstants.ROLE_NAME + "]...");

    // -- Check Role validity
    Set<String> retAttrs = new HashSet<String>();
    retAttrs.add(RoleManagerConstants.ROLE_KEY);

    Role role = null;
    try {
      role = this.roleManager.getDetails(RoleManagerConstants.ROLE_NAME, groupNameOim, retAttrs);
    }
    catch (NoSuchRoleException e) {
      this.logger.debug("No role found with name[" + groupNameOim + "]. Gracefully skipping...");
      return null;

    }
    catch (SearchKeyNotUniqueException e) { // Documentation says this is if more than 1 role is found with search. Not expected to happen with our search...
      this.logger.warn("There was a problem when searching for Role Name[" + groupNameOim + "]. GRACEFULLY skipping, however this means the Particpant source might not be fully in sync with OIM", e);
      return null;
    }
    this.logger.debug("Found Role with ROLE_KEY[" + role.getEntityId() + "] for Name[" + groupNameOim + "]");
    return role;
  }

  private final void deleteMembershipsMetadata(String groupNameOim, String lookupTable)
    throws tcAPIException, tcInvalidLookupException, tcAPIException {

    this.logger.debug("Deleting LastMemberships for groupNameOim[" + groupNameOim + "]...");
    boolean hasMoreEntries = true;
    int     i = 0;

    while (hasMoreEntries && i < FAIL_SAFE_MAX_ITERATION) {
      String lookupKey = membershipLookupKeyForIndex(groupNameOim, i);
      try {
        this.logger.trace("Attempting to remove lookupKey[" + lookupKey + "]...");
        this.lookupService.removeLookupValue(lookupTable, lookupKey);

      }
      catch (tcInvalidValueException e) {
        logger.debug("Finished deleting lookup values for groupNameOim[" + groupNameOim + "], iteration[" + i + "]");
        hasMoreEntries = false;
        break;
      }
      i++;
    }
  }

  private final void storeMemberships(String groupNameOim, String[] userKeys, String lookupTable)
    throws tcAPIException, tcInvalidLookupException {

    String   serialisedJson = Serialiser.serialise(userKeys);
    String[] columnValues   = Serialiser.bulkSplitString(serialisedJson);
    int      i              = 0;
    Map<String, String> map = new HashMap<String, String>();
    for (String columnValue : columnValues) {
      map.put(membershipLookupKeyForIndex(groupNameOim, i), columnValue);
      i++;
    }
    this.lookupService.addBulkLookupValues(lookupTable, map, "", "");
  }

  private final String[] getLastMemberships(String groupNameOim, String lookupTable)
    throws tcAPIException, tcAPIException {

    this.logger.debug("Retrieving LastMemberships for groupNameOim[" + groupNameOim + "]...");
    ArrayList<String> memberships = new ArrayList<String>();
    boolean           hasMoreEntries = true;
    int               i = 0;

    while (hasMoreEntries && i < FAIL_SAFE_MAX_ITERATION) {
      String value = this.lookupService.getDecodedValueForEncodedValue(lookupTable, membershipLookupKeyForIndex(groupNameOim, i));

      if (value == null || value.equals("")) {
        this.logger.debug("Finished checking for values. No value found for groupNameOim[" + groupNameOim + "], iteration[" + i + "] in Lookup[" + lookupTable + "], encode[" + membershipLookupKeyForIndex(groupNameOim, i) + "]");
        hasMoreEntries = false;
        break;
      }
      this.logger.trace("Found value[" + value + "] for groupNameOim[" + groupNameOim + "], iteration[" + i + "]");
      memberships.add(value);
      i++;
    }

    if (memberships.size() == 0) { // No entry ever existed here...
      this.logger.trace("No LastMembership entries found, returning an empty array for groupNameOim[" + groupNameOim + "]");
      return new String[0];
    }

    String serialisedJson = Serialiser.bulkUnsplitString(memberships.toArray(new String[0]));
    this.logger.trace("Serialised JSON[" + serialisedJson + "] for groupNameOim[" + groupNameOim + "]");
    String[] json = Serialiser.deserialise(serialisedJson);
    return json;
  }

  private final String getUserKeyFromDn(String userDn)
    throws UserLookupException, SearchKeyNotUniqueException {

    Set<String> retAttrs = new HashSet<String>();
    retAttrs.add(UserManagerConstants.AttributeName.USER_KEY.toString());

    User user;
    try {
      user = this.userManager.getDetails(USR_SCHEMA_LDAPDN, userDn, retAttrs);
    }
    catch (NoSuchUserException e) {
      return null;
    }
    catch (UserLookupException e) {
      this.logger.error("UNEXPECTED ERROR when trying to identiy username[" + userDn + "] within OIM.", e);
      // so that this error may be seen by admins, and prevent graceful continue
      throw e;
    }
    return user.getEntityId();
  }

  private final String[] parseListOfUsers(final String groupNameOim, final HashMap<String, ArrayList<HashMap<String, String>>> entitlement)
    throws UserLookupException, SearchKeyNotUniqueException {

    final ArrayList<HashMap<String, String>> membersRaw = entitlement.get(GM_MEMBERS);
    this.logger.trace("Parsing raw member list. - membersRaw=" + membersRaw.toString());

    ArrayList<String> memberUIDs = new ArrayList<String>();
    for (HashMap<String, String> memberRaw : membersRaw) {
      String userDn  = removeConcat(memberRaw.get(GM_MEMBERS_KEY));
      String userKey = getUserKeyFromDn(userDn);
      if (userKey != null) {
        memberUIDs.add(userKey);
        this.logger.debug("Found userkey[" + userKey + "] for userDn[" + userDn + "], for membership to Group[" + groupNameOim + "]");
      }
      else {
        this.logger.warn("UserDn[" + userDn + "] was not found in OIM when adding membership to Group[" + groupNameOim + "]. GRACEFULLY skipping, however this means the Particpant source might not be fully in sync with OIM");
      }
    }
    return memberUIDs.toArray(new String[0]);
  }

  /**
   *
   * @param weirdConcat e.g. "102~cn=abc,ou=def,dc=123"
   * @return the right hand side of this concatenation
   */
  private static final String removeConcat(String weirdConcat) {
    return weirdConcat.split("~")[1];
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   relativeDistinguishedName
  /**
   ** Returns the relative Distinguished Name (RDN) from the specified
   ** fullqualified Distinguished Name <code>value</code>.
   **
   ** @param  value              the fullqualified Distinguished Name e.g.
   **                            "cn=abc,ou=def,dc=123".
   **
   ** @return                    the relative Distinguished Name (RDN) e.g.
   **                            "abc".
   **
   ** @throws InvalidNameException if a syntax violation is detected in the
   **                              specified distinguished name
   **                              <code>value</code>.
   */
  public static final String relativeDistinguishedName(final String value)
    throws InvalidNameException {

    final LdapName dn = new LdapName(value);
    return (String)dn.getRdn(dn.size() - 1).getValue();
  }

  /*
     * Business-Logic whiteboard...
     * Okay so we only have 2 possible situations. A new userKey is present, or an old userKey is no longer present
     * Let's just check for both scenarios
     */
  private void doMemberships(String roleKey, String groupNameOim, String[] users, String[] lastUsers)
    throws RoleGrantException, ValidationFailedException, RoleGrantRevokeException {

    this.logger.debug("Doing membership operations for groupNameOim[" + groupNameOim + "]...");

    final Set<String> userListAdd    = new HashSet<String>();
    final Set<String> userListRemove = new HashSet<String>();

    for (String userKey : users) {
      boolean userNeedsAdding = true;
      for (String lastUserKey : lastUsers) {
        if (userKey.equals(lastUserKey)) {
          userNeedsAdding = false;
          break;
        }
      }
      if (userNeedsAdding) {
        userListAdd.add(userKey);
      }
    }

    for (String lastUserKey : lastUsers) {
      boolean userNeedsRemoving = true;

      for (String userKey : users) {
        if (userKey.equals(lastUserKey)) {
          userNeedsRemoving = false;
          break;
        }
      }
      if (userNeedsRemoving) {
        userListRemove.add(lastUserKey);
      }
    }

    this.logger.info("Adding " + userListAdd.size() + " users, Removing " + userListRemove.size() + " users, from OIM Group: " + groupNameOim);
    final Set<String> workingSet = new HashSet<String>();
    if (userListAdd.size() > 0) {
      for (String userKey : userListAdd) {
        try {
          // ensure that no user is attached to the working set
          workingSet.clear();
          workingSet.add(userKey);
          this.roleManager.grantRole(roleKey, workingSet);
        }
        catch (ValidationFailedException e) {
          // This is thrown if membership already exists.
          // Not sure what else it might be thrown for, however we are
          // gracefully skipping this with a warning
          this.logger.warn("Unable to process membership-ADD for userKey[" + userKey + "],OIMGroupName[" + groupNameOim + "],roleKey[" + roleKey + "], Reason[" + e.getMessage() + "]");
        }
        catch (RoleGrantException e) {
          // This is if the "operation fails".
          // Capturing, Logging, and throwing this to ensure attention is
          // brought to administrators
          this.logger.error("Unable to process membership-ADD for OIMGroupName[" + groupNameOim + "] while using userKey[" + userKey + "]", e);
          throw e;
        }
      }
    }
    if (userListRemove.size() > 0) {
      for (String userKey : userListRemove) {
        try {
          // ensure that no previously assign user is attached to the working
          // set
          workingSet.clear();
          // ensure that no previously assign user is attached to the working
          // set
          workingSet.add(userKey);
          this.roleManager.revokeRoleGrant(roleKey, workingSet);
        }
        catch (ValidationFailedException e) {
          // This is thrown if membership already exists.
          // Not sure what else it might be thrown for, however we are
          // gracefully skipping this with a warning
          logger.warn("Unable to process membership-REMOVE for userKey[" + userKey + "],OIMGroupName[" + groupNameOim + "],roleKey[" + roleKey + "], Reason[" + e.getMessage() + "]");
        }
        catch (RoleGrantRevokeException e) {
          // This is if the "operation fails".
          // Capturing, Logging, and throwing this to ensure attention is
          // brought to administrators
          logger.error("Unable to process membership-REMOVALS for OIMGroupName[" + groupNameOim + "] while using userKey[" + userKey + "]", e);
          throw e;
        }
      }
    }
  }

  private void retrieveCacheLookupTableName(long itResourceKey)
    throws tcITResourceNotFoundException, tcAPIException, tcAPIException, tcColumnNotFoundException {

    this.logger.trace("Retrieving cache lookup table name via ITResurceKey: " + itResourceKey);
    this.cacheLookupName = ResourceConfig.configurationValue(itResourceKey, ITR_CONFIG_ATTR_CACHE);
    this.logger.debug("Found cacheLookupName as value[" + cacheLookupName + "]");
  }

  /**
   * This is the centralised method for generating a lookup key for storing membership metadata
   *
   * @param groupNameOim
   * @param index
   * @return
   */
  private static final String membershipLookupKeyForIndex(String groupNameOim, int index) {
    return groupNameOim + "[" + index + "]";
  }
}