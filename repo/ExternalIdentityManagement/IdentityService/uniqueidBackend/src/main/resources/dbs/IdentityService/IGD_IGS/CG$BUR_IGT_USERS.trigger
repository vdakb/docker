-- <?xml version = '1.0' encoding = 'UTF-8'?>
-- <trigger xmlns="http://xmlns.oracle.com/jdeveloper/1211/offlinedb">
--   <name>CG$BUR_IGT_USERS</name>
--   <enabled>true</enabled>
--   <properties>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_CONNECTION</key>
--       <value class="java.lang.String">igs@hardy</value>
--     </entry>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_ID</key>
--       <value class="oracle.javatools.db.ReferenceID">
--         <name>CG$BUR_IGT_USERS</name>
--         <identifier class="java.math.BigDecimal">46730</identifier>
--         <schemaName>IGD_IGS</schemaName>
--         <type>TRIGGER</type>
--       </value>
--     </entry>
--   </properties>
--   <statementLevel>true</statementLevel>
-- </trigger>

CREATE OR REPLACE
TRIGGER cg$bur_igt_users
BEFORE UPDATE ON igt_users FOR EACH ROW
DECLARE
  cg$rec     cg$igt_users.cg$row_type;
  cg$ind     cg$igt_users.cg$ind_type;
  cg$old_rec cg$igt_users.cg$row_type;
