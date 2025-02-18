-- /Users/dsteding/Project/Bundeskriminalamt12c/ExternalIdentityManagement/IdentityService/identifierBackend/src/main/resources/install/pid/api/pit_aid.trg
--
--   Anonymous Identifier (AID)
--
-- Generated for Oracle Database 12c on Fri Mar 11 12.47.21 2022 by Server Generator 10.1.2.93.10

PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating Trigger Logic for Table 'pit_identifiers'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating Before Insert Statement Trigger on 'pit_identifiers'
CREATE OR REPLACE TRIGGER cg$bis_pit_identifiers
BEFORE INSERT ON pit_identifiers
BEGIN
  -- Application_logic Pre-Before-Insert-statement <<Start>>
  -- Application_logic Pre-Before-Insert-statement << End >>

  cg$pit_identifiers.cg$table.DELETE;
  cg$pit_identifiers.cg$tableind.DELETE;
  cg$pit_identifiers.idx := 1;

  -- Application_logic Post-Before-Insert-statement <<Start>>
  -- Application_logic Post-Before-Insert-statement << End >>
END;
/
SHOW ERROR
PROMPT Creating Before Insert Row Trigger on 'pit_identifiers'
CREATE OR REPLACE TRIGGER cg$bir_pit_identifiers
BEFORE INSERT ON pit_identifiers FOR EACH ROW
DECLARE
  cg$rec cg$pit_identifiers.cg$row_type;
  cg$ind cg$pit_identifiers.cg$ind_type;
BEGIN
  -- Application_logic Pre-Before-Insert-row <<Start>>
  -- Application_logic Pre-Before-Insert-row << End >>

  -- Load cg$rec/cg$ind values from new
  cg$rec.id := :new.id;
  IF (:new.id IS NULL)
  THEN
    cg$ind.id := FALSE;
  ELSE
    cg$ind.id := TRUE;
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
  cg$rec.active := :new.active;
  IF (:new.active IS NULL)
  THEN
    cg$ind.active := FALSE;
  ELSE
    cg$ind.active := TRUE;
  END IF;
  cg$rec.usedby := :new.usedby;
  IF (:new.usedby IS NULL)
  THEN
    cg$ind.usedby := FALSE;
  ELSE
    cg$ind.usedby := TRUE;
  END IF;

  IF NOT (cg$pit_identifiers.called_from_package)
  THEN
    cg$pit_identifiers.ins(cg$rec, cg$ind, FALSE);
    cg$pit_identifiers.called_from_package := FALSE;
  END IF;

  cg$pit_identifiers.cg$table(cg$pit_identifiers.idx).id                := cg$rec.id;
  cg$pit_identifiers.cg$tableind(cg$pit_identifiers.idx).id             := cg$ind.id;

  cg$pit_identifiers.cg$table(cg$pit_identifiers.idx).rowversion        := cg$rec.rowversion;
  cg$pit_identifiers.cg$tableind(cg$pit_identifiers.idx).rowversion     := cg$ind.rowversion;

  cg$pit_identifiers.cg$table(cg$pit_identifiers.idx).created_by        := cg$rec.created_by;
  cg$pit_identifiers.cg$tableind(cg$pit_identifiers.idx).created_by     := cg$ind.created_by;

  cg$pit_identifiers.cg$table(cg$pit_identifiers.idx).created_on        := cg$rec.created_on;
  cg$pit_identifiers.cg$tableind(cg$pit_identifiers.idx).created_on     := cg$ind.created_on;

  cg$pit_identifiers.cg$table(cg$pit_identifiers.idx).updated_by        := cg$rec.updated_by;
  cg$pit_identifiers.cg$tableind(cg$pit_identifiers.idx).updated_by     := cg$ind.updated_by;

  cg$pit_identifiers.cg$table(cg$pit_identifiers.idx).updated_on        := cg$rec.updated_on;
  cg$pit_identifiers.cg$tableind(cg$pit_identifiers.idx).updated_on     := cg$ind.updated_on;

  cg$pit_identifiers.cg$table(cg$pit_identifiers.idx).active             := cg$rec.active;
  cg$pit_identifiers.cg$tableind(cg$pit_identifiers.idx).active          := cg$ind.active;

  cg$pit_identifiers.cg$table(cg$pit_identifiers.idx).usedby             := cg$rec.usedby;
  cg$pit_identifiers.cg$tableind(cg$pit_identifiers.idx).usedby          := cg$ind.usedby;

  cg$pit_identifiers.idx := cg$pit_identifiers.idx + 1;

  :new.id             := cg$rec.id;
  :new.rowversion     := cg$rec.rowversion;
  :new.created_by     := cg$rec.created_by;
  :new.created_on     := cg$rec.created_on;
  :new.updated_by     := cg$rec.updated_by;
  :new.updated_on     := cg$rec.updated_on;
  :new.updated_on     := cg$rec.updated_on;
  :new.active         := cg$rec.active;
  :new.usedby         := cg$rec.usedby;

  -- Application_logic Post-Before-Insert-row <<Start>>
  -- Application_logic Post-Before-Insert-row << End >>
