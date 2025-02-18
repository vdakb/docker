-- <?xml version = '1.0' encoding = 'UTF-8'?>
-- <trigger xmlns="http://xmlns.oracle.com/jdeveloper/1211/offlinedb">
--   <name>CG$AIS_QMS_RULE_PROPERTIES</name>
--   <enabled>true</enabled>
--   <properties>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_CONNECTION</key>
--       <value class="java.lang.String">igs@hardy</value>
--     </entry>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_ID</key>
--       <value class="oracle.javatools.db.ReferenceID">
--         <name>CG$AIS_QMS_RULE_PROPERTIES</name>
--         <identifier class="java.math.BigDecimal">46698</identifier>
--         <schemaName>IGD_IGS</schemaName>
--         <type>TRIGGER</type>
--       </value>
--     </entry>
--   </properties>
--   <statementLevel>true</statementLevel>
-- </trigger>

CREATE OR REPLACE
TRIGGER cg$ais_qms_rule_properties
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
