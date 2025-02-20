-- /Users/dsteding/Project/Bundeskriminalamt12c/ExternalIdentityManagement/IdentityService/governanceBackend/src/main/resources/install/adm/api/igs_ses.pkb
--
-- Generated for Oracle Database 12c on Fri Mar 11 12.47.21 2022 by Server Generator 10.1.2.93.10

PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating API Package Implementation 'igs$session'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
CREATE OR REPLACE PACKAGE BODY igs$session
IS
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
-- private constants
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/* Revision label of package */
REVISION_LABEL   CONSTANT VARCHAR2( 8) := '2.0.0.0';

/* Name of preference group */
PREFERENCE_GROUP CONSTANT VARCHAR2(30) := 'security';
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
-- private variables
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
G_SESSION igs$session.type_session_record;
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
-- private program units
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
--------------------------------------------------------------------------------
--
-- Purpose  Initialize the username
--
-- Usage    -
--
-- Remarks  -
--
--------------------------------------------------------------------------------
PROCEDURE init_username
IS
BEGIN
  g_session.user.data.username := NVL( sys_context('userenv', 'client_identifier')
                                     , NVL( sys_context('userenv', 'session_user')
                                          , sys_context('userenv', 'current_user')
                                          )
                                     );
END init_username;
--------------------------------------------------------------------------------
--
-- Purpose  Initialize session interface
--
-- Usage    -
--
-- Remarks  -
--
--------------------------------------------------------------------------------
PROCEDURE initialize
IS
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
  g_session.cookie.expire := qms$preference.get_number(preference_group, 'cookie.expiry') / 24;
  g_session.state         := SUCCESS;
END initialize;
--------------------------------------------------------------------------------
--
-- Purpose  Remove all matching entries in session tracking table for passed
--          login name.
--
-- Usage    -
--
-- Remarks  -
--
--------------------------------------------------------------------------------
PROCEDURE delete_by_name(p_login_name IN VARCHAR2)
IS
BEGIN
  -- -------------------------------------
  -- perform operation on table
  DELETE FROM igt_sessions
  WHERE login_name = p_login_name
  ;
EXCEPTION
  WHEN OTHERS
  THEN
    qms$errors.unhandled(g_package_name, 'delete_by_name');
END delete_by_name;
--------------------------------------------------------------------------------
--
-- Purpose  Restore the session information for a dedicated application cookie
--
-- Usage    -
--
-- Remarks  -
--
--------------------------------------------------------------------------------
PROCEDURE restore_session
 IS
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
  FROM   igt_sessions ses
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
    qms$errors.unhandled(g_package_name, 'restore_session');
END restore_session;
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
-- public program units
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
--------------------------------------------------------------------------------
--
-- Purpose  Returns the revision label of this package
--
-- Usage    -
--
-- Remarks  -
--
--------------------------------------------------------------------------------
FUNCTION revision
  RETURN VARCHAR2
IS
BEGIN
  RETURN REVISION_LABEL;
END revision;
--------------------------------------------------------------------------------
--
-- Purpose  Remove a particular session
--
-- Usage    -
--
-- Remarks  -
--
--------------------------------------------------------------------------------
PROCEDURE delete_by_cookie(p_cookie_value IN VARCHAR2)
IS
BEGIN
  -- -------------------------------------
  -- perform operation on table
  DELETE FROM igt_sessions
  WHERE id = p_cookie_value
  ;
EXCEPTION
  WHEN OTHERS
  THEN
    qms$errors.unhandled(g_package_name, 'delete_by_cookie');
END delete_by_cookie;
--------------------------------------------------------------------------------
--
-- Purpose  Store a new session in tracking table
--
-- Usage    -
--
-- Remarks  -
--
--------------------------------------------------------------------------------
PROCEDURE store( p_cookie IN OUT NOCOPY VARCHAR2
               , p_userid IN OUT NOCOPY VARCHAR2
               )
