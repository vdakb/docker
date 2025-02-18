-- <?xml version = '1.0' encoding = 'UTF-8'?>
-- <trigger xmlns="http://xmlns.oracle.com/jdeveloper/1211/offlinedb">
--   <name>CG$AUS_UIT_CLAIMS</name>
--   <enabled>true</enabled>
--   <properties>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_CONNECTION</key>
--       <value class="java.lang.String">igs@hardy</value>
--     </entry>
--     <entry>
--       <key>OfflineDBConstants.IMPORT_SOURCE_ID</key>
--       <value class="oracle.javatools.db.ReferenceID">
--         <name>CG$AUS_UIT_CLAIMS</name>
--         <identifier class="java.math.BigDecimal">46908</identifier>
--         <schemaName>IGD_IGS</schemaName>
--         <type>TRIGGER</type>
--       </value>
--     </entry>
--   </properties>
--   <statementLevel>true</statementLevel>
-- </trigger>

CREATE OR REPLACE
TRIGGER cg$aus_uit_claims
AFTER UPDATE ON uit_claims
DECLARE
  idx        BINARY_INTEGER := cg$uit_claims.cg$table.FIRST;
  cg$old_rec cg$uit_claims.cg$row_type;
  cg$rec     cg$uit_claims.cg$row_type;
  cg$ind     cg$uit_claims.cg$ind_type;
BEGIN
  -- Application_logic Pre-After-Update-statement <<Start>>
  -- Application_logic Pre-After-Update-statement << End >>

  WHILE idx IS NOT NULL
  LOOP
    cg$old_rec.tnt_id     := cg$uit_claims.cg$table(idx).tnt_id;
    cg$old_rec.rol_id     := cg$uit_claims.cg$table(idx).rol_id;
    cg$old_rec.usr_id     := cg$uit_claims.cg$table(idx).usr_id;
    cg$old_rec.rowversion := cg$uit_claims.cg$table(idx).rowversion;
    cg$old_rec.created_by := cg$uit_claims.cg$table(idx).created_by;
    cg$old_rec.created_on := cg$uit_claims.cg$table(idx).created_on;
    cg$old_rec.updated_by := cg$uit_claims.cg$table(idx).updated_by;
    cg$old_rec.updated_on := cg$uit_claims.cg$table(idx).updated_on;

    IF NOT (cg$uit_claims.called_from_package)
    THEN
      idx                 := cg$uit_claims.cg$table.NEXT(idx);
      cg$rec.tnt_id       := cg$uit_claims.cg$table(idx).tnt_id;
      cg$ind.tnt_id       := updating('tnt_id');
      cg$rec.rol_id       := cg$uit_claims.cg$table(idx).rol_id;
      cg$ind.rol_id       := updating('rol_id');
      cg$rec.usr_id       := cg$uit_claims.cg$table(idx).usr_id;
      cg$ind.usr_id       := updating('usr_id');
      cg$rec.rowversion   := cg$uit_claims.cg$table(idx).rowversion;
      cg$ind.rowversion   := updating('rowversion');
      cg$rec.created_by   := cg$uit_claims.cg$table(idx).created_by;
      cg$ind.created_by   := updating('created_by');
      cg$rec.created_on   := cg$uit_claims.cg$table(idx).created_on;
      cg$ind.created_on   := updating('created_on');
      cg$rec.updated_by   := cg$uit_claims.cg$table(idx).updated_by;
      cg$ind.updated_by   := updating('updated_by');
      cg$rec.updated_on   := cg$uit_claims.cg$table(idx).updated_on;
      cg$ind.updated_on   := updating('updated_on');

      cg$uit_claims.validate_foreign_keys_upd(cg$rec, cg$old_rec, cg$ind);
      cg$uit_claims.cascade_update(cg$rec, cg$old_rec);

      cg$uit_claims.called_from_package := FALSE;
    END IF;
    idx := cg$uit_claims.cg$table.NEXT(idx);
  END LOOP;

  cg$uit_claims.cg$table.DELETE;

  -- Application_logic Post-After-Update-statement <<Start>>
  -- Application_logic Post-After-Update-statement << End >>
END;
/
