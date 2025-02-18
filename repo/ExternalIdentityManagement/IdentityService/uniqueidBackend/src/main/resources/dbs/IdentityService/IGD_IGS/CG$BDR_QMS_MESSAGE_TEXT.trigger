-- <?xml version = '1.0' encoding = 'UTF-8'?>
-- <trigger xmlns="http://xmlns.oracle.com/jdeveloper/1211/offlinedb">
--   <name>CG$BDR_QMS_MESSAGE_TEXT</name>
--   <enabled>true</enabled>
--   <properties>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_CONNECTION</key>
--       <value class="java.lang.String">igs@hardy</value>
--     </entry>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_ID</key>
--       <value class="oracle.javatools.db.ReferenceID">
--         <name>CG$BDR_QMS_MESSAGE_TEXT</name>
--         <identifier class="java.math.BigDecimal">46694</identifier>
--         <schemaName>IGD_IGS</schemaName>
--         <type>TRIGGER</type>
--       </value>
--     </entry>
--   </properties>
--   <statementLevel>true</statementLevel>
-- </trigger>

CREATE OR REPLACE
TRIGGER cg$bdr_qms_message_text
BEFORE DELETE ON qms_message_text FOR EACH ROW
DECLARE
  cg$pk  qms$message_text.cg$pk_type;
  cg$rec qms$message_text.cg$row_type;
  cg$ind qms$message_text.cg$ind_type;
BEGIN
  --  Application_logic Pre-Before-Delete-row <<Start>>
  --  Application_logic Pre-Before-Delete-row << End >>

  --  Load cg$rec/cg$ind values from new
  cg$pk.code  := :old.code;
  cg$rec.code := :old.code;
  qms$message_text.cg$table(qms$message_text.idx).code := cg$pk.code;

  cg$pk.language  := :old.language;
  cg$rec.language := :old.language;
  qms$message_text.cg$table(qms$message_text.idx).language := cg$pk.language;

  cg$rec.code := :old.code;
  qms$message_text.cg$table(qms$message_text.idx).code := cg$rec.code;

  qms$message_text.idx := qms$message_text.idx + 1;

  IF NOT (qms$message_text.called_from_package)
  THEN
    qms$message_text.del(cg$pk, FALSE);
    qms$message_text.called_from_package := FALSE;
  END IF;

  --  Application_logic Post-Before-Delete-row <<Start>>
  --  Application_logic Post-Before-Delete-row << End >>
END;
/
