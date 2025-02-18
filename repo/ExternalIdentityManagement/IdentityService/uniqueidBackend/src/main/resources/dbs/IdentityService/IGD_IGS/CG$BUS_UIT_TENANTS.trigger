-- <?xml version = '1.0' encoding = 'UTF-8'?>
-- <trigger xmlns="http://xmlns.oracle.com/jdeveloper/1211/offlinedb">
--   <name>CG$BUS_UIT_TENANTS</name>
--   <enabled>true</enabled>
--   <properties>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_CONNECTION</key>
--       <value class="java.lang.String">igs@hardy</value>
--     </entry>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_ID</key>
--       <value class="oracle.javatools.db.ReferenceID">
--         <name>CG$BUS_UIT_TENANTS</name>
--         <identifier class="java.math.BigDecimal">46831</identifier>
--         <schemaName>IGD_IGS</schemaName>
--         <type>TRIGGER</type>
--       </value>
--     </entry>
--   </properties>
--   <statementLevel>true</statementLevel>
-- </trigger>

CREATE OR REPLACE
TRIGGER cg$bus_uit_tenants
BEFORE UPDATE ON uit_tenants
BEGIN
  -- Application_logic Pre-Before-Update-statement <<Start>>
  -- Application_logic Pre-Before-Update-statement << End >>

  cg$uit_tenants.cg$table.DELETE;
  cg$uit_tenants.cg$tableind.DELETE;
  cg$uit_tenants.idx := 1;

  -- Application_logic Post-Before-Update-statement <<Start>>
  -- Application_logic Post-Before-Update-statement << End >>
END;
/
