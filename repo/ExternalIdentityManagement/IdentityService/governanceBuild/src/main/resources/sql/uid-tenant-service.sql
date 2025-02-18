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
PROMPT Creating tenant T-36-15-15 (Saxony-Anhalt) ...
PROMPT ######################################################
BEGIN
  uid$tenant.create_entity( p_id   => 'T-36-15-15'
                          , p_name => 'Saxony-Anhalt' 
                          );
END;
/
PROMPT ######################################################
PROMPT Creating tenant T-36-12-12 (Brandenburg) ...
PROMPT ######################################################
BEGIN
  uid$tenant.create_entity( p_id   => 'T-36-12-12'
                          , p_name => 'Brandenburg' 
                          );
END;
/
PROMPT ######################################################
PROMPT Creating tenant T-36-11-11 (Berlin) ...
PROMPT ######################################################
BEGIN
  uid$tenant.create_entity( p_id   => 'T-36-11-11'
                          , p_name => 'Berlin' 
                          );
END;
/
PROMPT ######################################################
PROMPT Creating tenant T-36-8-08 (Baden-Wuerttemberg) ...
PROMPT ######################################################
BEGIN
  uid$tenant.create_entity( p_id   => 'T-36-8-08'
                          , p_name => 'Baden-Wuerttemberg' 
                          );
END;
/
PROMPT ######################################################
PROMPT Creating tenant T-36-9-09 (Bavaria) ...
PROMPT ######################################################
BEGIN
  uid$tenant.create_entity( p_id   => 'T-36-9-09'
                          , p_name => 'Bavaria' 
                          );
END;
/
PROMPT ######################################################
PROMPT Creating tenant T-36-4-04 (Bremen) ...
PROMPT ######################################################
BEGIN
  uid$tenant.create_entity( p_id   => 'T-36-4-04'
                          , p_name => 'Bremen' 
                          );
END;
/
PROMPT ######################################################
PROMPT Creating tenant T-36-6-06 (Hesse) ...
PROMPT ######################################################
BEGIN
  uid$tenant.create_entity( p_id   => 'T-36-6-06'
                          , p_name => 'Hesse' 
                          );
END;
/
PROMPT ######################################################
PROMPT Creating tenant T-36-2-02 (Hamburg) ...
PROMPT ######################################################
BEGIN
  uid$tenant.create_entity( p_id   => 'T-36-2-02'
                          , p_name => 'Hamburg' 
                          );
END;
/
PROMPT ######################################################
PROMPT Creating tenant T-36-13-13 (Mecklenburg-Western Pomerania) ...
PROMPT ######################################################
BEGIN
  uid$tenant.create_entity( p_id   => 'T-36-13-13'
                          , p_name => 'Mecklenburg-Western Pomerania' 
                          );
END;
/
PROMPT ######################################################
PROMPT Creating tenant T-36-3-03 (Lower Saxony) ...
PROMPT ######################################################
BEGIN
  uid$tenant.create_entity( p_id   => 'T-36-3-03'
                          , p_name => 'Lower Saxony' 
                          );
END;
/
PROMPT ######################################################
PROMPT Creating tenant T-36-5-05 (North Rhine-Westphalia) ...
PROMPT ######################################################
BEGIN
  uid$tenant.create_entity( p_id   => 'T-36-5-05'
                          , p_name => 'North Rhine-Westphalia' 
                          );
END;
/
PROMPT ######################################################
PROMPT Creating tenant T-36-7-07 (Rhineland-Palatinate) ...
PROMPT ######################################################
BEGIN
  uid$tenant.create_entity( p_id   => 'T-36-7-07'
                          , p_name => 'Rhineland-Palatinate' 
                          );
END;
/
PROMPT ######################################################
PROMPT Creating tenant T-36-1-01 (Schleswig-Holstein) ...
PROMPT ######################################################
BEGIN
  uid$tenant.create_entity( p_id   => 'T-36-1-01'
                          , p_name => 'Schleswig-Holstein' 
                          );
END;
/
PROMPT ######################################################
PROMPT Creating tenant T-36-10-10 (Saarland) ...
PROMPT ######################################################
BEGIN
  uid$tenant.create_entity( p_id   => 'T-36-10-10'
                          , p_name => 'Saarland' 
                          );
END;
/
PROMPT ######################################################
PROMPT Creating tenant T-36-14-14 (Saxony) ...
PROMPT ######################################################
BEGIN
  uid$tenant.create_entity( p_id   => 'T-36-14-14'
                          , p_name => 'Saxony' 
                          );
END;
/
PROMPT ######################################################
PROMPT Creating tenant T-36-16-16 (Thuringia) ...
PROMPT ######################################################
BEGIN
  uid$tenant.create_entity( p_id   => 'T-36-16-16'
                          , p_name => 'Thuringia' 
                          );
END;
/
COMMIT
/