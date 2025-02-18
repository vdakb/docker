Rem ! $ORACLE_HOME/bin/sqlplus
Rem
Rem Program: sqlplus
Rem
Rem Requirements: sqlplus
Rem     SCHEMA MUST EXISTS
Rem
Rem Purpose:
Rem     This script deletes Unique Identifier Generator seeded data and
Rem     objects.
Rem
Rem Usage Information:
Rem     sqlplus igd_igs/<password>
Rem     @<path>/delete
Rem
Rem
Rem Revisions   Date        Editor      Comment
Rem -----------+-----------+-----------+-----------------------------------
Rem 1.0.0.0     2022-03-11  DSteding    First release version
Rem

Rem *
Rem * Switch schema
Rem *
PROMPT ######################################################
PROMPT Switching session context ...
PROMPT ######################################################
ALTER SESSION SET CURRENT_SCHEMA = igd_igs
/

PROMPT ######################################################
PROMPT Delete Unique Identifier Generator
PROMPT ######################################################
@resources/uid/install/val/delete
@resources/uid/install/api/delete
@resources/uid/install/obj/delete

exit