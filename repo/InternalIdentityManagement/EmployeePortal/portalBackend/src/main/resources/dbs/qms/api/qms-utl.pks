-- D:\Project\Bundeskriminalamt12c\InternalIdentityManagement\EmployeePortal\eppAplication\src\main\dbs\qms\api\qms-utl.pks
--
-- Generated for Oracle Database 12c on Fri Apr 24 10:07:06 2020 by Server Generator 10.1.2.93.10

PROMPT Creating Package Specification 'qms$utility'
CREATE OR REPLACE PACKAGE qms$utility
AS

  -- ---------------------------------------------------------------------------
  -- public constants
  -- ---------------------------------------------------------------------------

  STRING_TRUE   CONSTANT VARCHAR2(5) := 'true';
  STRING_FALSE  CONSTANT VARCHAR2(5) := 'false';
  INTEGER_TRUE  CONSTANT INTEGER     := 1;
  INTEGER_FALSE CONSTANT INTEGER     := 0;

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
  -- Purpose  Converts a boolean value in a appropriate string value
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  FUNCTION bool2string(p_value IN BOOLEAN)
    RETURN VARCHAR2;
  PRAGMA RESTRICT_REFERENCES(BOOL2STRING, WNDS, WNPS, RNDS);

  --
  -- Purpose  Converts a boolean value in a appropriate integer value
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  FUNCTION bool2int(p_value IN BOOLEAN)
    RETURN INTEGER;
  PRAGMA RESTRICT_REFERENCES(BOOL2INT, WNDS, WNPS, RNDS);

  --
  -- Purpose  Converts a string value in a appropriate boolean value
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  FUNCTION string2bool (p_value IN VARCHAR2)
    RETURN BOOLEAN;
  PRAGMA RESTRICT_REFERENCES(STRING2BOOL, WNDS, WNPS, RNDS);

  --
  -- Purpose  Converts a integer value in a appropriate boolean value
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  FUNCTION int2bool(p_value INTEGER)
    RETURN BOOLEAN;
  PRAGMA RESTRICT_REFERENCES(INT2BOOL, WNDS, WNPS, RNDS);

  --
  -- Purpose  Converts a number value in a appropriate string value
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  FUNCTION num2string(p_value IN VARCHAR2)
    RETURN NUMBER;
  PRAGMA RESTRICT_REFERENCES(NUM2STRING, WNDS, WNPS, RNDS, RNPS);

  --
  -- Purpose  Converts a hexadecimal digit string to number
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  FUNCTION hex2num(p_hexadigit IN VARCHAR2)
    RETURN NUMBER;
  PRAGMA RESTRICT_REFERENCES(hex2num, WNDS, WNPS, RNDS);

END qms$utility;
/
SHOW ERROR