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
PROMPT Creating user anserviceuser ...
PROMPT ######################################################
BEGIN
  igs$user.create_entity( p_username  => 'anserviceuser'
                        , p_firstname => 'Identity Governance'
                        , p_lastname  => 'Saxony-Anhalt'
                        , p_language  => 'de'
                        , p_email     => 'anserviceuser@bka.bund.de'
                        , p_phone     => null
                        , p_mobile    => null
                        );
END;
/
PROMPT ######################################################
PROMPT Creating user bbserviceuser ...
PROMPT ######################################################
BEGIN
  igs$user.create_entity( p_username  => 'bbserviceuser'
                        , p_firstname => 'Identity Governance'
                        , p_lastname  => 'Brandenburg'
                        , p_language  => 'de'
                        , p_email     => 'bbserviceuser@bka.bund.de'
                        , p_phone     => null
                        , p_mobile    => null
                        );
END;
/
PROMPT ######################################################
PROMPT Creating user beserviceuser ...
PROMPT ######################################################
BEGIN
  igs$user.create_entity( p_username  => 'beserviceuser'
                        , p_firstname => 'Identity Governance'
                        , p_lastname  => 'Berlin'
                        , p_language  => 'de'
                        , p_email     => 'beserviceuser@bka.bund.de'
                        , p_phone     => null
                        , p_mobile    => null
                        );
END;
/
PROMPT ######################################################
PROMPT Creating user bkserviceuser ...
PROMPT ######################################################
BEGIN
  igs$user.create_entity( p_username  => 'bkserviceuser'
                        , p_firstname => 'Identity Governance'
                        , p_lastname  => 'Federal Criminal Police Office'
                        , p_language  => 'de'
                        , p_email     => 'bkserviceuser@bka.bund.de'
                        , p_phone     => null
                        , p_mobile    => null
                        );
END;
/
PROMPT ######################################################
PROMPT Creating user bpserviceuser ...
PROMPT ######################################################
BEGIN
  igs$user.create_entity( p_username  => 'bpserviceuser'
                        , p_firstname => 'Identity Governance'
                        , p_lastname  => 'Federal Police'
                        , p_language  => 'de'
                        , p_email     => 'bpserviceuser@bka.bund.de'
                        , p_phone     => null
                        , p_mobile    => null
                        );
END;
/
PROMPT ######################################################
PROMPT Creating user bwserviceuser ...
PROMPT ######################################################
BEGIN
  igs$user.create_entity( p_username  => 'bwserviceuser'
                        , p_firstname => 'Identity Governance'
                        , p_lastname  => 'Baden-Wuerttemberg'
                        , p_language  => 'de'
                        , p_email     => 'bwserviceuser@bka.bund.de'
                        , p_phone     => null
                        , p_mobile    => null
                        );
END;
/
PROMPT ######################################################
PROMPT Creating user byserviceuser ...
PROMPT ######################################################
BEGIN
  igs$user.create_entity( p_username  => 'byserviceuser'
                        , p_firstname => 'Identity Governance'
                        , p_lastname  => 'Bavaria'
                        , p_language  => 'de'
                        , p_email     => 'byserviceuser@bka.bund.de'
                        , p_phone     => null
                        , p_mobile    => null
                        );
END;
/
PROMPT ######################################################
PROMPT Creating user hbserviceuser ...
PROMPT ######################################################
BEGIN
  igs$user.create_entity( p_username  => 'hbserviceuser'
                        , p_firstname => 'Identity Governance'
                        , p_lastname  => 'Bremen'
                        , p_language  => 'de'
                        , p_email     => 'hbserviceuser@bka.bund.de'
                        , p_phone     => null
                        , p_mobile    => null
                        );
END;
/
PROMPT ######################################################
PROMPT Creating user heserviceuser ...
PROMPT ######################################################
BEGIN
  igs$user.create_entity( p_username  => 'heserviceuser'
                        , p_firstname => 'Identity Governance'
                        , p_lastname  => 'Hesse'
                        , p_language  => 'de'
                        , p_email     => 'heserviceuser@bka.bund.de'
                        , p_phone     => null
                        , p_mobile    => null
                        );
END;
/
PROMPT ######################################################
PROMPT Creating user hhserviceuser ...
PROMPT ######################################################
BEGIN
  igs$user.create_entity( p_username  => 'hhserviceuser'
                        , p_firstname => 'Identity Governance'
                        , p_lastname  => 'Hamburg'
                        , p_language  => 'de'
                        , p_email     => 'hhserviceuser@bka.bund.de'
                        , p_phone     => null
                        , p_mobile    => null
                        );
