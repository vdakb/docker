-- <?xml version = '1.0' encoding = 'UTF-8'?>
-- <trigger xmlns="http://xmlns.oracle.com/jdeveloper/1211/offlinedb">
--   <name>CG$BUR_IGT_ROLES</name>
--   <enabled>true</enabled>
--   <properties>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_CONNECTION</key>
--       <value class="java.lang.String">igs@hardy</value>
--     </entry>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_ID</key>
--       <value class="oracle.javatools.db.ReferenceID">
--         <name>CG$BUR_IGT_ROLES</name>
--         <identifier class="java.math.BigDecimal">46721</identifier>
--         <schemaName>IGD_IGS</schemaName>
--         <type>TRIGGER</type>
--       </value>
--     </entry>
--   </properties>
--   <statementLevel>true</statementLevel>
-- </trigger>

CREATE OR REPLACE
TRIGGER cg$bur_igt_roles
BEFORE UPDATE ON igt_roles FOR EACH ROW
DECLARE
  cg$rec     cg$igt_roles.cg$row_type;
  cg$ind     cg$igt_roles.cg$ind_type;
  cg$old_rec cg$igt_roles.cg$row_type;
BEGIN
  -- Application_logic Pre-Before-Update-row <<Start>>
  -- Application_logic Pre-Before-Update-row << End >>

  -- Load cg$rec/cg$ind values from new
  cg$rec.id := :new.id;
  cg$ind.id := (:new.id IS NULL AND :old.id IS NOT NULL)
            OR (:old.id IS NULL AND :new.id IS NOT NULL)
            OR NOT(:new.id = :old.id);
  cg$igt_roles.cg$table(cg$igt_roles.idx).id := :old.id;

  cg$rec.rowversion := :new.rowversion;
  cg$ind.rowversion := (:new.rowversion IS NULL AND :old.rowversion IS NOT NULL)
                    OR (:old.rowversion IS NULL AND :new.rowversion IS NOT NULL)
                    OR NOT(:new.rowversion = :old.rowversion);
  cg$igt_roles.cg$table(cg$igt_roles.idx).rowversion := :old.rowversion;

  cg$rec.created_by := :new.created_by;
  cg$ind.created_by := (:new.created_by IS NULL AND :old.created_by IS NOT NULL)
                    OR (:old.created_by IS NULL AND :new.created_by IS NOT NULL)
                    OR NOT(:new.created_by = :old.created_by);
  cg$igt_roles.cg$table(cg$igt_roles.idx).created_by := :old.created_by;

  cg$rec.created_on := :new.created_on;
  cg$ind.created_on := (:new.created_on IS NULL AND :old.created_on IS NOT NULL)
                    OR (:old.created_on IS NULL AND :new.created_on IS NOT NULL)
                    OR NOT(:new.created_on = :old.created_on);
  cg$igt_roles.cg$table(cg$igt_roles.idx).created_on := :old.created_on;

  cg$rec.updated_by := :new.updated_by;
  cg$ind.updated_by := (:new.updated_by IS NULL AND :old.updated_by IS NOT NULL)
                    OR (:old.updated_by IS NULL AND :new.updated_by IS NOT NULL)
                    OR NOT(:new.updated_by = :old.updated_by);
  cg$igt_roles.cg$table(cg$igt_roles.idx).updated_by := :old.updated_by;

  cg$rec.updated_on := :new.updated_on;
  cg$ind.updated_on := (:new.updated_on IS NULL AND :old.updated_on IS NOT NULL)
                    OR (:old.updated_on IS NULL AND :new.updated_on IS NOT NULL)
                    OR NOT(:new.updated_on = :old.updated_on);
  cg$igt_roles.cg$table(cg$igt_roles.idx).updated_on := :old.updated_on;

  cg$rec.active := :new.active;
  cg$ind.active := (:new.active IS NULL AND :old.active IS NOT NULL)
                OR (:old.active IS NULL AND :new.active IS NOT NULL)
                OR NOT(:new.active = :old.active);
  cg$igt_roles.cg$table(cg$igt_roles.idx).active := :old.active;

  cg$rec.display_name := :new.display_name;
  cg$ind.display_name := (:new.display_name IS NULL AND :old.display_name IS NOT NULL)
                      OR (:old.display_name IS NULL AND :new.display_name IS NOT NULL)
                      OR NOT(:new.display_name = :old.display_name);
  cg$igt_roles.cg$table(cg$igt_roles.idx).display_name := :old.display_name;

  cg$rec.description := :new.description;
  cg$ind.description := (:new.description IS NULL AND :old.description IS NOT NULL)
                     OR (:old.description IS NULL AND :new.description IS NOT NULL)
                     OR NOT(:new.description = :old.description);
  cg$igt_roles.cg$table(cg$igt_roles.idx).description := :old.description;

  cg$igt_roles.idx := cg$igt_roles.idx + 1;

  IF NOT (cg$igt_roles.called_from_package)
  THEN
    cg$igt_roles.upd(cg$rec, cg$ind, FALSE);
    cg$igt_roles.called_from_package := FALSE;
  END IF;

  :new.rowversion   := cg$rec.rowversion;
  :new.created_by   := cg$rec.created_by;
  :new.created_on   := cg$rec.created_on;
  :new.updated_by   := cg$rec.updated_by;
  :new.updated_on   := cg$rec.updated_on;
  :new.active       := cg$rec.active;
  :new.display_name := cg$rec.display_name;
  :new.description  := cg$rec.description;

  -- Application_logic Post-Before-Update-row <<Start>>
  -- Application_logic Post-Before-Update-row << End >>
END;
/
