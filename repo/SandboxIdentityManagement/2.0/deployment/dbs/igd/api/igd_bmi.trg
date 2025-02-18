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
Rem      This script creates the trigger required to expose Federation
Rem      Participant Federal Ministry of the Interior and Community.
Rem
Rem Author......: Dieter Steding, Oracle Consulting Berlin
Rem
Rem
Rem Revision   Date        Editor      Comment
Rem ----------+-----------+-----------+------------------------------------
Rem 1.0.0.0    2019-06-12  DSteding    First release version
Rem

PROMPT ********************************************************************
PROMPT Creating Trigger 'ods$igd_bmi_io'
PROMPT ********************************************************************
CREATE OR REPLACE TRIGGER ods$igd_bmi_io
 INSTEAD OF DELETE OR INSERT OR UPDATE
 ON igd_usr_bmi
BEGIN
  igd$errors.raiseTransactionFailed;
END ods$igd_bmi_io;
/
SHOW ERROR