END;
/
PROMPT ######################################################
PROMPT Creating user mvserviceuser ...
PROMPT ######################################################
BEGIN
  igs$user.create_entity( p_username  => 'mvserviceuser'
                        , p_firstname => 'Identity Governance'
                        , p_lastname  => 'Mecklenburg-Western Pomerania'
                        , p_language  => 'de'
                        , p_email     => 'mvserviceuser@bka.bund.de'
                        , p_phone     => null
                        , p_mobile    => null
                        );
END;
/
PROMPT ######################################################
PROMPT Creating user niserviceuser ...
PROMPT ######################################################
BEGIN
  igs$user.create_entity( p_username  => 'niserviceuser'
                        , p_firstname => 'Identity Governance'
                        , p_lastname  => 'Lower Saxony'
                        , p_language  => 'de'
                        , p_email     => 'niserviceuser@bka.bund.de'
                        , p_phone     => null
                        , p_mobile    => null
                        );
END;
/
PROMPT ######################################################
PROMPT Creating user nwserviceuser ...
PROMPT ######################################################
BEGIN
  igs$user.create_entity( p_username  => 'nwserviceuser'
                        , p_firstname => 'Identity Governance'
                        , p_lastname  => 'North Rhine-Westphalia'
                        , p_language  => 'de'
                        , p_email     => 'nwserviceuser@bka.bund.de'
                        , p_phone     => null
                        , p_mobile    => null
                        );
END;
/
PROMPT ######################################################
PROMPT Creating user rpserviceuser ...
PROMPT ######################################################
BEGIN
  igs$user.create_entity( p_username  => 'rpserviceuser'
                        , p_firstname => 'Identity Governance'
                        , p_lastname  => 'Rhineland-Palatinate'
                        , p_language  => 'de'
                        , p_email     => 'rpserviceuser@bka.bund.de'
                        , p_phone     => null
                        , p_mobile    => null
                        );
END;
/
PROMPT ######################################################
PROMPT Creating user shserviceuser ...
PROMPT ######################################################
BEGIN
  igs$user.create_entity( p_username  => 'shserviceuser'
                        , p_firstname => 'Identity Governance'
                        , p_lastname  => 'Schleswig-Holstein'
                        , p_language  => 'de'
                        , p_email     => 'shserviceuser@bka.bund.de'
                        , p_phone     => null
                        , p_mobile    => null
                        );
END;
/
PROMPT ######################################################
PROMPT Creating user slserviceuser ...
PROMPT ######################################################
BEGIN
  igs$user.create_entity( p_username  => 'slserviceuser'
                        , p_firstname => 'Identity Governance'
                        , p_lastname  => 'Saarland'
                        , p_language  => 'de'
                        , p_email     => 'slserviceuser@bka.bund.de'
                        , p_phone     => null
                        , p_mobile    => null
                        );
END;
/
PROMPT ######################################################
PROMPT Creating user snserviceuser ...
PROMPT ######################################################
BEGIN
  igs$user.create_entity( p_username  => 'snserviceuser'
                        , p_firstname => 'Identity Governance'
                        , p_lastname  => 'Saxony'
                        , p_language  => 'de'
                        , p_email     => 'snserviceuser@bka.bund.de'
                        , p_phone     => null
                        , p_mobile    => null
                        );
END;
/
PROMPT ######################################################
PROMPT Creating user thserviceuser ...
PROMPT ######################################################
BEGIN
  igs$user.create_entity( p_username  => 'thserviceuser'
                        , p_firstname => 'Identity Governance'
                        , p_lastname  => 'Thuringia'
                        , p_language  => 'de'
                        , p_email     => 'thserviceuser@bka.bund.de'
                        , p_phone     => null
                        , p_mobile    => null
                        );
END;
/
PROMPT ######################################################
PROMPT Creating user zfserviceuser ...
PROMPT ######################################################
BEGIN
  igs$user.create_entity( p_username  => 'zfserviceuser'
                        , p_firstname => 'Identity Governance'
                        , p_lastname  => 'Customs Investigation Bureau'
                        , p_language  => 'de'
                        , p_email     => 'zfserviceuser@bka.bund.de'
                        , p_phone     => null
                        , p_mobile    => null
                        );
END;
/
COMMIT
/