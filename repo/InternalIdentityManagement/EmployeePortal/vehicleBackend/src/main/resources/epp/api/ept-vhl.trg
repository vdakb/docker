-- D:\Project\Bundeskriminalamt12c\InternalIdentityManagement\EmployeePortal\portalBackend\..\vehicleBackend\src\main\resources\dbs\epp\api\ept-vhl.trg
--
-- Generated for Oracle Database 12c on Sun May 24 17:30:58 2020 by Server Generator 10.1.2.93.10

PROMPT Creating Trigger Logic for Table 'ept_vehicles'
PROMPT Creating Before Insert Statement Trigger on 'ept_vehicles'
CREATE OR REPLACE TRIGGER cg$bis_ept_vehicles
BEFORE INSERT ON ept_vehicles
BEGIN
  -- Application_logic Pre-Before-Insert-statement <<Start>>
  -- Application_logic Pre-Before-Insert-statement <<End>>

  cg$ept_vehicles.cg$table.DELETE;
  cg$ept_vehicles.cg$tableind.DELETE;
  cg$ept_vehicles.idx := 1;

  -- Application_logic Post-Before-Insert-statement <<Start>>
  -- Application_logic Post-Before-Insert-statement <<End>>
END;
/
SHOW ERROR

PROMPT Creating Before Insert Row Trigger on 'ept_vehicles'
CREATE OR REPLACE TRIGGER cg$bir_ept_vehicles
BEFORE INSERT ON ept_vehicles FOR EACH ROW
DECLARE
  cg$rec cg$ept_vehicles.cg$row_type;
  cg$ind cg$ept_vehicles.cg$ind_type;
