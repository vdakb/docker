-- D:\Project\Bundeskriminalamt12c\ExternalIdentityManagement\IdentityService\identityIdentifier\src\main\resources\dbs\qms\api\qms_spf.pks
--
-- Generated for Oracle 10g on Tue Mar 30 08:58:19 2010 by Server Generator 10.1.2.93.10

PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating API Package Specification 'qms$preference'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
CREATE OR REPLACE PACKAGE qms$preference
IS
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
-- public constnats
-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  EVAL_START    CONSTANT VARCHAR2(2)  := '${';
  EVAL_STOP     CONSTANT VARCHAR2(2)  := '}';
  NOT_FOUND     CONSTANT VARCHAR2(50) := 'qms$preference.get_string: No preference found !';
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
-- Purpose  Add a system preference
--
-- Usage    -
--
-- Remarks  -
--
--------------------------------------------------------------------------------
PROCEDURE add_value( p_group         IN VARCHAR2
                   , p_name          IN VARCHAR2
                   , p_value         IN VARCHAR2
                   , p_display_order IN NUMBER   := 1
                   , p_description   IN VARCHAR2 := NULL
                   );
--------------------------------------------------------------------------------
--
-- Purpose  Change a system preference
--
-- Usage    -
--
-- Remarks  -
--
PROCEDURE change_value( p_group      IN VARCHAR2
                      , p_name       IN VARCHAR2
                      , p_value      IN VARCHAR2
                      );
--------------------------------------------------------------------------------
--
-- Purpose  Remove a complete preference group
--
-- Usage    -
--
-- Remarks  -
--
--------------------------------------------------------------------------------
PROCEDURE remove_group(p_group IN VARCHAR2);
--------------------------------------------------------------------------------
--
-- Purpose  Remove a value from a preference group
--
-- Usage    -
--
-- Remarks  -
--
--------------------------------------------------------------------------------
PROCEDURE remove_value(p_group IN VARCHAR2, p_name IN VARCHAR2);
--------------------------------------------------------------------------------
--
-- Purpose  Retrieve a string representation from preference table
--
-- Usage    -
--
-- Remarks  -
--
--------------------------------------------------------------------------------
FUNCTION get_string(p_group IN VARCHAR2, p_name IN VARCHAR2)
  RETURN VARCHAR2;
PRAGMA RESTRICT_REFERENCES(get_string, WNDS, WNPS);
--------------------------------------------------------------------------------
--
-- Purpose  Retrieve a boolean representation from preference table
--
-- Usage    -
--
-- Remarks  -
--
--------------------------------------------------------------------------------
FUNCTION get_boolean(p_group IN VARCHAR2, p_name IN VARCHAR2)
  RETURN BOOLEAN;
PRAGMA RESTRICT_REFERENCES(get_boolean, WNDS, WNPS);
--------------------------------------------------------------------------------
--
-- Purpose  Retrieve a number representation from preference table
--
-- Usage    -
--
-- Remarks  -
--
--------------------------------------------------------------------------------
FUNCTION get_number(p_group IN VARCHAR2, p_name IN VARCHAR2, p_increment IN NUMBER := 0)
  RETURN NUMBER;
PRAGMA RESTRICT_REFERENCES(get_number, WNDS, WNPS);
--------------------------------------------------------------------------------
--
-- Purpose  Retrieve a date representation from preference table
--
-- Usage    -
--
-- Remarks  -
--
FUNCTION get_date(p_group IN VARCHAR2, p_name IN VARCHAR2, p_increment IN NUMBER := 0)
  RETURN DATE;
PRAGMA RESTRICT_REFERENCES(get_date, WNDS, WNPS);

-- For some reason, we need to close the package specification here.
-- Designer will remove it when it generates the .pks file.
END qms$preference;
/
SHOW ERROR