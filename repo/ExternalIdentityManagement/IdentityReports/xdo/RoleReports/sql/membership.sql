/**
 ** This report will display membership details of all the roles.
 ** The report will not show indirect memberships.
 ** Security model is not implemented in this report.
 */
SELECT ugp.ugp_key        AS "roleKey"
,      ugp.ugp_name       AS "roleName"
,      ugp.ugp_create     AS "createdOn"
,      cre.usr_login      AS "createdLoginName"
,      cre.usr_first_name AS "createdFirstName"
,      cre.usr_last_name  AS "createdLastName"
,      ugp.ugp_update     AS "updatedOn"
,      upd.usr_login      AS "updatedLoginName"
,      upd.usr_first_name AS "updatedFirstName"
,      upd.usr_last_name  AS "updatedLastName"
FROM   ugp            ugp
,      usr            cre
,      usr            upd
,      role_category  cat
WHERE  ugp.ugp_name IN (
         SELECT DISTINCT ugp.ugp_name
         FROM   usr usr
         ,      usg usg
         ,      ugp ugp
         WHERE  usr.usr_key(+) = usg.usr_key
         AND    usg.ugp_key(+) = ugp.ugp_key
         AND    (NVL(:p_name, 'x') = 'x' OR ugp.ugp_name LIKE :p_name)
       )
AND    ugp.ugp_createby          = cre.usr_key
AND    ugp.ugp_updateby          = upd.usr_key
AND    ugp.ugp_role_category_key = cat.role_category_key
AND    (NVL(:p_category, 'x') = 'x' OR cat.role_category_name LIKE :p_category)
ORDER BY ugp.ugp_name
/

SELECT usr.usr_login      AS "memberLoginName"
,      usr.usr_status     AS "memberStatus"
,      usr.usr_emp_type   AS "memberType"
,      usr.usr_last_name  AS "memberLastName"
,      usr.usr_first_name AS "memberFirstName"
,      usg.usg_create     AS "memberSince"
,      act.act_key        AS "organizationKey"
,      act.act_name       AS "organizationName"
,      mgr.usr_login      AS "managerLoginName"
,      mgr.usr_first_name AS "managerFirstName"
,      mgr.usr_last_name  AS "managerLastName"
FROM   act act
,      ugp ugp
,      usg usg
,      usr LEFT OUTER JOIN usr mgr ON NVL(mgr.usr_key, -1) = NVL(usr.usr_manager_key, -1)
WHERE  ugp.ugp_key = :roleKey
AND    usr.act_key = act.act_key
AND    usg.ugp_key = ugp.ugp_key
AND    usg.usr_key = usr.usr_key
ORDER BY usr.usr_login
/
