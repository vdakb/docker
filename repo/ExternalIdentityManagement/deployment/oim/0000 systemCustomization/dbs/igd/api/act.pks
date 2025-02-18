/* ! $ORACLE_HOME/bin/sqlplus
 *
 * Program.....: sqlplus
 *
 * Requirements: sqlplus
 *      SCHEMA MUST EXISTS
 *
 *  Purpose.....:
 *      Creating API Package Specification for igd$act.
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
PROMPT -- Creating API Package Specification igd$act
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
CREATE OR REPLACE PACKAGE igd$act
  -- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  -- Name:        igd$act
  -- Description: API for Identity Governance Organization Administration
  -- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
IS
  cg$row act%ROWTYPE;

  -- act row type variable
  TYPE cg$row_type IS RECORD (
    id         cg$row.act_key%TYPE
  , name       cg$row.act_name%TYPE
  , data_level cg$row.act_data_level%TYPE
  , version    cg$row.act_rowver%TYPE
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
END igd$act;
/
SHOW ERROR