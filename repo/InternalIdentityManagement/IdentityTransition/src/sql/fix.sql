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
Rem * Grant necessary object privileges to the repository owner
Rem *
PROMPT ********************************************************************
PROMPT * Granting object privileges to user &&1
PROMPT ********************************************************************
GRANT SELECT ON igd_oim.usr TO &&1
/
GRANT SELECT ON igd_oim.act TO &&1
/

ALTER SESSION SET CURRENT_SCHEMA=&&1
/

PROMPT ********************************************************************
PROMPT Compiling Package Body 'qms$profile'
PROMPT ********************************************************************
ALTER PACKAGE qms$profile COMPILE BODY
/
PROMPT ********************************************************************
PROMPT Compiling Package 'epp$session'
PROMPT ********************************************************************
ALTER PACKAGE epp$session COMPILE PACKAGE
/
PROMPT ********************************************************************
PROMPT Compiling Package Body 'epp$session'
PROMPT ********************************************************************
ALTER PACKAGE epp$session COMPILE BODY
/
PROMPT ********************************************************************
PROMPT Compiling Package 'epp$user'
PROMPT ********************************************************************
ALTER PACKAGE epp$user COMPILE BODY
/
PROMPT ********************************************************************
PROMPT Compiling Package Body 'epp$user'
PROMPT ********************************************************************
ALTER PACKAGE epp$user COMPILE BODY
/
PROMPT ********************************************************************
PROMPT Compiling Package Body 'cg$ept_vehicle_colors'
PROMPT ********************************************************************
ALTER PACKAGE cg$ept_vehicle_colors COMPILE BODY
/
PROMPT ********************************************************************
PROMPT Compiling Package Body 'cg$ept_vehicle_types'
PROMPT ********************************************************************
ALTER PACKAGE cg$ept_vehicle_types COMPILE BODY
/
PROMPT ********************************************************************
PROMPT Compiling Package Body 'cg$ept_vehicle_brands'
PROMPT ********************************************************************
ALTER PACKAGE cg$ept_vehicle_brands COMPILE BODY
/
PROMPT ********************************************************************
PROMPT Compiling Package Body 'cg$ept_vehicle_brand_types'
PROMPT ********************************************************************
ALTER PACKAGE cg$ept_vehicle_brand_types COMPILE BODY
/
PROMPT ********************************************************************
PROMPT Compiling Package Body 'cg$ept_vehicles'
PROMPT ********************************************************************
ALTER PACKAGE cg$ept_vehicles COMPILE BODY
/

exit