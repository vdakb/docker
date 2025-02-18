-- /Users/dsteding/Project/Bundeskriminalamt12c/ExternalIdentityManagement/IdentityService/governanceBackend/src/main/resources/install/adm/api/igt_url.trg
--
-- Generated for Oracle Database 12c on Fri Mar 11 12.47.21 2022 by Server Generator 10.1.2.93.10

PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating Trigger Logic for Table 'igt_userroles'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating Before Insert Statement Trigger on 'igt_userroles'
CREATE OR REPLACE TRIGGER cg$bis_igt_userroles
BEFORE INSERT ON igt_userroles
BEGIN
  -- Application_logic Pre-Before-Insert-statement <<Start>>
  -- Application_logic Pre-Before-Insert-statement << End >>

  cg$igt_userroles.cg$table.DELETE;
  cg$igt_userroles.cg$tableind.DELETE;
  cg$igt_userroles.idx := 1;

  -- Application_logic Post-Before-Insert-statement <<Start>>
  -- Application_logic Post-Before-Insert-statement << End >>
END;
/
SHOW ERROR
PROMPT Creating Before Insert Row Trigger on 'igt_userroles'
CREATE OR REPLACE TRIGGER cg$bir_igt_userroles
BEFORE INSERT ON igt_userroles FOR EACH ROW
DECLARE
  cg$rec cg$igt_userroles.cg$row_type;
  cg$ind cg$igt_userroles.cg$ind_type;
BEGIN
  -- Application_logic Pre-Before-Insert-row <<Start>>
  -- Application_logic Pre-Before-Insert-row << End >>

  -- Load cg$rec/cg$ind values from new
  cg$rec.usr_id := :new.usr_id;
  IF (:new.usr_id IS NULL)
  THEN
    cg$ind.usr_id := FALSE;
  ELSE
    cg$ind.usr_id := TRUE;
  END IF;
  cg$rec.rol_id := :new.rol_id;
  IF (:new.rol_id IS NULL)
  THEN
    cg$ind.rol_id := FALSE;
  ELSE
    cg$ind.rol_id := TRUE;
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

  IF NOT (cg$igt_userroles.called_from_package)
  THEN
    cg$igt_userroles.ins(cg$rec, cg$ind, FALSE);
    cg$igt_userroles.called_from_package := FALSE;
  END IF;

  cg$igt_userroles.cg$table(cg$igt_userroles.idx).usr_id          := cg$rec.usr_id;
  cg$igt_userroles.cg$tableind(cg$igt_userroles.idx).usr_id       := cg$ind.usr_id;

  cg$igt_userroles.cg$table(cg$igt_userroles.idx).rol_id          := cg$rec.rol_id;
  cg$igt_userroles.cg$tableind(cg$igt_userroles.idx).rol_id       := cg$ind.rol_id;

  cg$igt_userroles.cg$table(cg$igt_userroles.idx).rowversion      := cg$rec.rowversion;
  cg$igt_userroles.cg$tableind(cg$igt_userroles.idx).rowversion   := cg$ind.rowversion;

  cg$igt_userroles.cg$table(cg$igt_userroles.idx).created_by      := cg$rec.created_by;
  cg$igt_userroles.cg$tableind(cg$igt_userroles.idx).created_by   := cg$ind.created_by;

  cg$igt_userroles.cg$table(cg$igt_userroles.idx).created_on      := cg$rec.created_on;
  cg$igt_userroles.cg$tableind(cg$igt_userroles.idx).created_on   := cg$ind.created_on;

  cg$igt_userroles.cg$table(cg$igt_userroles.idx).updated_by      := cg$rec.updated_by;
  cg$igt_userroles.cg$tableind(cg$igt_userroles.idx).updated_by   := cg$ind.updated_by;

  cg$igt_userroles.cg$table(cg$igt_userroles.idx).updated_on      := cg$rec.updated_on;
  cg$igt_userroles.cg$tableind(cg$igt_userroles.idx).updated_on   := cg$ind.updated_on;

  cg$igt_userroles.idx := cg$igt_userroles.idx + 1;

  :new.usr_id       := cg$rec.usr_id;
  :new.rol_id       := cg$rec.rol_id;
  :new.rowversion   := cg$rec.rowversion;
  :new.created_by   := cg$rec.created_by;
  :new.created_on   := cg$rec.created_on;
  :new.updated_by   := cg$rec.updated_by;
  :new.updated_on   := cg$rec.updated_on;

  -- Application_logic Post-Before-Insert-row <<Start>>
  -- Application_logic Post-Before-Insert-row << End >>
