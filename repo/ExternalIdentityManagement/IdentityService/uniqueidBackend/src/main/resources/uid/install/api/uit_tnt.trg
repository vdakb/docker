-- /Users/dsteding/Project/Bundeskriminalamt12c/ExternalIdentityManagement/IdentityService/uniqueidBackend/src/main/resources/install/uid/api/uit_cnt.trg
--
--   Einfuehrung einer personenbezogenen ID im Progamm Polizei 2020
--   Unique Identifier (UID)
--   Version 1.2 | Stand 15.03.2022
--
--   Reference to Page 20 Table Row 5 Codelist - Vorgabe durch P20IAM
--
-- Generated for Oracle Database 12c on Fri Mar 11 12.47.21 2022 by Server Generator 10.1.2.93.10

PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating Trigger Logic for Table 'uit_tenants'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating Before Insert Statement Trigger on 'uit_tenants'
CREATE OR REPLACE TRIGGER cg$bis_uit_tenants
BEFORE INSERT ON uit_tenants
BEGIN
  -- Application_logic Pre-Before-Insert-statement <<Start>>
  -- Application_logic Pre-Before-Insert-statement << End >>

  cg$uit_tenants.cg$table.DELETE;
  cg$uit_tenants.cg$tableind.DELETE;
  cg$uit_tenants.idx := 1;

  -- Application_logic Post-Before-Insert-statement <<Start>>
  -- Application_logic Post-Before-Insert-statement << End >>
END;
/
SHOW ERROR
PROMPT Creating Before Insert Row Trigger on 'uit_tenants'
CREATE OR REPLACE TRIGGER cg$bir_uit_tenants
BEFORE INSERT ON uit_tenants FOR EACH ROW
DECLARE
  cg$rec cg$uit_tenants.cg$row_type;
  cg$ind cg$uit_tenants.cg$ind_type;
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
  cg$rec.name := :new.name;
  IF (:new.name IS NULL)
  THEN
    cg$ind.name := FALSE;
  ELSE
    cg$ind.name := TRUE;
  END IF;

  IF NOT (cg$uit_tenants.called_from_package)
  THEN
    cg$uit_tenants.ins(cg$rec, cg$ind, FALSE);
    cg$uit_tenants.called_from_package := FALSE;
  END IF;

  cg$uit_tenants.cg$table(cg$uit_tenants.idx).id            := cg$rec.id;
  cg$uit_tenants.cg$tableind(cg$uit_tenants.idx).id         := cg$ind.id;

  cg$uit_tenants.cg$table(cg$uit_tenants.idx).rowversion    := cg$rec.rowversion;
  cg$uit_tenants.cg$tableind(cg$uit_tenants.idx).rowversion := cg$ind.rowversion;

  cg$uit_tenants.cg$table(cg$uit_tenants.idx).created_by    := cg$rec.created_by;
  cg$uit_tenants.cg$tableind(cg$uit_tenants.idx).created_by := cg$ind.created_by;

  cg$uit_tenants.cg$table(cg$uit_tenants.idx).created_on    := cg$rec.created_on;
  cg$uit_tenants.cg$tableind(cg$uit_tenants.idx).created_on := cg$ind.created_on;

  cg$uit_tenants.cg$table(cg$uit_tenants.idx).updated_by    := cg$rec.updated_by;
  cg$uit_tenants.cg$tableind(cg$uit_tenants.idx).updated_by := cg$ind.updated_by;

  cg$uit_tenants.cg$table(cg$uit_tenants.idx).updated_on    := cg$rec.updated_on;
  cg$uit_tenants.cg$tableind(cg$uit_tenants.idx).updated_on := cg$ind.updated_on;

  cg$uit_tenants.cg$table(cg$uit_tenants.idx).name          := cg$rec.name;
  cg$uit_tenants.cg$tableind(cg$uit_tenants.idx).name       := cg$ind.name;

  cg$uit_tenants.idx := cg$uit_tenants.idx + 1;

  :new.id             := cg$rec.id;
  :new.rowversion     := cg$rec.rowversion;
  :new.created_by     := cg$rec.created_by;
  :new.created_on     := cg$rec.created_on;
  :new.updated_by     := cg$rec.updated_by;
  :new.updated_on     := cg$rec.updated_on;
  :new.name           := cg$rec.name;

  -- Application_logic Post-Before-Insert-row <<Start>>
  -- Application_logic Post-Before-Insert-row << End >>
