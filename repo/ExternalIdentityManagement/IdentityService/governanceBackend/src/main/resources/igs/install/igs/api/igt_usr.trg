-- /Users/dsteding/Project/Bundeskriminalamt12c/ExternalIdentityManagement/IdentityService/governanceBackend/src/main/resources/install/adm/api/igt_usr.trg
--
-- Generated for Oracle Database 12c on Fri Mar 11 12.47.21 2022 by Server Generator 10.1.2.93.10

PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating Trigger Logic for Table 'igt_users'
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating Before Insert Statement Trigger on 'igt_users'
CREATE OR REPLACE TRIGGER cg$bis_igt_users
BEFORE INSERT ON igt_users
BEGIN
  -- Application_logic Pre-Before-Insert-statement <<Start>>
  -- Application_logic Pre-Before-Insert-statement << End >>

  cg$igt_users.cg$table.DELETE;
  cg$igt_users.cg$tableind.DELETE;
  cg$igt_users.idx := 1;

  -- Application_logic Post-Before-Insert-statement <<Start>>
  -- Application_logic Post-Before-Insert-statement << End >>
END;
/
SHOW ERROR
PROMPT Creating Before Insert Row Trigger on 'igt_users'
CREATE OR REPLACE TRIGGER cg$bir_igt_users
BEFORE INSERT ON igt_users FOR EACH ROW
DECLARE
  cg$rec cg$igt_users.cg$row_type;
  cg$ind cg$igt_users.cg$ind_type;
