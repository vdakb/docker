-- <?xml version = '1.0' encoding = 'UTF-8'?>
-- <trigger xmlns="http://xmlns.oracle.com/jdeveloper/1211/offlinedb">
--   <name>CG$BUR_UIT_IDENTIFIERS</name>
--   <enabled>true</enabled>
--   <properties>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_CONNECTION</key>
--       <value class="java.lang.String">igs@hardy</value>
--     </entry>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_ID</key>
--       <value class="oracle.javatools.db.ReferenceID">
--         <name>CG$BUR_UIT_IDENTIFIERS</name>
--         <identifier class="java.math.BigDecimal">46841</identifier>
--         <schemaName>IGD_IGS</schemaName>
--         <type>TRIGGER</type>
--       </value>
--     </entry>
--   </properties>
--   <statementLevel>true</statementLevel>
-- </trigger>

CREATE OR REPLACE
TRIGGER cg$bur_uit_identifiers
BEFORE UPDATE ON uit_identifiers FOR EACH ROW
DECLARE
  cg$rec     cg$uit_identifiers.cg$row_type;
  cg$ind     cg$uit_identifiers.cg$ind_type;
  cg$old_rec cg$uit_identifiers.cg$row_type;
BEGIN
  -- Application_logic Pre-Before-Update-row <<Start>>
  -- Application_logic Pre-Before-Update-row << End >>

  -- Load cg$rec/cg$ind values from new
  cg$rec.tnt_id := :new.tnt_id;
  cg$ind.tnt_id := (:new.tnt_id IS NULL AND :old.tnt_id IS NOT NULL)
                OR (:old.tnt_id IS NULL AND :new.tnt_id IS NOT NULL)
                OR NOT(:new.tnt_id = :old.tnt_id);
  cg$uit_identifiers.cg$table(cg$uit_identifiers.idx).tnt_id := :old.tnt_id;

  cg$rec.typ_id := :new.typ_id;
  cg$ind.typ_id := (:new.typ_id IS NULL AND :old.typ_id IS NOT NULL)
                OR (:old.typ_id IS NULL AND :new.typ_id IS NOT NULL)
                OR NOT(:new.typ_id = :old.typ_id);
  cg$uit_identifiers.cg$table(cg$uit_identifiers.idx).typ_id := :old.typ_id;

  cg$rec.ext_id := :new.ext_id;
  cg$ind.ext_id := (:new.ext_id IS NULL AND :old.ext_id IS NOT NULL)
                OR (:old.ext_id IS NULL AND :new.ext_id IS NOT NULL)
                OR NOT(:new.ext_id = :old.ext_id);
  cg$uit_identifiers.cg$table(cg$uit_identifiers.idx).ext_id := :old.ext_id;

  cg$rec.rowversion := :new.rowversion;
  cg$ind.rowversion := (:new.rowversion IS NULL AND :old.rowversion IS NOT NULL)
                    OR (:old.rowversion IS NULL AND :new.rowversion IS NOT NULL)
                    OR NOT(:new.rowversion = :old.rowversion);
  cg$uit_identifiers.cg$table(cg$uit_identifiers.idx).rowversion := :old.rowversion;

  cg$rec.created_by := :new.created_by;
  cg$ind.created_by := (:new.created_by IS NULL AND :old.created_by IS NOT NULL)
                    OR (:old.created_by IS NULL AND :new.created_by IS NOT NULL)
                    OR NOT(:new.created_by = :old.created_by);
  cg$uit_identifiers.cg$table(cg$uit_identifiers.idx).created_by := :old.created_by;

  cg$rec.created_on := :new.created_on;
  cg$ind.created_on := (:new.created_on IS NULL AND :old.created_on IS NOT NULL)
                    OR (:old.created_on IS NULL AND :new.created_on IS NOT NULL)
                    OR NOT(:new.created_on = :old.created_on);
  cg$uit_identifiers.cg$table(cg$uit_identifiers.idx).created_on := :old.created_on;

  cg$rec.updated_by := :new.updated_by;
  cg$ind.updated_by := (:new.updated_by IS NULL AND :old.updated_by IS NOT NULL)
                    OR (:old.updated_by IS NULL AND :new.updated_by IS NOT NULL)
                    OR NOT(:new.updated_by = :old.updated_by);
  cg$uit_identifiers.cg$table(cg$uit_identifiers.idx).updated_by := :old.updated_by;

  cg$rec.updated_on := :new.updated_on;
  cg$ind.updated_on := (:new.updated_on IS NULL AND :old.updated_on IS NOT NULL)
                    OR (:old.updated_on IS NULL AND :new.updated_on IS NOT NULL)
                    OR NOT(:new.updated_on = :old.updated_on);
  cg$uit_identifiers.cg$table(cg$uit_identifiers.idx).updated_on := :old.updated_on;

  cg$rec.state := :new.state;
  cg$ind.state := (:new.state IS NULL AND :old.state IS NOT NULL)
               OR (:old.state IS NULL AND :new.state IS NOT NULL)
               OR NOT(:new.state = :old.state);
  cg$uit_identifiers.cg$table(cg$uit_identifiers.idx).state := :old.state;

  cg$uit_identifiers.idx := cg$uit_identifiers.idx + 1;

  IF NOT (cg$uit_identifiers.called_from_package)
  THEN
    cg$uit_identifiers.upd(cg$rec, cg$ind, FALSE);
    cg$uit_identifiers.called_from_package := FALSE;
  END IF;

  :new.rowversion := cg$rec.rowversion;
  :new.created_by := cg$rec.created_by;
  :new.created_on := cg$rec.created_on;
  :new.updated_by := cg$rec.updated_by;
  :new.updated_on := cg$rec.updated_on;
  :new.state      := cg$rec.state;

  -- Application_logic Post-Before-Update-row <<Start>>
  -- Application_logic Post-Before-Update-row << End >>
END;
/
