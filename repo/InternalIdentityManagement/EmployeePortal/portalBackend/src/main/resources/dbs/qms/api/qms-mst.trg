-- D:\Project\Bundeskriminalamt12c\InternalIdentityManagement\EmployeePortal\eppAplication\src\main\dbs\qms\api\qms-mst.trg
--
-- Generated for Oracle Database 12c on Fri Apr 24 10:07:06 2020 by Server Generator 10.1.2.93.10

PROMPT Creating Trigger Logic for Table 'qms_message_text'
PROMPT Creating Before Insert Statement Trigger on 'qms_message_text'
CREATE OR REPLACE TRIGGER cg$bis_qms_message_text
BEFORE INSERT ON qms_message_text
BEGIN
  --  Application_logic Pre-Before-Insert-statement <<Start>>
  --  Application_logic Pre-Before-Insert-statement << End >>

  qms$message_text.cg$table.DELETE;
  qms$message_text.cg$tableind.DELETE;
  qms$message_text.idx := 1;

  --  Application_logic Post-Before-Insert-statement <<Start>>
  --  Application_logic Post-Before-Insert-statement << End >>
END;
/

PROMPT Creating Before Insert Row Trigger on 'qms_message_text'
CREATE OR REPLACE TRIGGER cg$bir_qms_message_text
BEFORE INSERT ON qms_message_text FOR EACH ROW
DECLARE
  cg$rec qms$message_text.cg$row_type;
  cg$ind qms$message_text.cg$ind_type;
BEGIN
  --  Application_logic Pre-Before-Insert-row <<Start>>
  --  Application_logic Pre-Before-Insert-row << End >>

  --  Load cg$rec/cg$ind values from new
  cg$rec.language  := :new.language;
  cg$ind.language  := TRUE;
  cg$rec.text      := :new.text;
  cg$ind.text      := TRUE;
  cg$rec.help_text := :new.help_text;
  cg$ind.help_text := TRUE;
  cg$rec.msp_code  := :new.msp_code;
  cg$ind.msp_code  := TRUE;

  cg$rec.created_by  := :new.created_by;
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

  IF NOT (qms$message_text.called_from_package)
  THEN
    qms$message_text.validate_arc(cg$rec);
    qms$message_text.validate_domain(cg$rec);

    qms$message_text.ins(cg$rec, cg$ind, FALSE);
    qms$message_text.called_from_package := FALSE;
  END IF;

  :new.language   := cg$rec.language;
  :new.text       := cg$rec.text;
  :new.help_text  := cg$rec.help_text;
  :new.msp_code   := cg$rec.msp_code;
  :new.created_by := cg$rec.created_by;
  :new.created_on := cg$rec.created_on;
  :new.updated_by := cg$rec.updated_by;
  :new.updated_on := cg$rec.updated_on;

  --  Application_logic Post-Before-Insert-row <<Start>>
  --  Application_logic Post-Before-Insert-row << End >>
END;
/

PROMPT Creating After Insert Row Trigger on 'qms_message_text'
CREATE OR REPLACE TRIGGER cg$air_qms_message_text
AFTER INSERT ON qms_message_text FOR EACH ROW
DECLARE
  cg$rec qms$message_text.cg$row_type;
  cg$ind qms$message_text.cg$ind_type;
BEGIN
  --  Application_logic Pre-After-Insert-row <<Start>>
  --  Application_logic Pre-After-Insert-row << End >>

  cg$rec.language   := :new.language;
  cg$ind.language   := TRUE;
  cg$rec.text       := :new.text;
  cg$ind.text       := TRUE;
  cg$rec.help_text  := :new.help_text;
  cg$ind.help_text  := TRUE;
  cg$rec.msp_code   := :new.msp_code;
  cg$ind.msp_code   := TRUE;
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

PROMPT Creating After Insert Statement Trigger on 'qms_message_text'
CREATE OR REPLACE TRIGGER cg$ais_qms_message_text
AFTER INSERT ON qms_message_text
DECLARE
  fk_check   INTEGER;
  idx        BINARY_INTEGER := qms$message_text.cg$table.FIRST;
  cg$rec     qms$message_text.cg$row_type;
  cg$old_rec qms$message_text.cg$row_type;
