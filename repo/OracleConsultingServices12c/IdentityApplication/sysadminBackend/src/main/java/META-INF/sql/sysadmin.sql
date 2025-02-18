# orchestration processes
orp.select=\
SELECT orp.id\
,      orp.name\
,      orp.bulkparentid\
,      orp.status\
,      orp.parentprocessid\
,      orp.depprocessid\
,      orp.entityid\
,      orp.entitytype\
,      orp.operation\
,      orp.stage\
,      orp.changetype\
,      orp.retry\
,      orp.createdon\
,      orp.modifiedon\
,      rownum AS rownumber \
FROM   orchprocess orp
