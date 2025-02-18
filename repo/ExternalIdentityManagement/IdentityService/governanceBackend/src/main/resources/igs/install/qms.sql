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
Rem     This script creates business object required to use Unique
Rem     Identifier Assembler inside of a database used as Metadata Repository.
Rem
Rem     This option is optional if the database is used as an
Rem     repository for Custom Application Development.
Rem
Rem Author......: Dieter Steding, Oracle Consulting Berlin
Rem
Rem
Rem Revision   Date        Editor      Comment
Rem ----------+-----------+-----------+------------------------------------
Rem 1.0.0.0    2022-03-11  DSteding    First release version
Rem

Rem *
Rem * Switch console output off.
Rem * Setting this option to ON is not necessary
Rem *
set echo off

Rem *
Rem * Switch spool ON
Rem *
spool ./qms.log

PROMPT ######################################################
PROMPT Dropping Identity Governance Services persistence API ...
PROMPT ######################################################
@qms/api/delete

PROMPT ######################################################
PROMPT Dropping Identity Governance Services business object ...
PROMPT ######################################################
@qms/obj/delete

PROMPT ######################################################
PROMPT Creating Identity Governance Services business object ...
PROMPT ######################################################
@qms/obj/create

PROMPT ######################################################
PROMPT Creating Identity Governance Services persistence API ...
PROMPT ######################################################
@qms/api/create

Rem *
Rem * Switch spool off
Rem *
spool off
