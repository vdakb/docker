-- D:\Project\Bundeskriminalamt12c\ExternalIdentityManagement\IdentityService\identityIdentifier\src\main\resources\dbs\qms\api\qms_rlp.trg
--
-- Generated for Oracle Database 12c on Fri Mar 11 12.47.21 2022 by Server Generator 10.1.2.93.10

PROMPT ======================================================
PROMPT Creating Package Interface for Table 'qms_message_text'
PROMPT ======================================================
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating Before Insert Statement Trigger on 'qms_rule_properties'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
CREATE OR REPLACE TRIGGER cg$bis_qms_rule_properties
BEFORE INSERT ON qms_rule_properties
BEGIN
  --  Application_logic Pre-Before-Insert-statement <<Start>>
  --  Application_logic Pre-Before-Insert-statement << End >>

  qms$rule_properties.cg$table.DELETE;
  qms$rule_properties.cg$tableind.DELETE;
  qms$rule_properties.idx := 1;

  --  Application_logic Post-Before-Insert-statement <<Start>>
  --  Application_logic Post-Before-Insert-statement << End >>
END;
/
SHOW ERROR
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating Before Insert Row Trigger on 'qms_rule_properties'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
CREATE OR REPLACE TRIGGER cg$bir_qms_rule_properties
BEFORE INSERT ON qms_rule_properties FOR EACH ROW
DECLARE
  cg$rec qms$rule_properties.cg$row_type;
  cg$ind qms$rule_properties.cg$ind_type;
BEGIN
  --  Application_logic Pre-Before-Insert-row <<Start>>
  --  Application_logic Pre-Before-Insert-row << End >>

  --  Load cg$rec/cg$ind values from new
  cg$rec.process := :new.process;
  cg$ind.process := TRUE;
  cg$rec.code    := :new.code;
  cg$ind.code    := TRUE;
  cg$rec.name    := :new.name;
  cg$ind.name    := TRUE;
  cg$rec.enabled := :new.enabled;
  cg$ind.enabled := TRUE;
  cg$rec.remark  := :new.remark;
  cg$ind.remark  := TRUE;
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

  IF NOT (qms$rule_properties.called_from_package)
  THEN
    qms$rule_properties.ins(cg$rec, cg$ind, FALSE);
    qms$rule_properties.called_from_package := FALSE;
  END IF;

  qms$rule_properties.cg$table(qms$rule_properties.idx).process := cg$rec.process;
  qms$rule_properties.cg$tableind(qms$rule_properties.idx).process := cg$ind.process;

  qms$rule_properties.cg$table(qms$rule_properties.idx).code := cg$rec.code;
  qms$rule_properties.cg$tableind(qms$rule_properties.idx).code := cg$ind.code;

  qms$rule_properties.cg$table(qms$rule_properties.idx).name := cg$rec.name;
  qms$rule_properties.cg$tableind(qms$rule_properties.idx).name := cg$ind.name;

  qms$rule_properties.cg$table(qms$rule_properties.idx).enabled := cg$rec.enabled;
  qms$rule_properties.cg$tableind(qms$rule_properties.idx).enabled := cg$ind.enabled;

  qms$rule_properties.cg$table(qms$rule_properties.idx).remark := cg$rec.remark;
  qms$rule_properties.cg$tableind(qms$rule_properties.idx).remark := cg$ind.remark;

  qms$rule_properties.cg$table(qms$rule_properties.idx).created_by := cg$rec.created_by;
  qms$rule_properties.cg$tableind(qms$rule_properties.idx).created_by := cg$ind.created_by;

  qms$rule_properties.cg$table(qms$rule_properties.idx).created_on := cg$rec.created_on;
  qms$rule_properties.cg$tableind(qms$rule_properties.idx).created_on := cg$ind.created_on;

  qms$rule_properties.cg$table(qms$rule_properties.idx).updated_by := cg$rec.updated_by;
  qms$rule_properties.cg$tableind(qms$rule_properties.idx).updated_by := cg$ind.updated_by;

  qms$rule_properties.cg$table(qms$rule_properties.idx).updated_on := cg$rec.updated_on;
  qms$rule_properties.cg$tableind(qms$rule_properties.idx).updated_on := cg$ind.updated_on;

  qms$rule_properties.idx := qms$rule_properties.idx + 1;

  :new.process    := cg$rec.process;
  :new.code       := cg$rec.code;
  :new.name       := cg$rec.name;
  :new.enabled    := cg$rec.enabled;
  :new.remark     := cg$rec.remark;
  :new.created_by := cg$rec.created_by;
  :new.created_on := cg$rec.created_on;
  :new.updated_by := cg$rec.updated_by;
  :new.updated_on := cg$rec.updated_on;

  --  Application_logic Post-Before-Insert-row <<Start>>
  --  Application_logic Post-Before-Insert-row << End >>
