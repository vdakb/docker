-- D:\Project\Bundeskriminalamt12c\InternalIdentityManagement\EmployeePortal\eppAplication\src\main\dbs\qms\api\qms-spf.pks
--
-- Generated for Oracle Database 12c on Fri Apr 24 10:07:06 2020 by Server Generator 10.1.2.93.10

PROMPT Creating Package Specification 'qms$preference'
CREATE OR REPLACE PACKAGE qms$preference
AS

  -- ---------------------------------------------------------------------------
  -- public constants
  -- ---------------------------------------------------------------------------

  START_TAG           CONSTANT VARCHAR2(2)  := '${';
  END_TAG             CONSTANT VARCHAR2(2)  := '}';
  NO_PREFERENCE_FOUND CONSTANT VARCHAR2(50) := 'qms$preference.get_string: No preference found !';

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
  -- Purpose  Add a system preference
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  PROCEDURE add_value( p_group         IN VARCHAR2
                     , p_name          IN VARCHAR2
                     , p_value         IN VARCHAR2
                     , p_display_order IN NUMBER   := 1
                     , p_description   IN VARCHAR2 := NULL
                     );

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

  --
  -- Purpose  Remove a complete preference group
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  PROCEDURE remove_group(p_group IN VARCHAR2);

  --
  -- Purpose  Remove a a value from a preference group
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  PROCEDURE remove_value( p_group      IN VARCHAR2
                        , p_name       IN VARCHAR2
                        );

  --
  -- Purpose  Retrieve a string representation from preference table
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  FUNCTION get_string( p_group      IN VARCHAR2
                     , p_name       IN VARCHAR2
                     )
    RETURN VARCHAR2;
  PRAGMA RESTRICT_REFERENCES(get_string, WNDS, WNPS);

  --
  -- Purpose  Retrieve a boolean representation from preference table
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  FUNCTION get_boolean( p_group      IN VARCHAR2
                      , p_name       IN VARCHAR2
                      )
    RETURN BOOLEAN;
  PRAGMA RESTRICT_REFERENCES(get_boolean, WNDS, WNPS);

  --
  -- Purpose  Retrieve a number representation from preference table
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  FUNCTION get_number( p_group     IN VARCHAR2
                     , p_name      IN VARCHAR2
                     , p_increment IN NUMBER := 0
                     )
    RETURN NUMBER;
  PRAGMA RESTRICT_REFERENCES(get_number, WNDS, WNPS);

  --
  -- Purpose  Retrieve a date representation from preference table
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  FUNCTION get_date( p_group     IN VARCHAR2
                   , p_name      IN VARCHAR2
                   , p_increment IN NUMBER := 0
                   )
    RETURN DATE;
  PRAGMA RESTRICT_REFERENCES(get_date, WNDS, WNPS);
END qms$preference;
/
SHOW ERROR