BEGIN
  -- Application_logic Pre-Before-Update-row <<Start>>
  -- Application_logic Pre-Before-Update-row << End >>

  -- Load cg$rec/cg$ind values from new
  cg$rec.id := :new.id;
  cg$ind.id := (:new.id IS NULL AND :old.id IS NOT NULL)
            OR (:old.id IS NULL AND :new.id IS NOT NULL)
            OR NOT(:new.id = :old.id);
  cg$igt_users.cg$table(cg$igt_users.idx).id := :old.id;

  cg$rec.rowversion := :new.rowversion;
  cg$ind.rowversion := (:new.rowversion IS NULL AND :old.rowversion IS NOT NULL)
                    OR (:old.rowversion IS NULL AND :new.rowversion IS NOT NULL)
                    OR NOT(:new.rowversion = :old.rowversion);
  cg$igt_users.cg$table(cg$igt_users.idx).rowversion := :old.rowversion;

  cg$rec.created_by := :new.created_by;
  cg$ind.created_by := (:new.created_by IS NULL AND :old.created_by IS NOT NULL)
                    OR (:old.created_by IS NULL AND :new.created_by IS NOT NULL)
                    OR NOT(:new.created_by = :old.created_by);
  cg$igt_users.cg$table(cg$igt_users.idx).created_by := :old.created_by;

  cg$rec.created_on := :new.created_on;
  cg$ind.created_on := (:new.created_on IS NULL AND :old.created_on IS NOT NULL)
                    OR (:old.created_on IS NULL AND :new.created_on IS NOT NULL)
                    OR NOT(:new.created_on = :old.created_on);
  cg$igt_users.cg$table(cg$igt_users.idx).created_on := :old.created_on;

  cg$rec.updated_by := :new.updated_by;
  cg$ind.updated_by := (:new.updated_by IS NULL AND :old.updated_by IS NOT NULL)
                    OR (:old.updated_by IS NULL AND :new.updated_by IS NOT NULL)
                    OR NOT(:new.updated_by = :old.updated_by);
  cg$igt_users.cg$table(cg$igt_users.idx).updated_by := :old.updated_by;

  cg$rec.updated_on := :new.updated_on;
  cg$ind.updated_on := (:new.updated_on IS NULL AND :old.updated_on IS NOT NULL)
                    OR (:old.updated_on IS NULL AND :new.updated_on IS NOT NULL)
                    OR NOT(:new.updated_on = :old.updated_on);
  cg$igt_users.cg$table(cg$igt_users.idx).updated_on := :old.updated_on;

  cg$rec.active := :new.active;
  cg$ind.active := (:new.active IS NULL AND :old.active IS NOT NULL)
                OR (:old.active IS NULL AND :new.active IS NOT NULL)
                OR NOT(:new.active = :old.active);
  cg$igt_users.cg$table(cg$igt_users.idx).active := :old.active;

  cg$rec.username := :new.username;
  cg$ind.username := (:new.username IS NULL AND :old.username IS NOT NULL)
                  OR (:old.username IS NULL AND :new.username IS NOT NULL)
                  OR NOT(:new.username = :old.username);
  cg$igt_users.cg$table(cg$igt_users.idx).username := :old.username;

  cg$rec.lastname := :new.lastname;
  cg$ind.lastname := (:new.lastname IS NULL AND :old.lastname IS NOT NULL)
                  OR (:old.lastname IS NULL AND :new.lastname IS NOT NULL)
                  OR NOT(:new.lastname = :old.lastname);
  cg$igt_users.cg$table(cg$igt_users.idx).lastname := :old.lastname;

  cg$rec.firstname := :new.firstname;
  cg$ind.firstname := (:new.firstname IS NULL AND :old.firstname IS NOT NULL)
                   OR (:old.firstname IS NULL AND :new.firstname IS NOT NULL)
                   OR NOT(:new.firstname = :old.firstname);
  cg$igt_users.cg$table(cg$igt_users.idx).firstname := :old.firstname;

  cg$rec.language := :new.language;
  cg$ind.language := (:new.language IS NULL AND :old.language IS NOT NULL)
                  OR (:old.language IS NULL AND :new.language IS NOT NULL)
                  OR NOT(:new.language = :old.language);
  cg$igt_users.cg$table(cg$igt_users.idx).language := :old.language;

  cg$rec.email := :new.email;
  cg$ind.email := (:new.email IS NULL AND :old.email IS NOT NULL)
               OR (:old.email IS NULL AND :new.email IS NOT NULL)
               OR NOT(:new.email = :old.email);
  cg$igt_users.cg$table(cg$igt_users.idx).email := :old.email;

  cg$rec.phone := :new.phone;
  cg$ind.phone := (:new.phone IS NULL AND :old.phone IS NOT NULL)
               OR (:old.phone IS NULL AND :new.phone IS NOT NULL)
               OR NOT(:new.phone = :old.phone);
  cg$igt_users.cg$table(cg$igt_users.idx).phone := :old.phone;

  cg$rec.mobile := :new.mobile;
  cg$ind.mobile := (:new.mobile IS NULL AND :old.mobile IS NOT NULL)
                OR (:old.mobile IS NULL AND :new.mobile IS NOT NULL)
                OR NOT(:new.mobile = :old.mobile);
  cg$igt_users.cg$table(cg$igt_users.idx).mobile := :old.mobile;

  cg$igt_users.idx := cg$igt_users.idx + 1;

  IF NOT (cg$igt_users.called_from_package)
  THEN
    cg$igt_users.upd(cg$rec, cg$ind, FALSE);
    cg$igt_users.called_from_package := FALSE;
  END IF;

  :new.rowversion := cg$rec.rowversion;
  :new.created_by := cg$rec.created_by;
  :new.created_on := cg$rec.created_on;
  :new.updated_by := cg$rec.updated_by;
  :new.updated_on := cg$rec.updated_on;
  :new.active     := cg$rec.active;
  :new.username   := cg$rec.username;
  :new.lastname   := cg$rec.lastname;
  :new.firstname  := cg$rec.firstname;
  :new.language   := cg$rec.language;
  :new.email      := cg$rec.email;
  :new.phone      := cg$rec.phone;
  :new.mobile     := cg$rec.mobile;

  -- Application_logic Post-Before-Update-row <<Start>>
  -- Application_logic Post-Before-Update-row << End >>
END;
/