BEGIN
  -- Application_logic Pre-Before-Insert-row <<Start>>
  -- Application_logic Pre-Before-Insert-row << End >>

  -- Load cg$rec/cg$ind values from new
  cg$rec.id := :new.id;
  IF (:new.id IS NULL)
  THEN
    cg$ind.id := FALSE;
  ELSE
    cg$ind.id := TRUE;
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
  cg$rec.active     := :new.active;
  cg$ind.active     := TRUE;
  cg$rec.username   := :new.username;
  cg$ind.username   := TRUE;
  cg$rec.credential := :new.credential;
  cg$ind.credential := TRUE;
  cg$rec.lastname   := :new.lastname;
  cg$ind.lastname   := TRUE;
  cg$rec.firstname  := :new.firstname;
  cg$ind.firstname  := TRUE;
  cg$rec.language   := :new.language;
  cg$ind.language   := TRUE;
  cg$rec.email      := :new.email;
  cg$ind.email      := TRUE;
  cg$rec.phone      := :new.phone;
  cg$ind.phone      := TRUE;
  cg$rec.mobile     := :new.mobile;
  cg$ind.mobile     := TRUE;

  IF NOT (cg$igt_users.called_from_package)
  THEN
    cg$igt_users.ins(cg$rec, cg$ind, FALSE);
    cg$igt_users.called_from_package := FALSE;
  END IF;

  cg$igt_users.cg$table(cg$igt_users.idx).id            := cg$rec.id;
  cg$igt_users.cg$tableind(cg$igt_users.idx).id         := cg$ind.id;

  cg$igt_users.cg$table(cg$igt_users.idx).rowversion    := cg$rec.rowversion;
  cg$igt_users.cg$tableind(cg$igt_users.idx).rowversion := cg$ind.rowversion;

  cg$igt_users.cg$table(cg$igt_users.idx).created_by    := cg$rec.created_by;
  cg$igt_users.cg$tableind(cg$igt_users.idx).created_by := cg$ind.created_by;

  cg$igt_users.cg$table(cg$igt_users.idx).created_on    := cg$rec.created_on;
  cg$igt_users.cg$tableind(cg$igt_users.idx).created_on := cg$ind.created_on;

  cg$igt_users.cg$table(cg$igt_users.idx).updated_by    := cg$rec.updated_by;
  cg$igt_users.cg$tableind(cg$igt_users.idx).updated_by := cg$ind.updated_by;

  cg$igt_users.cg$table(cg$igt_users.idx).updated_on    := cg$rec.updated_on;
  cg$igt_users.cg$tableind(cg$igt_users.idx).updated_on := cg$ind.updated_on;

  cg$igt_users.cg$table(cg$igt_users.idx).active        := cg$rec.active;
  cg$igt_users.cg$tableind(cg$igt_users.idx).active     := cg$ind.active;

  cg$igt_users.cg$table(cg$igt_users.idx).username      := cg$rec.username;
  cg$igt_users.cg$tableind(cg$igt_users.idx).username   := cg$ind.username;

  cg$igt_users.cg$table(cg$igt_users.idx).credential    := cg$rec.credential;
  cg$igt_users.cg$tableind(cg$igt_users.idx).credential := cg$ind.credential;

  cg$igt_users.cg$table(cg$igt_users.idx).lastname      := cg$rec.lastname;
  cg$igt_users.cg$tableind(cg$igt_users.idx).lastname   := cg$ind.lastname;

  cg$igt_users.cg$table(cg$igt_users.idx).firstname     := cg$rec.firstname;
  cg$igt_users.cg$tableind(cg$igt_users.idx).firstname  := cg$ind.firstname;

  cg$igt_users.cg$table(cg$igt_users.idx).language      := cg$rec.language;
  cg$igt_users.cg$tableind(cg$igt_users.idx).language   := cg$ind.language;

  cg$igt_users.cg$table(cg$igt_users.idx).email         := cg$rec.email;
  cg$igt_users.cg$tableind(cg$igt_users.idx).email      := cg$ind.email;

  cg$igt_users.cg$table(cg$igt_users.idx).phone         := cg$rec.phone;
  cg$igt_users.cg$tableind(cg$igt_users.idx).phone      := cg$ind.phone;

  cg$igt_users.cg$table(cg$igt_users.idx).mobile        := cg$rec.mobile;
  cg$igt_users.cg$tableind(cg$igt_users.idx).mobile     := cg$ind.mobile;

  cg$igt_users.idx := cg$igt_users.idx + 1;

  :new.id         := cg$rec.id;
  :new.rowversion := cg$rec.rowversion;
  :new.created_by := cg$rec.created_by;
  :new.created_on := cg$rec.created_on;
  :new.updated_by := cg$rec.updated_by;
  :new.updated_on := cg$rec.updated_on;
  :new.active     := cg$rec.active;
  :new.username   := cg$rec.username;
  :new.credential := cg$rec.credential;
  :new.lastname   := cg$rec.lastname;
  :new.firstname  := cg$rec.firstname;
  :new.language   := cg$rec.language;
  :new.email      := cg$rec.email;
  :new.phone      := cg$rec.phone;
  :new.mobile     := cg$rec.mobile;

  -- Application_logic Post-Before-Insert-row <<Start>>
  -- Application_logic Post-Before-Insert-row << End >>
END;
/
SHOW ERROR
PROMPT Creating After Insert Row Trigger on 'igt_users'
CREATE OR REPLACE TRIGGER cg$air_igt_users
AFTER INSERT ON igt_users FOR EACH ROW
DECLARE
  cg$rec cg$igt_users.cg$row_type;
  cg$ind cg$igt_users.cg$ind_type;
BEGIN
  --  Application_logic Pre-After-Insert-row <<Start>>
  --  Application_logic Pre-After-Insert-row << End >>

  cg$rec.id           := :new.id;
  cg$ind.id           := TRUE;
  cg$rec.rowversion   := :new.created_by;
  cg$ind.rowversion   := TRUE;
  cg$rec.created_by   := :new.created_by;
  cg$ind.created_by   := TRUE;
  cg$rec.created_on   := :new.created_on;
  cg$ind.created_on   := TRUE;
  cg$rec.updated_by   := :new.updated_by;
  cg$ind.updated_by   := TRUE;
  cg$rec.updated_on   := :new.updated_on;
  cg$ind.updated_on   := TRUE;
  cg$rec.active       := :new.active;
  cg$ind.active       := TRUE;
  cg$rec.username     := :new.username;
  cg$ind.username     := TRUE;
  cg$rec.credential   := :new.credential;
  cg$ind.credential   := TRUE;
  cg$rec.firstname    := :new.firstname;
  cg$ind.firstname    := TRUE;
  cg$rec.language     := :new.language;
  cg$ind.language     := TRUE;
  cg$rec.email        := :new.email;
  cg$ind.email        := TRUE;
  cg$rec.phone        := :new.phone;
  cg$ind.phone        := TRUE;
  cg$rec.mobile       := :new.mobile;
  cg$ind.mobile       := TRUE;

  --  Application_logic Post-After-Insert-row <<Start>>
  --  Application_logic Post-After-Insert-row << End >>
