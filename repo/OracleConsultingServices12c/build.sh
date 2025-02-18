#!/bin/bash
#
# $Header$
#
# Program.....: bash
#
# Requirements:
#     The administration server is up and running.
#     The managed server is up and running
#
# Purpose.....:
#     This script builds the development artifacts in an Oracle Consulting
#     Identity and Access Management Workspace.
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
OCS_BASE=~/Project/OracleConsultingServices12c
HST_BASE=${OCS_BASE}/FoundationFramework
OIG_BASE=${OCS_BASE}/IdentityManager
OAM_BASE=${OCS_BASE}/AccessManager
ODS_BASE=${OCS_BASE}/DirectoryFramework
OIG_BUILD=${OCS_BASE}/IdentityTransition
OAM_BUILD=${OCS_BASE}/AccessTransition
ODS_BUILD=${OCS_BASE}/DirectoryTranistion
#
# Set environment variables for Oracle Product Installation
#
ORACLE_BASE=/opt/oracle/product
FMW_HOME=${ORACLE_BASE}/ide/12.2.1
JRF_HOME=${FMW_HOME}/oracle_common/modules
SOA_HOME=${FMW_HOME}/soa
WLS_HOME=${FMW_HOME}/wlserver
ANT_HOME=${JRF_HOME}/thirdparty/org.apache.ant/1.9.8.0.0/apache-ant-1.9.8
#
# Set environment variables for Runtime Environment
#
JDK_VENDOR=Sun
JDK_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0_202.jdk/Contents/Home
JDK_ARGS="-d64 -client"
JDK_OPTS=-Dfmw.env=intranet
#
# Set required ANT classpath
#
ANT_PATH=${JDK_HOME}/lib/tools.jar:${ANT_HOME}/lib/ant-launcher.jar:${ANT_HOME}/lib/ant.jar
ANT_MAIN=org.apache.tools.ant.launch.Launcher
#
# the ugly way
#
JRF_LIB=${FMW_HOME}/oracle_common/modules
WLS_LIB=${WLS_HOME}/modules
SOA_LIB=${SOA_HOME}/soa/modules

JAR_PATH=$(tr -d $'\n ' <<< "
  ${JRF_LIB}/javax.management.j2ee.jar
 :${JRF_HOME}/thirdparty/ant-contrib-1.0b3.jar
 :${JRF_LIB}/thirdparty/features/jsch.jar
 :${JRF_LIB}/org.apache.commons.logging_1.2.jar
 :${JRF_LIB}/oracle.logging-utils.jar
 :${JRF_LIB}/oracle.odl/ojdl.jar
 :${JRF_LIB}/oracle.dms/dms.jar
 :${JRF_LIB}/oracle.ucp.jar
 :${JRF_LIB}/oracle.javatools/resourcebundle.jar
 :${JRF_LIB}/oracle.javatools/javatools-nodeps.jar
 :${JRF_LIB}/oracle.javatools/javatools-annotations.jar
 :${JRF_LIB}/oracle.adf.share.ca/adf-share-base.jar
 :${JRF_LIB}/oracle.adf.share.ca/adf-share-ca.jar
 :${JRF_LIB}/oracle.adf.share/adflogginghandler.jar
 :${JRF_LIB}/oracle.adf.share/adf-share-support.jar
 :${JRF_LIB}/oracle.adf.model/adfm.jar
 :${JRF_LIB}/oracle.adf.businesseditor/adf-businesseditor-model.jar
 :${JRF_LIB}/oracle.mds/mdsrt.jar
 :${JRF_LIB}/oracle.mds/oramds.jar
 :${JRF_LIB}/oracle.jdbc/ojdbc8.jar
 :${JRF_LIB}/oracle.nlsrtl/orai18n-mapping.jar
 :${JRF_LIB}/oracle.jps/jps-mbeans.jar
 :${SOA_LIB}/commons-cli-1.1.jar
 :${SOA_LIB}/oracle.soa.fabric_11.1.1/fabric-runtime.jar
 :${SOA_LIB}/oracle.soa.fabric_11.1.1/soa-infra-tools.jar
 :${SOA_LIB}/oracle.soa.fabric_11.1.1/oracle-soa-client-api.jar
 :${SOA_LIB}/oracle.soa.mgmt_11.1.1/soa-infra-mgmt.jar
 :${SOA_LIB}/oracle.soa.workflow_11.1.1/bpm-services.jar
 :${WLS_LIB}/wlthint3client.jar
 :${WLS_LIB}/com.oracle.weblogic.package.jar
 :${WLS_LIB}/com.oracle.weblogic.ant.taskdefs.jar
 :${OIG_BASE}/CodeBasePlatform/12.2.1.3/lib/iam-platform-pluginframework.jar
 :${OIG_BASE}/CodeBasePlatform/12.2.1.3/lib/iam-platform-workflowservice.jar
 :${JRF_LIB}/oracle.jrf/jrf-api.jar
 :${OIG_BASE}/CodeBaseServer/12.2.1.3/lib/xlAPI.jar
 :${OIG_BASE}/CodeBaseServer/12.2.1.3/lib/xlDDM.jar
 :${OIG_BASE}/CodeBaseClient/12.2.1.3/lib/oimclient.jar
 :${HST_BASE}/hstFoundation/lib/hst-foundation.jar
 :${OIG_BASE}/oimFoundation/lib/ocs-foundation.jar
 :${OAM_BASE}/oamFoundation/lib/oam-foundation.jar
 :${ODS_BASE}/odsFoundation/lib/ocs-foundation.jar
 :${OIG_BUILD}/lib/hst-deployment.jar
 :${OIG_BUILD}/lib/oim-deployment.jar
 :${OAM_BUILD}/lib/oam-deployment.jar
 :${ODS_BUILD}/lib/ods-deployment.jar
 ")
#
# exceute
#
${JDK_HOME}/bin/java ${JDK_ARGS} ${JDK_OPTS} -classpath ${ANT_PATH}:${JAR_PATH} ${ANT_MAIN} $@
