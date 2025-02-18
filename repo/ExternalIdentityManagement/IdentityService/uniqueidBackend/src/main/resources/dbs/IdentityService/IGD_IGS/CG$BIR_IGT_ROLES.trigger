-- <?xml version = '1.0' encoding = 'UTF-8'?>
-- <trigger xmlns="http://xmlns.oracle.com/jdeveloper/1211/offlinedb">
--   <name>CG$BIR_IGT_ROLES</name>
--   <enabled>true</enabled>
--   <properties>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_CONNECTION</key>
--       <value class="java.lang.String">igs@hardy</value>
--     </entry>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_ID</key>
--       <value class="oracle.javatools.db.ReferenceID">
--         <name>CG$BIR_IGT_ROLES</name>
--         <identifier class="java.math.BigDecimal">46718</identifier>
--         <schemaName>IGD_IGS</schemaName>
--         <type>TRIGGER</type>
--       </value>
--     </entry>
--   </properties>
--   <statementLevel>true</statementLevel>
-- </trigger>

CREATE OR REPLACE
TRIGGER cg$bir_igt_roles
BEFORE INSERT ON igt_roles FOR EACH ROW
DECLARE
  cg$rec cg$igt_roles.cg$row_type;
  cg$ind cg$igt_roles.cg$ind_type;
BEGIN
  -- Application_logic Pre-Before-Insert-row <<Start>>
  -- Application_logic Pre-Before-Insert-row << End >>

  -- Load cg$rec/cg$ind values from new
  cg$rec.id := :new.id;
  IF (:new.id IS NULL)
  THEN
    cg$ind.id := FALSE;
  ELSE
    cg$ind.id := TRUE;
  END IF;
  cg$rec.rowversion := :new.rowversion;
  IF (:new.rowversion IS NULL)
  THEN
    cg$ind.rowversion := FALSE;
  ELSE
    cg$ind.rowversion := TRUE;
  END IF;
  cg$rec.created_by := :new.created_by;
  IF (:new.created_by IS NULL)
  THEN
    cg$ind.created_by := FALSE;
  ELSE
    cg$ind.created_by := TRUE;
  END IF;
  cg$rec.created_on := :new.created_on;
  IF (:new.created_on IS NULL)
  THEN
    cg$ind.created_on := FALSE;
  ELSE
    cg$ind.created_on := TRUE;
  END IF;
  cg$rec.updated_by := :new.updated_by;
  IF (:new.updated_by IS NULL)
  THEN
    cg$ind.updated_by := FALSE;
  ELSE
    cg$ind.updated_by := TRUE;
  END IF;
  cg$rec.updated_on := :new.updated_on;
  IF (:new.updated_on IS NULL)
  THEN
    cg$ind.updated_on := FALSE;
  ELSE
    cg$ind.updated_on := TRUE;
  END IF;
  cg$rec.active       := :new.active;
  cg$ind.active       := TRUE;
  cg$rec.display_name := :new.display_name;
  cg$ind.display_name := TRUE;
  cg$rec.description  := :new.description;
  cg$ind.description  := TRUE;

  IF NOT (cg$igt_roles.called_from_package)
  THEN
    cg$igt_roles.ins(cg$rec, cg$ind, FALSE);
    cg$igt_roles.called_from_package := FALSE;
  END IF;

  cg$igt_roles.cg$table(cg$igt_roles.idx).id              := cg$rec.id;
  cg$igt_roles.cg$tableind(cg$igt_roles.idx).id           := cg$ind.id;

  cg$igt_roles.cg$table(cg$igt_roles.idx).rowversion      := cg$rec.rowversion;
  cg$igt_roles.cg$tableind(cg$igt_roles.idx).rowversion   := cg$ind.rowversion;

  cg$igt_roles.cg$table(cg$igt_roles.idx).created_by      := cg$rec.created_by;
  cg$igt_roles.cg$tableind(cg$igt_roles.idx).created_by   := cg$ind.created_by;

  cg$igt_roles.cg$table(cg$igt_roles.idx).created_on      := cg$rec.created_on;
  cg$igt_roles.cg$tableind(cg$igt_roles.idx).created_on   := cg$ind.created_on;

  cg$igt_roles.cg$table(cg$igt_roles.idx).updated_by      := cg$rec.updated_by;
  cg$igt_roles.cg$tableind(cg$igt_roles.idx).updated_by   := cg$ind.updated_by;

  cg$igt_roles.cg$table(cg$igt_roles.idx).updated_on      := cg$rec.updated_on;
  cg$igt_roles.cg$tableind(cg$igt_roles.idx).updated_on   := cg$ind.updated_on;

  cg$igt_roles.cg$table(cg$igt_roles.idx).active          := cg$rec.active;
  cg$igt_roles.cg$tableind(cg$igt_roles.idx).active       := cg$ind.active;

  cg$igt_roles.cg$table(cg$igt_roles.idx).display_name    := cg$rec.display_name;
  cg$igt_roles.cg$tableind(cg$igt_roles.idx).display_name := cg$ind.display_name;

  cg$igt_roles.cg$table(cg$igt_roles.idx).description     := cg$rec.description;
  cg$igt_roles.cg$tableind(cg$igt_roles.idx).description  := cg$ind.description;

  cg$igt_roles.idx := cg$igt_roles.idx + 1;

  :new.id           := cg$rec.id;
  :new.rowversion   := cg$rec.rowversion;
  :new.created_by   := cg$rec.created_by;
  :new.created_on   := cg$rec.created_on;
  :new.updated_by   := cg$rec.updated_by;
  :new.updated_on   := cg$rec.updated_on;
  :new.active       := cg$rec.active;
  :new.display_name := cg$rec.display_name;
  :new.description  := cg$rec.description;

  -- Application_logic Post-Before-Insert-row <<Start>>
  -- Application_logic Post-Before-Insert-row << End >>
END;
/
