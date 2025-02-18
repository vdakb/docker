-- <?xml version = '1.0' encoding = 'UTF-8'?>
-- <trigger xmlns="http://xmlns.oracle.com/jdeveloper/1211/offlinedb">
--   <name>CG$AIS_UIT_TYPES</name>
--   <enabled>true</enabled>
--   <properties>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_CONNECTION</key>
--       <value class="java.lang.String">igs@hardy</value>
--     </entry>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_ID</key>
--       <value class="oracle.javatools.db.ReferenceID">
--         <name>CG$AIS_UIT_TYPES</name>
--         <identifier class="java.math.BigDecimal">46821</identifier>
--         <schemaName>IGD_IGS</schemaName>
--         <type>TRIGGER</type>
--       </value>
--     </entry>
--   </properties>
--   <statementLevel>true</statementLevel>
-- </trigger>

CREATE OR REPLACE
TRIGGER cg$ais_uit_types
AFTER INSERT ON uit_types
DECLARE
  fk_check   INTEGER;
  idx        BINARY_INTEGER := cg$uit_types.cg$table.FIRST;
  cg$rec     cg$uit_types.cg$row_type;
  cg$old_rec cg$uit_types.cg$row_type;
BEGIN
  -- Application_logic Pre-After-Insert-statement <<Start>>
  -- Application_logic Pre-After-Insert-statement << End >>

  IF NOT (cg$uit_types.called_from_package)
  THEN
    WHILE idx IS NOT NULL
    LOOP
      cg$rec.id             := cg$uit_types.cg$table(idx).id;
      cg$rec.rowversion     := cg$uit_types.cg$table(idx).rowversion;
      cg$rec.created_by     := cg$uit_types.cg$table(idx).created_by;
      cg$rec.created_on     := cg$uit_types.cg$table(idx).created_on;
      cg$rec.updated_by     := cg$uit_types.cg$table(idx).updated_by;
      cg$rec.updated_on     := cg$uit_types.cg$table(idx).updated_on;
      cg$rec.name           := cg$uit_types.cg$table(idx).name;
      cg$rec.description    := cg$uit_types.cg$table(idx).description;

      cg$uit_types.validate_foreign_keys_ins(cg$rec);

      idx := cg$uit_types.cg$table.NEXT(idx);
    END LOOP;
  END IF;

  -- Application_logic Post-After-Insert-statement <<Start>>
  -- Application_logic Post-After-Insert-statement << End >>
END;
/
