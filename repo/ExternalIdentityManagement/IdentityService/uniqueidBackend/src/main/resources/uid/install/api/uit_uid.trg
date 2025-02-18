-- /Users/dsteding/Project/Bundeskriminalamt12c/ExternalIdentityManagement/IdentityService/uniqueidBackend/src/main/resources/install/uid/api/uit_uid.trg
--
--   Einfuehrung einer personenbezogenen ID im Progamm Polizei 2020
--   Unique Identifier (UID)
--   Version 1.2 | Stand 15.03.2022
--
--   Reference to Page 21 Table 2
--
-- Generated for Oracle Database 12c on Fri Mar 11 12.47.21 2022 by Server Generator 10.1.2.93.10

PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating Trigger Logic for Table 'uit_identifiers'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating Before Insert Statement Trigger on 'uit_identifiers'
CREATE OR REPLACE TRIGGER cg$bis_uit_identifiers
BEFORE INSERT ON uit_identifiers
BEGIN
  -- Application_logic Pre-Before-Insert-statement <<Start>>
  -- Application_logic Pre-Before-Insert-statement << End >>

  cg$uit_identifiers.cg$table.DELETE;
  cg$uit_identifiers.cg$tableind.DELETE;
  cg$uit_identifiers.idx := 1;

  -- Application_logic Post-Before-Insert-statement <<Start>>
  -- Application_logic Post-Before-Insert-statement << End >>
END;
/
SHOW ERROR
PROMPT Creating Before Insert Row Trigger on 'uit_identifiers'
CREATE OR REPLACE TRIGGER cg$bir_uit_identifiers
BEFORE INSERT ON uit_identifiers FOR EACH ROW
DECLARE
  cg$rec cg$uit_identifiers.cg$row_type;
  cg$ind cg$uit_identifiers.cg$ind_type;
BEGIN
  -- Application_logic Pre-Before-Insert-row <<Start>>
  -- Application_logic Pre-Before-Insert-row << End >>

  -- Load cg$rec/cg$ind values from new
  cg$rec.tnt_id := :new.tnt_id;
  IF (:new.tnt_id IS NULL)
  THEN
    cg$ind.tnt_id := FALSE;
  ELSE
    cg$ind.tnt_id := TRUE;
  END IF;
  cg$rec.typ_id := :new.typ_id;
  IF (:new.typ_id IS NULL)
  THEN
    cg$ind.typ_id := FALSE;
  ELSE
    cg$ind.typ_id := TRUE;
  END IF;
  cg$rec.ext_id := :new.ext_id;
  IF (:new.ext_id IS NULL)
  THEN
    cg$ind.ext_id := FALSE;
  ELSE
    cg$ind.ext_id := TRUE;
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
  cg$rec.state := :new.state;
  IF (:new.state IS NULL)
  THEN
    cg$ind.state := FALSE;
  ELSE
    cg$ind.state := TRUE;
  END IF;

  IF NOT (cg$uit_identifiers.called_from_package)
  THEN
    cg$uit_identifiers.ins(cg$rec, cg$ind, FALSE);
    cg$uit_identifiers.called_from_package := FALSE;
  END IF;

  cg$uit_identifiers.cg$table(cg$uit_identifiers.idx).tnt_id            := cg$rec.tnt_id;
  cg$uit_identifiers.cg$tableind(cg$uit_identifiers.idx).tnt_id         := cg$ind.tnt_id;

  cg$uit_identifiers.cg$table(cg$uit_identifiers.idx).typ_id            := cg$rec.typ_id;
  cg$uit_identifiers.cg$tableind(cg$uit_identifiers.idx).typ_id         := cg$ind.typ_id;

  cg$uit_identifiers.cg$table(cg$uit_identifiers.idx).ext_id            := cg$rec.ext_id;
  cg$uit_identifiers.cg$tableind(cg$uit_identifiers.idx).ext_id         := cg$ind.ext_id;

  cg$uit_identifiers.cg$table(cg$uit_identifiers.idx).rowversion        := cg$rec.rowversion;
  cg$uit_identifiers.cg$tableind(cg$uit_identifiers.idx).rowversion     := cg$ind.rowversion;

  cg$uit_identifiers.cg$table(cg$uit_identifiers.idx).created_by        := cg$rec.created_by;
  cg$uit_identifiers.cg$tableind(cg$uit_identifiers.idx).created_by     := cg$ind.created_by;

  cg$uit_identifiers.cg$table(cg$uit_identifiers.idx).created_on        := cg$rec.created_on;
  cg$uit_identifiers.cg$tableind(cg$uit_identifiers.idx).created_on     := cg$ind.created_on;

  cg$uit_identifiers.cg$table(cg$uit_identifiers.idx).updated_by        := cg$rec.updated_by;
  cg$uit_identifiers.cg$tableind(cg$uit_identifiers.idx).updated_by     := cg$ind.updated_by;

  cg$uit_identifiers.cg$table(cg$uit_identifiers.idx).updated_on        := cg$rec.updated_on;
  cg$uit_identifiers.cg$tableind(cg$uit_identifiers.idx).updated_on     := cg$ind.updated_on;

  cg$uit_identifiers.cg$table(cg$uit_identifiers.idx).state             := cg$rec.state;
  cg$uit_identifiers.cg$tableind(cg$uit_identifiers.idx).state          := cg$ind.state;

  cg$uit_identifiers.idx := cg$uit_identifiers.idx + 1;

  :new.tnt_id         := cg$rec.tnt_id;
  :new.typ_id         := cg$rec.typ_id;
  :new.ext_id         := cg$rec.ext_id;
  :new.rowversion     := cg$rec.rowversion;
  :new.created_by     := cg$rec.created_by;
  :new.created_on     := cg$rec.created_on;
  :new.updated_by     := cg$rec.updated_by;
  :new.updated_on     := cg$rec.updated_on;
  :new.state          := cg$rec.state;

  -- Application_logic Post-Before-Insert-row <<Start>>
  -- Application_logic Post-Before-Insert-row << End >>
