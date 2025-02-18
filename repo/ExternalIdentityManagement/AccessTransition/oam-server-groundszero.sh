#!/bin/sh
#
# $Header$
#
# Program.....: sh
#
# Requirements:
#
# Purpose.....:
#     Script to backup, restore and customize the Oracle Access Server
#     enterprise archive according to the requirements of P20 program.
#
# -----------------------------------------------------------------------
# IMPORTANT
# Please note that script are NOT supported by Oracle Support and are
# available for reference only. You may need to modify the script
# according to your system and startup requirements. Oracle will not
# support the use of these, and you may be available to abandon the use of
# these scripts, if a problem arises.
# -----------------------------------------------------------------------
#
# Author......: Dieter Steding, Oracle Consulting Berlin
#
#
# Revision   Date        Editor      Comment
# ----------+-----------+-----------+------------------------------------
# 1.0.0.0    2024-06-10  DSteding    First release version
#

#
# Set environment variables common for Oracle Identity and Access Management Domains
#
. ./oam-server-groundszero.env

#
# Helper to printout a console message.
#
message() {
  printf "Access Server Customization | %5s | %20s | %s\n" "${STATE}" "${1}" "${2}" >> ${WORK}/oam-server-customization.log
  printf "Access Server Customization | \e[38;5;207m%5s\e[0m | \e[38;5;207m%20s\e[0m | %s\n" "${STATE}" "${1}" "${2}"
}
#
# Set and persists the state
#
state() {
  STATE=${1}
  echo -n ${STATE} > ${WORK}/oam-server-customization.state
}
#
# Verify application
#
application() {
  if [ -f ${APP_FOLDER}/${COMPONENT_FILE} ]
  then
    state "2.1"
    message "Enterprise Archive" "found"
  else
    state "-2.1"
    message "Enterprise Archive" "not found"
  fi
}
#
verify() {
  unzip -j ${APP_FOLDER}/${COMPONENT_FILE} "META-INF/application.xml" -d ${TMP_SERVER}/${COMPONENT_FILE}/META-INF >> ${WORK}/oam-server-customization.log
  if [ $(grep "${EXT_CONTEXT}" "${TMP_SERVER}/${COMPONENT_FILE}/META-INF/application.xml") ]
  then
    state "-3.1"
    message "Enterprise Archive" "customization not available"
  else
    state "3.1"
    message "Enterprise Archive" "customization available"
  fi
  rm -rf ${TMP_SERVER}
}
#
# Create the initial backup
#
backup() {
  if [ "${STATE}" == "2.1" ]
  then
    if [ -f ${APP_FOLDER}/.oam-server.origin ]
    then
      #
      # Ask user for confirmation
      #
      ync="x"
      echo -e "About to backup snapshot \e[38;5;207m${COMPONENT_FILE}\e[0m of Access Server application"
      while true
      do
        echo -e -n "Do you really want to override staged \e[38;5;207m.oam-server.origin\e[0m with \e[38;5;207m${COMPONENT_FILE}\e[0m (yes or no or cancel): "
        read ync
        case $ync in
          [Yy]* ) state "2.2"
                  message "Enterprise Archive" "backup override"
                  break
          ;;
          #
          # skip backup
          #
          [Nn]* ) state "-2.2"
                  message "Enterprise Archive" "backup skipped"
                  break
          ;;
          #
          # exit if user canceled
          #
          [Cc]* ) state "-2.2"
                  echo -e "\e[38;5;207mAborted\e[0m by user request"
                  exit 1
          ;;
          *     ) echo -e "Wrong answer please give only (yes or no or cancel) or the abreviation of both."
        esac
      done
    fi
    if [ "${STATE}" == "2.2" ]
    then
      verify
      # backup only if customization is not installed
      if [ "${STATE}" == "3.1" ]
      then
        message "Enterprise Archive" "backing up origin ..."
        cp ${APP_FOLDER}/${COMPONENT_FILE} ${APP_FOLDER}/.oam-server.origin
        touch ${APP_FOLDER}/.oam-server.origin -r ${APP_FOLDER}/${COMPONENT_FILE}
        message "Enterprise Archive" "origin backed up..."
      fi
    fi
  else
    state "-2.2"
    message "Enterprise Archive" "application file missing"
  fi
}
#
# Restore the initial backup
#
restore() {
  if [ "${STATE}" == "2.2" ]
  then
    if [ -f ${APP_FOLDER}/.oam-server.origin ]
    then
      message "Enterprise Archive" "restoring origin ..."
      cp ${APP_FOLDER}/.oam-server.origin ${APP_FOLDER}/${COMPONENT_FILE}
      touch ${APP_FOLDER}/${COMPONENT_FILE} -r ${APP_FOLDER}/.oam-server.origin
      message "Enterprise Archive" "origin restored"
    else
      state "-2.2"
      message "Enterprise Archive" "backup not found"
    fi
  else
    state "-2.1"
    message "Enterprise Archive" "application file missing"
  fi
}
#
# Extract the current application
#
inflating() {
  if [ ! -d "${TMP_SERVER}" ]
  then
    mkdir -p ${TMP_SERVER}
  fi
  application
  if [ "${STATE}" == "2.1" ]
  then
    if [ -d ${TMP_SERVER}/${COMPONENT_FILE} ]
    then
      message "Enterprise Archive" "deleting previously created stage ..."
      rm -rf ${TMP_SERVER}/${COMPONENT_FILE}
      message "Enterprise Archive" "previously created stage deleted"
    fi
    message "Enterprise Archive" "inflating ..."
    unzip "${APP_FOLDER}/${COMPONENT_FILE}" -d "${TMP_SERVER}/${COMPONENT_FILE}" >> ${WORK}/oam-server-customization.log
    message "Enterprise Archive" "inflated"
    if [ $(grep "${EXT_CONTEXT}" "${TMP_SERVER}/${COMPONENT_FILE}/META-INF/application.xml") ]
    then
      state "-3.2"
      message "Enterprise Archive" "customized"
    else
      state "3.2"
      message "Enterprise Archive" "not customized"
    fi
  else
    message "Enterprise Archive" "application file missing"
  fi
}
#
# Install the customization by registering the extension appication into
# the inflated application
#
register() {
  if [ "${STATE}" == "3.3" ]
  then
    message "Enterprise Archive" "register application module ..."
    # as a one liner to keep it simple
    sed -i '/<library-directory>/i \ \ \ \ <module>\n\ \ \ \ \ \ <web>\n\ \ \ \ \ \ \ \ <web-uri>oidc.extension.access.module.war<\/web-uri>\n\ \ \ \ \ \ \ \ <context-root>\/oidc-extension<\/context-root>\n\ \ \ \ \ \ <\/web>\n\ \ \ \ <\/module>' ${TMP_SERVER}/${COMPONENT_FILE}/META-INF/application.xml
    state "3.4"
    message "Enterprise Archive" "application module registered"
  else
    message "Enterprise Archive" "invalid state for register"
  fi
}
#
# Remove the customization by unregistering the extension appication from
# the inflated application
#
unregister() {
  if [ "${STATE}" == "3.4" ]
  then
    message "Enterprise Archive" "unregister application module ..."
    #
    # The first line uses a range between two module tags (inclusive range).
    # The `H' command appends the line to the Hold buffer.
    # The second line tests whether the line read in has the closing tag.
    # If it does, the line buffer is cleared and swapped with the Hold buffer.
    #
    # sed -n '/<module>/,/<\/module>/{ H /<\/module>/ { s/.*//;x /\/oidc-extension//d p } }' ./application.txt
    message "Enterprise Archive" "THIS IS NOT WORKING AT THE TIME BEING"
    message "Enterprise Archive" "application module unregistered"
  else
    message "Enterprise Archive" "invalid state for unregister"
  fi
}
#
# Install the customization by copying the extension file to
# the inflated application
#
copy() {
  if [ "${STATE}" == "3.2" ] || [ "${STATE}" == "-3.2" ]
  then
    if [ -f ${EXT_FOLDER}/${EXT_FILE} ]
    then
      message "Enterprise Archive" "copy extension ..."
      cp ${EXT_FOLDER}/${EXT_FILE} ${TMP_SERVER}/${COMPONENT_FILE}
      state "3.3"
      message "Enterprise Archive" "extension copied"
    else
      state "-3.3"
      message "Enterprise Archive" "extension ${EXT_FOLDER}/${EXT_FILE} not found"
    fi
  else
    message "Enterprise Archive" "invalid state for copy"
  fi
}
#
# Removing the customization by deleting the extension file from
# the inflated application
#
delete() {
  if [ "${STATE}" == "3.3" ]
  then
    if [ -f ${TMP_SERVER}/${COMPONENT_FILE}/${EXT_FILE} ]
    then
      message "Enterprise Archive" "deleting customization ..."
      rm -f ${TMP_SERVER}/${COMPONENT_FILE}/${EXT_FILE}
      message "Enterprise Archive" "customization deleted"
    else
      state "-3.3"
      message "Enterprise Archive" "extension ${TMP_SERVER}/${COMPONENT_FILE}/${EXT_FILE} not found"
    fi
  else
    message "Enterprise Archive" "invalid state for delete"
  fi
}
#
# Repackaging the customized application
#
archive() {
  if [ "${STATE}" == "3.3" ] || [ "${STATE}" == "3.4" ] 
  then
    state "3.5"
    message "Enterprise Archive" "building ..."
    jar cvMf ${APP_FOLDER}/${COMPONENT_FILE} -C ${TMP_SERVER}/${COMPONENT_FILE} . >> ${WORK}/oam-server-customization.log
    message "Enterprise Archive" "build"
  else
    message "Enterprise Archive" "invalid state for archive"
  fi
}
#
# Install the customization by copying the application file to
# the inflated application and extending the deployment descriptors
# accordingly
#
# As the result the application enterprise archive is repackaged.
#
install() {
  if [ ${STATE} == "3.2" ]
  then
    if [ -d ${TMP_SERVER} ]
    then
      message "Enterprise Archive" "installimg customization ..."
      copy
      register
      archive
      state "3.6"
      message "Enterprise Archive" "customization installed"
    else
      state "-3.6"
      message "Enterprise Archive" "unable to find ${TMP_SERVER}"
    fi
  else
    if [ ${STATE} == "-3.2" ]
    then
      message "Enterprise Archive" "customization update available"
    else
      message "Enterprise Archive" "called with invalid state"
    fi
  fi
}
#
# Update the customization by copying the application file to
# the inflated application
# The application deployment descriptor is left untouched.
#
# As the result the application enterprise archive is repackaged.
#
update() {
  if [ ${STATE} == "-3.2" ]
  then
    if [ -d ${TMP_SERVER} ]
    then
      message "Enterprise Archive" "updating customization ..."
      copy
      archive
      state "3.6"
      message "Enterprise Archive" "customization updated"
    else
      state "-3.6"
      message "Enterprise Archive" "unable to find ${TMP_SERVER}"
    fi
  else
    if [ ${STATE} == "3.2" ]
    then
      message "Enterprise Archive" "customization install available"
    else
      message "Enterprise Archive" "called with invalid state"
    fi
  fi
}
#
# Remove the customization by deleting the application file from
# the inflated application and removing the module from the
# application deployment descriptor.
#
# As the result the application enterprise archive is repackaged.
#
remove() {
  if [ ${STATE} == "3.2" ]
  then
    if [ -d ${TMP_SERVER} ]
    then
      message "Enterprise Archive" "deleting customization ..."
      unregister
      delete
      message "Enterprise Archive" "customization deleted"
    fi
  fi
}
#
# Cleanup artifacts created by the ceveral stages
#
cleanup() {
  message "Workspace" "clean up"
  rm -rf ${TMP_FOLDER}
  rm -rf ${WORK}/*.md5
  message "Workspace" "cleaned up"
}
#
# Resets the eintire stage process
#
reset() {
  cleanup
  state "-99"
  rm -rf ${WORK}/*.log
}

message "Workspace" "=================================="
message "Workspace" $(date -d now +'%Y-%m-%dT%H:%M:%S')
message "Workspace" "=================================="
#
# Initialize customization state
#
if [ ! -d ${WORK} ]
then
  mkdir -p ${WORK}
fi
touch ${WORK}/oam-server-customization.log
if [ ! -d ${TMP_SERVER}/${COMPONENT_FILE}/META-INF ]
then
 mkdir -p ${TMP_SERVER}/${COMPONENT_FILE}/META-INF
fi
touch ${WORK}/oam-server-customization.log
if [ -f ${WORK}/oam-server-customization.state ]
then
  STATE=`cat ${WORK}/oam-server-customization.state`
else
  STATE="-99"
fi
#
# Dispatch command
#
case "${1}" in
  backup)   application
            backup
            reset
  ;;
  restore)  application
            restore
            reset
  ;;
  precheck) inflating
            cleanup
  ;;
  install)  if [ ${STATE} == "3.2" ] || [ ${STATE} == "-3.2" ]
            then
              inflating
              install
            else
              message "Workspace" "precheck is a required predecessor"
            fi
            cleanup
  ;;
  update)   if [ ${STATE} == "3.2" ] || [ ${STATE} == "-3.2" ]
            then
              inflating
              update
            else
              message "Workspace" "precheck is a required predecessor"
            fi
            cleanup
  ;;
  remove)   if [ ${STATE} == "3.2" ] || [ ${STATE} == "-3.2" ]
            then
              inflating
              remove
            else
              message "Workspace" "precheck is a required predecessor"
            fi
            cleanup
  ;;
  reset)    reset
  ;;
  status)   message "Workspace" "Last State ${STATE}"
  ;;
  *)        message "usage: $0" "(backup | restore | precheck | install | update | reset | status | help)"
esac