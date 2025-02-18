Rem ! $ORACLE_HOME/bin/sqlplus
Rem
Rem Program: sqlplus
Rem
Rem Requirements: sqlplus
Rem     SCHEMA MUST EXISTS
Rem
Rem Purpose:
Rem     This script deletes Identity Governance Services seeded data and
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
PROMPT Delete Identity Governance Services
PROMPT ######################################################
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Delete Identity Governance Services
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
@governanceBackend/src/main/resources/install/igs/val/delete
@governanceBackend/src/main/resources/install/igs/api/delete
@governanceBackend/src/main/resources/install/igs/obj/delete

PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Delete Quality Managed System
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
@governanceBackend/src/main/resources/install/qms/val/delete
@governanceBackend/src/main/resources/install/qms/api/delete
@governanceBackend/src/main/resources/install/qms/obj/delete

exit