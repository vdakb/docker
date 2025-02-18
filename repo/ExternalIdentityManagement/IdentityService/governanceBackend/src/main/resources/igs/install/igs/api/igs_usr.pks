-- /Users/dsteding/Project/Bundeskriminalamt12c/ExternalIdentityManagement/IdentityService/governanceBackend/src/main/resources/install/adm/api/igs_usr.pks
--
-- Generated for Oracle Database 12c on Fri Mar 11 12.47.21 2022 by Server Generator 10.1.2.93.10

PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PROMPT Creating API Package Specification Identity Governance User Administration
PROMPT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
--------------------------------------------------------------------------------
-- Name:        igs$user
-- Description: API for Identity Governance User Administration
--------------------------------------------------------------------------------
CREATE OR REPLACE PACKAGE igs$user
IS

  ------------------------------------------------------------------------------
  -- Name:        create_entity
  --
  -- Description: Create a user.
  --
  -- Parameters:  p_username  The name of the user to create.
  --              p_firstname The firstname of the user to create.
  --              p_lastname  The lastname of the user to create.
  --              p_language  The preferred language of the user to create.
  --              p_email     The e-mail Address  of the user to create.
  --              p_phone     The telephone number  of the user to create.
  --              p_mobile    The mobile phone number  of the user to create.
  ------------------------------------------------------------------------------
  PROCEDURE create_entity( p_username IN VARCHAR2
                         , p_firstname IN VARCHAR2
                         , p_lastname IN VARCHAR2
                         , p_language IN VARCHAR2
                         , p_email    IN VARCHAR2
                         , p_phone    IN VARCHAR2
                         , p_mobile   IN VARCHAR2 
                         );
  ------------------------------------------------------------------------------
  -- Name:        delete_entity
  --
  -- Description: Delete a user.
  --
  -- Parameters:  p_username The name of the user to delete.
  ------------------------------------------------------------------------------
  PROCEDURE delete_entity(p_username IN VARCHAR2);
  ------------------------------------------------------------------------------
  -- Name:        enable_entity
  --
  -- Description: Enable a user.
  --
  -- Parameters:  p_username The name of the user to enable.
  ------------------------------------------------------------------------------
  PROCEDURE enable_entity(p_username IN VARCHAR2);
  ------------------------------------------------------------------------------
  -- Name:        disable_entity
  --
  -- Description: Disable a user.
  --
  -- Parameters:  p_username The name of the user to disable.
  ------------------------------------------------------------------------------
  PROCEDURE disable_entity(p_username IN VARCHAR2);
  ------------------------------------------------------------------------------
  -- Name:        assign_role
  --
  -- Description: Assign a role to a user.
  --
  -- Parameters:  p_username The name of the user to assign to a role.
  --              p_rolename The name of the role to assign to a user.
  ------------------------------------------------------------------------------
  PROCEDURE assign_role(p_username IN VARCHAR2, p_rolename IN VARCHAR2);
  ------------------------------------------------------------------------------
  -- Name:        revoke_role
  --
  -- Description: Revoke a role from a user.
  --
  -- Parameters:  p_username The name of the user to revoke from a role.
  --              p_rolename The name of the role to revoke from  a user.
  ------------------------------------------------------------------------------
  PROCEDURE revoke_role(p_username IN VARCHAR2, p_rolename IN VARCHAR2);
END igs$user;
/
SHOW ERROR