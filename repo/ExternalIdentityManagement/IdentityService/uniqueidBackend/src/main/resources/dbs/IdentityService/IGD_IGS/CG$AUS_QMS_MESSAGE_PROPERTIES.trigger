-- <?xml version = '1.0' encoding = 'UTF-8'?>
-- <trigger xmlns="http://xmlns.oracle.com/jdeveloper/1211/offlinedb">
--   <name>CG$AUS_QMS_MESSAGE_PROPERTIES</name>
--   <enabled>true</enabled>
--   <properties>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_CONNECTION</key>
--       <value class="java.lang.String">igs@hardy</value>
--     </entry>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_ID</key>
--       <value class="oracle.javatools.db.ReferenceID">
--         <name>CG$AUS_QMS_MESSAGE_PROPERTIES</name>
--         <identifier class="java.math.BigDecimal">46682</identifier>
--         <schemaName>IGD_IGS</schemaName>
--         <type>TRIGGER</type>
--       </value>
--     </entry>
--   </properties>
--   <statementLevel>true</statementLevel>
-- </trigger>

CREATE OR REPLACE
TRIGGER cg$aus_qms_message_properties
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
