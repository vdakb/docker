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
Rem     This script creates all object required to use Oracle Identity
Rem     Manager inside of a Oracle Unified Directory database workflow
Rem     element.
Rem
Rem     This option is mandatory if the database is used as an
Rem     identity store for participants exposed by Oracle Unified Directory.
Rem
Rem      This script creates all views required to expose Federation
Rem      Participants.
Rem
Rem Author......: Dieter Steding, Oracle Consulting Berlin
Rem
Rem
Rem Revision   Date        Editor      Comment
Rem ----------+-----------+-----------+------------------------------------
Rem 1.0.0.0    2019-06-12  DSteding    First release version
Rem

Rem *
Rem * Switch console output off.
Rem * Setting this option to ON is not necessary
Rem *
set echo off

Rem *
Rem * Switch spool ON
Rem *
spool ./log/igd/api.log

Rem *
Rem * Switch schema
Rem *
ALTER SESSION SET CURRENT_SCHEMA=&&oudschema
/

PROMPT ********************************************************************
PROMPT * Creating dentity Governance Core Application Interfaces
PROMPT ********************************************************************
-----------------------------------------------------------------------------------------
-- CREATE Core API
-----------------------------------------------------------------------------------------
@@igd_err.pks
@@igd_err.pkb
-----------------------------------------------------------------------------------------
-- CREATE View API
-----------------------------------------------------------------------------------------
@@igd_an.trg
@@igd_be.trg
@@igd_by.trg
@@igd_hb.trg
@@igd_mv.trg
@@igd_ni.trg
@@igd_nw.trg
@@igd_rp.trg
@@igd_sh.trg
@@igd_sl.trg
@@igd_sn.trg
@@igd_th.trg
@@igd_zf.trg
@@igd_bmi.trg
@@igd_gs.trg

Rem *
Rem * Switch spool off
Rem *
spool off