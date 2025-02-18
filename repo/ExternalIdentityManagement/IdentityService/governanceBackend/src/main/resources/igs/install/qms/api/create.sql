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
-- CREATE Core API prodecure
-----------------------------------------------------------------------------------------
@@qms_exe.prc
-----------------------------------------------------------------------------------------
-- CREATE Core API specification
-----------------------------------------------------------------------------------------
@@hil_msg.pks
@@cg_err.pks
@@cg_ses.pks
@@qms_txc.pks
@@qms_utl.pks
@@qms_msg.pks
@@qms_err.pks
@@qms_spf.pks
-----------------------------------------------------------------------------------------
-- CREATE Core API implementation
-----------------------------------------------------------------------------------------
@@hil_msg.pkb
@@cg_err.pkb
@@cg_ses.pkb
@@qms_txc.pkb
@@qms_utl.pkb
@@qms_msg.pkb
@@qms_err.pkb
@@qms_spf.pkb
-----------------------------------------------------------------------------------------
-- CREATE Core Table API specification
-----------------------------------------------------------------------------------------
@@qms_msp.pks
@@qms_mst.pks
@@qms_rlp.pks
-----------------------------------------------------------------------------------------
-- CREATE Core Table API implementation
-----------------------------------------------------------------------------------------
@@qms_msp.pkb
@@qms_mst.pkb
@@qms_rlp.pkb
-----------------------------------------------------------------------------------------
-- CREATE Core Table API interfaces
-----------------------------------------------------------------------------------------
@@qms_msp.trg
@@qms_mst.trg
@@qms_rlp.trg
