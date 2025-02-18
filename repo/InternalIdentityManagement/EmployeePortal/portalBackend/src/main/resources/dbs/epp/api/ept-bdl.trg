-- D:\Project\Bundeskriminalamt12c\InternalIdentityManagement\EmployeePortal\portalBackend\src\main\resources\dbs\epp\api\ept-bdl.trg
--
-- Generated for Oracle Database 12c on Sun May 24 17:02:48 2020 by Server Generator 10.1.2.93.10

PROMPT Creating Trigger Logic for Table 'ept_bundles'
PROMPT Creating Before Insert Statement Trigger on 'ept_bundles'
CREATE OR REPLACE TRIGGER cg$bis_ept_bundles
BEFORE INSERT ON ept_bundles
BEGIN
  -- Application_logic Pre-Before-Insert-statement <<Start>>
  -- Application_logic Pre-Before-Insert-statement <<End>>

  cg$ept_bundles.cg$table.DELETE;
  cg$ept_bundles.cg$tableind.DELETE;
  cg$ept_bundles.idx := 1;

  -- Application_logic Post-Before-Insert-statement <<Start>>
  -- Application_logic Post-Before-Insert-statement <<End>>
END;
/
SHOW ERROR

PROMPT Creating Before Insert Row Trigger on 'ept_bundles'
CREATE OR REPLACE TRIGGER cg$bir_ept_bundles
BEFORE INSERT ON ept_bundles FOR EACH ROW
DECLARE
  cg$rec cg$ept_bundles.cg$row_type;
  cg$ind cg$ept_bundles.cg$ind_type;
BEGIN
  -- Application_logic Pre-Before-Insert-row <<Start>>
  -- Application_logic Pre-Before-Insert-row << End >>

  -- Load cg$rec/cg$ind values from new
  cg$rec.loc_id := :new.loc_id;
  cg$ind.loc_id := TRUE;
  cg$rec.entity := :new.entity;
  cg$ind.entity := TRUE;
  cg$rec.item   := :new.item;
  cg$ind.item   := TRUE;
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
  cg$rec.meaning := :new.meaning;
  IF (:new.meaning IS NULL)
  THEN
    cg$ind.meaning := FALSE;
  ELSE
    cg$ind.meaning := TRUE;
  END IF;

  IF NOT (cg$ept_bundles.called_from_package)
  THEN
    cg$ept_bundles.validate_arc(cg$rec);
    cg$ept_bundles.validate_domain(cg$rec);

    cg$ept_bundles.ins(cg$rec, cg$ind, FALSE);
    cg$ept_bundles.called_from_package := FALSE;
  END IF;

  cg$ept_bundles.cg$table(cg$ept_bundles.idx).loc_id        := cg$rec.loc_id;
  cg$ept_bundles.cg$tableind(cg$ept_bundles.idx).loc_id     := cg$ind.loc_id;

  cg$ept_bundles.cg$table(cg$ept_bundles.idx).entity        := cg$rec.entity;
  cg$ept_bundles.cg$tableind(cg$ept_bundles.idx).entity     := cg$ind.entity;

  cg$ept_bundles.cg$table(cg$ept_bundles.idx).item          := cg$rec.item;
  cg$ept_bundles.cg$tableind(cg$ept_bundles.idx).item       := cg$ind.item;

  cg$ept_bundles.cg$table(cg$ept_bundles.idx).rowversion    := cg$rec.rowversion;
  cg$ept_bundles.cg$tableind(cg$ept_bundles.idx).rowversion := cg$ind.rowversion;

  cg$ept_bundles.cg$table(cg$ept_bundles.idx).created_by    := cg$rec.created_by;
  cg$ept_bundles.cg$tableind(cg$ept_bundles.idx).created_by := cg$ind.created_by;

  cg$ept_bundles.cg$table(cg$ept_bundles.idx).created_on    := cg$rec.created_on;
  cg$ept_bundles.cg$tableind(cg$ept_bundles.idx).created_on := cg$ind.created_on;

  cg$ept_bundles.cg$table(cg$ept_bundles.idx).updated_by    := cg$rec.updated_by;
  cg$ept_bundles.cg$tableind(cg$ept_bundles.idx).updated_by := cg$ind.updated_by;

  cg$ept_bundles.cg$table(cg$ept_bundles.idx).updated_on    := cg$rec.updated_on;
  cg$ept_bundles.cg$tableind(cg$ept_bundles.idx).updated_on := cg$ind.updated_on;

  cg$ept_bundles.cg$table(cg$ept_bundles.idx).meaning       := cg$rec.meaning;
  cg$ept_bundles.cg$tableind(cg$ept_bundles.idx).meaning    := cg$ind.meaning;

  cg$ept_bundles.idx := cg$ept_bundles.idx + 1;

  :new.loc_id     := cg$rec.loc_id;
  :new.entity     := cg$rec.entity;
  :new.item       := cg$rec.item;
  :new.rowversion := cg$rec.rowversion;
  :new.created_by := cg$rec.created_by;
  :new.created_on := cg$rec.created_on;
  :new.updated_by := cg$rec.updated_by;
  :new.updated_on := cg$rec.updated_on;
  :new.meaning    := cg$rec.meaning;

  -- Application_logic Post-Before-Insert-row <<Start>>
  -- Application_logic Post-Before-Insert-row << End >>
