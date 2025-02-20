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
Rem     This script creates all object required to use Database
Rem     Utility inside of a database used as Metadata Repository.
Rem
Rem     This option is optional if the database is used as an
Rem     repository for Custom Application Development.
Rem
Rem Author......: Dieter Steding, Oracle Consulting Berlin
Rem
Rem
Rem Revision   Date        Editor      Comment
Rem ----------+-----------+-----------+------------------------------------
Rem 1.0.0.0    2020-03-30  DSteding    First release version
Rem

Rem *
Rem * Switch console output off.
Rem * Setting this option to ON is not necessary
Rem *
set echo off

Rem *
Rem * Switch spool ON
Rem *
spool ./log/api/adm.log

Rem *
Rem * Switch schema
Rem *
ALTER SESSION SET CURRENT_SCHEMA = &&admschema
/

PROMPT ********************************************************************
PROMPT * Creating Employee Self Service Portal Core Application Interfaces
PROMPT ********************************************************************
-----------------------------------------------------------------------------------------
-- CREATE Core API
-----------------------------------------------------------------------------------------
@@adm_dir.java
@@adm_dir.pks
@@adm_dir.pkb

Rem *
Rem * Switch spool off
Rem *
spool off