-- D:\Project\Bundeskriminalamt12c\InternalIdentityManagement\EmployeePortal\eppPersistence\install\epp\api\epp-usr.pks
--
-- Generated for Oracle Database 12c on Fri Apr 24 10:07:06 2020 by Server Generator 10.1.2.93.10

PROMPT Creating Package 'epp$user'
CREATE OR REPLACE PACKAGE epp$user
IS

  -- ---------------------------------------------------------------------------
  -- public types
  -- ---------------------------------------------------------------------------

  TYPE row_type IS RECORD (
    id           igd_oim.usr.usr_key%TYPE
  , login        igd_oim.usr.usr_login%TYPE
  , first_name   igd_oim.usr.usr_first_name%TYPE
  , last_name    igd_oim.usr.usr_last_name%TYPE
  , display_name igd_oim.usr.usr_display_name%TYPE
  , email        igd_oim.usr.usr_email%TYPE
  , granted      epp$permission.type_grant
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

END epp$user;
/
SHOW ERROR