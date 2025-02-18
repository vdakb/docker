#!\%System%\PowerShell
#
# $Header$
#
# Program.....: sh
#
# Requirements:
#     The database and the listener are up and running.
#     The administration server is up and running.
#     The managed server is up and running
#
# Purpose.....:
#     This script builds the development artifacts in an Oracle Consulting
#     Identity and Access MAnagement Workspace.
#
# -----------------------------------------------------------------------
# IMPORTANT
# Please note that script are NOT supported by Oracle Support and are
# available for reference only. You may need to modify the script
# according to your system and startup requirements. Oracle will not
# support the use of these, and you may be required to abandon the use of
# these scripts, if a problem arises.
# -----------------------------------------------------------------------
#
# Author......: Dieter Steding, Oracle Consulting Berlin
#
# Revision   Date        Editor      Comment
# ----------+-----------+-----------+------------------------------------
# 1.0.0.0    2019-05-27  dfteding    First release version
# ----------+-----------+-----------+------------------------------------

#
# Set environment variables for Oracle Consulting Workspace
#
Set-Variable -Name "ocs.base"    -Value "D:\Project\OracleConsultingServices12c"
Set-Variable -Name "hst.base"    -Value "${ocs.base}\FoundationFramework"
Set-Variable -Name "oim.base"    -Value "${ocs.base}\IdentityManager"
Set-Variable -Name "oam.base"    -Value "${ocs.base}\AccessMAnager"
Set-Variable -Name "ods.base"    -Value "${ocs.base}\DirectoryFramework"

#
# Set environment variables for Oracle Product Installation
#
Set-Variable -Name "oracle.base" -Value "D:\Oracle\product"
Set-Variable -Name "fmw.home"    -Value "${oracle.base}\ide\12.2.1"
Set-Variable -Name "jrf.home"    -Value "${fmw.home}\oracle_common\modules"
Set-Variable -Name "soa.home"    -Value "${fmw.home}\soa"
Set-Variable -Name "wls.home"    -Value "${fmw.home}\wlserver"
Set-Variable -Name "ant.home"    -Value "${jrf.home}\thirdparty\org.apache.ant\1.9.8.0.0\apache-ant-1.9.8"

#
# Set environment variables for Runtime Environment
#
Set-Variable -Name "jdk.vendor"  -Value "Sun"
Set-Variable -Name "jdk.home"    -Value "${oracle.base}\jdk\1.8.0\x64"
Set-Variable -Name "jdk.args"    -Value "-d64 -client"
Set-Variable -Name "jdk.opts"    -Value "-Dfmw.env=vwase052"

#
# Set required ANT classpath
#
Set-Variable -Name "ant.path" -Value "${jdk.home}\lib\tools.jar;${ant.home}\lib\ant-launcher.jar;${ant.home}\lib\ant.jar"
Set-Variable -Name "ant.main" -Value "org.apache.tools.ant.launch.Launcher"

#
# the ugly way
#
Set-Variable -Name "jrf.library" -Value "${fmw.home}\oracle_common\modules"
Set-Variable -Name "wls.library" -Value "${wls.home}\modules"
Set-Variable -Name "oig.library" -Value "${oim.home}"
Set-Variable -Name "soa.library" -Value "${soa.home}\soa\modules"

$jarpath  = "${jrf.library}\javax.management.j2ee.jar"
$jarpath += ";${jrf.home}\thirdparty\ant-contrib-1.0b3.jar"
$jarpath += ";${jrf.library}\thirdparty\features\jsch.jar"
$jarpath += ";${jrf.library}\org.apache.commons.logging_1.2.jar"
$jarpath += ";${jrf.library}\oracle.logging-utils.jar"
$jarpath += ";${jrf.library}\oracle.odl\ojdl.jar"
$jarpath += ";${jrf.library}\oracle.dms\dms.jar"
$jarpath += ";${jrf.library}\oracle.ucp.jar"
$jarpath += ";${jrf.library}\oracle.javatools\resourcebundle.jar"
$jarpath += ";${jrf.library}\oracle.javatools\javatools-nodeps.jar"
$jarpath += ";${jrf.library}\oracle.javatools\javatools-annotations.jar"
$jarpath += ";${jrf.library}\oracle.adf.share.ca\adf-share-base.jar"
$jarpath += ";${jrf.library}\oracle.adf.share.ca\adf-share-ca.jar"
$jarpath += ";${jrf.library}\oracle.adf.share\adflogginghandler.jar"
$jarpath += ";${jrf.library}\oracle.adf.share\adf-share-support.jar"
$jarpath += ";${jrf.library}\oracle.adf.model\adfm.jar"
$jarpath += ";${jrf.library}\oracle.adf.businesseditor\adf-businesseditor-model.jar"
$jarpath += ";${jrf.library}\oracle.mds\mdsrt.jar"
$jarpath += ";${jrf.library}\oracle.mds\oramds.jar"
$jarpath += ";${jrf.library}\oracle.jdbc\ojdbc8.jar"
$jarpath += ";${jrf.library}\oracle.nlsrtl\orai18n-mapping.jar"
$jarpath += ";${jrf.library}\oracle.jps\jps-mbeans.jar"
$jarpath += ";${soa.library}\commons-cli-1.1.jar"
$jarpath += ";${soa.library}\oracle.soa.fabric_11.1.1\fabric-runtime.jar"
$jarpath += ";${soa.library}\oracle.soa.fabric_11.1.1\soa-infra-tools.jar"
$jarpath += ";${soa.library}\oracle.soa.fabric_11.1.1\oracle-soa-client-api.jar"
$jarpath += ";${soa.library}\oracle.soa.mgmt_11.1.1\soa-infra-mgmt.jar"
$jarpath += ";${soa.library}\oracle.soa.workflow_11.1.1\bpm-services.jar"
$jarpath += ";${wls.library}\wlthint3client.jar"
$jarpath += ";${wls.library}\com.oracle.weblogic.package.jar"
$jarpath += ";${wls.library}\com.oracle.weblogic.ant.taskdefs.jar"
$jarpath += ";${oim.base}\CodeBasePlatform\12.2.1.3\lib\iam-platform-pluginframework.jar"
$jarpath += ";${oim.base}\CodeBasePlatform\12.2.1.3\lib\iam-platform-workflowservice.jar"
$jarpath += ";${jrf.library}\oracle.jrf\jrf-api.jar"
$jarpath += ";${oim.base}\CodeBaseServer\12.2.1.3\lib\xlAPI.jar"
$jarpath += ";${oim.base}\CodeBaseServer\12.2.1.3\lib\xlDDM.jar"
$jarpath += ";${oim.base}\CodeBaseClient\12.2.1.3\lib\oimclient.jar"
$jarpath += ";${hst.base}\hstFoundation\lib\hst-foundation.jar"
$jarpath += ";${hst.base}\hstDeployment\lib\hst-deployment.jar"
$jarpath += ";${oim.base}\oimFoundation\lib\ocs-foundation.jar"
$jarpath += ";${oim.base}\oimDeployment\lib\oim-deployment.jar"
$jarpath += ";${oam.base}\oamFoundation\lib\oam-foundation.jar"
$jarpath += ";${oam.base}\oamDeployment\lib\oam-deployment.jar"
$jarpath += ";${ods.base}\odsFoundation\lib\ocs-foundation.jar"
$jarpath += ";${ods.base}\odsDeployment\lib\ods-deployment.jar"

#
# exceute
#
$build = "${jdk.home}\bin\java ${jdk.args} '${jdk.opts}' -classpath '${ant.path};${jarpath}' ${ant.main} ${args}"
invoke-expression $build