END;
/
SHOW ERROR
PROMPT Creating After Insert Row Trigger on 'uit_identifiers'
CREATE OR REPLACE TRIGGER cg$air_uit_identifiers
AFTER INSERT ON uit_identifiers FOR EACH ROW
DECLARE
  cg$rec cg$uit_identifiers.cg$row_type;
  cg$ind cg$uit_identifiers.cg$ind_type;
BEGIN
  --  Application_logic Pre-After-Insert-row <<Start>>
  --  Application_logic Pre-After-Insert-row << End >>

  cg$rec.tnt_id       := :new.tnt_id;
  cg$ind.tnt_id       := TRUE;
  cg$rec.typ_id       := :new.typ_id;
  cg$ind.typ_id       := TRUE;
  cg$rec.ext_id       := :new.ext_id;
  cg$ind.ext_id       := TRUE;
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
  cg$rec.state        := :new.state;
  cg$ind.state        := TRUE;

  --  Application_logic Post-After-Insert-row <<Start>>
  --  Application_logic Post-After-Insert-row << End >>
END;
/
PROMPT Creating After Insert Statement Trigger on 'uit_identifiers'
CREATE OR REPLACE TRIGGER cg$ais_uit_identifiers
AFTER INSERT ON uit_identifiers
DECLARE
  fk_check   INTEGER;
  idx        BINARY_INTEGER := cg$uit_identifiers.cg$table.FIRST;
  cg$rec     cg$uit_identifiers.cg$row_type;
  cg$old_rec cg$uit_identifiers.cg$row_type;
