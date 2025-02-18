/**
 ** This report view the details (Requestor, Current Approver etc) of all
 ** requests with the current status.
 **
 ** Additionally it displays the details of all users that will be provisioned
 ** as a result of the request approval.
 **
 ** This report helps administrators in planning and prioritizing operational
 ** activities so that they may expedite the closure of pending requests.
 */
SELECT DISTINCT
       req.request_key           AS requestID
,      req.request_parent_key    AS parentRequestID
,      req.request_model_name    AS requestType
,      req.request_creation_date AS requestDate
,      req.request_status        AS requestStatus
,      usr.usr_login             AS requesterLogin
,      usr.usr_first_name        AS requesterFirstName
,      usr.usr_last_name         AS requesterLastName
,      usr.usr_display_name      AS requesterName
,      ben.usr_login             AS beneficiaryLogin
,      ben.usr_first_name        AS beneficiaryFirstName
,      ben.usr_last_name         AS beneficiaryLastName
FROM   request req
,      (SELECT ben.usr_login
        ,      ben.usr_first_name
        ,      ben.usr_last_name
        FROM   request             req3
        ,      request_beneficiary rbn
        ,      usr                 ben
        WHERE  req3.request_key    = rbn.request_key
        AND    rbn.beneficiary_key = ben.usr_key
        UNION
        SELECT ben.usr_login
        ,      ben.usr_first_name
        ,      ben.usr_last_name
        FROM   request             req3
        ,      request_entities    ree1
        ,      usr                 ben
        WHERE  req3.request_key    = ree1.request_key
        AND    ree1.entity_key     = ben.usr_key
       ) ben
,      usr usr JOIN request req1 ON usr.usr_key = NVL(req1.request_key, 1)
WHERE  req1.request_key = req.request_key
AND    (NVL(:p_request,              ' ') = ' ' OR req.request_key         LIKE :p_request)
AND    (NVL(:p_parent,               ' ') = ' ' OR req.request_parent_key  LIKE :p_parent)
AND    (NVL(:p_status,               ' ') = ' ' OR req.request_status      LIKE :p_status)
AND    (NVL(:p_requesterLogin,       ' ') = ' ' OR usr.usr_login           LIKE UPPER(:p_requesterLogin))
AND    (NVL(:p_requesterFirstName,   ' ') = ' ' OR usr.usr_first_name      LIKE :p_requesterFirstName)
AND    (NVL(:p_requesterLastName,    ' ') = ' ' OR usr.usr_last_name       LIKE :p_requesterLastName)
AND    (NVL(:p_beneficiaryLogin,     ' ') = ' ' OR ben.usr_login           LIKE UPPER(:p_beneficiaryLogin))
AND    (NVL(:p_beneficiaryFirstName, ' ') = ' ' OR ben.usr_first_name      LIKE :p_beneficiaryFirstName)
AND    (NVL(:p_beneficiaryLastName,  ' ') = ' ' OR ben.usr_last_name       LIKE :p_beneficiaryLastName)
AND    TRUNC(req.request_creation_date) BETWEEN NVL(:p_period_start, TO_DATE('01-JAN-1970', 'dd-MON-YYYY')) AND NVL(:p_period_end, TO_DATE(sysdate, 'dd-MON-YYYY'))
/

SELECT det.entity_type  AS entityType
,      det.entity_name  AS entityName
FROM   (SELECT req.request_key     request_id
        ,      rbe.rbe_entity_name entity_name
        ,      rbe.rbe_entity_type entity_type
        FROM   request_beneficiary_entities rbe
        ,      request req
        WHERE  rbe.rbe_request_key = req.request_key
        UNION
        SELECT req.request_key        request_id
        ,      red.entity_field_value entity_name
        ,      red.entity_field_name  entity_type
        FROM   request_entity_data red
        ,      request_entities    rqe
        ,      request             req
        WHERE  red.request_entity_key = rqe.request_entity_key
        AND    rqe.request_key        = req.request_key
        AND    red.entity_field_name != 'Organization'
        AND    red.entity_field_name != 'User Manager'
        UNION
        SELECT req.request_key       request_id
        ,      act.act_name          entity_name
        ,      red.entity_field_name entity_type
        FROM   request_entity_DATA red
        ,      request_entities rqe
        ,      request req
        ,      act
        WHERE  red.request_entity_key = rqe.request_entity_key
        AND    rqe.request_key        = req.request_key
        AND    red.entity_field_VALUE = act.act_key
        AND    red.entity_field_name  = 'Organization'
        UNION
        SELECT req.request_key       request_id
        ,      usr.usr_login         entity_name
        ,      red.entity_field_name entity_type
        FROM   request_entity_data red
        ,      request_entities    rqe
        ,      request             req
        ,      usr usr
        WHERE  red.request_entity_key = rqe.request_entity_key
        AND    rqe.request_key        = req.request_key
        AND    red.entity_field_VALUE = usr.usr_key
        AND    red.entity_field_name  = 'User Manager'
       ) det
WHERE  det.request_id = :requestID
/