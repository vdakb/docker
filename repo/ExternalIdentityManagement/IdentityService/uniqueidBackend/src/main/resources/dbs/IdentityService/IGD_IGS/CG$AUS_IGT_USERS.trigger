-- <?xml version = '1.0' encoding = 'UTF-8'?>
-- <trigger xmlns="http://xmlns.oracle.com/jdeveloper/1211/offlinedb">
--   <name>CG$AUS_IGT_USERS</name>
--   <enabled>true</enabled>
--   <properties>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_CONNECTION</key>
--       <value class="java.lang.String">igs@hardy</value>
--     </entry>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_ID</key>
--       <value class="oracle.javatools.db.ReferenceID">
--         <name>CG$AUS_IGT_USERS</name>
--         <identifier class="java.math.BigDecimal">46731</identifier>
--         <schemaName>IGD_IGS</schemaName>
--         <type>TRIGGER</type>
--       </value>
--     </entry>
--   </properties>
--   <statementLevel>true</statementLevel>
-- </trigger>

CREATE OR REPLACE
TRIGGER cg$aus_igt_users
AFTER UPDATE ON igt_users
DECLARE
  idx        BINARY_INTEGER := cg$igt_users.cg$table.FIRST;
  cg$old_rec cg$igt_users.cg$row_type;
  cg$rec     cg$igt_users.cg$row_type;
  cg$ind     cg$igt_users.cg$ind_type;
BEGIN
  -- Application_logic Pre-After-Update-statement <<Start>>
  -- Application_logic Pre-After-Update-statement << End >>

  WHILE idx IS NOT NULL
  LOOP
    cg$old_rec.id         := cg$igt_users.cg$table(idx).id;
    cg$old_rec.rowversion := cg$igt_users.cg$table(idx).rowversion;
    cg$old_rec.created_by := cg$igt_users.cg$table(idx).created_by;
    cg$old_rec.created_on := cg$igt_users.cg$table(idx).created_on;
    cg$old_rec.updated_by := cg$igt_users.cg$table(idx).updated_by;
    cg$old_rec.updated_on := cg$igt_users.cg$table(idx).updated_on;
    cg$old_rec.active     := cg$igt_users.cg$table(idx).active;
    cg$old_rec.username   := cg$igt_users.cg$table(idx).username;
    cg$old_rec.lastname   := cg$igt_users.cg$table(idx).lastname;
    cg$old_rec.firstname  := cg$igt_users.cg$table(idx).firstname;
    cg$old_rec.language   := cg$igt_users.cg$table(idx).language;
    cg$old_rec.email      := cg$igt_users.cg$table(idx).email;
    cg$old_rec.phone      := cg$igt_users.cg$table(idx).phone;
    cg$old_rec.mobile     := cg$igt_users.cg$table(idx).mobile;

    IF NOT (cg$igt_users.called_from_package)
    THEN
      idx               := cg$igt_users.cg$table.NEXT(idx);
      cg$rec.id         := cg$igt_users.cg$table(idx).id;
      cg$ind.id         := updating('id');
      cg$rec.rowversion := cg$igt_users.cg$table(idx).rowversion;
      cg$ind.rowversion := updating('rowversion');
      cg$rec.created_by := cg$igt_users.cg$table(idx).created_by;
      cg$ind.created_by := updating('created_by');
      cg$rec.created_on := cg$igt_users.cg$table(idx).created_on;
      cg$ind.created_on := updating('created_on');
      cg$rec.updated_by := cg$igt_users.cg$table(idx).updated_by;
      cg$ind.updated_by := updating('updated_by');
      cg$rec.updated_on := cg$igt_users.cg$table(idx).updated_on;
      cg$ind.updated_on := updating('updated_on');
      cg$rec.active     := cg$igt_users.cg$table(idx).active;
      cg$ind.active     := updating('active');
      cg$rec.username   := cg$igt_users.cg$table(idx).username;
      cg$ind.username   := updating('username');
      cg$rec.lastname   := cg$igt_users.cg$table(idx).lastname;
      cg$ind.lastname   := updating('lastname');
      cg$rec.firstname  := cg$igt_users.cg$table(idx).firstname;
      cg$ind.firstname  := updating('firstname');
      cg$rec.language   := cg$igt_users.cg$table(idx).language;
      cg$ind.language   := updating('language');
      cg$rec.email      := cg$igt_users.cg$table(idx).email;
      cg$ind.email      := updating('email');
      cg$rec.phone      := cg$igt_users.cg$table(idx).phone;
      cg$ind.phone      := updating('phone');
      cg$rec.mobile     := cg$igt_users.cg$table(idx).mobile;
      cg$ind.mobile     := updating('mobile');

      cg$igt_users.validate_foreign_keys_upd(cg$rec, cg$old_rec, cg$ind);
      cg$igt_users.cascade_update(cg$rec, cg$old_rec);

      cg$igt_users.called_from_package := FALSE;
    END IF;
    idx := cg$igt_users.cg$table.NEXT(idx);
  END LOOP;

  cg$igt_users.cg$table.DELETE;

  -- Application_logic Post-After-Update-statement <<Start>>
  -- Application_logic Post-After-Update-statement << End >>
END;
/
