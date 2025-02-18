-- /Users/dsteding/Project/Bundeskriminalamt12c/ExternalIdentityManagement/IdentityService/uniqueidBackend/src/main/resources/install/uid/api/uit_clm.trg
--
--   Einfuehrung einer personenbezogenen ID im Progamm Polizei 2020
--   Unique Identifier (CLM)
--   Version 1.2 | Stand 15.03.2022
--
-- Generated for Oracle Database 12c on Fri Mar 11 12.47.21 2022 by Server Generator 10.1.2.93.10

PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating Trigger Logic for Table 'uit_claims'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating Before Insert Statement Trigger on 'uit_claims'
CREATE OR REPLACE TRIGGER cg$bis_uit_claims
BEFORE INSERT ON uit_claims
BEGIN
  -- Application_logic Pre-Before-Insert-statement <<Start>>
  -- Application_logic Pre-Before-Insert-statement << End >>

  cg$uit_claims.cg$table.DELETE;
  cg$uit_claims.cg$tableind.DELETE;
  cg$uit_claims.idx := 1;

  -- Application_logic Post-Before-Insert-statement <<Start>>
  -- Application_logic Post-Before-Insert-statement << End >>
END;
/
SHOW ERROR
PROMPT Creating Before Insert Row Trigger on 'uit_claims'
CREATE OR REPLACE TRIGGER cg$bir_uit_claims
BEFORE INSERT ON uit_claims FOR EACH ROW
DECLARE
  cg$rec cg$uit_claims.cg$row_type;
  cg$ind cg$uit_claims.cg$ind_type;
BEGIN
  -- Application_logic Pre-Before-Insert-row <<Start>>
  -- Application_logic Pre-Before-Insert-row << End >>

  -- Load cg$rec/cg$ind values from new
  cg$rec.tnt_id     := :new.tnt_id;
  cg$ind.tnt_id     := (:new.tnt_id IS NOT NULL);
  cg$rec.rol_id     := :new.rol_id;
  cg$ind.rol_id     := (:new.rol_id IS NOT NULL);
  cg$rec.usr_id     := :new.usr_id;
  cg$ind.usr_id     := (:new.usr_id IS NOT NULL);
  cg$rec.rowversion := :new.rowversion;
  cg$ind.rowversion := (:new.rowversion IS NOT NULL);
  cg$rec.created_by := :new.created_by;
  cg$ind.created_by := (:new.created_by IS NOT NULL);
  cg$rec.created_on := :new.created_on;
  cg$ind.created_on := (:new.created_on IS NOT NULL);
  cg$rec.updated_by := :new.updated_by;
  cg$ind.updated_by := (:new.updated_by IS NOT NULL);
  cg$rec.updated_on := :new.updated_on;
  cg$ind.updated_on := (:new.updated_on IS NOT NULL);

  IF NOT (cg$uit_claims.called_from_package)
  THEN
    cg$uit_claims.ins(cg$rec, cg$ind, FALSE);
    cg$uit_claims.called_from_package := FALSE;
  END IF;

  cg$uit_claims.cg$table(cg$uit_claims.idx).tnt_id        := cg$rec.tnt_id;
  cg$uit_claims.cg$tableind(cg$uit_claims.idx).tnt_id     := cg$ind.tnt_id;

  cg$uit_claims.cg$table(cg$uit_claims.idx).rol_id        := cg$rec.rol_id;
  cg$uit_claims.cg$tableind(cg$uit_claims.idx).rol_id     := cg$ind.rol_id;

  cg$uit_claims.cg$table(cg$uit_claims.idx).usr_id        := cg$rec.usr_id;
  cg$uit_claims.cg$tableind(cg$uit_claims.idx).usr_id     := cg$ind.usr_id;

  cg$uit_claims.cg$table(cg$uit_claims.idx).rowversion    := cg$rec.rowversion;
  cg$uit_claims.cg$tableind(cg$uit_claims.idx).rowversion := cg$ind.rowversion;

  cg$uit_claims.cg$table(cg$uit_claims.idx).created_by    := cg$rec.created_by;
  cg$uit_claims.cg$tableind(cg$uit_claims.idx).created_by := cg$ind.created_by;

  cg$uit_claims.cg$table(cg$uit_claims.idx).created_on    := cg$rec.created_on;
  cg$uit_claims.cg$tableind(cg$uit_claims.idx).created_on := cg$ind.created_on;

  cg$uit_claims.cg$table(cg$uit_claims.idx).updated_by    := cg$rec.updated_by;
  cg$uit_claims.cg$tableind(cg$uit_claims.idx).updated_by := cg$ind.updated_by;

  cg$uit_claims.cg$table(cg$uit_claims.idx).updated_on    := cg$rec.updated_on;
  cg$uit_claims.cg$tableind(cg$uit_claims.idx).updated_on := cg$ind.updated_on;

  cg$uit_claims.idx := cg$uit_claims.idx + 1;

  :new.tnt_id         := cg$rec.tnt_id;
  :new.rol_id         := cg$rec.rol_id;
  :new.usr_id         := cg$rec.usr_id;
  :new.rowversion     := cg$rec.rowversion;
  :new.created_by     := cg$rec.created_by;
  :new.created_on     := cg$rec.created_on;
  :new.updated_by     := cg$rec.updated_by;
  :new.updated_on     := cg$rec.updated_on;

  -- Application_logic Post-Before-Insert-row <<Start>>
  -- Application_logic Post-Before-Insert-row << End >>
