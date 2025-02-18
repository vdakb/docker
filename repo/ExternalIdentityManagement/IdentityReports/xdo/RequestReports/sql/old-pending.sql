/**
 ** This report view the details of all
 ** requests that have been in pending stage for more than two months
 **
 */
 SELECT req.REQUEST_KEY              AS REQUEST_KEY,
       req.REQUEST_DRAFT_KEY        AS REQUEST_DRAFT_KEY,
       req.REQUEST_PARENT_KEY       AS REQUEST_PARENT_KEY,
       req.REQUEST_ISPARENT         AS REQUEST_ISPARENT,
       req.REQUEST_ID               AS REQUEST_ID,
       req.REQUESTER_KEY            AS REQUESTER_KEY,
       req.REQUEST_JUSTIFICATION    AS REQUEST_JUSTIFICATION,
       req.REQUEST_EXECUTION_DATE   AS REQUEST_EXECUTION_DATE,
       req.REQUEST_CREATION_DATE    AS REQUEST_CREATION_DATE,
       req.REQUEST_END_DATE         AS REQUEST_END_DATE,
       req.REQUEST_MODEL_NAME       AS REQUEST_MODEL_NAME,
       req.REQUEST_TEMPLATE_NAME    AS REQUEST_TEMPLATE_NAME,
       req.REQUEST_STATUS           AS REQUEST_STATUS,
       req.REQUEST_STAGE            AS REQUEST_STAGE,
       req.REQUEST_ISDIRECT         AS REQUEST_ISDIRECT,
       req.REQUEST_BENEFICIARY_TYPE AS REQUEST_BENEFICIARY_TYPE,
       req.REQUEST_FAILURE_REASON   AS REQUEST_FAILURE_REASON,
       req.REQ_DEPENDS_ON_REQID     AS REQ_DEPENDS_ON_REQID,
       req.ORCHESTRATION_PROCESS_ID AS ORCHESTRATION_PROCESS_ID,
       req.EVENT_ID                 AS EVENT_ID
FROM request req
WHERE req.REQUEST_CREATION_DATE < (SELECT add_months(sysdate, -2) FROM dual)
  AND req.REQUEST_STAGE IN
      (SELECT REQUEST_STAGE_KEY
       FROM REQUEST_STAGES
       WHERE REQUEST_STAGE_NAME IN
             ('Request Initialized', 'Request Created', 'Obtaining Request Approval',
              'Obtaining Operation Approval', 'Request Approval Approved',
              'Operation Approval Approved', 'Operation Initiated',
              'Request Awaiting Completion', 'Operation Completed',
              'Post Operation Processing Initiated', 'Request Approval Auto Approved',
              'Operation Approval Auto Approved', 'Provide Information',
              'Request Awaiting Dependent Request Completion', 'Request Awaiting Approval',
              'Request Approved', 'Request Auto Approved', 'SoD check not initiated',
              'SoD check result pending', 'SoD check completed'))
/