END;
/
SHOW ERROR

-- No application logic defined for Trigger cg$air_ept_bundles, so drop it.
-- To avoid an error if there isn't one, create or replace it, and then drop it
PROMPT Creating After Insert Row Trigger on 'ept_bundles'
CREATE OR REPLACE TRIGGER cg$air_ept_bundles
AFTER INSERT ON ept_bundles FOR EACH ROW
BEGIN
  NULL;
END;
/
SHOW ERROR
PROMPT Dropping After Insert Row Trigger on 'ept_bundles'
DROP TRIGGER cg$air_ept_bundles
/

PROMPT Creating After Insert Statement Trigger on 'ept_bundles'
CREATE OR REPLACE TRIGGER cg$ais_ept_bundles
AFTER INSERT ON ept_bundles
DECLARE
  fk_check   INTEGER;
  idx        BINARY_INTEGER := cg$ept_bundles.cg$table.FIRST;
  cg$rec     cg$ept_bundles.cg$row_type;
  cg$old_rec cg$ept_bundles.cg$row_type;
BEGIN
  -- Application_logic Pre-After-Insert-statement <<Start>>
  -- Application_logic Pre-After-Insert-statement <<End>>

  IF NOT (cg$ept_bundles.called_from_package)
  THEN
    WHILE idx IS NOT NULL
    LOOP
      cg$rec.loc_id     := cg$ept_bundles.cg$table(idx).loc_id;
      cg$rec.entity     := cg$ept_bundles.cg$table(idx).entity;
      cg$rec.item       := cg$ept_bundles.cg$table(idx).item;
      cg$rec.rowversion := cg$ept_bundles.cg$table(idx).rowversion;
      cg$rec.created_by := cg$ept_bundles.cg$table(idx).created_by;
      cg$rec.created_on := cg$ept_bundles.cg$table(idx).created_on;
      cg$rec.updated_by := cg$ept_bundles.cg$table(idx).updated_by;
      cg$rec.updated_on := cg$ept_bundles.cg$table(idx).updated_on;
      cg$rec.meaning    := cg$ept_bundles.cg$table(idx).meaning;

      cg$ept_bundles.validate_foreign_keys_ins(cg$rec);
      cg$ept_bundles.upd_oper_denorm2(cg$rec, cg$old_rec, cg$ept_bundles.cg$tableind(idx), 'INS');

      idx := cg$ept_bundles.cg$table.NEXT(idx);
    END LOOP;
  END IF;

  -- Application_logic Post-After-Insert-statement <<Start>>
  -- Application_logic Post-After-Insert-statement <<End>>
END;
/
SHOW ERROR

