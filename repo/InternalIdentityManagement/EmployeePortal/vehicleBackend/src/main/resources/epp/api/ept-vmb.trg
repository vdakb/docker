-- D:\Project\Bundeskriminalamt12c\InternalIdentityManagement\EmployeePortal\portalBackend\..\vehicleBackend\src\main\resources\dbs\epp\api\ept-vmb.trg
--
-- Generated for Oracle Database 12c on Sun May 24 17:30:58 2020 by Server Generator 10.1.2.93.10

PROMPT Creating Trigger Logic for Table 'ept_vehicle_brands'
PROMPT Creating Before Insert Statement Trigger on 'ept_vehicle_brands'
CREATE OR REPLACE TRIGGER cg$bis_ept_vehicle_brands
BEFORE INSERT ON ept_vehicle_brands
BEGIN
  -- Application_logic Pre-Before-Insert-statement <<Start>>
  -- Application_logic Pre-Before-Insert-statement <<End>>

  cg$ept_vehicle_brands.cg$table.DELETE;
  cg$ept_vehicle_brands.cg$tableind.DELETE;
  cg$ept_vehicle_brands.idx := 1;

  -- Application_logic Post-Before-Insert-statement <<Start>>
  -- Application_logic Post-Before-Insert-statement <<End>>
END;
/
SHOW ERROR

PROMPT Creating Before Insert Row Trigger on 'ept_vehicle_brands'
CREATE OR REPLACE TRIGGER cg$bir_ept_vehicle_brands
BEFORE INSERT ON ept_vehicle_brands FOR EACH ROW
DECLARE
  cg$rec cg$ept_vehicle_brands.cg$row_type;
  cg$ind cg$ept_vehicle_brands.cg$ind_type;
BEGIN
  -- Application_logic Pre-Before-Insert-row <<Start>>
  -- Application_logic Pre-Before-Insert-row << End >>

  -- Load cg$rec/cg$ind values from new
  cg$rec.id := :new.id;
  cg$ind.id := TRUE;
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
  cg$rec.name := :new.name;
  IF (:new.name IS NULL)
  THEN
    cg$ind.name := FALSE;
  ELSE
    cg$ind.name := TRUE;
  END IF;
  cg$rec.icon := :new.icon;
  IF (:new.icon IS NULL)
  THEN
    cg$ind.icon := FALSE;
  ELSE
    cg$ind.icon := TRUE;
  END IF;

  IF NOT (cg$ept_vehicle_brands.called_from_package)
  THEN
    cg$ept_vehicle_brands.validate_arc(cg$rec);
    cg$ept_vehicle_brands.validate_domain(cg$rec);

    cg$ept_vehicle_brands.ins(cg$rec, cg$ind, FALSE);
    cg$ept_vehicle_brands.called_from_package := FALSE;
  END IF;

  cg$ept_vehicle_brands.cg$table(cg$ept_vehicle_brands.idx).id            := cg$rec.id;
  cg$ept_vehicle_brands.cg$tableind(cg$ept_vehicle_brands.idx).id         := cg$ind.id;

  cg$ept_vehicle_brands.cg$table(cg$ept_vehicle_brands.idx).rowversion    := cg$rec.rowversion;
  cg$ept_vehicle_brands.cg$tableind(cg$ept_vehicle_brands.idx).rowversion := cg$ind.rowversion;

  cg$ept_vehicle_brands.cg$table(cg$ept_vehicle_brands.idx).created_by    := cg$rec.created_by;
  cg$ept_vehicle_brands.cg$tableind(cg$ept_vehicle_brands.idx).created_by := cg$ind.created_by;

  cg$ept_vehicle_brands.cg$table(cg$ept_vehicle_brands.idx).created_on    := cg$rec.created_on;
  cg$ept_vehicle_brands.cg$tableind(cg$ept_vehicle_brands.idx).created_on := cg$ind.created_on;

  cg$ept_vehicle_brands.cg$table(cg$ept_vehicle_brands.idx).updated_by    := cg$rec.updated_by;
  cg$ept_vehicle_brands.cg$tableind(cg$ept_vehicle_brands.idx).updated_by := cg$ind.updated_by;

  cg$ept_vehicle_brands.cg$table(cg$ept_vehicle_brands.idx).updated_on    := cg$rec.updated_on;
  cg$ept_vehicle_brands.cg$tableind(cg$ept_vehicle_brands.idx).updated_on := cg$ind.updated_on;

  cg$ept_vehicle_brands.cg$table(cg$ept_vehicle_brands.idx).name          := cg$rec.name;
  cg$ept_vehicle_brands.cg$tableind(cg$ept_vehicle_brands.idx).name       := cg$ind.name;

  cg$ept_vehicle_brands.cg$table(cg$ept_vehicle_brands.idx).icon          := cg$rec.icon;
  cg$ept_vehicle_brands.cg$tableind(cg$ept_vehicle_brands.idx).icon       := cg$ind.icon;

  cg$ept_vehicle_brands.idx := cg$ept_vehicle_brands.idx + 1;

  :new.id         := cg$rec.id;
  :new.rowversion := cg$rec.rowversion;
  :new.created_by := cg$rec.created_by;
  :new.created_on := cg$rec.created_on;
  :new.updated_by := cg$rec.updated_by;
  :new.updated_on := cg$rec.updated_on;
  :new.name       := cg$rec.name;
  :new.icon       := cg$rec.icon;

  -- Application_logic Post-Before-Insert-row <<Start>>
  -- Application_logic Post-Before-Insert-row << End >>