END;
/
SHOW ERROR
PROMPT Creating After Insert Statement Trigger on 'igt_users'
CREATE OR REPLACE TRIGGER cg$ais_igt_users
AFTER INSERT ON igt_users
DECLARE
  fk_check   INTEGER;
  idx        BINARY_INTEGER := cg$igt_users.cg$table.FIRST;
  cg$rec     cg$igt_users.cg$row_type;
  cg$old_rec cg$igt_users.cg$row_type;
BEGIN
  -- Application_logic Pre-After-Insert-statement <<Start>>
  -- Application_logic Pre-After-Insert-statement << End >>

  IF NOT (cg$igt_users.called_from_package)
  THEN
    WHILE idx IS NOT NULL
    LOOP
      cg$rec.id         := cg$igt_users.cg$table(idx).id;
      cg$rec.rowversion := cg$igt_users.cg$table(idx).rowversion;
      cg$rec.created_by := cg$igt_users.cg$table(idx).created_by;
      cg$rec.created_on := cg$igt_users.cg$table(idx).created_on;
      cg$rec.updated_by := cg$igt_users.cg$table(idx).updated_by;
      cg$rec.updated_on := cg$igt_users.cg$table(idx).updated_on;
      cg$rec.active     := cg$igt_users.cg$table(idx).active;
      cg$rec.username   := cg$igt_users.cg$table(idx).username;
      cg$rec.credential := cg$igt_users.cg$table(idx).credential;
      cg$rec.lastname   := cg$igt_users.cg$table(idx).lastname;
      cg$rec.firstname  := cg$igt_users.cg$table(idx).firstname;
      cg$rec.language   := cg$igt_users.cg$table(idx).language;
      cg$rec.email      := cg$igt_users.cg$table(idx).email;
      cg$rec.phone      := cg$igt_users.cg$table(idx).phone;
      cg$rec.mobile     := cg$igt_users.cg$table(idx).mobile;

      cg$igt_users.validate_foreign_keys_ins(cg$rec);

      idx := cg$igt_users.cg$table.NEXT(idx);
    END LOOP;
  END IF;

  -- Application_logic Post-After-Insert-statement <<Start>>
  -- Application_logic Post-After-Insert-statement << End >>
END;
/
SHOW ERROR
PROMPT Creating Before Update Statement Trigger on 'igt_users'
CREATE OR REPLACE TRIGGER cg$bus_igt_users
BEFORE UPDATE ON igt_users
BEGIN
  -- Application_logic Pre-Before-Update-statement <<Start>>
  -- Application_logic Pre-Before-Update-statement << End >>

  cg$igt_users.cg$table.DELETE;
  cg$igt_users.cg$tableind.DELETE;
  cg$igt_users.idx := 1;

  -- Application_logic Post-Before-Update-statement <<Start>>
  -- Application_logic Post-Before-Update-statement << End >>
END;
/
SHOW ERROR
PROMPT Creating Before Update Row Trigger on 'igt_users'
CREATE OR REPLACE TRIGGER cg$bur_igt_users
BEFORE UPDATE ON igt_users FOR EACH ROW
DECLARE
  cg$rec     cg$igt_users.cg$row_type;
  cg$ind     cg$igt_users.cg$ind_type;
  cg$old_rec cg$igt_users.cg$row_type;
