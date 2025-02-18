-- <?xml version = '1.0' encoding = 'UTF-8'?>
-- <trigger xmlns="http://xmlns.oracle.com/jdeveloper/1211/offlinedb">
--   <name>CG$BDR_UIT_IDENTIFIERS</name>
--   <enabled>true</enabled>
--   <properties>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_CONNECTION</key>
--       <value class="java.lang.String">igs@hardy</value>
--     </entry>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_ID</key>
--       <value class="oracle.javatools.db.ReferenceID">
--         <name>CG$BDR_UIT_IDENTIFIERS</name>
--         <identifier class="java.math.BigDecimal">46844</identifier>
--         <schemaName>IGD_IGS</schemaName>
--         <type>TRIGGER</type>
--       </value>
--     </entry>
--   </properties>
--   <statementLevel>true</statementLevel>
-- </trigger>

CREATE OR REPLACE
TRIGGER cg$bdr_uit_identifiers
BEFORE DELETE ON uit_identifiers FOR EACH ROW
DECLARE
  cg$pk  cg$uit_identifiers.cg$pk_type;
  cg$rec cg$uit_identifiers.cg$row_type;
  cg$ind cg$uit_identifiers.cg$ind_type;
BEGIN
  -- Application_logic Pre-Before-Delete-row <<Start>>
  -- Application_logic Pre-Before-Delete-row << End >>

  -- Load cg$rec/cg$ind values from old
  cg$pk.tnt_id  := :old.tnt_id;
  cg$rec.tnt_id := :old.tnt_id;
  cg$uit_identifiers.cg$table(cg$uit_identifiers.idx).tnt_id := :old.tnt_id;
  cg$pk.typ_id  := :old.typ_id;
  cg$rec.typ_id := :old.typ_id;
  cg$uit_identifiers.cg$table(cg$uit_identifiers.idx).typ_id := :old.typ_id;
  cg$pk.ext_id  := :old.ext_id;
  cg$rec.ext_id := :old.ext_id;
  cg$uit_identifiers.cg$table(cg$uit_identifiers.idx).ext_id := :old.ext_id;

  cg$uit_identifiers.idx := cg$uit_identifiers.idx + 1;
  IF NOT (cg$uit_identifiers.called_from_package)
  THEN
    cg$uit_identifiers.del(cg$pk, FALSE);
    cg$uit_identifiers.called_from_package := FALSE;
  END IF;

  -- Application_logic Post-Before-Delete-row <<Start>>
  -- Application_logic Post-Before-Delete-row << End >>
END;
/
