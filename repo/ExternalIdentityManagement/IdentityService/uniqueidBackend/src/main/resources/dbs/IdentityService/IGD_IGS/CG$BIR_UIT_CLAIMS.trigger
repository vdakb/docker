-- <?xml version = '1.0' encoding = 'UTF-8'?>
-- <trigger xmlns="http://xmlns.oracle.com/jdeveloper/1211/offlinedb">
--   <name>CG$BIR_UIT_CLAIMS</name>
--   <enabled>true</enabled>
--   <properties>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_CONNECTION</key>
--       <value class="java.lang.String">igs@hardy</value>
--     </entry>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_ID</key>
--       <value class="oracle.javatools.db.ReferenceID">
--         <name>CG$BIR_UIT_CLAIMS</name>
--         <identifier class="java.math.BigDecimal">46904</identifier>
--         <schemaName>IGD_IGS</schemaName>
--         <type>TRIGGER</type>
--       </value>
--     </entry>
--   </properties>
--   <statementLevel>true</statementLevel>
-- </trigger>

CREATE OR REPLACE
TRIGGER cg$bir_uit_claims
BEFORE INSERT ON uit_claims FOR EACH ROW
DECLARE
  cg$rec cg$uit_claims.cg$row_type;
  cg$ind cg$uit_claims.cg$ind_type;
BEGIN
  -- Application_logic Pre-Before-Insert-row <<Start>>
  -- Application_logic Pre-Before-Insert-row << End >>

  -- Load cg$rec/cg$ind values from new
  cg$rec.tnt_id := :new.tnt_id;
  IF (:new.tnt_id IS NULL)
  THEN
    cg$ind.tnt_id := FALSE;
  ELSE
    cg$ind.tnt_id := TRUE;
  END IF;
  cg$rec.rol_id := :new.rol_id;
  IF (:new.rol_id IS NULL)
  THEN
    cg$ind.rol_id := FALSE;
  ELSE
    cg$ind.rol_id := TRUE;
  END IF;
  cg$rec.usr_id := :new.usr_id;
  IF (:new.usr_id IS NULL)
  THEN
    cg$ind.usr_id := FALSE;
  ELSE
    cg$ind.usr_id := TRUE;
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

  IF NOT (cg$uit_claims.called_from_package)
  THEN
    cg$uit_claims.ins(cg$rec, cg$ind, FALSE);
    cg$uit_claims.called_from_package := FALSE;
  END IF;

  cg$uit_claims.cg$table(cg$uit_claims.idx).tnt_id            := cg$rec.tnt_id;
  cg$uit_claims.cg$tableind(cg$uit_claims.idx).tnt_id         := cg$ind.tnt_id;

  cg$uit_claims.cg$table(cg$uit_claims.idx).rol_id            := cg$rec.rol_id;
  cg$uit_claims.cg$tableind(cg$uit_claims.idx).rol_id         := cg$ind.rol_id;

  cg$uit_claims.cg$table(cg$uit_claims.idx).usr_id            := cg$rec.usr_id;
  cg$uit_claims.cg$tableind(cg$uit_claims.idx).usr_id         := cg$ind.usr_id;

  cg$uit_claims.cg$table(cg$uit_claims.idx).rowversion        := cg$rec.rowversion;
  cg$uit_claims.cg$tableind(cg$uit_claims.idx).rowversion     := cg$ind.rowversion;

  cg$uit_claims.cg$table(cg$uit_claims.idx).created_by        := cg$rec.created_by;
  cg$uit_claims.cg$tableind(cg$uit_claims.idx).created_by     := cg$ind.created_by;

  cg$uit_claims.cg$table(cg$uit_claims.idx).created_on        := cg$rec.created_on;
  cg$uit_claims.cg$tableind(cg$uit_claims.idx).created_on     := cg$ind.created_on;

  cg$uit_claims.cg$table(cg$uit_claims.idx).updated_by        := cg$rec.updated_by;
  cg$uit_claims.cg$tableind(cg$uit_claims.idx).updated_by     := cg$ind.updated_by;

  cg$uit_claims.cg$table(cg$uit_claims.idx).updated_on        := cg$rec.updated_on;
  cg$uit_claims.cg$tableind(cg$uit_claims.idx).updated_on     := cg$ind.updated_on;

  cg$uit_claims.idx := cg$uit_claims.idx + 1;

  :new.tnt_id         := cg$rec.tnt_id;
  :new.rol_id         := cg$rec.rol_id;
  :new.usr_id         := cg$rec.usr_id;
  :new.rowversion     := cg$rec.rowversion;
  :new.created_by     := cg$rec.created_by;
  :new.created_on     := cg$rec.created_on;
  :new.updated_by     := cg$rec.updated_by;
  :new.updated_on     := cg$rec.updated_on;

  -- Application_logic Post-Before-Insert-row <<Start>>
  -- Application_logic Post-Before-Insert-row << End >>
END;
/