BEGIN
  -- Application_logic Pre-Before-Insert-row <<Start>>
  -- Application_logic Pre-Before-Insert-row << End >>

  -- Load cg$rec/cg$ind values from new
  cg$rec.prefix := :new.prefix;
  cg$ind.prefix := TRUE;
  cg$rec.code := :new.code;
  IF (:new.code IS NULL)
  THEN
    cg$ind.code := FALSE;
  ELSE
    cg$ind.code := TRUE;
  END IF;
  cg$rec.rowversion := :new.rowversion;
  IF (:new.rowversion IS NULL)
  THEN
    cg$ind.rowversion := FALSE;
  ELSE
    cg$ind.rowversion := TRUE;
  END IF;
  cg$rec.created_by := :new.created_by;
  IF (:new.created_by IS NULL)
  THEN
    cg$ind.created_by := FALSE;
  ELSE
    cg$ind.created_by := TRUE;
  END IF;
  cg$rec.created_on := :new.created_on;
  IF (:new.created_on IS NULL)
  THEN
    cg$ind.created_on := FALSE;
  ELSE
    cg$ind.created_on := TRUE;
  END IF;
  cg$rec.updated_by := :new.updated_by;
  IF (:new.updated_by IS NULL)
  THEN
    cg$ind.updated_by := FALSE;
  ELSE
    cg$ind.updated_by := TRUE;
  END IF;
  cg$rec.updated_on := :new.updated_on;
  IF (:new.updated_on IS NULL)
  THEN
    cg$ind.updated_on := FALSE;
  ELSE
    cg$ind.updated_on := TRUE;
  END IF;
  cg$rec.vmb_id := :new.vmb_id;
  IF (:new.vmb_id IS NULL)
  THEN
    cg$ind.vmb_id := FALSE;
  ELSE
    cg$ind.vmb_id := TRUE;
  END IF;
  cg$rec.vht_id := :new.vht_id;
  IF (:new.vht_id IS NULL)
  THEN
    cg$ind.vht_id := FALSE;
  ELSE
    cg$ind.vht_id := TRUE;
  END IF;
  cg$rec.vhc_id := :new.vhc_id;
  IF (:new.vhc_id IS NULL)
  THEN
    cg$ind.vhc_id := FALSE;
  ELSE
    cg$ind.vhc_id := TRUE;
  END IF;
  cg$rec.driver := :new.driver;
  IF (:new.driver IS NULL)
  THEN
    cg$ind.driver := FALSE;
  ELSE
    cg$ind.driver := TRUE;
  END IF;

  IF NOT (cg$ept_vehicles.called_from_package)
  THEN
    cg$ept_vehicles.validate_arc(cg$rec);
    cg$ept_vehicles.validate_domain(cg$rec);

    cg$ept_vehicles.ins(cg$rec, cg$ind, FALSE);
    cg$ept_vehicles.called_from_package := FALSE;
  END IF;

  cg$ept_vehicles.cg$table(cg$ept_vehicles.idx).prefix        := cg$rec.prefix;
  cg$ept_vehicles.cg$tableind(cg$ept_vehicles.idx).prefix     := cg$ind.prefix;

  cg$ept_vehicles.cg$table(cg$ept_vehicles.idx).code          := cg$rec.code;
  cg$ept_vehicles.cg$tableind(cg$ept_vehicles.idx).code       := cg$ind.code;

  cg$ept_vehicles.cg$table(cg$ept_vehicles.idx).rowversion    := cg$rec.rowversion;
  cg$ept_vehicles.cg$tableind(cg$ept_vehicles.idx).rowversion := cg$ind.rowversion;

  cg$ept_vehicles.cg$table(cg$ept_vehicles.idx).created_by    := cg$rec.created_by;
  cg$ept_vehicles.cg$tableind(cg$ept_vehicles.idx).created_by := cg$ind.created_by;

  cg$ept_vehicles.cg$table(cg$ept_vehicles.idx).created_on    := cg$rec.created_on;
  cg$ept_vehicles.cg$tableind(cg$ept_vehicles.idx).created_on := cg$ind.created_on;

  cg$ept_vehicles.cg$table(cg$ept_vehicles.idx).updated_by    := cg$rec.updated_by;
  cg$ept_vehicles.cg$tableind(cg$ept_vehicles.idx).updated_by := cg$ind.updated_by;

  cg$ept_vehicles.cg$table(cg$ept_vehicles.idx).updated_on    := cg$rec.updated_on;
  cg$ept_vehicles.cg$tableind(cg$ept_vehicles.idx).updated_on := cg$ind.updated_on;

  cg$ept_vehicles.cg$table(cg$ept_vehicles.idx).vmb_id        := cg$rec.vmb_id;
  cg$ept_vehicles.cg$tableind(cg$ept_vehicles.idx).vmb_id     := cg$ind.vmb_id;

  cg$ept_vehicles.cg$table(cg$ept_vehicles.idx).vht_id        := cg$rec.vht_id;
  cg$ept_vehicles.cg$tableind(cg$ept_vehicles.idx).vht_id     := cg$ind.vht_id;

  cg$ept_vehicles.cg$table(cg$ept_vehicles.idx).vhc_id        := cg$rec.vhc_id;
  cg$ept_vehicles.cg$tableind(cg$ept_vehicles.idx).vhc_id     := cg$ind.vhc_id;

  cg$ept_vehicles.cg$table(cg$ept_vehicles.idx).driver        := cg$rec.driver;
  cg$ept_vehicles.cg$tableind(cg$ept_vehicles.idx).driver     := cg$ind.driver;

  cg$ept_vehicles.idx := cg$ept_vehicles.idx + 1;

  :new.prefix     := cg$rec.prefix;
  :new.code       := cg$rec.code;
  :new.rowversion := cg$rec.rowversion;
  :new.created_by := cg$rec.created_by;
  :new.created_on := cg$rec.created_on;
  :new.updated_by := cg$rec.updated_by;
  :new.updated_on := cg$rec.updated_on;
  :new.vmb_id     := cg$rec.vmb_id;
  :new.vht_id     := cg$rec.vht_id;
  :new.vhc_id     := cg$rec.vhc_id;
  :new.driver     := cg$rec.driver;

  -- Application_logic Post-Before-Insert-row <<Start>>
  -- Application_logic Post-Before-Insert-row << End >>
END;
/
SHOW ERROR