BEGIN
  -- Application_logic Pre-After-Insert-statement <<Start>>
  -- Application_logic Pre-After-Insert-statement << End >>

  IF NOT (cg$uit_identifiers.called_from_package)
  THEN
    WHILE idx IS NOT NULL
    LOOP
      cg$rec.tnt_id         := cg$uit_identifiers.cg$table(idx).tnt_id;
      cg$rec.typ_id         := cg$uit_identifiers.cg$table(idx).typ_id;
      cg$rec.ext_id         := cg$uit_identifiers.cg$table(idx).ext_id;
      cg$rec.rowversion     := cg$uit_identifiers.cg$table(idx).rowversion;
      cg$rec.created_by     := cg$uit_identifiers.cg$table(idx).created_by;
      cg$rec.created_on     := cg$uit_identifiers.cg$table(idx).created_on;
      cg$rec.updated_by     := cg$uit_identifiers.cg$table(idx).updated_by;
      cg$rec.updated_on     := cg$uit_identifiers.cg$table(idx).updated_on;
      cg$rec.state          := cg$uit_identifiers.cg$table(idx).state;

      cg$uit_identifiers.validate_foreign_keys_ins(cg$rec);

      idx := cg$uit_identifiers.cg$table.NEXT(idx);
    END LOOP;
  END IF;

  -- Application_logic Post-After-Insert-statement <<Start>>
  -- Application_logic Post-After-Insert-statement << End >>
END;
/
SHOW ERROR
PROMPT Creating Before Update Statement Trigger on 'uit_identifiers'
CREATE OR REPLACE TRIGGER cg$bus_uit_identifiers
BEFORE UPDATE ON uit_identifiers
BEGIN
  -- Application_logic Pre-Before-Update-statement <<Start>>
  -- Application_logic Pre-Before-Update-statement << End >>

  cg$uit_identifiers.cg$table.DELETE;
  cg$uit_identifiers.cg$tableind.DELETE;
  cg$uit_identifiers.idx := 1;

  -- Application_logic Post-Before-Update-statement <<Start>>
  -- Application_logic Post-Before-Update-statement << End >>
END;
/
SHOW ERROR
PROMPT Creating Before Update Row Trigger on 'uit_identifiers'
CREATE OR REPLACE TRIGGER cg$bur_uit_identifiers
BEFORE UPDATE ON uit_identifiers FOR EACH ROW
DECLARE
  cg$rec     cg$uit_identifiers.cg$row_type;
  cg$ind     cg$uit_identifiers.cg$ind_type;
  cg$old_rec cg$uit_identifiers.cg$row_type;
