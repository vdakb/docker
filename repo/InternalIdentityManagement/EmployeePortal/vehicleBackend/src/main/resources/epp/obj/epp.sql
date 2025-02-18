Rem *
Rem * Switch console output off.
Rem * Setting this option to ON is not necessary
Rem *
set echo off

Rem *
Rem * Switch spool ON
Rem *
spool ./log/epp/obj.log

Rem *
Rem * Switch schema
Rem *
ALTER SESSION SET CURRENT_SCHEMA = &&eppschema
/

PROMPT ********************************************************************
PROMPT * Creating Employee Self Service Portal Vehicle Objects
PROMPT ********************************************************************
-----------------------------------------------------------------------------------------
-- CREATE tables
-----------------------------------------------------------------------------------------
@@ept-vhc.tab
@@ept-vht.tab
@@ept-vmb.tab
@@ept-vmt.tab
@@ept-vhl.tab
-----------------------------------------------------------------------------------------
-- CREATE constraints
-----------------------------------------------------------------------------------------
@@ept-vhc.con
@@ept-vht.con
@@ept-vmb.con
@@ept-vmt.con
@@ept-vhl.con
-----------------------------------------------------------------------------------------
-- CREATE indexes
-----------------------------------------------------------------------------------------
@@ept-vmt.ind
@@ept-vhl.ind

Rem *
Rem * Switch spool OFF
Rem *
spool off