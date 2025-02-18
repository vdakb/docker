-- D:\Project\Bundeskriminalamt12c\InternalIdentityManagement\EmployeePortal\eppAplication\src\main\dbs\qms\api\qms-prf.pkb
--
-- Generated for Oracle Database 12c on Fri Apr 24 10:07:06 2020 by Server Generator 10.1.2.93.10

/*****************************************************************************************
Purpose  Get profile values.
         Currently, only profiles supported are end user preferences
         End user preferences are stored per user in table usr of Identity Manager.
         This package functions as an interface to the table, and prevents
         redundant SELECT's to this table by holding the current users' settings in
         package variables.

         In a future version, it may implement the generic profiles data structure used in
         APPS/AOL and IAD/CFD
*****************************************************************************************/
PROMPT Creating Package Implementation 'qms$profile'
CREATE OR REPLACE PACKAGE BODY qms$profile IS
-- PL/SQL Block

  revision_label CONSTANT VARCHAR2(10) := '1.0.0.0';

  g_language     VARCHAR2 (50)         := 'en';
  g_fetched      BOOLEAN               := false;
  g_profile      hil$profile.table_type;

  -- ---------------------------------------------------------------------------
  -- private program units
  -- ---------------------------------------------------------------------------

  --
  -- Purpose: Fetch user-level profile values for current user and store value in
  -- PL/SQL table
  --
  PROCEDURE fetch_user_profiles (p_user_name IN VARCHAR2)
  IS
    CURSOR c_usr (b_user_name VARCHAR2)
    IS
      SELECT usr.usr_language
      FROM   igd_oim.usr usr
      WHERE  usr.usr_login = b_user_name
      ;
  BEGIN
    OPEN  c_usr(p_user_name);
    FETCH c_usr
    INTO  g_language
    ;
    IF c_usr%NOTFOUND
    THEN
      -- Current user does not have yet a row in the user
      -- preference table. Do not raise error, we use default settings
      NULL;
    END IF;

    g_profile.delete;
    g_profile(1).name  := 'LANGUAGE';
    g_profile(1).value := g_language;
    g_fetched := true;
  EXCEPTION
    WHEN OTHERS
    THEN
      qms$errors.unhandled_exception('qms$profile.fetch_user_profiles');
  END fetch_user_profiles;

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
  -- Purpose: Return profile value
  --
  -- Usage    Parameters
  --    p_profile_name - name of the profile, for example HIGHLIGHT_REQUIRED_ITEMS.
  --    p_user_name    - user name the profile value must be looked up for
  --
  -- Remarks:
  --    If the user does not have his profile saved, a default value for the profile
  --    will be returned
  --
  FUNCTION get_profile_value ( p_profile_name IN VARCHAR2
                             , p_user_name    IN VARCHAR2
                             )
    RETURN VARCHAR2
  IS
    l_value         VARCHAR2(500);
    l_profile_name  VARCHAR2 (50);
    l_value_found   BOOLEAN       := FALSE;
    l_rowindex      NUMBER  (4,0) := NVL(g_profile.FIRST, 1);
  BEGIN
    IF p_user_name IS NULL
    THEN
      qms$errors.internal_exception('User name must be passed to qms$profile.get_profile_value');
    END IF;

    l_profile_name := upper(p_profile_name);
    IF NOT g_fetched
    THEN
      fetch_user_profiles(p_user_name);
    END IF;

    WHILE NOT l_value_found AND l_rowindex IS NOT NULL
    LOOP
      l_value_found := g_profile(l_rowindex).name = l_profile_name;
      IF NOT l_value_found
      THEN
        l_rowindex := g_profile.NEXT (l_rowindex);
      END IF;
    END LOOP;

    IF l_value_found
    THEN
      l_value := g_profile(l_rowindex).value;
    ELSE
      qms$errors.internal_exception('Invalid profile '||l_profile_name||' passed to qms$profile.get_profile_value');
    END IF;
    return l_value;
  EXCEPTION
    WHEN OTHERS
    THEN
      qms$errors.unhandled_exception('qms$profile.get_profile_value');
  END get_profile_value;

  FUNCTION get_user_profiles(p_user_name IN VARCHAR2)
    RETURN hil$profile.table_type
  IS
  BEGIN
    IF NOT g_fetched
    THEN
      fetch_user_profiles(p_user_name);
    END IF;
    RETURN g_profile;
  EXCEPTION
    WHEN OTHERS
    THEN
      qms$errors.unhandled_exception('qms$profile.get_user_profiles');
  END get_user_profiles;
END qms$profile;
/
SHOW ERROR