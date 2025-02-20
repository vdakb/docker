Rem ! $ORACLE_HOME/bin/sqlplus
Rem
Rem Program.....: sqlplus
Rem
Rem Requirements: sqlplus
Rem     The database must be up and running and the connetion used to execute
Rem     this script needs the appropriate privileges.
Rem
Rem Purpose.....:
Rem     This script creates the Database Utility Repository at a glance in an
Rem     Oracle Database Server 12c.
Rem
Rem Author......: Dieter Steding, Oracle Consulting Germany
Rem
Rem Note........:
Rem     This type of installation is NOT supported on MS Windows 2000/XP.
Rem
Rem Revision   Date        Editor      Comment
Rem ----------+-----------+-----------+------------------------------------
Rem 1.0.0.0    2020-04-12  DSteding     First release version
Rem

Rem *
Rem * Defining variables values
Rem *
define admschema=iam_adm
define admpassword=Sophie20061990$
define admdata=usr_data
define admindex=usr_data
define admtemp=temp

Rem *
Rem * Creating objects owner
Rem *
@sys/usr/adm

Rem *
Rem * Creating schema objects and API
Rem *
@iam/obj/adm
@iam/api/adm
