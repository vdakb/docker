Rem ! $ORACLE_HOME/bin/sqlplus
Rem
Rem Program: sqlplus
Rem
Rem Requirements: sqlplus
Rem     SCHEMA MUST EXISTS
Rem
Rem Purpose:
Rem     This script deletes Password Reset Services objects.
Rem
Rem Usage Information:
Rem     sqlplus igd_igs/<password>
Rem     @<path>/delete

Rem *
Rem * Switch schema
Rem *
PROMPT ######################################################
PROMPT Switching session context ...
PROMPT ######################################################
ALTER SESSION SET CURRENT_SCHEMA = igd_igs
/

PROMPT ######################################################
PROMPT Delete Password Reset Services
PROMPT ######################################################
@resources/pwr/install/obj/delete

exit