BEGIN
  -- Application_logic Pre-Before-Update-row <<Start>>
  -- Application_logic Pre-Before-Update-row << End >>

  -- Load cg$rec/cg$ind values from new
  cg$rec.tnt_id := :new.tnt_id;
  cg$ind.tnt_id := (:new.tnt_id IS NULL AND :old.tnt_id IS NOT NULL)
                OR (:old.tnt_id IS NULL AND :new.tnt_id IS NOT NULL)
                OR NOT(:new.tnt_id = :old.tnt_id);
  cg$uit_identifiers.cg$table(cg$uit_identifiers.idx).tnt_id := :old.tnt_id;

  cg$rec.typ_id := :new.typ_id;
  cg$ind.typ_id := (:new.typ_id IS NULL AND :old.typ_id IS NOT NULL)
                OR (:old.typ_id IS NULL AND :new.typ_id IS NOT NULL)
                OR NOT(:new.typ_id = :old.typ_id);
  cg$uit_identifiers.cg$table(cg$uit_identifiers.idx).typ_id := :old.typ_id;

  cg$rec.ext_id := :new.ext_id;
  cg$ind.ext_id := (:new.ext_id IS NULL AND :old.ext_id IS NOT NULL)
                OR (:old.ext_id IS NULL AND :new.ext_id IS NOT NULL)
                OR NOT(:new.ext_id = :old.ext_id);
  cg$uit_identifiers.cg$table(cg$uit_identifiers.idx).ext_id := :old.ext_id;

  cg$rec.rowversion := :new.rowversion;
  cg$ind.rowversion := (:new.rowversion IS NULL AND :old.rowversion IS NOT NULL)
                    OR (:old.rowversion IS NULL AND :new.rowversion IS NOT NULL)
                    OR NOT(:new.rowversion = :old.rowversion);
  cg$uit_identifiers.cg$table(cg$uit_identifiers.idx).rowversion := :old.rowversion;

  cg$rec.created_by := :new.created_by;
  cg$ind.created_by := (:new.created_by IS NULL AND :old.created_by IS NOT NULL)
                    OR (:old.created_by IS NULL AND :new.created_by IS NOT NULL)
                    OR NOT(:new.created_by = :old.created_by);
  cg$uit_identifiers.cg$table(cg$uit_identifiers.idx).created_by := :old.created_by;

  cg$rec.created_on := :new.created_on;
  cg$ind.created_on := (:new.created_on IS NULL AND :old.created_on IS NOT NULL)
                    OR (:old.created_on IS NULL AND :new.created_on IS NOT NULL)
                    OR NOT(:new.created_on = :old.created_on);
  cg$uit_identifiers.cg$table(cg$uit_identifiers.idx).created_on := :old.created_on;

  cg$rec.updated_by := :new.updated_by;
  cg$ind.updated_by := (:new.updated_by IS NULL AND :old.updated_by IS NOT NULL)
                    OR (:old.updated_by IS NULL AND :new.updated_by IS NOT NULL)
                    OR NOT(:new.updated_by = :old.updated_by);
  cg$uit_identifiers.cg$table(cg$uit_identifiers.idx).updated_by := :old.updated_by;

  cg$rec.updated_on := :new.updated_on;
  cg$ind.updated_on := (:new.updated_on IS NULL AND :old.updated_on IS NOT NULL)
                    OR (:old.updated_on IS NULL AND :new.updated_on IS NOT NULL)
                    OR NOT(:new.updated_on = :old.updated_on);
  cg$uit_identifiers.cg$table(cg$uit_identifiers.idx).updated_on := :old.updated_on;

  cg$rec.state := :new.state;
  cg$ind.state := (:new.state IS NULL AND :old.state IS NOT NULL)
               OR (:old.state IS NULL AND :new.state IS NOT NULL)
               OR NOT(:new.state = :old.state);
  cg$uit_identifiers.cg$table(cg$uit_identifiers.idx).state := :old.state;

  cg$uit_identifiers.idx := cg$uit_identifiers.idx + 1;

  IF NOT (cg$uit_identifiers.called_from_package)
  THEN
    cg$uit_identifiers.upd(cg$rec, cg$ind, FALSE);
    cg$uit_identifiers.called_from_package := FALSE;
  END IF;

  :new.rowversion := cg$rec.rowversion;
  :new.created_by := cg$rec.created_by;
  :new.created_on := cg$rec.created_on;
  :new.updated_by := cg$rec.updated_by;
  :new.updated_on := cg$rec.updated_on;
  :new.state      := cg$rec.state;

  -- Application_logic Post-Before-Update-row <<Start>>
  -- Application_logic Post-Before-Update-row << End >>
END;
/
SHOW ERROR
PROMPT Creating After Update Statement Trigger on 'uit_identifiers'
CREATE OR REPLACE TRIGGER cg$aus_uit_identifiers
AFTER UPDATE ON uit_identifiers
DECLARE
  idx        BINARY_INTEGER := cg$uit_identifiers.cg$table.FIRST;
  cg$old_rec cg$uit_identifiers.cg$row_type;
  cg$rec     cg$uit_identifiers.cg$row_type;
  cg$ind     cg$uit_identifiers.cg$ind_type;