END;
/
SHOW ERROR
PROMPT Creating After Insert Row Trigger on 'pit_identifiers'
CREATE OR REPLACE TRIGGER cg$air_pit_identifiers
AFTER INSERT ON pit_identifiers FOR EACH ROW
DECLARE
  cg$rec cg$pit_identifiers.cg$row_type;
  cg$ind cg$pit_identifiers.cg$ind_type;
BEGIN
  --  Application_logic Pre-After-Insert-row <<Start>>
  --  Application_logic Pre-After-Insert-row << End >>

  cg$rec.id           := :new.id;
  cg$ind.id           := TRUE;
  cg$rec.rowversion   := :new.created_by;
  cg$ind.rowversion   := TRUE;
  cg$rec.created_by   := :new.created_by;
  cg$ind.created_by   := TRUE;
  cg$rec.created_on   := :new.created_on;
  cg$ind.created_on   := TRUE;
  cg$rec.updated_by   := :new.updated_by;
  cg$ind.updated_by   := TRUE;
  cg$rec.updated_on   := :new.updated_on;
  cg$ind.updated_on   := TRUE;
  cg$rec.active       := :new.active;
  cg$ind.active       := TRUE;
  cg$rec.usedby       := :new.usedby;
  cg$ind.usedby       := TRUE;

  --  Application_logic Post-After-Insert-row <<Start>>
  --  Application_logic Post-After-Insert-row << End >>
END;
/
PROMPT Creating After Insert Statement Trigger on 'pit_identifiers'
CREATE OR REPLACE TRIGGER cg$ais_pit_identifiers
AFTER INSERT ON pit_identifiers
DECLARE
  fk_check   INTEGER;
  idx        BINARY_INTEGER := cg$pit_identifiers.cg$table.FIRST;
  cg$rec     cg$pit_identifiers.cg$row_type;
  cg$old_rec cg$pit_identifiers.cg$row_type;
BEGIN
  -- Application_logic Pre-After-Insert-statement <<Start>>
  -- Application_logic Pre-After-Insert-statement << End >>

  IF NOT (cg$pit_identifiers.called_from_package)
  THEN
    WHILE idx IS NOT NULL
    LOOP
      cg$rec.id             := cg$pit_identifiers.cg$table(idx).id;
      cg$rec.rowversion     := cg$pit_identifiers.cg$table(idx).rowversion;
      cg$rec.created_by     := cg$pit_identifiers.cg$table(idx).created_by;
      cg$rec.created_on     := cg$pit_identifiers.cg$table(idx).created_on;
      cg$rec.updated_by     := cg$pit_identifiers.cg$table(idx).updated_by;
      cg$rec.updated_on     := cg$pit_identifiers.cg$table(idx).updated_on;
      cg$rec.active         := cg$pit_identifiers.cg$table(idx).active;
      cg$rec.usedby         := cg$pit_identifiers.cg$table(idx).usedby;

      cg$pit_identifiers.validate_foreign_keys_ins(cg$rec);

      idx := cg$pit_identifiers.cg$table.NEXT(idx);
    END LOOP;
  END IF;

  -- Application_logic Post-After-Insert-statement <<Start>>
  -- Application_logic Post-After-Insert-statement << End >>
