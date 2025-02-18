/**
 ** This report lists the users visible to the logged in user and its membership history.
 */
 SELECT usr.usr_key        AS "userKey"
 ,      usr.usr_login      AS "userLoginName"
 ,      usr.usr_first_name AS "userFirstName"
 ,      usr.usr_last_name  AS "userLastName"
 ,      usr.usr_status     AS "userStatus"
 ,      usr.usr_create     AS "userCreatedOn"
 ,      usr.usr_createby   AS "userCreatedBy"
 ,      usr.usr_emp_type   AS "userType"
 ,      act.act_key        AS "userOrganizationKey"
 ,      act.act_name       AS "userOrganizationName"
 ,      mgr.usr_login      AS "managerLoginName"
 ,      mgr.usr_first_name AS "managerFirstName"
 ,      mgr.usr_last_name  AS "managerLastName"
 ,      DECODE((SELECT DISTINCT 'Y' FROM recon_events rce WHERE rce.re_status = 'Creation Succeeded' AND rce.usr_key = usr.usr_key), 'Y', 'Reconciliation', DECODE((SELECT 'Y' FROM bulkload_usr blu WHERE blu.bl_usr_key = usr.usr_key), 'Y', 'BulkLoad'), 'Manual') AS "userSource"
 FROM   usr LEFT OUTER JOIN usr mgr ON usr.usr_manager_key = mgr.usr_key
 ,      act act
 WHERE  usr.act_key = act.act_key
 AND    (NVL(:p_loginName,        ' ') = ' ' OR usr.usr_login      LIKE upper(:p_loginName))
 AND    (NVL(:p_firstName,        ' ') = ' ' OR usr.usr_first_name LIKE :p_firstName)
 AND    (NVL(:p_lastName,         ' ') = ' ' OR usr.usr_last_name  LIKE :p_lastName)
 AND    (NVL(:p_status,           ' ') = ' ' OR usr.usr_status     LIKE :p_status)
 AND    (NVL(:p_type,             ' ') = ' ' OR usr.usr_emp_type   LIKE :p_type)
 AND    (NVL(:p_organizationName, ' ') = ' ' OR act.act_name       LIKE :p_organizationName)
 AND    TRUNC(usr.usr_create) BETWEEN NVL(:p_period_start, TO_DATE('01-JAN-1970', 'dd-MON-YYYY')) AND NVL(:p_period_end, TO_DATE(sysdate, 'dd-MON-YYYY'))
 ORDER BY usr.usr_login
 ,        act.act_name
 ,        usr.usr_create


 SELECT ugm.ugp_name              AS "roleName"
 ,      ugm.status                AS "membershipStatus"
 ,      ugm.upa_grp_eff_from_date AS "effectiveFromDate"
 ,      (SELECT usr_display_name FROM usr WHERE usr.usr_key = ugm.usg_createby_key) AS "createdBy"
 ,      (SELECT usr_display_name FROM usr WHERE usr.usr_key = ugm.usg_updateby_key) AS "updatedBy"
 FROM   upa_grp_membership ugm
 ,      upa_usr
 WHERE  upa_usr.upa_usr_key = ugm.upa_usr_key
 AND    upa_usr.usr_key = :userKey
 ORDER BY ugm.ugp_name
 ,        ugm.upa_grp_eff_from_date
 /