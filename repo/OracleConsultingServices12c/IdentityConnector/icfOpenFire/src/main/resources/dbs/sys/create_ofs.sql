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
Rem     This script creates all object required to set Openfire
Rem     inside of a database used as Oracle Metadata Repository.
Rem
Rem     This option is optional if the database is used as an
Rem     repository for the Openfire XMPP Server.
Rem
Rem     After complete installation the Oracle Metadata Repository
Rem     contains all objects required to use following products:
Rem
Rem     o  Openfire XMPP Server
Rem
Rem Author......: Dieter Steding, Oracle Consulting Berlin
Rem
Rem Note........:
Rem     This type of installation is NOT supported on MS Windows 2000/XP.
Rem
Rem Revision   Date        Editor      Comment
Rem ----------+-----------+-----------+------------------------------------
Rem 1.0.0.0    2011-03-11  DSteding    First release version
Rem

Rem *
Rem * Switch console output off.
Rem * Setting this option to on is not necessary
Rem *
set echo off

Rem *
Rem * Connect to the database kernel as SYSDBA
Rem *
Rem * The anonymous convention of the connect assumes that the user
Rem * creating this database is member of the DBA group.
Rem * Note:
Rem *   (On MS Windows ensure that SQLNET.AUTHENTICATION_SERVICES is set to NTS)
Rem *
connect / as sysdba

Rem *
Rem * Switch spool on
Rem *
spool ./log/create_ofs.log

Rem *
Rem * Create the tablespace to hold database objetcs of the Openfire XMPP Server
Rem * repository
Rem *
PROMPT Create data tablespace ofs_data
CREATE SMALLFILE TABLESPACE ofs_data
  DATAFILE '/dbs/oracle/MDR/ofsd01.dbf' SIZE 1024M REUSE
  AUTOEXTEND ON NEXT 40M MAXSIZE UNLIMITED
  EXTENT MANAGEMENT LOCAL AUTOALLOCATE
  SEGMENT SPACE MANAGEMENT AUTO
/

Rem *
Rem * Create the temporary tablespace decicated to Openfire XMPP Server
Rem * schema owner
Rem *
PROMPT Create temporary tablespace ofs_temp
CREATE SMALLFILE TEMPORARY TABLESPACE ofs_temp
  TEMPFILE '/dbs/oracle/MDR/ofst01.dbf' SIZE 128M REUSE
  EXTENT MANAGEMENT LOCAL
/

Rem *
Rem * Create the directory target for Openfire XMPP Server
Rem * backups and grant the proper permissions to system
Rem *
PROMPT Creating directory for identity backup data
CREATE DIRECTORY ofsdata AS '/dbs/oracle/admin/OFS/backup/data'
/
PROMPT Granting permission on directory for openfire backup data to system
GRANT READ, WRITE ON DIRECTORY ofsdata TO system
/
PROMPT Creating directory for openfire backup logs
CREATE DIRECTORY ofslogs AS '/dbs/oracle/admin/OFS/backup/logs'
/
PROMPT Granting permission on directory for openfire backup logs to system
GRANT READ, WRITE ON DIRECTORY ofslogs TO system


Rem *
Rem * Drop the owner of the Openfire XMPP Server Metadata Service and all
Rem * database objects owned by him.
Rem *
PROMPT ********************************************************************
PROMPT * Dropping user ofsowner
PROMPT ********************************************************************
DROP USER ofsowner CASCADE
/

Rem *
Rem * Create the owner of the Openfire XMPP Server Service with the
Rem * appropriate properties to create and store database objects
Rem *
PROMPT ********************************************************************
PROMPT * Creating user ofsowner
PROMPT ********************************************************************
CREATE USER ofsowner IDENTIFIED BY Welcome1
  DEFAULT   TABLESPACE ofs_data
  TEMPORARY TABLESPACE ofs_temp
  QUOTA UNLIMITED ON   ofs_data
/

Rem *
Rem * Grant necessary system roles to the repository owner
Rem *
PROMPT ********************************************************************
PROMPT * Granting system roles to user ofsowner
PROMPT ********************************************************************
GRANT CONNECT, RESOURCE TO ofsowner
/

Rem *
Rem * Switch spool off
Rem *
spool off
