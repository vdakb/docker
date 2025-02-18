-- D:\Project\Bundeskriminalamt12c\ExternalIdentityManagement\IdentityService\identityIdentifier\src\main\resources\dbs\qms\api\cg_ses.pks
--
-- Generated for Oracle Database 12c on Tue Mar 30 08:58:19 2010 by Server Generator 10.1.2.93.10

PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating API Package Specification 'cg$sessions'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
CREATE OR REPLACE PACKAGE cg$sessions
IS
  -- Encapsulate cookie attributes */
  TYPE type_cookie_record IS RECORD (
    name   VARCHAR2(128)
  , value  VARCHAR2(128)
  , domain VARCHAR2(128)
  , path   VARCHAR2(128)
  , expire NUMBER
  );

  TYPE type_role_record IS RECORD (
    name VARCHAR2 (30)
  );

  TYPE type_role_table IS TABLE OF
   cg$sessions.type_role_record INDEX BY BINARY_INTEGER;

  -- Encapsulate user attributes
  TYPE type_user_record IS RECORD (
    name   VARCHAR2(30)
  , role   cg$sessions.type_role_table
  );

  -- Encapsulate session attributes
  TYPE type_session_record IS RECORD (
    cookie          cg$sessions.type_cookie_record
  , login_date      DATE
  , expiration_date DATE
  , state           INTEGER
  , user            cg$sessions.type_user_record
  );
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
-- public program units
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
--------------------------------------------------------------------------------
--
-- Name:        revision
-- Description: Returns the revision label
--
-- Parameters:  none
--
-- Returns:     the revision label
--
--------------------------------------------------------------------------------
FUNCTION revision
  RETURN VARCHAR2;
PRAGMA RESTRICT_REFERENCES(revision, WNDS, WNPS);
--------------------------------------------------------------------------------
--
-- Name:        systemid
-- Description: Returns the system dependendent process identifier
--
-- Parameters:  none
--
-- Returns:     the system dependendent process identifier
--
--------------------------------------------------------------------------------
FUNCTION systemid
  RETURN VARCHAR2;
PRAGMA RESTRICT_REFERENCES(systemid, WNDS);
--------------------------------------------------------------------------------
--
-- Name:        sessionid
-- Description: Returns the oracle session identifier
--
-- Parameters:  none
--
-- Returns:     the oracle session identifier
--
--------------------------------------------------------------------------------
FUNCTION sessionid
  RETURN INTEGER;
PRAGMA RESTRICT_REFERENCES(sessionid, WNDS);
--------------------------------------------------------------------------------
--
-- Name:        set_identity
-- Description: Set the session identifier
--
-- Parameters:  p_name the session identifier
--
--------------------------------------------------------------------------------
PROCEDURE set_identity(p_name IN VARCHAR2);
--------------------------------------------------------------------------------
-- Name:        get_identity
-- Description: Returns the stored session identifier
--
-- Parameters:  none
--
-- Returns:     the session identifier
--
--------------------------------------------------------------------------------
FUNCTION get_identity
  RETURN VARCHAR2;
PRAGMA RESTRICT_REFERENCES(get_identity, WNDS);
--------------------------------------------------------------------------------
-- Name:        clear_identity
-- Description: Clear any previous set identity for session
--
-- Parameters:  none
--
--------------------------------------------------------------------------------
PROCEDURE clear_identity;

-- For some reason, we need to close the package specification here.
-- Designer will remove it when it generates the .pks file.
END cg$sessions;
/
SHOW ERROR