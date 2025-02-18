-- <?xml version = '1.0' encoding = 'UTF-8'?>
-- <trigger xmlns="http://xmlns.oracle.com/jdeveloper/1211/offlinedb">
--   <name>CG$BDR_UIT_CLAIMS</name>
--   <enabled>true</enabled>
--   <properties>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_CONNECTION</key>
--       <value class="java.lang.String">igs@hardy</value>
--     </entry>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_ID</key>
--       <value class="oracle.javatools.db.ReferenceID">
--         <name>CG$BDR_UIT_CLAIMS</name>
--         <identifier class="java.math.BigDecimal">46910</identifier>
--         <schemaName>IGD_IGS</schemaName>
--         <type>TRIGGER</type>
--       </value>
--     </entry>
--   </properties>
--   <statementLevel>true</statementLevel>
-- </trigger>

CREATE OR REPLACE
TRIGGER cg$bdr_uit_claims
BEFORE DELETE ON uit_claims FOR EACH ROW
DECLARE
  cg$pk  cg$uit_claims.cg$pk_type;
  cg$rec cg$uit_claims.cg$row_type;
  cg$ind cg$uit_claims.cg$ind_type;
BEGIN
  -- Application_logic Pre-Before-Delete-row <<Start>>
  -- Application_logic Pre-Before-Delete-row << End >>

  -- Load cg$rec/cg$ind values from old
  cg$pk.tnt_id  := :old.tnt_id;
  cg$rec.tnt_id := :old.tnt_id;
  cg$uit_claims.cg$table(cg$uit_claims.idx).tnt_id := :old.tnt_id;
  cg$pk.rol_id  := :old.rol_id;
  cg$rec.rol_id := :old.rol_id;
  cg$uit_claims.cg$table(cg$uit_claims.idx).rol_id := :old.rol_id;
  cg$pk.usr_id  := :old.usr_id;
  cg$rec.usr_id := :old.usr_id;
  cg$uit_claims.cg$table(cg$uit_claims.idx).usr_id := :old.usr_id;

  cg$uit_claims.idx := cg$uit_claims.idx + 1;
  IF NOT (cg$uit_claims.called_from_package)
  THEN
    cg$uit_claims.del(cg$pk, FALSE);
    cg$uit_claims.called_from_package := FALSE;
  END IF;

  -- Application_logic Post-Before-Delete-row <<Start>>
  -- Application_logic Post-Before-Delete-row << End >>
END;
/
