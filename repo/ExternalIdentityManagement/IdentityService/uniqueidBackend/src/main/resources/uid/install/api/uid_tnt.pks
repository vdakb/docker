-- /Users/dsteding/Project/Bundeskriminalamt12c/ExternalIdentityManagement/IdentityService/governanceBackend/src/main/resources/install/adm/api/uid_pks.pks
--
-- Generated for Oracle Database 12c on Fri Mar 11 12.47.21 2022 by Server Generator 10.1.2.93.10

PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating API Package Specification Identity Governance Tenant Administration
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
--------------------------------------------------------------------------------
-- Name:        uid$tenant
-- Description: API for Identity Governance Tenant Administration
--------------------------------------------------------------------------------
CREATE OR REPLACE PACKAGE uid$tenant
IS

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
                         );
 ------------------------------------------------------------------------------
  -- Name:        delete_entity
  --
  -- Description: Delete a user.
  --
  -- Parameters:  p_id        The identifier of the tenant to delete.
  ------------------------------------------------------------------------------
  PROCEDURE delete_entity(p_id IN VARCHAR2);
  ------------------------------------------------------------------------------
  -- Name:        enable_entity
  --
  -- Description: Enable a user.
  --
  -- Parameters:  p_id        The identifier of the tenant to enable.
  ------------------------------------------------------------------------------
  PROCEDURE enable_entity(p_id IN VARCHAR2);
  ------------------------------------------------------------------------------
  -- Name:        disable_entity
  --
  -- Description: Disable a user.
  --
  -- Parameters:  p_id        The identifier of the tenant to disable.
  ------------------------------------------------------------------------------
  PROCEDURE disable_entity(p_id IN VARCHAR2);
  ------------------------------------------------------------------------------
  -- Name:        assign_user
  --
  -- Description: Assign a role in a tennat to a user.
  --
  -- Parameters:  p_tenantname The name of the tenant to assign to a user.
  --              p_username   The name of the user to assign to a tenant.
  --              p_rolename   The name of the role to assign to user in a tenant.
  ------------------------------------------------------------------------------
  PROCEDURE assign_user(p_tenantname IN VARCHAR2, p_username IN VARCHAR2, p_rolename IN VARCHAR2);
  ------------------------------------------------------------------------------
  -- Name:        revoke_user
  --
  -- Description: Revoke a role in a tennat from a user.
  --
  -- Parameters:  p_tenantname The name of the tenant to revoke a user.
  --              p_username   The name of the user to revoke in a tenant.
  --              p_rolename   The name of the role to revoke from user in a tenant.
  ------------------------------------------------------------------------------
  PROCEDURE revoke_user(p_tenantname IN VARCHAR2, p_username IN VARCHAR2, p_rolename IN VARCHAR2);
END uid$tenant;
/
SHOW ERROR