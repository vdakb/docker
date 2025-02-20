-- D:\Project\Bundeskriminalamt12c\ExternalIdentityManagement\IdentityService\identityIdentifier\src\main\resources\dbs\qms\api\qms_utl.pks
--
-- Generated for Oracle 10g on Tue Mar 30 08:58:19 2010 by Server Generator 10.1.2.93.10

PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating API Package Specification 'qms$utility'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
CREATE OR REPLACE PACKAGE qms$utility
IS
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
-- public constnats
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  STRING_TRUE   CONSTANT VARCHAR2(5) := 'true';
  STRING_FALSE  CONSTANT VARCHAR2(5) := 'false';
  INTEGER_TRUE  CONSTANT INTEGER     := 1;
  INTEGER_FALSE CONSTANT INTEGER     := 0;
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
  RETURN VARCHAR2;
PRAGMA RESTRICT_REFERENCES(REVISION, WNDS, WNPS, RNDS, RNPS);
--------------------------------------------------------------------------------
--
-- Purpose  Converts a boolean value in a appropriate string value
--
-- Usage    -
--
-- Remarks  -
--
--------------------------------------------------------------------------------
FUNCTION bool2string(p_value IN BOOLEAN)
  RETURN VARCHAR2;
PRAGMA RESTRICT_REFERENCES(BOOL2STRING, WNDS, WNPS, RNDS);
--------------------------------------------------------------------------------
--
-- Purpose  Converts a boolean value in a appropriate integer value
--
-- Usage    -
--
-- Remarks  -
--
--------------------------------------------------------------------------------
FUNCTION bool2int(p_value IN BOOLEAN)
  RETURN INTEGER;
PRAGMA RESTRICT_REFERENCES(BOOL2INT, WNDS, WNPS, RNDS);
--------------------------------------------------------------------------------
--
-- Purpose  Converts a string value in a appropriate boolean value
--
-- Usage    -
--
-- Remarks  -
--
--------------------------------------------------------------------------------
FUNCTION string2bool (p_value IN VARCHAR2)
  RETURN BOOLEAN;
PRAGMA RESTRICT_REFERENCES(STRING2BOOL, WNDS, WNPS, RNDS);
--------------------------------------------------------------------------------
--
-- Purpose  Converts a integer value in a appropriate boolean value
--
-- Usage    -
--
-- Remarks  -
--
--------------------------------------------------------------------------------
FUNCTION int2bool(p_value INTEGER)
  RETURN BOOLEAN;
PRAGMA RESTRICT_REFERENCES(INT2BOOL, WNDS, WNPS, RNDS);
--------------------------------------------------------------------------------
--
-- Purpose  Converts a number value in a appropriate string value
--
-- Usage    -
--
-- Remarks  -
--
--------------------------------------------------------------------------------
FUNCTION num2string(p_value IN VARCHAR2)
  RETURN NUMBER;
PRAGMA RESTRICT_REFERENCES(NUM2STRING, WNDS, WNPS, RNDS, RNPS);
--------------------------------------------------------------------------------
--
-- Purpose  Converts a hexadecimal digit string to number
--
-- Usage    -
--
-- Remarks  -
--
--------------------------------------------------------------------------------
FUNCTION hex2num(p_hexadigit IN VARCHAR2)
  RETURN NUMBER;
PRAGMA RESTRICT_REFERENCES(hex2num, WNDS, WNPS, RNDS);

-- For some reason, we need to close the package specification here.
-- Designer will remove it when it generates the .pks file.
END qms$utility;
/
SHOW ERROR