END;
/
SHOW ERROR
PROMPT Creating After Insert Row Trigger on 'igt_userroles'
CREATE OR REPLACE TRIGGER cg$air_igt_userroles
AFTER INSERT ON igt_userroles FOR EACH ROW
DECLARE
  cg$rec cg$igt_userroles.cg$row_type;
  cg$ind cg$igt_userroles.cg$ind_type;
BEGIN
  --  Application_logic Pre-After-Insert-row <<Start>>
  --  Application_logic Pre-After-Insert-row << End >>

  cg$rec.usr_id       := :new.usr_id;
  cg$ind.usr_id       := TRUE;
  cg$rec.rol_id       := :new.rol_id;
  cg$ind.rol_id       := TRUE;
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

  --  Application_logic Post-After-Insert-row <<Start>>
  --  Application_logic Post-After-Insert-row << End >>
END;
/
SHOW ERROR
PROMPT Creating After Insert Statement Trigger on 'igt_userroles'
CREATE OR REPLACE TRIGGER cg$ais_igt_userroles
AFTER INSERT ON igt_userroles
DECLARE
  fk_check   INTEGER;
  idx        BINARY_INTEGER := cg$igt_userroles.cg$table.FIRST;
  cg$rec     cg$igt_userroles.cg$row_type;
  cg$old_rec cg$igt_userroles.cg$row_type;
BEGIN
  -- Application_logic Pre-After-Insert-statement <<Start>>
  -- Application_logic Pre-After-Insert-statement << End >>

  IF NOT (cg$igt_userroles.called_from_package)
  THEN
    WHILE idx IS NOT NULL
    LOOP
      cg$rec.usr_id       := cg$igt_userroles.cg$table(idx).usr_id;
      cg$rec.rol_id       := cg$igt_userroles.cg$table(idx).rol_id;
      cg$rec.rowversion   := cg$igt_userroles.cg$table(idx).rowversion;
      cg$rec.created_by   := cg$igt_userroles.cg$table(idx).created_by;
      cg$rec.created_on   := cg$igt_userroles.cg$table(idx).created_on;
      cg$rec.updated_by   := cg$igt_userroles.cg$table(idx).updated_by;
      cg$rec.updated_on   := cg$igt_userroles.cg$table(idx).updated_on;

      cg$igt_userroles.validate_foreign_keys_ins(cg$rec);

      idx := cg$igt_userroles.cg$table.NEXT(idx);
    END LOOP;
  END IF;

  -- Application_logic Post-After-Insert-statement <<Start>>
  -- Application_logic Post-After-Insert-statement << End >>
END;
/
SHOW ERROR
PROMPT Creating Before Update Statement Trigger on 'igt_userroles'
CREATE OR REPLACE TRIGGER cg$bus_igt_userroles
BEFORE UPDATE ON igt_userroles
BEGIN
  -- Application_logic Pre-Before-Update-statement <<Start>>
  -- Application_logic Pre-Before-Update-statement << End >>

  cg$igt_userroles.cg$table.DELETE;
  cg$igt_userroles.cg$tableind.DELETE;
  cg$igt_userroles.idx := 1;

  -- Application_logic Post-Before-Update-statement <<Start>>
  -- Application_logic Post-Before-Update-statement << End >>
