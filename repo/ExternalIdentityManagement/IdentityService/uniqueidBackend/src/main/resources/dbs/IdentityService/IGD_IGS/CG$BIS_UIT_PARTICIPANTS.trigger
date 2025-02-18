-- <?xml version = '1.0' encoding = 'UTF-8'?>
-- <trigger xmlns="http://xmlns.oracle.com/jdeveloper/1211/offlinedb">
--   <name>CG$BIS_UIT_PARTICIPANTS</name>
--   <enabled>true</enabled>
--   <properties>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_CONNECTION</key>
--       <value class="java.lang.String">igs@hardy</value>
--     </entry>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_ID</key>
--       <value class="oracle.javatools.db.ReferenceID">
--         <name>CG$BIS_UIT_PARTICIPANTS</name>
--         <identifier class="java.math.BigDecimal">46810</identifier>
--         <schemaName>IGD_IGS</schemaName>
--         <type>TRIGGER</type>
--       </value>
--     </entry>
--   </properties>
--   <statementLevel>true</statementLevel>
-- </trigger>

CREATE OR REPLACE
TRIGGER cg$bis_uit_participants
BEFORE INSERT ON uit_participants
BEGIN
  -- Application_logic Pre-Before-Insert-statement <<Start>>
  -- Application_logic Pre-Before-Insert-statement << End >>

  cg$uit_participants.cg$table.DELETE;
  cg$uit_participants.cg$tableind.DELETE;
  cg$uit_participants.idx := 1;

  -- Application_logic Post-Before-Insert-statement <<Start>>
  -- Application_logic Post-Before-Insert-statement << End >>
END;
/
