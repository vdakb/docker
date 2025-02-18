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
Rem Revisions   Date        Editor      Comment
Rem -----------+-----------+-----------+-----------------------------------
Rem 1.0.0.0     2022-03-11  DSteding    First release version
Rem

-----------------------------------------------------------------------------------------
-- CREATE tables
-----------------------------------------------------------------------------------------
@@uit_ptt.tab
@@uit_cnt.tab
@@uit_sts.tab
@@uit_pts.tab
@@uit_typ.tab
@@uit_tnt.tab
@@uit_clm.tab
@@uit_uid.tab
-----------------------------------------------------------------------------------------
-- CREATE constraints
-----------------------------------------------------------------------------------------
@@uit_ptt.con
@@uit_cnt.con
@@uit_sts.con
@@uit_pts.con
@@uit_typ.con
@@uit_tnt.con
@@uit_clm.con
@@uit_uid.con
-----------------------------------------------------------------------------------------
-- CREATE indexes
-----------------------------------------------------------------------------------------
@@uit_clm.ind
@@uit_uid.ind