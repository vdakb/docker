Rem ! $ORACLE_HOME/bin/sqlplus
Rem
Rem Program.....: sqlplus
Rem
Rem Requirements: sqlplus
Rem     SCHEMA MUST EXISTS
Rem
Rem Purpose.....:
Rem     This script creates Identity Governance objects and seeded data.
Rem
Rem Usage.......:
Rem     sqlplus igd_oim/<password>
Rem     @<path>/delete
Rem
Rem Author......: Dieter Steding, Red.Security, , dieter.steding@icloud.com
Rem
Rem
Rem Revisions   Date        Editor      Comment
Rem ~~~~~~~~~~~~+~~~~~~~~~~+~~~~~~~~~~~+~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Rem 1.0.0.0     2022-03-11  DSteding    First release version
Rem

Rem *
Rem * Switch schema
Rem *
PROMPT ######################################################
PROMPT Switching session context ...
PROMPT ######################################################
ALTER SESSION SET CURRENT_SCHEMA = igd_oim
/
PROMPT ######################################################
PROMPT Create Identity Governance API Specification
PROMPT ######################################################
@igd/api/act.pks
@igd/api/ugp.pks
@igd/api/usr.pks

PROMPT ######################################################
PROMPT Create Identity Governance API Implementation
PROMPT ######################################################
@igd/api/act.pkb
@igd/api/ugp.pkb
@igd/api/usr.pkb