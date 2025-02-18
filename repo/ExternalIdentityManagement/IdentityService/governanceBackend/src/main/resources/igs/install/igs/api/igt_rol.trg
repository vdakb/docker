-- /Users/dsteding/Project/Bundeskriminalamt12c/ExternalIdentityManagement/IdentityService/governanceBackend/src/main/resources/install/adm/api/igt_rol.trg
--
-- Generated for Oracle Database 12c on Fri Mar 11 12.47.21 2022 by Server Generator 10.1.2.93.10

PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating Trigger Logic for Table 'igt_roles'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating Before Insert Statement Trigger on 'igt_roles'
CREATE OR REPLACE TRIGGER cg$bis_igt_roles
BEFORE INSERT ON igt_roles
BEGIN
  -- Application_logic Pre-Before-Insert-statement <<Start>>
  -- Application_logic Pre-Before-Insert-statement << End >>

  cg$igt_roles.cg$table.DELETE;
  cg$igt_roles.cg$tableind.DELETE;
  cg$igt_roles.idx := 1;

  -- Application_logic Post-Before-Insert-statement <<Start>>
  -- Application_logic Post-Before-Insert-statement << End >>
END;
/
SHOW ERROR
PROMPT Creating Before Insert Row Trigger on 'igt_roles'
CREATE OR REPLACE TRIGGER cg$bir_igt_roles
BEFORE INSERT ON igt_roles FOR EACH ROW
DECLARE
  cg$rec cg$igt_roles.cg$row_type;
  cg$ind cg$igt_roles.cg$ind_type;
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
  cg$rec.active       := :new.active;
  cg$ind.active       := TRUE;
  cg$rec.display_name := :new.display_name;
  cg$ind.display_name := TRUE;
  cg$rec.description  := :new.description;
  cg$ind.description  := TRUE;

  IF NOT (cg$igt_roles.called_from_package)
  THEN
    cg$igt_roles.ins(cg$rec, cg$ind, FALSE);
    cg$igt_roles.called_from_package := FALSE;
  END IF;

  cg$igt_roles.cg$table(cg$igt_roles.idx).id              := cg$rec.id;
  cg$igt_roles.cg$tableind(cg$igt_roles.idx).id           := cg$ind.id;

  cg$igt_roles.cg$table(cg$igt_roles.idx).rowversion      := cg$rec.rowversion;
  cg$igt_roles.cg$tableind(cg$igt_roles.idx).rowversion   := cg$ind.rowversion;

  cg$igt_roles.cg$table(cg$igt_roles.idx).created_by      := cg$rec.created_by;
  cg$igt_roles.cg$tableind(cg$igt_roles.idx).created_by   := cg$ind.created_by;

  cg$igt_roles.cg$table(cg$igt_roles.idx).created_on      := cg$rec.created_on;
  cg$igt_roles.cg$tableind(cg$igt_roles.idx).created_on   := cg$ind.created_on;

  cg$igt_roles.cg$table(cg$igt_roles.idx).updated_by      := cg$rec.updated_by;
  cg$igt_roles.cg$tableind(cg$igt_roles.idx).updated_by   := cg$ind.updated_by;

  cg$igt_roles.cg$table(cg$igt_roles.idx).updated_on      := cg$rec.updated_on;
  cg$igt_roles.cg$tableind(cg$igt_roles.idx).updated_on   := cg$ind.updated_on;

  cg$igt_roles.cg$table(cg$igt_roles.idx).active          := cg$rec.active;
  cg$igt_roles.cg$tableind(cg$igt_roles.idx).active       := cg$ind.active;

  cg$igt_roles.cg$table(cg$igt_roles.idx).display_name    := cg$rec.display_name;
  cg$igt_roles.cg$tableind(cg$igt_roles.idx).display_name := cg$ind.display_name;

  cg$igt_roles.cg$table(cg$igt_roles.idx).description     := cg$rec.description;
  cg$igt_roles.cg$tableind(cg$igt_roles.idx).description  := cg$ind.description;

  cg$igt_roles.idx := cg$igt_roles.idx + 1;

  :new.id           := cg$rec.id;
  :new.rowversion   := cg$rec.rowversion;
  :new.created_by   := cg$rec.created_by;
  :new.created_on   := cg$rec.created_on;
  :new.updated_by   := cg$rec.updated_by;
  :new.updated_on   := cg$rec.updated_on;
  :new.active       := cg$rec.active;
  :new.display_name := cg$rec.display_name;
  :new.description  := cg$rec.description;

  -- Application_logic Post-Before-Insert-row <<Start>>
  -- Application_logic Post-Before-Insert-row << End >>