BEGIN
  -- Application_logic Pre-Before-Update-row <<Start>>
  -- Application_logic Pre-Before-Update-row << End >>

  -- Load cg$rec/cg$ind values from new
  cg$rec.id := :new.id;
  cg$ind.id := (:new.id IS NULL AND :old.id IS NOT NULL)
            OR (:old.id IS NULL AND :new.id IS NOT NULL)
            OR NOT(:new.id = :old.id);
  cg$igt_users.cg$table(cg$igt_users.idx).id := :old.id;

  cg$rec.rowversion := :new.rowversion;
  cg$ind.rowversion := (:new.rowversion IS NULL AND :old.rowversion IS NOT NULL)
                    OR (:old.rowversion IS NULL AND :new.rowversion IS NOT NULL)
                    OR NOT(:new.rowversion = :old.rowversion);
  cg$igt_users.cg$table(cg$igt_users.idx).rowversion := :old.rowversion;

  cg$rec.created_by := :new.created_by;
  cg$ind.created_by := (:new.created_by IS NULL AND :old.created_by IS NOT NULL)
                    OR (:old.created_by IS NULL AND :new.created_by IS NOT NULL)
                    OR NOT(:new.created_by = :old.created_by);
  cg$igt_users.cg$table(cg$igt_users.idx).created_by := :old.created_by;

  cg$rec.created_on := :new.created_on;
  cg$ind.created_on := (:new.created_on IS NULL AND :old.created_on IS NOT NULL)
                    OR (:old.created_on IS NULL AND :new.created_on IS NOT NULL)
                    OR NOT(:new.created_on = :old.created_on);
  cg$igt_users.cg$table(cg$igt_users.idx).created_on := :old.created_on;

  cg$rec.updated_by := :new.updated_by;
  cg$ind.updated_by := (:new.updated_by IS NULL AND :old.updated_by IS NOT NULL)
                    OR (:old.updated_by IS NULL AND :new.updated_by IS NOT NULL)
                    OR NOT(:new.updated_by = :old.updated_by);
  cg$igt_users.cg$table(cg$igt_users.idx).updated_by := :old.updated_by;

  cg$rec.updated_on := :new.updated_on;
  cg$ind.updated_on := (:new.updated_on IS NULL AND :old.updated_on IS NOT NULL)
                    OR (:old.updated_on IS NULL AND :new.updated_on IS NOT NULL)
                    OR NOT(:new.updated_on = :old.updated_on);
  cg$igt_users.cg$table(cg$igt_users.idx).updated_on := :old.updated_on;

  cg$rec.active := :new.active;
  cg$ind.active := (:new.active IS NULL AND :old.active IS NOT NULL)
                OR (:old.active IS NULL AND :new.active IS NOT NULL)
                OR NOT(:new.active = :old.active);
  cg$igt_users.cg$table(cg$igt_users.idx).active := :old.active;

  cg$rec.username := :new.username;
  cg$ind.username := (:new.username IS NULL AND :old.username IS NOT NULL)
                  OR (:old.username IS NULL AND :new.username IS NOT NULL)
                  OR NOT(:new.username = :old.username);
  cg$igt_users.cg$table(cg$igt_users.idx).username := :old.username;

  cg$rec.credential := :new.credential;
  cg$ind.credential := (:new.credential IS NULL AND :old.credential IS NOT NULL)
                    OR (:old.credential IS NULL AND :new.credential IS NOT NULL)
                    OR NOT(:new.credential = :old.credential);
  cg$igt_users.cg$table(cg$igt_users.idx).credential := :old.credential;

  cg$rec.lastname := :new.lastname;
  cg$ind.lastname := (:new.lastname IS NULL AND :old.lastname IS NOT NULL)
                  OR (:old.lastname IS NULL AND :new.lastname IS NOT NULL)
                  OR NOT(:new.lastname = :old.lastname);
  cg$igt_users.cg$table(cg$igt_users.idx).lastname := :old.lastname;

  cg$rec.firstname := :new.firstname;
  cg$ind.firstname := (:new.firstname IS NULL AND :old.firstname IS NOT NULL)
                   OR (:old.firstname IS NULL AND :new.firstname IS NOT NULL)
                   OR NOT(:new.firstname = :old.firstname);
  cg$igt_users.cg$table(cg$igt_users.idx).firstname := :old.firstname;

  cg$rec.language := :new.language;
  cg$ind.language := (:new.language IS NULL AND :old.language IS NOT NULL)
                  OR (:old.language IS NULL AND :new.language IS NOT NULL)
                  OR NOT(:new.language = :old.language);
  cg$igt_users.cg$table(cg$igt_users.idx).language := :old.language;

  cg$rec.email := :new.email;
  cg$ind.email := (:new.email IS NULL AND :old.email IS NOT NULL)
               OR (:old.email IS NULL AND :new.email IS NOT NULL)
               OR NOT(:new.email = :old.email);
  cg$igt_users.cg$table(cg$igt_users.idx).email := :old.email;

  cg$rec.phone := :new.phone;
  cg$ind.phone := (:new.phone IS NULL AND :old.phone IS NOT NULL)
               OR (:old.phone IS NULL AND :new.phone IS NOT NULL)
               OR NOT(:new.phone = :old.phone);
  cg$igt_users.cg$table(cg$igt_users.idx).phone := :old.phone;

  cg$rec.mobile := :new.mobile;
  cg$ind.mobile := (:new.mobile IS NULL AND :old.mobile IS NOT NULL)
                OR (:old.mobile IS NULL AND :new.mobile IS NOT NULL)
                OR NOT(:new.mobile = :old.mobile);
  cg$igt_users.cg$table(cg$igt_users.idx).mobile := :old.mobile;

  cg$igt_users.idx := cg$igt_users.idx + 1;

  IF NOT (cg$igt_users.called_from_package)
  THEN
    cg$igt_users.upd(cg$rec, cg$ind, FALSE);
    cg$igt_users.called_from_package := FALSE;
  END IF;

  :new.rowversion := cg$rec.rowversion;
  :new.created_by := cg$rec.created_by;
  :new.created_on := cg$rec.created_on;
  :new.updated_by := cg$rec.updated_by;
  :new.updated_on := cg$rec.updated_on;
  :new.active     := cg$rec.active;
  :new.username   := cg$rec.username;
  :new.credential := cg$rec.credential;
  :new.lastname   := cg$rec.lastname;
  :new.firstname  := cg$rec.firstname;
  :new.language   := cg$rec.language;
  :new.email      := cg$rec.email;
  :new.phone      := cg$rec.phone;
  :new.mobile     := cg$rec.mobile;

  -- Application_logic Post-Before-Update-row <<Start>>
  -- Application_logic Post-Before-Update-row << End >>
