-- <?xml version = '1.0' encoding = 'UTF-8'?>
-- <trigger xmlns="http://xmlns.oracle.com/jdeveloper/1211/offlinedb">
--   <name>CG$BIR_IGT_USERS</name>
--   <enabled>true</enabled>
--   <properties>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_CONNECTION</key>
--       <value class="java.lang.String">igs@hardy</value>
--     </entry>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_ID</key>
--       <value class="oracle.javatools.db.ReferenceID">
--         <name>CG$BIR_IGT_USERS</name>
--         <identifier class="java.math.BigDecimal">46727</identifier>
--         <schemaName>IGD_IGS</schemaName>
--         <type>TRIGGER</type>
--       </value>
--     </entry>
--   </properties>
--   <statementLevel>true</statementLevel>
-- </trigger>

CREATE OR REPLACE
TRIGGER cg$bir_igt_users
BEFORE INSERT ON igt_users FOR EACH ROW
DECLARE
  cg$rec cg$igt_users.cg$row_type;
  cg$ind cg$igt_users.cg$ind_type;
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
  cg$rec.active     := :new.active;
  cg$ind.active     := TRUE;
  cg$rec.username   := :new.username;
  cg$ind.username   := TRUE;
  cg$rec.lastname   := :new.lastname;
  cg$ind.lastname   := TRUE;
  cg$rec.firstname  := :new.firstname;
  cg$ind.firstname  := TRUE;
  cg$rec.language   := :new.language;
  cg$ind.language   := TRUE;
  cg$rec.email      := :new.email;
  cg$ind.email      := TRUE;
  cg$rec.phone      := :new.phone;
  cg$ind.phone      := TRUE;
  cg$rec.mobile     := :new.mobile;
  cg$ind.mobile     := TRUE;

  IF NOT (cg$igt_users.called_from_package)
  THEN
    cg$igt_users.ins(cg$rec, cg$ind, FALSE);
    cg$igt_users.called_from_package := FALSE;
  END IF;

  cg$igt_users.cg$table(cg$igt_users.idx).id            := cg$rec.id;
  cg$igt_users.cg$tableind(cg$igt_users.idx).id         := cg$ind.id;

  cg$igt_users.cg$table(cg$igt_users.idx).rowversion    := cg$rec.rowversion;
  cg$igt_users.cg$tableind(cg$igt_users.idx).rowversion := cg$ind.rowversion;

  cg$igt_users.cg$table(cg$igt_users.idx).created_by    := cg$rec.created_by;
  cg$igt_users.cg$tableind(cg$igt_users.idx).created_by := cg$ind.created_by;

  cg$igt_users.cg$table(cg$igt_users.idx).created_on    := cg$rec.created_on;
  cg$igt_users.cg$tableind(cg$igt_users.idx).created_on := cg$ind.created_on;

  cg$igt_users.cg$table(cg$igt_users.idx).updated_by    := cg$rec.updated_by;
  cg$igt_users.cg$tableind(cg$igt_users.idx).updated_by := cg$ind.updated_by;

  cg$igt_users.cg$table(cg$igt_users.idx).updated_on    := cg$rec.updated_on;
  cg$igt_users.cg$tableind(cg$igt_users.idx).updated_on := cg$ind.updated_on;

  cg$igt_users.cg$table(cg$igt_users.idx).active        := cg$rec.active;
  cg$igt_users.cg$tableind(cg$igt_users.idx).active     := cg$ind.active;

  cg$igt_users.cg$table(cg$igt_users.idx).username      := cg$rec.username;
  cg$igt_users.cg$tableind(cg$igt_users.idx).username   := cg$ind.username;

  cg$igt_users.cg$table(cg$igt_users.idx).lastname      := cg$rec.lastname;
  cg$igt_users.cg$tableind(cg$igt_users.idx).lastname   := cg$ind.lastname;

  cg$igt_users.cg$table(cg$igt_users.idx).firstname     := cg$rec.firstname;
  cg$igt_users.cg$tableind(cg$igt_users.idx).firstname  := cg$ind.firstname;

  cg$igt_users.cg$table(cg$igt_users.idx).language      := cg$rec.language;
  cg$igt_users.cg$tableind(cg$igt_users.idx).language   := cg$ind.language;

  cg$igt_users.cg$table(cg$igt_users.idx).email         := cg$rec.email;
  cg$igt_users.cg$tableind(cg$igt_users.idx).email      := cg$ind.email;

  cg$igt_users.cg$table(cg$igt_users.idx).phone         := cg$rec.phone;
  cg$igt_users.cg$tableind(cg$igt_users.idx).phone      := cg$ind.phone;

  cg$igt_users.cg$table(cg$igt_users.idx).mobile        := cg$rec.mobile;
  cg$igt_users.cg$tableind(cg$igt_users.idx).mobile     := cg$ind.mobile;

  cg$igt_users.idx := cg$igt_users.idx + 1;

  :new.id         := cg$rec.id;
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

  -- Application_logic Post-Before-Insert-row <<Start>>
  -- Application_logic Post-Before-Insert-row << End >>
END;
/
