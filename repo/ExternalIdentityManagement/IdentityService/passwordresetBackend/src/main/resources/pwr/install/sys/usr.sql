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
Rem     This script creates the owner of the Identity
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
Rem * Drop the owner of the Identity Governance Services repository and all
Rem * database objects owned by him.
Rem *
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Dropping user igd_igs
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
DROP USER igd_igs CASCADE
/

Rem *
Rem * Create the owner of the Identity Governance Services repository with
Rem * the appropriate properties to create and store database objects
Rem *
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating user igd_igs
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
CREATE USER igd_igs IDENTIFIED BY Welcome1
  DEFAULT   TABLESPACE  igs_data
  TEMPORARY TABLESPACE temp
  QUOTA UNLIMITED ON  igs_data
/

Rem *
Rem * Grant necessary system roles to the repository owner
Rem *
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Granting system roles to user igd_igs
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
GRANT CONNECT, RESOURCE TO igd_igs
/

Rem *
Rem * Grant necessary object privileges to the repository owner
Rem *
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Granting object privileges to user igd_igs
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
GRANT SELECT ON sys.v_$process TO igd_igs
/
GRANT SELECT ON sys.v_$session TO igd_igs
/
