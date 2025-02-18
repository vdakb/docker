Rem ! $ORACLE_HOME/bin/sqlplus
Rem
Rem Program.....: sqlplus
Rem
Rem Requirements: sqlplus
Rem     The database must be up and running and the connetion used to execute
Rem     this script needs the appropriate privileges.
Rem
Rem Purpose.....:
Rem     This script creates the Employee Portal Repository at a glance in an
Rem     Oracle Database Server 12c.
Rem
Rem Author......: Sylvert Bernet, Oracle Consulting France
Rem
Rem Note........:
Rem     This type of installation is NOT supported on MS Windows 2000/XP.
Rem
Rem Revision   Date        Editor      Comment
Rem ----------+-----------+-----------+------------------------------------
Rem 1.0.0.0    2020-03-30  SBernet     First release version
Rem

Rem *
Rem * Defining variables values
Rem *
define pcfschema=igd_pcf
define pcfpassword=wmkah1mdkh
define pcfdata=usr_data
define pcfindex=usr_data
define pcftemp=temp

Rem *
Rem * Creating schema objects and API
Rem *
@@install

Rem *
Rem * Importing seeded data
Rem *
@@import