END;
/
SHOW ERROR
PROMPT Creating Before Update Statement Trigger on 'pit_identifiers'
CREATE OR REPLACE TRIGGER cg$bus_pit_identifiers
BEFORE UPDATE ON pit_identifiers
BEGIN
  -- Application_logic Pre-Before-Update-statement <<Start>>
  -- Application_logic Pre-Before-Update-statement << End >>

  cg$pit_identifiers.cg$table.DELETE;
  cg$pit_identifiers.cg$tableind.DELETE;
  cg$pit_identifiers.idx := 1;

  -- Application_logic Post-Before-Update-statement <<Start>>
  -- Application_logic Post-Before-Update-statement << End >>
END;
/
SHOW ERROR
PROMPT Creating Before Update Row Trigger on 'pit_identifiers'
CREATE OR REPLACE TRIGGER cg$bur_pit_identifiers
BEFORE UPDATE ON pit_identifiers FOR EACH ROW
DECLARE
  cg$rec     cg$pit_identifiers.cg$row_type;
  cg$ind     cg$pit_identifiers.cg$ind_type;
  cg$old_rec cg$pit_identifiers.cg$row_type;
BEGIN
  -- Application_logic Pre-Before-Update-row <<Start>>
  -- Application_logic Pre-Before-Update-row << End >>

  -- Load cg$rec/cg$ind values from new
  cg$rec.id  := :new.id;
  cg$ind.id  := (:new.id IS NULL AND :old.id IS NOT NULL)
             OR (:old.id IS NULL AND :new.id IS NOT NULL)
             OR NOT(:new.id = :old.id);
  cg$pit_identifiers.cg$table(cg$pit_identifiers.idx).id := :old.id;

  cg$rec.rowversion := :new.rowversion;
  cg$ind.rowversion := (:new.rowversion IS NULL AND :old.rowversion IS NOT NULL)
                    OR (:old.rowversion IS NULL AND :new.rowversion IS NOT NULL)
                    OR NOT(:new.rowversion = :old.rowversion);
  cg$pit_identifiers.cg$table(cg$pit_identifiers.idx).rowversion := :old.rowversion;

  cg$rec.created_by := :new.created_by;
  cg$ind.created_by := (:new.created_by IS NULL AND :old.created_by IS NOT NULL)
                    OR (:old.created_by IS NULL AND :new.created_by IS NOT NULL)
                    OR NOT(:new.created_by = :old.created_by);
  cg$pit_identifiers.cg$table(cg$pit_identifiers.idx).created_by := :old.created_by;

  cg$rec.created_on := :new.created_on;
  cg$ind.created_on := (:new.created_on IS NULL AND :old.created_on IS NOT NULL)
                    OR (:old.created_on IS NULL AND :new.created_on IS NOT NULL)
                    OR NOT(:new.created_on = :old.created_on);
  cg$pit_identifiers.cg$table(cg$pit_identifiers.idx).created_on := :old.created_on;

  cg$rec.updated_by := :new.updated_by;
  cg$ind.updated_by := (:new.updated_by IS NULL AND :old.updated_by IS NOT NULL)
                    OR (:old.updated_by IS NULL AND :new.updated_by IS NOT NULL)
                    OR NOT(:new.updated_by = :old.updated_by);
  cg$pit_identifiers.cg$table(cg$pit_identifiers.idx).updated_by := :old.updated_by;

  cg$rec.updated_on := :new.updated_on;
  cg$ind.updated_on := (:new.updated_on IS NULL AND :old.updated_on IS NOT NULL)
                    OR (:old.updated_on IS NULL AND :new.updated_on IS NOT NULL)
                    OR NOT(:new.updated_on = :old.updated_on);
  cg$pit_identifiers.cg$table(cg$pit_identifiers.idx).updated_on := :old.updated_on;

  cg$rec.active := :new.active;
  cg$ind.active := (:new.active IS NULL AND :old.active IS NOT NULL)
                    OR (:old.active IS NULL AND :new.active IS NOT NULL)
                    OR NOT(:new.active = :old.active);
  cg$pit_identifiers.cg$table(cg$pit_identifiers.idx).active := :old.active;

  cg$rec.usedby := :new.usedby;
  cg$ind.usedby := (:new.usedby IS NULL AND :old.usedby IS NOT NULL)
                    OR (:old.usedby IS NULL AND :new.usedby IS NOT NULL)
                    OR NOT(:new.usedby = :old.usedby);
  cg$pit_identifiers.cg$table(cg$pit_identifiers.idx).usedby := :old.usedby;

  cg$pit_identifiers.idx := cg$pit_identifiers.idx + 1;

  IF NOT (cg$pit_identifiers.called_from_package)
  THEN
    cg$pit_identifiers.upd(cg$rec, cg$ind, FALSE);
    cg$pit_identifiers.called_from_package := FALSE;
  END IF;

  :new.rowversion     := cg$rec.rowversion;
  :new.created_by     := cg$rec.created_by;
  :new.created_on     := cg$rec.created_on;
  :new.updated_by     := cg$rec.updated_by;
  :new.updated_on     := cg$rec.updated_on;
  :new.active         := cg$rec.active;
  :new.usedby         := cg$rec.usedby;

  -- Application_logic Post-Before-Update-row <<Start>>
  -- Application_logic Post-Before-Update-row << End >>