END;
/
SHOW ERROR

-- No application logic defined for Trigger cg$air_ept_vehicle_brands, so drop it.
-- To avoid an error if there isn't one, create or replace it, and then drop it
PROMPT Creating After Insert Row Trigger on 'ept_vehicle_brands'
CREATE OR REPLACE TRIGGER cg$air_ept_vehicle_brands
AFTER INSERT ON ept_vehicle_brands FOR EACH ROW
BEGIN
  NULL;
END;
/
SHOW ERROR
PROMPT Dropping After Insert Row Trigger on 'ept_vehicle_brands'
DROP TRIGGER cg$air_ept_vehicle_brands
/

PROMPT Creating After Insert Statement Trigger on 'ept_vehicle_brands'
CREATE OR REPLACE TRIGGER cg$ais_ept_vehicle_brands
AFTER INSERT ON ept_vehicle_brands
DECLARE
  fk_check   INTEGER;
  idx        BINARY_INTEGER := cg$ept_vehicle_brands.cg$table.FIRST;
  cg$rec     cg$ept_vehicle_brands.cg$row_type;
  cg$old_rec cg$ept_vehicle_brands.cg$row_type;
BEGIN
  -- Application_logic Pre-After-Insert-statement <<Start>>
  -- Application_logic Pre-After-Insert-statement <<End>>

  IF NOT (cg$ept_vehicle_brands.called_from_package)
  THEN
    WHILE idx IS NOT NULL
    LOOP
      cg$rec.id         := cg$ept_vehicle_brands.cg$table(idx).id;
      cg$rec.rowversion := cg$ept_vehicle_brands.cg$table(idx).rowversion;
      cg$rec.created_by := cg$ept_vehicle_brands.cg$table(idx).created_by;
      cg$rec.created_on := cg$ept_vehicle_brands.cg$table(idx).created_on;
      cg$rec.updated_by := cg$ept_vehicle_brands.cg$table(idx).updated_by;
      cg$rec.updated_on := cg$ept_vehicle_brands.cg$table(idx).updated_on;
      cg$rec.name       := cg$ept_vehicle_brands.cg$table(idx).name;
      cg$rec.icon       := cg$ept_vehicle_brands.cg$table(idx).icon;

      cg$ept_vehicle_brands.validate_foreign_keys_ins(cg$rec);
      cg$ept_vehicle_brands.upd_oper_denorm2(cg$rec, cg$old_rec, cg$ept_vehicle_brands.cg$tableind(idx), 'INS');

      idx := cg$ept_vehicle_brands.cg$table.NEXT(idx);
    END LOOP;
  END IF;

  -- Application_logic Post-After-Insert-statement <<Start>>
  -- Application_logic Post-After-Insert-statement <<End>>