-- No application logic defined for Trigger cg$air_ept_vehicles, so drop it.
-- To avoid an error if there isn't one, create or replace it, and then drop it
PROMPT Creating After Insert Row Trigger on 'ept_vehicles'
CREATE OR REPLACE TRIGGER cg$air_ept_vehicles
AFTER INSERT ON ept_vehicles FOR EACH ROW
BEGIN
  NULL;
END;
/
SHOW ERROR
PROMPT Dropping After Insert Row Trigger on 'ept_vehicles'
DROP TRIGGER cg$air_ept_vehicles
/

PROMPT Creating After Insert Statement Trigger on 'ept_vehicles'
CREATE OR REPLACE TRIGGER cg$ais_ept_vehicles
AFTER INSERT ON ept_vehicles
DECLARE
  fk_check   INTEGER;
  idx        BINARY_INTEGER := cg$ept_vehicles.cg$table.FIRST;
  cg$rec     cg$ept_vehicles.cg$row_type;
  cg$old_rec cg$ept_vehicles.cg$row_type;
BEGIN
  -- Application_logic Pre-After-Insert-statement <<Start>>
  -- Application_logic Pre-After-Insert-statement <<End>>

  IF NOT (cg$ept_vehicles.called_from_package)
  THEN
    WHILE idx IS NOT NULL
    LOOP
      cg$rec.prefix     := cg$ept_vehicles.cg$table(idx).prefix;
      cg$rec.code       := cg$ept_vehicles.cg$table(idx).code;
      cg$rec.rowversion := cg$ept_vehicles.cg$table(idx).rowversion;
      cg$rec.created_by := cg$ept_vehicles.cg$table(idx).created_by;
      cg$rec.created_on := cg$ept_vehicles.cg$table(idx).created_on;
      cg$rec.updated_by := cg$ept_vehicles.cg$table(idx).updated_by;
      cg$rec.updated_on := cg$ept_vehicles.cg$table(idx).updated_on;
      cg$rec.vmb_id     := cg$ept_vehicles.cg$table(idx).vmb_id;
      cg$rec.vht_id     := cg$ept_vehicles.cg$table(idx).vht_id;
      cg$rec.vhc_id     := cg$ept_vehicles.cg$table(idx).vhc_id;
      cg$rec.driver     := cg$ept_vehicles.cg$table(idx).driver;

      cg$ept_vehicles.validate_foreign_keys_ins(cg$rec);
      cg$ept_vehicles.upd_oper_denorm2(cg$rec, cg$old_rec, cg$ept_vehicles.cg$tableind(idx), 'INS');

      idx := cg$ept_vehicles.cg$table.NEXT(idx);
    END LOOP;
  END IF;

  -- Application_logic Post-After-Insert-statement <<Start>>
  -- Application_logic Post-After-Insert-statement <<End>>
END;
/
SHOW ERROR

PROMPT Creating Before Update Statement Trigger on 'ept_vehicles'
CREATE OR REPLACE TRIGGER cg$bus_ept_vehicles
BEFORE UPDATE ON ept_vehicles
BEGIN
  -- Application_logic Pre-Before-Update-statement <<Start>>
  -- Application_logic Pre-Before-Update-statement <<End>>

  cg$ept_vehicles.cg$table.DELETE;
  cg$ept_vehicles.cg$tableind.DELETE;
  cg$ept_vehicles.idx := 1;

  -- Application_logic Post-Before-Update-statement <<Start>>
  -- Application_logic Post-Before-Update-statement <<End>>
END;
/
SHOW ERROR

PROMPT Creating Before Update Row Trigger on 'ept_vehicles'
CREATE OR REPLACE TRIGGER cg$bur_ept_vehicles
BEFORE UPDATE ON ept_vehicles FOR EACH ROW
DECLARE
  cg$rec     cg$ept_vehicles.cg$row_type;
  cg$ind     cg$ept_vehicles.cg$ind_type;
  cg$old_rec cg$ept_vehicles.cg$row_type;
