-- D:\Project\Bundeskriminalamt12c\ExternalIdentityManagement\IdentityService\identityIdentifier\src\main\resources\dbs\qms\api\qms_msp.trg
--
-- Generated for Oracle Database 12c on Fri Mar 11 12.47.21 2022 by Server Generator 10.1.2.93.10

PROMPT ======================================================
PROMPT Creating Package Interface for Table 'qms_message_properties'
PROMPT ======================================================
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating Before Insert Statement Trigger on 'qms_message_properties'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
CREATE OR REPLACE TRIGGER cg$bis_qms_message_properties
BEFORE INSERT ON qms_message_properties
BEGIN
  --  Application_logic Pre-Before-Insert-statement <<Start>>
  --  Application_logic Pre-Before-Insert-statement << End >>

  qms$message_properties.cg$table.DELETE;
  qms$message_properties.cg$tableind.DELETE;
  qms$message_properties.idx := 1;

  --  Application_logic Post-Before-Insert-statement <<Start>>
  --  Application_logic Post-Before-Insert-statement << End >>
END;
/
SHOW ERROR
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating Before Insert Row Trigger on 'qms_message_properties'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
CREATE OR REPLACE TRIGGER cg$bir_qms_message_properties
BEFORE INSERT ON qms_message_properties FOR EACH ROW
DECLARE
  cg$rec qms$message_properties.cg$row_type;
  cg$ind qms$message_properties.cg$ind_type;
BEGIN
  --  Application_logic Pre-Before-Insert-row <<Start>>
  --  Application_logic Pre-Before-Insert-row << End >>

  --  Load cg$rec/cg$ind values from new
  cg$rec.code             := :new.code;
  cg$ind.code             := TRUE;
  cg$rec.description      := :new.description;
  cg$ind.description      := TRUE;
  cg$rec.severity         := :new.severity;
  cg$ind.severity         := TRUE;
  cg$rec.logging          := :new.logging;
  cg$ind.logging          := TRUE;
  cg$rec.suppress_warning := :new.suppress_warning;
  cg$ind.suppress_warning := TRUE;
  cg$rec.suppress_always  := :new.suppress_always;
  cg$ind.suppress_always  := TRUE;
  cg$rec.constraint_name  := :new.constraint_name;
  cg$ind.constraint_name  := TRUE;

  cg$rec.created_by      := :new.created_by;
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

  IF NOT (qms$message_properties.called_from_package)
  THEN
    qms$message_properties.ins(cg$rec, cg$ind, FALSE);
    qms$message_properties.called_from_package := FALSE;
  END IF;

  :new.code             := cg$rec.code;
  :new.description      := cg$rec.description;
  :new.severity         := cg$rec.severity;
  :new.logging          := cg$rec.logging;
  :new.suppress_warning := cg$rec.suppress_warning;
  :new.suppress_always  := cg$rec.suppress_always;
  :new.constraint_name  := cg$rec.constraint_name;
  :new.created_by       := cg$rec.created_by;
  :new.created_on       := cg$rec.created_on;
  :new.updated_by       := cg$rec.updated_by;
  :new.updated_on       := cg$rec.updated_on;

  --  Application_logic Post-Before-Insert-row <<Start>>
  --  Application_logic Post-Before-Insert-row << End >>
END;
/
SHOW ERROR
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating After Insert Row Trigger on 'qms_message_properties'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
CREATE OR REPLACE TRIGGER cg$air_qms_message_properties
AFTER INSERT ON qms_message_properties FOR EACH ROW
DECLARE
  cg$rec qms$message_properties.cg$row_type;
  cg$ind qms$message_properties.cg$ind_type;
BEGIN
  --  Application_logic Pre-After-Insert-row <<Start>>
  --  Application_logic Pre-After-Insert-row << End >>

  cg$rec.code             := :new.code;
  cg$ind.code             := TRUE;
  cg$rec.description      := :new.description;
  cg$ind.description      := TRUE;
  cg$rec.severity         := :new.severity;
  cg$ind.severity         := TRUE;
  cg$rec.logging          := :new.logging;
  cg$ind.logging          := TRUE;
  cg$rec.suppress_warning := :new.suppress_warning;
  cg$ind.suppress_warning := TRUE;
  cg$rec.suppress_always  := :new.suppress_always;
  cg$ind.suppress_always  := TRUE;
  cg$rec.constraint_name  := :new.constraint_name;
  cg$ind.constraint_name  := TRUE;
  cg$rec.created_by       := :new.created_by;
  cg$ind.created_by       := TRUE;
  cg$rec.created_on       := :new.created_on;
  cg$ind.created_on       := TRUE;
  cg$rec.updated_by       := :new.updated_by;
  cg$ind.updated_by       := TRUE;
  cg$rec.updated_on       := :new.updated_on;
  cg$ind.updated_on       := TRUE;

  --  Application_logic Post-After-Insert-row <<Start>>
  --  Application_logic Post-After-Insert-row << End >>
