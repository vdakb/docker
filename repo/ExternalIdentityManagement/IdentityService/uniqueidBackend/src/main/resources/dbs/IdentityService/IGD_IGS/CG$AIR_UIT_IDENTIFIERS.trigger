-- <?xml version = '1.0' encoding = 'UTF-8'?>
-- <trigger xmlns="http://xmlns.oracle.com/jdeveloper/1211/offlinedb">
--   <name>CG$AIR_UIT_IDENTIFIERS</name>
--   <enabled>true</enabled>
--   <properties>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_CONNECTION</key>
--       <value class="java.lang.String">igs@hardy</value>
--     </entry>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_ID</key>
--       <value class="oracle.javatools.db.ReferenceID">
--         <name>CG$AIR_UIT_IDENTIFIERS</name>
--         <identifier class="java.math.BigDecimal">47407</identifier>
--         <schemaName>IGD_IGS</schemaName>
--         <type>TRIGGER</type>
--       </value>
--     </entry>
--   </properties>
--   <statementLevel>true</statementLevel>
-- </trigger>

CREATE OR REPLACE
TRIGGER cg$air_uit_identifiers
AFTER INSERT ON uit_identifiers FOR EACH ROW
DECLARE
  cg$rec cg$uit_identifiers.cg$row_type;
  cg$ind cg$uit_identifiers.cg$ind_type;
BEGIN
  --  Application_logic Pre-After-Insert-row <<Start>>
  --  Application_logic Pre-After-Insert-row << End >>

  cg$rec.tnt_id       := :new.tnt_id;
  cg$ind.tnt_id       := TRUE;
  cg$rec.typ_id       := :new.typ_id;
  cg$ind.typ_id       := TRUE;
  cg$rec.ext_id       := :new.ext_id;
  cg$ind.ext_id       := TRUE;
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
  cg$rec.state        := :new.state;
  cg$ind.state        := TRUE;

  --  Application_logic Post-After-Insert-row <<Start>>
  --  Application_logic Post-After-Insert-row << End >>
END;
/
