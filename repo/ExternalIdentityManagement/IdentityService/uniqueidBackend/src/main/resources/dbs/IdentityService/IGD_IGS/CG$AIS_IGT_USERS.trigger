-- <?xml version = '1.0' encoding = 'UTF-8'?>
-- <trigger xmlns="http://xmlns.oracle.com/jdeveloper/1211/offlinedb">
--   <name>CG$AIS_IGT_USERS</name>
--   <enabled>true</enabled>
--   <properties>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_CONNECTION</key>
--       <value class="java.lang.String">igs@hardy</value>
--     </entry>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_ID</key>
--       <value class="oracle.javatools.db.ReferenceID">
--         <name>CG$AIS_IGT_USERS</name>
--         <identifier class="java.math.BigDecimal">46728</identifier>
--         <schemaName>IGD_IGS</schemaName>
--         <type>TRIGGER</type>
--       </value>
--     </entry>
--   </properties>
--   <statementLevel>true</statementLevel>
-- </trigger>

CREATE OR REPLACE
TRIGGER cg$ais_igt_users
AFTER INSERT ON igt_users
DECLARE
  fk_check   INTEGER;
  idx        BINARY_INTEGER := cg$igt_users.cg$table.FIRST;
  cg$rec     cg$igt_users.cg$row_type;
  cg$old_rec cg$igt_users.cg$row_type;
BEGIN
  -- Application_logic Pre-After-Insert-statement <<Start>>
  -- Application_logic Pre-After-Insert-statement << End >>

  IF NOT (cg$igt_users.called_from_package)
  THEN
    WHILE idx IS NOT NULL
    LOOP
      cg$rec.id         := cg$igt_users.cg$table(idx).id;
      cg$rec.rowversion := cg$igt_users.cg$table(idx).rowversion;
      cg$rec.created_by := cg$igt_users.cg$table(idx).created_by;
      cg$rec.created_on := cg$igt_users.cg$table(idx).created_on;
      cg$rec.updated_by := cg$igt_users.cg$table(idx).updated_by;
      cg$rec.updated_on := cg$igt_users.cg$table(idx).updated_on;
      cg$rec.active     := cg$igt_users.cg$table(idx).active;
      cg$rec.username   := cg$igt_users.cg$table(idx).username;
      cg$rec.lastname   := cg$igt_users.cg$table(idx).lastname;
      cg$rec.firstname  := cg$igt_users.cg$table(idx).firstname;
      cg$rec.language   := cg$igt_users.cg$table(idx).language;
      cg$rec.email      := cg$igt_users.cg$table(idx).email;
      cg$rec.phone      := cg$igt_users.cg$table(idx).phone;
      cg$rec.mobile     := cg$igt_users.cg$table(idx).mobile;

      cg$igt_users.validate_foreign_keys_ins(cg$rec);

      idx := cg$igt_users.cg$table.NEXT(idx);
    END LOOP;
  END IF;

  -- Application_logic Post-After-Insert-statement <<Start>>
  -- Application_logic Post-After-Insert-statement << End >>
END;
/