END;
/
SHOW ERROR
PROMPT Creating After Insert Row Trigger on 'igt_roles'
CREATE OR REPLACE TRIGGER cg$air_igt_roles
AFTER INSERT ON igt_roles FOR EACH ROW
DECLARE
  cg$rec cg$igt_roles.cg$row_type;
  cg$ind cg$igt_roles.cg$ind_type;
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
  cg$rec.display_name := :new.display_name;
  cg$ind.display_name := TRUE;
  cg$rec.description  := :new.description;
  cg$ind.description  := TRUE;

  --  Application_logic Post-After-Insert-row <<Start>>
  --  Application_logic Post-After-Insert-row << End >>
END;
/
SHOW ERROR
PROMPT Creating After Insert Statement Trigger on 'igt_roles'
CREATE OR REPLACE TRIGGER cg$ais_igt_roles
AFTER INSERT ON igt_roles
DECLARE
  fk_check   INTEGER;
  idx        BINARY_INTEGER := cg$igt_roles.cg$table.FIRST;
  cg$rec     cg$igt_roles.cg$row_type;
  cg$old_rec cg$igt_roles.cg$row_type;
BEGIN
  -- Application_logic Pre-After-Insert-statement <<Start>>
  -- Application_logic Pre-After-Insert-statement << End >>

  IF NOT (cg$igt_roles.called_from_package)
  THEN
    WHILE idx IS NOT NULL
    LOOP
      cg$rec.id           := cg$igt_roles.cg$table(idx).id;
      cg$rec.rowversion   := cg$igt_roles.cg$table(idx).rowversion;
      cg$rec.created_by   := cg$igt_roles.cg$table(idx).created_by;
      cg$rec.created_on   := cg$igt_roles.cg$table(idx).created_on;
      cg$rec.updated_by   := cg$igt_roles.cg$table(idx).updated_by;
      cg$rec.updated_on   := cg$igt_roles.cg$table(idx).updated_on;
      cg$rec.active       := cg$igt_roles.cg$table(idx).active;
      cg$rec.display_name := cg$igt_roles.cg$table(idx).display_name;
      cg$rec.description  := cg$igt_roles.cg$table(idx).description;

      cg$igt_roles.validate_foreign_keys_ins(cg$rec);

      idx := cg$igt_roles.cg$table.NEXT(idx);
    END LOOP;
  END IF;

  -- Application_logic Post-After-Insert-statement <<Start>>
  -- Application_logic Post-After-Insert-statement << End >>
END;
/
SHOW ERROR
PROMPT Creating Before Update Statement Trigger on 'igt_roles'
CREATE OR REPLACE TRIGGER cg$bus_igt_roles
BEFORE UPDATE ON igt_roles
BEGIN
  -- Application_logic Pre-Before-Update-statement <<Start>>
  -- Application_logic Pre-Before-Update-statement << End >>

  cg$igt_roles.cg$table.DELETE;
  cg$igt_roles.cg$tableind.DELETE;
  cg$igt_roles.idx := 1;

  -- Application_logic Post-Before-Update-statement <<Start>>
  -- Application_logic Post-Before-Update-statement << End >>
END;
/
SHOW ERROR
PROMPT Creating Before Update Row Trigger on 'igt_roles'
CREATE OR REPLACE TRIGGER cg$bur_igt_roles
BEFORE UPDATE ON igt_roles FOR EACH ROW
DECLARE
  cg$rec     cg$igt_roles.cg$row_type;
  cg$ind     cg$igt_roles.cg$ind_type;
  cg$old_rec cg$igt_roles.cg$row_type;