BEGIN
  -- Application_logic Pre-Before-Update-row <<Start>>
  -- Application_logic Pre-Before-Update-row << End >>

  -- Load cg$rec/cg$ind values from new
  cg$rec.prefix := :new.prefix;
  cg$ind.prefix := (:new.prefix IS NULL AND :old.prefix IS NOT NULL)
                OR (:old.prefix IS NULL AND :new.prefix IS NOT NULL)
                OR NOT (:new.prefix = :old.prefix);
  cg$ept_vehicles.cg$table(cg$ept_vehicles.idx).prefix := :old.prefix;

  cg$rec.code := :new.code;
  cg$ind.code := (:new.code IS NULL AND :old.code IS NOT NULL)
              OR (:old.code IS NULL AND :new.code IS NOT NULL)
              OR NOT (:new.code = :old.code);
  cg$ept_vehicles.cg$table(cg$ept_vehicles.idx).code := :old.code;

  cg$rec.rowversion := :new.rowversion;
  cg$ind.rowversion := (:new.rowversion IS NULL AND :old.rowversion IS NOT NULL)
                    OR (:old.rowversion IS NULL AND :new.rowversion IS NOT NULL)
                    OR NOT (:new.rowversion = :old.rowversion);
  cg$ept_vehicles.cg$table(cg$ept_vehicles.idx).rowversion := :old.rowversion;

  cg$rec.created_by := :new.created_by;
  cg$ind.created_by := (:new.created_by IS NULL AND :old.created_by IS NOT NULL)
                    OR (:old.created_by IS NULL AND :new.created_by IS NOT NULL)
                    OR NOT (:new.created_by = :old.created_by);
  cg$ept_vehicles.cg$table(cg$ept_vehicles.idx).created_by := :old.created_by;

  cg$rec.created_on := :new.created_on;
  cg$ind.created_on := (:new.created_on IS NULL AND :old.created_on IS NOT NULL)
                    OR (:old.created_on IS NULL AND :new.created_on IS NOT NULL)
                    OR NOT (:new.created_on = :old.created_on);
  cg$ept_vehicles.cg$table(cg$ept_vehicles.idx).created_on := :old.created_on;

  cg$rec.updated_by := :new.updated_by;
  cg$ind.updated_by := (:new.updated_by IS NULL AND :old.updated_by IS NOT NULL)
                    OR (:old.updated_by IS NULL AND :new.updated_by IS NOT NULL)
                    OR NOT (:new.updated_by = :old.updated_by);
  cg$ept_vehicles.cg$table(cg$ept_vehicles.idx).updated_by := :old.updated_by;

  cg$rec.updated_on := :new.updated_on;
  cg$ind.updated_on := (:new.updated_on IS NULL AND :old.updated_on IS NOT NULL)
                    OR (:old.updated_on IS NULL AND :new.updated_on IS NOT NULL)
                    OR NOT (:new.updated_on = :old.updated_on);
  cg$ept_vehicles.cg$table(cg$ept_vehicles.idx).updated_on := :old.updated_on;

  cg$rec.vmb_id := :new.vmb_id;
  cg$ind.vmb_id := (:new.vmb_id IS NULL AND :old.vmb_id IS NOT NULL)
                OR (:old.vmb_id IS NULL AND :new.vmb_id IS NOT NULL)
                OR NOT (:new.vmb_id = :old.vmb_id);
  cg$ept_vehicles.cg$table(cg$ept_vehicles.idx).vmb_id := :old.vmb_id;

  cg$rec.vht_id := :new.vht_id;
  cg$ind.vht_id := (:new.vht_id IS NULL AND :old.vht_id IS NOT NULL)
                OR (:old.vht_id IS NULL AND :new.vht_id IS NOT NULL)
                OR NOT (:new.vht_id = :old.vht_id);
  cg$ept_vehicles.cg$table(cg$ept_vehicles.idx).vht_id := :old.vht_id;

  cg$rec.vhc_id := :new.vhc_id;
  cg$ind.vhc_id := (:new.vhc_id IS NULL AND :old.vhc_id IS NOT NULL)
                OR (:old.vhc_id IS NULL AND :new.vhc_id IS NOT NULL)
                OR NOT (:new.vhc_id = :old.vhc_id);
  cg$ept_vehicles.cg$table(cg$ept_vehicles.idx).vhc_id := :old.vhc_id;

  cg$rec.driver := :new.driver;
  cg$ind.driver := (:new.driver IS NULL AND :old.driver IS NOT NULL)
                OR (:old.driver IS NULL AND :new.driver IS NOT NULL)
                OR NOT (:new.driver = :old.driver);
  cg$ept_vehicles.cg$table(cg$ept_vehicles.idx).driver := :old.driver;

  cg$ept_vehicles.idx := cg$ept_vehicles.idx + 1;

  IF NOT (cg$ept_vehicles.called_from_package)
  THEN
    cg$ept_vehicles.validate_arc(cg$rec);
    cg$ept_vehicles.validate_domain(cg$rec, cg$ind);
    cg$ept_vehicles.validate_domain_cascade_update(cg$old_rec);

    cg$ept_vehicles.upd(cg$rec, cg$ind, FALSE);
    cg$ept_vehicles.called_from_package := FALSE;
  END IF;

  :new.prefix     := cg$rec.prefix;
  :new.code       := cg$rec.code;
  :new.rowversion := cg$rec.rowversion;
  :new.created_by := cg$rec.created_by;
  :new.created_on := cg$rec.created_on;
  :new.updated_by := cg$rec.updated_by;
  :new.updated_on := cg$rec.updated_on;
  :new.vmb_id     := cg$rec.vmb_id;
  :new.vht_id     := cg$rec.vht_id;
  :new.vhc_id     := cg$rec.vhc_id;
  :new.driver     := cg$rec.driver;

  -- Application_logic Post-Before-Insert-row <<Start>>
  -- Application_logic Post-Before-Insert-row << End >>
