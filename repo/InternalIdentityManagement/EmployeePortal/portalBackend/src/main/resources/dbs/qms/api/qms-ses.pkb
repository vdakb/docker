-- D:\Project\Bundeskriminalamt12c\InternalIdentityManagement\EmployeePortal\eppAplication\src\main\dbs\qms\api\qms-ses.pkb
--
-- Generated for Oracle Database 12c on Fri Apr 24 10:07:06 2020 by Server Generator 10.1.2.93.10

PROMPT Creating Package Body 'qms$session'
CREATE OR REPLACE PACKAGE BODY qms$session IS

  -- ---------------------------------------------------------------------------
  -- private constants
  -- ---------------------------------------------------------------------------
  package_label  CONSTANT VARCHAR2(30) := 'qms$session';
  -- Revision label of package
  revision_label CONSTANT VARCHAR2(8)  := '1.0.0.0';

  -- ---------------------------------------------------------------------------
  -- private program units
  -- ---------------------------------------------------------------------------

  /* Contains the operating system identifier if running */
  G_SYSTEM_ID VARCHAR2(9)   := NULL;
  /* Contains the oracle session identifier if running */
  G_SESSION_ID PLS_INTEGER := NULL;

  -- ---------------------------------------------------------------------------
  -- public program units
  -- ---------------------------------------------------------------------------

  --
  -- Purpose  returns the name of this package
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  FUNCTION package_name
   RETURN VARCHAR2
  IS
  BEGIN
    RETURN package_label;
  END package_name;

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
  -- Purpose  returns the system dependendent process identifier
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  FUNCTION get_systemid
   RETURN VARCHAR2
   IS
  BEGIN
    -- check if always done
    IF g_system_id IS NULL
    THEN
      SELECT spid
      INTO   g_system_id
      FROM   v$process p
      ,      v$session s
      WHERE  p.addr   = s.paddr
      AND    s.audsid = sys_context('userenv', 'sessionid');
    END IF;
    --
    RETURN g_system_id;
  END get_systemid;

  --
  -- Purpose  returns the oracle session identifier
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  FUNCTION get_sessionid
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
  END get_sessionid;

END qms$session;
/
SHOW ERROR