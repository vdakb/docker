SET SERVEROUTPUT ON
PROMPT ######################################################
PROMPT Configure session context ...
PROMPT ######################################################
EXECUTE cg$sessions.set_identity('igssysadm')
PROMPT ######################################################
PROMPT Enable Logging ...
PROMPT ######################################################
BEGIN
  dbms_output.enable(buffer_size => 32768);
END;
/
PROMPT ######################################################
PROMPT Creating user bk4711128 ...
PROMPT ######################################################
BEGIN
  igs$user.create_entity( p_username  => 'bk4711128'
                        , p_firstname => 'a'
                        , p_lastname  => 'b'
                        , p_language  => 'de'
                        , p_email     => 'a.b.@vm.oracke,com'
                        , p_phone     => null
                        , p_mobile    => null
                        );
END;
/
SELECT id
,      username
,      active
  FROM igt_users
 WHERE username = 'bk4711128'
/
PROMPT ######################################################
PROMPT Disabling user bk4711128 ...
PROMPT ######################################################
BEGIN
 igs$user.disable_entity(p_username => 'bk4711128');
END;
/
SELECT id
,      username
,      active
  FROM igt_users
 WHERE username = 'bk4711128'
/
PROMPT ######################################################
PROMPT Enabling user bk4711128 ...
PROMPT ######################################################
BEGIN
  igs$user.enable_entity(p_username => 'bk4711128');
END;
/
SELECT id
,      username
,      active
  FROM igt_users
 WHERE username = 'bk4711128'
/
PROMPT ######################################################
PROMPT Assigning role uid.register to user bk4711128 ...
PROMPT ######################################################
BEGIN
  igs$user.assign_role(p_username => 'bk4711128', p_rolename => 'uid.generate');
  igs$user.assign_role(p_username => 'bk4711128', p_rolename => 'uid.register');
END;
/
SELECT usr_id
  ,    rol_id
  FROM igt_userroles
 WHERE usr_id = (
         SELECT id
           FROM igt_users
          WHERE username = 'bk4711128'
       )
/
PROMPT ######################################################
PROMPT Reoking role uid.register and uid.generate from user bk4711128 ...
PROMPT ######################################################
BEGIN
  igs$user.revoke_role(p_username => 'bk4711128', p_rolename => 'uid.register');
  igs$user.revoke_role(p_username => 'bk4711128', p_rolename => 'uid.generate');
END;
/
SELECT usr_id
  ,    rol_id
  FROM igt_userroles
 WHERE usr_id = (
         SELECT id
           FROM igt_users
          WHERE username = 'bk4711128'
       )
/
PROMPT ######################################################
PROMPT Deleting user bk4711128 ...
PROMPT ######################################################
BEGIN
  igs$user.delete_entity(p_username => 'bk4711128');
END;
/
SELECT id
,      username
,      active
  FROM igt_users
 WHERE username = 'bk4711128'
/
ROLLBACK
/