END;
/
SHOW ERROR
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating After Insert Statement Trigger on 'qms_rule_properties'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
CREATE OR REPLACE TRIGGER cg$ais_qms_rule_properties
AFTER INSERT ON qms_rule_properties
DECLARE
  fk_check   INTEGER;
  idx        BINARY_INTEGER := qms$rule_properties.cg$table.FIRST;
  cg$rec     qms$rule_properties.cg$row_type;
  cg$old_rec qms$rule_properties.cg$row_type;
BEGIN
  --  Application_logic Pre-After-Insert-statement <<Start>>
  --  Application_logic Pre-After-Insert-statement << End >>

  IF NOT (qms$rule_properties.called_from_package)
  THEN
    WHILE idx IS NOT NULL
    LOOP
      cg$rec.process    := qms$rule_properties.cg$table(idx).process;
      cg$rec.code       := qms$rule_properties.cg$table(idx).code;
      cg$rec.name       := qms$rule_properties.cg$table(idx).name;
      cg$rec.enabled    := qms$rule_properties.cg$table(idx).enabled;
      cg$rec.remark     := qms$rule_properties.cg$table(idx).remark;
      cg$rec.created_by := qms$rule_properties.cg$table(idx).created_by;
      cg$rec.created_on := qms$rule_properties.cg$table(idx).created_on;
      cg$rec.updated_by := qms$rule_properties.cg$table(idx).updated_by;
      cg$rec.updated_on := qms$rule_properties.cg$table(idx).updated_on;

      qms$rule_properties.validate_foreign_keys_ins(cg$rec);

      idx := qms$rule_properties.cg$table.NEXT(idx);
    END LOOP;
  END IF;

  --  Application_logic Post-After-Insert-statement <<Start>>
  --  Application_logic Post-After-Insert-statement << End >>
END;
/
SHOW ERROR
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating Before Update Statement Trigger on 'qms_rule_properties'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
CREATE OR REPLACE TRIGGER cg$bus_qms_rule_properties
BEFORE UPDATE ON qms_rule_properties
BEGIN
  --  Application_logic Pre-Before-Update-statement <<Start>>
  --  Application_logic Pre-Before-Update-statement << End >>

  qms$rule_properties.cg$table.DELETE;
  qms$rule_properties.cg$tableind.DELETE;
  qms$rule_properties.idx := 1;

  --  Application_logic Post-Before-Update-statement <<Start>>
  --  Application_logic Post-Before-Update-statement << End >>
END;
/
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating Before Update Row Trigger on 'qms_rule_properties'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
CREATE OR REPLACE TRIGGER cg$bur_qms_rule_properties
BEFORE UPDATE ON qms_rule_properties FOR EACH ROW
DECLARE
  cg$rec     qms$rule_properties.cg$row_type;
  cg$ind     qms$rule_properties.cg$ind_type;
  cg$old_rec qms$rule_properties.cg$row_type;
