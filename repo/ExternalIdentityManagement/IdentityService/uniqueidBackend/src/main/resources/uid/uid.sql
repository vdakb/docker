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
Rem 1.0.0.0    2010-03-30  DSteding    First release version
Rem

Rem *
Rem * Switch console output off.
Rem * Setting this option to ON is not necessary
Rem *
set echo off

Rem *
Rem * Switch spool ON
Rem *
spool ./uid.log

PROMPT ######################################################
PROMPT Dropping Unique Identifier Generator persistence API ...
PROMPT ######################################################
@install/api/delete

PROMPT ######################################################
PROMPT Dropping Unique Identifier Generator business object ...
PROMPT ######################################################
@install/obj/delete

PROMPT ######################################################
PROMPT Creating Unique Identifier Generator business object ...
PROMPT ######################################################
@install/obj/create

PROMPT ######################################################
PROMPT Creating Unique Identifier Generator persistence API ...
PROMPT ######################################################
@install/api/create

Rem *
Rem * Switch spool off
Rem *
spool off