PROMPT Creating Before Update Statement Trigger on 'ept_bundles'
CREATE OR REPLACE TRIGGER cg$bus_ept_bundles
BEFORE UPDATE ON ept_bundles
BEGIN
  -- Application_logic Pre-Before-Update-statement <<Start>>
  -- Application_logic Pre-Before-Update-statement <<End>>

  cg$ept_bundles.cg$table.DELETE;
  cg$ept_bundles.cg$tableind.DELETE;
  cg$ept_bundles.idx := 1;

  -- Application_logic Post-Before-Update-statement <<Start>>
  -- Application_logic Post-Before-Update-statement <<End>>
END;
/
SHOW ERROR

PROMPT Creating Before Update Row Trigger on 'ept_bundles'
CREATE OR REPLACE TRIGGER cg$bur_ept_bundles
BEFORE UPDATE ON ept_bundles FOR EACH ROW
DECLARE
  cg$rec     cg$ept_bundles.cg$row_type;
  cg$ind     cg$ept_bundles.cg$ind_type;
  cg$old_rec cg$ept_bundles.cg$row_type;
BEGIN
  -- Application_logic Pre-Before-Update-row <<Start>>
  -- Application_logic Pre-Before-Update-row << End >>

  -- Load cg$rec/cg$ind values from new
  cg$rec.loc_id := :new.loc_id;
  cg$ind.loc_id := (:new.loc_id IS NULL AND :old.loc_id IS NOT NULL)
                OR (:old.loc_id IS NULL AND :new.loc_id IS NOT NULL)
                OR NOT (:new.loc_id = :old.loc_id);
  cg$ept_bundles.cg$table(cg$ept_bundles.idx).loc_id := :old.loc_id;

  cg$rec.entity := :new.entity;
  cg$ind.entity := (:new.entity IS NULL AND :old.entity IS NOT NULL)
                OR (:old.entity IS NULL AND :new.entity IS NOT NULL)
                OR NOT (:new.entity = :old.entity);
  cg$ept_bundles.cg$table(cg$ept_bundles.idx).entity := :old.entity;

  cg$rec.item := :new.item;
  cg$ind.item := (:new.item IS NULL AND :old.item IS NOT NULL)
              OR (:old.item IS NULL AND :new.item IS NOT NULL)
              OR NOT (:new.item = :old.item);
  cg$ept_bundles.cg$table(cg$ept_bundles.idx).item := :old.item;

  cg$rec.rowversion := :new.rowversion;
  cg$ind.rowversion := (:new.rowversion IS NULL AND :old.rowversion IS NOT NULL)
                    OR (:old.rowversion IS NULL AND :new.rowversion IS NOT NULL)
                    OR NOT (:new.rowversion = :old.rowversion);
  cg$ept_bundles.cg$table(cg$ept_bundles.idx).rowversion := :old.rowversion;

  cg$rec.created_by := :new.created_by;
  cg$ind.created_by := (:new.created_by IS NULL AND :old.created_by IS NOT NULL)
                    OR (:old.created_by IS NULL AND :new.created_by IS NOT NULL)
                    OR NOT (:new.created_by = :old.created_by);
  cg$ept_bundles.cg$table(cg$ept_bundles.idx).created_by := :old.created_by;

  cg$rec.created_on := :new.created_on;
  cg$ind.created_on := (:new.created_on IS NULL AND :old.created_on IS NOT NULL)
                    OR (:old.created_on IS NULL AND :new.created_on IS NOT NULL)
                    OR NOT (:new.created_on = :old.created_on);
  cg$ept_bundles.cg$table(cg$ept_bundles.idx).created_on := :old.created_on;

  cg$rec.updated_by := :new.updated_by;
  cg$ind.updated_by := (:new.updated_by IS NULL AND :old.updated_by IS NOT NULL)
                    OR (:old.updated_by IS NULL AND :new.updated_by IS NOT NULL)
                    OR NOT (:new.updated_by = :old.updated_by);
  cg$ept_bundles.cg$table(cg$ept_bundles.idx).updated_by := :old.updated_by;

  cg$rec.updated_on := :new.updated_on;
  cg$ind.updated_on := (:new.updated_on IS NULL AND :old.updated_on IS NOT NULL)
                    OR (:old.updated_on IS NULL AND :new.updated_on IS NOT NULL)
                    OR NOT (:new.updated_on = :old.updated_on);
  cg$ept_bundles.cg$table(cg$ept_bundles.idx).updated_on := :old.updated_on;

  cg$rec.meaning := :new.meaning;
  cg$ind.meaning := (:new.meaning IS NULL AND :old.meaning IS NOT NULL)
                 OR (:old.meaning IS NULL AND :new.meaning IS NOT NULL)
                 OR NOT (:new.meaning = :old.meaning);
  cg$ept_bundles.cg$table(cg$ept_bundles.idx).meaning := :old.meaning;

  cg$ept_bundles.idx := cg$ept_bundles.idx + 1;

  IF NOT (cg$ept_bundles.called_from_package)
  THEN
    cg$ept_bundles.validate_arc(cg$rec);
    cg$ept_bundles.validate_domain(cg$rec, cg$ind);
    cg$ept_bundles.validate_domain_cascade_update(cg$old_rec);

    cg$ept_bundles.upd(cg$rec, cg$ind, FALSE);
    cg$ept_bundles.called_from_package := FALSE;
  END IF;

  :new.loc_id     := cg$rec.loc_id;
  :new.entity     := cg$rec.entity;
  :new.item       := cg$rec.item;
  :new.rowversion := cg$rec.rowversion;
  :new.created_by := cg$rec.created_by;
  :new.created_on := cg$rec.created_on;
  :new.updated_by := cg$rec.updated_by;
  :new.updated_on := cg$rec.updated_on;
  :new.meaning    := cg$rec.meaning;

  -- Application_logic Post-Before-Insert-row <<Start>>
  -- Application_logic Post-Before-Insert-row << End >>
