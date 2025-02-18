-- D:\Project\Bundeskriminalamt12c\ExternalIdentityManagement\IdentityManager\bkaSimulation\src\main\resources\dbs\pcf\api\pct-rol.trg
--
-- Generated for Oracle Database 12c on Sun June 21 17:02:48 2020 by Server Generator 10.1.2.93.10

PROMPT Creating Before Insert Row Trigger on 'pct_grp'
CREATE OR REPLACE TRIGGER cg$bir_pct_grp
BEFORE INSERT ON pct_grp FOR EACH ROW
BEGIN
  IF :new.id IS NULL
  THEN
    :new.id := sys_guid();
  END IF;
  :new.rowversion := TO_CHAR(systimestamp, 'YYYYMMDDHH24MISS.FF');
  :new.created_on := sysdate;
  :new.updated_on := sysdate;
  IF :new.validfrom IS NULL
  THEN
    :new.validfrom := sysdate;
  END IF;
END;
/
SHOW ERROR

PROMPT Creating Before Update Row Trigger on 'pct_grp'
CREATE OR REPLACE TRIGGER cg$bur_pct_grp
BEFORE UPDATE ON pct_grp FOR EACH ROW
BEGIN
  :new.rowversion := TO_CHAR(systimestamp, 'YYYYMMDDHH24MISS.FF');
  :new.updated_on := sysdate;
END;
/
SHOW ERROR