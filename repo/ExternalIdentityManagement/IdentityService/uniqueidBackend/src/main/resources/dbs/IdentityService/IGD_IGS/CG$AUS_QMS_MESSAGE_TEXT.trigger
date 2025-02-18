-- <?xml version = '1.0' encoding = 'UTF-8'?>
-- <trigger xmlns="http://xmlns.oracle.com/jdeveloper/1211/offlinedb">
--   <name>CG$AUS_QMS_MESSAGE_TEXT</name>
--   <enabled>true</enabled>
--   <properties>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_CONNECTION</key>
--       <value class="java.lang.String">igs@hardy</value>
--     </entry>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_ID</key>
--       <value class="oracle.javatools.db.ReferenceID">
--         <name>CG$AUS_QMS_MESSAGE_TEXT</name>
--         <identifier class="java.math.BigDecimal">46692</identifier>
--         <schemaName>IGD_IGS</schemaName>
--         <type>TRIGGER</type>
--       </value>
--     </entry>
--   </properties>
--   <statementLevel>true</statementLevel>
-- </trigger>

CREATE OR REPLACE
TRIGGER cg$aus_qms_message_text
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
    cg$old_rec.code       := qms$message_text.cg$table(idx).code;
    cg$old_rec.language   := qms$message_text.cg$table(idx).language;
    cg$old_rec.text       := qms$message_text.cg$table(idx).text;
    cg$old_rec.help       := qms$message_text.cg$table(idx).help;
    cg$old_rec.created_by := qms$message_text.cg$table(idx).created_by;
    cg$old_rec.created_on := qms$message_text.cg$table(idx).created_on;
    cg$old_rec.updated_by := qms$message_text.cg$table(idx).updated_by;
    cg$old_rec.updated_on := qms$message_text.cg$table(idx).updated_on;

    IF NOT (qms$message_text.called_from_package)
    THEN
      idx := qms$message_text.cg$table.NEXT(idx);

      cg$rec.code       := qms$message_text.cg$table(idx).code;
      cg$ind.code       := updating('code');
      cg$rec.language   := qms$message_text.cg$table(idx).language;
      cg$ind.language   := updating('language');
      cg$rec.text       := qms$message_text.cg$table(idx).text;
      cg$ind.text       := updating('text');
      cg$rec.help       := qms$message_text.cg$table(idx).help;
      cg$ind.help       := updating('help');
      cg$rec.created_by := qms$message_text.cg$table(idx).created_by;
      cg$ind.created_by := updating('created_by');
      cg$rec.created_on := qms$message_text.cg$table(idx).created_on;
      cg$ind.created_on := updating('created_on');
      cg$rec.updated_by := qms$message_text.cg$table(idx).updated_by;
      cg$ind.updated_by := updating('updated_by');
      cg$rec.updated_on := qms$message_text.cg$table(idx).updated_on;
      cg$ind.updated_on := updating('updated_on');

      qms$message_text.validate_foreign_keys_upd(cg$rec, cg$old_rec, cg$ind);
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
