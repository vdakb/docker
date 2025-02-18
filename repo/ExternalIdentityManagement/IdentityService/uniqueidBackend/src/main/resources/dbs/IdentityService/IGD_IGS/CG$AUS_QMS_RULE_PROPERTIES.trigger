-- <?xml version = '1.0' encoding = 'UTF-8'?>
-- <trigger xmlns="http://xmlns.oracle.com/jdeveloper/1211/offlinedb">
--   <name>CG$AUS_QMS_RULE_PROPERTIES</name>
--   <enabled>true</enabled>
--   <properties>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_CONNECTION</key>
--       <value class="java.lang.String">igs@hardy</value>
--     </entry>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_ID</key>
--       <value class="oracle.javatools.db.ReferenceID">
--         <name>CG$AUS_QMS_RULE_PROPERTIES</name>
--         <identifier class="java.math.BigDecimal">46701</identifier>
--         <schemaName>IGD_IGS</schemaName>
--         <type>TRIGGER</type>
--       </value>
--     </entry>
--   </properties>
--   <statementLevel>true</statementLevel>
-- </trigger>

CREATE OR REPLACE
TRIGGER cg$aus_qms_rule_properties
AFTER UPDATE ON qms_rule_properties
DECLARE
  idx        BINARY_INTEGER := qms$rule_properties.cg$table.FIRST;
  cg$old_rec qms$rule_properties.cg$row_type;
  cg$rec     qms$rule_properties.cg$row_type;
  cg$ind     qms$rule_properties.cg$ind_type;
BEGIN
  --  Application_logic Pre-After-Update-statement <<Start>>
  --  Application_logic Pre-After-Update-statement << End >>

  WHILE idx IS NOT NULL
  LOOP
    cg$old_rec.process    := qms$rule_properties.cg$table(idx).process;
    cg$old_rec.code       := qms$rule_properties.cg$table(idx).code;
    cg$old_rec.name       := qms$rule_properties.cg$table(idx).name;
    cg$old_rec.enabled    := qms$rule_properties.cg$table(idx).enabled;
    cg$old_rec.remark     := qms$rule_properties.cg$table(idx).remark;
    cg$old_rec.created_by := qms$rule_properties.cg$table(idx).created_by;
    cg$old_rec.created_on := qms$rule_properties.cg$table(idx).created_on;
    cg$old_rec.updated_by := qms$rule_properties.cg$table(idx).updated_by;
    cg$old_rec.updated_on := qms$rule_properties.cg$table(idx).updated_on;

    IF NOT (qms$rule_properties.called_from_package)
    THEN
      idx := qms$rule_properties.cg$table.NEXT(idx);
      cg$rec.process    := qms$rule_properties.cg$table(idx).process;
      cg$ind.process    := updating('process');
      cg$rec.code       := qms$rule_properties.cg$table(idx).code;
      cg$ind.code       := updating('code');
      cg$rec.name       := qms$rule_properties.cg$table(idx).name;
      cg$ind.name       := updating('name');
      cg$rec.enabled    := qms$rule_properties.cg$table(idx).enabled;
      cg$ind.enabled    := updating('enabled');
      cg$rec.remark     := qms$rule_properties.cg$table(idx).remark;
      cg$ind.remark     := updating('remark');
      cg$rec.created_by := qms$rule_properties.cg$table(idx).created_by;
      cg$ind.created_by := updating('created_by');
      cg$rec.created_on := qms$rule_properties.cg$table(idx).created_on;
      cg$ind.created_on := updating('created_on');
      cg$rec.updated_by := qms$rule_properties.cg$table(idx).updated_by;
      cg$ind.updated_by := updating('updated_by');
      cg$rec.updated_on := qms$rule_properties.cg$table(idx).updated_on;
      cg$ind.updated_on := updating('updated_on');

      qms$rule_properties.validate_foreign_keys_upd(cg$rec, cg$old_rec, cg$ind);
      qms$rule_properties.cascade_update(cg$rec, cg$old_rec);

      qms$rule_properties.called_from_package := FALSE;
    END IF;
    idx := qms$rule_properties.cg$table.NEXT(idx);
  END LOOP;
  qms$rule_properties.cg$table.DELETE;

  --  Application_logic Post-After-Update-statement <<Start>>
  --  Application_logic Post-After-Update-statement << End >>
END;
/
