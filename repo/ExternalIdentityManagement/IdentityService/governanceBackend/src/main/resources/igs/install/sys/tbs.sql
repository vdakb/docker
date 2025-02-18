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
Rem     This script creates the tablespace used by Identity
Rem     Governance Services inside of a database used as Metadata Repository.
Rem
Rem     This option is optional if the database is used as an
Rem     repository for Custom Application Development.
Rem
Rem Author......: Dieter Steding, Oracle Consulting Berlin
Rem
Rem
Rem Revisions   Date        Editor      Comment
Rem -----------+-----------+-----------+-----------------------------------
Rem 1.0.0.0     2022-03-11  DSteding    First release version
Rem

Rem *
Rem * Create the tablespace to hold database objetcs of the Identity
Rem * Governance Services repository
Rem *
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Create permanent tablespace igs_data
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
CREATE SMALLFILE TABLESPACE igs_data
  DATAFILE '/dbs/oracle/MDR/igsd01.dbf' SIZE 128M REUSE
  AUTOEXTEND ON NEXT 12M MAXSIZE UNLIMITED
  EXTENT MANAGEMENT LOCAL AUTOALLOCATE
  SEGMENT SPACE MANAGEMENT AUTO
/