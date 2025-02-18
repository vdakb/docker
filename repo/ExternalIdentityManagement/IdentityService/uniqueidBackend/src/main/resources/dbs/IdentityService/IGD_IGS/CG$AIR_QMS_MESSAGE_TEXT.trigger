-- <?xml version = '1.0' encoding = 'UTF-8'?>
-- <trigger xmlns="http://xmlns.oracle.com/jdeveloper/1211/offlinedb">
--   <name>CG$AIR_QMS_MESSAGE_TEXT</name>
--   <enabled>true</enabled>
--   <properties>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_CONNECTION</key>
--       <value class="java.lang.String">igs@hardy</value>
--     </entry>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_ID</key>
--       <value class="oracle.javatools.db.ReferenceID">
--         <name>CG$AIR_QMS_MESSAGE_TEXT</name>
--         <identifier class="java.math.BigDecimal">46688</identifier>
--         <schemaName>IGD_IGS</schemaName>
--         <type>TRIGGER</type>
--       </value>
--     </entry>
--   </properties>
--   <statementLevel>true</statementLevel>
-- </trigger>

CREATE OR REPLACE
TRIGGER cg$air_qms_message_text
AFTER INSERT ON qms_message_text FOR EACH ROW
DECLARE
  cg$rec qms$message_text.cg$row_type;
  cg$ind qms$message_text.cg$ind_type;
BEGIN
  --  Application_logic Pre-After-Insert-row <<Start>>
  --  Application_logic Pre-After-Insert-row << End >>

  cg$rec.code       := :new.code;
  cg$ind.code       := TRUE;
  cg$rec.language   := :new.language;
  cg$ind.language   := TRUE;
  cg$rec.text       := :new.text;
  cg$ind.text       := TRUE;
  cg$rec.help       := :new.help;
  cg$ind.help       := TRUE;
  cg$rec.created_by := :new.created_by;
  cg$ind.created_by := TRUE;
  cg$rec.created_on := :new.created_on;
  cg$ind.created_on := TRUE;
  cg$rec.updated_by := :new.updated_by;
  cg$ind.updated_by := TRUE;
  cg$rec.updated_on := :new.updated_on;
  cg$ind.updated_on := TRUE;

  --  Application_logic Post-After-Insert-row <<Start>>
  --  Application_logic Post-After-Insert-row << End >>
END;
/
