-- <?xml version = '1.0' encoding = 'UTF-8'?>
-- <trigger xmlns="http://xmlns.oracle.com/jdeveloper/1211/offlinedb">
--   <name>CG$ADS_IGT_USERS</name>
--   <enabled>true</enabled>
--   <properties>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_CONNECTION</key>
--       <value class="java.lang.String">igs@hardy</value>
--     </entry>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_ID</key>
--       <value class="oracle.javatools.db.ReferenceID">
--         <name>CG$ADS_IGT_USERS</name>
--         <identifier class="java.math.BigDecimal">46734</identifier>
--         <schemaName>IGD_IGS</schemaName>
--         <type>TRIGGER</type>
--       </value>
--     </entry>
--   </properties>
--   <statementLevel>true</statementLevel>
-- </trigger>

CREATE OR REPLACE
TRIGGER cg$ads_igt_users
AFTER DELETE ON igt_users
DECLARE
  idx        BINARY_INTEGER := cg$igt_users.cg$table.FIRST;
  cg$rec     cg$igt_users.cg$row_type;
  cg$old_rec cg$igt_users.cg$row_type;
BEGIN
  -- Application_logic Pre-After-Delete-statement <<Start>>
  -- Application_logic Pre-After-Delete-statement << End >>

  IF NOT (cg$igt_users.called_from_package)
  THEN
    WHILE idx IS NOT NULL
    LOOP
      cg$rec.id := cg$igt_users.cg$table(idx).id;
      cg$igt_users.cg$tableind(idx).id := TRUE;

      cg$rec.rowversion := cg$igt_users.cg$table(idx).rowversion;
      cg$igt_users.cg$tableind(idx).rowversion := TRUE;

      cg$rec.created_by := cg$igt_users.cg$table(idx).created_by;
      cg$igt_users.cg$tableind(idx).created_by := TRUE;

      cg$rec.created_on := cg$igt_users.cg$table(idx).created_on;
      cg$igt_users.cg$tableind(idx).created_on := TRUE;

      cg$rec.updated_by := cg$igt_users.cg$table(idx).updated_by;
      cg$igt_users.cg$tableind(idx).updated_by := TRUE;

      cg$rec.updated_on := cg$igt_users.cg$table(idx).updated_on;
      cg$igt_users.cg$tableind(idx).updated_on := TRUE;

      cg$rec.active := cg$igt_users.cg$table(idx).active;
      cg$igt_users.cg$tableind(idx).active := TRUE;

      cg$rec.username := cg$igt_users.cg$table(idx).username;
      cg$igt_users.cg$tableind(idx).username := TRUE;

      cg$rec.lastname := cg$igt_users.cg$table(idx).lastname;
      cg$igt_users.cg$tableind(idx).lastname := TRUE;

      cg$rec.firstname := cg$igt_users.cg$table(idx).firstname;
      cg$igt_users.cg$tableind(idx).firstname := TRUE;

      cg$rec.language := cg$igt_users.cg$table(idx).language;
      cg$igt_users.cg$tableind(idx).language := TRUE;

      cg$rec.email := cg$igt_users.cg$table(idx).email;
      cg$igt_users.cg$tableind(idx).email := TRUE;

      cg$rec.phone := cg$igt_users.cg$table(idx).phone;
      cg$igt_users.cg$tableind(idx).phone := TRUE;

      cg$rec.mobile := cg$igt_users.cg$table(idx).mobile;
      cg$igt_users.cg$tableind(idx).mobile := TRUE;

      cg$igt_users.validate_foreign_keys_del(cg$rec);
      cg$igt_users.cascade_delete(cg$rec);

      idx := cg$igt_users.cg$table.NEXT(idx);
    END LOOP;
  END IF;

  -- Application_logic Post-After-Delete-statement <<Start>>
  -- Application_logic Post-After-Delete-statement << End >>
END;
/
