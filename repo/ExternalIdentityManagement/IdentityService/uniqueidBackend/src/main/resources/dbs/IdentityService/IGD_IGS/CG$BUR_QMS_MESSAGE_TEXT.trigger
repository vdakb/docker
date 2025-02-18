-- <?xml version = '1.0' encoding = 'UTF-8'?>
-- <trigger xmlns="http://xmlns.oracle.com/jdeveloper/1211/offlinedb">
--   <name>CG$BUR_QMS_MESSAGE_TEXT</name>
--   <enabled>true</enabled>
--   <properties>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_CONNECTION</key>
--       <value class="java.lang.String">igs@hardy</value>
--     </entry>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_ID</key>
--       <value class="oracle.javatools.db.ReferenceID">
--         <name>CG$BUR_QMS_MESSAGE_TEXT</name>
--         <identifier class="java.math.BigDecimal">46691</identifier>
--         <schemaName>IGD_IGS</schemaName>
--         <type>TRIGGER</type>
--       </value>
--     </entry>
--   </properties>
--   <statementLevel>true</statementLevel>
-- </trigger>

CREATE OR REPLACE
TRIGGER cg$bur_qms_message_text
BEFORE UPDATE ON qms_message_text FOR EACH ROW
DECLARE
  cg$rec qms$message_text.cg$row_type;
  cg$ind qms$message_text.cg$ind_type;
BEGIN
  --  Application_logic Pre-Before-Update-row <<Start>>
  --  Application_logic Pre-Before-Update-row << End >>

  --  Load cg$rec/cg$ind values from new
  cg$rec.code := :new.code;
  cg$ind.code := (:new.code IS NULL AND :old.code IS NOT NULL )
              OR (:new.code IS NOT NULL AND :old.code IS NULL)
              OR NOT(:new.code = :old.code);
  cg$rec.language := :new.language;
  cg$ind.language := (:new.language IS NULL AND :old.language IS NOT NULL )
                  OR (:new.language IS NOT NULL AND :old.language IS NULL)
                  OR NOT(:new.language = :old.language);
  cg$rec.text := :new.text;
  cg$ind.text := (:new.text IS NULL AND :old.text IS NOT NULL )
              OR (:new.text IS NOT NULL AND :old.text IS NULL)
              OR NOT(:new.text = :old.text);
  cg$rec.help := :new.help;
  cg$ind.help := (:new.help IS NULL AND :old.help IS NOT NULL )
                   OR (:new.help IS NOT NULL AND :old.help IS NULL)
                   OR NOT(:new.help = :old.help);
  cg$rec.created_by := :new.created_by;
  cg$ind.created_by := (:new.created_by IS NULL AND :old.created_by IS NOT NULL )
                    OR (:new.created_by IS NOT NULL AND :old.created_by IS NULL)
                    OR NOT(:new.created_by = :old.created_by);
  cg$rec.created_on := :new.created_on;
  cg$ind.created_on := (:new.created_on IS NULL AND :old.created_on IS NOT NULL )
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

  qms$message_text.cg$table(qms$message_text.idx).code       := :old.code;
  qms$message_text.cg$table(qms$message_text.idx).language   := :old.language;
  qms$message_text.cg$table(qms$message_text.idx).text       := :old.text;
  qms$message_text.cg$table(qms$message_text.idx).help       := :old.help;
  qms$message_text.cg$table(qms$message_text.idx).created_by := :old.created_by;
  qms$message_text.cg$table(qms$message_text.idx).created_on := :old.created_on;
  qms$message_text.cg$table(qms$message_text.idx).updated_by := :old.updated_by;
  qms$message_text.cg$table(qms$message_text.idx).updated_on := :old.updated_on;

  qms$message_text.idx := qms$message_text.idx + 1;

  IF NOT (qms$message_text.called_from_package)
  THEN
    qms$message_text.upd(cg$rec, cg$ind, FALSE);
    qms$message_text.called_from_package := FALSE;
  END IF;

  :new.code       := cg$rec.code;
  :new.language   := cg$rec.language;
  :new.text       := cg$rec.text;
  :new.help       := cg$rec.help;
  :new.created_by := cg$rec.created_by;
  :new.created_on := cg$rec.created_on;
  :new.updated_by := cg$rec.updated_by;
  :new.updated_on := cg$rec.updated_on;

  --  Application_logic Post-Before-Update-row <<Start>>
  --  Application_logic Post-Before-Update-row << End >>
END;
/