END;
/
SHOW ERROR
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating After Insert Statement Trigger on 'qms_message_properties'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
CREATE OR REPLACE TRIGGER cg$ais_qms_message_properties
AFTER INSERT ON qms_message_properties
DECLARE
  fk_check   INTEGER;
  idx        BINARY_INTEGER := qms$message_properties.cg$table.FIRST;
  cg$rec     qms$message_properties.cg$row_type;
  cg$old_rec qms$message_properties.cg$row_type;
BEGIN
  --  Application_logic Pre-After-Insert-statement <<Start>>
  --  Application_logic Pre-After-Insert-statement << End >>

  IF NOT (qms$message_properties.called_from_package)
  THEN
    WHILE idx IS NOT NULL
    LOOP
      cg$rec.code             := qms$message_properties.cg$table(idx).code;
      cg$rec.description      := qms$message_properties.cg$table(idx).description;
      cg$rec.severity         := qms$message_properties.cg$table(idx).severity;
      cg$rec.logging          := qms$message_properties.cg$table(idx).logging;
      cg$rec.suppress_warning := qms$message_properties.cg$table(idx).suppress_warning;
      cg$rec.suppress_always  := qms$message_properties.cg$table(idx).suppress_always;
      cg$rec.constraint_name  := qms$message_properties.cg$table(idx).constraint_name;
      cg$rec.created_by       := qms$message_properties.cg$table(idx).created_by;
      cg$rec.created_on       := qms$message_properties.cg$table(idx).created_on;
      cg$rec.updated_by       := qms$message_properties.cg$table(idx).updated_by;
      cg$rec.updated_on       := qms$message_properties.cg$table(idx).updated_on;

      qms$message_properties.validate_foreign_keys_ins(cg$rec);

      idx := qms$message_properties.cg$table.NEXT(idx);
    END LOOP;
  END IF;

  --  Application_logic Post-After-Insert-statement <<Start>>
  --  Application_logic Post-After-Insert-statement << End >>
END;
/
SHOW ERROR
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating Before Update Statement Trigger on 'qms_message_properties'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
CREATE OR REPLACE TRIGGER cg$bus_qms_message_properties
BEFORE UPDATE ON qms_message_properties
BEGIN
  --  Application_logic Pre-Before-Update-statement <<Start>>
  --  Application_logic Pre-Before-Update-statement << End >>

  qms$message_properties.cg$table.DELETE;
  qms$message_properties.cg$tableind.DELETE;
  qms$message_properties.idx := 1;

--  Application_logic Post-Before-Update-statement <<Start>>
--  Application_logic Post-Before-Update-statement << End >>
END;
/
SHOW ERROR
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating Before Update Row Trigger on 'qms_message_properties'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
CREATE OR REPLACE TRIGGER cg$bur_qms_message_properties
BEFORE UPDATE ON qms_message_properties FOR EACH ROW
DECLARE
  cg$rec qms$message_properties.cg$row_type;
  cg$ind qms$message_properties.cg$ind_type;
