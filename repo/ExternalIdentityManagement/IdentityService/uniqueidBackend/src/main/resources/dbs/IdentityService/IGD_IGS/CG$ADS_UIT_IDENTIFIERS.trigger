-- <?xml version = '1.0' encoding = 'UTF-8'?>
-- <trigger xmlns="http://xmlns.oracle.com/jdeveloper/1211/offlinedb">
--   <name>CG$ADS_UIT_IDENTIFIERS</name>
--   <enabled>true</enabled>
--   <properties>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_CONNECTION</key>
--       <value class="java.lang.String">igs@hardy</value>
--     </entry>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_ID</key>
--       <value class="oracle.javatools.db.ReferenceID">
--         <name>CG$ADS_UIT_IDENTIFIERS</name>
--         <identifier class="java.math.BigDecimal">46845</identifier>
--         <schemaName>IGD_IGS</schemaName>
--         <type>TRIGGER</type>
--       </value>
--     </entry>
--   </properties>
--   <statementLevel>true</statementLevel>
-- </trigger>

CREATE OR REPLACE
TRIGGER cg$ads_uit_identifiers
AFTER DELETE ON uit_identifiers
DECLARE
  idx        BINARY_INTEGER := cg$uit_identifiers.cg$table.FIRST;
  cg$rec     cg$uit_identifiers.cg$row_type;
  cg$old_rec cg$uit_identifiers.cg$row_type;
BEGIN
  -- Application_logic Pre-After-Delete-statement <<Start>>
  -- Application_logic Pre-After-Delete-statement << End >>

  IF NOT (cg$uit_identifiers.called_from_package)
  THEN
    WHILE idx IS NOT NULL
    LOOP
      cg$rec.tnt_id := cg$uit_identifiers.cg$table(idx).tnt_id;
      cg$uit_identifiers.cg$tableind(idx).tnt_id := TRUE;

      cg$rec.typ_id := cg$uit_identifiers.cg$table(idx).typ_id;
      cg$uit_identifiers.cg$tableind(idx).typ_id := TRUE;

      cg$rec.ext_id := cg$uit_identifiers.cg$table(idx).ext_id;
      cg$uit_identifiers.cg$tableind(idx).ext_id := TRUE;

      cg$rec.rowversion := cg$uit_identifiers.cg$table(idx).rowversion;
      cg$uit_identifiers.cg$tableind(idx).rowversion := TRUE;

      cg$rec.created_by := cg$uit_identifiers.cg$table(idx).created_by;
      cg$uit_identifiers.cg$tableind(idx).created_by := TRUE;

      cg$rec.created_on := cg$uit_identifiers.cg$table(idx).created_on;
      cg$uit_identifiers.cg$tableind(idx).created_on := TRUE;

      cg$rec.updated_by := cg$uit_identifiers.cg$table(idx).updated_by;
      cg$uit_identifiers.cg$tableind(idx).updated_by := TRUE;

      cg$rec.updated_on := cg$uit_identifiers.cg$table(idx).updated_on;
      cg$uit_identifiers.cg$tableind(idx).updated_on := TRUE;

      cg$uit_identifiers.validate_foreign_keys_del(cg$rec);
      cg$uit_identifiers.cascade_delete(cg$rec);

      cg$rec.state := cg$uit_identifiers.cg$table(idx).state;
      cg$uit_identifiers.cg$tableind(idx).state := TRUE;

      idx := cg$uit_identifiers.cg$table.NEXT(idx);
    END LOOP;
  END IF;

  -- Application_logic Post-After-Delete-statement <<Start>>
  -- Application_logic Post-After-Delete-statement << End >>
END;
/
