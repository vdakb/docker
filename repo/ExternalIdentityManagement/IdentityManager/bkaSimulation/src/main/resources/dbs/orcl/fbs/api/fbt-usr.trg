-- D:\Project\Bundeskriminalamt12c\ExternalIdentityManagement\IdentityManager\bkaSimulation\src\main\resources\dbs\fbs\api\ept-usr.trg
--
-- Generated for Oracle Database 12c on Sun June 21 17:02:48 2020 by Server Generator 10.1.2.93.10

PROMPT Creating Before Insert Row Trigger on 'fbt_users'
CREATE OR REPLACE TRIGGER cg$bir_fbt_users
BEFORE INSERT ON fbt_users FOR EACH ROW
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
  IF :new.active IS NULL
  THEN
    :new.active := '1';
  END IF;
END;
/
SHOW ERROR

PROMPT Creating Before Update Row Trigger on 'fbt_users'
CREATE OR REPLACE TRIGGER cg$bur_fbt_users
BEFORE UPDATE ON fbt_users FOR EACH ROW
BEGIN
  :new.rowversion := TO_CHAR(systimestamp, 'YYYYMMDDHH24MISS.FF');
  :new.updated_on := sysdate;
END;
/
SHOW ERROR