END;
/
SHOW ERROR
PROMPT Creating After Update Statement Trigger on 'pit_identifiers'
CREATE OR REPLACE TRIGGER cg$aus_pit_identifiers
AFTER UPDATE ON pit_identifiers
DECLARE
  idx        BINARY_INTEGER := cg$pit_identifiers.cg$table.FIRST;
  cg$old_rec cg$pit_identifiers.cg$row_type;
  cg$rec     cg$pit_identifiers.cg$row_type;
  cg$ind     cg$pit_identifiers.cg$ind_type;
BEGIN
  -- Application_logic Pre-After-Update-statement <<Start>>
  -- Application_logic Pre-After-Update-statement << End >>

  WHILE idx IS NOT NULL
  LOOP
    cg$old_rec.id         := cg$pit_identifiers.cg$table(idx).id;
    cg$old_rec.rowversion := cg$pit_identifiers.cg$table(idx).rowversion;
    cg$old_rec.created_by := cg$pit_identifiers.cg$table(idx).created_by;
    cg$old_rec.created_on := cg$pit_identifiers.cg$table(idx).created_on;
    cg$old_rec.updated_by := cg$pit_identifiers.cg$table(idx).updated_by;
    cg$old_rec.updated_on := cg$pit_identifiers.cg$table(idx).updated_on;
    cg$old_rec.active     := cg$pit_identifiers.cg$table(idx).active;
    cg$old_rec.usedby     := cg$pit_identifiers.cg$table(idx).usedby;

    IF NOT (cg$pit_identifiers.called_from_package)
    THEN
      idx                 := cg$pit_identifiers.cg$table.NEXT(idx);
      cg$rec.id           := cg$pit_identifiers.cg$table(idx).id;
      cg$ind.id           := updating('id');
      cg$rec.rowversion   := cg$pit_identifiers.cg$table(idx).rowversion;
      cg$ind.rowversion   := updating('rowversion');
      cg$rec.created_by   := cg$pit_identifiers.cg$table(idx).created_by;
      cg$ind.created_by   := updating('created_by');
      cg$rec.created_on   := cg$pit_identifiers.cg$table(idx).created_on;
      cg$ind.created_on   := updating('created_on');
      cg$rec.updated_by   := cg$pit_identifiers.cg$table(idx).updated_by;
      cg$ind.updated_by   := updating('updated_by');
      cg$rec.updated_on   := cg$pit_identifiers.cg$table(idx).updated_on;
      cg$ind.updated_on   := updating('updated_on');
      cg$rec.active       := cg$pit_identifiers.cg$table(idx).active;
      cg$ind.active       := updating('active');
      cg$rec.usedby       := cg$pit_identifiers.cg$table(idx).usedby;
      cg$ind.usedby       := updating('usedby');

      cg$pit_identifiers.validate_foreign_keys_upd(cg$rec, cg$old_rec, cg$ind);
      cg$pit_identifiers.cascade_update(cg$rec, cg$old_rec);

      cg$pit_identifiers.called_from_package := FALSE;
    END IF;
    idx := cg$pit_identifiers.cg$table.NEXT(idx);
  END LOOP;

  cg$pit_identifiers.cg$table.DELETE;

  -- Application_logic Post-After-Update-statement <<Start>>
  -- Application_logic Post-After-Update-statement << End >>
