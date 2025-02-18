-- <?xml version = '1.0' encoding = 'UTF-8'?>
-- <trigger xmlns="http://xmlns.oracle.com/jdeveloper/1211/offlinedb">
--   <name>CG$ADS_IGT_ROLES</name>
--   <enabled>true</enabled>
--   <properties>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_CONNECTION</key>
--       <value class="java.lang.String">igs@hardy</value>
--     </entry>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_ID</key>
--       <value class="oracle.javatools.db.ReferenceID">
--         <name>CG$ADS_IGT_ROLES</name>
--         <identifier class="java.math.BigDecimal">46725</identifier>
--         <schemaName>IGD_IGS</schemaName>
--         <type>TRIGGER</type>
--       </value>
--     </entry>
--   </properties>
--   <statementLevel>true</statementLevel>
-- </trigger>

CREATE OR REPLACE
TRIGGER cg$ads_igt_roles
AFTER DELETE ON igt_roles
DECLARE
  idx        BINARY_INTEGER := cg$igt_roles.cg$table.FIRST;
  cg$rec     cg$igt_roles.cg$row_type;
  cg$old_rec cg$igt_roles.cg$row_type;
BEGIN
  -- Application_logic Pre-After-Delete-statement <<Start>>
  -- Application_logic Pre-After-Delete-statement << End >>

  IF NOT (cg$igt_roles.called_from_package)
  THEN
    WHILE idx IS NOT NULL
    LOOP
      cg$rec.id := cg$igt_roles.cg$table(idx).id;
      cg$igt_roles.cg$tableind(idx).id := TRUE;

      cg$rec.rowversion := cg$igt_roles.cg$table(idx).rowversion;
      cg$igt_roles.cg$tableind(idx).rowversion := TRUE;

      cg$rec.created_by := cg$igt_roles.cg$table(idx).created_by;
      cg$igt_roles.cg$tableind(idx).created_by := TRUE;

      cg$rec.created_on := cg$igt_roles.cg$table(idx).created_on;
      cg$igt_roles.cg$tableind(idx).created_on := TRUE;

      cg$rec.updated_by := cg$igt_roles.cg$table(idx).updated_by;
      cg$igt_roles.cg$tableind(idx).updated_by := TRUE;

      cg$rec.updated_on := cg$igt_roles.cg$table(idx).updated_on;
      cg$igt_roles.cg$tableind(idx).updated_on := TRUE;

      cg$rec.active := cg$igt_roles.cg$table(idx).active;
      cg$igt_roles.cg$tableind(idx).active := TRUE;

      cg$rec.display_name := cg$igt_roles.cg$table(idx).display_name;
      cg$igt_roles.cg$tableind(idx).display_name := TRUE;

      cg$rec.description := cg$igt_roles.cg$table(idx).description;
      cg$igt_roles.cg$tableind(idx).description := TRUE;

      cg$igt_roles.validate_foreign_keys_del(cg$rec);
      cg$igt_roles.cascade_delete(cg$rec);

      idx := cg$igt_roles.cg$table.NEXT(idx);
    END LOOP;
  END IF;

  -- Application_logic Post-After-Delete-statement <<Start>>
  -- Application_logic Post-After-Delete-statement << End >>
END;
/