END;
/
SHOW ERROR

PROMPT Creating Before Update Statement Trigger on 'ept_vehicle_brands'
CREATE OR REPLACE TRIGGER cg$bus_ept_vehicle_brands
BEFORE UPDATE ON ept_vehicle_brands
BEGIN
  -- Application_logic Pre-Before-Update-statement <<Start>>
  -- Application_logic Pre-Before-Update-statement <<End>>

  cg$ept_vehicle_brands.cg$table.DELETE;
  cg$ept_vehicle_brands.cg$tableind.DELETE;
  cg$ept_vehicle_brands.idx := 1;

  -- Application_logic Post-Before-Update-statement <<Start>>
  -- Application_logic Post-Before-Update-statement <<End>>
END;
/
SHOW ERROR

PROMPT Creating Before Update Row Trigger on 'ept_vehicle_brands'
CREATE OR REPLACE TRIGGER cg$bur_ept_vehicle_brands
BEFORE UPDATE ON ept_vehicle_brands FOR EACH ROW
DECLARE
  cg$rec     cg$ept_vehicle_brands.cg$row_type;
  cg$ind     cg$ept_vehicle_brands.cg$ind_type;
  cg$old_rec cg$ept_vehicle_brands.cg$row_type;
BEGIN
  -- Application_logic Pre-Before-Update-row <<Start>>
  -- Application_logic Pre-Before-Update-row << End >>

  -- Load cg$rec/cg$ind values from new
  cg$rec.id := :new.id;
  cg$ind.id := (:new.id IS NULL AND :old.id IS NOT NULL)
            OR (:old.id IS NULL AND :new.id IS NOT NULL)
            OR NOT (:new.id = :old.id);
  cg$ept_vehicle_brands.cg$table(cg$ept_vehicle_brands.idx).id := :old.id;

  cg$rec.rowversion := :new.rowversion;
  cg$ind.rowversion := (:new.rowversion IS NULL AND :old.rowversion IS NOT NULL)
                    OR (:old.rowversion IS NULL AND :new.rowversion IS NOT NULL)
                    OR NOT (:new.rowversion = :old.rowversion);
  cg$ept_vehicle_brands.cg$table(cg$ept_vehicle_brands.idx).rowversion := :old.rowversion;

  cg$rec.created_by := :new.created_by;
  cg$ind.created_by := (:new.created_by IS NULL AND :old.created_by IS NOT NULL)
                    OR (:old.created_by IS NULL AND :new.created_by IS NOT NULL)
                    OR NOT (:new.created_by = :old.created_by);
  cg$ept_vehicle_brands.cg$table(cg$ept_vehicle_brands.idx).created_by := :old.created_by;

  cg$rec.created_on := :new.created_on;
  cg$ind.created_on := (:new.created_on IS NULL AND :old.created_on IS NOT NULL)
                    OR (:old.created_on IS NULL AND :new.created_on IS NOT NULL)
                    OR NOT (:new.created_on = :old.created_on);
  cg$ept_vehicle_brands.cg$table(cg$ept_vehicle_brands.idx).created_on := :old.created_on;

  cg$rec.updated_by := :new.updated_by;
  cg$ind.updated_by := (:new.updated_by IS NULL AND :old.updated_by IS NOT NULL)
                    OR (:old.updated_by IS NULL AND :new.updated_by IS NOT NULL)
                    OR NOT (:new.updated_by = :old.updated_by);
  cg$ept_vehicle_brands.cg$table(cg$ept_vehicle_brands.idx).updated_by := :old.updated_by;

  cg$rec.updated_on := :new.updated_on;
  cg$ind.updated_on := (:new.updated_on IS NULL AND :old.updated_on IS NOT NULL)
                    OR (:old.updated_on IS NULL AND :new.updated_on IS NOT NULL)
                    OR NOT (:new.updated_on = :old.updated_on);
  cg$ept_vehicle_brands.cg$table(cg$ept_vehicle_brands.idx).updated_on := :old.updated_on;

  cg$rec.name := :new.name;
  cg$ind.name := (:new.name IS NULL AND :old.name IS NOT NULL)
              OR (:old.name IS NULL AND :new.name IS NOT NULL)
              OR NOT (:new.name = :old.name);
  cg$ept_vehicle_brands.cg$table(cg$ept_vehicle_brands.idx).name := :old.name;

  cg$rec.icon := :new.icon;
  cg$ind.icon := (:new.icon IS NULL AND :old.icon IS NOT NULL)
              OR (:old.icon IS NULL AND :new.icon IS NOT NULL)
              OR NOT (:new.icon = :old.icon);
  cg$ept_vehicle_brands.cg$table(cg$ept_vehicle_brands.idx).icon := :old.icon;

  cg$ept_vehicle_brands.idx := cg$ept_vehicle_brands.idx + 1;

  IF NOT (cg$ept_vehicle_brands.called_from_package)
  THEN
    cg$ept_vehicle_brands.validate_arc(cg$rec);
    cg$ept_vehicle_brands.validate_domain(cg$rec, cg$ind);
    cg$ept_vehicle_brands.validate_domain_cascade_update(cg$old_rec);

    cg$ept_vehicle_brands.upd(cg$rec, cg$ind, FALSE);
    cg$ept_vehicle_brands.called_from_package := FALSE;
  END IF;

  :new.id         := cg$rec.id;
  :new.rowversion := cg$rec.rowversion;
  :new.created_by := cg$rec.created_by;
  :new.created_on := cg$rec.created_on;
  :new.updated_by := cg$rec.updated_by;
  :new.updated_on := cg$rec.updated_on;
  :new.name       := cg$rec.name;
  :new.icon       := cg$rec.icon;

  -- Application_logic Post-Before-Insert-row <<Start>>
  -- Application_logic Post-Before-Insert-row << End >>
