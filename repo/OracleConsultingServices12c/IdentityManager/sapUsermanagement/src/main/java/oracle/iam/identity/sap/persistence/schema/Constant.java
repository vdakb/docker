package oracle.iam.identity.sap.persistence.schema;

interface Constant {
  static final String PARID                            = "PARID";
  static final String PARVA                            = "PARVA";

  static final String ATTRIBUT_ACCOUNT                 = "USERNAME;BAPIBNAME";
  static final String ATTRIBUT_FIRSTNAME               = "FIRSTNAME;ADDRESS;FIRSTNAME;ADDRESSX";
  static final String ATTRIBUT_LASTNAME                = "LASTNAME;ADDRESS;LASTNAME;ADDRESSX";
  static final String ATTRIBUT_E_MAIL                  = "E_MAIL;ADDRESS;E_MAIL;ADDRESSX";
  static final String ATTRIBUT_TITLE_P                 = "TITLE_P;ADDRESS;TITLE_P;ADDRESSX";
  static final String ATTRIBUT_LANGUAGE_KEY_P          = "LANGU_P;ADDRESS;LANGU_P;ADDRESSX";
  static final String ATTRIBUT_FUNCTION                = "FUNCTION;ADDRESS;FUNCTION;ADDRESSX";
  static final String ATTRIBUT_COMMUNICATION_TYPE      = "COMM_TYPE;ADDRESS;COMM_TYPE;ADDRESSX";
  static final String ATTRIBUT_TIME_ZONE               = "TZONE;LOGONDATA;TZONE;LOGONDATAX";
  static final String ATTRIBUT_USER_TYPE               = "USTYP;LOGONDATA;USTYP;LOGONDATAX";
  static final String ATTRIBUT_FAX_NUMBER              = "FAX_NUMBER;ADDRESS;FAX_NUMBER;ADDRESSX";
  static final String ATTRIBUT_FAX_EXTENSION           = "FAX_EXTENS;ADDRESS;FAX_EXTENS;ADDRESSX";
  static final String ATTRIBUT_ACCOUNTING_NUMBER       = "ACCNT;LOGONDATA;ACCNT;LOGONDATAX";
  static final String ATTRIBUT_ALIAS                   = "USERALIAS;ALIAS;BAPIALIAS;ALIASX";
  static final String ATTRIBUT_BUILDING                = "BUILDING_P;ADDRESS;BUILDING_P;ADDRESSX";
  static final String ATTRIBUT_COMPANY                 = "COMPANY;COMPANY;COMPANY;COMPANYX";
  static final String ATTRIBUT_CONTRACTUAL_USER_TYPE   = "LIC_TYPE;UCLASS;UCLASS;UCLASSX";
  static final String ATTRIBUT_COST_CENTER             = "KOSTL;DEFAULTS;KOSTL;DEFAULTSX";
  static final String ATTRIBUT_DATE_FORMAT             = "DATFM;DEFAULTS;DATFM;DEFAULTSX";
  static final String ATTRIBUT_DECIMAL_NOTATION        = "DCPFM;DEFAULTS;DCPFM;DEFAULTSX";
  static final String ATTRIBUT_DEPARTMENT              = "DEPARTMENT;ADDRESS;DEPARTMENT;ADDRESSX";
  static final String ATTRIBUT_FLOOR                   = "FLOOR_P;ADDRESS;FLOOR_P;ADDRESSX";
  static final String ATTRIBUT_LOGON_LANGUAGE          = "LANGU;DEFAULTS;LANGU;DEFAULTSX";
  static final String ATTRIBUT_PASSWORD                = "__PASSWORD__";
  static final String ATTRIBUT_ROMM_NUMBER             = "ROOM_NO_P;ADDRESS;ROOM_NO_P;ADDRESSX";
  static final String ATTRIBUT_START_MENU              = "START_MENU;DEFAULTS;START_MENU;DEFAULTSX";
  static final String ATTRIBUT_TELEPHONE_NUMBER        = "TEL1_NUMBR;ADDRESS;TEL1_NUMBR;ADDRESSX";
  static final String ATTRIBUT_TELEPHONE_EXTENSION     = "TEL1_EXT;ADDRESS;TEL1_EXT;ADDRESSX";
  static final String ATTRIBUT_GROUPS                  = "CLASS;LOGONDATA;CLASS;LOGONDATAX";
  static final String ATTRIBUT_CUA_SYSTEMS             = "SUBSYSTEM;SYSTEMS";
  static final String ATTRIBUT_ACCOUNT_LOCKED_WRNG_PWD = "ISLOCKED;WRNG_LOGON";
  static final String ATTRIBUT_PROFILES                = "PROFILE";
  static final String ATTRIBUT_ACCOUNT_LOCKED_NO_PWD   = "ISLOCKED;NO_USER_PW";
  static final String ATTRIBUT_ROLES_EMBEDED           = "roles";
  static final String ATTRIBUT_PROFILES_EMBEDED        = "profiles";
  static final String ATTRIBUT_PARAMETERS_EMBEDED      = "parameters";
  static final String ATTRIBUT_GROUPS_EMBEDED          = "groups";

  static final String SUBSYSTEM                        = "SUBSYSTEM";
  static final String PROFILE                          = "PROFILE";
  static final String PROFILE_TEXT                     = "BAPIPTEXT";
  static final String PROFILE_TYPE                     = "BAPITYPE";
  static final String PROFILE_AKTPS                    = "BAPIAKTPS";
  static final String PERSONNEL_NUMBER                 = "PERNR";

  static final String ROLE_SYSTEM_NAME                 = "SUBSYSTEM";
  static final String ROLE_NAME                        = "AGR_NAME";
  static final String ROLE_FROM_DATE                   = "FROM_DAT";
  static final String ROLE_TO_DATE                     = "TO_DAT";
  static final String ROLE_TEXT                        = "AGR_TEXT";
  static final String AGR_ORG_FLAG                     = "ORG_FLAG";
}