END;
/
SHOW ERROR
PROMPT Creating After Update Statement Trigger on 'igt_users'
CREATE OR REPLACE TRIGGER cg$aus_igt_users
AFTER UPDATE ON igt_users
DECLARE
  idx        BINARY_INTEGER := cg$igt_users.cg$table.FIRST;
  cg$old_rec cg$igt_users.cg$row_type;
  cg$rec     cg$igt_users.cg$row_type;
  cg$ind     cg$igt_users.cg$ind_type;
BEGIN
  -- Application_logic Pre-After-Update-statement <<Start>>
  -- Application_logic Pre-After-Update-statement << End >>

  WHILE idx IS NOT NULL
  LOOP
    cg$old_rec.id         := cg$igt_users.cg$table(idx).id;
    cg$old_rec.rowversion := cg$igt_users.cg$table(idx).rowversion;
    cg$old_rec.created_by := cg$igt_users.cg$table(idx).created_by;
    cg$old_rec.created_on := cg$igt_users.cg$table(idx).created_on;
    cg$old_rec.updated_by := cg$igt_users.cg$table(idx).updated_by;
    cg$old_rec.updated_on := cg$igt_users.cg$table(idx).updated_on;
    cg$old_rec.active     := cg$igt_users.cg$table(idx).active;
    cg$old_rec.username   := cg$igt_users.cg$table(idx).username;
    cg$old_rec.credential := cg$igt_users.cg$table(idx).credential;
    cg$old_rec.lastname   := cg$igt_users.cg$table(idx).lastname;
    cg$old_rec.firstname  := cg$igt_users.cg$table(idx).firstname;
    cg$old_rec.language   := cg$igt_users.cg$table(idx).language;
    cg$old_rec.email      := cg$igt_users.cg$table(idx).email;
    cg$old_rec.phone      := cg$igt_users.cg$table(idx).phone;
    cg$old_rec.mobile     := cg$igt_users.cg$table(idx).mobile;

    IF NOT (cg$igt_users.called_from_package)
    THEN
      idx               := cg$igt_users.cg$table.NEXT(idx);
      cg$rec.id         := cg$igt_users.cg$table(idx).id;
      cg$ind.id         := updating('id');
      cg$rec.rowversion := cg$igt_users.cg$table(idx).rowversion;
      cg$ind.rowversion := updating('rowversion');
      cg$rec.created_by := cg$igt_users.cg$table(idx).created_by;
      cg$ind.created_by := updating('created_by');
      cg$rec.created_on := cg$igt_users.cg$table(idx).created_on;
      cg$ind.created_on := updating('created_on');
      cg$rec.updated_by := cg$igt_users.cg$table(idx).updated_by;
      cg$ind.updated_by := updating('updated_by');
      cg$rec.updated_on := cg$igt_users.cg$table(idx).updated_on;
      cg$ind.updated_on := updating('updated_on');
      cg$rec.active     := cg$igt_users.cg$table(idx).active;
      cg$ind.active     := updating('active');
      cg$rec.username   := cg$igt_users.cg$table(idx).username;
      cg$ind.username   := updating('username');
      cg$rec.credential := cg$igt_users.cg$table(idx).credential;
      cg$ind.credential := updating('credential');
      cg$rec.lastname   := cg$igt_users.cg$table(idx).lastname;
      cg$ind.lastname   := updating('lastname');
      cg$rec.firstname  := cg$igt_users.cg$table(idx).firstname;
      cg$ind.firstname  := updating('firstname');
      cg$rec.language   := cg$igt_users.cg$table(idx).language;
      cg$ind.language   := updating('language');
      cg$rec.email      := cg$igt_users.cg$table(idx).email;
      cg$ind.email      := updating('email');
      cg$rec.phone      := cg$igt_users.cg$table(idx).phone;
      cg$ind.phone      := updating('phone');
      cg$rec.mobile     := cg$igt_users.cg$table(idx).mobile;
      cg$ind.mobile     := updating('mobile');

      cg$igt_users.validate_foreign_keys_upd(cg$rec, cg$old_rec, cg$ind);
      cg$igt_users.cascade_update(cg$rec, cg$old_rec);

      cg$igt_users.called_from_package := FALSE;
    END IF;
    idx := cg$igt_users.cg$table.NEXT(idx);
  END LOOP;

  cg$igt_users.cg$table.DELETE;

  -- Application_logic Post-After-Update-statement <<Start>>
  -- Application_logic Post-After-Update-statement << End >>