END;
/
SHOW ERROR

-- No application logic defined for Trigger cg$aur_ept_vehicle_brands, so drop it.
-- To avoid an error if there isn't one, create or replace it, and then drop it
PROMPT Creating After Update Row Trigger on 'ept_vehicle_brands'
CREATE OR REPLACE TRIGGER cg$aur_ept_vehicle_brands
AFTER INSERT ON ept_vehicle_brands FOR EACH ROW
BEGIN
  NULL;
END;
/
SHOW ERROR
PROMPT Dropping After Update Row Trigger on 'ept_vehicle_brands'
DROP TRIGGER cg$aur_ept_vehicle_brands
/

PROMPT Creating After Update Statement Trigger on 'ept_vehicle_brands'
CREATE OR REPLACE TRIGGER cg$aus_ept_vehicle_brands
AFTER UPDATE ON ept_vehicle_brands
DECLARE
  idx        BINARY_INTEGER := cg$ept_vehicle_brands.cg$table.FIRST;
  cg$old_rec cg$ept_vehicle_brands.cg$row_type;
  cg$rec     cg$ept_vehicle_brands.cg$row_type;
  cg$ind     cg$ept_vehicle_brands.cg$ind_type;
BEGIN
  -- Application_logic Pre-After-Update-statement <<Start>>
  -- Application_logic Pre-After-Update-statement <<End>>

  WHILE idx IS NOT NULL
  LOOP
    cg$old_rec.id         := cg$ept_vehicle_brands.cg$table(idx).id;
    cg$old_rec.rowversion := cg$ept_vehicle_brands.cg$table(idx).rowversion;
    cg$old_rec.created_by := cg$ept_vehicle_brands.cg$table(idx).created_by;
    cg$old_rec.created_on := cg$ept_vehicle_brands.cg$table(idx).created_on;
    cg$old_rec.updated_by := cg$ept_vehicle_brands.cg$table(idx).updated_by;
    cg$old_rec.updated_on := cg$ept_vehicle_brands.cg$table(idx).updated_on;
    cg$old_rec.name       := cg$ept_vehicle_brands.cg$table(idx).name;
    cg$old_rec.icon       := cg$ept_vehicle_brands.cg$table(idx).icon;

    IF NOT (cg$ept_vehicle_brands.called_from_package)
    THEN
      idx               := cg$ept_vehicle_brands.cg$table.NEXT(idx);
      cg$rec.id         := cg$ept_vehicle_brands.cg$table(idx).id;
      cg$ind.id         := updating('id');
      cg$rec.rowversion := cg$ept_vehicle_brands.cg$table(idx).rowversion;
      cg$ind.rowversion := updating('rowversion');
      cg$rec.created_by := cg$ept_vehicle_brands.cg$table(idx).created_by;
      cg$ind.created_by := updating('created_by');
      cg$rec.created_on := cg$ept_vehicle_brands.cg$table(idx).created_on;
      cg$ind.created_on := updating('created_on');
      cg$rec.updated_by := cg$ept_vehicle_brands.cg$table(idx).updated_by;
      cg$ind.updated_by := updating('updated_by');
      cg$rec.updated_on := cg$ept_vehicle_brands.cg$table(idx).updated_on;
      cg$ind.updated_on := updating('updated_on');
      cg$rec.name       := cg$ept_vehicle_brands.cg$table(idx).name;
      cg$ind.name       := updating('name');
      cg$rec.icon       := cg$ept_vehicle_brands.cg$table(idx).icon;
      cg$ind.icon       := updating('icon');

      cg$ept_vehicle_brands.validate_foreign_keys_upd(cg$rec, cg$old_rec, cg$ind);
      cg$ept_vehicle_brands.upd_denorm2(cg$rec, cg$ept_vehicle_brands.cg$tableind(idx));
      cg$ept_vehicle_brands.cascade_update(cg$rec, cg$old_rec);
      cg$ept_vehicle_brands.domain_cascade_update(cg$rec, cg$ind, cg$old_rec);

      cg$ept_vehicle_brands.called_from_package := FALSE;
    END IF;
    idx := cg$ept_vehicle_brands.cg$table.NEXT(idx);
  END LOOP;

  cg$ept_vehicle_brands.cg$table.DELETE;

  -- Application_logic Post-After-Insert-statement <<Start>>
  -- Application_logic Post-After-Insert-statement <<End>>