BEGIN
  --  Application_logic Pre-Before-Update-row <<Start>>
  --  Application_logic Pre-Before-Update-row << End >>

  --  Load cg$rec/cg$ind values from new
  cg$rec.process := :new.process;
  cg$ind.process := (:new.process IS NULL AND :old.process IS NOT NULL )
                 OR (:new.process IS NOT NULL AND :old.process IS NULL)
                 OR NOT(:new.process = :old.process) ;
  qms$rule_properties.cg$table(qms$rule_properties.idx).process := :old.process;
  cg$rec.code := :new.code;
  cg$ind.code := (:new.code IS NULL AND :old.code IS NOT NULL )
              OR (:new.code IS NOT NULL AND :old.code IS NULL)
              OR NOT(:new.code = :old.code) ;
  qms$rule_properties.cg$table(qms$rule_properties.idx).code := :old.code;
  cg$rec.name := :new.name;
  cg$ind.name := (:new.name IS NULL AND :old.name IS NOT NULL )
              OR (:new.name IS NOT NULL AND :old.name IS NULL)
              OR NOT(:new.name = :old.name) ;
  qms$rule_properties.cg$table(qms$rule_properties.idx).name := :old.name;
  cg$rec.enabled := :new.enabled;
  cg$ind.enabled := (:new.enabled IS NULL AND :old.enabled IS NOT NULL )
                 OR (:new.enabled IS NOT NULL AND :old.enabled IS NULL)
                 OR NOT(:new.enabled = :old.enabled) ;
  qms$rule_properties.cg$table(qms$rule_properties.idx).enabled := :old.enabled;
  cg$rec.remark := :new.remark;
  cg$ind.remark := (:new.remark IS NULL AND :old.remark IS NOT NULL )
                OR (:new.remark IS NOT NULL AND :old.remark IS NULL)
                OR NOT(:new.remark = :old.remark) ;
  qms$rule_properties.cg$table(qms$rule_properties.idx).remark := :old.remark;
  cg$rec.created_by := :new.created_by;
  cg$ind.created_by := (:new.created_by IS NULL AND :old.created_by IS NOT NULL )
                    OR (:new.created_by IS NOT NULL AND :old.created_by IS NULL)
                    OR NOT(:new.created_by = :old.created_by) ;
  qms$rule_properties.cg$table(qms$rule_properties.idx).created_by := :old.created_by;
  cg$rec.created_on := :new.created_on;
  cg$ind.created_on := (:new.created_on IS NULL AND :old.created_on IS NOT NULL )
                    OR (:new.created_on IS NOT NULL AND :old.created_on IS NULL)
                    OR NOT(:new.created_on = :old.created_on) ;
  qms$rule_properties.cg$table(qms$rule_properties.idx).created_on := :old.created_on;
  cg$rec.updated_by := :new.updated_by;
  cg$ind.updated_by := (:new.updated_by IS NULL AND :old.updated_by IS NOT NULL )
                    OR (:new.updated_by IS NOT NULL AND :old.updated_by IS NULL)
                    OR NOT(:new.updated_by = :old.updated_by) ;
  qms$rule_properties.cg$table(qms$rule_properties.idx).updated_by := :old.updated_by;
  cg$rec.updated_on := :new.updated_on;
  cg$ind.updated_on := (:new.updated_on IS NULL AND :old.updated_on IS NOT NULL )
                    OR (:new.updated_on IS NOT NULL AND :old.updated_on IS NULL)
                    OR NOT(:new.updated_on = :old.updated_on) ;
  qms$rule_properties.cg$table(qms$rule_properties.idx).updated_on := :old.updated_on;

  qms$rule_properties.idx := qms$rule_properties.idx + 1;

  IF NOT (qms$rule_properties.called_from_package)
  THEN
    qms$rule_properties.upd(cg$rec, cg$ind, FALSE);
    qms$rule_properties.called_from_package := FALSE;
  END IF;

  :new.name       := cg$rec.name;
  :new.enabled    := cg$rec.enabled;
  :new.remark     := cg$rec.remark;
  :new.created_by := cg$rec.created_by;
  :new.created_on := cg$rec.created_on;
  :new.updated_by := cg$rec.updated_by;
  :new.updated_on := cg$rec.updated_on;

  --  Application_logic Post-Before-Update-row <<Start>>
  --  Application_logic Post-Before-Update-row << End >>
