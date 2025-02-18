-- D:\Project\Bundeskriminalamt12c\InternalIdentityManagement\EmployeePortal\eppAplication\src\main\dbs\qms\api\qms-utl.pkb
--
-- Generated for Oracle Database 12c on Fri Apr 24 10:07:06 2020 by Server Generator 10.1.2.93.10

PROMPT Creating Package Implementation 'qms$utility'
CREATE OR REPLACE PACKAGE BODY qms$utility
IS

  -- ---------------------------------------------------------------------------
  -- private constants
  -- ---------------------------------------------------------------------------
  revision_label CONSTANT VARCHAR2(8) := '1.0.0.0';

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

  --
  -- Purpose  Converts a boolean value in a appropriate string value
  --
  -- Usage    -
  --
  -- Remarks  -
  --
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

  --
  -- Purpose  Converts a boolean value in a appropriate integer value
  --
  -- Usage    -
  --
  -- Remarks  -
  --
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

  --
  -- Purpose  Converts a string value in a appropriate boolean value
  --
  -- Usage    -
  --
  -- Remarks  -
  --
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

  --
  -- Purpose  Converts a integer value in a appropriate boolean value
  --
  -- Usage    -
  --
  -- Remarks  -
  --
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

  --
  -- Purpose  Converts a number value in a appropriate string value
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  FUNCTION num2string(p_value IN VARCHAR2)
    RETURN NUMBER
  IS
  BEGIN
    NULL;
  END num2string;

  --
  -- Purpose  Converts a hexadecimal digit string to number
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  FUNCTION hex2num(p_hexadigit IN VARCHAR2)
    RETURN NUMBER
  IS
    l_length CONSTANT SMALLINT := length(p_hexadigit);
    l_digit           CHAR(1);
    l_value           SMALLINT;
    l_result          INTEGER := 0;
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

END qms$utility;
/
SHOW ERROR