END;
/
SHOW ERROR

-- No application logic defined for Trigger cg$aur_ept_bundles, so drop it.
-- To avoid an error if there isn't one, create or replace it, and then drop it
PROMPT Creating After Update Row Trigger on 'ept_bundles'
CREATE OR REPLACE TRIGGER cg$aur_ept_bundles
AFTER INSERT ON ept_bundles FOR EACH ROW
BEGIN
  NULL;
END;
/
SHOW ERROR
PROMPT Dropping After Update Row Trigger on 'ept_bundles'
DROP TRIGGER cg$aur_ept_bundles
/

PROMPT Creating After Update Statement Trigger on 'ept_bundles'
CREATE OR REPLACE TRIGGER cg$aus_ept_bundles
AFTER UPDATE ON ept_bundles
DECLARE
  idx        BINARY_INTEGER := cg$ept_bundles.cg$table.FIRST;
  cg$old_rec cg$ept_bundles.cg$row_type;
  cg$rec     cg$ept_bundles.cg$row_type;
  cg$ind     cg$ept_bundles.cg$ind_type;
BEGIN
  -- Application_logic Pre-After-Update-statement <<Start>>
  -- Application_logic Pre-After-Update-statement <<End>>

  WHILE idx IS NOT NULL
  LOOP
    cg$old_rec.loc_id     := cg$ept_bundles.cg$table(idx).loc_id;
    cg$old_rec.entity     := cg$ept_bundles.cg$table(idx).entity;
    cg$old_rec.item       := cg$ept_bundles.cg$table(idx).item;
    cg$old_rec.rowversion := cg$ept_bundles.cg$table(idx).rowversion;
    cg$old_rec.created_by := cg$ept_bundles.cg$table(idx).created_by;
    cg$old_rec.created_on := cg$ept_bundles.cg$table(idx).created_on;
    cg$old_rec.updated_by := cg$ept_bundles.cg$table(idx).updated_by;
    cg$old_rec.updated_on := cg$ept_bundles.cg$table(idx).updated_on;
    cg$old_rec.meaning    := cg$ept_bundles.cg$table(idx).meaning;

    IF NOT (cg$ept_bundles.called_from_package)
    THEN
      idx               := cg$ept_bundles.cg$table.NEXT(idx);
      cg$rec.loc_id     := cg$ept_bundles.cg$table(idx).loc_id;
      cg$ind.loc_id     := updating('loc_id');
      cg$rec.entity     := cg$ept_bundles.cg$table(idx).entity;
      cg$ind.entity     := updating('entity');
      cg$rec.item       := cg$ept_bundles.cg$table(idx).item;
      cg$ind.item       := updating('item');
      cg$rec.rowversion := cg$ept_bundles.cg$table(idx).rowversion;
      cg$ind.rowversion := updating('rowversion');
      cg$rec.created_by := cg$ept_bundles.cg$table(idx).created_by;
      cg$ind.created_by := updating('created_by');
      cg$rec.created_on := cg$ept_bundles.cg$table(idx).created_on;
      cg$ind.created_on := updating('created_on');
      cg$rec.updated_by := cg$ept_bundles.cg$table(idx).updated_by;
      cg$ind.updated_by := updating('updated_by');
      cg$rec.updated_on := cg$ept_bundles.cg$table(idx).updated_on;
      cg$ind.updated_on := updating('updated_on');
      cg$rec.meaning    := cg$ept_bundles.cg$table(idx).meaning;
      cg$ind.meaning    := updating('meaning');

      cg$ept_bundles.validate_foreign_keys_upd(cg$rec, cg$old_rec, cg$ind);
      cg$ept_bundles.upd_denorm2(cg$rec, cg$ept_bundles.cg$tableind(idx));
      cg$ept_bundles.cascade_update(cg$rec, cg$old_rec);
      cg$ept_bundles.domain_cascade_update(cg$rec, cg$ind, cg$old_rec);

      cg$ept_bundles.called_from_package := FALSE;
    END IF;
    idx := cg$ept_bundles.cg$table.NEXT(idx);
  END LOOP;

  cg$ept_bundles.cg$table.DELETE;

  -- Application_logic Post-After-Insert-statement <<Start>>
  -- Application_logic Post-After-Insert-statement <<End>>
