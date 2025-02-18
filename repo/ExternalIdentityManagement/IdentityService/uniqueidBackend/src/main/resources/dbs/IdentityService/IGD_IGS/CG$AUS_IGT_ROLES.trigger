-- <?xml version = '1.0' encoding = 'UTF-8'?>
-- <trigger xmlns="http://xmlns.oracle.com/jdeveloper/1211/offlinedb">
--   <name>CG$AUS_IGT_ROLES</name>
--   <enabled>true</enabled>
--   <properties>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_CONNECTION</key>
--       <value class="java.lang.String">igs@hardy</value>
--     </entry>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_ID</key>
--       <value class="oracle.javatools.db.ReferenceID">
--         <name>CG$AUS_IGT_ROLES</name>
--         <identifier class="java.math.BigDecimal">46722</identifier>
--         <schemaName>IGD_IGS</schemaName>
--         <type>TRIGGER</type>
--       </value>
--     </entry>
--   </properties>
--   <statementLevel>true</statementLevel>
-- </trigger>

CREATE OR REPLACE
TRIGGER cg$aus_igt_roles
AFTER UPDATE ON igt_roles
DECLARE
  idx        BINARY_INTEGER := cg$igt_roles.cg$table.FIRST;
  cg$old_rec cg$igt_roles.cg$row_type;
  cg$rec     cg$igt_roles.cg$row_type;
  cg$ind     cg$igt_roles.cg$ind_type;
BEGIN
  -- Application_logic Pre-After-Update-statement <<Start>>
  -- Application_logic Pre-After-Update-statement << End >>

  WHILE idx IS NOT NULL
  LOOP
    cg$old_rec.id           := cg$igt_roles.cg$table(idx).id;
    cg$old_rec.rowversion   := cg$igt_roles.cg$table(idx).rowversion;
    cg$old_rec.created_by   := cg$igt_roles.cg$table(idx).created_by;
    cg$old_rec.created_on   := cg$igt_roles.cg$table(idx).created_on;
    cg$old_rec.updated_by   := cg$igt_roles.cg$table(idx).updated_by;
    cg$old_rec.updated_on   := cg$igt_roles.cg$table(idx).updated_on;
    cg$old_rec.active       := cg$igt_roles.cg$table(idx).active;
    cg$old_rec.display_name := cg$igt_roles.cg$table(idx).display_name;
    cg$old_rec.description  := cg$igt_roles.cg$table(idx).description;

    IF NOT (cg$igt_roles.called_from_package)
    THEN
      idx                 := cg$igt_roles.cg$table.NEXT(idx);
      cg$rec.id           := cg$igt_roles.cg$table(idx).id;
      cg$ind.id           := updating('id');
      cg$rec.rowversion   := cg$igt_roles.cg$table(idx).rowversion;
      cg$ind.rowversion   := updating('rowversion');
      cg$rec.created_by   := cg$igt_roles.cg$table(idx).created_by;
      cg$ind.created_by   := updating('created_by');
      cg$rec.created_on   := cg$igt_roles.cg$table(idx).created_on;
      cg$ind.created_on   := updating('created_on');
      cg$rec.updated_by   := cg$igt_roles.cg$table(idx).updated_by;
      cg$ind.updated_by   := updating('updated_by');
      cg$rec.updated_on   := cg$igt_roles.cg$table(idx).updated_on;
      cg$ind.updated_on   := updating('updated_on');
      cg$rec.active       := cg$igt_roles.cg$table(idx).active;
      cg$ind.active       := updating('active');
      cg$rec.display_name := cg$igt_roles.cg$table(idx).display_name;
      cg$ind.display_name := updating('display_name');
      cg$rec.description  := cg$igt_roles.cg$table(idx).description;
      cg$ind.description  := updating('description');

      cg$igt_roles.validate_foreign_keys_upd(cg$rec, cg$old_rec, cg$ind);
      cg$igt_roles.called_from_package := FALSE;
    END IF;
    idx := cg$igt_roles.cg$table.NEXT(idx);
  END LOOP;

  cg$igt_roles.cg$table.DELETE;

  -- Application_logic Post-After-Update-statement <<Start>>
  -- Application_logic Post-After-Update-statement << End >>
END;
/