END;
/
SHOW ERROR
PROMPT Creating After Insert Row Trigger on 'uit_tenants'
CREATE OR REPLACE TRIGGER cg$air_uit_tenants
AFTER INSERT ON uit_tenants FOR EACH ROW
DECLARE
  cg$rec  cg$uit_tenants.cg$row_type;
  cg$ind  cg$uit_tenants.cg$ind_type;
  --
  clm$rec cg$uit_claims.cg$row_type;
  clm$ind cg$uit_claims.cg$ind_type;
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
  cg$rec.name         := :new.name;
  cg$ind.name         := TRUE;

  --  Application_logic Post-After-Insert-row <<Start>>
  --  Application_logic Post-After-Insert-row <<Start>>
  --
  clm$rec.tnt_id      := :new.id;
  clm$ind.tnt_id      := TRUE;
  clm$rec.rol_id      := 'uid.generate';
  clm$ind.rol_id      := TRUE;
  clm$rec.usr_id      := -1;
  clm$ind.usr_id      := TRUE;
  cg$uit_claims.ins(clm$rec, clm$ind);
  --
  clm$rec.tnt_id      := :new.id;
  clm$ind.tnt_id      := TRUE;
  clm$rec.rol_id      := 'uid.register';
  clm$ind.rol_id      := TRUE;
  clm$rec.usr_id      := -1;
  clm$ind.usr_id      := TRUE;
  cg$uit_claims.ins(clm$rec, clm$ind);
  --
  --  Application_logic Post-After-Insert-row << End >>
  --  Application_logic Post-After-Insert-row << End >>
END;
/
SHOW ERROR
PROMPT Creating After Insert Statement Trigger on 'uit_tenants'
CREATE OR REPLACE TRIGGER cg$ais_uit_tenants
AFTER INSERT ON uit_tenants
DECLARE
  fk_check   INTEGER;
  idx        BINARY_INTEGER := cg$uit_tenants.cg$table.FIRST;
  cg$rec     cg$uit_tenants.cg$row_type;
  cg$old_rec cg$uit_tenants.cg$row_type;
BEGIN
  -- Application_logic Pre-After-Insert-statement <<Start>>
  -- Application_logic Pre-After-Insert-statement << End >>

  IF NOT (cg$uit_tenants.called_from_package)
  THEN
    WHILE idx IS NOT NULL
    LOOP
      cg$rec.id             := cg$uit_tenants.cg$table(idx).id;
      cg$rec.rowversion     := cg$uit_tenants.cg$table(idx).rowversion;
      cg$rec.created_by     := cg$uit_tenants.cg$table(idx).created_by;
      cg$rec.created_on     := cg$uit_tenants.cg$table(idx).created_on;
      cg$rec.updated_by     := cg$uit_tenants.cg$table(idx).updated_by;
      cg$rec.updated_on     := cg$uit_tenants.cg$table(idx).updated_on;
      cg$rec.name           := cg$uit_tenants.cg$table(idx).name;

      cg$uit_tenants.validate_foreign_keys_ins(cg$rec);

      idx := cg$uit_tenants.cg$table.NEXT(idx);
    END LOOP;
  END IF;

  -- Application_logic Post-After-Insert-statement <<Start>>
  -- Application_logic Post-After-Insert-statement << End >>
