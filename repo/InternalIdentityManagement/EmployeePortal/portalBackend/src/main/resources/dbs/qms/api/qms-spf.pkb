-- D:\Project\Bundeskriminalamt12c\InternalIdentityManagement\EmployeePortal\eppAplication\src\main\dbs\qms\api\qms-spf.pkb
--
-- Generated for Oracle Database 12c on Fri Apr 24 10:07:06 2020 by Server Generator 10.1.2.93.10

PROMPT Creating Package Implementation 'qms$preference'
CREATE OR REPLACE PACKAGE BODY qms$preference
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
                     )
  IS
    -- predefined method name for debugging purpose */
    l_method_name CONSTANT VARCHAR2(30) := 'add_value';
  BEGIN
    INSERT INTO qms_system_preferences (
      group_name
    , tag_name
    , tag_value
    , display_order
    , description
    )
    VALUES (
      p_group
    , p_name
    , p_value
    , p_display_order
    , p_description
    );
  END add_value;

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
                        )
  IS
    -- predefined method name for debugging purpose */
    l_method_name CONSTANT VARCHAR2(30) := 'change_value';
  BEGIN
    UPDATE qms_system_preferences
    SET    tag_value  = p_value
    WHERE  group_name = p_group
    AND    tag_name   = p_name;
  END change_value;

  --
  -- Purpose  Remove a complete preference group
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  PROCEDURE remove_group(p_group IN VARCHAR2)
  IS
    -- predefined method name for debugging purpose */
    l_method_name CONSTANT VARCHAR2(30) := 'remove_group';
  BEGIN
    DELETE FROM qms_system_preferences
    WHERE  group_name = p_group;
  END remove_group;

  --
  -- Purpose  Remove a a value from a preference group
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  PROCEDURE remove_value( p_group      IN VARCHAR2
                        , p_name       IN VARCHAR2
                        )
  IS
    -- predefined method name for debugging purpose */
    l_method_name CONSTANT VARCHAR2(30) := 'remove_value';
  BEGIN
    DELETE FROM qms_system_preferences
    WHERE  group_name = p_group
    AND    tag_name   = p_name;
  END REMOVE_VALUE;

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
    RETURN VARCHAR2
  IS
    -- predefined method name for debugging purpose
    l_method_name CONSTANT VARCHAR2(30) := 'get_string';

    l_not_found BOOLEAN;
    l_value qms_system_preferences.tag_value%TYPE;
    l_group qms_system_preferences.tag_value%TYPE;
    l_name  qms_system_preferences.tag_value%TYPE;
    l_tag_start_pos INTEGER;
    l_tag_end_pos   INTEGER;
    l_dot_pos       INTEGER;
    l_result        VARCHAR2(4000);

    -- Retrieve a value from preference table
    CURSOR c_value( p_group IN VARCHAR2
                  , p_name  IN VARCHAR2
                  )
    IS
      SELECT tag_value
      FROM   qms_system_preferences
      WHERE  UPPER(group_name) = UPPER(p_group)
      AND    UPPER(tag_name)   = UPPER(p_name)
      ;
  BEGIN
    OPEN  c_value(p_group, p_name);
    FETCH c_value INTO l_value;
    l_not_found := c_value%NOTFOUND;
    CLOSE c_value;
    --
    IF l_not_found
    THEN
      l_result := no_preference_found;
    ELSE
      << parse >>
      LOOP
        -- generell exit nothing to parse
        IF l_value IS NULL
        THEN
          EXIT parse;
        END IF;
        --
        l_tag_start_pos := INSTR(l_value, start_tag);
        -- generell exit no substitution
        IF l_tag_start_pos = 0
        THEN
          l_result := l_result||l_value;
          EXIT parse;
        -- substitution necessary in string
        ELSIF l_tag_start_pos != 1
        THEN
          l_result := l_result||substr(l_value, 1, l_tag_start_pos - 1);
          l_value  := substr(l_value, l_tag_start_pos);
        ELSE
          l_tag_start_pos := l_tag_start_pos + 2;
          l_tag_end_pos   := INSTR(l_value, end_tag);
          l_name          := substr(l_value, l_tag_start_pos, l_tag_end_pos - l_tag_start_pos);
          --
          -- separate tag in group and name
          l_dot_pos   := INSTR(l_name, '.');
          IF l_dot_pos = 0
          THEN
            l_group := p_group;
          ELSE
            l_group := SUBSTR(l_name, 1, l_dot_pos - 1);
            l_name  := substr(l_name, l_dot_pos + 1, LENGTH(l_name));
          END IF;
          l_result      := l_result||get_string(l_group, l_name);
          -- remove parsed substition tag
          l_value       := substr(l_value, l_tag_end_pos + 1);
        END IF;
      END LOOP;
    END IF;
    --
    RETURN l_result;
  END get_string;

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
    RETURN BOOLEAN
  IS
    -- predefined method name for debugging purpose
    l_method_name CONSTANT VARCHAR2(30) := 'get_boolean';
  BEGIN
    RETURN qms$utility.string2bool(get_string(p_group, p_name));
  END get_boolean;

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
    RETURN NUMBER
   IS
    -- predefined method name for debugging purpose
   l_method_name CONSTANT VARCHAR2(30) := 'get_number';
  BEGIN
    RETURN TO_NUMBER(get_string(p_group, p_name)) + p_increment;
  END get_number;

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
    RETURN DATE
   IS
    -- predefined method name for debugging purpose
    l_method_name CONSTANT VARCHAR2(30) := 'get_date';
  BEGIN
    RETURN TO_DATE(get_string(p_group, p_name)) + p_increment;
  END get_date;

END qms$preference;
/
SHOW ERROR