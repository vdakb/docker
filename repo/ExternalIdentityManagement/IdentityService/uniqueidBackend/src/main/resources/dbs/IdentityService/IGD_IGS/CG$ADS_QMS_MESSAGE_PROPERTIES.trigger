-- <?xml version = '1.0' encoding = 'UTF-8'?>
-- <trigger xmlns="http://xmlns.oracle.com/jdeveloper/1211/offlinedb">
--   <name>CG$ADS_QMS_MESSAGE_PROPERTIES</name>
--   <enabled>true</enabled>
--   <properties>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_CONNECTION</key>
--       <value class="java.lang.String">igs@hardy</value>
--     </entry>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_ID</key>
--       <value class="oracle.javatools.db.ReferenceID">
--         <name>CG$ADS_QMS_MESSAGE_PROPERTIES</name>
--         <identifier class="java.math.BigDecimal">46685</identifier>
--         <schemaName>IGD_IGS</schemaName>
--         <type>TRIGGER</type>
--       </value>
--     </entry>
--   </properties>
--   <statementLevel>true</statementLevel>
-- </trigger>

CREATE OR REPLACE
TRIGGER cg$ads_qms_message_properties
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
