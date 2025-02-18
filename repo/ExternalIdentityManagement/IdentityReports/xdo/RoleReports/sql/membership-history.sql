/**
 ** This report will display membership history of all the roles.
 ** The report will not show indirect memberships.
 ** Security model is not implemented in this report.
 */
SELECT ugp.ugp_key    AS "roleKey"
,      ugp.ugp_name   AS "roleName"
,      ugp.ugp_create AS "createdOn"
,      usr.usr_login  AS "createdBy"
FROM   ugp            ugp
,      usr            usr
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
AND    ugp.ugp_createby          = usr.usr_key
AND    ugp.ugp_role_category_key = cat.role_category_key
AND    (nvl(:p_category, 'x') = 'x' OR cat.role_category_name LIKE :p_category)
ORDER BY ugp.ugp_name
/

/**
 ** This report will display membership history of all the roles.
 ** The report will not show indirect memberships.
 ** Security model is not implemented in this report.
 */
SELECT ugp.ugp_key    AS "roleKey"
,      ugp.ugp_name   AS "roleName"
,      ugp.ugp_create AS "createdOn"
,      usr.usr_login  AS "createdBy"
FROM   ugp            ugp
,      usr            usr
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
AND    ugp.ugp_createby          = usr.usr_key
AND    ugp.ugp_role_category_key = cat.role_category_key
AND    (nvl(:p_category, 'x') = 'x' OR cat.role_category_name LIKE :p_category)
ORDER BY ugp.ugp_name
/

SELECT usr.usr_first_name        AS "userFirstName"
,      usr.usr_last_name         AS "userLastName"
,      usr.usr_login             AS "userLoginName"
,      usr.usr_status            AS "userStatus"
,      usr.usr_emp_type          AS "userType"
,      act.act_key               AS "organizationKey"
,      act.act_name              AS "organizationName"
,      mgr.usr_login             AS "managerLoginName"
,      mgr.usr_first_name        AS "managerFirstName"
,      mgr.usr_last_name         AS "managerLastName"
,      ugm.status                AS "membershipStatus"
,      ugm.upa_grp_eff_FROM_date AS "membershipFrom"
,      ugm.upa_grp_eff_to_date   AS "membershipTo"
,      (SELECT usr_display_name FROM usr WHERE usr.usr_key = ugm.usg_updateby_key) AS "updatedBy"
FROM   act act
,      upa_grp_membership ugm
,      upa_usr            upu
,      usr LEFT OUTER JOIN usr mgr ON NVL(mgr.usr_key, -1) = NVL(usr.usr_manager_key, -1)
WHERE  ugm.ugp_key         = :roleKey
AND    usr.act_key         = act.act_key
AND    upu.usr_key         = usr.usr_key
AND    ugm.upa_usr_key     = upu.upa_usr_key
AND    ugm.membership_type = 'Direct'
AND    (NVL(:p_userType,         ' ') = ' ' OR usr.usr_emp_type LIKE :p_userType)
AND    (NVL(:p_userStatus,       ' ') = ' ' OR usr.usr_status   LIKE :p_userStatus)
AND    (NVL(:p_membershipStatus, ' ') = ' ' OR ugm.status       LIKE :p_membershipStatus)
AND    ugm.upa_grp_eff_from_date BETWEEN :p_effectiveFrom AND :p_effectiveTo
ORDER BY usr.usr_first_name
,        usr.usr_last_name
/