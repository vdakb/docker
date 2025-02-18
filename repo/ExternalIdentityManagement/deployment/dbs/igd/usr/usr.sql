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
Rem     This script modifies the properties of the System Administrator
Rem     of Identity Manager so that the password never expires and regardless
Rem     how the password policy is configured.
Rem
Rem     This option is optional.
Rem
Rem Author......: Dieter Steding, Oracle Consulting Berlin
Rem
Rem
Rem Revision   Date        Editor      Comment
Rem ----------+-----------+-----------+------------------------------------
Rem 1.0.0.0    2010-06-12  DSteding    First release version
Rem

Rem *
Rem * Switch console output off.
Rem * Setting this option to ON is not necessary
Rem *
set echo off

Rem *
Rem * Switch spool ON
Rem *
spool ./log/igd/usr.log

Rem *
Rem * Switch schema
Rem *
ALTER SESSION SET CURRENT_SCHEMA=&&igdschema
/

PROMPT ********************************************************************
PROMPT * Modifiying System Administrator
PROMPT ********************************************************************
UPDATE usr SET
  usr_pwd_expire_date          = null
, usr_pwd_warn_date            = null
, usr_pwd_expired              = null
, usr_change_pwd_at_next_logon = null
, usr_pwd_never_expires        = 1
WHERE  usr_key = 1
/

Rem *
Rem * Switch spool off
Rem *
spool off