BEGIN
  --  Application_logic Pre-Before-Update-row <<Start>>
  --  Application_logic Pre-Before-Update-row << End >>

  --  Load cg$rec/cg$ind values from new
  cg$rec.code := :new.code;
  cg$ind.code := (:new.code IS NULL AND :old.code IS NOT NULL )
              OR (:old.code IS NULL AND :new.code IS NOT NULL)
              OR NOT(:new.code = :old.code);
  cg$rec.description := :new.description;
  cg$ind.description := (:new.description IS NULL AND :old.description IS NOT NULL )
                     OR (:new.description IS NOT NULL AND :old.description IS NULL)
                     OR NOT(:new.description = :old.description);
  cg$rec.severity := :new.severity;
  cg$ind.severity := (:new.severity IS NULL AND :old.severity IS NOT NULL )
                  OR (:new.severity IS NOT NULL AND :old.severity IS NULL)
                  OR NOT(:new.severity = :old.severity);
  cg$rec.logging := :new.logging;
  cg$ind.logging := (:new.logging IS NULL AND :old.logging IS NOT NULL )
                 OR (:new.logging IS NOT NULL AND :old.logging IS NULL)
                 OR NOT(:new.logging = :old.logging);
  cg$rec.suppress_warning := :new.suppress_warning;
  cg$ind.suppress_warning := (:new.suppress_warning IS NULL AND :old.suppress_warning IS NOT NULL )
                          OR (:new.suppress_warning IS NOT NULL AND :old.suppress_warning IS NULL)
                          OR NOT(:new.suppress_warning = :old.suppress_warning);
  cg$rec.suppress_always := :new.suppress_always;
  cg$ind.suppress_always := (:new.suppress_always IS NULL AND :old.suppress_always IS NOT NULL )
                         OR (:new.suppress_always IS NOT NULL AND :old.suppress_always IS NULL)
                         OR NOT(:new.suppress_always = :old.suppress_always);
  cg$rec.constraint_name := :new.constraint_name;
  cg$ind.constraint_name := (:new.constraint_name IS NULL AND :old.constraint_name IS NOT NULL )
                         OR (:new.constraint_name IS NOT NULL AND :old.constraint_name IS NULL)
                         OR NOT(:new.constraint_name = :old.constraint_name);
  cg$rec.created_by := :new.created_by;
  cg$ind.created_by := (:new.created_by IS NULL AND :old.created_by IS NOT NULL )
                    OR (:new.created_by IS NOT NULL AND :old.created_by IS NULL)
                    OR NOT(:new.created_by = :old.created_by);
  cg$rec.created_on := :new.created_on;
  cg$ind.created_on :=  (:new.created_on IS NULL AND :old.created_on IS NOT NULL )
                    OR (:new.created_on IS NOT NULL AND :old.created_on IS NULL)
                    OR NOT(:new.created_on = :old.created_on);
  cg$rec.updated_by := :new.updated_by;
  cg$ind.updated_by := (:new.updated_by IS NULL AND :old.updated_by IS NOT NULL )
                    OR (:new.updated_by IS NOT NULL AND :old.updated_by IS NULL)
                    OR NOT(:new.updated_by = :old.updated_by);
  cg$rec.updated_on := :new.updated_on;
  cg$ind.updated_on :=  (:new.updated_on IS NULL AND :old.updated_on IS NOT NULL )
                    OR (:new.updated_on IS NOT NULL AND :old.updated_on IS NULL)
                    OR NOT(:new.updated_on = :old.updated_on);

  qms$message_properties.cg$table(qms$message_properties.idx).code             := :old.code;
  qms$message_properties.cg$table(qms$message_properties.idx).description      := :old.description;
  qms$message_properties.cg$table(qms$message_properties.idx).severity         := :old.severity;
  qms$message_properties.cg$table(qms$message_properties.idx).logging          := :old.logging;
  qms$message_properties.cg$table(qms$message_properties.idx).suppress_warning := :old.suppress_warning;
  qms$message_properties.cg$table(qms$message_properties.idx).suppress_always  := :old.suppress_always;
  qms$message_properties.cg$table(qms$message_properties.idx).constraint_name  := :old.constraint_name;
  qms$message_properties.cg$table(qms$message_properties.idx).created_by       := :old.created_by;
  qms$message_properties.cg$table(qms$message_properties.idx).created_on       := :old.created_on;
  qms$message_properties.cg$table(qms$message_properties.idx).updated_by       := :old.updated_by;
  qms$message_properties.cg$table(qms$message_properties.idx).updated_on       := :old.updated_on;

  qms$message_properties.idx := qms$message_properties.idx + 1;

  IF NOT (qms$message_properties.called_from_package)
  THEN
    qms$message_properties.upd(cg$rec, cg$ind, FALSE);
    qms$message_properties.called_from_package := FALSE;
  END IF;

  :new.code             := cg$rec.code;
  :new.description      := cg$rec.description;
  :new.severity         := cg$rec.severity;
  :new.logging          := cg$rec.logging;
  :new.suppress_warning := cg$rec.suppress_warning;
  :new.suppress_always  := cg$rec.suppress_always;
  :new.constraint_name  := cg$rec.constraint_name;
  :new.created_by       := cg$rec.created_by;
  :new.created_on       := cg$rec.created_on;
  :new.updated_by       := cg$rec.updated_by;
  :new.updated_on       := cg$rec.updated_on;

  --  Application_logic Post-Before-Update-row <<Start>>
  --  Application_logic Post-Before-Update-row << End >>