BEGIN
  --  Application_logic Pre-After-Insert-statement <<Start>>
  --  Application_logic Pre-After-Insert-statement << End >>

  IF NOT (qms$message_text.called_from_package)
  THEN
    WHILE idx IS NOT NULL
    LOOP
      cg$rec.language   := qms$message_text.cg$table(idx).language;
      cg$rec.text       := qms$message_text.cg$table(idx).text;
      cg$rec.help_text  := qms$message_text.cg$table(idx).help_text;
      cg$rec.msp_code   := qms$message_text.cg$table(idx).msp_code;
      cg$rec.created_by := qms$message_text.cg$table(idx).created_by;
      cg$rec.created_on := qms$message_text.cg$table(idx).created_on;
      cg$rec.updated_by := qms$message_text.cg$table(idx).updated_by;
      cg$rec.updated_on := qms$message_text.cg$table(idx).updated_on;

      qms$message_text.validate_foreign_keys_ins(cg$rec);
      qms$message_text.upd_oper_denorm2(cg$rec, cg$old_rec, qms$message_text.cg$tableind(idx), 'INS');

      idx := qms$message_text.cg$table.NEXT(idx);
    END LOOP;
  END IF;

  --  Application_logic Post-After-Insert-statement <<Start>>
  --  Application_logic Post-After-Insert-statement << End >>
END;
/

PROMPT Creating Before Update Statement Trigger on 'qms_message_text'
CREATE OR REPLACE TRIGGER cg$bus_qms_message_text
BEFORE UPDATE ON qms_message_text
BEGIN
  --  Application_logic Pre-Before-Update-statement <<Start>>
  --  Application_logic Pre-Before-Update-statement << End >>

  qms$message_text.cg$table.DELETE;
  qms$message_text.cg$tableind.DELETE;
  qms$message_text.idx := 1;

  --  Application_logic Post-Before-Update-statement <<Start>>
  --  Application_logic Post-Before-Update-statement << End >>
END;
/

PROMPT Creating Before Update Row Trigger on 'qms_message_text'
CREATE OR REPLACE TRIGGER cg$bur_qms_message_text
BEFORE UPDATE ON qms_message_text FOR EACH ROW
DECLARE
  cg$rec qms$message_text.cg$row_type;
  cg$ind qms$message_text.cg$ind_type;
BEGIN
  --  Application_logic Pre-Before-Update-row <<Start>>
  --  Application_logic Pre-Before-Update-row << End >>

  --  Load cg$rec/cg$ind values from new
  cg$rec.language := :new.language;
  cg$ind.language := (:new.language IS NULL AND :old.language IS NOT NULL )
                  OR (:new.language IS NOT NULL AND :old.language IS NULL)
                  OR NOT(:new.language = :old.language) ;
  cg$rec.text := :new.text;
  cg$ind.text := (:new.text IS NULL AND :old.text IS NOT NULL )
              OR (:new.text IS NOT NULL AND :old.text IS NULL)
              OR NOT(:new.text = :old.text) ;
  cg$rec.help_text := :new.help_text;
  cg$ind.help_text := (:new.help_text IS NULL AND :old.help_text IS NOT NULL )
                   OR (:new.help_text IS NOT NULL AND :old.help_text IS NULL)
                   OR NOT(:new.help_text = :old.help_text) ;
  cg$rec.msp_code := :new.msp_code;
  cg$ind.msp_code := (:new.msp_code IS NULL AND :old.msp_code IS NOT NULL )
                  OR (:new.msp_code IS NOT NULL AND :old.msp_code IS NULL)
                  OR NOT(:new.msp_code = :old.msp_code) ;
  cg$rec.created_by := :new.created_by;
  cg$ind.created_by := (:new.created_by IS NULL AND :old.created_by IS NOT NULL )
                    OR (:new.created_by IS NOT NULL AND :old.created_by IS NULL)
                    OR NOT(:new.created_by = :old.created_by) ;
  cg$rec.created_on := :new.created_on;
  cg$ind.created_on := (:new.created_on IS NULL AND :old.created_on IS NOT NULL )
                    OR (:new.created_on IS NOT NULL AND :old.created_on IS NULL)
                    OR NOT(:new.created_on = :old.created_on) ;
  cg$rec.updated_by := :new.updated_by;
  cg$ind.updated_by := (:new.updated_by IS NULL AND :old.updated_by IS NOT NULL )
                    OR (:new.updated_by IS NOT NULL AND :old.updated_by IS NULL)
                    OR NOT(:new.updated_by = :old.updated_by) ;
  cg$rec.updated_on := :new.updated_on;
  cg$ind.updated_on :=  (:new.updated_on IS NULL AND :old.updated_on IS NOT NULL )
                    OR (:new.updated_on IS NOT NULL AND :old.updated_on IS NULL)
                    OR NOT(:new.updated_on = :old.updated_on) ;

  qms$message_text.cg$table(qms$message_text.idx).language   := :old.language;
  qms$message_text.cg$table(qms$message_text.idx).text       := :old.text;
  qms$message_text.cg$table(qms$message_text.idx).help_text  := :old.help_text;
  qms$message_text.cg$table(qms$message_text.idx).msp_code   := :old.msp_code;
  qms$message_text.cg$table(qms$message_text.idx).created_by := :old.created_by;
  qms$message_text.cg$table(qms$message_text.idx).created_on := :old.created_on;
  qms$message_text.cg$table(qms$message_text.idx).updated_by := :old.updated_by;
  qms$message_text.cg$table(qms$message_text.idx).updated_on := :old.updated_on;

  qms$message_text.idx := qms$message_text.idx + 1;

  IF NOT (qms$message_text.called_from_package)
  THEN
    qms$message_text.validate_arc(cg$rec);
    qms$message_text.validate_domain(cg$rec);

    qms$message_text.upd(cg$rec, cg$ind, FALSE);
    qms$message_text.called_from_package := FALSE;
  END IF;

  :new.language   := cg$rec.language;
  :new.text       := cg$rec.text;
  :new.help_text  := cg$rec.help_text;
  :new.msp_code   := cg$rec.msp_code;
  :new.created_by := cg$rec.created_by;
  :new.created_on := cg$rec.created_on;
  :new.updated_by := cg$rec.updated_by;
  :new.updated_on := cg$rec.updated_on;

  --  Application_logic Post-Before-Update-row <<Start>>
  --  Application_logic Post-Before-Update-row << End >>
