-- <?xml version = '1.0' encoding = 'UTF-8'?>
-- <trigger xmlns="http://xmlns.oracle.com/jdeveloper/1211/offlinedb">
--   <name>CG$BIS_QMS_MESSAGE_TEXT</name>
--   <enabled>true</enabled>
--   <properties>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_CONNECTION</key>
--       <value class="java.lang.String">igs@hardy</value>
--     </entry>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_ID</key>
--       <value class="oracle.javatools.db.ReferenceID">
--         <name>CG$BIS_QMS_MESSAGE_TEXT</name>
--         <identifier class="java.math.BigDecimal">46686</identifier>
--         <schemaName>IGD_IGS</schemaName>
--         <type>TRIGGER</type>
--       </value>
--     </entry>
--   </properties>
--   <statementLevel>true</statementLevel>
-- </trigger>

CREATE OR REPLACE
TRIGGER cg$bis_qms_message_text
BEFORE INSERT ON qms_message_text
BEGIN
  --  Application_logic Pre-Before-Insert-statement <<Start>>
  --  Application_logic Pre-Before-Insert-statement << End >>

  qms$message_text.cg$table.DELETE;
  qms$message_text.cg$tableind.DELETE;
  qms$message_text.idx := 1;

  --  Application_logic Post-Before-Insert-statement <<Start>>
  --  Application_logic Post-Before-Insert-statement << End >>
END;
/