END;
/
SHOW ERROR
PROMPT Creating After Insert Row Trigger on 'uit_claims'
CREATE OR REPLACE TRIGGER cg$air_uit_claims
AFTER INSERT ON uit_claims FOR EACH ROW
DECLARE
  cg$rec cg$uit_claims.cg$row_type;
  cg$ind cg$uit_claims.cg$ind_type;
BEGIN
  --  Application_logic Pre-After-Insert-row <<Start>>
  --  Application_logic Pre-After-Insert-row << End >>

  cg$rec.tnt_id     := :new.tnt_id;
  cg$ind.tnt_id     := TRUE;
  cg$rec.rol_id     := :new.rol_id;
  cg$ind.rol_id     := TRUE;
  cg$rec.usr_id     := :new.usr_id;
  cg$ind.usr_id     := TRUE;
  cg$rec.rowversion := :new.created_by;
  cg$ind.rowversion := TRUE;
  cg$rec.created_by := :new.created_by;
  cg$ind.created_by := TRUE;
  cg$rec.created_on := :new.created_on;
  cg$ind.created_on := TRUE;
  cg$rec.updated_by := :new.updated_by;
  cg$ind.updated_by := TRUE;
  cg$rec.updated_on := :new.updated_on;
  cg$ind.updated_on := TRUE;

  --  Application_logic Post-After-Insert-row <<Start>>
  --  Application_logic Post-After-Insert-row << End >>
END;
/
SHOW ERROR
PROMPT Creating After Insert Statement Trigger on 'uit_claims'
CREATE OR REPLACE TRIGGER cg$ais_uit_claims
AFTER INSERT ON uit_claims
DECLARE
  fk_check   INTEGER;
  idx        BINARY_INTEGER := cg$uit_claims.cg$table.FIRST;
  cg$rec     cg$uit_claims.cg$row_type;
  cg$old_rec cg$uit_claims.cg$row_type;
BEGIN
  -- Application_logic Pre-After-Insert-statement <<Start>>
  -- Application_logic Pre-After-Insert-statement << End >>

  IF NOT (cg$uit_claims.called_from_package)
  THEN
    WHILE idx IS NOT NULL
    LOOP
      cg$rec.tnt_id     := cg$uit_claims.cg$table(idx).tnt_id;
      cg$rec.rol_id     := cg$uit_claims.cg$table(idx).rol_id;
      cg$rec.usr_id     := cg$uit_claims.cg$table(idx).usr_id;
      cg$rec.rowversion := cg$uit_claims.cg$table(idx).rowversion;
      cg$rec.created_by := cg$uit_claims.cg$table(idx).created_by;
      cg$rec.created_on := cg$uit_claims.cg$table(idx).created_on;
      cg$rec.updated_by := cg$uit_claims.cg$table(idx).updated_by;
      cg$rec.updated_on := cg$uit_claims.cg$table(idx).updated_on;

      cg$uit_claims.validate_foreign_keys_ins(cg$rec);

      idx := cg$uit_claims.cg$table.NEXT(idx);
    END LOOP;
  END IF;

  -- Application_logic Post-After-Insert-statement <<Start>>
  -- Application_logic Post-After-Insert-statement << End >>
