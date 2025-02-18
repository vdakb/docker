-- D:\Project\Bundeskriminalamt12c\InternalIdentityManagement\EmployeePortal\eppAplication\src\main\dbs\qms\api\qms-prf.pks
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
PROMPT Creating Package Specification 'qms$profile'
CREATE OR REPLACE PACKAGE qms$profile
AS

  -- PL/SQL Specification

  --
  -- Purpose  returns the revision label of this package
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  FUNCTION revision
    RETURN VARCHAR2;
  PRAGMA RESTRICT_REFERENCES(revision, WNDS);

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
  FUNCTION get_profile_value( p_profile_name IN VARCHAR2
                            , p_user_name    IN VARCHAR2
                            )
    RETURN VARCHAR2;
  PRAGMA RESTRICT_REFERENCES(get_profile_value, WNDS);

  --
  -- Purpose: Overloaded function to return all user level profile settings
  --          Profile values are stored in PL/SQL table
  --
  -- Usage    Parameters
  --          p_user_name        - user name the profile value must be looked up for
  --
  -- Remarks  -
  --
  FUNCTION get_user_profiles(p_user_name in VARCHAR2)
    RETURN hil$profile.table_type;

PRAGMA RESTRICT_REFERENCES(qms$profile, WNDS);

-- For some reason, we need to close the package specification here. Designer will remove it
-- when it generates the .pkb file.
END qms$profile;
/
SHOW ERROR