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
PROMPT Assigning role uid.generate and uid.register to user anserviceuser (Saxony-Anhalt) ...
PROMPT ######################################################
BEGIN
  igs$user.assign_role(p_username => 'anserviceuser', p_rolename => 'uid.generate');
  igs$user.assign_role(p_username => 'anserviceuser', p_rolename => 'uid.register');
END;
/
PROMPT ######################################################
PROMPT Assigning role uid.generate and uid.register to user bbserviceuser (Brandenburg) ...
PROMPT ######################################################
BEGIN
  igs$user.assign_role(p_username => 'bbserviceuser', p_rolename => 'uid.generate');
  igs$user.assign_role(p_username => 'bbserviceuser', p_rolename => 'uid.register');
END;
/
PROMPT ######################################################
PROMPT Assigning role uid.generate and uid.register to user beserviceuser (Berlin) ...
PROMPT ######################################################
BEGIN
  igs$user.assign_role(p_username => 'beserviceuser', p_rolename => 'uid.generate');
  igs$user.assign_role(p_username => 'beserviceuser', p_rolename => 'uid.register');
END;
/
PROMPT ######################################################
PROMPT Assigning role uid.generate and uid.register to user bkserviceuser (Federal Criminal Police Office) ...
PROMPT ######################################################
BEGIN
  igs$user.assign_role(p_username => 'bkserviceuser', p_rolename => 'uid.generate');
  igs$user.assign_role(p_username => 'bkserviceuser', p_rolename => 'uid.register');
END;
/
PROMPT ######################################################
PROMPT Assigning role uid.generate and uid.register to user bpserviceuser (Federal Police) ...
PROMPT ######################################################
BEGIN
  igs$user.assign_role(p_username => 'bpserviceuser', p_rolename => 'uid.generate');
  igs$user.assign_role(p_username => 'bpserviceuser', p_rolename => 'uid.register');
END;
/
PROMPT ######################################################
PROMPT Assigning role uid.generate and uid.register to user bwserviceuser (Baden-Wuerttemberg) ...
PROMPT ######################################################
BEGIN
  igs$user.assign_role(p_username => 'bwserviceuser', p_rolename => 'uid.generate');
  igs$user.assign_role(p_username => 'bwserviceuser', p_rolename => 'uid.register');
END;
/
PROMPT ######################################################
PROMPT Assigning role uid.generate and uid.register to user byserviceuser (Bavaria) ...
PROMPT ######################################################
BEGIN
  igs$user.assign_role(p_username => 'byserviceuser', p_rolename => 'uid.generate');
  igs$user.assign_role(p_username => 'byserviceuser', p_rolename => 'uid.register');
END;
/
PROMPT ######################################################
PROMPT Assigning role uid.generate and uid.register to user hbserviceuser (Bremen) ...
PROMPT ######################################################
BEGIN
  igs$user.assign_role(p_username => 'hbserviceuser', p_rolename => 'uid.generate');
  igs$user.assign_role(p_username => 'hbserviceuser', p_rolename => 'uid.register');
END;
/
PROMPT ######################################################
PROMPT Assigning role uid.generate and uid.register to user heserviceuser (Hesse) ...
PROMPT ######################################################
BEGIN
  igs$user.assign_role(p_username => 'heserviceuser', p_rolename => 'uid.generate');
  igs$user.assign_role(p_username => 'heserviceuser', p_rolename => 'uid.register');
END;
/
PROMPT ######################################################
PROMPT Assigning role uid.generate and uid.register to user hhserviceuser (Hamburg) ...
PROMPT ######################################################
BEGIN
  igs$user.assign_role(p_username => 'hhserviceuser', p_rolename => 'uid.generate');
  igs$user.assign_role(p_username => 'hhserviceuser', p_rolename => 'uid.register');
END;
/
PROMPT ######################################################
PROMPT Assigning role uid.generate and uid.register to user mvserviceuser (Mecklenburg-Western Pomerania) ...
PROMPT ######################################################
BEGIN
  igs$user.assign_role(p_username => 'mvserviceuser', p_rolename => 'uid.generate');
  igs$user.assign_role(p_username => 'mvserviceuser', p_rolename => 'uid.register');
END;
/
PROMPT ######################################################
PROMPT Assigning role uid.generate and uid.register to user niserviceuser (Lower Saxony) ...
PROMPT ######################################################
BEGIN
  igs$user.assign_role(p_username => 'niserviceuser', p_rolename => 'uid.generate');
  igs$user.assign_role(p_username => 'niserviceuser', p_rolename => 'uid.register');
END;
/
PROMPT ######################################################
PROMPT Assigning role uid.generate and uid.register to user nwserviceuser (North Rhine-Westphalia) ...
PROMPT ######################################################
BEGIN
  igs$user.assign_role(p_username => 'nwserviceuser', p_rolename => 'uid.generate');
  igs$user.assign_role(p_username => 'nwserviceuser', p_rolename => 'uid.register');
END;
/
PROMPT ######################################################
PROMPT Assigning role uid.generate and uid.register to user rpserviceuser (Rhineland-Palatinate) ...
PROMPT ######################################################
BEGIN
  igs$user.assign_role(p_username => 'rpserviceuser', p_rolename => 'uid.generate');
  igs$user.assign_role(p_username => 'rpserviceuser', p_rolename => 'uid.register');
END;
/
PROMPT ######################################################
PROMPT Assigning role uid.generate and uid.register to user shserviceuser (Rhineland-Palatinate) ...
PROMPT ######################################################
BEGIN
  igs$user.assign_role(p_username => 'shserviceuser', p_rolename => 'uid.generate');
  igs$user.assign_role(p_username => 'shserviceuser', p_rolename => 'uid.register');
END;
/
PROMPT ######################################################
PROMPT Assigning role uid.generate and uid.register to user slserviceuser (Saarland) ...
PROMPT ######################################################
BEGIN
  igs$user.assign_role(p_username => 'slserviceuser', p_rolename => 'uid.generate');
  igs$user.assign_role(p_username => 'slserviceuser', p_rolename => 'uid.register');
END;
/
PROMPT ######################################################
PROMPT Assigning role uid.generate and uid.register to user snserviceuser (Saxony) ...
PROMPT ######################################################
BEGIN
  igs$user.assign_role(p_username => 'snserviceuser', p_rolename => 'uid.generate');
  igs$user.assign_role(p_username => 'snserviceuser', p_rolename => 'uid.register');
END;
/
PROMPT ######################################################
PROMPT Assigning role uid.generate and uid.register to user thserviceuser (Thuringia) ...
PROMPT ######################################################
BEGIN
  igs$user.assign_role(p_username => 'thserviceuser', p_rolename => 'uid.generate');
  igs$user.assign_role(p_username => 'thserviceuser', p_rolename => 'uid.register');
END;
/
PROMPT ######################################################
PROMPT Assigning role uid.generate and uid.register to user thserviceuser (Customs Investigation Bureau) ...
PROMPT ######################################################
BEGIN
  igs$user.assign_role(p_username => 'thserviceuser', p_rolename => 'uid.generate');
  igs$user.assign_role(p_username => 'thserviceuser', p_rolename => 'uid.register');
END;
/
COMMIT
/