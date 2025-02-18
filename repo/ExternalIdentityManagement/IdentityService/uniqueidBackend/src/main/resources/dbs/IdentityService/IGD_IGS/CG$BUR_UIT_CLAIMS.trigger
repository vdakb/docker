-- <?xml version = '1.0' encoding = 'UTF-8'?>
-- <trigger xmlns="http://xmlns.oracle.com/jdeveloper/1211/offlinedb">
--   <name>CG$BUR_UIT_CLAIMS</name>
--   <enabled>true</enabled>
--   <properties>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_CONNECTION</key>
--       <value class="java.lang.String">igs@hardy</value>
--     </entry>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_ID</key>
--       <value class="oracle.javatools.db.ReferenceID">
--         <name>CG$BUR_UIT_CLAIMS</name>
--         <identifier class="java.math.BigDecimal">46907</identifier>
--         <schemaName>IGD_IGS</schemaName>
--         <type>TRIGGER</type>
--       </value>
--     </entry>
--   </properties>
--   <statementLevel>true</statementLevel>
-- </trigger>

CREATE OR REPLACE
TRIGGER cg$bur_uit_claims
BEFORE UPDATE ON uit_claims FOR EACH ROW
DECLARE
  cg$rec     cg$uit_claims.cg$row_type;
  cg$ind     cg$uit_claims.cg$ind_type;
  cg$old_rec cg$uit_claims.cg$row_type;
BEGIN
  -- Application_logic Pre-Before-Update-row <<Start>>
  -- Application_logic Pre-Before-Update-row << End >>

  -- Load cg$rec/cg$ind values from new
  cg$rec.tnt_id := :new.tnt_id;
  cg$ind.tnt_id := (:new.tnt_id IS NULL AND :old.tnt_id IS NOT NULL)
                OR (:old.tnt_id IS NULL AND :new.tnt_id IS NOT NULL)
                OR NOT(:new.tnt_id = :old.tnt_id);
  cg$uit_claims.cg$table(cg$uit_claims.idx).tnt_id := :old.tnt_id;

  cg$rec.rol_id := :new.rol_id;
  cg$ind.rol_id := (:new.rol_id IS NULL AND :old.rol_id IS NOT NULL)
                OR (:old.rol_id IS NULL AND :new.rol_id IS NOT NULL)
                OR NOT(:new.rol_id = :old.rol_id);
  cg$uit_claims.cg$table(cg$uit_claims.idx).rol_id := :old.rol_id;

  cg$rec.usr_id := :new.usr_id;
  cg$ind.usr_id := (:new.usr_id IS NULL AND :old.usr_id IS NOT NULL)
                OR (:old.usr_id IS NULL AND :new.usr_id IS NOT NULL)
                OR NOT(:new.usr_id = :old.usr_id);
  cg$uit_claims.cg$table(cg$uit_claims.idx).usr_id := :old.usr_id;

  cg$rec.rowversion := :new.rowversion;
  cg$ind.rowversion := (:new.rowversion IS NULL AND :old.rowversion IS NOT NULL)
                    OR (:old.rowversion IS NULL AND :new.rowversion IS NOT NULL)
                    OR NOT(:new.rowversion = :old.rowversion);
  cg$uit_claims.cg$table(cg$uit_claims.idx).rowversion := :old.rowversion;

  cg$rec.created_by := :new.created_by;
  cg$ind.created_by := (:new.created_by IS NULL AND :old.created_by IS NOT NULL)
                    OR (:old.created_by IS NULL AND :new.created_by IS NOT NULL)
                    OR NOT(:new.created_by = :old.created_by);
  cg$uit_claims.cg$table(cg$uit_claims.idx).created_by := :old.created_by;

  cg$rec.created_on := :new.created_on;
  cg$ind.created_on := (:new.created_on IS NULL AND :old.created_on IS NOT NULL)
                    OR (:old.created_on IS NULL AND :new.created_on IS NOT NULL)
                    OR NOT(:new.created_on = :old.created_on);
  cg$uit_claims.cg$table(cg$uit_claims.idx).created_on := :old.created_on;

  cg$rec.updated_by := :new.updated_by;
  cg$ind.updated_by := (:new.updated_by IS NULL AND :old.updated_by IS NOT NULL)
                    OR (:old.updated_by IS NULL AND :new.updated_by IS NOT NULL)
                    OR NOT(:new.updated_by = :old.updated_by);
  cg$uit_claims.cg$table(cg$uit_claims.idx).updated_by := :old.updated_by;

  cg$rec.updated_on := :new.updated_on;
  cg$ind.updated_on := (:new.updated_on IS NULL AND :old.updated_on IS NOT NULL)
                    OR (:old.updated_on IS NULL AND :new.updated_on IS NOT NULL)
                    OR NOT(:new.updated_on = :old.updated_on);
  cg$uit_claims.cg$table(cg$uit_claims.idx).updated_on := :old.updated_on;

  cg$uit_claims.idx := cg$uit_claims.idx + 1;

  IF NOT (cg$uit_claims.called_from_package)
  THEN
    cg$uit_claims.upd(cg$rec, cg$ind, FALSE);
    cg$uit_claims.called_from_package := FALSE;
  END IF;

  :new.rowversion := cg$rec.rowversion;
  :new.created_by := cg$rec.created_by;
  :new.created_on := cg$rec.created_on;
  :new.updated_by := cg$rec.updated_by;
  :new.updated_on := cg$rec.updated_on;

  -- Application_logic Post-Before-Update-row <<Start>>
  -- Application_logic Post-Before-Update-row << End >>
END;
/
