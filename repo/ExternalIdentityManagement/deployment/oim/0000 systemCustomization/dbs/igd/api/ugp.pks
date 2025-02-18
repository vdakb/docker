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
 *  ~~~~~~~~~~~~+~~~~~~~~~~+~~~~~~~~~~~+~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 *  1.0.0.0     2022-03-11  DSteding    First release version
 */
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating API Package Implementation for igd$ugp
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
CREATE OR REPLACE PACKAGE igd$ugp
  -- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  -- Name:        igd$ugp
  -- Description: API for Identity Governance Role Administration
  -- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
IS
  cg$row ugp%ROWTYPE;

  -- ugp row type variable
  TYPE cg$row_type IS RECORD (
    id   cg$row.ugp_key%TYPE
  , name       cg$row.ugp_name%TYPE
  , data_level cg$row.ugp_data_level%TYPE
  , version    cg$row.ugp_rowver%TYPE
  );
  -- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  --
  -- Purpose  Returns the revision label of this package
  --
  -- Usage    -
  --
  -- Remarks  -
  --
  -- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  FUNCTION revision
    RETURN VARCHAR2;
END igd$ugp;
/
SHOW ERROR