BEGIN
  -- Application_logic Pre-Before-Update-row <<Start>>
  -- Application_logic Pre-Before-Update-row << End >>

  -- Load cg$rec/cg$ind values from new
  cg$rec.id := :new.id;
  cg$ind.id := (:new.id IS NULL AND :old.id IS NOT NULL)
            OR (:old.id IS NULL AND :new.id IS NOT NULL)
            OR NOT(:new.id = :old.id);
  cg$igt_roles.cg$table(cg$igt_roles.idx).id := :old.id;

  cg$rec.rowversion := :new.rowversion;
  cg$ind.rowversion := (:new.rowversion IS NULL AND :old.rowversion IS NOT NULL)
                    OR (:old.rowversion IS NULL AND :new.rowversion IS NOT NULL)
                    OR NOT(:new.rowversion = :old.rowversion);
  cg$igt_roles.cg$table(cg$igt_roles.idx).rowversion := :old.rowversion;

  cg$rec.created_by := :new.created_by;
  cg$ind.created_by := (:new.created_by IS NULL AND :old.created_by IS NOT NULL)
                    OR (:old.created_by IS NULL AND :new.created_by IS NOT NULL)
                    OR NOT(:new.created_by = :old.created_by);
  cg$igt_roles.cg$table(cg$igt_roles.idx).created_by := :old.created_by;

  cg$rec.created_on := :new.created_on;
  cg$ind.created_on := (:new.created_on IS NULL AND :old.created_on IS NOT NULL)
                    OR (:old.created_on IS NULL AND :new.created_on IS NOT NULL)
                    OR NOT(:new.created_on = :old.created_on);
  cg$igt_roles.cg$table(cg$igt_roles.idx).created_on := :old.created_on;

  cg$rec.updated_by := :new.updated_by;
  cg$ind.updated_by := (:new.updated_by IS NULL AND :old.updated_by IS NOT NULL)
                    OR (:old.updated_by IS NULL AND :new.updated_by IS NOT NULL)
                    OR NOT(:new.updated_by = :old.updated_by);
  cg$igt_roles.cg$table(cg$igt_roles.idx).updated_by := :old.updated_by;

  cg$rec.updated_on := :new.updated_on;
  cg$ind.updated_on := (:new.updated_on IS NULL AND :old.updated_on IS NOT NULL)
                    OR (:old.updated_on IS NULL AND :new.updated_on IS NOT NULL)
                    OR NOT(:new.updated_on = :old.updated_on);
  cg$igt_roles.cg$table(cg$igt_roles.idx).updated_on := :old.updated_on;

  cg$rec.active := :new.active;
  cg$ind.active := (:new.active IS NULL AND :old.active IS NOT NULL)
                OR (:old.active IS NULL AND :new.active IS NOT NULL)
                OR NOT(:new.active = :old.active);
  cg$igt_roles.cg$table(cg$igt_roles.idx).active := :old.active;

  cg$rec.display_name := :new.display_name;
  cg$ind.display_name := (:new.display_name IS NULL AND :old.display_name IS NOT NULL)
                      OR (:old.display_name IS NULL AND :new.display_name IS NOT NULL)
                      OR NOT(:new.display_name = :old.display_name);
  cg$igt_roles.cg$table(cg$igt_roles.idx).display_name := :old.display_name;

  cg$rec.description := :new.description;
  cg$ind.description := (:new.description IS NULL AND :old.description IS NOT NULL)
                     OR (:old.description IS NULL AND :new.description IS NOT NULL)
                     OR NOT(:new.description = :old.description);
  cg$igt_roles.cg$table(cg$igt_roles.idx).description := :old.description;

  cg$igt_roles.idx := cg$igt_roles.idx + 1;

  IF NOT (cg$igt_roles.called_from_package)
  THEN
    cg$igt_roles.upd(cg$rec, cg$ind, FALSE);
    cg$igt_roles.called_from_package := FALSE;
  END IF;

  :new.rowversion   := cg$rec.rowversion;
  :new.created_by   := cg$rec.created_by;
  :new.created_on   := cg$rec.created_on;
  :new.updated_by   := cg$rec.updated_by;
  :new.updated_on   := cg$rec.updated_on;
  :new.active       := cg$rec.active;
  :new.display_name := cg$rec.display_name;
  :new.description  := cg$rec.description;

  -- Application_logic Post-Before-Update-row <<Start>>
  -- Application_logic Post-Before-Update-row << End >>
