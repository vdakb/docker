-- D:\Project\Bundeskriminalamt12c\ExternalIdentityManagement\IdentityService\identityIdentifier\src\main\resources\dbs\qms\api\cg_ses.pkb
--
-- Generated for Oracle Database 12c on Tue Mar 30 08:58:19 2010 by Server Generator 10.1.2.93.10

PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating API Package Implementation 'cg$sessions'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
CREATE or REPLACE PACKAGE BODY cg$sessions
IS
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
-- private constants
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/* Revision label of package */
REVISION_LABEL   CONSTANT VARCHAR2( 8) := '2.0.0.0';
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
-- private variables
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
--
-- contains the operating system identifier if running
--
G_SYSTEM_ID  VARCHAR2(9)  := NULL;
--
-- contains the oracle session identifier if running
--
G_SESSION_ID PLS_INTEGER := NULL;

G_SESSION cg$sessions.type_session_record;
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
-- private program units
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
--------------------------------------------------------------------------------
--
-- Name:        init_username
-- Description: Initialize the username
--
-- Parameters:  none
--
--------------------------------------------------------------------------------
PROCEDURE init_username
IS
BEGIN
  g_session.user.name := NVL( sys_context('userenv', 'client_identifier')
                            , NVL( sys_context('userenv', 'session_user')
                                 , sys_context('userenv', 'current_user')
                                 )
                            );
END init_username;
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
-- public program units
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
--------------------------------------------------------------------------------
--
-- Name:        revision
-- Description: Returns the revision label of this package
--
-- Parameters:  none
--
-- Returns:     the revision label
--------------------------------------------------------------------------------
FUNCTION revision
 RETURN VARCHAR2
IS
BEGIN
  RETURN REVISION_LABEL;
END revision;
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
 RETURN VARCHAR2
IS
BEGIN
  -- check if always done
  IF g_system_id IS NULL
  THEN
  --  SELECT spid
  --  INTO   g_system_id
  --  FROM   v$process p
  --  ,      v$session s
  --  WHERE  p.addr   = s.paddr
  --  AND    s.audsid = sys_context('userenv', 'sessionid');
    NULL;
  END IF;
  --
  RETURN g_system_id;
END systemid;
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
 RETURN INTEGER
 IS
BEGIN
  -- check if always done
  IF g_session_id IS NULL
  THEN
    --
    -- due to bug 2466845 bg_job_id is null for a submitted background
    -- process so resolve process identifier in this manner
    g_session_id := TO_NUMBER(NVL(sys_context('userenv', 'bg_job_id')
                                 ,sys_context('userenv', 'fg_job_id')
                                 )
                             );
    --
    -- the value is always 0 if this is not a job (may be a undetected bug)
    -- so conecrt it to a valid system identifier
    IF g_session_id = 0
    THEN
      g_session_id := TO_NUMBER(sys_context('userenv', 'sessionid'));
    END IF;
  END IF;
  --
  RETURN g_session_id;
END sessionid;
--------------------------------------------------------------------------------
--
-- Name:        set_identity
-- Description: Set the session identifier
--
-- Parameters:  p_name the session identifier
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
-- Name:        get_identity
-- Description: Returns the stored session identifier
--
-- Parameters:  none
--
-- Returns:     the stored session identifier
--
--------------------------------------------------------------------------------
FUNCTION get_identity
  RETURN VARCHAR2
IS
BEGIN
  IF g_session.user.name IS NULL
  THEN
    cg$errors.push('User has no permissions to operate.', 'E', 'API', 'OSG-01001', 'cg$sessions.get_identity.username');
    cg$errors.raise_failure;
  END IF;
  --
  RETURN g_session.user.name;
END get_identity;
--------------------------------------------------------------------------------
--
-- Name:        clear_identity
-- Description: Clear any previous set identity for session
--
-- Parameters:  none
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

-- For some reason, we need to close the package specification here.
-- Designer will remove it when it generates the .pkb file.
END cg$sessions;
/
SHOW ERROR