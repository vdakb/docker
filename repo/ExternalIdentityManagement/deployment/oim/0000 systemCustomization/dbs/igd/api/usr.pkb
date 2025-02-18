CREATE OR REPLACE PACKAGE BODY igd$usr
IS
  -- ------------------------------------------------------------------
  -- private constants
  -- ------------------------------------------------------------------
  /* Revision label of package */
  REVISION_LABEL   CONSTANT VARCHAR2( 8) := '1.0.0.0';

  -- ------------------------------------------------------------------
  -- private program units
  -- ------------------------------------------------------------------

  -- ------------------------------------------------------------------
  --
  -- Purpose...:
  --   Selects a user identity into the given parameter attribute for
  --   the row given by the user login name.
  --
  -- Usage.....: -
  --
  -- Remarks...: -
  --
  -- Parameters:
  --    p_username     The name of the user to fetch form the database.
  -- ------------------------------------------------------------------
  PROCEDURE sel(p_username IN VARCHAR2, cg$rec IN OUT cg$row_type)
  IS
  BEGIN
    SELECT usr_key
    ,      usr_login
    ,      usr_status
    ,      usr_data_level
    ,      usr_rowver
    INTO   cg$rec.id
    ,      cg$rec.name
    ,      cg$rec.status
    ,      cg$rec.data_level
    ,      cg$rec.version
    FROM   usr
    WHERE  UPPER(usr_login) = UPPER(p_username)
    ;
  END sel;

  -- ------------------------------------------------------------------
  --
  -- Purpose...:
  --   Deletes a user identity for the row given by the user identifier.
  --
  -- Usage.....: -
  --
  -- Remarks...: -
  --
  -- Parameters:
  --    cg%rec     The recored identifying the user.
  -- ------------------------------------------------------------------
  PROCEDURE del(cg$rec IN OUT cg$row_type)
  IS
  BEGIN
    DELETE FROM user_provisioning_attrs
    WHERE  usr_key = cg$rec.id
    ;
    DELETE FROM usr
    WHERE  usr_key = cg$rec.id
    ;
  END del;

  -- ------------------------------------------------------------------
  --
  -- Purpose...:
  --   Deletes any occurence of user identity from the policy
  --   evaluation given by the user record.
  --
  -- Usage.....: -
  --
  -- Remarks...: -
  --
  -- Parameters:
  --    cg%rec     The recored identifying the user.
  -- ------------------------------------------------------------------
  PROCEDURE cat(cg$rec IN OUT cg$row_type)
  IS
  BEGIN
    UPDATE catalog
    SET    approver_user = null
    WHERE  approver_user = cg$rec.id
    ;
    UPDATE catalog
    SET    certifier_user = null
    WHERE  certifier_user = cg$rec.id
    ;
    UPDATE catalog
    SET    fulfillment_user = null
    WHERE  fulfillment_user = cg$rec.id
    ;
  END cat;

  -- ------------------------------------------------------------------
  --
  -- Purpose...:
  --   Deletes any occurence of user identity from the audit trial
  --   given by the user record.
  --
  -- Usage.....: -
  --
  -- Remarks...: -
  --
  -- Parameters:
  --    cg%rec     The recored identifying the user.
  -- ------------------------------------------------------------------
  PROCEDURE upa(cg$rec IN OUT cg$row_type)
  IS
  BEGIN
    -- cleanup user profile audit trial fields
    DELETE FROM upa_fields
    WHERE       upa_usr_key IN (
      SELECT upa_usr_key
      FROM   upa_usr
      WHERE  usr_key = cg$rec.id
    )
    ;
    -- cleanup user profile audit trial
    DELETE FROM upa_usr
    WHERE       usr_key = cg$rec.id
    ;
    -- finally cleanup user profile audit trial
    DELETE FROM upa
    WHERE       usr_key = cg$rec.id;
  END upa;

  -- ------------------------------------------------------------------
  --
  -- Purpose...:
  --   Deletes any occurence of user identity from the users account
  --   instances given by the user record.
  --
  -- Usage.....: -
  --
  -- Remarks...: -
  --
  -- Parameters:
  --    cg%rec     The recored identifying the user.
  -- ------------------------------------------------------------------
  PROCEDURE orc(cg$rec IN OUT cg$row_type)
  IS
  BEGIN
    DELETE ent_assign_delta
    WHERE  oiu_key IN (
      SELECT oiu_key
      FROM   oiu
      WHERE  usr_key = cg$rec.id
    )
    ;
    -- cleanup history of entitlements assigned to user
    DELETE ent_assign_hist
    WHERE  usr_key = cg$rec.id
    ;
    -- finally cleanup entitlements assigned to user
    DELETE ent_assign
    WHERE  usr_key = cg$rec.id
    ;
    -- cleanup user account
    DELETE FROM osi
    WHERE       osi_assigned_to = cg$rec.id
    ;
    DELETE FROM osi
    WHERE       orc_key IN (
      SELECT orc_key
      FROM   orc
      WHERE  usr_key = cg$rec.id
    )
    ;
    -- cleanup user account
    DELETE FROM obi
    WHERE       orc_key IN (
      SELECT orc_key
      FROM   orc
      WHERE  usr_key = cg$rec.id
    )
    ;
    -- cleanup user account
    DELETE FROM oio
    WHERE       orc_key IN (
      SELECT orc_key
      FROM   orc
      WHERE  usr_key = cg$rec.id
    )
    ;
    -- cleanup user account
    DELETE FROM oiu
    WHERE       orc_key IN (
      SELECT orc_key
      FROM   orc
      WHERE  usr_key = cg$rec.id
    )
    ;
    -- finally cleanup users account instances
    DELETE FROM orc
    WHERE  usr_key = cg$rec.id
    ;
  END orc;
  -- ------------------------------------------------------------------
  --
  -- Purpose...:
  --   Deletes any occurence of user identity from the admin role
  --   instances given by the user record.
  --
  -- Usage.....: -
  --
  -- Remarks...: -
  --
  -- Parameters:
  --    cg%rec     The recored identifying the user.
  -- ------------------------------------------------------------------
  PROCEDURE adm(cg$rec IN OUT cg$row_type)
  IS
  BEGIN
    -- finally cleanup users membership instances
    DELETE FROM arm_aud
    WHERE  user_id = cg$rec.id
    ;
    -- finally cleanup users membership instances
    DELETE FROM admin_role_membership
    WHERE  user_id = cg$rec.id
    ;
  END adm;

  -- ------------------------------------------------------------------
  --
  -- Purpose...:
  --   Deletes any occurence of user identity from the role instances
  --   given by the user record.
  --
  -- Usage.....: -
  --
  -- Remarks...: -
  --
  -- Parameters:
  --    cg%rec     The recored identifying the user.
  -- ------------------------------------------------------------------
  PROCEDURE ugp(cg$rec IN OUT cg$row_type)
  IS
  BEGIN
    -- cleanup pending role grants
    DELETE FROM pending_role_grants
    WHERE  usr_key = cg$rec.id
    ;
    -- cleanup membership history
    DELETE FROM usg_revoke_history
    WHERE  usr_key = cg$rec.id
    ;
    -- finally cleanup membership
    DELETE FROM usg
    WHERE  usr_key = cg$rec.id
    ;
  END ugp;

  -- ------------------------------------------------------------------
  --
  -- Purpose...:
  --   Deletes any occurence of user identity from the role instances
  --   given by the user record.
  --
  -- Usage.....: -
  --
  -- Remarks...: -
  --
  -- Parameters:
  --    cg%rec     The recored identifying the user.
  -- ------------------------------------------------------------------
  PROCEDURE org(cg$rec IN OUT cg$row_type)
  IS
  BEGIN
    -- cleanup organization membership
    DELETE FROM org_user_memberships
    WHERE  usr_key = cg$rec.id
    ;
    -- cleanup membership history
    DELETE FROM usg_revoke_history
    WHERE  usr_key = cg$rec.id
    ;
    -- finally cleanup membership
    DELETE FROM usg
    WHERE  usr_key = cg$rec.id
    ;
  END org;

  -- ------------------------------------------------------------------
  --
  -- Purpose...:
  --   Deletes any occurence of user identity from the certifier
  --   instances given by the user record.
  --
  -- Usage.....: -
  --
  -- Remarks...: -
  --
  -- Parameters:
  --    cg%rec     The recored identifying the user.
  -- ------------------------------------------------------------------
  PROCEDURE crt(cg$rec IN OUT cg$row_type)
  IS
  BEGIN
    DELETE FROM arch_cert_action_history
    WHERE  actor_id = cg$rec.id
    ;
    DELETE FROM cert_action_history
    WHERE  actor_id = cg$rec.id
    ;
    DELETE FROM arch_cert_action_history
    WHERE  recipient_id = cg$rec.id
    ;
    DELETE FROM cert_action_history
    WHERE  recipient_id = cg$rec.id
    ;
    DELETE FROM arch_certds_ent_asgn
    WHERE  user_id = cg$rec.id
    ;
    DELETE FROM certds_ent_asgn
    WHERE  user_id = cg$rec.id
    ;
    DELETE FROM arch_certds_user_role_asgn
    WHERE  user_id = cg$rec.id
    ;
    DELETE FROM certds_user_role_asgn
    WHERE  user_id = cg$rec.id
    ;
    DELETE FROM arch_certd_user_acct
    WHERE  user_id = cg$rec.id
    ;
    DELETE FROM certd_user_acct
    WHERE  user_id = cg$rec.id
    ;
    DELETE FROM arch_certd_user
    WHERE  user_id = cg$rec.id
    ;
    DELETE FROM certd_user
    WHERE  user_id = cg$rec.id
    ;

    -- finally cleanup certification user
    DELETE FROM arch_certs_user
    WHERE  iam_user_id = cg$rec.id
    ;
    DELETE FROM certs_user
    WHERE  iam_user_id = cg$rec.id
    ;
  END crt;

  -- ------------------------------------------------------------------
  -- public program units
  -- ------------------------------------------------------------------

  -- ------------------------------------------------------------------
  --
  -- Purpose..: Returns the revision label of this package
  --
  -- Usage....: -
  --
  -- Remarks..: -
  --
  -- ------------------------------------------------------------------
  FUNCTION revision
    RETURN VARCHAR2
  IS
  BEGIN
    RETURN REVISION_LABEL;
  END revision;

  -- ------------------------------------------------------------------
  -- Name:        delete_identity
  --
  -- Purpose...:
  --    Delete a users identity.
  --
  -- Parameters:
  --    p_username     The name of the user to delete.
  -- ------------------------------------------------------------------
  PROCEDURE delete_identity(p_username IN VARCHAR2)
  IS
    cg$usr cg$row_type;
  BEGIN
    dbms_output.put_line('Permanently deleting user ' || p_username || ' from metadata repository.');
    --
    -- verify if an internal user is passed and avoid deletion of those users
    --
    IF (UPPER(p_username) IN ('WEBLOGIC', 'OIMINTERNAL', 'XELSYSADM'))
    THEN
      dbms_output.put_line('User ' || p_username || ' cannot be deleted.');
      return;
    END IF;
    --
    sel( p_username => p_username
       , cg$rec     => cg$usr
       );
    --
    crt(cg$rec => cg$usr);
    org(cg$rec => cg$usr);
    ugp(cg$rec => cg$usr);
    cat(cg$rec => cg$usr);
    orc(cg$rec => cg$usr);
    upa(cg$rec => cg$usr);
    del(cg$rec => cg$usr);
    --
    dbms_output.put_line('User ' || cg$usr.name || ' deleted.');
  EXCEPTION
    WHEN no_data_found
    THEN
      dbms_output.put_line('User ' || p_username || ' cannot be deleted due to it does not exists.');
    WHEN others
    THEN
      rollback;
      raise;
  END delete_identity;

END igd$usr;
/
SHOW ERROR