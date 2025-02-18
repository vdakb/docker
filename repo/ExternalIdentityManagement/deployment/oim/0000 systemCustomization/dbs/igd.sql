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
Rem     This script creates persistence API required by Identity
Rem     Governance inside of a database used as Metadata Repository.
Rem
Rem     This option is optional if the database is used as an
Rem     repository for Custom Application Development.
Rem
Rem Author......: Dieter Steding, Red.Security, , dieter.steding@icloud.com
Rem
Rem
Rem Revision   Date        Editor      Comment
Rem ----------+-----------+-----------+------------------------------------
Rem 1.0.0.0    2022-03-11  DSteding    First release version
Rem

/*
SELECT   uc.table_name
,        uc.r_constraint_name
,        ucc.table_name
,        ucc.column_name
FROM     user_constraints  uc
,        user_cons_columns ucc
WHERE    uc.r_constraint_name = 'PK_ORG_USER_MEMBERSHIPS'
  AND    uc.r_constraint_name = ucc.constraint_name
  AND    uc.constraint_type   = 'R'
ORDER BY uc.table_name
,        uc.r_constraint_name
,        ucc.table_name
,        ucc.column_name
/
*/
Rem *
Rem * Switch console output off.
Rem * Setting this option to ON is not necessary
Rem *
set echo off

Rem *
Rem * Switch spool ON
Rem *
spool ./igd.log

@igd/delete

@igd/create

Rem *
Rem * Switch spool off
Rem *
spool off