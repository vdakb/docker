Rem ! $ORACLE_HOME/bin/sqlplus
Rem
Rem Program: sqlplus
Rem
Rem Requirements: sqlplus
Rem     SCHEMA MUST EXISTS
Rem
Rem Purpose:
Rem     This script creates Pivotal User Repository seeded roles.
Rem
Rem Usage Information:
Rem     sqlplus pcfschema/<password>
Rem     @<path>/grp
Rem
Rem
Rem Revisions   Date        Editor      Comment
Rem -----------+-----------+-----------+-----------------------------------
Rem 1.0.0.0    2020-03-30  DSteding    First release version
Rem

PROMPT
PROMPT Create seeded role for Pivotal User Repository
INSERT INTO pct_grp (
  rolename
, description
)
VALUES (
  'uaa.none'
, 'This scope indicates that this client will not be performing actions on behalf of a user.'
)
/
INSERT INTO pct_grp (
  rolename
, description
)
VALUES (
  'uaa.user'
, 'This scope indicates that this is a user. It is required in the token if submitting a GET request to the OAuth 2 /authorize endpoint.'
)
/
INSERT INTO pct_grp (
  rolename
, description
)
VALUES (
  'uaa.resource'
, 'This scope indicates that this is a resource server, used for the /introspect endpoint.'
)
/
INSERT INTO pct_grp (
  rolename
, description
)
VALUES (
  'uaa.admin'
, 'This scope indicates that this is the superuser.'
)
/
INSERT INTO pct_grp (
  rolename
, description
)
VALUES (
  'scim.read'
, 'This scope gives admin read access to all SCIM endpoints, /Users, and /Groups.'
)
/
INSERT INTO pct_grp (
  rolename
, description
)
VALUES (
  'scim.write'
, 'This scope gives admin write access to all SCIM endpoints, /Users, and /Groups.'
)
/
INSERT INTO pct_grp (
  rolename
, description
)
VALUES (
  'scim.create'
, 'This scope gives the ability to create a user with a POST request to the /Users endpoint, but not to modify, read, or delete users.'
)
/
INSERT INTO pct_grp (
  rolename
, description
)
VALUES (
  'scim.userids'
, 'This scope is required to convert a username and origin into a user ID and vice versa.'
)
/
INSERT INTO pct_grp (
  rolename
, description
)
VALUES (
  'scim.invite'
, 'This scope is required to participate in invitations using the /invite_users endpoint.'
)
/
INSERT INTO pct_grp (
  rolename
, description
)
VALUES (
  'password.write'
, 'This admin scope gives the ability to change a user’s password.'
)
/
INSERT INTO pct_grp (
  rolename
, description
)
VALUES (
  'groups.update'
, 'This scope gives the ability to update a group. This ability can also be provided by the broader scim.write scope.'
)
/
INSERT INTO pct_grp (
  rolename
, description
)
VALUES (
  'openid'
, 'This scope is required to access the /userinfo endpoint. It is intended for OpenID clients.'
)
/
INSERT INTO pct_grp (
  rolename
, description
)
VALUES (
  'idps.read'
, 'This scope gives read access to retrieve identity providers from the /identity-providers endpoint.'
)
/
INSERT INTO pct_grp (
  rolename
, description
)
VALUES (
  'idps.write'
, 'This scope gives the ability to create and update identity providers from the /identity-providers endpoint.'
)
/
INSERT INTO pct_grp (
  rolename
, description
)
VALUES (
  'clients.read'
, 'This scope gives the ability to read information about clients.'
)
/
INSERT INTO pct_grp (
  rolename
, description
)
VALUES (
  'clients.write'
, 'This scope is required to create and modify clients. The scopes are prefixed with the scope holder’s client ID. For example, id:testclient authorities:client.write gives the ability to create a client that has scopes with the testclient. prefix. Authorities are limited to uaa.resource.'
)
/
INSERT INTO pct_grp (
  rolename
, description
)
VALUES (
  'clients.admin'
, 'This scope gives the ability to create, modify, and delete clients.'
)
/
INSERT INTO pct_grp (
  rolename
, description
)
VALUES (
  'clients.secret'
, 'This admin scope is required to change the password of a client.'
)
/
INSERT INTO pct_grp (
  rolename
, description
)
VALUES (
  'zones.read'
, 'This scope is required to invoke the /identity-zones endpoint to read identity zones.'
)
/
INSERT INTO pct_grp (
  rolename
, description
)
VALUES (
  'zones.write'
, 'This scope is required to invoke the /identity-zones endpoint to create and update identity zones.'
)
/
INSERT INTO pct_grp (
  rolename
, description
)
VALUES (
  'zones.ZONE-ID.read'
, 'This scope permits reading the given identity zone. This scope is used with the X-Identity-Zone-Id header.'
)
/
INSERT INTO pct_grp (
  rolename
, description
)
VALUES (
  'zones.ZONE-ID.admin'
, 'This scope permits operations in a designated zone, such as creating identity providers or clients in another zone, by authenticating against the default zone. This scope is used with the X-Identity-Zone-Id header.'
)
/
INSERT INTO pct_grp (
  rolename
, description
)
VALUES (
  'zones.ZONE-ID.scim.read'
, 'This scope translates into scim.read after zone switch completes. This scope is used with the X-Identity-Zone-Id header.'
)
/
INSERT INTO pct_grp (
  rolename
, description
)
VALUES (
  'zones.ZONE-ID.scim.write'
, 'This scope translates into scim.write after zone switch completes. This scope is used with the X-Identity-Zone-Id header.'
)
/
INSERT INTO pct_grp (
  rolename
, description
)
VALUES (
  'zones.ZONE-ID.scim.create'
, 'This scope translates into scim.write after zone switch completes. This scope is used with the X-Identity-Zone-Id header.'
)
/
INSERT INTO pct_grp (
  rolename
, description
)
VALUES (
  'zones.ZONE-ID.idps.read'
, 'This scope translates into idps.read after zone switch completes. This scope is used with the X-Identity-Zone-Id header.'
)
/


INSERT INTO pct_grp (
  rolename
, description
)
VALUES (
  'zones.ZONE-ID.clients.read'
, 'This scope translates into clients.read after zone switch completes. This scope is used with the X-Identity-Zone-Id header.'
)
/
INSERT INTO pct_grp (
  rolename
, description
)
VALUES (
  'zones.ZONE-ID.clients.write'
, 'This scope translates into clients.write after zone switch completes. This scope is used with the X-Identity-Zone-Id header.'
)
/
INSERT INTO pct_grp (
  rolename
, description
)
VALUES (
  'zones.ZONE-ID.clients.admin'
, 'This scope translates into clients.admin after zone switch completes. This scope is used with the X-Identity-Zone-Id header.'
)
/


INSERT INTO pct_grp (
  rolename
, description
)
VALUES (
  'scim.zones'
, 'This is a limited scope that only allows adding a user to, or removing a user from, zone management groups under the path /Groups/zones.'
)
/
INSERT INTO pct_grp (
  rolename
, description
)
VALUES (
  'oauth.login'
, 'This scope is used to indicate a login app, such as external login servers, can perform trusted operations, such as creating users not authenticated in the UAA.'
)
/
INSERT INTO pct_grp (
  rolename
, description
)
VALUES (
  'oauth.approval'
, ' 	/approvals endpoint. This scope is required to approve or reject clients to act on a user’s behalf. This is a default scope defined in the uaa.yml file.'
)
/

COMMIT
/