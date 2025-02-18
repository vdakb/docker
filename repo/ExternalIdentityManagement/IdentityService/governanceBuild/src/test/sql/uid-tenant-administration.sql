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
PROMPT ######################################################
PROMPT Creating tenant T-36-0-9 (Bavaria) ...
PROMPT ######################################################
BEGIN
  uid$tenant.create_entity( p_id   => 'T-36-0-9'
                          , p_name => 'Bavaria' 
                          );
END;
/
PROMPT ######################################################
PROMPT Assigning role uid.generate and uid.register to user bk4711128 in tenant T-36-0-9 (Bavaria) ...
PROMPT ######################################################
BEGIN
  uid$tenant.assign_user(p_tenantname => 'T-36-0-9', p_username => 'bk4711128', p_rolename => 'uid.generate');
  uid$tenant.assign_user(p_tenantname => 'T-36-0-9', p_username => 'bk4711128', p_rolename => 'uid.register');
END;
/
SELECT tnt_id
,      usr_id
,      rol_id
  FROM uit_claims
 WHERE usr_id = (
         SELECT id
           FROM igt_users
          WHERE username = 'bk4711128'
       )
/      
PROMPT ######################################################
PROMPT Revoking role uid.generate and uid.register from user bk4711128 in tenant T-36-0-9 (Bavaria) ...
PROMPT ######################################################
BEGIN
  uid$tenant.revoke_user(p_tenantname => 'T-36-0-9', p_username => 'bk4711128', p_rolename => 'uid.generate');
  uid$tenant.revoke_user(p_tenantname => 'T-36-0-9', p_username => 'bk4711128', p_rolename => 'uid.register');
END;
/
SELECT tnt_id
,      usr_id
,      rol_id
  FROM uit_claims
 WHERE usr_id = (
         SELECT id
           FROM igt_users
          WHERE username = 'bk4711128'
       )
/      
ROLLBACK
/