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

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Library
    Subsystem   :   Deployment Utilities 12c

    File        :   TestBase.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    TestBase.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-08-31  DSteding    First release version
*/

package oracle.iam.identity.common.adf.marshal;

import java.io.File;

import java.util.Map;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import oracle.iam.identity.TestBase;
import oracle.iam.identity.common.FeatureError;
import oracle.iam.identity.common.FeatureResourceBundle;

import org.apache.tools.ant.BuildException;

////////////////////////////////////////////////////////////////////////////////
// class TestCatalog
// ~~~~~ ~~~~~~~~~~~
/**
 ** Test coverage for validate and enforce the Catalog Application Module.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestCatalog extends TestBase {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TestCatalog</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestCatalog() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   main
  /**
   ** Simple command line interface to execute the test case.
   **
   ** @param  args               the command line arguments.
   **                            <br>
   **                            Allowed object is array of {@link String}.
   */
  @SuppressWarnings("unused")
  public static void main(final String[] args) {
    final String[] parameter = { TestCatalog.class.getName() };
    JUnitCore.main(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testOffline
  /**
   ** Tests the loading the Catalog Application Module from an offline file.
   **
   ** @throws Exception          if an error occurs.
   */
  @Test
  public void testLoad()
    throws Exception {
  
    final File    path    = new File(RESOURCES, "CatalogAM.xml.xml");
    assertTrue(path.getAbsolutePath(), path.exists());

    final Catalog catalog = new Catalog();
    CatalogFactory.configure(catalog, path);
    
    final Map<String, Catalog.View> usage = catalog.usage();
    assertNotNull("usage", usage);
    assertTrue("IDSRequestVO",                                                    usage.containsKey("IDSRequestVO"));
    assertTrue("IDSRequestUD_IDS_GRPVO",                                          usage.containsKey("IDSRequestUD_IDS_GRPVO"));
    assertTrue("IDSRequestVOToIDSRequestUD_IDS_GRPVOLink",                        usage.containsKey("IDSRequestVOToIDSRequestUD_IDS_GRPVOLink"));
    assertTrue("IDSRequestUD_IDS_ROLVO",                                          usage.containsKey("IDSRequestUD_IDS_ROLVO"));
    assertTrue("IDSRequestVOToIDSRequestUD_IDS_ROLVOLink",                        usage.containsKey("IDSRequestVOToIDSRequestUD_IDS_ROLVOLink"));
    assertTrue("CTSRequestUD_CTS_UGPVO",                                          usage.containsKey("CTSRequestUD_CTS_UGPVO"));
    assertTrue("CTSRequestVOToCTSRequestUD_CTS_UGPVOLink",                        usage.containsKey("CTSRequestVOToCTSRequestUD_CTS_UGPVOLink"));
    assertTrue("eFBSRequestProductionVO",                                         usage.containsKey("eFBSRequestProductionVO"));
    assertTrue("eFBSRequestProductionUD_EFBSP_MVO",                               usage.containsKey("eFBSRequestProductionUD_EFBSP_MVO"));
    assertTrue("eFBSRequestProductionVOToeFBSRequestProductionUD_EFBSP_MVOLink",  usage.containsKey("eFBSRequestProductionVOToeFBSRequestProductionUD_EFBSP_MVOLink"));
    assertTrue("eFBSRequestProductionUD_EFBSP_PVO",                               usage.containsKey("eFBSRequestProductionUD_EFBSP_PVO"));
    assertTrue("eFBSRequestProductionVOToeFBSRequestProductionUD_EFBSP_PVOLink",  usage.containsKey("eFBSRequestProductionVOToeFBSRequestProductionUD_EFBSP_PVOLink"));
    assertTrue("eFBSRequestEducationVO",                                          usage.containsKey("eFBSRequestEducationVO"));
    assertTrue("eFBSRequestEducationUD_EFBSE_MVO",                                usage.containsKey("eFBSRequestEducationUD_EFBSE_MVO"));
    assertTrue("eFBSRequestEducationVOToeFBSRequestEducationUD_EFBSE_MVOLink",    usage.containsKey("eFBSRequestEducationVOToeFBSRequestEducationUD_EFBSE_MVOLink"));
    assertTrue("eFBSRequestEducationUD_EFBSE_PVO",                                usage.containsKey("eFBSRequestEducationUD_EFBSE_PVO"));
    assertTrue("eFBSRequestEducationVOToeFBSRequestEducationUD_EFBSE_PVOLink",    usage.containsKey("eFBSRequestEducationVOToeFBSRequestEducationUD_EFBSE_PVOLink"));
    assertTrue("PSCRequestVO",                                                    usage.containsKey("PSCRequestVO"));
    assertTrue("PSCRequestUD_PSC_UGPVO",                                          usage.containsKey("PSCRequestUD_PSC_UGPVO"));
    assertTrue("PSCRequestVOToPSCRequestUD_PSC_UGPVOLink",                        usage.containsKey("PSCRequestVOToPSCRequestUD_PSC_UGPVOLink"));
    assertTrue("PCFRequestProductionVO",                                          usage.containsKey("PCFRequestProductionVO"));
    assertTrue("PCFRequestProductionUD_PCFP_SRLVO",                               usage.containsKey("PCFRequestProductionUD_PCFP_SRLVO"));
    assertTrue("PCFRequestProductionVOToPCFRequestProductionUD_PCFP_SRLVOLink",   usage.containsKey("PCFRequestProductionVOToPCFRequestProductionUD_PCFP_SRLVOLink"));
    assertTrue("PCFRequestProductionUD_PCFP_ORLVO",                               usage.containsKey("PCFRequestProductionUD_PCFP_ORLVO"));
    assertTrue("PCFRequestProductionVOToPCFRequestProductionUD_PCFP_ORLVOLink",   usage.containsKey("PCFRequestProductionVOToPCFRequestProductionUD_PCFP_ORLVOLink"));
    assertTrue("PCFRequestProductionUD_PCFP_UGPVO",                               usage.containsKey("PCFRequestProductionUD_PCFP_UGPVO"));
    assertTrue("PCFRequestProductionVOToPCFRequestProductionUD_PCFP_UGPVOLink",   usage.containsKey("PCFRequestProductionVOToPCFRequestProductionUD_PCFP_UGPVOLink"));
    assertTrue("PCFRequestDevelopmentVO",                                         usage.containsKey("PCFRequestDevelopmentVO"));
    assertTrue("PCFRequestDevelopmentUD_PCFD_SRLVO",                              usage.containsKey("PCFRequestDevelopmentUD_PCFD_SRLVO"));
    assertTrue("PCFRequestDevelopmentVOToPCFRequestDevelopmentUD_PCFD_SRLVOLink", usage.containsKey("PCFRequestDevelopmentVOToPCFRequestDevelopmentUD_PCFD_SRLVOLink"));
    assertTrue("PCFRequestDevelopmentUD_PCFD_ORLVO",                              usage.containsKey("PCFRequestDevelopmentUD_PCFD_ORLVO"));
    assertTrue("PCFRequestDevelopmentVOToPCFRequestDevelopmentUD_PCFD_ORLVOLink", usage.containsKey("PCFRequestDevelopmentVOToPCFRequestDevelopmentUD_PCFD_ORLVOLink"));
    assertTrue("PCFRequestDevelopmentUD_PCFD_UGPVO",                              usage.containsKey("PCFRequestDevelopmentUD_PCFD_UGPVO"));
    assertTrue("PCFRequestDevelopmentVOToPCFRequestDevelopmentUD_PCFD_UGPVOLink", usage.containsKey("PCFRequestDevelopmentVOToPCFRequestDevelopmentUD_PCFD_UGPVOLink"));
    assertTrue("PIAVRequestVO",                                                   usage.containsKey("PIAVRequestVO"));
    assertTrue("PIAVRequestUD_PIAV_GRPVO",                                        usage.containsKey("PIAVRequestUD_PIAV_GRPVO"));
    assertTrue("PIAVRequestVOToPIAVRequestUD_PIAV_GRPVOLink",                     usage.containsKey("PIAVRequestVOToPIAVRequestUD_PIAV_GRPVOLink"));
    assertTrue("PIAVRequestUD_PIAV_ROLVO",                                        usage.containsKey("PIAVRequestUD_PIAV_ROLVO"));
    assertTrue("PIAVRequestVOToPIAVRequestUD_PIAV_ROLVOLink",                     usage.containsKey("PIAVRequestVOToPIAVRequestUD_PIAV_ROLVOLink"));
    assertTrue("PAPRequestVO",                                                    usage.containsKey("PAPRequestVO"));
    assertTrue("PAPRequestUD_PAP_UGPVO",                                          usage.containsKey("PAPRequestUD_PAP_UGPVO"));
    assertTrue("PAPRequestVOToPAPRequestUD_PAP_UGPVOLink",                        usage.containsKey("PAPRequestVOToPAPRequestUD_PAP_UGPVOLink"));
    assertTrue("SZ4RequestVO",                                                    usage.containsKey("SZ4RequestVO"));
    assertTrue("SZ4RequestUD_SZ4_UGPVO",                                          usage.containsKey("SZ4RequestUD_SZ4_UGPVO"));
    assertTrue("SZ4RequestVOToSZ4RequestUD_SZ4_UGPVOLink",                        usage.containsKey("SZ4RequestVOToSZ4RequestUD_SZ4_UGPVOLink"));
    assertTrue("IDSRequestVO",                             usage.containsKey("IDSRequestVO"));
    assertTrue("IDSRequestVO",                             usage.containsKey("IDSRequestVO"));
    assertTrue("IDSRequestVO",                             usage.containsKey("IDSRequestVO"));
    assertTrue("IDSRequestVO",                             usage.containsKey("IDSRequestVO"));
    assertTrue("IDSRequestVO",                             usage.containsKey("IDSRequestVO"));
    assertTrue("IDSRequestVO",                             usage.containsKey("IDSRequestVO"));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testSave
  /**
   ** Tests the extending the Catalog Application Module with new view objects.
   **
   ** @throws Exception          if an error occurs.
   */
  @Test
  public void testSave()
    throws Exception {
  
    File    path    = new File(RESOURCES, "CatalogAM.xml.xml");
    assertTrue(path.getAbsolutePath(), path.exists());

    final Catalog catalog = new Catalog();
    CatalogFactory.configure(catalog, path);
    final Map<String, Catalog.View> usage = catalog.usage();
    assertNotNull("usage", usage);
    
    catalog.usage("IGSPRequest");
    catalog.usage("IGSPRequest", "UD_IGSP_URL");
    catalog.link("IGSPRequest",  "UD_IGSP_URL");
    catalog.usage("IGSPRequest", "UD_IGSP_UTN");
    catalog.link("IGSPRequest",  "UD_IGSP_UTN");

    assertNotNull("usage", usage);
    assertTrue("IGSPRequestVO",                               usage.containsKey("IGSPRequestVO"));
    assertTrue("IGSPRequestUD_IGSP_URLVO",                    usage.containsKey("IGSPRequestUD_IGSP_URLVO"));
    assertTrue("IGSPRequestVOToIGSPRequestUD_IGSP_URLVOLink", usage.containsKey("IGSPRequestVOToIGSPRequestUD_IGSP_URLVOLink"));
    assertTrue("IGSPRequestUD_IGSP_UTNVO",                    usage.containsKey("IGSPRequestUD_IGSP_UTNVO"));
    assertTrue("IGSPRequestVOToIGSPRequestUD_IGSP_UTNVOLink", usage.containsKey("IGSPRequestVOToIGSPRequestUD_IGSP_UTNVOLink"));

    try {
       path = new File("/Users/dsteding/Project/Bundeskriminalamt12c/ExternalIdentityManagement/deployment/oim/1801 systemConnector IGS/xxx/igs-p-account-sysadmin/persDef/oracle/iam/ui/catalog/model/am/mdssys/cust/site/site");  
      // verify if the path where we want to operate exists
      Catalog.marshalModule(null, Metadata.path(path.getAbsolutePath()), catalog);
    }
    catch (Exception e) {
      failed(e);
    }
  }
}