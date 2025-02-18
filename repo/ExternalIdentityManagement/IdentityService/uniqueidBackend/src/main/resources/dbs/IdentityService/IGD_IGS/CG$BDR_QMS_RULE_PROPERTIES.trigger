-- <?xml version = '1.0' encoding = 'UTF-8'?>
-- <trigger xmlns="http://xmlns.oracle.com/jdeveloper/1211/offlinedb">
--   <name>CG$BDR_QMS_RULE_PROPERTIES</name>
--   <enabled>true</enabled>
--   <properties>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_CONNECTION</key>
--       <value class="java.lang.String">igs@hardy</value>
--     </entry>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_ID</key>
--       <value class="oracle.javatools.db.ReferenceID">
--         <name>CG$BDR_QMS_RULE_PROPERTIES</name>
--         <identifier class="java.math.BigDecimal">46703</identifier>
--         <schemaName>IGD_IGS</schemaName>
--         <type>TRIGGER</type>
--       </value>
--     </entry>
--   </properties>
--   <statementLevel>true</statementLevel>
-- </trigger>

CREATE OR REPLACE
TRIGGER cg$bdr_qms_rule_properties
BEFORE DELETE ON qms_rule_properties FOR EACH ROW
DECLARE
  cg$pk qms$rule_properties.cg$pk_type;
  cg$rec qms$rule_properties.cg$row_type;
  cg$ind qms$rule_properties.cg$ind_type;
BEGIN
  --  Application_logic Pre-Before-Delete-row <<Start>>
  --  Application_logic Pre-Before-Delete-row << End >>

  --  Load cg$rec/cg$ind values from new
  cg$pk.process  := :old.process;
  cg$rec.process := :old.process;
  qms$rule_properties.cg$table(qms$rule_properties.idx).process := :old.process;
  cg$pk.code  := :old.code;
  cg$rec.code := :old.code;
  qms$rule_properties.cg$table(qms$rule_properties.idx).code := :old.code;

  qms$rule_properties.idx := qms$rule_properties.idx + 1;

  IF NOT (qms$rule_properties.called_from_package)
  THEN
    qms$rule_properties.del(cg$pk, FALSE);
    qms$rule_properties.called_from_package := FALSE;
  END IF;

  --  Application_logic Post-Before-Delete-row <<Start>>
  --  Application_logic Post-Before-Delete-row << End >>
END;
/
