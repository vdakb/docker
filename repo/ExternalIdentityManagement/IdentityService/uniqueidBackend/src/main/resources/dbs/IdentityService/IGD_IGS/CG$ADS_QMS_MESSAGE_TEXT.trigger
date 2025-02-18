-- <?xml version = '1.0' encoding = 'UTF-8'?>
-- <trigger xmlns="http://xmlns.oracle.com/jdeveloper/1211/offlinedb">
--   <name>CG$ADS_QMS_MESSAGE_TEXT</name>
--   <enabled>true</enabled>
--   <properties>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_CONNECTION</key>
--       <value class="java.lang.String">igs@hardy</value>
--     </entry>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_ID</key>
--       <value class="oracle.javatools.db.ReferenceID">
--         <name>CG$ADS_QMS_MESSAGE_TEXT</name>
--         <identifier class="java.math.BigDecimal">46695</identifier>
--         <schemaName>IGD_IGS</schemaName>
--         <type>TRIGGER</type>
--       </value>
--     </entry>
--   </properties>
--   <statementLevel>true</statementLevel>
-- </trigger>

CREATE OR REPLACE
TRIGGER cg$ads_qms_message_text
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
      cg$rec.code                                  := qms$message_text.cg$table(idx).code;
      qms$message_text.cg$tableind(idx).code       := TRUE;
      cg$rec.language                              := qms$message_text.cg$table(idx).language;
      qms$message_text.cg$tableind(idx).language   := TRUE;
      cg$rec.text                                  := qms$message_text.cg$table(idx).text;
      qms$message_text.cg$tableind(idx).text       := TRUE;
      cg$rec.help                                  := qms$message_text.cg$table(idx).help;
      qms$message_text.cg$tableind(idx).help       := TRUE;
      cg$rec.created_by                            := qms$message_text.cg$table(idx).created_by;
      qms$message_text.cg$tableind(idx).created_by := TRUE;
      cg$rec.created_on                            := qms$message_text.cg$table(idx).created_on;
      qms$message_text.cg$tableind(idx).created_on := TRUE;
      cg$rec.updated_by                            := qms$message_text.cg$table(idx).updated_by;
      qms$message_text.cg$tableind(idx).updated_by := TRUE;
      cg$rec.updated_on                            := qms$message_text.cg$table(idx).updated_on;
      qms$message_text.cg$tableind(idx).updated_on := TRUE;

      qms$message_text.validate_foreign_keys_del(cg$rec);
      qms$message_text.cascade_delete(cg$rec);

      idx := qms$message_text.cg$table.NEXT(idx);
    END LOOP;
  END IF;

  --  Application_logic Post-After-Delete-statement <<Start>>
  --  Application_logic Post-After-Delete-statement << End >>
END;
/