END;
/
SHOW ERROR
PROMPT Creating Before Delete Statement Trigger on 'igt_users'
CREATE OR REPLACE TRIGGER cg$bds_igt_users
BEFORE DELETE ON igt_users
BEGIN
  -- Application_logic Pre-Before-Delete-statement <<Start>>
  -- Application_logic Pre-Before-Delete-statement << End >>

  cg$igt_users.cg$table.DELETE;
  cg$igt_users.cg$tableind.DELETE;
  cg$igt_users.idx := 1;

  -- Application_logic Post-Before-Delete-statement <<Start>>
  -- Application_logic Post-Before-Delete-statement << End >>
END;
/
SHOW ERROR
PROMPT Creating Before Delete Row Trigger on 'igt_users'
CREATE OR REPLACE TRIGGER cg$bdr_igt_users
BEFORE DELETE ON igt_users FOR EACH ROW
DECLARE
  cg$pk  cg$igt_users.cg$pk_type;
  cg$rec cg$igt_users.cg$row_type;
  cg$ind cg$igt_users.cg$ind_type;
BEGIN
  -- Application_logic Pre-Before-Delete-row <<Start>>
  -- Application_logic Pre-Before-Delete-row << End >>

  -- Load cg$rec/cg$ind values from old
  cg$pk.id  := :old.id;
  cg$rec.id := :old.id;
  cg$igt_users.cg$table(cg$igt_users.idx).id := :old.id;
  cg$rec.username := :old.username;
  cg$igt_users.cg$table(cg$igt_users.idx).username := :old.username;
  cg$rec.email := :old.email;
  cg$igt_users.cg$table(cg$igt_users.idx).email := :old.email;

  cg$igt_users.idx := cg$igt_users.idx + 1;
  IF NOT (cg$igt_users.called_from_package)
  THEN
    cg$igt_users.del(cg$pk, FALSE);
    cg$igt_users.called_from_package := FALSE;
  END IF;

  -- Application_logic Post-Before-Delete-row <<Start>>
  -- Application_logic Post-Before-Delete-row << End >>
