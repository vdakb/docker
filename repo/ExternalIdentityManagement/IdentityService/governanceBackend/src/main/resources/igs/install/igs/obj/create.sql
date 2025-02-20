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
Rem     This script creates administrative object required to use Unique
Rem     Identifier Assembler of a database used as Metadata Repository.
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
-- CREATE sequences
-----------------------------------------------------------------------------------------
@@ids_seq.sqs
-----------------------------------------------------------------------------------------
-- CREATE tables
-----------------------------------------------------------------------------------------
@@igt_ses.tab
@@igt_rol.tab
@@igt_usr.tab
@@igt_url.tab
-----------------------------------------------------------------------------------------
-- CREATE constraints
-----------------------------------------------------------------------------------------
@@igt_ses.con
@@igt_rol.con
@@igt_usr.con
@@igt_url.con
-----------------------------------------------------------------------------------------
-- CREATE indexes
-----------------------------------------------------------------------------------------
@@igt_url.ind