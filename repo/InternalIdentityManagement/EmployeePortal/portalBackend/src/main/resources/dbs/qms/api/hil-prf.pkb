-- D:\Project\Bundeskriminalamt12c\InternalIdentityManagement\EmployeePortal\eppAplication\src\main\dbs\qms\api\hil-prf.pkb
--
-- Generated for Oracle Database 12c on Fri Apr 24 10:07:06 2020 by Server Generator 10.1.2.93.10

/*******************************************************************************
Purpose  Interface Package to fetch profile values.

         Currently, only end user preferences supported
         End user preferences are stored per user in table usr of Identity Manager,
         so the only parameters required are the user name and profile name.

         This interface to profiles has a number of additional parameters which
         anticipate on more sophisticated storage of end user preferences and
         profiles in general. In particular, this interface is designed to
         support the generic profiles data structure used in APPS/AOL and
         IAD/CFD, which allows to use profile values at site level, application
         level, user role level and user level.

         This powerful and flexible profile system is likely to be implemented
         in a future version.

         The advantage of having this profiles interface in place right now is
         twofold:
         - The client-side template package libraries (qmslib..) can function
           independent of the underlying table structure used to store the
           profile values. If this structure changes, no modifications
           whatsoever are required in the template package libraries.
         - customers might have their own profiles system in place, and want to
           use this system instead of the Identity Manager user table.
           To use their own system they only need to replace the call to
           qms$profile.get_profile_value in function get_profile_value with a
           call to their own function which uses their own data structure.

*******************************************************************************/
PROMPT Creating Package Implementation 'hil$profile'
CREATE OR REPLACE PACKAGE BODY hil$profile
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
    RETURN REVISION_LABEL;
  END revision;

  FUNCTION get_profile_value( p_profile_name IN VARCHAR2
                            , p_user_name    IN VARCHAR2
                            )
    RETURN VARCHAR2
  IS
  BEGIN
    RETURN qms$profile.get_profile_value(p_profile_name, p_user_name);
  END get_profile_value;

  FUNCTION get_user_profiles(p_user_name in VARCHAR2)
    RETURN hil$profile.table_type
  IS
  BEGIN
    RETURN qms$profile.get_user_profiles(p_user_name);
  END get_user_profiles;

  FUNCTION get_user_profiles(p_user_id in NUMBER)
    RETURN hil$profile.table_type
  IS
  BEGIN
   qms$errors.internal_exception('hil$profile.get_user_profiles invoked with parameter set which is not supported');
  END get_user_profiles;

-- For some reason, we need to close the package body here. Designer will remove it
-- when it generates the .pkb file.
END hil$profile;
/
SHOW ERROR