END;
/
SHOW ERROR
PROMPT Creating Before Delete Statement Trigger on 'pit_identifiers'
CREATE OR REPLACE TRIGGER cg$bds_pit_identifiers
BEFORE DELETE ON pit_identifiers
BEGIN
  -- Application_logic Pre-Before-Delete-statement <<Start>>
  -- Application_logic Pre-Before-Delete-statement << End >>

  cg$pit_identifiers.cg$table.DELETE;
  cg$pit_identifiers.cg$tableind.DELETE;
  cg$pit_identifiers.idx := 1;

  -- Application_logic Post-Before-Delete-statement <<Start>>
  -- Application_logic Post-Before-Delete-statement << End >>
END;
/
SHOW ERROR
PROMPT Creating Before Delete Row Trigger on 'pit_identifiers'
CREATE OR REPLACE TRIGGER cg$bdr_pit_identifiers
BEFORE DELETE ON pit_identifiers FOR EACH ROW
DECLARE
  cg$pk  cg$pit_identifiers.cg$pk_type;
  cg$rec cg$pit_identifiers.cg$row_type;
  cg$ind cg$pit_identifiers.cg$ind_type;
BEGIN
  -- Application_logic Pre-Before-Delete-row <<Start>>
  -- Application_logic Pre-Before-Delete-row << End >>

  -- Load cg$rec/cg$ind values from old
  cg$pk.id  := :old.id;
  cg$rec.id := :old.id;
  cg$pit_identifiers.cg$table(cg$pit_identifiers.idx).id := :old.id;

  cg$pit_identifiers.idx := cg$pit_identifiers.idx + 1;
  IF NOT (cg$pit_identifiers.called_from_package)
  THEN
    cg$pit_identifiers.del(cg$pk, FALSE);
    cg$pit_identifiers.called_from_package := FALSE;
  END IF;

  -- Application_logic Post-Before-Delete-row <<Start>>
  -- Application_logic Post-Before-Delete-row << End >>
END;
/
SHOW ERROR
PROMPT Creating After Delete Statement Trigger on 'pit_identifiers'
CREATE OR REPLACE TRIGGER cg$ads_pit_identifiers
AFTER DELETE ON pit_identifiers
DECLARE
  idx        BINARY_INTEGER := cg$pit_identifiers.cg$table.FIRST;
  cg$rec     cg$pit_identifiers.cg$row_type;
  cg$old_rec cg$pit_identifiers.cg$row_type;
BEGIN
  -- Application_logic Pre-After-Delete-statement <<Start>>
  -- Application_logic Pre-After-Delete-statement << End >>

  IF NOT (cg$pit_identifiers.called_from_package)
  THEN
    WHILE idx IS NOT NULL
    LOOP
      cg$rec.id := cg$pit_identifiers.cg$table(idx).id;
      cg$pit_identifiers.cg$tableind(idx).id := TRUE;

      cg$rec.rowversion := cg$pit_identifiers.cg$table(idx).rowversion;
      cg$pit_identifiers.cg$tableind(idx).rowversion := TRUE;

      cg$rec.created_by := cg$pit_identifiers.cg$table(idx).created_by;
      cg$pit_identifiers.cg$tableind(idx).created_by := TRUE;

      cg$rec.created_on := cg$pit_identifiers.cg$table(idx).created_on;
      cg$pit_identifiers.cg$tableind(idx).created_on := TRUE;

      cg$rec.updated_by := cg$pit_identifiers.cg$table(idx).updated_by;
      cg$pit_identifiers.cg$tableind(idx).updated_by := TRUE;

      cg$rec.updated_on := cg$pit_identifiers.cg$table(idx).updated_on;
      cg$pit_identifiers.cg$tableind(idx).updated_on := TRUE;

      cg$rec.active := cg$pit_identifiers.cg$table(idx).active;
      cg$pit_identifiers.cg$tableind(idx).active := TRUE;

      cg$rec.usedby := cg$pit_identifiers.cg$table(idx).usedby;
      cg$pit_identifiers.cg$tableind(idx).usedby := TRUE;

      cg$pit_identifiers.validate_foreign_keys_del(cg$rec);
      cg$pit_identifiers.cascade_delete(cg$rec);

      idx := cg$pit_identifiers.cg$table.NEXT(idx);
    END LOOP;
  END IF;

  -- Application_logic Post-After-Delete-statement <<Start>>
  -- Application_logic Post-After-Delete-statement << End >>
END;
/
SHOW ERROR