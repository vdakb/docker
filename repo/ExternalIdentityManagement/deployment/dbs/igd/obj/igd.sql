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
spool ./log/igd/obj.log

Rem *
Rem * Switch schema
Rem *
ALTER SESSION SET CURRENT_SCHEMA=&&oudschema
/

PROMPT ********************************************************************
PROMPT * Creating Federation Participant Core Objects
PROMPT ********************************************************************
-----------------------------------------------------------------------------------------
-- CREATE views
-----------------------------------------------------------------------------------------
@@igd_an.vws
@@igd_be.vws
@@igd_by.vws
@@igd_hb.vws
@@igd_mv.vws
@@igd_ni.vws
@@igd_nw.vws
@@igd_rp.vws
@@igd_sh.vws
@@igd_sl.vws
@@igd_sn.vws
@@igd_th.vws
@@igd_zf.vws

Rem *
Rem * Switch spool off
Rem *
spool off