END;
/
SHOW ERROR
PROMPT Creating Before Update Row Trigger on 'igt_userroles'
CREATE OR REPLACE TRIGGER cg$bur_igt_userroles
BEFORE UPDATE ON igt_userroles FOR EACH ROW
DECLARE
  cg$rec     cg$igt_userroles.cg$row_type;
  cg$ind     cg$igt_userroles.cg$ind_type;
  cg$old_rec cg$igt_userroles.cg$row_type;
BEGIN
  -- Application_logic Pre-Before-Update-row <<Start>>
  -- Application_logic Pre-Before-Update-row << End >>

  -- Load cg$rec/cg$ind values from new
  cg$rec.usr_id := :new.usr_id;
  cg$ind.usr_id := (:new.usr_id IS NULL AND :old.usr_id IS NOT NULL)
                OR (:old.usr_id IS NULL AND :new.usr_id IS NOT NULL)
                OR NOT(:new.usr_id = :old.usr_id);
  cg$igt_userroles.cg$table(cg$igt_userroles.idx).usr_id := :old.usr_id;

  cg$rec.rol_id := :new.rol_id;
  cg$ind.rol_id := (:new.rol_id IS NULL AND :old.rol_id IS NOT NULL)
                OR (:old.rol_id IS NULL AND :new.rol_id IS NOT NULL)
                OR NOT(:new.rol_id = :old.rol_id);
  cg$igt_userroles.cg$table(cg$igt_userroles.idx).rol_id := :old.rol_id;

  cg$rec.rowversion := :new.rowversion;
  cg$ind.rowversion := (:new.rowversion IS NULL AND :old.rowversion IS NOT NULL)
                    OR (:old.rowversion IS NULL AND :new.rowversion IS NOT NULL)
                    OR NOT(:new.rowversion = :old.rowversion);
  cg$igt_userroles.cg$table(cg$igt_userroles.idx).rowversion := :old.rowversion;

  cg$rec.created_by := :new.created_by;
  cg$ind.created_by := (:new.created_by IS NULL AND :old.created_by IS NOT NULL)
                    OR (:old.created_by IS NULL AND :new.created_by IS NOT NULL)
                    OR NOT(:new.created_by = :old.created_by);
  cg$igt_userroles.cg$table(cg$igt_userroles.idx).created_by := :old.created_by;

  cg$rec.created_on := :new.created_on;
  cg$ind.created_on := (:new.created_on IS NULL AND :old.created_on IS NOT NULL)
                    OR (:old.created_on IS NULL AND :new.created_on IS NOT NULL)
                    OR NOT(:new.created_on = :old.created_on);
  cg$igt_userroles.cg$table(cg$igt_userroles.idx).created_on := :old.created_on;

  cg$rec.updated_by := :new.updated_by;
  cg$ind.updated_by := (:new.updated_by IS NULL AND :old.updated_by IS NOT NULL)
                    OR (:old.updated_by IS NULL AND :new.updated_by IS NOT NULL)
                    OR NOT(:new.updated_by = :old.updated_by);
  cg$igt_userroles.cg$table(cg$igt_userroles.idx).updated_by := :old.updated_by;

  cg$rec.updated_on := :new.updated_on;
  cg$ind.updated_on := (:new.updated_on IS NULL AND :old.updated_on IS NOT NULL)
                    OR (:old.updated_on IS NULL AND :new.updated_on IS NOT NULL)
                    OR NOT(:new.updated_on = :old.updated_on);
  cg$igt_userroles.cg$table(cg$igt_userroles.idx).updated_on := :old.updated_on;

  cg$igt_userroles.idx := cg$igt_userroles.idx + 1;

  IF NOT (cg$igt_userroles.called_from_package)
  THEN
    cg$igt_userroles.upd(cg$rec, cg$ind, FALSE);
    cg$igt_userroles.called_from_package := FALSE;
  END IF;

  :new.rowversion   := cg$rec.rowversion;
  :new.created_by   := cg$rec.created_by;
  :new.created_on   := cg$rec.created_on;
  :new.updated_by   := cg$rec.updated_by;
  :new.updated_on   := cg$rec.updated_on;

  -- Application_logic Post-Before-Update-row <<Start>>
  -- Application_logic Post-Before-Update-row << End >>
