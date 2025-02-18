-- <?xml version = '1.0' encoding = 'UTF-8'?>
-- <trigger xmlns="http://xmlns.oracle.com/jdeveloper/1211/offlinedb">
--   <name>CG$BUR_QMS_RULE_PROPERTIES</name>
--   <enabled>true</enabled>
--   <properties>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_CONNECTION</key>
--       <value class="java.lang.String">igs@hardy</value>
--     </entry>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_ID</key>
--       <value class="oracle.javatools.db.ReferenceID">
--         <name>CG$BUR_QMS_RULE_PROPERTIES</name>
--         <identifier class="java.math.BigDecimal">46700</identifier>
--         <schemaName>IGD_IGS</schemaName>
--         <type>TRIGGER</type>
--       </value>
--     </entry>
--   </properties>
--   <statementLevel>true</statementLevel>
-- </trigger>

CREATE OR REPLACE
TRIGGER cg$bur_qms_rule_properties
BEFORE UPDATE ON qms_rule_properties FOR EACH ROW
DECLARE
  cg$rec     qms$rule_properties.cg$row_type;
  cg$ind     qms$rule_properties.cg$ind_type;
  cg$old_rec qms$rule_properties.cg$row_type;
BEGIN
  --  Application_logic Pre-Before-Update-row <<Start>>
  --  Application_logic Pre-Before-Update-row << End >>

  --  Load cg$rec/cg$ind values from new
  cg$rec.process := :new.process;
  cg$ind.process := (:new.process IS NULL AND :old.process IS NOT NULL )
                 OR (:new.process IS NOT NULL AND :old.process IS NULL)
                 OR NOT(:new.process = :old.process) ;
  qms$rule_properties.cg$table(qms$rule_properties.idx).process := :old.process;
  cg$rec.code := :new.code;
  cg$ind.code := (:new.code IS NULL AND :old.code IS NOT NULL )
              OR (:new.code IS NOT NULL AND :old.code IS NULL)
              OR NOT(:new.code = :old.code) ;
  qms$rule_properties.cg$table(qms$rule_properties.idx).code := :old.code;
  cg$rec.name := :new.name;
  cg$ind.name := (:new.name IS NULL AND :old.name IS NOT NULL )
              OR (:new.name IS NOT NULL AND :old.name IS NULL)
              OR NOT(:new.name = :old.name) ;
  qms$rule_properties.cg$table(qms$rule_properties.idx).name := :old.name;
  cg$rec.enabled := :new.enabled;
  cg$ind.enabled := (:new.enabled IS NULL AND :old.enabled IS NOT NULL )
                 OR (:new.enabled IS NOT NULL AND :old.enabled IS NULL)
                 OR NOT(:new.enabled = :old.enabled) ;
  qms$rule_properties.cg$table(qms$rule_properties.idx).enabled := :old.enabled;
  cg$rec.remark := :new.remark;
  cg$ind.remark := (:new.remark IS NULL AND :old.remark IS NOT NULL )
                OR (:new.remark IS NOT NULL AND :old.remark IS NULL)
                OR NOT(:new.remark = :old.remark) ;
  qms$rule_properties.cg$table(qms$rule_properties.idx).remark := :old.remark;
  cg$rec.created_by := :new.created_by;
  cg$ind.created_by := (:new.created_by IS NULL AND :old.created_by IS NOT NULL )
                    OR (:new.created_by IS NOT NULL AND :old.created_by IS NULL)
                    OR NOT(:new.created_by = :old.created_by) ;
  qms$rule_properties.cg$table(qms$rule_properties.idx).created_by := :old.created_by;
  cg$rec.created_on := :new.created_on;
  cg$ind.created_on := (:new.created_on IS NULL AND :old.created_on IS NOT NULL )
                    OR (:new.created_on IS NOT NULL AND :old.created_on IS NULL)
                    OR NOT(:new.created_on = :old.created_on) ;
  qms$rule_properties.cg$table(qms$rule_properties.idx).created_on := :old.created_on;
  cg$rec.updated_by := :new.updated_by;
  cg$ind.updated_by := (:new.updated_by IS NULL AND :old.updated_by IS NOT NULL )
                    OR (:new.updated_by IS NOT NULL AND :old.updated_by IS NULL)
                    OR NOT(:new.updated_by = :old.updated_by) ;
  qms$rule_properties.cg$table(qms$rule_properties.idx).updated_by := :old.updated_by;
  cg$rec.updated_on := :new.updated_on;
  cg$ind.updated_on := (:new.updated_on IS NULL AND :old.updated_on IS NOT NULL )
                    OR (:new.updated_on IS NOT NULL AND :old.updated_on IS NULL)
                    OR NOT(:new.updated_on = :old.updated_on) ;
  qms$rule_properties.cg$table(qms$rule_properties.idx).updated_on := :old.updated_on;

  qms$rule_properties.idx := qms$rule_properties.idx + 1;

  IF NOT (qms$rule_properties.called_from_package)
  THEN
    qms$rule_properties.upd(cg$rec, cg$ind, FALSE);
    qms$rule_properties.called_from_package := FALSE;
  END IF;

  :new.name       := cg$rec.name;
  :new.enabled    := cg$rec.enabled;
  :new.remark     := cg$rec.remark;
  :new.created_by := cg$rec.created_by;
  :new.created_on := cg$rec.created_on;
  :new.updated_by := cg$rec.updated_by;
  :new.updated_on := cg$rec.updated_on;

  --  Application_logic Post-Before-Update-row <<Start>>
  --  Application_logic Post-Before-Update-row << End >>
END;
/
