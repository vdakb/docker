-- D:\Project\Bundeskriminalamt12c\InternalIdentityManagement\EmployeePortal\eppAplication\src\main\dbs\qms\api\qms-ses.pks
--
-- Generated for Oracle Database 12c on Fri Apr 24 10:07:06 2020 by Server Generator 10.1.2.93.10

PROMPT Creating Package 'qms$session'
CREATE OR REPLACE PACKAGE qms$session
IS

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
   RETURN VARCHAR2;
  PRAGMA RESTRICT_REFERENCES(package_name, WNDS, WNPS);

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
  -- Purpose  returns the system dependendent process identifier
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  FUNCTION get_systemid
   RETURN VARCHAR2;
  PRAGMA RESTRICT_REFERENCES(get_systemid, WNDS);

  --
  -- Purpose  returns the oracle session identifier
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  FUNCTION get_sessionid
   RETURN INTEGER;
  PRAGMA RESTRICT_REFERENCES(get_sessionid, WNDS);

END qms$session;
/
SHOW ERROR