END;
/
SHOW ERROR

-- No application logic defined for Trigger cg$aur_ept_vehicles, so drop it.
-- To avoid an error if there isn't one, create or replace it, and then drop it
PROMPT Creating After Update Row Trigger on 'ept_vehicles'
CREATE OR REPLACE TRIGGER cg$aur_ept_vehicles
AFTER INSERT ON ept_vehicles FOR EACH ROW
BEGIN
  NULL;
END;
/
SHOW ERROR
PROMPT Dropping After Update Row Trigger on 'ept_vehicles'
DROP TRIGGER cg$aur_ept_vehicles
/

PROMPT Creating After Update Statement Trigger on 'ept_vehicles'
CREATE OR REPLACE TRIGGER cg$aus_ept_vehicles
AFTER UPDATE ON ept_vehicles
DECLARE
  idx        BINARY_INTEGER := cg$ept_vehicles.cg$table.FIRST;
  cg$old_rec cg$ept_vehicles.cg$row_type;
  cg$rec     cg$ept_vehicles.cg$row_type;
  cg$ind     cg$ept_vehicles.cg$ind_type;
BEGIN
  -- Application_logic Pre-After-Update-statement <<Start>>
  -- Application_logic Pre-After-Update-statement <<End>>

  WHILE idx IS NOT NULL
  LOOP
    cg$old_rec.prefix     := cg$ept_vehicles.cg$table(idx).prefix;
    cg$old_rec.code       := cg$ept_vehicles.cg$table(idx).code;
    cg$old_rec.rowversion := cg$ept_vehicles.cg$table(idx).rowversion;
    cg$old_rec.created_by := cg$ept_vehicles.cg$table(idx).created_by;
    cg$old_rec.created_on := cg$ept_vehicles.cg$table(idx).created_on;
    cg$old_rec.updated_by := cg$ept_vehicles.cg$table(idx).updated_by;
    cg$old_rec.updated_on := cg$ept_vehicles.cg$table(idx).updated_on;
    cg$old_rec.vmb_id     := cg$ept_vehicles.cg$table(idx).vmb_id;
    cg$old_rec.vht_id     := cg$ept_vehicles.cg$table(idx).vht_id;
    cg$old_rec.vhc_id     := cg$ept_vehicles.cg$table(idx).vhc_id;
    cg$old_rec.driver     := cg$ept_vehicles.cg$table(idx).driver;

    IF NOT (cg$ept_vehicles.called_from_package)
    THEN
      idx               := cg$ept_vehicles.cg$table.NEXT(idx);
      cg$rec.prefix     := cg$ept_vehicles.cg$table(idx).prefix;
      cg$ind.prefix     := updating('prefix');
      cg$rec.code       := cg$ept_vehicles.cg$table(idx).code;
      cg$ind.code       := updating('code');
      cg$rec.rowversion := cg$ept_vehicles.cg$table(idx).rowversion;
      cg$ind.rowversion := updating('rowversion');
      cg$rec.created_by := cg$ept_vehicles.cg$table(idx).created_by;
      cg$ind.created_by := updating('created_by');
      cg$rec.created_on := cg$ept_vehicles.cg$table(idx).created_on;
      cg$ind.created_on := updating('created_on');
      cg$rec.updated_by := cg$ept_vehicles.cg$table(idx).updated_by;
      cg$ind.updated_by := updating('updated_by');
      cg$rec.updated_on := cg$ept_vehicles.cg$table(idx).updated_on;
      cg$ind.updated_on := updating('updated_on');
      cg$rec.vmb_id     := cg$ept_vehicles.cg$table(idx).vmb_id;
      cg$ind.vmb_id     := updating('vmb_id');
      cg$rec.vht_id     := cg$ept_vehicles.cg$table(idx).vht_id;
      cg$ind.vht_id     := updating('vht_id');
      cg$rec.vhc_id     := cg$ept_vehicles.cg$table(idx).vhc_id;
      cg$ind.vhc_id     := updating('vhc_id');
      cg$rec.driver     := cg$ept_vehicles.cg$table(idx).driver;
      cg$ind.driver     := updating('driver');

      cg$ept_vehicles.validate_foreign_keys_upd(cg$rec, cg$old_rec, cg$ind);
      cg$ept_vehicles.upd_denorm2(cg$rec, cg$ept_vehicles.cg$tableind(idx));
      cg$ept_vehicles.cascade_update(cg$rec, cg$old_rec);
      cg$ept_vehicles.domain_cascade_update(cg$rec, cg$ind, cg$old_rec);

      cg$ept_vehicles.called_from_package := FALSE;
    END IF;
    idx := cg$ept_vehicles.cg$table.NEXT(idx);
  END LOOP;

  cg$ept_vehicles.cg$table.DELETE;

  -- Application_logic Post-After-Insert-statement <<Start>>
  -- Application_logic Post-After-Insert-statement <<End>>