END;
/
SHOW ERROR
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating After Update Statement Trigger on 'qms_rule_properties'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
CREATE OR REPLACE TRIGGER cg$aus_qms_rule_properties
AFTER UPDATE ON qms_rule_properties
DECLARE
  idx        BINARY_INTEGER := qms$rule_properties.cg$table.FIRST;
  cg$old_rec qms$rule_properties.cg$row_type;
  cg$rec     qms$rule_properties.cg$row_type;
  cg$ind     qms$rule_properties.cg$ind_type;
BEGIN
  --  Application_logic Pre-After-Update-statement <<Start>>
  --  Application_logic Pre-After-Update-statement << End >>

  WHILE idx IS NOT NULL
  LOOP
    cg$old_rec.process    := qms$rule_properties.cg$table(idx).process;
    cg$old_rec.code       := qms$rule_properties.cg$table(idx).code;
    cg$old_rec.name       := qms$rule_properties.cg$table(idx).name;
    cg$old_rec.enabled    := qms$rule_properties.cg$table(idx).enabled;
    cg$old_rec.remark     := qms$rule_properties.cg$table(idx).remark;
    cg$old_rec.created_by := qms$rule_properties.cg$table(idx).created_by;
    cg$old_rec.created_on := qms$rule_properties.cg$table(idx).created_on;
    cg$old_rec.updated_by := qms$rule_properties.cg$table(idx).updated_by;
    cg$old_rec.updated_on := qms$rule_properties.cg$table(idx).updated_on;

    IF NOT (qms$rule_properties.called_from_package)
    THEN
      idx := qms$rule_properties.cg$table.NEXT(idx);
      cg$rec.process    := qms$rule_properties.cg$table(idx).process;
      cg$ind.process    := updating('process');
      cg$rec.code       := qms$rule_properties.cg$table(idx).code;
      cg$ind.code       := updating('code');
      cg$rec.name       := qms$rule_properties.cg$table(idx).name;
      cg$ind.name       := updating('name');
      cg$rec.enabled    := qms$rule_properties.cg$table(idx).enabled;
      cg$ind.enabled    := updating('enabled');
      cg$rec.remark     := qms$rule_properties.cg$table(idx).remark;
      cg$ind.remark     := updating('remark');
      cg$rec.created_by := qms$rule_properties.cg$table(idx).created_by;
      cg$ind.created_by := updating('created_by');
      cg$rec.created_on := qms$rule_properties.cg$table(idx).created_on;
      cg$ind.created_on := updating('created_on');
      cg$rec.updated_by := qms$rule_properties.cg$table(idx).updated_by;
      cg$ind.updated_by := updating('updated_by');
      cg$rec.updated_on := qms$rule_properties.cg$table(idx).updated_on;
      cg$ind.updated_on := updating('updated_on');

      qms$rule_properties.validate_foreign_keys_upd(cg$rec, cg$old_rec, cg$ind);
      qms$rule_properties.cascade_update(cg$rec, cg$old_rec);

      qms$rule_properties.called_from_package := FALSE;
    END IF;
    idx := qms$rule_properties.cg$table.NEXT(idx);
  END LOOP;
  qms$rule_properties.cg$table.DELETE;

  --  Application_logic Post-After-Update-statement <<Start>>
  --  Application_logic Post-After-Update-statement << End >>
END;
/
SHOW ERROR
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating Before Delete Statement Trigger on 'qms_rule_properties'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
CREATE OR REPLACE TRIGGER cg$bds_qms_rule_properties
BEFORE DELETE ON qms_rule_properties
BEGIN
  --  Application_logic Pre-Before-Delete-statement <<Start>>
  --  Application_logic Pre-Before-Delete-statement << End >>

  qms$rule_properties.cg$table.DELETE;
  qms$rule_properties.cg$tableind.DELETE;
  qms$rule_properties.idx := 1;

  --  Application_logic Post-Before-Delete-statement <<Start>>
  --  Application_logic Post-Before-Delete-statement << End >>