END;
/
SHOW ERROR

PROMPT Creating Before Delete Statement Trigger on 'ept_bundles'
CREATE OR REPLACE TRIGGER cg$bds_ept_bundles
BEFORE DELETE ON ept_bundles
BEGIN
  -- Application_logic Pre-Before-Delete-statement <<Start>>
  -- Application_logic Pre-Before-Delete-statement <<End>>

  cg$ept_bundles.cg$table.DELETE;
  cg$ept_bundles.cg$tableind.DELETE;
  cg$ept_bundles.idx := 1;

  -- Application_logic Post-Before-Delete-statement <<Start>>
  -- Application_logic Post-Before-Delete-statement <<End>>
END;
/
SHOW ERROR

PROMPT Creating Before Delete Row Trigger on 'ept_bundles'
CREATE OR REPLACE TRIGGER cg$bdr_ept_bundles
BEFORE DELETE ON ept_bundles FOR EACH ROW
DECLARE
  cg$pk  cg$ept_bundles.cg$pk_type;
  cg$rec cg$ept_bundles.cg$row_type;
  cg$ind cg$ept_bundles.cg$ind_type;
BEGIN
  -- Application_logic Pre-Before-Delete-row <<Start>>
  -- Application_logic Pre-Before-Delete-row << End >>

  -- Load cg$rec/cg$ind values from old
  cg$pk.loc_id  := :old.loc_id;
  cg$rec.loc_id := :old.loc_id;
  cg$pk.entity  := :old.entity;
  cg$rec.entity := :old.entity;
  cg$pk.item    := :old.item;
  cg$rec.item   := :old.item;
  cg$ept_bundles.cg$table(cg$ept_bundles.idx).loc_id := :old.loc_id;
  cg$ept_bundles.cg$table(cg$ept_bundles.idx).entity := :old.entity;
  cg$ept_bundles.cg$table(cg$ept_bundles.idx).item   := :old.item;

  cg$ept_bundles.idx := cg$ept_bundles.idx + 1;
  IF NOT (cg$ept_bundles.called_from_package)
  THEN
    cg$ept_bundles.validate_domain_cascade_delete(cg$rec);
    cg$ept_bundles.del(cg$pk, FALSE);
    cg$ept_bundles.called_from_package := FALSE;
  END IF;

  -- Application_logic Post-Before-Insert-row <<Start>>
  -- Application_logic Post-Before-Insert-row << End >>
