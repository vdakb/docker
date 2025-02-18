Installation instructions:

####
# Import into OIM as follows:
###

moodle-adapters.xml
moodle-itresource.xml
moodle-lookup.xml
moodle-form.xml
moodle-resource.xml
moodle-resource.xml (!!Note: this one is imported twice because of missing adapter mappings)
moodle-scheduler.xml

###
# Execute the following:
###
. $HOME/scripts/identity.sh
export APP_SERVER=weblogic
export OIM_ORACLE_HOME=$ORACLE_HOME/idm
export MW_HOME=$ORACLE_HOME
export DOMAIN_HOME=$IGD_ASERVER_HOME
cd $OIM_ORACLE_HOME/server/bin
sh $OIM_ORACLE_HOME/server/bin/UploadJars.sh

#--- UPLOAD THIS FILE: bka-moodle-icfbundle.jar


###
# Manual Steps in UI:
###
New Sandbox
Create IT Resource from type "Moodle"
Create a Sysadmin console -> "Form"
Create an Application Instance with new Form.
Publish Sandbox
