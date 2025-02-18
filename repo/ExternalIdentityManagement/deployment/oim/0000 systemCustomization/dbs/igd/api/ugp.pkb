/* ! $ORACLE_HOME/bin/sqlplus
 *
 * Program.....: sqlplus
 *
 * Requirements: sqlplus
 *      SCHEMA MUST EXISTS
 *
 *  Purpose.....:
 *      Creating API Package Implementation for igd$ugp.
 *
 *
 *  Author......: Dieter Steding, Red.Security, , dieter.steding@icloud.com
 *
 *
 *  Revisions   Date        Editor      Comment
 *  ~~~~~~~~~~~~+~~~~~~~~~~+~~~~~~~~~~~+~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 *  1.0.0.0     2022-03-11  DSteding    First release version
 */
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating API Package Implementation for igd$ugp
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
CREATE OR REPLACE PACKAGE BODY igd$ugp
IS
  -- ------------------------------------------------------------------
  -- private constants
  -- ------------------------------------------------------------------
  /* Revision label of package */
  REVISION_LABEL   CONSTANT VARCHAR2( 8) := '1.0.0.0';

  -- ------------------------------------------------------------------
  -- private program units
  -- ------------------------------------------------------------------

  -- ------------------------------------------------------------------
  --
  -- Purpose...:
  --   Selects a role identity into the given parameter attribute for
  --   the row given by the role name.
  --
  -- Usage.....: -
  --
  -- Remarks...: -
  --
  -- Parameters:
  --    p_name         The name of the role to fetch form the database.
  -- ------------------------------------------------------------------
  PROCEDURE sel(p_name IN VARCHAR2, cg$rec IN OUT cg$row_type)
  IS
  BEGIN
    SELECT ugp_key
    ,      ugp_name
    ,      ugp_data_level
    ,      ugp_rowver
    INTO   cg$rec.id
    ,      cg$rec.name
    ,      cg$rec.data_level
    ,      cg$rec.version
    FROM   ugp
    WHERE  ugp_name = p_name
    ;
  END;

  -- ------------------------------------------------------------------
  -- public program units
  -- ------------------------------------------------------------------

  -- ------------------------------------------------------------------
  --
  -- Purpose  Returns the revision label of this package
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  -- ------------------------------------------------------------------
  FUNCTION revision
    RETURN VARCHAR2
  IS
  BEGIN
    RETURN REVISION_LABEL;
  END revision;

END igd$ugp;
/
SHOW ERROR