IS
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
  INSERT INTO igt_sessions (
    id
  , login_name
  , login_date
  , expiration_date
  ) VALUES (
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
    qms$errors.unhandled(g_package_name, 'store');
END store;
--------------------------------------------------------------------------------
--
-- Purpose   Returns the internal in memory stored cookie
--
-- Usage    -
--
-- Remarks  -
--
--------------------------------------------------------------------------------
PROCEDURE get_cookie_record(p_cookie IN OUT NOCOPY igs$session.type_cookie_record)
IS
BEGIN
  p_cookie := g_session.cookie;
END get_cookie_record;
--------------------------------------------------------------------------------
--
-- Purpose   Return the name of application cookie send to client
--
-- Usage    -
--
-- Remarks  -
--
--------------------------------------------------------------------------------
FUNCTION get_cookie_name
 RETURN VARCHAR2
IS
BEGIN
  RETURN g_session.cookie.name;
END get_cookie_name;
--------------------------------------------------------------------------------
--
-- Purpose   Returns the domain for the application cookie
--
-- Usage    -
--
-- Remarks  -
--
--------------------------------------------------------------------------------
FUNCTION get_cookie_domain
 RETURN VARCHAR2
IS
BEGIN
  RETURN g_session.cookie.domain;
END get_cookie_domain;
--------------------------------------------------------------------------------
--
-- Purpose   Return the path to the application cookie
--
-- Usage    -
--
-- Remarks  -
--
--------------------------------------------------------------------------------
FUNCTION get_cookie_path
 RETURN VARCHAR2
IS
BEGIN
  RETURN g_session.cookie.path;
END get_cookie_path;
--------------------------------------------------------------------------------
--
-- Purpose   Returns the number days after the application cookie will expire
--
-- Usage    -
--
-- Remarks  -
--
--------------------------------------------------------------------------------
FUNCTION get_cookie_expire
 RETURN NUMBER
IS
BEGIN
  RETURN g_session.cookie.expire;
END get_cookie_expire;
--------------------------------------------------------------------------------
--
-- Purpose   Returns application cookie
--
-- Usage    -
--
-- Remarks  -
--
--------------------------------------------------------------------------------
PROCEDURE get_cookie( p_login_name IN VARCHAR2
                    , p_cookie     OUT NOCOPY igs$session.type_cookie_record
                    )
IS
BEGIN
  -- -------------------------------------
  -- Lookup user session cookie
  SELECT id
  INTO   g_session.cookie.value
  FROM   igt_sessions
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
    qms$errors.unhandled(g_package_name, 'get_cookie');
END get_cookie;
--------------------------------------------------------------------------------
--
-- Purpose   Returns application cookie
--
-- Usage    -
--
-- Remarks  -
--
--------------------------------------------------------------------------------
PROCEDURE get_cookie( p_login_name    IN VARCHAR2
                    , p_cookie_name   OUT NOCOPY VARCHAR2
                    , p_cookie_value  OUT NOCOPY VARCHAR2
                    , p_cookie_domain OUT NOCOPY VARCHAR2
                    , p_cookie_path   OUT NOCOPY VARCHAR2
                    )
IS
BEGIN
  -- -------------------------------------
  -- Lookup user session cookie
  SELECT id
  INTO   g_session.cookie.value
  FROM   igt_sessions
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
    qms$errors.unhandled(g_package_name, 'get_cookie');
END get_cookie;
--------------------------------------------------------------------------------
--
-- Purpose   Convenience wrapper for Single Sign On
--
-- Usage    -
--
-- Remarks  -
--
--------------------------------------------------------------------------------
FUNCTION signon( p_cookie IN OUT NOCOPY igs$session.type_cookie_record
               , p_user      OUT NOCOPY igs$user.type_user_record
               )
  RETURN VARCHAR2
IS
  l_requested_url VARCHAR2(512);
BEGIN
  -- -------------------------------------
  -- synchronize user data with LDAP server
  -- igs$user.synchronize(g_session.user);
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
--------------------------------------------------------------------------------
--
-- Purpose   Convenience wrapper for Single Sign On
--
-- Usage    -
--
-- Remarks  -
--
--------------------------------------------------------------------------------
PROCEDURE signon( p_requested_url OUT NOCOPY VARCHAR2
                , p_userid        OUT NOCOPY NUMBER
                , p_cookie        OUT NOCOPY VARCHAR2
                )
IS
BEGIN
  -- -------------------------------------
  -- synchronize user data with LDAP server
  -- igs$user.synchronize(g_session.user);
  -- -------------------------------------
  -- store user meta data in passed parameters
  p_userid  := g_session.user.data.id;
  -- -------------------------------------
  -- store cookie meta data in passed parameters
  p_cookie  := g_session.cookie.value;
END SIGNON;
--------------------------------------------------------------------------------
--
-- Purpose   Convenience wrapper for Single Sign Out
--
-- Usage    -
--
-- Remarks  -
--
--------------------------------------------------------------------------------
PROCEDURE signout(p_cookie_value IN VARCHAR2)
IS
BEGIN
  -- -------------------------------------
  -- perform operation on table
  DELETE FROM igt_sessions
  WHERE  id = p_cookie_value
  ;
END signout;
--------------------------------------------------------------------------------
--
-- Purpose   Retrieve meta data for a session
--
-- Usage    -
--
-- Remarks  -
--
--------------------------------------------------------------------------------
FUNCTION find( p_cookie       IN OUT NOCOPY igs$session.type_cookie_record
             , p_login_date      OUT NOCOPY DATE
             , p_expiration_date OUT NOCOPY DATE
             )
  RETURN INTEGER
IS
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
    qms$errors.unhandled(g_package_name, 'find');
END find;
--------------------------------------------------------------------------------
--
-- Purpose   Set the session identifier
--
-- Usage    -
--
-- Remarks  -
--
--------------------------------------------------------------------------------
PROCEDURE set_identity(p_name IN VARCHAR2)
IS
BEGIN
  -- -------------------------------------
  -- set client identifier
  dbms_session.set_identifier(p_name);
  -- -------------------------------------
  -- initialize user identifier
  init_username;
END set_identity;
--------------------------------------------------------------------------------
--
-- Purpose   Returns the stored session identifier
--
-- Usage    -
--
-- Remarks  -
--
--------------------------------------------------------------------------------
FUNCTION get_identity
  RETURN VARCHAR2
IS
BEGIN
  IF g_session.user.data.username IS NULL
  THEN
    qms$errors.show('IGS-01001', '<NULL>');
  END IF;
  --
  RETURN g_session.user.data.username;
END get_identity;
--------------------------------------------------------------------------------
--
-- Purpose   Clear any previous set identity for session
--
-- Usage    -
--
-- Remarks  -
--
--------------------------------------------------------------------------------
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
--------------------------------------------------------------------------------
--
-- Purpose   Returns the user for the passed cookie
--
-- Usage    -
--
-- Remarks  -
--
--------------------------------------------------------------------------------
PROCEDURE get_user( p_cookie       IN VARCHAR2
                  , P_id           OUT NOCOPY NUMBER
                  , p_userid       OUT NOCOPY VARCHAR2
                  , p_lastname     OUT NOCOPY VARCHAR2
                  , p_firstname    OUT NOCOPY VARCHAR2
                  , p_email        OUT NOCOPY VARCHAR2
                  )
IS
BEGIN
  -- -------------------------------------
  -- check if passed input paramete are valid
  IF p_cookie IS NULL
  THEN
    RETURN;
  END IF;
  -- -------------------------------------
  -- fetch saved data associated with passed cookie
  SELECT usr.id
  ,      usr.username
  ,      usr.lastname
  ,      usr.firstname
  ,      usr.email
  INTO   p_id
  ,      p_userid
  ,      p_lastname
  ,      p_firstname
  ,      p_email
  FROM   igt_sessions ses
  ,      igt_users    usr
  WHERE  ses.id       = p_cookie
  AND    usr.username = ses.login_name
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
    qms$errors.unhandled_exception(g_package_name, 'get_user');
END get_user;
--------------------------------------------------------------------------------
--
-- Purpose   Opens a logical transaction
--
-- Usage    -
--
-- Remarks  -
--
--------------------------------------------------------------------------------
PROCEDURE open_transaction( p_userid    IN VARCHAR2
                          , p_opened_by IN VARCHAR2 := transaction_identifier
 )
 IS
BEGIN
  set_identity(p_userid);
  --
  -- qms$transaction.open_transaction(p_opened_by);
END open_transaction;
--------------------------------------------------------------------------------
--
-- Purpose   Close a logical RuleFrame transaction
--
-- Usage    -
--
-- Remarks  -
--
--------------------------------------------------------------------------------
PROCEDURE close_transaction(p_opened_by IN VARCHAR2 := transaction_identifier)
 IS
BEGIN
  -- qms$transaction.close_transaction(p_opened_by);
  --
  clear_identity;
END close_transaction;
--------------------------------------------------------------------------------
--
-- Purpose   Takes the error message that was last raised off the stack
--
-- Usage    -
--
-- Remarks  -
--
--------------------------------------------------------------------------------
FUNCTION pop_error(p_message OUT VARCHAR2)
  RETURN INTEGER
IS
BEGIN
  RETURN cg$errors.pop(p_message);
END pop_error;

BEGIN
  initialize;
END igs$session;
/
SHOW ERROR