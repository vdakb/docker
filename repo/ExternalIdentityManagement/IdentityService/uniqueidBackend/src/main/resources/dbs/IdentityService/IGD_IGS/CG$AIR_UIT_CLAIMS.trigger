-- <?xml version = '1.0' encoding = 'UTF-8'?>
-- <trigger xmlns="http://xmlns.oracle.com/jdeveloper/1211/offlinedb">
--   <name>CG$AIR_UIT_CLAIMS</name>
--   <enabled>true</enabled>
--   <properties>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_CONNECTION</key>
--       <value class="java.lang.String">igs@hardy</value>
--     </entry>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_ID</key>
--       <value class="oracle.javatools.db.ReferenceID">
--         <name>CG$AIR_UIT_CLAIMS</name>
--         <identifier class="java.math.BigDecimal">47374</identifier>
--         <schemaName>IGD_IGS</schemaName>
--         <type>TRIGGER</type>
--       </value>
--     </entry>
--   </properties>
--   <statementLevel>true</statementLevel>
-- </trigger>

CREATE OR REPLACE
TRIGGER cg$air_uit_claims
AFTER INSERT ON uit_claims FOR EACH ROW
DECLARE
  cg$rec cg$uit_claims.cg$row_type;
  cg$ind cg$uit_claims.cg$ind_type;
BEGIN
  --  Application_logic Pre-After-Insert-row <<Start>>
  --  Application_logic Pre-After-Insert-row << End >>

  cg$rec.tnt_id       := :new.tnt_id;
  cg$ind.tnt_id       := TRUE;
  cg$rec.rol_id       := :new.rol_id;
  cg$ind.rol_id       := TRUE;
  cg$rec.usr_id       := :new.usr_id;
  cg$ind.usr_id       := TRUE;
  cg$rec.rowversion   := :new.created_by;
  cg$ind.rowversion   := TRUE;
  cg$rec.created_by   := :new.created_by;
  cg$ind.created_by   := TRUE;
  cg$rec.created_on   := :new.created_on;
  cg$ind.created_on   := TRUE;
  cg$rec.updated_by   := :new.updated_by;
  cg$ind.updated_by   := TRUE;
  cg$rec.updated_on   := :new.updated_on;
  cg$ind.updated_on   := TRUE;

  --  Application_logic Post-After-Insert-row <<Start>>
  --  Application_logic Post-After-Insert-row << End >>
END;
/
