Rem ! $ORACLE_HOME/bin/sqlplus
Rem
Rem Program: sqlplus
Rem
Rem Requirements: sqlplus
Rem     SCHEMA MUST EXISTS
Rem
Rem Purpose:
Rem     This script creates all object required to use Oracle BI Platform 12c
Rem     Release 12.2.1.3.0 as the reporting tool for a database to apply
Rem     advanced security options.
Rem
Rem Usage Information:
Rem     sqlplus sys/<password> as sysdba
Rem     @<path>/create
Rem
Rem
Rem Revisions   Date        Editor      Comment
Rem -----------+-----------+-----------+-----------------------------------
Rem 1.0.0.0     2022-03-11  DSteding    First release version
Rem

PROMPT ######################################################
PROMPT Granting object privileges
PROMPT ######################################################
@@sys

Rem *
Rem * Switch schema
Rem *
PROMPT ######################################################
PROMPT Switching session context ...
PROMPT ######################################################
ALTER SESSION SET CURRENT_SCHEMA = igd_igs
/

PROMPT ######################################################
PROMPT Create Oracle BI Platform objects
PROMPT ######################################################
@@igs

exit