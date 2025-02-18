-- /Users/dsteding/Project/Bundeskriminalamt12c/ExternalIdentityManagement/IdentityService/governanceBackend/src/main/resources/install/adm/api/igs_usr.pkb
--
-- Generated for Oracle Database 12c on Fri Mar 11 12.47.21 2022 by Server Generator 10.1.2.93.10

PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating API Package Body Identity Governance User Administration
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
--------------------------------------------------------------------------------
-- Name:        igs$user
-- Description: API package definitions for Identity Governance User Administration
--------------------------------------------------------------------------------
CREATE OR REPLACE PACKAGE BODY igs$user
IS
  PROCEDURE select_user(p_username IN VARCHAR2, cg$rec IN OUT cg$igt_users.cg$row_type)
  IS
  BEGIN
    SELECT id
    ,      rowid
    INTO   cg$rec.id
    ,      cg$rec.the_rowid
    FROM   igt_users
    WHERE  username = p_username;
  END select_user;

  ------------------------------------------------------------------------------
  -- Name:        create_entity
  --
  -- Description: Create a user
  --
  -- Parameters:  p_username  The name of the user to login
  --              p_firstname The firstname of the user to create
  --              p_lastname  The lastname of the user to create
  --              p_language  The preferred language of the user to create
  --              p_email     The e-mail Address  of the user to create
  --              p_phone     The telephone number  of the user to create
  --              p_mobile    The mobile phone number  of the user to create
  ------------------------------------------------------------------------------
  PROCEDURE create_entity( p_username IN VARCHAR2
                         , p_firstname IN VARCHAR2
                         , p_lastname IN VARCHAR2
                         , p_language IN VARCHAR2
                         , p_email    IN VARCHAR2
                         , p_phone    IN VARCHAR2
                         , p_mobile   IN VARCHAR2 
                         )
  IS
    cg$rec cg$igt_users.cg$row_type;
    cg$ind cg$igt_users.cg$ind_type;
  BEGIN
    cg$ind.id := FALSE;
    --
    cg$rec.username   := p_username;
    cg$rec.active     := 1;
    cg$rec.credential := 'changeit';
    cg$rec.firstname  := p_firstname;
    cg$rec.lastname   := p_lastname;
    cg$rec.language   := p_language;
    cg$rec.email      := p_email;
    cg$rec.phone      := p_phone;
    cg$rec.mobile     := p_mobile;
    --
    cg$igt_users.ins(cg$rec, cg$ind);
  END create_entity;

  ------------------------------------------------------------------------------
  -- Name:        delete_entity
  --
  -- Description: Delete a user
  --
  -- Parameters:  p_username The name of the user to login
  ------------------------------------------------------------------------------
  PROCEDURE delete_entity(p_username IN VARCHAR2)
  IS
    cg$pk  cg$igt_users.cg$pk_type;
  BEGIN
    SELECT id
    ,      rowid
    INTO   cg$pk.id
    ,      cg$pk.the_rowid
    FROM   igt_users
    WHERE  username = p_username;
    --
    cg$igt_users.del(cg$pk);
  END delete_entity;

  ------------------------------------------------------------------------------
  -- Name:        enable_entity
  --
  -- Description: Enable a user
  --
  -- Parameters:  p_username The name of the user to enable
  ------------------------------------------------------------------------------
  PROCEDURE enable_entity(p_username IN VARCHAR2)
  IS
    cg$rec cg$igt_users.cg$row_type;
    cg$ind cg$igt_users.cg$ind_type;
  BEGIN
    select_user(p_username, cg$rec);
    --
    cg$ind.active := TRUE;
    cg$rec.active := 1;
    cg$igt_users.upd(cg$rec, cg$ind);
  END enable_entity;

  ------------------------------------------------------------------------------
  -- Name:        disable_entity
  --
  -- Description: Delete a user
  --
  -- Parameters:  p_username The name of the user to disable
  ------------------------------------------------------------------------------
  PROCEDURE disable_entity(p_username IN VARCHAR2)
  IS
    cg$rec cg$igt_users.cg$row_type;
    cg$ind cg$igt_users.cg$ind_type;
  BEGIN
    select_user(p_username, cg$rec);
    --
    cg$ind.active := TRUE;
    cg$rec.active := 0;
    cg$igt_users.upd(cg$rec, cg$ind);
  END disable_entity;

  ------------------------------------------------------------------------------
  -- Name:        assign_role
  --
  -- Description: Assign a role to a user.
  --
  -- Parameters:  p_username The name of the user to assign to a role.
  --              p_rolename The name of the role to assign to a user.
  ------------------------------------------------------------------------------
  PROCEDURE assign_role(p_username IN VARCHAR2, p_rolename IN VARCHAR2)
  IS
    cg$rec_usr cg$igt_users.cg$row_type;
    --
    cg$rec_url cg$igt_userroles.cg$row_type;
    cg$ind_url cg$igt_userroles.cg$ind_type;
  BEGIN
    select_user(p_username, cg$rec_usr);
    --
    cg$rec_url.usr_id := cg$rec_usr.id;
    cg$rec_url.rol_id := p_rolename;
    --
    cg$igt_userroles.ins(cg$rec_url, cg$ind_url);
  END assign_role;

  ------------------------------------------------------------------------------
  -- Name:        revoke_role
  --
  -- Description: Revoke a role from a user.
  --
  -- Parameters:  p_username The name of the user to revoke from a role.
  --              p_rolename The name of the role to revoke from  a user.
  ------------------------------------------------------------------------------
  PROCEDURE revoke_role(p_username IN VARCHAR2, p_rolename IN VARCHAR2)
  IS
    cg$rec_usr cg$igt_users.cg$row_type;
    cg$pk_url cg$igt_userroles.cg$pk_type;
  BEGIN
    select_user(p_username, cg$rec_usr);
    --
    cg$pk_url.usr_id := cg$rec_usr.id;
    cg$pk_url.rol_id := p_rolename;
    --
    cg$igt_userroles.del(cg$pk_url);
  END revoke_role;

  ------------------------------------------------------------------------------
  -- Name:        assign_tenant
  --
  -- Description: Assign a role in a tennat to a user.
  --
  -- Parameters:  p_username   The name of the user to assign to a tenant.
  --              p_tenantname The name of the tenant to assign to a user.
  --              p_rolename   The name of the role to assign to user in a tenant.
  ------------------------------------------------------------------------------
  PROCEDURE assign_tenant(p_username IN VARCHAR2, p_tenantname IN VARCHAR2, p_rolename IN VARCHAR2)
  IS
    cg$rec_usr cg$igt_users.cg$row_type;
  BEGIN
    select_user(p_username, cg$rec_usr);
    --
    select_user(p_username, cg$rec_usr);
    --
    NULL;
  END assign_tenant;

  ------------------------------------------------------------------------------
  -- Name:        revoke_tenant
  --
  -- Description: Revoke a role in a tennat from a user.
  --
  -- Parameters:  p_username   The name of the user to revoke in a tenant.
  --              p_tenantname The name of the tenant to revoke a user.
  --              p_rolename   The name of the role to revoke from user in a tenant.
  ------------------------------------------------------------------------------
  PROCEDURE revoke_tenant(p_username IN VARCHAR2, p_tenantname IN VARCHAR2, p_rolename IN VARCHAR2)
  IS
    cg$rec_usr cg$igt_users.cg$row_type;
  BEGIN
    select_user(p_username, cg$rec_usr);
    --
    NULL;
  END revoke_tenant;
END igs$user;
/
SHOW ERROR