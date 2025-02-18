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
Rem     This script creates eFBS User Repository seeded data.
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
Rem * Switch schema
Rem *
ALTER SESSION SET CURRENT_SCHEMA = &&agtschema
/

Rem *
Rem * Switch spool ON
Rem *
spool ./log/agt/data.log

PROMPT ********************************************************************
PROMPT * Creating eFBS User Repository seeded data
PROMPT ********************************************************************
-----------------------------------------------------------------------------------------
-- User Repository
-----------------------------------------------------------------------------------------
@@usr.sql
@@org.sql
@@env.sql
@@prp.sql
@@upg.sql
@@orl.sql
@@cmp.sql
@@dev.sql
@@prd.sql
@@app.sql

COMMIT
/

Rem *
Rem * Switch spool OFF
Rem *
spool off