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
Rem     This script creates all users and their permission required to expose
Rem     Oracle Identity Manager as an Directory Service leveraging Oracle Unified
Rem     Directory proxy capabilities.
Rem
Rem     This option is mandatory if the database is used as an
Rem     identity store for participants exposed by Oracle Unified Directory.
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
spool ./log/sys/fix.log

Rem *
Rem * Grant necessary object privileges to the repository owner
Rem *
PROMPT ********************************************************************
PROMPT * Granting object privileges to user &&oudschema
PROMPT ********************************************************************
GRANT SELECT ON igd_oim.usr TO &&oudschema
/
GRANT SELECT ON igd_oim.act TO &&oudschema
/

ALTER SESSION SET CURRENT_SCHEMA=&&oudschema
/

PROMPT ********************************************************************
PROMPT Compiling Package 'igd$errors'
PROMPT ********************************************************************
ALTER PACKAGE igd$errors COMPILE
/
PROMPT ********************************************************************
PROMPT Compiling Package Body 'igd$errors'
PROMPT ********************************************************************
ALTER PACKAGE igd$errors COMPILE BODY
/
PROMPT ********************************************************************
PROMPT Compiling View 'igd_usr_an'
PROMPT ********************************************************************
ALTER VIEW igd_usr_an COMPILE
/
PROMPT ********************************************************************
PROMPT Compiling View 'igd_usr_be'
PROMPT ********************************************************************
ALTER VIEW igd_usr_be COMPILE
/
PROMPT ********************************************************************
PROMPT Compiling View 'igd_usr_by'
PROMPT ********************************************************************
ALTER VIEW igd_usr_by COMPILE
/
PROMPT ********************************************************************
PROMPT Compiling View 'igd_usr_hb'
PROMPT ********************************************************************
ALTER VIEW igd_usr_hb COMPILE
/
PROMPT ********************************************************************
PROMPT Compiling View 'igd_usr_mv'
PROMPT ********************************************************************
ALTER VIEW igd_usr_mv COMPILE
/
PROMPT ********************************************************************
PROMPT Compiling View 'igd_usr_ni'
PROMPT ********************************************************************
ALTER VIEW igd_usr_ni COMPILE
/
PROMPT ********************************************************************
PROMPT Compiling View 'igd_usr_nw'
PROMPT ********************************************************************
ALTER VIEW igd_usr_nw COMPILE
/
PROMPT ********************************************************************
PROMPT Compiling View 'igd_usr_rp'
PROMPT ********************************************************************
ALTER VIEW igd_usr_rp COMPILE
/
PROMPT ********************************************************************
PROMPT Compiling View 'igd_usr_sh'
PROMPT ********************************************************************
ALTER VIEW igd_usr_sh COMPILE
/
PROMPT ********************************************************************
PROMPT Compiling View 'igd_usr_sl'
PROMPT ********************************************************************
ALTER VIEW igd_usr_sl COMPILE
/
PROMPT ********************************************************************
PROMPT Compiling View 'igd_usr_sn'
PROMPT ********************************************************************
ALTER VIEW igd_usr_sn COMPILE
/
PROMPT ********************************************************************
PROMPT Compiling View 'igd_usr_th'
PROMPT ********************************************************************
ALTER VIEW igd_usr_th COMPILE
/
PROMPT ********************************************************************
PROMPT Compiling View 'igd_usr_zf'
PROMPT ********************************************************************
ALTER VIEW igd_usr_zf COMPILE
/
PROMPT ********************************************************************
PROMPT Compiling Trigger 'ods$igd_an_io'
PROMPT ********************************************************************
ALTER TRIGGER ods$igd_an_io COMPILE
/
PROMPT ********************************************************************
PROMPT Compiling Trigger 'ods$igd_be_io'
PROMPT ********************************************************************
ALTER TRIGGER ods$igd_be_io COMPILE
/
PROMPT ********************************************************************
PROMPT Compiling Trigger 'ods$igd_be_io'
PROMPT ********************************************************************
ALTER TRIGGER ods$igd_be_io COMPILE
/
PROMPT ********************************************************************
PROMPT Compiling Trigger 'ods$igd_by_io'
PROMPT ********************************************************************
ALTER TRIGGER ods$igd_by_io COMPILE
/
PROMPT ********************************************************************
PROMPT Compiling Trigger 'ods$igd_hb_io'
PROMPT ********************************************************************
ALTER TRIGGER ods$igd_hb_io COMPILE
/
PROMPT ********************************************************************
PROMPT Compiling Trigger 'ods$igd_mv_io'
PROMPT ********************************************************************
ALTER TRIGGER ods$igd_mv_io COMPILE
/
PROMPT ********************************************************************
PROMPT Compiling Trigger 'ods$igd_ni_io'
PROMPT ********************************************************************
ALTER TRIGGER ods$igd_ni_io COMPILE
/
PROMPT ********************************************************************
PROMPT Compiling Trigger 'ods$igd_nw_io'
PROMPT ********************************************************************
ALTER TRIGGER ods$igd_nw_io COMPILE
/
PROMPT ********************************************************************
PROMPT Compiling Trigger 'ods$igd_rp_io'
PROMPT ********************************************************************
ALTER TRIGGER ods$igd_rp_io COMPILE
/
PROMPT ********************************************************************
PROMPT Compiling Trigger 'ods$igd_sh_io'
PROMPT ********************************************************************
ALTER TRIGGER ods$igd_sh_io COMPILE
/
PROMPT ********************************************************************
PROMPT Compiling Trigger 'ods$igd_sl_io'
PROMPT ********************************************************************
ALTER TRIGGER ods$igd_sl_io COMPILE
/
PROMPT ********************************************************************
PROMPT Compiling Trigger 'ods$igd_sn_io'
PROMPT ********************************************************************
ALTER TRIGGER ods$igd_sn_io COMPILE
/
PROMPT ********************************************************************
PROMPT Compiling Trigger 'ods$igd_th_io'
PROMPT ********************************************************************
ALTER TRIGGER ods$igd_th_io COMPILE
/
PROMPT ********************************************************************
PROMPT Compiling Trigger 'ods$igd_zf_io'
PROMPT ********************************************************************
ALTER TRIGGER ods$igd_zf_io COMPILE
/

Rem *
Rem * Switch spool off
Rem *
spool off

exit