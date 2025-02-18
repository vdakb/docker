-- <?xml version = '1.0' encoding = 'UTF-8'?>
-- <trigger xmlns="http://xmlns.oracle.com/jdeveloper/1211/offlinedb">
--   <name>CG$AIR_IGT_USERS</name>
--   <enabled>true</enabled>
--   <properties>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_CONNECTION</key>
--       <value class="java.lang.String">igs@hardy</value>
--     </entry>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_ID</key>
--       <value class="oracle.javatools.db.ReferenceID">
--         <name>CG$AIR_IGT_USERS</name>
--         <identifier class="java.math.BigDecimal">47319</identifier>
--         <schemaName>IGD_IGS</schemaName>
--         <type>TRIGGER</type>
--       </value>
--     </entry>
--   </properties>
--   <statementLevel>true</statementLevel>
-- </trigger>

CREATE OR REPLACE
TRIGGER cg$air_igt_users
AFTER INSERT ON igt_users FOR EACH ROW
DECLARE
  cg$rec cg$igt_users.cg$row_type;
  cg$ind cg$igt_users.cg$ind_type;
BEGIN
  --  Application_logic Pre-After-Insert-row <<Start>>
  --  Application_logic Pre-After-Insert-row << End >>

  cg$rec.id           := :new.id;
  cg$ind.id           := TRUE;
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
  cg$rec.active       := :new.active;
  cg$ind.active       := TRUE;
  cg$rec.username     := :new.username;
  cg$ind.username     := TRUE;
  cg$rec.firstname    := :new.firstname;
  cg$ind.firstname    := TRUE;
  cg$rec.language     := :new.language;
  cg$ind.language     := TRUE;
  cg$rec.email        := :new.email;
  cg$ind.email        := TRUE;
  cg$rec.phone        := :new.phone;
  cg$ind.phone        := TRUE;
  cg$rec.mobile       := :new.mobile;
  cg$ind.mobile       := TRUE;

  --  Application_logic Post-After-Insert-row <<Start>>
  --  Application_logic Post-After-Insert-row << End >>
END;
/