END;
/
SHOW ERROR
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating After Update Statement Trigger on 'qms_message_properties'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
CREATE OR REPLACE TRIGGER cg$aus_qms_message_properties
AFTER UPDATE ON qms_message_properties
DECLARE
  idx        BINARY_INTEGER := qms$message_properties.cg$table.FIRST;
  cg$old_rec qms$message_properties.cg$row_type;
  cg$rec     qms$message_properties.cg$row_type;
  cg$ind     qms$message_properties.cg$ind_type;
BEGIN
  --  Application_logic Pre-After-Update-statement <<Start>>
  --  Application_logic Pre-After-Update-statement << End >>

  WHILE idx IS NOT NULL
  LOOP
    cg$old_rec.code             := qms$message_properties.cg$table(idx).code;
    cg$old_rec.description      := qms$message_properties.cg$table(idx).description;
    cg$old_rec.severity         := qms$message_properties.cg$table(idx).severity;
    cg$old_rec.logging          := qms$message_properties.cg$table(idx).logging;
    cg$old_rec.suppress_warning := qms$message_properties.cg$table(idx).suppress_warning;
    cg$old_rec.suppress_always  := qms$message_properties.cg$table(idx).suppress_always;
    cg$old_rec.constraint_name  := qms$message_properties.cg$table(idx).constraint_name;
    cg$old_rec.created_by       := qms$message_properties.cg$table(idx).created_by;
    cg$old_rec.created_on       := qms$message_properties.cg$table(idx).created_on;
    cg$old_rec.updated_by       := qms$message_properties.cg$table(idx).updated_by;
    cg$old_rec.updated_on       := qms$message_properties.cg$table(idx).updated_on;

    IF NOT (qms$message_properties.called_from_package)
    THEN
      idx := qms$message_properties.cg$table.NEXT(idx);

      cg$rec.code             := qms$message_properties.cg$table(idx).code;
      cg$ind.code             := updating('code');
      cg$rec.description      := qms$message_properties.cg$table(idx).description;
      cg$ind.description      := updating('description');
      cg$rec.severity         := qms$message_properties.cg$table(idx).severity;
      cg$ind.severity         := updating('severity');
      cg$rec.logging          := qms$message_properties.cg$table(idx).logging;
      cg$ind.logging          := updating('logging');
      cg$rec.suppress_warning := qms$message_properties.cg$table(idx).suppress_warning;
      cg$ind.suppress_warning := updating('suppress_warning');
      cg$rec.suppress_always  := qms$message_properties.cg$table(idx).suppress_always;
      cg$ind.suppress_always  := updating('suppress_always');
      cg$rec.constraint_name  := qms$message_properties.cg$table(idx).constraint_name;
      cg$ind.constraint_name  := updating('constraint_name');
      cg$rec.created_by       := qms$message_properties.cg$table(idx).created_by;
      cg$ind.created_by       := updating('created_by');
      cg$rec.created_on       := qms$message_properties.cg$table(idx).created_on;
      cg$ind.created_on       := updating('created_on');
      cg$rec.updated_by       := qms$message_properties.cg$table(idx).updated_by;
      cg$ind.updated_by       := updating('updated_by');
      cg$rec.updated_on       := qms$message_properties.cg$table(idx).updated_on;
      cg$ind.updated_on       := updating('updated_on');

      qms$message_properties.cascade_update(cg$rec, cg$old_rec);

      qms$message_properties.called_from_package := FALSE;
    END IF;
    idx := qms$message_properties.cg$table.NEXT(idx);
  END LOOP;

  qms$message_properties.cg$table.DELETE;

  --  Application_logic Post-After-Update-statement <<Start>>
  --  Application_logic Post-After-Update-statement << End >>
END;
/
SHOW ERROR
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating Before Delete Statement Trigger on 'qms_message_properties'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
CREATE OR REPLACE TRIGGER cg$bds_qms_message_properties
BEFORE DELETE ON qms_message_properties
BEGIN
  --  Application_logic Pre-Before-Delete-statement <<Start>>
  --  Application_logic Pre-Before-Delete-statement << End >>

  qms$message_properties.cg$table.DELETE;
  qms$message_properties.cg$tableind.DELETE;
  qms$message_properties.idx := 1;

  --  Application_logic Post-Before-Delete-statement <<Start>>
  --  Application_logic Post-Before-Delete-statement << End >>
