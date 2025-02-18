Rem ! $ORACLE_HOME/bin/sqlplus
Rem
Rem Program: sqlplus
Rem
Rem Requirements: sqlplus
Rem     SCHEMA MUST EXISTS
Rem
Rem Purpose:
Rem     This script drops all object used by Oracle BI Platform 12c
Rem     Release 12.2.1.3.0 as the reporting tool for a database to apply
Rem     advanced security options.
Rem
Rem Usage Information:
Rem     sqlplus sys/<password> as sysdba
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

@resources/igd/install/api/delete
@resources/igd/install/obj/delete

PROMPT ######################################################
PROMPT Revoking object privileges ...
PROMPT ######################################################

PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Revoking Oracle Identity Governance Objects
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
REVOKE SELECT ON igd_oim.usr FROM igd_igs
/
REVOKE SELECT ON igd_oim.role_category FROM igd_igs
/
REVOKE SELECT ON igd_oim.ugp FROM igd_igs
/
REVOKE SELECT ON igd_oim.act FROM igd_igs
/

exit