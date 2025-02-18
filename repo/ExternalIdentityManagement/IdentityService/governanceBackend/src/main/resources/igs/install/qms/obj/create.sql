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
Rem     This script creates all object required to use Unique
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
@@qms_seq.sqs
-----------------------------------------------------------------------------------------
-- CREATE tables
-----------------------------------------------------------------------------------------
@@qms_rws.tab
@@qms_rls.tab
@@qms_txc.tab
@@qms_spf.tab
@@qms_msp.tab
@@qms_mst.tab
@@qms_rlp.tab
-----------------------------------------------------------------------------------------
-- CREATE constraints
-----------------------------------------------------------------------------------------
@@qms_rws.con
@@qms_rls.con
@@qms_txc.con
@@qms_spf.con
@@qms_msp.con
@@qms_mst.con
@@qms_rlp.con
-----------------------------------------------------------------------------------------
-- CREATE indexes
-----------------------------------------------------------------------------------------
@@qms_rls.ind
