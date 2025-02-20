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
@@uit_ptt.pks
@@uit_cnt.pks
@@uit_sts.pks
@@uit_pts.pks
@@uit_typ.pks
@@uit_tnt.pks
@@uit_clm.pks
@@uit_uid.pks
-----------------------------------------------------------------------------------------
-- CREATE Core Table API implementation
-----------------------------------------------------------------------------------------
@@uit_ptt.pkb
@@uit_cnt.pkb
@@uit_sts.pkb
@@uit_pts.pkb
@@uit_typ.pkb
@@uit_tnt.pkb
@@uit_clm.pkb
@@uit_uid.pkb
-----------------------------------------------------------------------------------------
-- CREATE Core Table API interfaces
-----------------------------------------------------------------------------------------
@@uit_ptt.trg
@@uit_cnt.trg
@@uit_sts.trg
@@uit_pts.trg
@@uit_typ.trg
@@uit_tnt.trg
@@uit_clm.trg
@@uit_uid.trg
-----------------------------------------------------------------------------------------
-- CREATE Administration API specification
-----------------------------------------------------------------------------------------
@@uid_tnt.pks
-----------------------------------------------------------------------------------------
-- CREATE Administration API implementation
-----------------------------------------------------------------------------------------
@@uid_tnt.pkb