END;
/
SHOW ERROR
PROMPT Creating Before Update Statement Trigger on 'uit_tenants'
CREATE OR REPLACE TRIGGER cg$bus_uit_tenants
BEFORE UPDATE ON uit_tenants
BEGIN
  -- Application_logic Pre-Before-Update-statement <<Start>>
  -- Application_logic Pre-Before-Update-statement << End >>

  cg$uit_tenants.cg$table.DELETE;
  cg$uit_tenants.cg$tableind.DELETE;
  cg$uit_tenants.idx := 1;

  -- Application_logic Post-Before-Update-statement <<Start>>
  -- Application_logic Post-Before-Update-statement << End >>
END;
/
SHOW ERROR
PROMPT Creating Before Update Row Trigger on 'uit_tenants'
CREATE OR REPLACE TRIGGER cg$bur_uit_tenants
BEFORE UPDATE ON uit_tenants FOR EACH ROW
DECLARE
  cg$rec     cg$uit_tenants.cg$row_type;
  cg$ind     cg$uit_tenants.cg$ind_type;
  cg$old_rec cg$uit_tenants.cg$row_type;
BEGIN
  -- Application_logic Pre-Before-Update-row <<Start>>
  -- Application_logic Pre-Before-Update-row << End >>

  -- Load cg$rec/cg$ind values from new
  cg$rec.id := :new.id;
  cg$ind.id := (:new.id IS NULL AND :old.id IS NOT NULL)
                OR (:old.id IS NULL AND :new.id IS NOT NULL)
                OR NOT(:new.id = :old.id);
  cg$uit_tenants.cg$table(cg$uit_tenants.idx).id := :old.id;

  cg$rec.rowversion := :new.rowversion;
  cg$ind.rowversion := (:new.rowversion IS NULL AND :old.rowversion IS NOT NULL)
                    OR (:old.rowversion IS NULL AND :new.rowversion IS NOT NULL)
                    OR NOT(:new.rowversion = :old.rowversion);
  cg$uit_tenants.cg$table(cg$uit_tenants.idx).rowversion := :old.rowversion;

  cg$rec.created_by := :new.created_by;
  cg$ind.created_by := (:new.created_by IS NULL AND :old.created_by IS NOT NULL)
                    OR (:old.created_by IS NULL AND :new.created_by IS NOT NULL)
                    OR NOT(:new.created_by = :old.created_by);
  cg$uit_tenants.cg$table(cg$uit_tenants.idx).created_by := :old.created_by;

  cg$rec.created_on := :new.created_on;
  cg$ind.created_on := (:new.created_on IS NULL AND :old.created_on IS NOT NULL)
                    OR (:old.created_on IS NULL AND :new.created_on IS NOT NULL)
                    OR NOT(:new.created_on = :old.created_on);
  cg$uit_tenants.cg$table(cg$uit_tenants.idx).created_on := :old.created_on;

  cg$rec.updated_by := :new.updated_by;
  cg$ind.updated_by := (:new.updated_by IS NULL AND :old.updated_by IS NOT NULL)
                    OR (:old.updated_by IS NULL AND :new.updated_by IS NOT NULL)
                    OR NOT(:new.updated_by = :old.updated_by);
  cg$uit_tenants.cg$table(cg$uit_tenants.idx).updated_by := :old.updated_by;

  cg$rec.updated_on := :new.updated_on;
  cg$ind.updated_on := (:new.updated_on IS NULL AND :old.updated_on IS NOT NULL)
                    OR (:old.updated_on IS NULL AND :new.updated_on IS NOT NULL)
                    OR NOT(:new.updated_on = :old.updated_on);
  cg$uit_tenants.cg$table(cg$uit_tenants.idx).updated_on := :old.updated_on;

  cg$rec.name := :new.name;
  cg$ind.name := (:new.name IS NULL AND :old.name IS NOT NULL)
                OR (:old.name IS NULL AND :new.name IS NOT NULL)
                OR NOT(:new.name = :old.name);
  cg$uit_tenants.cg$table(cg$uit_tenants.idx).name := :old.name;

  cg$uit_tenants.idx := cg$uit_tenants.idx + 1;

  IF NOT (cg$uit_tenants.called_from_package)
  THEN
    cg$uit_tenants.upd(cg$rec, cg$ind, FALSE);
    cg$uit_tenants.called_from_package := FALSE;
  END IF;

  :new.rowversion     := cg$rec.rowversion;
  :new.created_by     := cg$rec.created_by;
  :new.created_on     := cg$rec.created_on;
  :new.updated_by     := cg$rec.updated_by;
  :new.updated_on     := cg$rec.updated_on;
  :new.name           := cg$rec.name;

  -- Application_logic Post-Before-Update-row <<Start>>
  -- Application_logic Post-Before-Update-row << End >>
