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
Rem     This script creates business object required to use Password
Rem     Reset Services inside of a database used as Metadata Repository.

Rem *
Rem * Switch console output off.
Rem * Setting this option to ON is not necessary
Rem *
set echo off

Rem *
Rem * Switch spool ON
Rem *
spool ./pwr.log

PROMPT ######################################################
PROMPT Dropping Password Reset Services business object ...
PROMPT ######################################################
@pwr/obj/delete

PROMPT ######################################################
PROMPT Creating Password Reset Services business object ...
PROMPT ######################################################
@pwr/obj/create

Rem *
Rem * Switch spool off
Rem *
spool off
