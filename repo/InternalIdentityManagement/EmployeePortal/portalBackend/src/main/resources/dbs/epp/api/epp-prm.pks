-- D:\Project\Bundeskriminalamt12c\InternalIdentityManagement\EmployeePortal\eppPersistence\install\epp\api\epp-prm.pks
--
-- Generated for Oracle Database 12c on Fri Apr 24 10:07:06 2020 by Server Generator 10.1.2.93.10

PROMPT Creating Package 'epp$permission'
CREATE OR REPLACE PACKAGE epp$permission
IS
  -- ---------------------------------------------------------------------------
  -- public types
  -- ---------------------------------------------------------------------------

  TYPE row_type IS RECORD (
    name  VARCHAR2(30)
  , admin BOOLEAN
  );

  TYPE type_grant IS TABLE OF
   epp$permission.row_type INDEX BY BINARY_INTEGER;

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
  PRAGMA RESTRICT_REFERENCES(revision, WNDS, WNPS, RNDS, RNPS);

END epp$permission;
/
SHOW ERROR