END;
/
SHOW ERROR
PROMPT Creating After Update Statement Trigger on 'uit_tenants'
CREATE OR REPLACE TRIGGER cg$aus_uit_tenants
AFTER UPDATE ON uit_tenants
DECLARE
  idx        BINARY_INTEGER := cg$uit_tenants.cg$table.FIRST;
  cg$old_rec cg$uit_tenants.cg$row_type;
  cg$rec     cg$uit_tenants.cg$row_type;
  cg$ind     cg$uit_tenants.cg$ind_type;
BEGIN
  -- Application_logic Pre-After-Update-statement <<Start>>
  -- Application_logic Pre-After-Update-statement << End >>

  WHILE idx IS NOT NULL
  LOOP
    cg$old_rec.id             := cg$uit_tenants.cg$table(idx).id;
    cg$old_rec.rowversion     := cg$uit_tenants.cg$table(idx).rowversion;
    cg$old_rec.created_by     := cg$uit_tenants.cg$table(idx).created_by;
    cg$old_rec.created_on     := cg$uit_tenants.cg$table(idx).created_on;
    cg$old_rec.updated_by     := cg$uit_tenants.cg$table(idx).updated_by;
    cg$old_rec.updated_on     := cg$uit_tenants.cg$table(idx).updated_on;
    cg$old_rec.name           := cg$uit_tenants.cg$table(idx).name;

    IF NOT (cg$uit_tenants.called_from_package)
    THEN
      idx                   := cg$uit_tenants.cg$table.NEXT(idx);
      cg$rec.id             := cg$uit_tenants.cg$table(idx).id;
      cg$ind.id             := updating('id');
      cg$rec.rowversion     := cg$uit_tenants.cg$table(idx).rowversion;
      cg$ind.rowversion     := updating('rowversion');
      cg$rec.created_by     := cg$uit_tenants.cg$table(idx).created_by;
      cg$ind.created_by     := updating('created_by');
      cg$rec.created_on     := cg$uit_tenants.cg$table(idx).created_on;
      cg$ind.created_on     := updating('created_on');
      cg$rec.updated_by     := cg$uit_tenants.cg$table(idx).updated_by;
      cg$ind.updated_by     := updating('updated_by');
      cg$rec.updated_on     := cg$uit_tenants.cg$table(idx).updated_on;
      cg$ind.updated_on     := updating('updated_on');
      cg$rec.name           := cg$uit_tenants.cg$table(idx).name;
      cg$ind.name           := updating('name');

      cg$uit_tenants.validate_foreign_keys_upd(cg$rec, cg$old_rec, cg$ind);
      cg$uit_tenants.cascade_update(cg$rec, cg$old_rec);

      cg$uit_tenants.called_from_package := FALSE;
    END IF;
    idx := cg$uit_tenants.cg$table.NEXT(idx);
  END LOOP;

  cg$uit_tenants.cg$table.DELETE;

  -- Application_logic Post-After-Update-statement <<Start>>
  -- Application_logic Post-After-Update-statement << End >>
