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
Rem     This script creates administrative object required to use Password
Rem     Reset Services of a database used as Metadata Repository.
Rem
Rem     This option is optional if the database is used as an
Rem     repository for Custom Application Development.

-----------------------------------------------------------------------------------------
-- CREATE sequences
-----------------------------------------------------------------------------------------
@@pwr_seq.sqs
-----------------------------------------------------------------------------------------
-- CREATE tables
-----------------------------------------------------------------------------------------
@@pwr_req.tab
-----------------------------------------------------------------------------------------
-- CREATE constraints
-----------------------------------------------------------------------------------------
@@pwr_req.con