END;
/
SHOW ERROR

PROMPT Creating Before Delete Statement Trigger on 'ept_vehicles'
CREATE OR REPLACE TRIGGER cg$bds_ept_vehicles
BEFORE DELETE ON ept_vehicles
BEGIN
  -- Application_logic Pre-Before-Delete-statement <<Start>>
  -- Application_logic Pre-Before-Delete-statement <<End>>

  cg$ept_vehicles.cg$table.DELETE;
  cg$ept_vehicles.cg$tableind.DELETE;
  cg$ept_vehicles.idx := 1;

  -- Application_logic Post-Before-Delete-statement <<Start>>
  -- Application_logic Post-Before-Delete-statement <<End>>
END;
/
SHOW ERROR

PROMPT Creating Before Delete Row Trigger on 'ept_vehicles'
CREATE OR REPLACE TRIGGER cg$bdr_ept_vehicles
BEFORE DELETE ON ept_vehicles FOR EACH ROW
DECLARE
  cg$pk  cg$ept_vehicles.cg$pk_type;
  cg$rec cg$ept_vehicles.cg$row_type;
  cg$ind cg$ept_vehicles.cg$ind_type;
BEGIN
  -- Application_logic Pre-Before-Delete-row <<Start>>
  -- Application_logic Pre-Before-Delete-row << End >>

  -- Load cg$rec/cg$ind values from old
  cg$pk.prefix  := :old.prefix;
  cg$rec.prefix := :old.prefix;
  cg$ept_vehicles.cg$table(cg$ept_vehicles.idx).prefix := :old.prefix;

  cg$ept_vehicles.idx := cg$ept_vehicles.idx + 1;
  IF NOT (cg$ept_vehicles.called_from_package)
  THEN
    cg$ept_vehicles.validate_domain_cascade_delete(cg$rec);
    cg$ept_vehicles.del(cg$pk, FALSE);
    cg$ept_vehicles.called_from_package := FALSE;
  END IF;

  -- Application_logic Post-Before-Insert-row <<Start>>
  -- Application_logic Post-Before-Insert-row << End >>