END;
/
SHOW ERROR

PROMPT Creating Before Delete Statement Trigger on 'ept_vehicle_brands'
CREATE OR REPLACE TRIGGER cg$bds_ept_vehicle_brands
BEFORE DELETE ON ept_vehicle_brands
BEGIN
  -- Application_logic Pre-Before-Delete-statement <<Start>>
  -- Application_logic Pre-Before-Delete-statement <<End>>

  cg$ept_vehicle_brands.cg$table.DELETE;
  cg$ept_vehicle_brands.cg$tableind.DELETE;
  cg$ept_vehicle_brands.idx := 1;

  -- Application_logic Post-Before-Delete-statement <<Start>>
  -- Application_logic Post-Before-Delete-statement <<End>>
END;
/
SHOW ERROR

PROMPT Creating Before Delete Row Trigger on 'ept_vehicle_brands'
CREATE OR REPLACE TRIGGER cg$bdr_ept_vehicle_brands
BEFORE DELETE ON ept_vehicle_brands FOR EACH ROW
DECLARE
  cg$pk  cg$ept_vehicle_brands.cg$pk_type;
  cg$rec cg$ept_vehicle_brands.cg$row_type;
  cg$ind cg$ept_vehicle_brands.cg$ind_type;
BEGIN
  -- Application_logic Pre-Before-Delete-row <<Start>>
  -- Application_logic Pre-Before-Delete-row << End >>

  -- Load cg$rec/cg$ind values from old
  cg$pk.id  := :old.id;
  cg$rec.id := :old.id;
  cg$ept_vehicle_brands.cg$table(cg$ept_vehicle_brands.idx).id := :old.id;

  cg$ept_vehicle_brands.idx := cg$ept_vehicle_brands.idx + 1;
  IF NOT (cg$ept_vehicle_brands.called_from_package)
  THEN
    cg$ept_vehicle_brands.validate_domain_cascade_delete(cg$rec);
    cg$ept_vehicle_brands.del(cg$pk, FALSE);
    cg$ept_vehicle_brands.called_from_package := FALSE;
  END IF;

  -- Application_logic Post-Before-Insert-row <<Start>>
  -- Application_logic Post-Before-Insert-row << End >>
