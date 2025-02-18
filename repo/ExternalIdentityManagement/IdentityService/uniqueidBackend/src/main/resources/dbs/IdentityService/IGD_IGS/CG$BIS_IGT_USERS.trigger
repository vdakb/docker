-- <?xml version = '1.0' encoding = 'UTF-8'?>
-- <trigger xmlns="http://xmlns.oracle.com/jdeveloper/1211/offlinedb">
--   <name>CG$BIS_IGT_USERS</name>
--   <enabled>true</enabled>
--   <properties>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_CONNECTION</key>
--       <value class="java.lang.String">igs@hardy</value>
--     </entry>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_ID</key>
--       <value class="oracle.javatools.db.ReferenceID">
--         <name>CG$BIS_IGT_USERS</name>
--         <identifier class="java.math.BigDecimal">46726</identifier>
--         <schemaName>IGD_IGS</schemaName>
--         <type>TRIGGER</type>
--       </value>
--     </entry>
--   </properties>
--   <statementLevel>true</statementLevel>
-- </trigger>

CREATE OR REPLACE
TRIGGER cg$bis_igt_users
BEFORE INSERT ON igt_users
BEGIN
  -- Application_logic Pre-Before-Insert-statement <<Start>>
  -- Application_logic Pre-Before-Insert-statement << End >>

  cg$igt_users.cg$table.DELETE;
  cg$igt_users.cg$tableind.DELETE;
  cg$igt_users.idx := 1;

  -- Application_logic Post-Before-Insert-statement <<Start>>
  -- Application_logic Post-Before-Insert-statement << End >>
END;
/