END;
/
SHOW ERROR
PROMPT Creating Before Update Statement Trigger on 'uit_claims'
CREATE OR REPLACE TRIGGER cg$bus_uit_claims
BEFORE UPDATE ON uit_claims
BEGIN
  -- Application_logic Pre-Before-Update-statement <<Start>>
  -- Application_logic Pre-Before-Update-statement << End >>

  cg$uit_claims.cg$table.DELETE;
  cg$uit_claims.cg$tableind.DELETE;
  cg$uit_claims.idx := 1;

  -- Application_logic Post-Before-Update-statement <<Start>>
  -- Application_logic Post-Before-Update-statement << End >>
END;
/
SHOW ERROR
PROMPT Creating Before Update Row Trigger on 'uit_claims'
CREATE OR REPLACE TRIGGER cg$bur_uit_claims
BEFORE UPDATE ON uit_claims FOR EACH ROW
DECLARE
  cg$rec     cg$uit_claims.cg$row_type;
  cg$ind     cg$uit_claims.cg$ind_type;
  cg$old_rec cg$uit_claims.cg$row_type;
BEGIN
  -- Application_logic Pre-Before-Update-row <<Start>>
  -- Application_logic Pre-Before-Update-row << End >>

  -- Load cg$rec/cg$ind values from new
  cg$rec.tnt_id := :new.tnt_id;
  cg$ind.tnt_id := (:new.tnt_id IS NULL AND :old.tnt_id IS NOT NULL)
                OR (:old.tnt_id IS NULL AND :new.tnt_id IS NOT NULL)
                OR NOT(:new.tnt_id = :old.tnt_id);
  cg$uit_claims.cg$table(cg$uit_claims.idx).tnt_id := :old.tnt_id;

  cg$rec.rol_id := :new.rol_id;
  cg$ind.rol_id := (:new.rol_id IS NULL AND :old.rol_id IS NOT NULL)
                OR (:old.rol_id IS NULL AND :new.rol_id IS NOT NULL)
                OR NOT(:new.rol_id = :old.rol_id);
  cg$uit_claims.cg$table(cg$uit_claims.idx).rol_id := :old.rol_id;

  cg$rec.usr_id := :new.usr_id;
  cg$ind.usr_id := (:new.usr_id IS NULL AND :old.usr_id IS NOT NULL)
                OR (:old.usr_id IS NULL AND :new.usr_id IS NOT NULL)
                OR NOT(:new.usr_id = :old.usr_id);
  cg$uit_claims.cg$table(cg$uit_claims.idx).usr_id := :old.usr_id;

  cg$rec.rowversion := :new.rowversion;
  cg$ind.rowversion := (:new.rowversion IS NULL AND :old.rowversion IS NOT NULL)
                    OR (:old.rowversion IS NULL AND :new.rowversion IS NOT NULL)
                    OR NOT(:new.rowversion = :old.rowversion);
  cg$uit_claims.cg$table(cg$uit_claims.idx).rowversion := :old.rowversion;

  cg$rec.created_by := :new.created_by;
  cg$ind.created_by := (:new.created_by IS NULL AND :old.created_by IS NOT NULL)
                    OR (:old.created_by IS NULL AND :new.created_by IS NOT NULL)
                    OR NOT(:new.created_by = :old.created_by);
  cg$uit_claims.cg$table(cg$uit_claims.idx).created_by := :old.created_by;

  cg$rec.created_on := :new.created_on;
  cg$ind.created_on := (:new.created_on IS NULL AND :old.created_on IS NOT NULL)
                    OR (:old.created_on IS NULL AND :new.created_on IS NOT NULL)
                    OR NOT(:new.created_on = :old.created_on);
  cg$uit_claims.cg$table(cg$uit_claims.idx).created_on := :old.created_on;

  cg$rec.updated_by := :new.updated_by;
  cg$ind.updated_by := (:new.updated_by IS NULL AND :old.updated_by IS NOT NULL)
                    OR (:old.updated_by IS NULL AND :new.updated_by IS NOT NULL)
                    OR NOT(:new.updated_by = :old.updated_by);
  cg$uit_claims.cg$table(cg$uit_claims.idx).updated_by := :old.updated_by;

  cg$rec.updated_on := :new.updated_on;
  cg$ind.updated_on := (:new.updated_on IS NULL AND :old.updated_on IS NOT NULL)
                    OR (:old.updated_on IS NULL AND :new.updated_on IS NOT NULL)
                    OR NOT(:new.updated_on = :old.updated_on);
  cg$uit_claims.cg$table(cg$uit_claims.idx).updated_on := :old.updated_on;

  cg$uit_claims.idx := cg$uit_claims.idx + 1;

  IF NOT (cg$uit_claims.called_from_package)
  THEN
    cg$uit_claims.upd(cg$rec, cg$ind, FALSE);
    cg$uit_claims.called_from_package := FALSE;
  END IF;

  :new.rowversion := cg$rec.rowversion;
  :new.created_by := cg$rec.created_by;
  :new.created_on := cg$rec.created_on;
  :new.updated_by := cg$rec.updated_by;
  :new.updated_on := cg$rec.updated_on;

  -- Application_logic Post-Before-Update-row <<Start>>
  -- Application_logic Post-Before-Update-row << End >>
