-- <?xml version = '1.0' encoding = 'UTF-8'?>
-- <trigger xmlns="http://xmlns.oracle.com/jdeveloper/1211/offlinedb">
--   <name>CG$AIS_UIT_IDENTIFIERS</name>
--   <enabled>true</enabled>
--   <properties>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_CONNECTION</key>
--       <value class="java.lang.String">igs@hardy</value>
--     </entry>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_ID</key>
--       <value class="oracle.javatools.db.ReferenceID">
--         <name>CG$AIS_UIT_IDENTIFIERS</name>
--         <identifier class="java.math.BigDecimal">46839</identifier>
--         <schemaName>IGD_IGS</schemaName>
--         <type>TRIGGER</type>
--       </value>
--     </entry>
--   </properties>
--   <statementLevel>true</statementLevel>
-- </trigger>

CREATE OR REPLACE
TRIGGER cg$ais_uit_identifiers
AFTER INSERT ON uit_identifiers
DECLARE
  fk_check   INTEGER;
  idx        BINARY_INTEGER := cg$uit_identifiers.cg$table.FIRST;
  cg$rec     cg$uit_identifiers.cg$row_type;
  cg$old_rec cg$uit_identifiers.cg$row_type;
BEGIN
  -- Application_logic Pre-After-Insert-statement <<Start>>
  -- Application_logic Pre-After-Insert-statement << End >>

  IF NOT (cg$uit_identifiers.called_from_package)
  THEN
    WHILE idx IS NOT NULL
    LOOP
      cg$rec.tnt_id         := cg$uit_identifiers.cg$table(idx).tnt_id;
      cg$rec.typ_id         := cg$uit_identifiers.cg$table(idx).typ_id;
      cg$rec.ext_id         := cg$uit_identifiers.cg$table(idx).ext_id;
      cg$rec.rowversion     := cg$uit_identifiers.cg$table(idx).rowversion;
      cg$rec.created_by     := cg$uit_identifiers.cg$table(idx).created_by;
      cg$rec.created_on     := cg$uit_identifiers.cg$table(idx).created_on;
      cg$rec.updated_by     := cg$uit_identifiers.cg$table(idx).updated_by;
      cg$rec.updated_on     := cg$uit_identifiers.cg$table(idx).updated_on;
      cg$rec.state          := cg$uit_identifiers.cg$table(idx).state;

      cg$uit_identifiers.validate_foreign_keys_ins(cg$rec);

      idx := cg$uit_identifiers.cg$table.NEXT(idx);
    END LOOP;
  END IF;

  -- Application_logic Post-After-Insert-statement <<Start>>
  -- Application_logic Post-After-Insert-statement << End >>
END;
/
