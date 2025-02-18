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
spool ./log/qms/api.log

Rem *
Rem * Switch schema
Rem *
ALTER SESSION SET CURRENT_SCHEMA = &&eppschema
/

PROMPT ********************************************************************
PROMPT * Creating Employee Self Service Portal Core Application Interfaces
PROMPT ********************************************************************
-----------------------------------------------------------------------------------------
-- CREATE Core API
-----------------------------------------------------------------------------------------
@@hil-prf.pks
@@hil-msg.pks
@@cg-err.pks
@@qms-utl.pks
@@qms-err.pks
@@qms-ses.pks
@@qms-prf.pks
@@qms-msg.pks
@@qms-spf.pks

@@hil-prf.pkb
@@hil-msg.pkb
@@cg-err.pkb
@@qms-utl.pkb
@@qms-err.pkb
@@qms-ses.pkb
@@qms-prf.pkb
@@qms-msg.pkb
@@qms-spf.pkb
-----------------------------------------------------------------------------------------
-- CREATE Table API
-----------------------------------------------------------------------------------------
@@qms-msp.pks
@@qms-mst.pks

@@qms-msp.pkb
@@qms-mst.pkb

@@qms-msp.trg
@@qms-mst.trg

Rem *
Rem * Switch spool off
Rem *
spool off