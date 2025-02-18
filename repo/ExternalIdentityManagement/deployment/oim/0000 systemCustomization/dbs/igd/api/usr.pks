/* ! $ORACLE_HOME/bin/sqlplus
 *
 * Program.....: sqlplus
 *
 * Requirements: sqlplus
 *      SCHEMA MUST EXISTS
 *
 *  Purpose.....:
 *      Creating API Package Implementation for igd$usr.
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
PROMPT Creating API Package Specification igd$usr
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
CREATE OR REPLACE PACKAGE igd$usr
  -- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  -- Name:        igd$usr
  -- Description: API for Identity Governance User Administration
  -- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
IS
  cg$row usr%ROWTYPE;

  -- usr row type variable
  TYPE cg$row_type IS RECORD (
    id         cg$row.usr_key%TYPE
  , name       cg$row.usr_login%TYPE
  , status     cg$row.usr_status%TYPE
  , data_level cg$row.usr_data_level%TYPE
  , version    cg$row.usr_rowver%TYPE
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

  -- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  -- Name:        delete_identity
  --
  -- Description: Delete a users identity.
  --
  -- Parameters:  p_username The name of the user to delete.
  -- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  PROCEDURE delete_identity(p_username IN VARCHAR2);
END igd$usr;
/
SHOW ERROR