-- <?xml version = '1.0' encoding = 'UTF-8'?>
-- <trigger xmlns="http://xmlns.oracle.com/jdeveloper/1211/offlinedb">
--   <name>CG$BIR_QMS_RULE_PROPERTIES</name>
--   <enabled>true</enabled>
--   <properties>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_CONNECTION</key>
--       <value class="java.lang.String">igs@hardy</value>
--     </entry>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_ID</key>
--       <value class="oracle.javatools.db.ReferenceID">
--         <name>CG$BIR_QMS_RULE_PROPERTIES</name>
--         <identifier class="java.math.BigDecimal">46697</identifier>
--         <schemaName>IGD_IGS</schemaName>
--         <type>TRIGGER</type>
--       </value>
--     </entry>
--   </properties>
--   <statementLevel>true</statementLevel>
-- </trigger>

CREATE OR REPLACE
TRIGGER cg$bir_qms_rule_properties
BEFORE INSERT ON qms_rule_properties FOR EACH ROW
DECLARE
  cg$rec qms$rule_properties.cg$row_type;
  cg$ind qms$rule_properties.cg$ind_type;
BEGIN
  --  Application_logic Pre-Before-Insert-row <<Start>>
  --  Application_logic Pre-Before-Insert-row << End >>

  --  Load cg$rec/cg$ind values from new
  cg$rec.process := :new.process;
  cg$ind.process := TRUE;
  cg$rec.code    := :new.code;
  cg$ind.code    := TRUE;
  cg$rec.name    := :new.name;
  cg$ind.name    := TRUE;
  cg$rec.enabled := :new.enabled;
  cg$ind.enabled := TRUE;
  cg$rec.remark  := :new.remark;
  cg$ind.remark  := TRUE;
  cg$rec.created_by := :new.created_by;
  IF (:new.created_by IS NULL)
  THEN
    cg$ind.created_by := FALSE;
  ELSE
    cg$ind.created_by := TRUE;
  END IF;
  cg$rec.created_on := :new.created_on;
  IF (:new.created_on IS NULL)
  THEN
    cg$ind.created_on := FALSE;
  ELSE
    cg$ind.created_on := TRUE;
  END IF;
  cg$rec.updated_by := :new.updated_by;
  IF (:new.updated_by IS NULL)
  THEN
    cg$ind.updated_by := FALSE;
  ELSE
    cg$ind.updated_by := TRUE;
  END IF;
  cg$rec.updated_on := :new.updated_on;
  IF (:new.updated_on IS NULL)
  THEN
    cg$ind.updated_on := FALSE;
  ELSE
    cg$ind.updated_on := TRUE;
  END IF;

  IF NOT (qms$rule_properties.called_from_package)
  THEN
    qms$rule_properties.ins(cg$rec, cg$ind, FALSE);
    qms$rule_properties.called_from_package := FALSE;
  END IF;

  qms$rule_properties.cg$table(qms$rule_properties.idx).process := cg$rec.process;
  qms$rule_properties.cg$tableind(qms$rule_properties.idx).process := cg$ind.process;

  qms$rule_properties.cg$table(qms$rule_properties.idx).code := cg$rec.code;
  qms$rule_properties.cg$tableind(qms$rule_properties.idx).code := cg$ind.code;

  qms$rule_properties.cg$table(qms$rule_properties.idx).name := cg$rec.name;
  qms$rule_properties.cg$tableind(qms$rule_properties.idx).name := cg$ind.name;

  qms$rule_properties.cg$table(qms$rule_properties.idx).enabled := cg$rec.enabled;
  qms$rule_properties.cg$tableind(qms$rule_properties.idx).enabled := cg$ind.enabled;

  qms$rule_properties.cg$table(qms$rule_properties.idx).remark := cg$rec.remark;
  qms$rule_properties.cg$tableind(qms$rule_properties.idx).remark := cg$ind.remark;

  qms$rule_properties.cg$table(qms$rule_properties.idx).created_by := cg$rec.created_by;
  qms$rule_properties.cg$tableind(qms$rule_properties.idx).created_by := cg$ind.created_by;

  qms$rule_properties.cg$table(qms$rule_properties.idx).created_on := cg$rec.created_on;
  qms$rule_properties.cg$tableind(qms$rule_properties.idx).created_on := cg$ind.created_on;

  qms$rule_properties.cg$table(qms$rule_properties.idx).updated_by := cg$rec.updated_by;
  qms$rule_properties.cg$tableind(qms$rule_properties.idx).updated_by := cg$ind.updated_by;

  qms$rule_properties.cg$table(qms$rule_properties.idx).updated_on := cg$rec.updated_on;
  qms$rule_properties.cg$tableind(qms$rule_properties.idx).updated_on := cg$ind.updated_on;

  qms$rule_properties.idx := qms$rule_properties.idx + 1;

  :new.process    := cg$rec.process;
  :new.code       := cg$rec.code;
  :new.name       := cg$rec.name;
  :new.enabled    := cg$rec.enabled;
  :new.remark     := cg$rec.remark;
  :new.created_by := cg$rec.created_by;
  :new.created_on := cg$rec.created_on;
  :new.updated_by := cg$rec.updated_by;
  :new.updated_on := cg$rec.updated_on;

  --  Application_logic Post-Before-Insert-row <<Start>>
  --  Application_logic Post-Before-Insert-row << End >>
END;
/
