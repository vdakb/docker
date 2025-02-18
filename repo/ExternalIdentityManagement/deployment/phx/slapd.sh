#
# Install OpenLDAP Packages
#
# openldap         – A package containing the libraries necessary to run the
#                    OpenLDAP server and client applications.
# openldap-servers – A package containing both the services and utilities to
#                    configure and run an LDAP server.
#                    This includes the Standalone LDAP Daemon, slapd.
# openldap-clients – A package containing the command line utilities for
#                    viewing and modifying directories on an LDAP server.
#
yum install openldap openldap-servers openldap-clients

systemctl stop slapd.service
rm -rf /etc/openldap/slapd.d/*
rm -rf /dbs/ldap/pxd
rm -rf /dbs/ldap/pxt
rm -rf /dbs/ldap/pxq
rm -rf /dbs/ldap/pxp
mkdir -p /dbs/ldap/pxd
mkdir -p /dbs/ldap/pxt
mkdir -p /dbs/ldap/pxq
mkdir -p /dbs/ldap/pxp
cp /usr/share/openldap-servers/DB_CONFIG.example  /dbs/ldap/pxd/DB_CONFIG
cp /usr/share/openldap-servers/DB_CONFIG.example  /dbs/ldap/pxt/DB_CONFIG
cp /usr/share/openldap-servers/DB_CONFIG.example  /dbs/ldap/pxq/DB_CONFIG
cp /usr/share/openldap-servers/DB_CONFIG.example  /dbs/ldap/pxp/DB_CONFIG

# Generate initial database:
echo "" | slapadd -f /etc/openldap/slapd.conf

# Set file permission to default user and group
chown -R ldap.ldap /etc/openldap/slapd.conf /etc/openldap/slapd.d  /dbs/ldap
chmod -R u+rwx /etc/openldap/slapd.d

# Start service
systemctl start slapd.service

ldapsearch -h hardy.vm.oracle.com -p 389 -D "cn=Directory Manager,dc=pxd,dc=bka,dc=bund,dc=de" -w Welcome1 -b "ou=System,dc=pxd,dc=bka,dc=bund,dc=de" cn=phoenix memberof
ldapsearch -h hardy.vm.oracle.com -p 389 -D "cn=oimadmin,ou=System,dc=pxd,dc=bka,dc=bund,dc=de" -w Welcome1 -b "ou=People,dc=pxd,dc=bka,dc=bund,dc=de" objectClass=* cn,uid,memberof
ldapsearch -h hardy.vm.oracle.com -p 389 -D "cn=phoenix,ou=System,dc=pxd,dc=bka,dc=bund,dc=de" -w Welcome1 -b "dc=pxd,dc=bka,dc=bund,dc=de" objectClass=* entryUUID