END;
/
SHOW ERROR
PROMPT Creating After Update Statement Trigger on 'igt_roles'
CREATE OR REPLACE TRIGGER cg$aus_igt_roles
AFTER UPDATE ON igt_roles
DECLARE
  idx        BINARY_INTEGER := cg$igt_roles.cg$table.FIRST;
  cg$old_rec cg$igt_roles.cg$row_type;
  cg$rec     cg$igt_roles.cg$row_type;
  cg$ind     cg$igt_roles.cg$ind_type;
BEGIN
  -- Application_logic Pre-After-Update-statement <<Start>>
  -- Application_logic Pre-After-Update-statement << End >>

  WHILE idx IS NOT NULL
  LOOP
    cg$old_rec.id           := cg$igt_roles.cg$table(idx).id;
    cg$old_rec.rowversion   := cg$igt_roles.cg$table(idx).rowversion;
    cg$old_rec.created_by   := cg$igt_roles.cg$table(idx).created_by;
    cg$old_rec.created_on   := cg$igt_roles.cg$table(idx).created_on;
    cg$old_rec.updated_by   := cg$igt_roles.cg$table(idx).updated_by;
    cg$old_rec.updated_on   := cg$igt_roles.cg$table(idx).updated_on;
    cg$old_rec.active       := cg$igt_roles.cg$table(idx).active;
    cg$old_rec.display_name := cg$igt_roles.cg$table(idx).display_name;
    cg$old_rec.description  := cg$igt_roles.cg$table(idx).description;

    IF NOT (cg$igt_roles.called_from_package)
    THEN
      idx                 := cg$igt_roles.cg$table.NEXT(idx);
      cg$rec.id           := cg$igt_roles.cg$table(idx).id;
      cg$ind.id           := updating('id');
      cg$rec.rowversion   := cg$igt_roles.cg$table(idx).rowversion;
      cg$ind.rowversion   := updating('rowversion');
      cg$rec.created_by   := cg$igt_roles.cg$table(idx).created_by;
      cg$ind.created_by   := updating('created_by');
      cg$rec.created_on   := cg$igt_roles.cg$table(idx).created_on;
      cg$ind.created_on   := updating('created_on');
      cg$rec.updated_by   := cg$igt_roles.cg$table(idx).updated_by;
      cg$ind.updated_by   := updating('updated_by');
      cg$rec.updated_on   := cg$igt_roles.cg$table(idx).updated_on;
      cg$ind.updated_on   := updating('updated_on');
      cg$rec.active       := cg$igt_roles.cg$table(idx).active;
      cg$ind.active       := updating('active');
      cg$rec.display_name := cg$igt_roles.cg$table(idx).display_name;
      cg$ind.display_name := updating('display_name');
      cg$rec.description  := cg$igt_roles.cg$table(idx).description;
      cg$ind.description  := updating('description');

      cg$igt_roles.validate_foreign_keys_upd(cg$rec, cg$old_rec, cg$ind);
      cg$igt_roles.called_from_package := FALSE;
    END IF;
    idx := cg$igt_roles.cg$table.NEXT(idx);
  END LOOP;

  cg$igt_roles.cg$table.DELETE;

  -- Application_logic Post-After-Update-statement <<Start>>
  -- Application_logic Post-After-Update-statement << End >>