END;
/
SHOW ERROR

-- No application logic defined for Trigger cg$adr_ept_vehicles, so drop it.
-- To avoid an error if there isn't one, create or replace it, and then drop it
PROMPT Creating After Delete Row Trigger on 'ept_vehicles'
CREATE OR REPLACE TRIGGER cg$adr_ept_vehicles
AFTER DELETE ON ept_vehicles FOR EACH ROW
BEGIN
  NULL;
END;
/
SHOW ERROR
PROMPT Dropping After Delete Row Trigger on 'ept_vehicles'
DROP TRIGGER cg$adr_ept_vehicles
/

PROMPT Creating After Delete Statement Trigger on 'ept_vehicles'
CREATE OR REPLACE TRIGGER cg$ads_ept_vehicles
AFTER DELETE ON ept_vehicles
DECLARE
  idx        BINARY_INTEGER := cg$ept_vehicles.cg$table.FIRST;
  cg$rec     cg$ept_vehicles.cg$row_type;
  cg$old_rec cg$ept_vehicles.cg$row_type;
BEGIN
  -- Application_logic Pre-After-Delete-statement <<Start>>
  -- Application_logic Pre-After-Delete-statement <<End>>

  IF NOT (cg$ept_vehicles.called_from_package)
  THEN
    WHILE idx IS NOT NULL
    LOOP
      cg$rec.prefix := cg$ept_vehicles.cg$table(idx).prefix;
      cg$ept_vehicles.cg$tableind(idx).prefix := TRUE;

      cg$rec.code := cg$ept_vehicles.cg$table(idx).code;
      cg$ept_vehicles.cg$tableind(idx).code := TRUE;

      cg$rec.rowversion := cg$ept_vehicles.cg$table(idx).rowversion;
      cg$ept_vehicles.cg$tableind(idx).rowversion := TRUE;

      cg$rec.created_by := cg$ept_vehicles.cg$table(idx).created_by;
      cg$ept_vehicles.cg$tableind(idx).created_by := TRUE;

      cg$rec.created_on := cg$ept_vehicles.cg$table(idx).created_on;
      cg$ept_vehicles.cg$tableind(idx).created_on := TRUE;

      cg$rec.updated_by := cg$ept_vehicles.cg$table(idx).updated_by;
      cg$ept_vehicles.cg$tableind(idx).updated_by := TRUE;

      cg$rec.updated_on := cg$ept_vehicles.cg$table(idx).updated_on;
      cg$ept_vehicles.cg$tableind(idx).updated_on := TRUE;

      cg$rec.vmb_id := cg$ept_vehicles.cg$table(idx).vmb_id;
      cg$ept_vehicles.cg$tableind(idx).vmb_id := TRUE;

      cg$rec.vht_id := cg$ept_vehicles.cg$table(idx).vht_id;
      cg$ept_vehicles.cg$tableind(idx).vht_id := TRUE;

      cg$rec.vhc_id := cg$ept_vehicles.cg$table(idx).vhc_id;
      cg$ept_vehicles.cg$tableind(idx).vhc_id := TRUE;

      cg$rec.driver := cg$ept_vehicles.cg$table(idx).driver;
      cg$ept_vehicles.cg$tableind(idx).driver := TRUE;

      cg$ept_vehicles.validate_foreign_keys_del(cg$rec);
      cg$ept_vehicles.upd_oper_denorm2(cg$rec, cg$old_rec, cg$ept_vehicles.cg$tableind(idx), 'DEL');
      cg$ept_vehicles.cascade_delete(cg$rec);
      cg$ept_vehicles.domain_cascade_delete(cg$rec);

      idx := cg$ept_vehicles.cg$table.NEXT(idx);
    END LOOP;
  END IF;

  -- Application_logic Post-After-Delete-statement <<Start>>
  -- Application_logic Post-After-Delete-statement <<End>>
END;
/
SHOW ERROR