END;
/
SHOW ERROR

-- No application logic defined for Trigger cg$adr_ept_bundles, so drop it.
-- To avoid an error if there isn't one, create or replace it, and then drop it
PROMPT Creating After Delete Row Trigger on 'ept_bundles'
CREATE OR REPLACE TRIGGER cg$adr_ept_bundles
AFTER DELETE ON ept_bundles FOR EACH ROW
BEGIN
  NULL;
END;
/
SHOW ERROR
PROMPT Dropping After Delete Row Trigger on 'ept_bundles'
DROP TRIGGER cg$adr_ept_bundles
/

PROMPT Creating After Delete Statement Trigger on 'ept_bundles'
CREATE OR REPLACE TRIGGER cg$ads_ept_bundles
AFTER DELETE ON ept_bundles
DECLARE
  idx        BINARY_INTEGER := cg$ept_bundles.cg$table.FIRST;
  cg$rec     cg$ept_bundles.cg$row_type;
  cg$old_rec cg$ept_bundles.cg$row_type;
BEGIN
  -- Application_logic Pre-After-Delete-statement <<Start>>
  -- Application_logic Pre-After-Delete-statement <<End>>

  IF NOT (cg$ept_bundles.called_from_package)
  THEN
    WHILE idx IS NOT NULL
    LOOP
      cg$rec.loc_id := cg$ept_bundles.cg$table(idx).loc_id;
      cg$ept_bundles.cg$tableind(idx).loc_id := TRUE;

      cg$rec.entity := cg$ept_bundles.cg$table(idx).entity;
      cg$ept_bundles.cg$tableind(idx).entity := TRUE;

      cg$rec.item := cg$ept_bundles.cg$table(idx).item;
      cg$ept_bundles.cg$tableind(idx).item := TRUE;

      cg$rec.rowversion := cg$ept_bundles.cg$table(idx).rowversion;
      cg$ept_bundles.cg$tableind(idx).rowversion := TRUE;

      cg$rec.created_by := cg$ept_bundles.cg$table(idx).created_by;
      cg$ept_bundles.cg$tableind(idx).created_by := TRUE;

      cg$rec.created_on := cg$ept_bundles.cg$table(idx).created_on;
      cg$ept_bundles.cg$tableind(idx).created_on := TRUE;

      cg$rec.updated_by := cg$ept_bundles.cg$table(idx).updated_by;
      cg$ept_bundles.cg$tableind(idx).updated_by := TRUE;

      cg$rec.updated_on := cg$ept_bundles.cg$table(idx).updated_on;
      cg$ept_bundles.cg$tableind(idx).updated_on := TRUE;

      cg$rec.meaning := cg$ept_bundles.cg$table(idx).meaning;
      cg$ept_bundles.cg$tableind(idx).meaning := TRUE;

      cg$ept_bundles.validate_foreign_keys_del(cg$rec);
      cg$ept_bundles.upd_oper_denorm2(cg$rec, cg$old_rec, cg$ept_bundles.cg$tableind(idx), 'DEL');
      cg$ept_bundles.cascade_delete(cg$rec);
      cg$ept_bundles.domain_cascade_delete(cg$rec);

      idx := cg$ept_bundles.cg$table.NEXT(idx);
    END LOOP;
  END IF;

  -- Application_logic Post-After-Delete-statement <<Start>>
  -- Application_logic Post-After-Delete-statement <<End>>
END;
/
SHOW ERROR