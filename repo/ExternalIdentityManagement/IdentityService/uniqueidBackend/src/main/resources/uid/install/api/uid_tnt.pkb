-- /Users/dsteding/Project/Bundeskriminalamt12c/ExternalIdentityManagement/IdentityService/governanceBackend/src/main/resources/install/adm/api/igs_tnt.pkb
--
-- Generated for Oracle Database 12c on Fri Mar 11 12.47.21 2022 by Server Generator 10.1.2.93.10

PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating API Package Body Identity Governance User Administration
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
--------------------------------------------------------------------------------
-- Name:        uid$tenant
-- Description: API package definitions for Identity Governance User Administration
--------------------------------------------------------------------------------
CREATE OR REPLACE PACKAGE BODY uid$tenant
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
  -- Description: Create a tenant.
  --
  -- Parameters:  p_id        The identifier of the tenant to create.
  --              p_name      The name of the tenant to create.
  ------------------------------------------------------------------------------
  PROCEDURE create_entity( p_id   IN VARCHAR2
                         , p_name IN VARCHAR2
                         )
  IS
    cg$rec cg$uit_tenants.cg$row_type;
    cg$ind cg$uit_tenants.cg$ind_type;
  BEGIN
    cg$rec.id     := p_id;
    cg$rec.active := 1;
    cg$rec.name   := p_name;
    --
    cg$uit_tenants.ins(cg$rec, cg$ind);
  END create_entity;
 ------------------------------------------------------------------------------
  -- Name:        delete_entity
  --
  -- Description: Delete a user.
  --
  -- Parameters:  p_id        The identifier of the tenant to delete.
  ------------------------------------------------------------------------------
  PROCEDURE delete_entity(p_id IN VARCHAR2)
  IS
    cg$pk  cg$uit_tenants.cg$pk_type;
  BEGIN
    cg$pk.id := p_id;
    --
    cg$uit_tenants.del(cg$pk);
  END delete_entity;

  ------------------------------------------------------------------------------
  -- Name:        enable_entity
  --
  -- Description: Enable a user.
  --
  -- Parameters:  p_id        The identifier of the tenant to enable.
  ------------------------------------------------------------------------------
  PROCEDURE enable_entity(p_id IN VARCHAR2)
  IS
    cg$rec cg$uit_tenants.cg$row_type;
    cg$ind cg$uit_tenants.cg$ind_type;
  BEGIN
    cg$ind.active := TRUE;
    cg$rec.active := 1;
    cg$uit_tenants.upd(cg$rec, cg$ind);
  END enable_entity;

  ------------------------------------------------------------------------------
  -- Name:        disable_entity
  --
  -- Description: Disable a user.
  --
  -- Parameters:  p_id        The identifier of the tenant to disable.
  ------------------------------------------------------------------------------
  PROCEDURE disable_entity(p_id IN VARCHAR2)
  IS
    cg$rec cg$uit_tenants.cg$row_type;
    cg$ind cg$uit_tenants.cg$ind_type;
  BEGIN
    cg$ind.active := TRUE;
    cg$rec.active := 1;
    cg$uit_tenants.upd(cg$rec, cg$ind);
  END disable_entity;
  ------------------------------------------------------------------------------
  -- Name:        assign_user
  --
  -- Description: Assign a role in a tennat to a user.
  --
  -- Parameters:  p_tenantname The name of the tenant to assign to a user.
  --              p_username   The name of the user to assign to a tenant.
  --              p_rolename   The name of the role to assign to user in a tenant.
  ------------------------------------------------------------------------------
  PROCEDURE assign_user(p_tenantname IN VARCHAR2, p_username IN VARCHAR2, p_rolename IN VARCHAR2)
  IS
    cg$rec_usr cg$igt_users.cg$row_type;
    cg$ind_clm cg$uit_claims.cg$ind_type;
    cg$rec_clm cg$uit_claims.cg$row_type;
  BEGIN
    select_user(p_username, cg$rec_usr);
    --
    cg$rec_clm.tnt_id := p_tenantname;
    cg$rec_clm.rol_id := p_rolename;
    cg$rec_clm.usr_id := cg$rec_usr.id;
    --
    cg$uit_claims.ins(cg$rec_clm, cg$ind_clm);
  END assign_user;

  ------------------------------------------------------------------------------
  -- Name:        revoke_user
  --
  -- Description: Revoke a role in a tennat from a user.
  --
  -- Parameters:  p_tenantname The name of the tenant to revoke a user.
  --              p_username   The name of the user to revoke in a tenant.
  --              p_rolename   The name of the role to revoke from user in a tenant.
  ------------------------------------------------------------------------------
  PROCEDURE revoke_user(p_tenantname IN VARCHAR2, p_username IN VARCHAR2, p_rolename IN VARCHAR2)
  IS
    cg$rec_usr cg$igt_users.cg$row_type;
    cg$pk_clm  cg$uit_claims.cg$pk_type;

  BEGIN
    select_user(p_username, cg$rec_usr);
    --
    cg$pk_clm.tnt_id := p_tenantname;
    cg$pk_clm.rol_id := p_rolename;
    cg$pk_clm.usr_id := cg$rec_usr.id;
    --
    cg$uit_claims.del(cg$pk_clm);
  END revoke_user;
END uid$tenant;
/
SHOW ERROR