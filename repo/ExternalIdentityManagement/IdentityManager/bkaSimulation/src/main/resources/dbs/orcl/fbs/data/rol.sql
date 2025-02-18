Rem ! $ORACLE_HOME/bin/sqlplus
Rem
Rem Program: sqlplus
Rem
Rem Requirements: sqlplus
Rem     SCHEMA MUST EXISTS
Rem
Rem Purpose:
Rem     This script creates Apigee Role Repository seeded roles.
Rem
Rem Usage Information:
Rem     sqlplus aptowner/<password>
Rem     @<path>/rol
Rem
Rem
Rem Revisions   Date        Editor      Comment
Rem -----------+-----------+-----------+-----------------------------------
Rem 1.0.0.0    2020-03-30  DSteding    First release version
Rem

PROMPT
PROMPT Create seeded role for Apigee Role Repository
/* API Gateway roles */
INSERT INTO agt_roles (
  name
, display_name
, description
)
VALUES (
  'apigateway.admin'
, 'ApiGateway Admin'
, 'Full access to ApiGateway and related resources.'
)
/
INSERT INTO agt_roles (
  name
, display_name
, description
)
VALUES (
  'apigateway.viewer'
, 'ApiGateway Viewer'
, 'Read-only access to ApiGateway and related resources.'
)
/
/* Apigee  roles */
INSERT INTO agt_roles (
  name
, display_name
, description
)
VALUES (
  'apigee.admin'
, 'Apigee Organization Admin'
, 'Full access to all apigee resource features.'
)
INSERT INTO agt_roles (
  name
, display_name
, description
)
VALUES (
  'apigee.analyticsAgent'
, 'Apigee Analytics Agent'
, 'Curated set of permissions for Apigee Universal Data Collection Agent to manage analytics for an Apigee Organization.'
)
/
INSERT INTO agt_roles (
  name
, display_name
, description
)
VALUES (
  'apigee.analyticsEditor'
, 'Apigee Analytics Editor'
, 'Analytics editor for an Apigee Organization.'
)
/
INSERT INTO agt_roles (
  name
, display_name
, description
)
VALUES (
  'apigee.analyticsViewer'
, 'Apigee Analytics Viewer'
, 'Analytics viewer for an Apigee Organization.'
)
/
INSERT INTO agt_roles (
  name
, display_name
, description
)
VALUES (
  'apigee.apiAdmin'
, 'Apigee Analytics Viewer'
, 'Full read/write access to all apigee API resources.'
)
/
INSERT INTO agt_roles (
  name
, display_name
, description
)
VALUES (
  'apigee.apiCreator'
, 'Apigee API Creator'
, 'Creator of apigee resources.'
)
/
INSERT INTO agt_roles (
  name
, display_name
, description
)
VALUES (
  'apigee.apiReader'
, 'Apigee API Reader'
, 'Reader of apigee resources.'
)
/
INSERT INTO agt_roles (
  name
, display_name
, description
)
VALUES (
  'apigee.deployer'
, 'Apigee Deployer'
, 'Deployer of apigee resources.'
)
/
INSERT INTO agt_roles (
  name
, display_name
, description
)
VALUES (
  'apigee.developerAdmin'
, 'Apigee Developer Admin'
, 'Developer admin of apigee resources.'
)
/
INSERT INTO agt_roles (
  name
, display_name
, description
)
VALUES (
  'apigee.environmentAdmin'
, 'Apigee Environment Admin'
, 'Full read/write access to apigee environment resources, including deployments.'
)
/
INSERT INTO agt_roles (
  name
, display_name
, description
)
VALUES (
  'apigee.portalAdmin'
, 'Apigee Portal Admin'
, 'Portal admin for an Apigee Organization.'
)
/
INSERT INTO agt_roles (
  name
, display_name
, description
)
VALUES (
  'apigee.readOnlyAdmin'
, 'Apigee Read-only Admin'
, 'Viewer of all apigee resources.'
)
/
INSERT INTO agt_roles (
  name
, display_name
, description
)
VALUES (
  'apigee.runtimeAgent'
, 'Apigee Runtime Agent'
, 'Curated set of permissions for a runtime agent to access Apigee Organization resources.'
)
/
INSERT INTO agt_roles (
  name
, display_name
, description
)
VALUES (
  'apigee.synchronizerManager'
, 'Apigee Synchronizer Manager'
, 'Curated set of permissions for a Synchronizer to manage environments in an Apigee Organization.'
)
/
INSERT INTO agt_roles (
  name
, display_name
, description
)
VALUES (
  'apigeeconnect.Admin'
, 'Apigee Connect Admin'
, 'Admin of Apigee Connect.'
)
/
INSERT INTO agt_roles (
  name
, display_name
, description
)
VALUES (
  'apigeeconnect.Agent'
, 'Apigee Connect Agent'
, 'Ability to set up Apigee Connect agent between external clusters and Google.'
)
/
/* Resource Manager roles */
INSERT INTO agt_roles (
  name
, display_name
, description
)
VALUES (
  'resourcemanager.organizationAdmin'
, 'Organization Administrator'
, 'Access to administer all resources belonging to the organization.'
)
/
INSERT INTO agt_roles (
  name
, display_name
, description
)
VALUES (
  'resourcemanager.organizationViewer'
, 'Organization Viewer'
, 'Provides access to view an organization.'
)
/