END;
/
SHOW ERROR
PROMPT Creating After Update Statement Trigger on 'uit_claims'
CREATE OR REPLACE TRIGGER cg$aus_uit_claims
AFTER UPDATE ON uit_claims
DECLARE
  idx        BINARY_INTEGER := cg$uit_claims.cg$table.FIRST;
  cg$old_rec cg$uit_claims.cg$row_type;
  cg$rec     cg$uit_claims.cg$row_type;
  cg$ind     cg$uit_claims.cg$ind_type;
BEGIN
  -- Application_logic Pre-After-Update-statement <<Start>>
  -- Application_logic Pre-After-Update-statement << End >>

  WHILE idx IS NOT NULL
  LOOP
    cg$old_rec.tnt_id     := cg$uit_claims.cg$table(idx).tnt_id;
    cg$old_rec.rol_id     := cg$uit_claims.cg$table(idx).rol_id;
    cg$old_rec.usr_id     := cg$uit_claims.cg$table(idx).usr_id;
    cg$old_rec.rowversion := cg$uit_claims.cg$table(idx).rowversion;
    cg$old_rec.created_by := cg$uit_claims.cg$table(idx).created_by;
    cg$old_rec.created_on := cg$uit_claims.cg$table(idx).created_on;
    cg$old_rec.updated_by := cg$uit_claims.cg$table(idx).updated_by;
    cg$old_rec.updated_on := cg$uit_claims.cg$table(idx).updated_on;

    IF NOT (cg$uit_claims.called_from_package)
    THEN
      idx                 := cg$uit_claims.cg$table.NEXT(idx);
      cg$rec.tnt_id       := cg$uit_claims.cg$table(idx).tnt_id;
      cg$ind.tnt_id       := updating('tnt_id');
      cg$rec.rol_id       := cg$uit_claims.cg$table(idx).rol_id;
      cg$ind.rol_id       := updating('rol_id');
      cg$rec.usr_id       := cg$uit_claims.cg$table(idx).usr_id;
      cg$ind.usr_id       := updating('usr_id');
      cg$rec.rowversion   := cg$uit_claims.cg$table(idx).rowversion;
      cg$ind.rowversion   := updating('rowversion');
      cg$rec.created_by   := cg$uit_claims.cg$table(idx).created_by;
      cg$ind.created_by   := updating('created_by');
      cg$rec.created_on   := cg$uit_claims.cg$table(idx).created_on;
      cg$ind.created_on   := updating('created_on');
      cg$rec.updated_by   := cg$uit_claims.cg$table(idx).updated_by;
      cg$ind.updated_by   := updating('updated_by');
      cg$rec.updated_on   := cg$uit_claims.cg$table(idx).updated_on;
      cg$ind.updated_on   := updating('updated_on');

      cg$uit_claims.validate_foreign_keys_upd(cg$rec, cg$old_rec, cg$ind);
      cg$uit_claims.cascade_update(cg$rec, cg$old_rec);

      cg$uit_claims.called_from_package := FALSE;
    END IF;
    idx := cg$uit_claims.cg$table.NEXT(idx);
  END LOOP;

  cg$uit_claims.cg$table.DELETE;

  -- Application_logic Post-After-Update-statement <<Start>>
  -- Application_logic Post-After-Update-statement << End >>
END;
/
SHOW ERROR
PROMPT Creating Before Delete Statement Trigger on 'uit_claims'
CREATE OR REPLACE TRIGGER cg$bds_uit_claims
BEFORE DELETE ON uit_claims
BEGIN
  -- Application_logic Pre-Before-Delete-statement <<Start>>
  -- Application_logic Pre-Before-Delete-statement << End >>

  cg$uit_claims.cg$table.DELETE;
  cg$uit_claims.cg$tableind.DELETE;
  cg$uit_claims.idx := 1;

  -- Application_logic Post-Before-Delete-statement <<Start>>
  -- Application_logic Post-Before-Delete-statement << End >>