BEGIN
  -- Application_logic Pre-After-Update-statement <<Start>>
  -- Application_logic Pre-After-Update-statement << End >>

  WHILE idx IS NOT NULL
  LOOP
    cg$old_rec.tnt_id     := cg$uit_identifiers.cg$table(idx).tnt_id;
    cg$old_rec.typ_id     := cg$uit_identifiers.cg$table(idx).typ_id;
    cg$old_rec.ext_id     := cg$uit_identifiers.cg$table(idx).ext_id;
    cg$old_rec.rowversion := cg$uit_identifiers.cg$table(idx).rowversion;
    cg$old_rec.created_by := cg$uit_identifiers.cg$table(idx).created_by;
    cg$old_rec.created_on := cg$uit_identifiers.cg$table(idx).created_on;
    cg$old_rec.updated_by := cg$uit_identifiers.cg$table(idx).updated_by;
    cg$old_rec.updated_on := cg$uit_identifiers.cg$table(idx).updated_on;
    cg$old_rec.state      := cg$uit_identifiers.cg$table(idx).state;

    IF NOT (cg$uit_identifiers.called_from_package)
    THEN
      idx                 := cg$uit_identifiers.cg$table.NEXT(idx);
      cg$rec.tnt_id       := cg$uit_identifiers.cg$table(idx).tnt_id;
      cg$ind.tnt_id       := updating('tnt_id');
      cg$rec.typ_id       := cg$uit_identifiers.cg$table(idx).typ_id;
      cg$ind.typ_id       := updating('typ_id');
      cg$rec.ext_id       := cg$uit_identifiers.cg$table(idx).ext_id;
      cg$ind.ext_id       := updating('ext_id');
      cg$rec.rowversion   := cg$uit_identifiers.cg$table(idx).rowversion;
      cg$ind.rowversion   := updating('rowversion');
      cg$rec.created_by   := cg$uit_identifiers.cg$table(idx).created_by;
      cg$ind.created_by   := updating('created_by');
      cg$rec.created_on   := cg$uit_identifiers.cg$table(idx).created_on;
      cg$ind.created_on   := updating('created_on');
      cg$rec.updated_by   := cg$uit_identifiers.cg$table(idx).updated_by;
      cg$ind.updated_by   := updating('updated_by');
      cg$rec.updated_on   := cg$uit_identifiers.cg$table(idx).updated_on;
      cg$ind.updated_on   := updating('updated_on');
      cg$rec.state        := cg$uit_identifiers.cg$table(idx).state;
      cg$ind.state        := updating('state');

      cg$uit_identifiers.validate_foreign_keys_upd(cg$rec, cg$old_rec, cg$ind);
      cg$uit_identifiers.cascade_update(cg$rec, cg$old_rec);

      cg$uit_identifiers.called_from_package := FALSE;
    END IF;
    idx := cg$uit_identifiers.cg$table.NEXT(idx);
  END LOOP;

  cg$uit_identifiers.cg$table.DELETE;

  -- Application_logic Post-After-Update-statement <<Start>>
  -- Application_logic Post-After-Update-statement << End >>
END;
/
SHOW ERROR
PROMPT Creating Before Delete Statement Trigger on 'uit_identifiers'
CREATE OR REPLACE TRIGGER cg$bds_uit_identifiers
BEFORE DELETE ON uit_identifiers
BEGIN
  -- Application_logic Pre-Before-Delete-statement <<Start>>
  -- Application_logic Pre-Before-Delete-statement << End >>

  cg$uit_identifiers.cg$table.DELETE;
  cg$uit_identifiers.cg$tableind.DELETE;
  cg$uit_identifiers.idx := 1;

  -- Application_logic Post-Before-Delete-statement <<Start>>
  -- Application_logic Post-Before-Delete-statement << End >>
END;
/
SHOW ERROR
PROMPT Creating Before Delete Row Trigger on 'uit_identifiers'
CREATE OR REPLACE TRIGGER cg$bdr_uit_identifiers
BEFORE DELETE ON uit_identifiers FOR EACH ROW
DECLARE
  cg$pk  cg$uit_identifiers.cg$pk_type;
  cg$rec cg$uit_identifiers.cg$row_type;
  cg$ind cg$uit_identifiers.cg$ind_type;
