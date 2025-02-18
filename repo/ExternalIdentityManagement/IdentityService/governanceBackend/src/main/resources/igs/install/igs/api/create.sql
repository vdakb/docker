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
Rem     Governance Services inside of a database used as Metadata Repository.
Rem
Rem     This option is optional if the database is used as an
Rem     repository for Custom Application Development.
Rem
Rem Author......: Dieter Steding, Oracle Consulting Berlin
Rem
Rem
Rem Revision   Date        Editor      Comment
Rem ----------+-----------+-----------+------------------------------------
Rem 1.0.0.0    2022-03-11  DSteding    First release version
Rem

-----------------------------------------------------------------------------------------
-- CREATE Core Table API specification
-----------------------------------------------------------------------------------------
@@igt_rol.pks
@@igt_usr.pks
@@igt_url.pks
-----------------------------------------------------------------------------------------
-- CREATE Core Table API implementation
-----------------------------------------------------------------------------------------
@@igt_rol.pkb
@@igt_usr.pkb
@@igt_url.pkb
-----------------------------------------------------------------------------------------
-- CREATE Core Table API interfaces
-----------------------------------------------------------------------------------------
@@igt_rol.trg
@@igt_usr.trg
@@igt_url.trg
-----------------------------------------------------------------------------------------
-- CREATE Administration API specification
-----------------------------------------------------------------------------------------
@@igs_usr.pks
-----------------------------------------------------------------------------------------
-- CREATE Administration API implementation
-----------------------------------------------------------------------------------------
@@igs_usr.pkb