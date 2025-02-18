-- <?xml version = '1.0' encoding = 'UTF-8'?>
-- <trigger xmlns="http://xmlns.oracle.com/jdeveloper/1211/offlinedb">
--   <name>CG$AUS_UIT_TYPES</name>
--   <enabled>true</enabled>
--   <properties>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_CONNECTION</key>
--       <value class="java.lang.String">igs@hardy</value>
--     </entry>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_ID</key>
--       <value class="oracle.javatools.db.ReferenceID">
--         <name>CG$AUS_UIT_TYPES</name>
--         <identifier class="java.math.BigDecimal">46824</identifier>
--         <schemaName>IGD_IGS</schemaName>
--         <type>TRIGGER</type>
--       </value>
--     </entry>
--   </properties>
--   <statementLevel>true</statementLevel>
-- </trigger>

CREATE OR REPLACE
TRIGGER cg$aus_uit_types
AFTER UPDATE ON uit_types
DECLARE
  idx        BINARY_INTEGER := cg$uit_types.cg$table.FIRST;
  cg$old_rec cg$uit_types.cg$row_type;
  cg$rec     cg$uit_types.cg$row_type;
  cg$ind     cg$uit_types.cg$ind_type;
BEGIN
  -- Application_logic Pre-After-Update-statement <<Start>>
  -- Application_logic Pre-After-Update-statement << End >>

  WHILE idx IS NOT NULL
  LOOP
    cg$old_rec.id             := cg$uit_types.cg$table(idx).id;
    cg$old_rec.rowversion     := cg$uit_types.cg$table(idx).rowversion;
    cg$old_rec.created_by     := cg$uit_types.cg$table(idx).created_by;
    cg$old_rec.created_on     := cg$uit_types.cg$table(idx).created_on;
    cg$old_rec.updated_by     := cg$uit_types.cg$table(idx).updated_by;
    cg$old_rec.updated_on     := cg$uit_types.cg$table(idx).updated_on;
    cg$old_rec.name           := cg$uit_types.cg$table(idx).name;
    cg$old_rec.description    := cg$uit_types.cg$table(idx).description;

    IF NOT (cg$uit_types.called_from_package)
    THEN
      idx                   := cg$uit_types.cg$table.NEXT(idx);
      cg$rec.id             := cg$uit_types.cg$table(idx).id;
      cg$ind.id             := updating('id');
      cg$rec.rowversion     := cg$uit_types.cg$table(idx).rowversion;
      cg$ind.rowversion     := updating('rowversion');
      cg$rec.created_by     := cg$uit_types.cg$table(idx).created_by;
      cg$ind.created_by     := updating('created_by');
      cg$rec.created_on     := cg$uit_types.cg$table(idx).created_on;
      cg$ind.created_on     := updating('created_on');
      cg$rec.updated_by     := cg$uit_types.cg$table(idx).updated_by;
      cg$ind.updated_by     := updating('updated_by');
      cg$rec.updated_on     := cg$uit_types.cg$table(idx).updated_on;
      cg$ind.updated_on     := updating('updated_on');
      cg$rec.name           := cg$uit_types.cg$table(idx).name;
      cg$ind.name           := updating('name');
      cg$rec.description    := cg$uit_types.cg$table(idx).description;
      cg$ind.description    := updating('description');

      cg$uit_types.validate_foreign_keys_upd(cg$rec, cg$old_rec, cg$ind);
      cg$uit_types.cascade_update(cg$rec, cg$old_rec);

      cg$uit_types.called_from_package := FALSE;
    END IF;
    idx := cg$uit_types.cg$table.NEXT(idx);
  END LOOP;

  cg$uit_types.cg$table.DELETE;

  -- Application_logic Post-After-Update-statement <<Start>>
  -- Application_logic Post-After-Update-statement << End >>
END;
/