END;
/
SHOW ERROR
PROMPT Creating Before Delete Statement Trigger on 'igt_roles'
CREATE OR REPLACE TRIGGER cg$bds_igt_roles
BEFORE DELETE ON igt_roles
BEGIN
  -- Application_logic Pre-Before-Delete-statement <<Start>>
  -- Application_logic Pre-Before-Delete-statement << End >>

  cg$igt_roles.cg$table.DELETE;
  cg$igt_roles.cg$tableind.DELETE;
  cg$igt_roles.idx := 1;

  -- Application_logic Post-Before-Delete-statement <<Start>>
  -- Application_logic Post-Before-Delete-statement << End >>
END;
/
SHOW ERROR
PROMPT Creating Before Delete Row Trigger on 'igt_roles'
CREATE OR REPLACE TRIGGER cg$bdr_igt_roles
BEFORE DELETE ON igt_roles FOR EACH ROW
DECLARE
  cg$pk  cg$igt_roles.cg$pk_type;
  cg$rec cg$igt_roles.cg$row_type;
  cg$ind cg$igt_roles.cg$ind_type;
BEGIN
  -- Application_logic Pre-Before-Delete-row <<Start>>
  -- Application_logic Pre-Before-Delete-row << End >>

  -- Load cg$rec/cg$ind values from old
  cg$pk.id  := :old.id;
  cg$rec.id := :old.id;
  cg$igt_roles.cg$table(cg$igt_roles.idx).id := :old.id;

  cg$igt_roles.idx := cg$igt_roles.idx + 1;
  IF NOT (cg$igt_roles.called_from_package)
  THEN
    cg$igt_roles.del(cg$pk, FALSE);
    cg$igt_roles.called_from_package := FALSE;
  END IF;

  -- Application_logic Post-Before-Delete-row <<Start>>
  -- Application_logic Post-Before-Delete-row << End >>
END;
/
SHOW ERROR
PROMPT Creating After Delete Statement Trigger on 'igt_roles'
CREATE OR REPLACE TRIGGER cg$ads_igt_roles
AFTER DELETE ON igt_roles
DECLARE
  idx        BINARY_INTEGER := cg$igt_roles.cg$table.FIRST;
  cg$rec     cg$igt_roles.cg$row_type;
  cg$old_rec cg$igt_roles.cg$row_type;
BEGIN
  -- Application_logic Pre-After-Delete-statement <<Start>>
  -- Application_logic Pre-After-Delete-statement << End >>

  IF NOT (cg$igt_roles.called_from_package)
  THEN
    WHILE idx IS NOT NULL
    LOOP
      cg$rec.id := cg$igt_roles.cg$table(idx).id;
      cg$igt_roles.cg$tableind(idx).id := TRUE;

      cg$rec.rowversion := cg$igt_roles.cg$table(idx).rowversion;
      cg$igt_roles.cg$tableind(idx).rowversion := TRUE;

      cg$rec.created_by := cg$igt_roles.cg$table(idx).created_by;
      cg$igt_roles.cg$tableind(idx).created_by := TRUE;

      cg$rec.created_on := cg$igt_roles.cg$table(idx).created_on;
      cg$igt_roles.cg$tableind(idx).created_on := TRUE;

      cg$rec.updated_by := cg$igt_roles.cg$table(idx).updated_by;
      cg$igt_roles.cg$tableind(idx).updated_by := TRUE;

      cg$rec.updated_on := cg$igt_roles.cg$table(idx).updated_on;
      cg$igt_roles.cg$tableind(idx).updated_on := TRUE;

      cg$rec.active := cg$igt_roles.cg$table(idx).active;
      cg$igt_roles.cg$tableind(idx).active := TRUE;

      cg$rec.display_name := cg$igt_roles.cg$table(idx).display_name;
      cg$igt_roles.cg$tableind(idx).display_name := TRUE;

      cg$rec.description := cg$igt_roles.cg$table(idx).description;
      cg$igt_roles.cg$tableind(idx).description := TRUE;

      cg$igt_roles.validate_foreign_keys_del(cg$rec);
      cg$igt_roles.cascade_delete(cg$rec);

      idx := cg$igt_roles.cg$table.NEXT(idx);
    END LOOP;
  END IF;

  -- Application_logic Post-After-Delete-statement <<Start>>
  -- Application_logic Post-After-Delete-statement << End >>
END;
/
SHOW ERROR