END;
/
SHOW ERROR
PROMPT Creating After Update Statement Trigger on 'igt_userroles'
CREATE OR REPLACE TRIGGER cg$aus_igt_userroles
AFTER UPDATE ON igt_userroles
DECLARE
  idx        BINARY_INTEGER := cg$igt_userroles.cg$table.FIRST;
  cg$old_rec cg$igt_userroles.cg$row_type;
  cg$rec     cg$igt_userroles.cg$row_type;
  cg$ind     cg$igt_userroles.cg$ind_type;
BEGIN
  -- Application_logic Pre-After-Update-statement <<Start>>
  -- Application_logic Pre-After-Update-statement << End >>

  WHILE idx IS NOT NULL
  LOOP
    cg$old_rec.usr_id       := cg$igt_userroles.cg$table(idx).usr_id;
    cg$old_rec.rol_id       := cg$igt_userroles.cg$table(idx).rol_id;
    cg$old_rec.rowversion   := cg$igt_userroles.cg$table(idx).rowversion;
    cg$old_rec.created_by   := cg$igt_userroles.cg$table(idx).created_by;
    cg$old_rec.created_on   := cg$igt_userroles.cg$table(idx).created_on;
    cg$old_rec.updated_by   := cg$igt_userroles.cg$table(idx).updated_by;
    cg$old_rec.updated_on   := cg$igt_userroles.cg$table(idx).updated_on;

    IF NOT (cg$igt_userroles.called_from_package)
    THEN
      idx                 := cg$igt_userroles.cg$table.NEXT(idx);
      cg$rec.usr_id       := cg$igt_userroles.cg$table(idx).usr_id;
      cg$ind.usr_id       := updating('usr_id');
      cg$rec.rol_id       := cg$igt_userroles.cg$table(idx).rol_id;
      cg$ind.rol_id       := updating('rol_id');
      cg$rec.rowversion   := cg$igt_userroles.cg$table(idx).rowversion;
      cg$ind.rowversion   := updating('rowversion');
      cg$rec.created_by   := cg$igt_userroles.cg$table(idx).created_by;
      cg$ind.created_by   := updating('created_by');
      cg$rec.created_on   := cg$igt_userroles.cg$table(idx).created_on;
      cg$ind.created_on   := updating('created_on');
      cg$rec.updated_by   := cg$igt_userroles.cg$table(idx).updated_by;
      cg$ind.updated_by   := updating('updated_by');
      cg$rec.updated_on   := cg$igt_userroles.cg$table(idx).updated_on;
      cg$ind.updated_on   := updating('updated_on');

      cg$igt_userroles.validate_foreign_keys_upd(cg$rec, cg$old_rec, cg$ind);
      cg$igt_userroles.called_from_package := FALSE;
    END IF;
    idx := cg$igt_userroles.cg$table.NEXT(idx);
  END LOOP;

  cg$igt_userroles.cg$table.DELETE;

  -- Application_logic Post-After-Update-statement <<Start>>
  -- Application_logic Post-After-Update-statement << End >>
END;
/
SHOW ERROR
PROMPT Creating Before Delete Statement Trigger on 'igt_userroles'
CREATE OR REPLACE TRIGGER cg$bds_igt_userroles
BEFORE DELETE ON igt_userroles
BEGIN
  -- Application_logic Pre-Before-Delete-statement <<Start>>
  -- Application_logic Pre-Before-Delete-statement << End >>

  cg$igt_userroles.cg$table.DELETE;
  cg$igt_userroles.cg$tableind.DELETE;
  cg$igt_userroles.idx := 1;

  -- Application_logic Post-Before-Delete-statement <<Start>>
  -- Application_logic Post-Before-Delete-statement << End >>
