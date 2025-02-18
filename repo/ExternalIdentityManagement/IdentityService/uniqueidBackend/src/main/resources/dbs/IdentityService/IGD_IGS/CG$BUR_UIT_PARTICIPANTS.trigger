-- <?xml version = '1.0' encoding = 'UTF-8'?>
-- <trigger xmlns="http://xmlns.oracle.com/jdeveloper/1211/offlinedb">
--   <name>CG$BUR_UIT_PARTICIPANTS</name>
--   <enabled>true</enabled>
--   <properties>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_CONNECTION</key>
--       <value class="java.lang.String">igs@hardy</value>
--     </entry>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_ID</key>
--       <value class="oracle.javatools.db.ReferenceID">
--         <name>CG$BUR_UIT_PARTICIPANTS</name>
--         <identifier class="java.math.BigDecimal">46814</identifier>
--         <schemaName>IGD_IGS</schemaName>
--         <type>TRIGGER</type>
--       </value>
--     </entry>
--   </properties>
--   <statementLevel>true</statementLevel>
-- </trigger>

CREATE OR REPLACE
TRIGGER cg$bur_uit_participants
BEFORE UPDATE ON uit_participants FOR EACH ROW
DECLARE
  cg$rec     cg$uit_participants.cg$row_type;
  cg$ind     cg$uit_participants.cg$ind_type;
  cg$old_rec cg$uit_participants.cg$row_type;
BEGIN
  -- Application_logic Pre-Before-Update-row <<Start>>
  -- Application_logic Pre-Before-Update-row << End >>

  -- Load cg$rec/cg$ind values from new
  cg$rec.id := :new.id;
  cg$ind.id := (:new.id IS NULL AND :old.id IS NOT NULL)
                OR (:old.id IS NULL AND :new.id IS NOT NULL)
                OR NOT(:new.id = :old.id);
  cg$uit_participants.cg$table(cg$uit_participants.idx).id := :old.id;

  cg$rec.rowversion := :new.rowversion;
  cg$ind.rowversion := (:new.rowversion IS NULL AND :old.rowversion IS NOT NULL)
                    OR (:old.rowversion IS NULL AND :new.rowversion IS NOT NULL)
                    OR NOT(:new.rowversion = :old.rowversion);
  cg$uit_participants.cg$table(cg$uit_participants.idx).rowversion := :old.rowversion;

  cg$rec.created_by := :new.created_by;
  cg$ind.created_by := (:new.created_by IS NULL AND :old.created_by IS NOT NULL)
                    OR (:old.created_by IS NULL AND :new.created_by IS NOT NULL)
                    OR NOT(:new.created_by = :old.created_by);
  cg$uit_participants.cg$table(cg$uit_participants.idx).created_by := :old.created_by;

  cg$rec.created_on := :new.created_on;
  cg$ind.created_on := (:new.created_on IS NULL AND :old.created_on IS NOT NULL)
                    OR (:old.created_on IS NULL AND :new.created_on IS NOT NULL)
                    OR NOT(:new.created_on = :old.created_on);
  cg$uit_participants.cg$table(cg$uit_participants.idx).created_on := :old.created_on;

  cg$rec.updated_by := :new.updated_by;
  cg$ind.updated_by := (:new.updated_by IS NULL AND :old.updated_by IS NOT NULL)
                    OR (:old.updated_by IS NULL AND :new.updated_by IS NOT NULL)
                    OR NOT(:new.updated_by = :old.updated_by);
  cg$uit_participants.cg$table(cg$uit_participants.idx).updated_by := :old.updated_by;

  cg$rec.updated_on := :new.updated_on;
  cg$ind.updated_on := (:new.updated_on IS NULL AND :old.updated_on IS NOT NULL)
                    OR (:old.updated_on IS NULL AND :new.updated_on IS NOT NULL)
                    OR NOT(:new.updated_on = :old.updated_on);
  cg$uit_participants.cg$table(cg$uit_participants.idx).updated_on := :old.updated_on;

  cg$rec.name := :new.name;
  cg$ind.name := (:new.name IS NULL AND :old.name IS NOT NULL)
                OR (:old.name IS NULL AND :new.name IS NOT NULL)
                OR NOT(:new.name = :old.name);
  cg$uit_participants.cg$table(cg$uit_participants.idx).name := :old.name;

  cg$rec.description := :new.description;
  cg$ind.description := (:new.description IS NULL AND :old.description IS NOT NULL)
                OR (:old.description IS NULL AND :new.description IS NOT NULL)
                OR NOT(:new.description = :old.description);
  cg$uit_participants.cg$table(cg$uit_participants.idx).description := :old.description;

  cg$uit_participants.idx := cg$uit_participants.idx + 1;

  IF NOT (cg$uit_participants.called_from_package)
  THEN
    cg$uit_participants.upd(cg$rec, cg$ind, FALSE);
    cg$uit_participants.called_from_package := FALSE;
  END IF;

  :new.rowversion     := cg$rec.rowversion;
  :new.created_by     := cg$rec.created_by;
  :new.created_on     := cg$rec.created_on;
  :new.updated_by     := cg$rec.updated_by;
  :new.updated_on     := cg$rec.updated_on;
  :new.name           := cg$rec.name;
  :new.description    := cg$rec.description;

  -- Application_logic Post-Before-Update-row <<Start>>
  -- Application_logic Post-Before-Update-row << End >>
END;
/
