-- D:\Project\Bundeskriminalamt12c\ExternalIdentityManagement\IdentityService\identityIdentifier\src\main\resources\dbs\qms\api\qms_utl.pkb
--
-- Generated for Oracle 10g on Tue Mar 30 08:58:19 2010 by Server Generator 10.1.2.93.10

PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating API Package Implementation 'qms$utility'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
CREATE OR REPLACE PACKAGE BODY qms$utility IS
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
-- private constants
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/* Revision label of package */
REVISION_LABEL               CONSTANT VARCHAR2( 8) := '2.0.0.0';
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
  RETURN VARCHAR2
IS
BEGIN
  RETURN REVISION_LABEL;
END revision;
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
  RETURN VARCHAR2
IS
BEGIN
  IF p_value
  THEN
    RETURN string_true;
  ELSIF NOT p_value
  THEN
    RETURN string_false;
  ELSE
    RETURN NULL;
  END IF;
END bool2string;
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
  RETURN INTEGER
IS
BEGIN
  IF p_value
  THEN
    RETURN integer_true;
  ELSIF NOT p_value
  THEN
    RETURN integer_false;
  ELSE
    RETURN NULL;
  END IF;
END bool2int;
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
  RETURN BOOLEAN
IS
BEGIN
  IF LOWER(p_value) = string_true
  THEN
    RETURN TRUE;
  ELSIF LOWER(p_value)= string_false
  THEN
    RETURN FALSE;
  ELSIF p_value IS NULL
  THEN
    RETURN NULL;
  END IF;
END string2bool;
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
  RETURN BOOLEAN
IS
BEGIN
  IF p_value = integer_true
  THEN
    RETURN TRUE;
  ELSIF p_value = integer_false
  THEN
    RETURN FALSE;
  ELSIF p_value IS NULL
  THEN
    RETURN NULL;
  END IF;
END int2bool;
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
  RETURN NUMBER
IS
BEGIN
  NULL;
END num2string;
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
  RETURN NUMBER
IS
  l_length CONSTANT SMALLINT := length(p_hexadigit);
  l_digit           CHAR(1);
  L_value           SMALLINT;
  L_result          INTEGER := 0;
BEGIN
  FOR i IN 1..l_length
  LOOP
    l_digit := SUBSTR(p_hexadigit, i, 1);
    IF l_digit IN ('A','B','C','D','E','F')
    THEN
      l_value := ASCII(l_digit) - ASCII('A') + 10;
    ELSE
      l_value := TO_NUMBER(l_digit);
    END IF;
    l_result := (l_result * 16) + l_value;
  END LOOP;
  RETURN l_result;
END hex2num;


-- For some reason, we need to close the package specification here.
-- Designer will remove it when it generates the .pkb file.
END qms$utility;
/
SHOW ERROR