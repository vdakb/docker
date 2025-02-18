-- <?xml version = '1.0' encoding = 'UTF-8'?>
-- <trigger xmlns="http://xmlns.oracle.com/jdeveloper/1211/offlinedb">
--   <name>CG$BDR_IGT_USERS</name>
--   <enabled>true</enabled>
--   <properties>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_CONNECTION</key>
--       <value class="java.lang.String">igs@hardy</value>
--     </entry>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_ID</key>
--       <value class="oracle.javatools.db.ReferenceID">
--         <name>CG$BDR_IGT_USERS</name>
--         <identifier class="java.math.BigDecimal">46733</identifier>
--         <schemaName>IGD_IGS</schemaName>
--         <type>TRIGGER</type>
--       </value>
--     </entry>
--   </properties>
--   <statementLevel>true</statementLevel>
-- </trigger>

CREATE OR REPLACE
TRIGGER cg$bdr_igt_users
BEFORE DELETE ON igt_users FOR EACH ROW
DECLARE
  cg$pk  cg$igt_users.cg$pk_type;
  cg$rec cg$igt_users.cg$row_type;
  cg$ind cg$igt_users.cg$ind_type;
BEGIN
  -- Application_logic Pre-Before-Delete-row <<Start>>
  -- Application_logic Pre-Before-Delete-row << End >>

  -- Load cg$rec/cg$ind values from old
  cg$pk.id  := :old.id;
  cg$rec.id := :old.id;
  cg$igt_users.cg$table(cg$igt_users.idx).id := :old.id;
  cg$rec.username := :old.username;
  cg$igt_users.cg$table(cg$igt_users.idx).username := :old.username;
  cg$rec.email := :old.email;
  cg$igt_users.cg$table(cg$igt_users.idx).email := :old.email;

  cg$igt_users.idx := cg$igt_users.idx + 1;
  IF NOT (cg$igt_users.called_from_package)
  THEN
    cg$igt_users.del(cg$pk, FALSE);
    cg$igt_users.called_from_package := FALSE;
  END IF;

  -- Application_logic Post-Before-Delete-row <<Start>>
  -- Application_logic Post-Before-Delete-row << End >>
END;
/