END;
/
SHOW ERROR
PROMPT Creating Before Delete Statement Trigger on 'uit_tenants'
CREATE OR REPLACE TRIGGER cg$bds_uit_tenants
BEFORE DELETE ON uit_tenants
BEGIN
  -- Application_logic Pre-Before-Delete-statement <<Start>>
  -- Application_logic Pre-Before-Delete-statement << End >>

  cg$uit_tenants.cg$table.DELETE;
  cg$uit_tenants.cg$tableind.DELETE;
  cg$uit_tenants.idx := 1;

  -- Application_logic Post-Before-Delete-statement <<Start>>
  -- Application_logic Post-Before-Delete-statement << End >>
END;
/
SHOW ERROR
PROMPT Creating Before Delete Row Trigger on 'uit_tenants'
CREATE OR REPLACE TRIGGER cg$bdr_uit_tenants
BEFORE DELETE ON uit_tenants FOR EACH ROW
DECLARE
  cg$pk  cg$uit_tenants.cg$pk_type;
  cg$rec cg$uit_tenants.cg$row_type;
  cg$ind cg$uit_tenants.cg$ind_type;
BEGIN
  -- Application_logic Pre-Before-Delete-row <<Start>>
  --
  DELETE FROM uit_claims
  WHERE  tnt_id = :old.id
  ;
  --
  -- Application_logic Pre-Before-Delete-row << End >>

  -- Load cg$rec/cg$ind values from old
  cg$pk.id  := :old.id;
  cg$rec.id := :old.id;
  cg$uit_tenants.cg$table(cg$uit_tenants.idx).id := :old.id;

  cg$uit_tenants.idx := cg$uit_tenants.idx + 1;
  IF NOT (cg$uit_tenants.called_from_package)
  THEN
    cg$uit_tenants.del(cg$pk, FALSE);
    cg$uit_tenants.called_from_package := FALSE;
  END IF;

  -- Application_logic Post-Before-Delete-row <<Start>>
  -- Application_logic Post-Before-Delete-row << End >>
END;
/
SHOW ERROR
PROMPT Creating After Delete Statement Trigger on 'uit_tenants'
CREATE OR REPLACE TRIGGER cg$ads_uit_tenants
AFTER DELETE ON uit_tenants
DECLARE
  idx        BINARY_INTEGER := cg$uit_tenants.cg$table.FIRST;
  cg$rec     cg$uit_tenants.cg$row_type;
  cg$old_rec cg$uit_tenants.cg$row_type;
BEGIN
  -- Application_logic Pre-After-Delete-statement <<Start>>
  -- Application_logic Pre-After-Delete-statement << End >>

  IF NOT (cg$uit_tenants.called_from_package)
  THEN
    WHILE idx IS NOT NULL
    LOOP
      cg$rec.id := cg$uit_tenants.cg$table(idx).id;
      cg$uit_tenants.cg$tableind(idx).id := TRUE;

      cg$rec.rowversion := cg$uit_tenants.cg$table(idx).rowversion;
      cg$uit_tenants.cg$tableind(idx).rowversion := TRUE;

      cg$rec.created_by := cg$uit_tenants.cg$table(idx).created_by;
      cg$uit_tenants.cg$tableind(idx).created_by := TRUE;

      cg$rec.created_on := cg$uit_tenants.cg$table(idx).created_on;
      cg$uit_tenants.cg$tableind(idx).created_on := TRUE;

      cg$rec.updated_by := cg$uit_tenants.cg$table(idx).updated_by;
      cg$uit_tenants.cg$tableind(idx).updated_by := TRUE;

      cg$rec.updated_on := cg$uit_tenants.cg$table(idx).updated_on;
      cg$uit_tenants.cg$tableind(idx).updated_on := TRUE;

      cg$uit_tenants.validate_foreign_keys_del(cg$rec);
      cg$uit_tenants.cascade_delete(cg$rec);

      cg$rec.name := cg$uit_tenants.cg$table(idx).name;
      cg$uit_tenants.cg$tableind(idx).name := TRUE;

      idx := cg$uit_tenants.cg$table.NEXT(idx);
    END LOOP;
  END IF;

  -- Application_logic Post-After-Delete-statement <<Start>>
  -- Application_logic Post-After-Delete-statement << End >>
END;
/
SHOW ERROR