BEGIN
  -- Application_logic Pre-Before-Delete-row <<Start>>
  -- Application_logic Pre-Before-Delete-row << End >>

  -- Load cg$rec/cg$ind values from old
  cg$pk.tnt_id  := :old.tnt_id;
  cg$rec.tnt_id := :old.tnt_id;
  cg$uit_identifiers.cg$table(cg$uit_identifiers.idx).tnt_id := :old.tnt_id;
  cg$pk.typ_id  := :old.typ_id;
  cg$rec.typ_id := :old.typ_id;
  cg$uit_identifiers.cg$table(cg$uit_identifiers.idx).typ_id := :old.typ_id;
  cg$pk.ext_id  := :old.ext_id;
  cg$rec.ext_id := :old.ext_id;
  cg$uit_identifiers.cg$table(cg$uit_identifiers.idx).ext_id := :old.ext_id;

  cg$uit_identifiers.idx := cg$uit_identifiers.idx + 1;
  IF NOT (cg$uit_identifiers.called_from_package)
  THEN
    cg$uit_identifiers.del(cg$pk, FALSE);
    cg$uit_identifiers.called_from_package := FALSE;
  END IF;

  -- Application_logic Post-Before-Delete-row <<Start>>
  -- Application_logic Post-Before-Delete-row << End >>
END;
/
SHOW ERROR
PROMPT Creating After Delete Statement Trigger on 'uit_identifiers'
CREATE OR REPLACE TRIGGER cg$ads_uit_identifiers
AFTER DELETE ON uit_identifiers
DECLARE
  idx        BINARY_INTEGER := cg$uit_identifiers.cg$table.FIRST;
  cg$rec     cg$uit_identifiers.cg$row_type;
  cg$old_rec cg$uit_identifiers.cg$row_type;
BEGIN
  -- Application_logic Pre-After-Delete-statement <<Start>>
  -- Application_logic Pre-After-Delete-statement << End >>

  IF NOT (cg$uit_identifiers.called_from_package)
  THEN
    WHILE idx IS NOT NULL
    LOOP
      cg$rec.tnt_id := cg$uit_identifiers.cg$table(idx).tnt_id;
      cg$uit_identifiers.cg$tableind(idx).tnt_id := TRUE;

      cg$rec.typ_id := cg$uit_identifiers.cg$table(idx).typ_id;
      cg$uit_identifiers.cg$tableind(idx).typ_id := TRUE;

      cg$rec.ext_id := cg$uit_identifiers.cg$table(idx).ext_id;
      cg$uit_identifiers.cg$tableind(idx).ext_id := TRUE;

      cg$rec.rowversion := cg$uit_identifiers.cg$table(idx).rowversion;
      cg$uit_identifiers.cg$tableind(idx).rowversion := TRUE;

      cg$rec.created_by := cg$uit_identifiers.cg$table(idx).created_by;
      cg$uit_identifiers.cg$tableind(idx).created_by := TRUE;

      cg$rec.created_on := cg$uit_identifiers.cg$table(idx).created_on;
      cg$uit_identifiers.cg$tableind(idx).created_on := TRUE;

      cg$rec.updated_by := cg$uit_identifiers.cg$table(idx).updated_by;
      cg$uit_identifiers.cg$tableind(idx).updated_by := TRUE;

      cg$rec.updated_on := cg$uit_identifiers.cg$table(idx).updated_on;
      cg$uit_identifiers.cg$tableind(idx).updated_on := TRUE;

      cg$uit_identifiers.validate_foreign_keys_del(cg$rec);
      cg$uit_identifiers.cascade_delete(cg$rec);

      cg$rec.state := cg$uit_identifiers.cg$table(idx).state;
      cg$uit_identifiers.cg$tableind(idx).state := TRUE;

      idx := cg$uit_identifiers.cg$table.NEXT(idx);
    END LOOP;
  END IF;

  -- Application_logic Post-After-Delete-statement <<Start>>
  -- Application_logic Post-After-Delete-statement << End >>
END;
/
SHOW ERROR