END;
/
SHOW ERROR
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating Before Delete Row Trigger on 'qms_message_properties'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
CREATE OR REPLACE TRIGGER cg$bdr_qms_message_properties
BEFORE DELETE ON qms_message_properties FOR EACH ROW
DECLARE
  cg$pk  qms$message_properties.cg$pk_type;
  cg$rec qms$message_properties.cg$row_type;
  cg$ind qms$message_properties.cg$ind_type;
BEGIN
  --  Application_logic Pre-Before-Delete-row <<Start>>
  --  Application_logic Pre-Before-Delete-row << End >>

  --  Load cg$rec/cg$ind values from new

  cg$pk.code  := :old.code;
  cg$rec.code := :old.code;
  qms$message_properties.cg$table(qms$message_properties.idx).code := cg$pk.code;

  qms$message_properties.idx := qms$message_properties.idx + 1;

  IF NOT (qms$message_properties.called_from_package)
  THEN
    qms$message_properties.del(cg$pk, FALSE);
    qms$message_properties.called_from_package := FALSE;
 END IF;

  --  Application_logic Post-Before-Delete-row <<Start>>
  --  Application_logic Post-Before-Delete-row << End >>
END;
/
SHOW ERROR
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating After Delete Statement Trigger on 'qms_message_properties'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
CREATE OR REPLACE TRIGGER cg$ads_qms_message_properties
AFTER DELETE ON qms_message_properties
DECLARE
  idx        BINARY_INTEGER := qms$message_properties.cg$table.FIRST;
  cg$rec     qms$message_properties.cg$row_type;
  cg$old_rec qms$message_properties.cg$row_type;
BEGIN
  --  Application_logic Pre-After-Delete-statement <<Start>>
  --  Application_logic Pre-After-Delete-statement << End >>

  IF NOT (qms$message_properties.called_from_package)
  THEN
    WHILE idx IS NOT NULL
    LOOP
      cg$rec.code                                              := qms$message_properties.cg$table(idx).code;
      qms$message_properties.cg$tableind(idx).code             := TRUE;
      cg$rec.description                                       := qms$message_properties.cg$table(idx).description;
      qms$message_properties.cg$tableind(idx).description      := TRUE;
      cg$rec.severity                                          := qms$message_properties.cg$table(idx).severity;
      qms$message_properties.cg$tableind(idx).severity         := TRUE;
      cg$rec.logging                                           := qms$message_properties.cg$table(idx).logging;
      qms$message_properties.cg$tableind(idx).logging          := TRUE;
      cg$rec.suppress_warning                                  := qms$message_properties.cg$table(idx).suppress_warning;
      qms$message_properties.cg$tableind(idx).suppress_warning := TRUE;
      cg$rec.suppress_always                                   := qms$message_properties.cg$table(idx).suppress_always;
      qms$message_properties.cg$tableind(idx).suppress_always  := TRUE;
      cg$rec.constraint_name                                   := qms$message_properties.cg$table(idx).constraint_name;
      qms$message_properties.cg$tableind(idx).constraint_name  := TRUE;
      cg$rec.created_by                                        := qms$message_properties.cg$table(idx).created_by;
      qms$message_properties.cg$tableind(idx).created_by       := TRUE;
      cg$rec.created_on                                        := qms$message_properties.cg$table(idx).created_on;
      qms$message_properties.cg$tableind(idx).created_on       := TRUE;
      cg$rec.updated_by                                        := qms$message_properties.cg$table(idx).updated_by;
      qms$message_properties.cg$tableind(idx).updated_by       := TRUE;
      cg$rec.updated_on                                        := qms$message_properties.cg$table(idx).updated_on;
      qms$message_properties.cg$tableind(idx).updated_on       := TRUE;

      qms$message_properties.validate_foreign_keys_del(cg$rec);
      qms$message_properties.cascade_delete(cg$rec);

      idx := qms$message_properties.cg$table.NEXT(idx);
    END LOOP;
  END IF;

  --  Application_logic Post-After-Delete-statement <<Start>>
  --  Application_logic Post-After-Delete-statement << End >>
END;
/
SHOW ERROR