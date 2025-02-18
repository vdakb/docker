/**
 ** This report enables administrators, signing officers, internal and external
 ** auditors to analyze discrepancies in various process forms and related child
 ** tables of various resources and mitigate material weaknesses in the
 ** resources through remediation activities.
 */
SELECT DISTINCT
       usr.usr_key           AS "userKey"
,      usr.usr_last_name     AS "userLasrName"
,      usr.usr_first_name    AS "userFirstName"
,      usr.usr_login         AS "userLoginName"
,      usr.usr_status        AS "userStatus"
,      usr.usr_emp_type      AS "userType"
,      act.act_key           AS "organizationKey"
,      act.act_name          AS "organizationName"
,      fgm.formtype          AS "formType"
,      fgm.formfieldname     AS "formFieldName"
,      fgm.formfieldoldvalue AS "formFieldOldValue"
,      fgm.formfieldnewvalue AS "formfieldNewValue"
,      fgm.sdkkeyfield       AS "formFieldKey"
,      fgm.sdkkeyfieldvalue  AS "formFieldKeyValue"
,      fgm.exceptionapproved AS "exceptionApproved"
,      DECODE(fgm.exceptionapproved, 'YES', xl_sfg_get_attrevfname(fgm.oiu_key), NULL) AS "reviewerFirstName"
,      DECODE(fgm.exceptionapproved, 'YES', xl_sfg_get_attrevfname(fgm.oiu_key), NULL) AS "reviewerLastName"
,      fgm.orckey            AS "orcKey"
,      fgm.parent_sdk_key    AS "parent_sdk_key"
,      fgm.formname          AS "formName"
,      fgm.objname           AS "resourceName"
FROM   usr                         usr
,      act                         act
,      oim_recon_changes_by_res_mv fgm
WHERE  usr.usr_key = fgm.usr_key
AND    usr.act_key = act.act_key
AND    NOT EXISTS (
         SELECT 1
         FROM   upa_ud_forms udf
         ,      oim_recon_changes_by_res_mv fgm
         WHERE  udf.parent_sdk_key IS NULL
         AND    fgm.formtype = 'CHILD'
       )
AND    fgm.obj_key IN (
         SELECT oug.obj_key
         FROM   ugp
         ,      usg
         ,      oug
         WHERE  ugp.ugp_key = usg.ugp_key
         AND    oug.ugp_key = ugp.ugp_key
         AND    usg.usr_key = (
                  SELECT usr_key
                  FROM   usr
                  WHERE  usr_login = UPPER(:xdo_user_name)
                )
       )
AND    (NVL(:p_resourceName,     ' ') = ' ' OR fgm.objname  LIKE :p_resourceName)
AND    (NVL(:p_organizationName, ' ') = ' ' OR act.act_name LIKE :p_organizationName)
AND    EXISTS (
         SELECT ugp.ugp_name
         FROM   ugp
         ,      usg
         WHERE  ugp.ugp_key = usg.ugp_key
         AND    usg.usr_key = usr.usr_key
         AND    (NVL(:p_roleName, ' ') = ' ' OR ugp.ugp_name LIKE :p_roleName)
       )
AND    (NVL(:p_userType,      ' ') = ' ' OR usr.usr_emp_type   LIKE :p_userType)
AND    (NVL(:p_userLoginName, ' ') = ' ' OR usr.usr_login      LIKE UPPER(:p_userLoginName))
AND    (NVL(:p_userFirstName, ' ') = ' ' OR usr.usr_first_name LIKE :p_userFirstName)
AND    (NVL(:p_userLastName,  ' ') = ' ' OR usr.usr_last_name  LIKE :p_userLastName)
ORDER BY objname
,        orckey         DESC
,        parent_sdk_key DESC
,        formtype       DESC