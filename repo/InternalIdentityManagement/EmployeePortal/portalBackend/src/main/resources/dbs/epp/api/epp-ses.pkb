-- D:\Project\Bundeskriminalamt12c\InternalIdentityManagement\EmployeePortal\eppPersistence\install\epp\api\epp-ses.pkb
--
-- Generated for Oracle Database 12c on Fri Apr 24 10:07:06 2020 by Server Generator 10.1.2.93.10

PROMPT Creating Package Implementation 'epp$session'
CREATE OR REPLACE PACKAGE BODY epp$session
IS

  -- ---------------------------------------------------------------------------
  -- private constants
  -- ---------------------------------------------------------------------------
  revision_label CONSTANT VARCHAR2(8) := '1.0.0.0';

  -- The name to identify this package in error and debugging output
  G_PACKAGE_NAME  CONSTANT  VARCHAR2(30) := 'epp$session';

  -- Name of preference group
  PREFERENCE_GROUP CONSTANT VARCHAR2(30) := 'security';

  G_SESSION epp$session.row_type;

  -- ---------------------------------------------------------------------------
  -- private program units
  -- ---------------------------------------------------------------------------

  --
  -- Purpose  Initialize session interface
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  PROCEDURE initialize;

  --
  -- Purpose  Initialize the username
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  PROCEDURE init_username;

  --
  -- Purpose  Remove all matching entries in tracking table for passed common name
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  PROCEDURE delete_by_name(p_login_name IN VARCHAR2);

  --
  -- Purpose  Restore the session information for a dedicated application cookie
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  PROCEDURE restore_session;

  -- ---------------------------------------------------------------------------
  -- public program units
  -- ---------------------------------------------------------------------------

  --
  -- Purpose  returns the revision label of this package
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  FUNCTION revision
    RETURN VARCHAR2
  IS
  BEGIN
    RETURN revision_label;
  END revision;

  --
  -- Purpose  Initialize session interface
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  PROCEDURE initialize
  IS
    -- The name of method to identify in error and debugging handling
    l_method_name CONSTANT VARCHAR2(30) := 'initialize';
  BEGIN
    -- -------------------------------------
    -- initialize user identity
    init_username;
    -- -------------------------------------
    -- get parameteres from system preferences
    g_session.cookie.name   := qms$preference.get_string(preference_group, 'cookie.name');
    g_session.cookie.domain := qms$preference.get_string(preference_group, 'cookie.domain');
    g_session.cookie.path   := qms$preference.get_string(preference_group, 'cookie.path');
    -- -------------------------------------
    -- The duration after a cookie will expire.
    -- In most cases 10 hours
    g_session.cookie.expire := qms$preference.get_number(preference_group, 'cookie.expires') / 24;
    g_session.state         := SUCCESS;
  END initialize;

  --
  -- Purpose  Initialize the username
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  PROCEDURE init_username
  IS
  BEGIN
    g_session.user.login := NVL( sys_context('userenv', 'client_identifier')
                               , NVL( sys_context('userenv', 'session_user')
                                    , sys_context('userenv', 'current_user')
                                    )
                               );
  END init_username;

  --
  -- Purpose  Remove all matching entries in tracking table for passed common name
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  PROCEDURE delete_by_name(p_login_name IN VARCHAR2)
  IS
    -- The name of method to identify in error and debugging handling
    l_method_name CONSTANT VARCHAR2(30) := 'delete_by_name';
  BEGIN
    -- -------------------------------------
    -- perform operation on table
    DELETE FROM ept_sessions
    WHERE login_name = p_login_name
    ;
  EXCEPTION
    WHEN OTHERS
    THEN
      qms$errors.unhandled_exception(g_package_name, l_method_name);
  END delete_by_name;

  --
  -- Purpose  Remove a particular session
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  PROCEDURE delete_by_cookie(p_cookie_value IN VARCHAR2)
  IS
    -- The name of method to identify in error and debugging handling
    l_method_name CONSTANT VARCHAR2(30) := 'delete_by_cookie';
  BEGIN
    -- -------------------------------------
    -- perform operation on table
    DELETE FROM ept_sessions
    WHERE id = p_cookie_value
    ;
  EXCEPTION
    WHEN OTHERS
    THEN
      qms$errors.unhandled_exception(g_package_name, l_method_name);
  END delete_by_cookie;

  --
  -- Purpose  Store a new session in tracking table
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  PROCEDURE store( p_cookie IN OUT NOCOPY VARCHAR2
                 , p_userid IN OUT NOCOPY VARCHAR2
                 )
  IS
    -- The name of method to identify in error and debugging handling
    l_method_name CONSTANT VARCHAR2(30) := 'store';

    l_expiration NUMBER;
  BEGIN
    -- -------------------------------------
    -- Remove all previuos created session entries
    delete_by_name(p_userid);
    -- -------------------------------------
    -- get expiratioin time for cookie
    l_expiration := get_cookie_expire;
    -- -------------------------------------
    -- create a new entry
    INSERT INTO ept_sessions (
      id
    , login_name
    , login_date
    , expiration_date
    )
    VALUES (
      p_cookie
    , p_userid
    , SYSDATE
    , SYSDATE + l_expiration
    );
    --
    COMMIT;
  EXCEPTION
    WHEN OTHERS
    THEN
      qms$errors.unhandled_exception(g_package_name, l_method_name);
  END store;

  --
  -- Purpose   Returns the internal in memory stored cookie
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  PROCEDURE get_cookie_record(p_cookie IN OUT NOCOPY epp$session.cookie_type)
  IS
  BEGIN
    p_cookie := g_session.cookie;
  END get_cookie_record;

  --
  -- Purpose   Return the name of application cookie send to client
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  FUNCTION get_cookie_name
   RETURN VARCHAR2
  IS
  BEGIN
    RETURN g_session.cookie.name;
  END get_cookie_name;

  --
  -- Purpose   Returns the domain for the application cookie
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  FUNCTION get_cookie_domain
   RETURN VARCHAR2
  IS
  BEGIN
    RETURN g_session.cookie.domain;
  END get_cookie_domain;

  --
  -- Purpose   Return the path to the application cookie
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  FUNCTION get_cookie_path
   RETURN VARCHAR2
  IS
  BEGIN
    RETURN g_session.cookie.path;
  END get_cookie_path;

  -- Purpose   Returns the number days after the application cookie will expire
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  FUNCTION get_cookie_expire
   RETURN NUMBER
  IS
  BEGIN
    RETURN g_session.cookie.expire;
  END get_cookie_expire;

  --
  -- Purpose   Returns application cookie
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  PROCEDURE get_cookie( p_login_name IN VARCHAR2
                      , p_cookie     OUT NOCOPY epp$session.cookie_type
                      )
  IS
    -- The name of method to identify in error and debugging handling
    l_method_name CONSTANT VARCHAR2(30) := 'get_cookie';
  BEGIN
    -- -------------------------------------
    -- Lookup user session cookie
    SELECT id
    INTO   g_session.cookie.value
    FROM   ept_sessions
    WHERE  login_name = p_login_name
    ;
    -- -------------------------------------
    -- store cookie meta data in passed parameters
    p_cookie := g_session.cookie;
  EXCEPTION
    WHEN NO_DATA_FOUND
    THEN
      RAISE;
    WHEN OTHERS
    THEN
      qms$errors.unhandled_exception(g_package_name, l_method_name);
  END get_cookie;

  --
  -- Purpose   Returns application cookie
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  PROCEDURE get_cookie( p_login_name    IN VARCHAR2
                      , p_cookie_name   OUT NOCOPY VARCHAR2
                      , p_cookie_value  OUT NOCOPY VARCHAR2
                      , p_cookie_domain OUT NOCOPY VARCHAR2
                      , p_cookie_path   OUT NOCOPY VARCHAR2
                      )
  IS
    -- The name of method to identify in error and debugging handling
    l_method_name CONSTANT VARCHAR2(30) := 'get_cookie';
  BEGIN
    -- -------------------------------------
    -- Lookup user session cookie
    SELECT id
    INTO   g_session.cookie.value
    FROM   ept_sessions
    WHERE  login_name = p_login_name
    ;
    -- -------------------------------------
    -- store cookie meta data in passed parameters
    p_cookie_name   := g_session.cookie.name;
    p_cookie_value  := g_session.cookie.value;
    p_cookie_domain := g_session.cookie.domain;
    p_cookie_path   := g_session.cookie.path;
  EXCEPTION
    WHEN NO_DATA_FOUND
    THEN
      RAISE;
    WHEN OTHERS
    THEN
      qms$errors.unhandled_exception(g_package_name, l_method_name);
  END get_cookie;

  --
  -- Purpose   Convenience wrapper for Single Sign On
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  FUNCTION signon( p_cookie IN OUT NOCOPY epp$session.cookie_type
                 , p_user      OUT NOCOPY epp$user.row_type
                 )
    RETURN VARCHAR2
  IS
    -- The name of method to identify in error and debugging handling
    l_method_name   CONSTANT VARCHAR2(30) := 'signon';
    --
    l_requested_url VARCHAR2(512);
  BEGIN
    -- -------------------------------------
    -- synchronize user data with LDAP server
    -- epp$user.synchronize(g_session.user);
    -- -------------------------------------
    -- store user meta data in passed parameters
    p_cookie := g_session.cookie;
    p_user   := g_session.user;
    -- -------------------------------------
    -- set global session state to appropriate value
    g_session.state := SUCCESS;
    --
    RETURN l_requested_url;
  END signon;

  --
  -- Purpose   Convenience wrapper for Single Sign On
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  PROCEDURE signon( p_requested_url OUT NOCOPY VARCHAR2
                  , p_userid        OUT NOCOPY NUMBER
                  , p_cookie        OUT NOCOPY VARCHAR2
                  )
  IS
    -- The name of method to identify in error and debugging handling
    l_method_name   CONSTANT VARCHAR2(30) := 'signon';
  BEGIN
    -- -------------------------------------
    -- synchronize user data with LDAP server
    -- epp$user.synchronize(g_session.user);
    -- -------------------------------------
    -- store user meta data in passed parameters
    p_userid  := g_session.user.id;
    -- -------------------------------------
    -- store cookie meta data in passed parameters
    p_cookie  := g_session.cookie.value;
  END SIGNON;

  --
  -- Purpose   Convenience wrapper for Single Sign Out
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  PROCEDURE signout(p_cookie_value IN VARCHAR2)
  IS
    -- The name of method to identify in error and debugging handling
    l_method_name CONSTANT VARCHAR2(30) := 'signout';
  BEGIN
    -- -------------------------------------
    -- perform operation on table
    DELETE FROM ept_sessions
    WHERE  id = p_cookie_value
    ;
  END signout;

  --
  -- Purpose   Retrieve meta data for a session
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  FUNCTION find( p_cookie       IN OUT NOCOPY epp$session.cookie_type
               , p_login_date      OUT NOCOPY DATE
               , p_expiration_date OUT NOCOPY DATE
               )
    RETURN INTEGER
  IS
    -- The name of method to identify in error and debugging handling
    l_method_name CONSTANT VARCHAR2(30) := 'find';
  BEGIN
    IF g_session.cookie.value IS NULL
    OR g_session.cookie.value != p_cookie.value
    THEN
      g_session.cookie.value := p_cookie.value;
      --
      restore_session;
    END IF;
    --
    p_login_date      := g_session.login_date;
    p_expiration_date := g_session.expiration_date;
    --
    RETURN g_session.state;
  EXCEPTION
    WHEN OTHERS
    THEN
      qms$errors.unhandled_exception(g_package_name, l_method_name);
  END FIND;

  --
  -- Purpose   Set the session identifier
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  PROCEDURE set_identity(p_userid IN VARCHAR2, p_identifier IN VARCHAR2)
  IS
  BEGIN
    -- -------------------------------------
    -- set client identifier
    dbms_session.set_identifier(p_userid);
    -- -------------------------------------
    -- set application identifier
    dbms_application_info.set_client_info(p_identifier);
    -- -------------------------------------
    -- initialize user identifier
    init_username;
  END set_identity;

  --
  -- Purpose   Returns the stored session identifier
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  FUNCTION get_identity
    RETURN VARCHAR2
  IS
  BEGIN
    IF g_session.user.login IS NULL
    THEN
      qms$errors.show_message('EPP-01001', '<NULL>');
    END IF;
    --
   RETURN g_session.user.login;
  END get_identity;

  --
  -- Purpose   Clear any previous set identity for session
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  PROCEDURE clear_identity
  IS
  BEGIN
    -- -------------------------------------
    -- clear client identifier
    DBMS_SESSION.clear_identifier;
    -- -------------------------------------
    -- reinitialize user identifier
    init_username;
  END clear_identity;

  --
  -- Purpose  Restore the session information for a dedicated application cookie
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  PROCEDURE restore_session
   IS
    -- The name of method to identify in error and debugging handling
    l_method_name CONSTANT VARCHAR2(30) := 'restore_session';

    l_role_index PLS_INTEGER;
  BEGIN
    -- -------------------------------------
    -- check if passed input paramete are valid
    IF g_session.cookie.value IS NULL
    THEN
      g_session.state := COOKIE_NOT_FOUND;
      RETURN;
    END IF;
    -- -------------------------------------
    -- fetch saved data associated with passed cookie
    SELECT ses.login_date
    ,      ses.expiration_date
    INTO   g_session.login_date
    ,      g_session.expiration_date
    FROM   ept_sessions ses
    WHERE  id = g_session.cookie.value
    ;
    --
    IF g_session.expiration_date < SYSDATE
    THEN
      g_session.state := COOKIE_NOT_VALID;
    ELSE
      g_session.state := SUCCESS;
    END IF;
  EXCEPTION
    WHEN NO_DATA_FOUND
    THEN
      -- -------------------------------------
      -- indicate thats the requested session was not found
      g_session.state := COOKIE_NOT_FOUND;
    WHEN OTHERS
    THEN
      qms$errors.unhandled_exception(g_package_name, l_method_name);
  END restore_session;

  --
  -- Purpose   Returns the user for the passed cookie
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  PROCEDURE get_user( p_cookie       IN VARCHAR2
                    , P_id           OUT NOCOPY NUMBER
                    , p_userid       OUT NOCOPY VARCHAR2
                    , p_organization OUT NOCOPY NUMBER
                    , p_lastname     OUT NOCOPY VARCHAR2
                    , p_firstname    OUT NOCOPY VARCHAR2
                    , p_email        OUT NOCOPY VARCHAR2
                    )
  IS
    -- The name of method to identify in error and debugging handling
    l_method_name CONSTANT VARCHAR2(30) := 'get_user';
  BEGIN
    -- -------------------------------------
    -- check if passed input paramete are valid
    IF p_cookie IS NULL
    THEN
      RETURN;
    END IF;
    -- -------------------------------------
    -- fetch saved data associated with passed cookie
    SELECT usr.usr_key
    ,      usr.usr_login
    ,      usr.act_key
    ,      usr.usr_last_name
    ,      usr.usr_first_name
    ,      usr.usr_email
    INTO   p_id
    ,      p_userid
    ,      p_organization
    ,      p_lastname
    ,      p_firstname
    ,      p_email
    FROM   ept_sessions ses
    ,      igd_oim.usr  usr
    WHERE  ses.id        = p_cookie
    AND    usr.usr_login = ses.login_name
    ;
  EXCEPTION
    WHEN NO_DATA_FOUND
    THEN
      -- -------------------------------------
      -- indicate thats the requested session was not found
      -- p_session.state := COOKIE_NOT_FOUND;
      RETURN;
    WHEN OTHERS
    THEN
      qms$errors.unhandled_exception(g_package_name, l_method_name);
  END get_user;

  --
  -- Purpose   Opens a logical RuleFrame transaction
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  PROCEDURE open_transaction( p_userid     IN VARCHAR2
                            , p_identifier IN VARCHAR2 := transaction_identifier
                            )
  IS
  BEGIN
    set_identity(p_userid, p_identifier);
    --
  --  qms$transaction.open_transaction(p_identifier);
  END open_transaction;

  --
  -- Purpose   Close a logical RuleFrame transaction
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  PROCEDURE close_transaction(p_identifier IN VARCHAR2 := transaction_identifier)
   IS
  BEGIN
  --  qms$transaction.close_transaction(p_identifier);
    --
    clear_identity;
  END close_transaction;

  --
  -- Purpose   Takes the error message that was last raised off the stack
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  FUNCTION pop_error(p_message OUT VARCHAR2)
    RETURN INTEGER
  IS
  BEGIN
    RETURN cg$errors.pop(p_message);
  END pop_error;

BEGIN
  initialize;
END epp$session;
/
SHOW ERROR