END;
/
SHOW ERROR

-- No application logic defined for Trigger cg$adr_ept_vehicle_brands, so drop it.
-- To avoid an error if there isn't one, create or replace it, and then drop it
PROMPT Creating After Delete Row Trigger on 'ept_vehicle_brands'
CREATE OR REPLACE TRIGGER cg$adr_ept_vehicle_brands
AFTER DELETE ON ept_vehicle_brands FOR EACH ROW
BEGIN
  NULL;
END;
/
SHOW ERROR
PROMPT Dropping After Delete Row Trigger on 'ept_vehicle_brands'
DROP TRIGGER cg$adr_ept_vehicle_brands
/

PROMPT Creating After Delete Statement Trigger on 'ept_vehicle_brands'
CREATE OR REPLACE TRIGGER cg$ads_ept_vehicle_brands
AFTER DELETE ON ept_vehicle_brands
DECLARE
  idx        BINARY_INTEGER := cg$ept_vehicle_brands.cg$table.FIRST;
  cg$rec     cg$ept_vehicle_brands.cg$row_type;
  cg$old_rec cg$ept_vehicle_brands.cg$row_type;
BEGIN
  -- Application_logic Pre-After-Delete-statement <<Start>>
  -- Application_logic Pre-After-Delete-statement <<End>>

  IF NOT (cg$ept_vehicle_brands.called_from_package)
  THEN
    WHILE idx IS NOT NULL
    LOOP
      cg$rec.id := cg$ept_vehicle_brands.cg$table(idx).id;
      cg$ept_vehicle_brands.cg$tableind(idx).id := TRUE;

      cg$rec.rowversion := cg$ept_vehicle_brands.cg$table(idx).rowversion;
      cg$ept_vehicle_brands.cg$tableind(idx).rowversion := TRUE;

      cg$rec.created_by := cg$ept_vehicle_brands.cg$table(idx).created_by;
      cg$ept_vehicle_brands.cg$tableind(idx).created_by := TRUE;

      cg$rec.created_on := cg$ept_vehicle_brands.cg$table(idx).created_on;
      cg$ept_vehicle_brands.cg$tableind(idx).created_on := TRUE;

      cg$rec.updated_by := cg$ept_vehicle_brands.cg$table(idx).updated_by;
      cg$ept_vehicle_brands.cg$tableind(idx).updated_by := TRUE;

      cg$rec.updated_on := cg$ept_vehicle_brands.cg$table(idx).updated_on;
      cg$ept_vehicle_brands.cg$tableind(idx).updated_on := TRUE;

      cg$rec.name := cg$ept_vehicle_brands.cg$table(idx).name;
      cg$ept_vehicle_brands.cg$tableind(idx).name := TRUE;

      cg$rec.icon := cg$ept_vehicle_brands.cg$table(idx).icon;
      cg$ept_vehicle_brands.cg$tableind(idx).icon := TRUE;

      cg$ept_vehicle_brands.validate_foreign_keys_del(cg$rec);
      cg$ept_vehicle_brands.upd_oper_denorm2(cg$rec, cg$old_rec, cg$ept_vehicle_brands.cg$tableind(idx), 'DEL');
      cg$ept_vehicle_brands.cascade_delete(cg$rec);
      cg$ept_vehicle_brands.domain_cascade_delete(cg$rec);

      idx := cg$ept_vehicle_brands.cg$table.NEXT(idx);
    END LOOP;
  END IF;

  -- Application_logic Post-After-Delete-statement <<Start>>
  -- Application_logic Post-After-Delete-statement <<End>>
END;
/
SHOW ERROR