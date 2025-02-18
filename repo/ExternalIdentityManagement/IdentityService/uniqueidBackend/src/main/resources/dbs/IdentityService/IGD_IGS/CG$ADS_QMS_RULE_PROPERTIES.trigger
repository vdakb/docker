-- <?xml version = '1.0' encoding = 'UTF-8'?>
-- <trigger xmlns="http://xmlns.oracle.com/jdeveloper/1211/offlinedb">
--   <name>CG$ADS_QMS_RULE_PROPERTIES</name>
--   <enabled>true</enabled>
--   <properties>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_CONNECTION</key>
--       <value class="java.lang.String">igs@hardy</value>
--     </entry>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_ID</key>
--       <value class="oracle.javatools.db.ReferenceID">
--         <name>CG$ADS_QMS_RULE_PROPERTIES</name>
--         <identifier class="java.math.BigDecimal">46704</identifier>
--         <schemaName>IGD_IGS</schemaName>
--         <type>TRIGGER</type>
--       </value>
--     </entry>
--   </properties>
--   <statementLevel>true</statementLevel>
-- </trigger>

CREATE OR REPLACE
TRIGGER cg$ads_qms_rule_properties
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