END;
/

PROMPT Creating After Update Row Trigger on 'qms_message_text'
CREATE OR REPLACE TRIGGER cg$aur_qms_message_text
AFTER UPDATE ON qms_message_text FOR EACH ROW
BEGIN
  --  Application_logic Pre-After-Update-row <<Start>>
  --  Application_logic Pre-After-Update-row << End >>

  NULL;

  --  Application_logic Post-After-Update-row <<Start>>
  --  Application_logic Post-After-Update-row << End >>
END;
/

PROMPT Creating After Update Statement Trigger on 'qms_message_text'
CREATE OR REPLACE TRIGGER cg$aus_qms_message_text
AFTER UPDATE ON qms_message_text
DECLARE
  idx        BINARY_INTEGER := qms$message_text.cg$table.FIRST;
  cg$old_rec qms$message_text.cg$row_type;
  cg$rec     qms$message_text.cg$row_type;
  cg$ind     qms$message_text.cg$ind_type;
BEGIN
  --  Application_logic Pre-After-Update-statement <<Start>>
  --  Application_logic Pre-After-Update-statement << End >>

  WHILE idx IS NOT NULL
  LOOP
    cg$old_rec.language   := qms$message_text.cg$table(idx).language;
    cg$old_rec.text       := qms$message_text.cg$table(idx).text;
    cg$old_rec.help_text  := qms$message_text.cg$table(idx).help_text;
    cg$old_rec.msp_code   := qms$message_text.cg$table(idx).msp_code;
    cg$old_rec.created_by := qms$message_text.cg$table(idx).created_by;
    cg$old_rec.created_on := qms$message_text.cg$table(idx).created_on;
    cg$old_rec.updated_by := qms$message_text.cg$table(idx).updated_by;
    cg$old_rec.updated_on := qms$message_text.cg$table(idx).updated_on;

    IF NOT (qms$message_text.called_from_package)
    THEN
      idx := qms$message_text.cg$table.NEXT(idx);

      cg$rec.language   := qms$message_text.cg$table(idx).language;
      cg$ind.language   := updating('language');
      cg$rec.text       := qms$message_text.cg$table(idx).text;
      cg$ind.text       := updating('text');
      cg$rec.help_text  := qms$message_text.cg$table(idx).help_text;
      cg$ind.help_text  := updating('help_text');
      cg$rec.msp_code   := qms$message_text.cg$table(idx).msp_code;
      cg$ind.msp_code   := updating('msp_code');
      cg$rec.created_by := qms$message_text.cg$table(idx).created_by;
      cg$ind.created_by := updating('created_by');
      cg$rec.created_on := qms$message_text.cg$table(idx).created_on;
      cg$ind.created_on := updating('created_on');
      cg$rec.updated_by := qms$message_text.cg$table(idx).updated_by;
      cg$ind.updated_by := updating('updated_by');
      cg$rec.updated_on := qms$message_text.cg$table(idx).updated_on;
      cg$ind.updated_on := updating('updated_on');

      qms$message_text.validate_foreign_keys_upd(cg$rec, cg$old_rec, cg$ind);
      qms$message_text.upd_denorm2(cg$rec, qms$message_text.cg$tableind(idx));
      qms$message_text.upd_oper_denorm2(cg$rec, cg$old_rec, qms$message_text.cg$tableind(idx));
      qms$message_text.cascade_update(cg$rec, cg$old_rec);

      qms$message_text.called_from_package := FALSE;
    END IF;
    idx := qms$message_text.cg$table.NEXT(idx);
  END LOOP;

  qms$message_text.cg$table.DELETE;

  --  Application_logic Post-After-Update-statement <<Start>>
  --  Application_logic Post-After-Update-statement << End >>
