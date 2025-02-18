-- <?xml version = '1.0' encoding = 'UTF-8'?>
-- <trigger xmlns="http://xmlns.oracle.com/jdeveloper/1211/offlinedb">
--   <name>CG$AIS_QMS_MESSAGE_PROPERTIES</name>
--   <enabled>true</enabled>
--   <properties>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_CONNECTION</key>
--       <value class="java.lang.String">igs@hardy</value>
--     </entry>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_ID</key>
--       <value class="oracle.javatools.db.ReferenceID">
--         <name>CG$AIS_QMS_MESSAGE_PROPERTIES</name>
--         <identifier class="java.math.BigDecimal">46679</identifier>
--         <schemaName>IGD_IGS</schemaName>
--         <type>TRIGGER</type>
--       </value>
--     </entry>
--   </properties>
--   <statementLevel>true</statementLevel>
-- </trigger>

CREATE OR REPLACE
TRIGGER cg$ais_qms_message_properties
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