END;
/
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating Before Delete Row Trigger on 'qms_rule_properties'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
CREATE OR REPLACE TRIGGER cg$bdr_qms_rule_properties
BEFORE DELETE ON qms_rule_properties FOR EACH ROW
DECLARE
  cg$pk qms$rule_properties.cg$pk_type;
  cg$rec qms$rule_properties.cg$row_type;
  cg$ind qms$rule_properties.cg$ind_type;
BEGIN
  --  Application_logic Pre-Before-Delete-row <<Start>>
  --  Application_logic Pre-Before-Delete-row << End >>

  --  Load cg$rec/cg$ind values from new
  cg$pk.process  := :old.process;
  cg$rec.process := :old.process;
  qms$rule_properties.cg$table(qms$rule_properties.idx).process := :old.process;
  cg$pk.code  := :old.code;
  cg$rec.code := :old.code;
  qms$rule_properties.cg$table(qms$rule_properties.idx).code := :old.code;

  qms$rule_properties.idx := qms$rule_properties.idx + 1;

  IF NOT (qms$rule_properties.called_from_package)
  THEN
    qms$rule_properties.del(cg$pk, FALSE);
    qms$rule_properties.called_from_package := FALSE;
  END IF;

  --  Application_logic Post-Before-Delete-row <<Start>>
  --  Application_logic Post-Before-Delete-row << End >>
END;
/
SHOW ERROR
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating After Delete Statement Trigger on 'qms_rule_properties'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
CREATE OR REPLACE TRIGGER cg$ads_qms_rule_properties
AFTER DELETE ON qms_rule_properties
DECLARE
  idx        BINARY_INTEGER := qms$rule_properties.cg$table.FIRST;
  cg$rec   qms$rule_properties.cg$row_type;
  cg$old_rec   qms$rule_properties.cg$row_type;
BEGIN
  --  Application_logic Pre-After-Delete-statement <<Start>>
  --  Application_logic Pre-After-Delete-statement << End >>

  IF NOT (qms$rule_properties.called_from_package)
  THEN
    WHILE idx IS NOT NULL
    LOOP
      cg$rec.process                                  := qms$rule_properties.cg$table(idx).process;
      qms$rule_properties.cg$tableind(idx).process    := TRUE;
      cg$rec.code                                     := qms$rule_properties.cg$table(idx).code;
      qms$rule_properties.cg$tableind(idx).code       := TRUE;
      cg$rec.name                                     := qms$rule_properties.cg$table(idx).name;
      qms$rule_properties.cg$tableind(idx).name       := TRUE;
      cg$rec.enabled                                  := qms$rule_properties.cg$table(idx).enabled;
      qms$rule_properties.cg$tableind(idx).enabled    := TRUE;
      cg$rec.remark                                   := qms$rule_properties.cg$table(idx).remark;
      qms$rule_properties.cg$tableind(idx).remark     := TRUE;
      cg$rec.created_by                               := qms$rule_properties.cg$table(idx).created_by;
      qms$rule_properties.cg$tableind(idx).created_by := TRUE;
      cg$rec.created_on                               := qms$rule_properties.cg$table(idx).created_on;
      qms$rule_properties.cg$tableind(idx).created_on := TRUE;
      cg$rec.updated_by                               := qms$rule_properties.cg$table(idx).updated_by;
      qms$rule_properties.cg$tableind(idx).updated_by := TRUE;
      cg$rec.updated_on                               := qms$rule_properties.cg$table(idx).updated_on;
      qms$rule_properties.cg$tableind(idx).updated_on := TRUE;

      qms$rule_properties.validate_foreign_keys_del(cg$rec);
      qms$rule_properties.cascade_delete(cg$rec);

      idx := qms$rule_properties.cg$table.NEXT(idx);
    END LOOP;
  END IF;

  --  Application_logic Post-After-Delete-statement <<Start>>
  --  Application_logic Post-After-Delete-statement << End >>
END;
/
SHOW ERROR