END;
/

PROMPT Creating Before Delete Statement Trigger on 'qms_message_text'
CREATE OR REPLACE TRIGGER cg$bds_qms_message_text
BEFORE DELETE ON qms_message_text
BEGIN
  --  Application_logic Pre-Before-Delete-statement <<Start>>
  --  Application_logic Pre-Before-Delete-statement << End >>

  qms$message_text.cg$table.DELETE;
  qms$message_text.cg$tableind.DELETE;
  qms$message_text.idx := 1;

  --  Application_logic Post-Before-Delete-statement <<Start>>
  --  Application_logic Post-Before-Delete-statement << End >>
END;
/

PROMPT Creating Before Delete Row Trigger on 'qms_message_text'
CREATE OR REPLACE TRIGGER cg$bdr_qms_message_text
BEFORE DELETE ON qms_message_text FOR EACH ROW
DECLARE
  cg$pk qms$message_text.cg$pk_type;
  cg$rec qms$message_text.cg$row_type;
  cg$ind qms$message_text.cg$ind_type;
BEGIN
  --  Application_logic Pre-Before-Delete-row <<Start>>
  --  Application_logic Pre-Before-Delete-row << End >>

  --  Load cg$rec/cg$ind values from new
  cg$pk.msp_code  := :old.msp_code;
  cg$rec.msp_code := :old.msp_code;
  qms$message_text.cg$table(qms$message_text.idx).msp_code := cg$pk.msp_code;

  cg$pk.language  := :old.language;
  cg$rec.language := :old.language;
  qms$message_text.cg$table(qms$message_text.idx).language := cg$pk.language;

  cg$rec.msp_code := :old.msp_code;
  qms$message_text.cg$table(qms$message_text.idx).msp_code := cg$rec.msp_code;

  qms$message_text.idx := qms$message_text.idx + 1;

  IF NOT (qms$message_text.called_from_package)
  THEN
    qms$message_text.del(cg$pk, FALSE);
    qms$message_text.called_from_package := FALSE;
  END IF;

  --  Application_logic Post-Before-Delete-row <<Start>>
  --  Application_logic Post-Before-Delete-row << End >>
END;
/

PROMPT Creating After Delete Row Trigger on 'qms_message_text'
PROMPT Creating After Delete Statement Trigger on 'qms_message_text'
CREATE OR REPLACE TRIGGER cg$ads_qms_message_text
AFTER DELETE ON qms_message_text
DECLARE
  idx        BINARY_INTEGER := qms$message_text.cg$table.FIRST;
  cg$rec     qms$message_text.cg$row_type;
  cg$old_rec qms$message_text.cg$row_type;
BEGIN
  --  Application_logic Pre-After-Delete-statement <<Start>>
  --  Application_logic Pre-After-Delete-statement << End >>

  IF NOT (qms$message_text.called_from_package)
  THEN
    WHILE idx IS NOT NULL
    LOOP
      cg$rec.language                              := qms$message_text.cg$table(idx).language;
      qms$message_text.cg$tableind(idx).language   := TRUE;
      cg$rec.text                                  := qms$message_text.cg$table(idx).text;
      qms$message_text.cg$tableind(idx).text       := TRUE;
      cg$rec.help_text                             := qms$message_text.cg$table(idx).help_text;
      qms$message_text.cg$tableind(idx).help_text  := TRUE;
      cg$rec.msp_code                              := qms$message_text.cg$table(idx).msp_code;
      qms$message_text.cg$tableind(idx).msp_code   := TRUE;
      cg$rec.created_by                            := qms$message_text.cg$table(idx).created_by;
      qms$message_text.cg$tableind(idx).created_by := TRUE;
      cg$rec.created_on                            := qms$message_text.cg$table(idx).created_on;
      qms$message_text.cg$tableind(idx).created_on := TRUE;
      cg$rec.updated_by                            := qms$message_text.cg$table(idx).updated_by;
      qms$message_text.cg$tableind(idx).updated_by := TRUE;
      cg$rec.updated_on                            := qms$message_text.cg$table(idx).updated_on;
      qms$message_text.cg$tableind(idx).updated_on := TRUE;

      qms$message_text.validate_foreign_keys_del(cg$rec);
      qms$message_text.upd_oper_denorm2(cg$rec, cg$old_rec, qms$message_text.cg$tableind(idx), 'DEL');
      qms$message_text.cascade_delete(cg$rec);

      idx := qms$message_text.cg$table.NEXT(idx);
    END LOOP;
  END IF;

  --  Application_logic Post-After-Delete-statement <<Start>>
  --  Application_logic Post-After-Delete-statement << End >>
END;
/