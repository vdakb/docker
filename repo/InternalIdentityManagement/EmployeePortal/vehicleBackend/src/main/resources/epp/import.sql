Rem ! $ORACLE_HOME/bin/sqlplus
Rem
Rem Program.....: sqlplus
Rem
Rem Requirements: sqlplus
Rem     The database must be up and running and the connetion used to execute
Rem     this script needs the appropriate privileges.
Rem
Rem Purpose.....:
Rem     This script imports seeded data into the Employee Portal Repository
Rem     deloyed in an Oracle Database Server 12c.
Rem
Rem Author......: Dieter Steding, Oracle Consulting Berlin
Rem
Rem Note........:
Rem     This type of installation is NOT supported on MS Windows 2000/XP.
Rem
Rem Revision   Date        Editor      Comment
Rem ----------+-----------+-----------+------------------------------------
Rem 1.0.0.0    2020-03-30  DSteding    First release version
Rem

Rem *
Rem * Switch schema
Rem *
ALTER SESSION SET CURRENT_SCHEMA = &&eppschema
/

Rem *
Rem * Importing data
Rem *
@data/msg
@data/vhc
@data/vht
@data/vmb
@data/vmt