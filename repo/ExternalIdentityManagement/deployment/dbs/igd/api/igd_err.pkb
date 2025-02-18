Rem ! $ORACLE_HOME/bin/sqlplus
Rem
Rem $Header$
Rem
Rem Program.....: sqlplus
Rem
Rem Requirements: sqlplus
Rem     The database must be up and running.
Rem
Rem Purpose.....:
Rem     This script creates all object required to use Oracle Identity
Rem     Manager inside of a Oracle Unified Directory database workflow
Rem     element.
Rem
Rem     This option is mandatory if the database is used as an
Rem     identity store for participants exposed by Oracle Unified Directory.
Rem
Rem Author......: Dieter Steding, Oracle Consulting Berlin
Rem
Rem
Rem Revision   Date        Editor      Comment
Rem ----------+-----------+-----------+------------------------------------
Rem 1.0.0.0    2019-06-12  DSteding    First release version
Rem

PROMPT ********************************************************************
PROMPT Creating Package Body 'igd$errors'
PROMPT ********************************************************************
CREATE or REPLACE PACKAGE BODY igd$errors
AS

PROCEDURE raiseTransactionFailed
IS
BEGIN
  raise igd$exception;
EXCEPTION
  WHEN OTHERS
  THEN
    raise_application_error(-20998, 'Transaction Failed');
END raiseTransactionFailed;

-- For some reason, we need to close the package body here. Designer will remove it
-- when it generates the .pkb file.
END igd$errors;
/
SHOW ERROR
