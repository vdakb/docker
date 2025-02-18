/*
    Oracle Deutschland GmbH

    This software is the confidential and proprietary information of
    Oracle Corporation. ("Confidential Information").  You shall not
    disclose such Confidential Information and shall use it only in
    accordance with the terms of the license agreement you entered
    into with Oracle.

    ORACLE MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
    SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
    IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
    PURPOSE, OR NON-INFRINGEMENT. ORACLE SHALL NOT BE LIABLE FOR ANY DAMAGES
    SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
    THIS SOFTWARE OR ITS DERIVATIVES.

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Foundation Shared Library
    Subsystem   :   Common Shared LDIF Facilities

    File        :   LDIFNormalize.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    LDIFNormalize.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-06-23  DSteding    First release version
*/

package oracle.iam.identity.utility.file;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Enumeration;

import java.io.File;
import java.io.FileFilter;

import javax.naming.Binding;

import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

import oracle.hst.foundation.utility.FileSystem;
import oracle.hst.foundation.utility.StringUtility;

////////////////////////////////////////////////////////////////////////////////
// class LDIFNormalize
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** Normalizaing a LDAP Data Interchange Format (LDIF) file by removing all
 ** operational attributes.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class LDIFNormalize {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String      FOLDER_LONG          = "--folder";
  static final String      FOLDER_SHORT         = "-f";
  static final String      SOURCE_CONTEXT_LONG  = "--sourceContext";
  static final String      SOURCE_CONTEXT_SHORT = "-sc";
  static final String      TARGET_CONTEXT_LONG  = "--targetContext";
  static final String      TARGET_CONTEXT_SHORT = "-tc";
  static final String      SOURCE_DOMAIN_LONG   = "--sourceDomain";
  static final String      SOURCE_DOMAIN_SHORT  = "-sd";
  static final String      TARGET_DOMAIN_LONG   = "--targetDomain";
  static final String      TARGET_DOMAIN_SHORT  = "-td";
  final static String      EXTENSION            = "ldif";

  final static Set<String> OPERATIONAL  = new HashSet<String>();

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  private static String    folderName;
  private static String    sourceContext;
  private static String    targetContext;
  private static String    sourceDomain;
  private static String    targetDomain;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final File       folder;

  private final List<File> file         = new ArrayList<File>();
  private final FileFilter filter       = new Filter(EXTENSION);

  //////////////////////////////////////////////////////////////////////////////
  // static init block
  //////////////////////////////////////////////////////////////////////////////

  static {
    // operational attributes of a Microsoft Active Directory in alphabetical
    // order
    OPERATIONAL.add("accountExpires");
    OPERATIONAL.add("altRecipientBL");
    OPERATIONAL.add("authOrigBL");
    OPERATIONAL.add("autoReplyMessage");
    OPERATIONAL.add("badPwdCount");
    OPERATIONAL.add("badPasswordTime");
    OPERATIONAL.add("deletedItemFlags");
    OPERATIONAL.add("delivContLength");
    OPERATIONAL.add("deliverAndRedirect");
    OPERATIONAL.add("directReports");
    OPERATIONAL.add("distinguishedName");
    OPERATIONAL.add("dLMemDefault");
    OPERATIONAL.add("dSCorePropagationData");
    OPERATIONAL.add("extensionAttribute1");
    OPERATIONAL.add("extensionAttribute2");
    OPERATIONAL.add("extensionAttribute3");
    OPERATIONAL.add("extensionAttribute4");
    OPERATIONAL.add("extensionAttribute5");
    OPERATIONAL.add("extensionAttribute6");
    OPERATIONAL.add("extensionAttribute7");
    OPERATIONAL.add("extensionAttribute8");
    OPERATIONAL.add("extensionAttribute9");
    OPERATIONAL.add("extensionAttribute10");
    OPERATIONAL.add("extensionAttribute11");
    OPERATIONAL.add("extensionAttribute12");
    OPERATIONAL.add("extensionAttribute13");
    OPERATIONAL.add("extensionAttribute14");
    OPERATIONAL.add("extensionData");
    OPERATIONAL.add("garbageCollPeriod");
    OPERATIONAL.add("gPLink");
    OPERATIONAL.add("gPOptions");
    OPERATIONAL.add("homeMDB");
    OPERATIONAL.add("homeMTA");
    OPERATIONAL.add("internetEncoding");
    OPERATIONAL.add("lastLogoff");
    OPERATIONAL.add("lastLogon");
    OPERATIONAL.add("lastLogonTimestamp");
    OPERATIONAL.add("legacyExchangeDN");
    OPERATIONAL.add("lockoutTime");
    OPERATIONAL.add("logonCount");
    OPERATIONAL.add("logonHours");
    OPERATIONAL.add("memberOf");
    OPERATIONAL.add("mailNickname");
    OPERATIONAL.add("managedObjects");
    OPERATIONAL.add("manager");
    OPERATIONAL.add("mAPIRecipient");
    OPERATIONAL.add("mDBOverQuotaLimit");
    OPERATIONAL.add("mDBOverHardQuotaLimit");
    OPERATIONAL.add("mDBOverHardQuotaLimit");
    OPERATIONAL.add("mDBStorageQuota");
    OPERATIONAL.add("mDBUseDefaults");
    OPERATIONAL.add("msDS-SupportedEncryptionTypes");
    OPERATIONAL.add("msExchADCGlobalNames");
    OPERATIONAL.add("msExchAddressBookFlags");
    OPERATIONAL.add("msExchArchiveQuota");
    OPERATIONAL.add("msExchArchiveWarnQuota");
    OPERATIONAL.add("msExchAssistantName");
    OPERATIONAL.add("msExchALObjectVersion");
    OPERATIONAL.add("msExchBlockedSendersHash");
    OPERATIONAL.add("msExchBypassAudit");
    OPERATIONAL.add("msExchCoManagedObjectsBL");
    OPERATIONAL.add("msExchDelegateListBL");
    OPERATIONAL.add("msExchDelegateListLink");
    OPERATIONAL.add("msExchDumpsterQuota");
    OPERATIONAL.add("msExchDumpsterWarningQuota");
    OPERATIONAL.add("msExchELCMailboxFlags");
    OPERATIONAL.add("msExchHideFromAddressLists");
    OPERATIONAL.add("msExchHomeServerName");
    OPERATIONAL.add("msExchMailboxAuditEnable");
    OPERATIONAL.add("msExchMailboxAuditLogAgeLimit");
    OPERATIONAL.add("msExchMailboxGuid");
    OPERATIONAL.add("msExchMailboxMoveFlags");
    OPERATIONAL.add("msExchMailboxMoveStatus");
    OPERATIONAL.add("msExchMailboxMoveSourceMDBLink");
    OPERATIONAL.add("msExchMailboxMoveTargetMDBLink");
    OPERATIONAL.add("msExchMailboxMoveSourceUserBL");
    OPERATIONAL.add("msExchMailboxMoveTargetUserBL");
    OPERATIONAL.add("msExchMailboxSecurityDescriptor");
    OPERATIONAL.add("msExchMasterAccountSid");
    OPERATIONAL.add("msExchMDBRulesQuota");
    OPERATIONAL.add("msExchModerationFlags");
    OPERATIONAL.add("msExchMobileAllowedDeviceIDs");
    OPERATIONAL.add("msExchMobileBlockedDeviceIDs");
    OPERATIONAL.add("msExchMobileMailboxFlags");
    OPERATIONAL.add("msExchMobileMailboxPolicyLink");
    OPERATIONAL.add("msExchObjectsDeletedThisPeriod");
    OPERATIONAL.add("msExchOmaAdminWirelessEnable");
    OPERATIONAL.add("msExchPoliciesExcluded");
    OPERATIONAL.add("msExchPoliciesIncluded");
    OPERATIONAL.add("msExchPreviousAccountSid");
    OPERATIONAL.add("msExchProvisioningFlags");
    OPERATIONAL.add("msExchRBACPolicyLink");
    OPERATIONAL.add("msExchRecipientDisplayType");
    OPERATIONAL.add("msExchRecipientTypeDetails");
    OPERATIONAL.add("msExchRecipientTypeDetails");
    OPERATIONAL.add("msExchRequireAuthToSendTo");
    OPERATIONAL.add("msExchResourceCapacity");
    OPERATIONAL.add("msExchResourceDisplay");
    OPERATIONAL.add("msExchResourceMetaData");
    OPERATIONAL.add("msExchResourceSearchProperties");
    OPERATIONAL.add("msExchSafeRecipientsHash");
    OPERATIONAL.add("msExchSafeSendersHash");
    OPERATIONAL.add("msExchSenderHintTranslations");
    OPERATIONAL.add("msExchShadowCountryCode");
    OPERATIONAL.add("msExchShadowDisplayName");
    OPERATIONAL.add("msExchShadowGivenName");
    OPERATIONAL.add("msExchShadowProxyAddresses");
    OPERATIONAL.add("msExchShadowMailNickname");
    OPERATIONAL.add("msExchTextMessagingState");
    OPERATIONAL.add("msExchTransportRecipientSettingsFlags");
    OPERATIONAL.add("msExchUMDtmfMap");
    OPERATIONAL.add("msExchUMEnabledFlags2");
    OPERATIONAL.add("msExchUnmergedAttsPt");
    OPERATIONAL.add("msExchUserAccountControl");
    OPERATIONAL.add("msExchUserCulture");
    OPERATIONAL.add("msExchWhenMailboxCreated");
    OPERATIONAL.add("msExchVersion");
    OPERATIONAL.add("mSMQDependentClientServices");
    OPERATIONAL.add("mSMQDsServices");
    OPERATIONAL.add("mSMQEncryptKey");
    OPERATIONAL.add("mSMQSites");
    OPERATIONAL.add("mSMQOSType");
    OPERATIONAL.add("mSMQRoutingServices");
    OPERATIONAL.add("mSMQServiceType");
    OPERATIONAL.add("mSMQSignKey");
    OPERATIONAL.add("msNPAllowDialin");
    OPERATIONAL.add("msRTCSIP");
    OPERATIONAL.add("msRTCSIP-ArchivingEnabled");
    OPERATIONAL.add("msRTCSIP-FederationEnabled");
    OPERATIONAL.add("msRTCSIP-DeploymentLocator");
    OPERATIONAL.add("msRTCSIP-InternetAccessEnabled");
    OPERATIONAL.add("msRTCSIP-Line");
    OPERATIONAL.add("msRTCSIP-OptionFlags");
    OPERATIONAL.add("msRTCSIP-PrimaryHomeServer");
    OPERATIONAL.add("msRTCSIP-PrimaryUserAddress");
    OPERATIONAL.add("msRTCSIP-UserEnabled");
    OPERATIONAL.add("msRTCSIP-UserPolicies");
    OPERATIONAL.add("msTSExpireDate");
    OPERATIONAL.add("msTSLicenseVersion");
    OPERATIONAL.add("protocolSettings");
    OPERATIONAL.add("objectGUID");
    OPERATIONAL.add("objectSid");
    OPERATIONAL.add("primaryGroupID");
    OPERATIONAL.add("pwdLastSet");
    OPERATIONAL.add("publicDelegates");
    OPERATIONAL.add("publicDelegatesBL");
    OPERATIONAL.add("replicatedObjectVersion");
    OPERATIONAL.add("replicationSignature");
    OPERATIONAL.add("sAMAccountType");
    OPERATIONAL.add("securityProtocol");
    OPERATIONAL.add("showInAddressBook");
    OPERATIONAL.add("showInAddressBook");
    OPERATIONAL.add("submissionContLength");
    OPERATIONAL.add("textEncodedORAddress");
    OPERATIONAL.add("telephoneAssistant");
    OPERATIONAL.add("userAccountControl");
    OPERATIONAL.add("userParameters");
    OPERATIONAL.add("uSNCreated");
    OPERATIONAL.add("uSNChanged");
    OPERATIONAL.add("whenCreated");
    OPERATIONAL.add("whenChanged");
    // operational attributes of a Oracle Internet Directory in alphabetical
    // order
    OPERATIONAL.add("orclGUID");
    OPERATIONAL.add("authpassword");
    OPERATIONAL.add("orclsupassword");
    OPERATIONAL.add("orclpassword");
    // operational attributes of a Oracle Unified Directory in alphabetical
    // order
    OPERATIONAL.add("entryCSN");
    OPERATIONAL.add("entryUUID");
    OPERATIONAL.add("nsUniqueID");
    // operational attributes of a Novell eDirectory in alphabetical order
    OPERATIONAL.add("entryFlags");
    OPERATIONAL.add("localEntryID");
    OPERATIONAL.add("structuralObjectClass");
    OPERATIONAL.add("subordinateCount");
    OPERATIONAL.add("subschemaSubentry");
    // operational attributes of a Tivoli Directory in alphabetical order
    OPERATIONAL.add("aclEntry");
    OPERATIONAL.add("aclPropagate");
    OPERATIONAL.add("aclSource");
    OPERATIONAL.add("aliasedObjectName");
    OPERATIONAL.add("aliasedentryName");
    OPERATIONAL.add("entryOwner");
    OPERATIONAL.add("hasSubordinates");
    OPERATIONAL.add("ibm-allGroups");
    OPERATIONAL.add("ibm-allMembers");
    OPERATIONAL.add("ibm-capabilitiessubentry");
    OPERATIONAL.add("ibm-effectiveAcl");
    OPERATIONAL.add("ibm-entryChecksum");
    OPERATIONAL.add("ibm-entryChecksumOp");
    OPERATIONAL.add("ibm-entryUuid");
    OPERATIONAL.add("ibm-filterAclEntry");
    OPERATIONAL.add("ibm-filterAclInherit");
    OPERATIONAL.add("ibm-pwdAccountLocked");
    OPERATIONAL.add("ibm-replicationChangeLDIF");
    OPERATIONAL.add("ibm-replicationFailedChangeCount");
    OPERATIONAL.add("ibm-replicationFailedChanges");
    OPERATIONAL.add("ibm-replicationIsQuiesced");
    OPERATIONAL.add("ibm-replicationLastActivationTime");
    OPERATIONAL.add("ibm-replicationLastChangeId");
    OPERATIONAL.add("ibm-replicationLastFinishTime");
    OPERATIONAL.add("ibm-replicationLastGlobalChangeId");
    OPERATIONAL.add("ibm-replicationLastResult");
    OPERATIONAL.add("ibm-replicationLastResultAdditional");
    OPERATIONAL.add("ibm-replicationNextTime");
    OPERATIONAL.add("ibm-replicationPendingChangeCount");
    OPERATIONAL.add("ibm-replicationPendingChanges");
    OPERATIONAL.add("ibm-replicationperformance");
    OPERATIONAL.add("ibm-replicationState");
    OPERATIONAL.add("ibm-replicationThisServerIsMaster");
    OPERATIONAL.add("ibm-searchSizeLimit");
    OPERATIONAL.add("ibm-searchTimeLimit");
    OPERATIONAL.add("ibm-slapdCryptoSalt");
    OPERATIONAL.add("numSubordinates");
    // operational attributes of a Generic Directory in alphabetical
    // order
    OPERATIONAL.add("createTimestamp");
    OPERATIONAL.add("creatorsName");
    OPERATIONAL.add("modifiersName");
    OPERATIONAL.add("modifyTimestamp");
    OPERATIONAL.add("pwdAccountLockedTime");
    OPERATIONAL.add("pwdChangedTime");
    OPERATIONAL.add("pwdExpirationWarned");
    OPERATIONAL.add("pwdFailureTime");
    OPERATIONAL.add("pwdGraceUseTime");
    OPERATIONAL.add("pwdHistory");
    OPERATIONAL.add("pwdReset");
    OPERATIONAL.add("subschemaSubentry");
    OPERATIONAL.add("subtreeSpecification");
    OPERATIONAL.add("userPassword");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Filter
  // ~~~~~ ~~~~~~
  public class Filter implements FileFilter {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final String extension;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Filter</code> that accepts files with the specified
     ** extendsion.
     **
     ** @param  extension        the file extesion this {@link FileFilter}
     **                          accepts as a valid file.
     */
    public Filter(final String extension) {
      // ensure inheritance
      super();

      this.extension = extension;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: accept (FileFilter)
    /**
     ** Tests whether or not the specified abstract pathname should be included
     ** in a pathname list.
     **
     ** @param  file             the abstract pathname to be tested
     **
     ** @return                  <code>true</code> if and only if
     **                          <code>file</code> should be included.
     */
    @Override
    public boolean accept(final File file) {
      // may be we are iterating a deep tree drop all directories
      if (file.isDirectory())
        return true;

      return file.getName().endsWith(extension);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>LDIFNormalize</code> that will scan only for files
   ** containes in the specified folder.
   **
   ** @param  folder             the {@link File} of the folder where the files
   **                            to handle are contained within.
   */
  public LDIFNormalize(final File folder) {
    // ensure inheritance
    super();

    this.folder    = folder;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  /**
   ** Called by the project to let the task do its work.
   ** <p>
   ** This method may be called more than once, if the task is invoked more than
   ** once. For example, if target1 and target2 both depend on target3, then
   ** running "ant target1 target2" will run all tasks in target3 twice.
   **
   ** @param  recursive          indicates that folders has to be included in
   **                            the scna of files
   */
  private void execute(final boolean recursive) {
    enlistFiles(this.folder, recursive);
    for (File file : this.file) {
      normalize(file);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   normalize
  /**
   ** Normalize the specified file by removing all operational attributes.
   **
   ** @param  input              the {@link File} to normalize.
   */
  private void normalize(final File input) {
    info("Normalize file " + input);
    final boolean changeContext = !(StringUtility.isEmpty(sourceContext) && StringUtility.isEmpty(targetContext));
    final boolean changeDomain  = !(StringUtility.isEmpty(sourceDomain)  && StringUtility.isEmpty(targetDomain));

    File       output = null;
    LDIFReader reader = null;
    LDIFWriter writer = null;
    try {
      // create a temporary file in the same folder the file to handle exists
      output = FileSystem.createTempFile("~" + input.getName(), EXTENSION, input.getParentFile());
      reader = new LDIFReader(input);
      writer = new LDIFWriter(output);
      writer.folding(false);

      do {
        final Binding binding = reader.nextRecord();
        if (binding == null)
          break;

        final LDAPRecord source    = (LDAPRecord)binding.getObject();
        final Attributes attribute = source.attributes();
        // break out if the source record does not provide any attribute
        if (attribute.size() == 0)
          continue;

        // transform the dn if required
        String name = binding.getName();
        if (changeContext) {
          int pos = name.lastIndexOf(sourceContext);
          if (pos != -1)
            name =  name.substring(0, pos) + targetContext;
        }

        final LDAPRecord record = new LDAPRecord(name);
        // iterate over all content attributes and remove the registered
        // operational attributes
        Enumeration<? extends Attribute> e = attribute.getAll();
        while (e.hasMoreElements()) {
          final Attribute a = e.nextElement();
          if (!OPERATIONAL.contains(a.getID())) {
            if (changeDomain || changeContext) {
              for (int i = 0; i < a.size(); i++) {
                Object value = a.get(i);
                if (value instanceof String) {
                  final String what = (String)value;
                  try {
                    // check if we have to achnge the context relation
                    int pos = what.lastIndexOf(sourceContext);
                    if (pos != -1)
                      a.set(i, what.substring(0, pos) + targetContext);
                  }
                  catch (IllegalStateException x) {
                    error(name + "\n" + a.getID());
                    error(x.getLocalizedMessage());
                  }
                  try {
                    // check if we have to achnge the domain relation
                    int pos = what.lastIndexOf(sourceDomain);
                    if (pos != -1)
                      a.set(i, what.substring(0, pos) + targetDomain);
                  }
                  catch (IllegalStateException x) {
                    error(name + "\n" + a.getID());
                    error(x.getLocalizedMessage());
                  }
                }
              }
            }
            record.add(a);
          }
        }
        // if the attribute collection is empty after all unwanted attributes
        // are removed avoid writing only a DN with nothing else as the DN
        if (record.attributes().size() > 0)
          writer.printEntry(record);
      } while(true);

      try {
        reader.close();
      }
      catch (Exception t) {
        error(t.getLocalizedMessage());
      }
      writer.close();
      // copy the temporary working file to the destination
      FileSystem.copy(output, input);
    }
    catch (Exception e) {
      writer.close();
      try {
        reader.close();
      }
      catch (Exception ex) {
        error(ex.getLocalizedMessage());
      }
      error(e.getLocalizedMessage());
    }
    finally {
      if (output != null && output.exists())
        output.delete();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enlistFiles
  /**
   ** Enlist all affected files.
   */
  private void enlistFiles(final File folder, final boolean recursive) {
    // obtain all files including directories from the specified folder
    File[] file = folder.listFiles(filter);
    // iterate over all entries to register it or not
    for (int i = 0; i < file.length; i++) {
      if (file[i].isDirectory())
        if (recursive)
          enlistFiles(file[i], recursive);
        else
          warning("Folder " + file[i].getAbsolutePath() + " will be ignored.");
      else
        this.file.add(file[i]);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   usage
  /**
   ** Prints the usage of this class to stdout.
   */
  private static void usage() {
    System.out.println("Usage: java -cp <path to>/oim-deploy.jar:<path to>/oim-utility.jar:<path to>/oim-foundation.jar:<path to>/hst-foundation.jar oracle.iam.identity.common.task.LDIFNormalize [-f | --folder] D:\\Project [-sc | --sourceContext] \"dc=viola,dc=local\" [-tc | --targetContext] \"dc=vm,dc=oracle,dc=com\" [-sd | --sourceDomain] \"viola.local\" [-td | --targetDomain] \"vm.oracle.com\"");
    System.out.println("Where is:");
    System.out.println("  <path to> - the directory of the Java Archive needed to accomplish the runtime classpath");
    System.out.println("  folder        - the absolute or relative path to the directory containing the LDIF files to normalize");
    System.out.println("                  If this path contains whitepspaces you need to quote the path accordingly");
    System.out.println("  sourceContext - the context of distinguished names to substitute");
    System.out.println("                  If this context contains whitepspaces you need to quote the path accordingly");
    System.out.println("  targetContext - the context of distinguished names to replace with");
    System.out.println("                  If this context contains whitepspaces you need to quote the path accordingly");
    System.out.println("  sourceDomain  - the domain name to substitute");
    System.out.println("                  If this name contains whitepspaces you need to quote the path accordingly");
    System.out.println("  targetContext - the domain name replace with");
    System.out.println("                  If this name contains whitepspaces you need to quote the path accordingly");
  }

  public static void main(String[] args) {
    // the arguments can never be null. They just wont exist hence we need not
    // to test against null
    // check if we are missing some commend line arguments
    int i = 0;
    while (i < args.length) {
      final String optionName = args[i++];
      if (optionName.equals(FOLDER_LONG) || optionName.equals(FOLDER_SHORT))
        folderName = args[i++];
      if (optionName.equals(SOURCE_CONTEXT_LONG) || optionName.equals(SOURCE_CONTEXT_SHORT))
        sourceContext = args[i++];
      if (optionName.equals(TARGET_CONTEXT_LONG) || optionName.equals(TARGET_CONTEXT_SHORT))
        targetContext = args[i++];
      if (optionName.equals(SOURCE_DOMAIN_LONG) || optionName.equals(SOURCE_DOMAIN_SHORT))
        sourceDomain = args[i++];
      if (optionName.equals(TARGET_DOMAIN_LONG) || optionName.equals(TARGET_DOMAIN_SHORT))
        targetDomain = args[i++];
    }

    if (folderName == null) {
      usage();
      System.exit(-1);
    }

    final File folder = new File(folderName);
    if (!folder.exists()) {
      error("Cannot find " + folder.getAbsolutePath());
      System.exit(-2);
    }

    LDIFNormalize service = new LDIFNormalize(folder);
    service.execute(true);
  }

  private static void error(final String message) {
    System.err.println(message);
  }

  private static void warning(final String message) {
    System.out.println(message);
  }

  private static void info(final String message) {
    System.out.println(message);
  }
}