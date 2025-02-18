-- <?xml version = '1.0' encoding = 'UTF-8'?>
-- <trigger xmlns="http://xmlns.oracle.com/jdeveloper/1211/offlinedb">
--   <name>CG$BDR_QMS_MESSAGE_PROPERTIES</name>
--   <enabled>true</enabled>
--   <properties>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_CONNECTION</key>
--       <value class="java.lang.String">igs@hardy</value>
--     </entry>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_ID</key>
--       <value class="oracle.javatools.db.ReferenceID">
--         <name>CG$BDR_QMS_MESSAGE_PROPERTIES</name>
--         <identifier class="java.math.BigDecimal">46684</identifier>
--         <schemaName>IGD_IGS</schemaName>
--         <type>TRIGGER</type>
--       </value>
--     </entry>
--   </properties>
--   <statementLevel>true</statementLevel>
-- </trigger>

CREATE OR REPLACE
TRIGGER cg$bdr_qms_message_properties
BEFORE DELETE ON qms_message_properties FOR EACH ROW
DECLARE
  cg$pk  qms$message_properties.cg$pk_type;
  cg$rec qms$message_properties.cg$row_type;
  cg$ind qms$message_properties.cg$ind_type;
BEGIN
  --  Application_logic Pre-Before-Delete-row <<Start>>
  --  Application_logic Pre-Before-Delete-row << End >>

  --  Load cg$rec/cg$ind values from new

  cg$pk.code  := :old.code;
  cg$rec.code := :old.code;
  qms$message_properties.cg$table(qms$message_properties.idx).code := cg$pk.code;

  qms$message_properties.idx := qms$message_properties.idx + 1;

  IF NOT (qms$message_properties.called_from_package)
  THEN
    qms$message_properties.del(cg$pk, FALSE);
    qms$message_properties.called_from_package := FALSE;
 END IF;

  --  Application_logic Post-Before-Delete-row <<Start>>
  --  Application_logic Post-Before-Delete-row << End >>
END;
/
