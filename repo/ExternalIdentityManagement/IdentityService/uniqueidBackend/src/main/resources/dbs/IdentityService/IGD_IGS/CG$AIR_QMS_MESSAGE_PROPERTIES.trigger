-- <?xml version = '1.0' encoding = 'UTF-8'?>
-- <trigger xmlns="http://xmlns.oracle.com/jdeveloper/1211/offlinedb">
--   <name>CG$AIR_QMS_MESSAGE_PROPERTIES</name>
--   <enabled>true</enabled>
--   <properties>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_CONNECTION</key>
--       <value class="java.lang.String">igs@hardy</value>
--     </entry>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_ID</key>
--       <value class="oracle.javatools.db.ReferenceID">
--         <name>CG$AIR_QMS_MESSAGE_PROPERTIES</name>
--         <identifier class="java.math.BigDecimal">46678</identifier>
--         <schemaName>IGD_IGS</schemaName>
--         <type>TRIGGER</type>
--       </value>
--     </entry>
--   </properties>
--   <statementLevel>true</statementLevel>
-- </trigger>

CREATE OR REPLACE
TRIGGER cg$air_qms_message_properties
AFTER INSERT ON qms_message_properties FOR EACH ROW
DECLARE
  cg$rec qms$message_properties.cg$row_type;
  cg$ind qms$message_properties.cg$ind_type;
BEGIN
  --  Application_logic Pre-After-Insert-row <<Start>>
  --  Application_logic Pre-After-Insert-row << End >>

  cg$rec.code             := :new.code;
  cg$ind.code             := TRUE;
  cg$rec.description      := :new.description;
  cg$ind.description      := TRUE;
  cg$rec.severity         := :new.severity;
  cg$ind.severity         := TRUE;
  cg$rec.logging          := :new.logging;
  cg$ind.logging          := TRUE;
  cg$rec.suppress_warning := :new.suppress_warning;
  cg$ind.suppress_warning := TRUE;
  cg$rec.suppress_always  := :new.suppress_always;
  cg$ind.suppress_always  := TRUE;
  cg$rec.constraint_name  := :new.constraint_name;
  cg$ind.constraint_name  := TRUE;
  cg$rec.created_by       := :new.created_by;
  cg$ind.created_by       := TRUE;
  cg$rec.created_on       := :new.created_on;
  cg$ind.created_on       := TRUE;
  cg$rec.updated_by       := :new.updated_by;
  cg$ind.updated_by       := TRUE;
  cg$rec.updated_on       := :new.updated_on;
  cg$ind.updated_on       := TRUE;

  --  Application_logic Post-After-Insert-row <<Start>>
  --  Application_logic Post-After-Insert-row << End >>
END;
/
