-- D:\Project\Bundeskriminalamt12c\InternalIdentityManagement\EmployeePortal\eppPersistence\install\epp\api\epp-ses.pks
--
-- Generated for Oracle Database 12c on Fri Apr 24 10:07:06 2020 by Server Generator 10.1.2.93.10

PROMPT Creating Package Specification 'epp$session'
CREATE OR REPLACE PACKAGE epp$session
AS

  -- ---------------------------------------------------------------------------
  -- public constants
  -- ---------------------------------------------------------------------------

  SUCCESS                CONSTANT INTEGER      := 0;
  COOKIE_NOT_FOUND       CONSTANT INTEGER      := 1;
  COOKIE_NOT_VALID       CONSTANT INTEGER      := 2;
  TRANSACTION_IDENTIFIER CONSTANT VARCHAR2(30) := 'JEE';

  --
  -- Encapsulate cookie attributes
  --
  TYPE cookie_type IS RECORD (
    name   VARCHAR2(512)
  , value  VARCHAR2(512)
  , domain VARCHAR2(512)
  , path   VARCHAR2(512)
  , expire NUMBER
  );
  --
  -- Encapsulate session attributes
  --
  TYPE row_type IS RECORD (
    cookie          epp$session.cookie_type
  , login_date      DATE
  , expiration_date DATE
  , state           INTEGER
  , user            epp$user.row_type
  );

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
    RETURN VARCHAR2;
  PRAGMA RESTRICT_REFERENCES(revision, WNDS, WNPS);

  --
  -- Purpose  Remove a particular session
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  PROCEDURE delete_by_cookie (p_cookie_value IN VARCHAR2);

  --
  -- Purpose  Store a new session in tracking table
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  PROCEDURE store( p_cookie IN OUT NOCOPY VARCHAR2
                 , p_userid IN OUT NOCOPY VARCHAR2
                 );
  --
  -- Purpose   Returns the internal in memory stored cookie
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  PROCEDURE get_cookie_record(p_cookie IN OUT NOCOPY epp$session.cookie_type);

  --
  -- Purpose   Return the name of application cookie send to client
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  FUNCTION get_cookie_name
    RETURN VARCHAR2;
  PRAGMA RESTRICT_REFERENCES(get_cookie_name, WNDS, WNPS);

  --
  -- Purpose   Returns the domain for the application cookie
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  FUNCTION get_cookie_domain
    RETURN VARCHAR2;
  PRAGMA RESTRICT_REFERENCES(get_cookie_domain, WNDS, WNPS);

  --
  -- Purpose   Return the path to the application cookie
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  FUNCTION get_cookie_path
    RETURN VARCHAR2;
  PRAGMA RESTRICT_REFERENCES(get_cookie_path, WNDS, WNPS);

  --
  -- Purpose   Returns the number days after the application cookie will expire
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  FUNCTION get_cookie_expire
    RETURN NUMBER;
  PRAGMA RESTRICT_REFERENCES(get_cookie_expire, WNDS, WNPS);

  --
  -- Purpose   Returns application cookie
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  PROCEDURE get_cookie( p_login_name IN VARCHAR2
                      , p_cookie     OUT NOCOPY epp$session.cookie_type
                      );

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
                      );

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
    RETURN VARCHAR2;


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
                  );

  --
  -- Purpose   Convenience wrapper for Single Sign Out
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  PROCEDURE signout(p_cookie_value IN VARCHAR2);

  --
  -- Purpose   Retrieve meta data for a session
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  FUNCTION find ( p_cookie       IN OUT NOCOPY epp$session.cookie_type
                , p_login_date      OUT NOCOPY DATE
                , p_expiration_date OUT NOCOPY DATE
                )
    RETURN INTEGER;

  --
  -- Purpose   Set the session identifier
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  PROCEDURE set_identity(p_userid IN VARCHAR2, p_identifier IN VARCHAR2);

  --
  -- Purpose   Returns the stored session identifier
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  FUNCTION get_identity
    RETURN VARCHAR2;
  PRAGMA RESTRICT_REFERENCES(get_identity, WNDS);

  --
  -- Purpose   Clear any previous set identity for session
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  PROCEDURE clear_identity;

  --
  -- Purpose   Returns the user for the passed cookie
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  PROCEDURE get_user( p_cookie       IN VARCHAR2
                    , p_id           OUT NOCOPY NUMBER
                    , p_userid       OUT NOCOPY VARCHAR2
                    , p_organization OUT NOCOPY NUMBER
                    , p_lastname     OUT NOCOPY VARCHAR2
                    , p_firstname    OUT NOCOPY VARCHAR2
                    , p_email        OUT NOCOPY VARCHAR2
                    );

  --
  -- Purpose   Opens a logical RuleFrame transaction
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  PROCEDURE open_transaction( p_userid     IN VARCHAR2
                            , p_identifier IN VARCHAR2 := transaction_identifier
                            );

  --
  -- Purpose   Close a logical RuleFrame transaction
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  PROCEDURE close_transaction(p_identifier IN VARCHAR2 := transaction_identifier);

  --
  -- Purpose   Takes the error message that was last raised off the stack
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  FUNCTION pop_error(p_message OUT VARCHAR2)
    RETURN INTEGER;

END epp$session;
/
SHOW ERROR