END;
/
SHOW ERROR
PROMPT Creating Before Delete Row Trigger on 'igt_userroles'
CREATE OR REPLACE TRIGGER cg$bdr_igt_userroles
BEFORE DELETE ON igt_userroles FOR EACH ROW
DECLARE
  cg$pk  cg$igt_userroles.cg$pk_type;
  cg$rec cg$igt_userroles.cg$row_type;
  cg$ind cg$igt_userroles.cg$ind_type;
BEGIN
  -- Application_logic Pre-Before-Delete-row <<Start>>
  -- Application_logic Pre-Before-Delete-row << End >>

  -- Load cg$rec/cg$ind values from old
  cg$pk.usr_id  := :old.usr_id;
  cg$rec.usr_id := :old.usr_id;
  cg$pk.rol_id  := :old.rol_id;
  cg$rec.rol_id := :old.rol_id;
  cg$igt_userroles.cg$table(cg$igt_userroles.idx).usr_id := :old.usr_id;
  cg$igt_userroles.cg$table(cg$igt_userroles.idx).rol_id := :old.rol_id;

  cg$igt_userroles.idx := cg$igt_userroles.idx + 1;
  IF NOT (cg$igt_userroles.called_from_package)
  THEN
    cg$igt_userroles.del(cg$pk, FALSE);
    cg$igt_userroles.called_from_package := FALSE;
  END IF;

  -- Application_logic Post-Before-Delete-row <<Start>>
  -- Application_logic Post-Before-Delete-row << End >>
END;
/
SHOW ERROR
PROMPT Creating After Delete Statement Trigger on 'igt_userroles'
CREATE OR REPLACE TRIGGER cg$ads_igt_userroles
AFTER DELETE ON igt_userroles
DECLARE
  idx        BINARY_INTEGER := cg$igt_userroles.cg$table.FIRST;
  cg$rec     cg$igt_userroles.cg$row_type;
  cg$old_rec cg$igt_userroles.cg$row_type;
BEGIN
  -- Application_logic Pre-After-Delete-statement <<Start>>
  -- Application_logic Pre-After-Delete-statement << End >>

  IF NOT (cg$igt_userroles.called_from_package)
  THEN
    WHILE idx IS NOT NULL
    LOOP
      cg$rec.usr_id := cg$igt_userroles.cg$table(idx).usr_id;
      cg$igt_userroles.cg$tableind(idx).usr_id := TRUE;

      cg$rec.rol_id := cg$igt_userroles.cg$table(idx).rol_id;
      cg$igt_userroles.cg$tableind(idx).rol_id := TRUE;

      cg$rec.rowversion := cg$igt_userroles.cg$table(idx).rowversion;
      cg$igt_userroles.cg$tableind(idx).rowversion := TRUE;

      cg$rec.created_by := cg$igt_userroles.cg$table(idx).created_by;
      cg$igt_userroles.cg$tableind(idx).created_by := TRUE;

      cg$rec.created_on := cg$igt_userroles.cg$table(idx).created_on;
      cg$igt_userroles.cg$tableind(idx).created_on := TRUE;

      cg$rec.updated_by := cg$igt_userroles.cg$table(idx).updated_by;
      cg$igt_userroles.cg$tableind(idx).updated_by := TRUE;

      cg$rec.updated_on := cg$igt_userroles.cg$table(idx).updated_on;
      cg$igt_userroles.cg$tableind(idx).updated_on := TRUE;

      cg$igt_userroles.validate_foreign_keys_del(cg$rec);
      cg$igt_userroles.cascade_delete(cg$rec);

      idx := cg$igt_userroles.cg$table.NEXT(idx);
    END LOOP;
  END IF;

  -- Application_logic Post-After-Delete-statement <<Start>>
  -- Application_logic Post-After-Delete-statement << End >>
END;
/
SHOW ERROR