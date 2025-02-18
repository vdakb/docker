/**
 ** The rogue account report includes all rogue accounts for the input resource.
 ** It also includes the corresponding attestation data to analyze if the rogue
 ** accounts represent outstanding or accepted exceptions in the system.
 **
 ** This report enables administrators, internal and external auditors to
 ** identify material weaknesses in the resources and plan their mitigation
 ** through remediation activities.
 */
 SELECT "resourceName"
 ,      "resourceType"
 ,      "userLoginName"
 ,      "userFirstName"
 ,      "userLastName"
 ,      "userStatus"
 ,      "userType"
 ,      "organization"
 ,      "exceptionType"
 ,      "exceptionApproved"
 ,      "reviewerFirstname"
 ,      "reviewerLastname"
 ,      "reviewerLoginName"
 FROM   (
   SELECT obj.obj_name       AS "resourceName"
   ,      obj.obj_type       AS "resourceType"
   ,      usr.usr_login      AS "userLoginName"
   ,      usr.usr_first_name AS "userFirstName"
   ,      usr.usr_last_name  AS "userLastName"
   ,      usr.usr_status     AS "userStatus"
   ,      usr.usr_emp_type   AS "userType"
   ,      act.act_name       AS "organization"
   ,      rex.rex_exception  AS "exceptionType"
   ,      oim_report_utils.xl_sfg_isexcepapproved_new(oiu.oiu_key, oim_report_utils.xl_sfg_getlatestrecforpro_new(oiu.orc_key)) AS "exceptionApproved"
   ,      DECODE(oim_report_utils.xl_sfg_isexcepapproved_new(oiu.oiu_key, oim_report_utils.xl_sfg_getlatestrecforpro_new(oiu.orc_key)), 'Yes', XL_SFG_GET_ATTREVFNAME(oiu.oiu_key), NULL)                  AS "reviewerFirstname"
   ,      DECODE(oim_report_utils.xl_sfg_isexcepapproved_new(oiu.oiu_key, oim_report_utils.xl_sfg_getlatestrecforpro_new(oiu.orc_key)), 'Yes', XL_SFG_GET_ATTREVLNAME(oiu.oiu_key), NULL)                  AS "reviewerLastname"
   ,      DECODE(oim_report_utils.xl_sfg_isexcepapproved_new(oiu.oiu_key, oim_report_utils.xl_sfg_getlatestrecforpro_new(oiu.orc_key)), 'Yes', oim_report_utils.XL_SFG_GET_ATTREVLOGIN(oiu.oiu_key), NULL) AS "reviewerLoginName"
   FROM   recon_exceptions rex
   ,      orc orc
   ,      oiu oiu
   ,      obj obj
   ,      usr usr
   ,      act act
   WHERE  rex.rex_account_type = 'R'
   AND    usr.act_key          = act.act_key
   AND    usr.usr_key          = oiu.usr_key
   AND    rex.usr_key          = usr.usr_key
   AND    oiu.orc_key          = orc.orc_key
   AND    rex.rex_orc_key      = orc.orc_key
   AND    rex.rex_obj_key      = obj.obj_key
   AND    obj.obj_key IN (
            SELECT oug.obj_key
            FROM   ugp
            ,      usg
            ,      oug
            WHERE  ugp.ugp_key = usg.ugp_key
            AND    oug.ugp_key = ugp.ugp_key
            AND    usg.usr_key = (
                     SELECT usr_key
                     FROM usr
                     WHERE UPPER(usr.usr_login) = UPPER(:xdo_user_name)
                   )
          )
   AND    (NVL(:p_resourceName,     ' ') =' ' OR obj.obj_name         LIKE :p_resourceName)
   AND    (NVL(:p_userOrganization, ' ') =' ' OR act.act_name         LIKE :p_userOrganization)
   AND    (NVL(:p_userType,         ' ') =' ' OR usr.usr_emp_type     LIKE :p_userType)
   AND    (NVL(:p_userStatus,       ' ') =' ' OR usr.usr_status       LIKE :p_userStatus)
   AND    (NVL(:p_userFirstName,    ' ') =' ' OR usr.usr_first_name   LIKE :p_userFirstName)
   AND    (NVL(:p_userLastName,     ' ') =' ' OR usr.usr_last_name    LIKE :p_userLastName)
   AND    (NVL(:p_userLogin,        ' ') =' ' OR UPPER(usr.usr_login) LIKE UPPER(:p_userLogin))
   AND    (NVL(:p_exceptionType,    ' ') =' ' OR rex.rex_exception    LIKE :p_exceptionType)
   AND    EXISTS (
            SELECT ugp.ugp_name
            FROM   ugp
            ,      usg
            WHERE  ugp.ugp_key = usg.ugp_key
            AND    usg.usr_key = usr.usr_key
          )
   UNION ALL
   SELECT obj.obj_name                  AS "resourceName"
   ,      obj.obj_type                  AS "resourceType"
   ,      NVL(usr.usr_login,      NULL) AS "userLoginName"
   ,      NVL(usr.usr_first_name, NULL) AS "userFirstName"
   ,      NVL(usr.usr_last_name,  NULL) AS "userLastName"
   ,      NVL(usr.usr_status,     NULL) AS "userStatus"
   ,      NVL(usr.usr_emp_type,   NULL) AS "userType"
   ,      NVL(act.act_name,       NULL) AS "organization"
   ,      rex.rex_exception             AS "exceptionType"
   ,      'No'                          AS "exceptionApproved"
   ,      NULL                          AS "reviewerFirstname"
   ,      NULL                          AS "reviewerLastname"
   ,      NULL                          AS "reviewerLoginName"
   FROM   recon_exceptions rex
   ,      usr usr
   ,      obj obj
   ,      act act
   WHERE  rex.rex_account_type = 'R'
   AND    rex.rex_exception IN ('USER_NOT_FOUND','USER_DELETED')
   AND    usr.act_key     = act.act_key(+)
   AND    rex.rex_obj_key = obj.obj_key
   AND    rex.usr_key     = usr.usr_key(+)
   AND    obj.obj_key IN (
            SELECT oug.obj_key
            FROM   ugp
            ,      usg
            ,      oug
            WHERE  ugp.ugp_key = usg.ugp_key
            AND    oug.ugp_key = ugp.ugp_key
            AND    usg.usr_key = (
                     SELECT usr_key
                     FROM usr
                     WHERE UPPER(usr.usr_login) = UPPER(:xdo_user_name)
                   )
          )
   AND    (NVL(:p_resourceName,     ' ') =' ' OR obj.obj_name         LIKE :p_resourceName)
   AND    (NVL(:p_userOrganization, ' ') =' ' OR act.act_name         LIKE :p_userOrganization)
   AND    (NVL(:p_userType,         ' ') =' ' OR usr.usr_emp_type     LIKE :p_userType)
   AND    (NVL(:p_userStatus,       ' ') =' ' OR usr.usr_status       LIKE :p_userStatus)
   AND    (NVL(:p_userFirstName,    ' ') =' ' OR usr.usr_first_name   LIKE :p_userFirstName)
   AND    (NVL(:p_userLastName,     ' ') =' ' OR usr.usr_last_name    LIKE :p_userLastName)
   AND    (NVL(:p_userLogin,        ' ') =' ' OR UPPER(usr.usr_login) LIKE UPPER(:p_userLogin))
   AND    (NVL(:p_exceptionType,    ' ') =' ' OR rex.rex_exception    LIKE :p_exceptionType)
 )
ORDER BY "resourceName"
,        "userLoginName"
/