END;
/
SHOW ERROR
PROMPT Creating Before Delete Row Trigger on 'uit_claims'
CREATE OR REPLACE TRIGGER cg$bdr_uit_claims
BEFORE DELETE ON uit_claims FOR EACH ROW
DECLARE
  cg$pk  cg$uit_claims.cg$pk_type;
  cg$rec cg$uit_claims.cg$row_type;
  cg$ind cg$uit_claims.cg$ind_type;
BEGIN
  -- Application_logic Pre-Before-Delete-row <<Start>>
  -- Application_logic Pre-Before-Delete-row << End >>

  -- Load cg$rec/cg$ind values from old
  cg$pk.tnt_id  := :old.tnt_id;
  cg$rec.tnt_id := :old.tnt_id;
  cg$uit_claims.cg$table(cg$uit_claims.idx).tnt_id := :old.tnt_id;
  cg$pk.rol_id  := :old.rol_id;
  cg$rec.rol_id := :old.rol_id;
  cg$uit_claims.cg$table(cg$uit_claims.idx).rol_id := :old.rol_id;
  cg$pk.usr_id  := :old.usr_id;
  cg$rec.usr_id := :old.usr_id;
  cg$uit_claims.cg$table(cg$uit_claims.idx).usr_id := :old.usr_id;

  cg$uit_claims.idx := cg$uit_claims.idx + 1;
  IF NOT (cg$uit_claims.called_from_package)
  THEN
    cg$uit_claims.del(cg$pk, FALSE);
    cg$uit_claims.called_from_package := FALSE;
  END IF;

  -- Application_logic Post-Before-Delete-row <<Start>>
  -- Application_logic Post-Before-Delete-row << End >>
END;
/
SHOW ERROR
PROMPT Creating After Delete Statement Trigger on 'uit_claims'
CREATE OR REPLACE TRIGGER cg$ads_uit_claims
AFTER DELETE ON uit_claims
DECLARE
  idx        BINARY_INTEGER := cg$uit_claims.cg$table.FIRST;
  cg$rec     cg$uit_claims.cg$row_type;
  cg$old_rec cg$uit_claims.cg$row_type;
BEGIN
  -- Application_logic Pre-After-Delete-statement <<Start>>
  -- Application_logic Pre-After-Delete-statement << End >>

  IF NOT (cg$uit_claims.called_from_package)
  THEN
    WHILE idx IS NOT NULL
    LOOP
      cg$rec.tnt_id := cg$uit_claims.cg$table(idx).tnt_id;
      cg$uit_claims.cg$tableind(idx).tnt_id := TRUE;

      cg$rec.rol_id := cg$uit_claims.cg$table(idx).rol_id;
      cg$uit_claims.cg$tableind(idx).rol_id := TRUE;

      cg$rec.usr_id := cg$uit_claims.cg$table(idx).usr_id;
      cg$uit_claims.cg$tableind(idx).usr_id := TRUE;

      cg$rec.rowversion := cg$uit_claims.cg$table(idx).rowversion;
      cg$uit_claims.cg$tableind(idx).rowversion := TRUE;

      cg$rec.created_by := cg$uit_claims.cg$table(idx).created_by;
      cg$uit_claims.cg$tableind(idx).created_by := TRUE;

      cg$rec.created_on := cg$uit_claims.cg$table(idx).created_on;
      cg$uit_claims.cg$tableind(idx).created_on := TRUE;

      cg$rec.updated_by := cg$uit_claims.cg$table(idx).updated_by;
      cg$uit_claims.cg$tableind(idx).updated_by := TRUE;

      cg$rec.updated_on := cg$uit_claims.cg$table(idx).updated_on;
      cg$uit_claims.cg$tableind(idx).updated_on := TRUE;

      cg$uit_claims.validate_foreign_keys_del(cg$rec);
      cg$uit_claims.cascade_delete(cg$rec);

      idx := cg$uit_claims.cg$table.NEXT(idx);
    END LOOP;
  END IF;

  -- Application_logic Post-After-Delete-statement <<Start>>
  -- Application_logic Post-After-Delete-statement << End >>
END;
/
SHOW ERROR