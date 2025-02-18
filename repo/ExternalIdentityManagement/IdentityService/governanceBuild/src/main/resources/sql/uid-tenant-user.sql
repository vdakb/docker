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
PROMPT Assigning role uid.generate and uid.register to user anserviceuser in tenant T-36-15-15 (Saxony-Anhalt) ...
PROMPT ######################################################
BEGIN
  uid$tenant.assign_user(p_tenantname => 'T-36-15-15', p_username => 'anserviceuser', p_rolename => 'uid.generate');
  uid$tenant.assign_user(p_tenantname => 'T-36-15-15', p_username => 'anserviceuser', p_rolename => 'uid.register');
END;
/
PROMPT ######################################################
PROMPT Assigning role uid.generate and uid.register to user bbserviceuser in tenant T-36-12-12 (Brandenburg) ...
PROMPT ######################################################
BEGIN
  uid$tenant.assign_user(p_tenantname => 'T-36-12-12', p_username => 'bbserviceuser', p_rolename => 'uid.generate');
  uid$tenant.assign_user(p_tenantname => 'T-36-12-12', p_username => 'bbserviceuser', p_rolename => 'uid.register');
END;
/
PROMPT ######################################################
PROMPT Assigning role uid.generate and uid.register to user beserviceuser in tenant T-36-11-11 (Berlin) ...
PROMPT ######################################################
BEGIN
  uid$tenant.assign_user(p_tenantname => 'T-36-11-11', p_username => 'beserviceuser', p_rolename => 'uid.generate');
  uid$tenant.assign_user(p_tenantname => 'T-36-11-11', p_username => 'beserviceuser', p_rolename => 'uid.register');
END;
/
PROMPT ######################################################
PROMPT Assigning role uid.generate and uid.register to user bkserviceuser in tenant T-36-0-20 (Federal Criminal Police Office) ...
PROMPT ######################################################
BEGIN
  uid$tenant.assign_user(p_tenantname => 'T-36-0-20', p_username => 'bkserviceuser', p_rolename => 'uid.generate');
  uid$tenant.assign_user(p_tenantname => 'T-36-0-20', p_username => 'bkserviceuser', p_rolename => 'uid.register');
END;
/
PROMPT ######################################################
PROMPT Assigning role uid.generate and uid.register to user bpserviceuser in tenant T-36-0-30 (Federal Police) ...
PROMPT ######################################################
BEGIN
  uid$tenant.assign_user(p_tenantname => 'T-36-0-30', p_username => 'bpserviceuser', p_rolename => 'uid.generate');
  uid$tenant.assign_user(p_tenantname => 'T-36-0-30', p_username => 'bpserviceuser', p_rolename => 'uid.register');
END;
/
PROMPT ######################################################
PROMPT Assigning role uid.generate and uid.register to user bwserviceuser in tenant T-36-8-08 (Baden-Wuerttemberg) ...
PROMPT ######################################################
BEGIN
  uid$tenant.assign_user(p_tenantname => 'T-36-8-08', p_username => 'bwserviceuser', p_rolename => 'uid.generate');
  uid$tenant.assign_user(p_tenantname => 'T-36-8-08', p_username => 'bwserviceuser', p_rolename => 'uid.register');
END;
/
PROMPT ######################################################
PROMPT Assigning role uid.generate and uid.register to user byserviceuser in tenant T-36-9-09 (Bavaria) ...
PROMPT ######################################################
BEGIN
  uid$tenant.assign_user(p_tenantname => 'T-36-9-09', p_username => 'byserviceuser', p_rolename => 'uid.generate');
  uid$tenant.assign_user(p_tenantname => 'T-36-9-09', p_username => 'byserviceuser', p_rolename => 'uid.register');
END;
/
PROMPT ######################################################
PROMPT Assigning role uid.generate and uid.register to user hbserviceuser in tenant T-36-4-04 (Bremen) ...
PROMPT ######################################################
BEGIN
  uid$tenant.assign_user(p_tenantname => 'T-36-4-04', p_username => 'hbserviceuser', p_rolename => 'uid.generate');
  uid$tenant.assign_user(p_tenantname => 'T-36-4-04', p_username => 'hbserviceuser', p_rolename => 'uid.register');
END;
/
PROMPT ######################################################
PROMPT Assigning role uid.generate and uid.register to user heserviceuser in tenant T-36-6-06 (Hesse) ...
PROMPT ######################################################
BEGIN
  uid$tenant.assign_user(p_tenantname => 'T-36-6-06', p_username => 'heserviceuser', p_rolename => 'uid.generate');
  uid$tenant.assign_user(p_tenantname => 'T-36-6-06', p_username => 'heserviceuser', p_rolename => 'uid.register');