END;
/
SHOW ERROR
PROMPT Creating After Delete Statement Trigger on 'igt_users'
CREATE OR REPLACE TRIGGER cg$ads_igt_users
AFTER DELETE ON igt_users
DECLARE
  idx        BINARY_INTEGER := cg$igt_users.cg$table.FIRST;
  cg$rec     cg$igt_users.cg$row_type;
  cg$old_rec cg$igt_users.cg$row_type;
BEGIN
  -- Application_logic Pre-After-Delete-statement <<Start>>
  -- Application_logic Pre-After-Delete-statement << End >>

  IF NOT (cg$igt_users.called_from_package)
  THEN
    WHILE idx IS NOT NULL
    LOOP
      cg$rec.id := cg$igt_users.cg$table(idx).id;
      cg$igt_users.cg$tableind(idx).id := TRUE;

      cg$rec.rowversion := cg$igt_users.cg$table(idx).rowversion;
      cg$igt_users.cg$tableind(idx).rowversion := TRUE;

      cg$rec.created_by := cg$igt_users.cg$table(idx).created_by;
      cg$igt_users.cg$tableind(idx).created_by := TRUE;

      cg$rec.created_on := cg$igt_users.cg$table(idx).created_on;
      cg$igt_users.cg$tableind(idx).created_on := TRUE;

      cg$rec.updated_by := cg$igt_users.cg$table(idx).updated_by;
      cg$igt_users.cg$tableind(idx).updated_by := TRUE;

      cg$rec.updated_on := cg$igt_users.cg$table(idx).updated_on;
      cg$igt_users.cg$tableind(idx).updated_on := TRUE;

      cg$rec.active := cg$igt_users.cg$table(idx).active;
      cg$igt_users.cg$tableind(idx).active := TRUE;

      cg$rec.username := cg$igt_users.cg$table(idx).username;
      cg$igt_users.cg$tableind(idx).username := TRUE;

      cg$rec.credential := cg$igt_users.cg$table(idx).credential;
      cg$igt_users.cg$tableind(idx).credential := TRUE;

      cg$rec.lastname := cg$igt_users.cg$table(idx).lastname;
      cg$igt_users.cg$tableind(idx).lastname := TRUE;

      cg$rec.firstname := cg$igt_users.cg$table(idx).firstname;
      cg$igt_users.cg$tableind(idx).firstname := TRUE;

      cg$rec.language := cg$igt_users.cg$table(idx).language;
      cg$igt_users.cg$tableind(idx).language := TRUE;

      cg$rec.email := cg$igt_users.cg$table(idx).email;
      cg$igt_users.cg$tableind(idx).email := TRUE;

      cg$rec.phone := cg$igt_users.cg$table(idx).phone;
      cg$igt_users.cg$tableind(idx).phone := TRUE;

      cg$rec.mobile := cg$igt_users.cg$table(idx).mobile;
      cg$igt_users.cg$tableind(idx).mobile := TRUE;

      cg$igt_users.validate_foreign_keys_del(cg$rec);
      cg$igt_users.cascade_delete(cg$rec);

      idx := cg$igt_users.cg$table.NEXT(idx);
    END LOOP;
  END IF;

  -- Application_logic Post-After-Delete-statement <<Start>>
  -- Application_logic Post-After-Delete-statement << End >>
END;
/