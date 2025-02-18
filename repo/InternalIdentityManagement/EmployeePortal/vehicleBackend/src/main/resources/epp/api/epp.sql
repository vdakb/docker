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
Rem     This script creates all object required to use Employee
Rem     Portal inside of a database used as Metadata Repository.
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
spool ./log/api.log

Rem *
Rem * Switch schema
Rem *
ALTER SESSION SET CURRENT_SCHEMA = &&eppschema
/

PROMPT ********************************************************************
PROMPT * Creating Employee Self Service Portal Vehicle Application Interfaces
PROMPT ********************************************************************
-----------------------------------------------------------------------------------------
-- CREATE Table API
-----------------------------------------------------------------------------------------
@@ept-vhc.pks
@@ept-vht.pks
@@ept-vmb.pks
@@ept-vmt.pks
@@ept-vhl.pks

@@ept-vhc.pkb
@@ept-vht.pkb
@@ept-vmb.pkb
@@ept-vmt.pkb
@@ept-vhl.pkb

@@ept-vhc.trg
@@ept-vht.trg
@@ept-vmb.trg
@@ept-vmt.trg
@@ept-vhl.trg

Rem *
Rem * Switch spool off
Rem *
spool off