END;
/
PROMPT ######################################################
PROMPT Assigning role uid.generate and uid.register to user hhserviceuser in tenant T-36-2-02 (Hamburg) ...
PROMPT ######################################################
BEGIN
  uid$tenant.assign_user(p_tenantname => 'T-36-2-02', p_username => 'hhserviceuser', p_rolename => 'uid.generate');
  uid$tenant.assign_user(p_tenantname => 'T-36-2-02', p_username => 'hhserviceuser', p_rolename => 'uid.register');
END;
/
PROMPT ######################################################
PROMPT Assigning role uid.generate and uid.register to user mvserviceuser in tenant T-36-13-13 (Mecklenburg-Western Pomerania) ...
PROMPT ######################################################
BEGIN
  uid$tenant.assign_user(p_tenantname => 'T-36-13-13', p_username => 'mvserviceuser', p_rolename => 'uid.generate');
  uid$tenant.assign_user(p_tenantname => 'T-36-13-13', p_username => 'mvserviceuser', p_rolename => 'uid.register');
END;
/
PROMPT ######################################################
PROMPT Assigning role uid.generate and uid.register to user niserviceuser in tenant T-36-13-13 (Lower Saxony) ...
PROMPT ######################################################
BEGIN
  uid$tenant.assign_user(p_tenantname => 'T-36-3-03', p_username => 'niserviceuser', p_rolename => 'uid.generate');
  uid$tenant.assign_user(p_tenantname => 'T-36-3-03', p_username => 'niserviceuser', p_rolename => 'uid.register');
END;
/
PROMPT ######################################################
PROMPT Assigning role uid.generate and uid.register to user nwserviceuser in tenant T-36-13-13 (North Rhine-Westphalia) ...
PROMPT ######################################################
BEGIN
  uid$tenant.assign_user(p_tenantname => 'T-36-5-05', p_username => 'nwserviceuser', p_rolename => 'uid.generate');
  uid$tenant.assign_user(p_tenantname => 'T-36-5-05', p_username => 'nwserviceuser', p_rolename => 'uid.register');
END;
/
PROMPT ######################################################
PROMPT Assigning role uid.generate and uid.register to user rpserviceuser in tenant T-36-7-07 (Rhineland-Palatinate) ...
PROMPT ######################################################
BEGIN
  uid$tenant.assign_user(p_tenantname => 'T-36-7-07', p_username => 'rpserviceuser', p_rolename => 'uid.generate');
  uid$tenant.assign_user(p_tenantname => 'T-36-7-07', p_username => 'rpserviceuser', p_rolename => 'uid.register');
END;
/
PROMPT ######################################################
PROMPT Assigning role uid.generate and uid.register to user shserviceuser in tenant T-36-1-01 (Rhineland-Palatinate) ...
PROMPT ######################################################
BEGIN
  uid$tenant.assign_user(p_tenantname => 'T-36-1-01', p_username => 'shserviceuser', p_rolename => 'uid.generate');
  uid$tenant.assign_user(p_tenantname => 'T-36-1-01', p_username => 'shserviceuser', p_rolename => 'uid.register');
END;
/
PROMPT ######################################################
PROMPT Assigning role uid.generate and uid.register to user slserviceuser in tenant T-36-10-10 (Saarland) ...
PROMPT ######################################################
BEGIN
  uid$tenant.assign_user(p_tenantname => 'T-36-10-10', p_username => 'slserviceuser', p_rolename => 'uid.generate');
  uid$tenant.assign_user(p_tenantname => 'T-36-10-10', p_username => 'slserviceuser', p_rolename => 'uid.register');
END;
/
PROMPT ######################################################
PROMPT Assigning role uid.generate and uid.register to user snserviceuser in tenant T-36-14-14 (Saxony) ...
PROMPT ######################################################
BEGIN
  uid$tenant.assign_user(p_tenantname => 'T-36-14-14', p_username => 'snserviceuser', p_rolename => 'uid.generate');
  uid$tenant.assign_user(p_tenantname => 'T-36-14-14', p_username => 'snserviceuser', p_rolename => 'uid.register');
END;
/
PROMPT ######################################################
PROMPT Assigning role uid.generate and uid.register to user thserviceuser in tenant T-36-16-16 (Thuringia) ...
PROMPT ######################################################
BEGIN
  uid$tenant.assign_user(p_tenantname => 'T-36-16-16', p_username => 'thserviceuser', p_rolename => 'uid.generate');
  uid$tenant.assign_user(p_tenantname => 'T-36-16-16', p_username => 'thserviceuser', p_rolename => 'uid.register');
END;
/
PROMPT ######################################################
PROMPT Assigning role uid.generate and uid.register to user thserviceuser in tenant T-36-0-31 (Customs Investigation Bureau) ...
PROMPT ######################################################
BEGIN
  uid$tenant.assign_user(p_tenantname => 'T-36-0-31', p_username => 'thserviceuser', p_rolename => 'uid.generate');
  uid$tenant.assign_user(p_tenantname => 'T-36-0-31', p_username => 'thserviceuser', p_rolename => 'uid.register');
END;
/
COMMIT
/