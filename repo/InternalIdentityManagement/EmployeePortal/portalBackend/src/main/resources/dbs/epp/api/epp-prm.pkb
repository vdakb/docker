-- D:\Project\Bundeskriminalamt12c\InternalIdentityManagement\EmployeePortal\eppPersistence\install\epp\api\epp-rol.pkb
--
-- Generated for Oracle Database 12c on Fri Apr 24 10:07:06 2020 by Server Generator 10.1.2.93.10

PROMPT Creating Package Body 'epp$permission'
CREATE OR REPLACE PACKAGE BODY epp$permission
IS
  -- ---------------------------------------------------------------------------
  -- private constants
  -- ---------------------------------------------------------------------------
  revision_label CONSTANT VARCHAR2(8)  := '1.0.0.0';

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

END epp$permission;
/
SHOW ERROR
