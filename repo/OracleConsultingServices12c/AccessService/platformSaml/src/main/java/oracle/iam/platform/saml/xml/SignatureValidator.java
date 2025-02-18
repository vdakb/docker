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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   SignatureValidator.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    SignatureValidator.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.saml.xml;

import java.util.List;
import java.util.Objects;

import java.security.PublicKey;

import javax.xml.crypto.MarshalException;

import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.XMLSignatureException;

import javax.xml.crypto.dsig.dom.DOMValidateContext;

import oracle.hst.platform.core.logging.Logger;

import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import oracle.iam.platform.saml.AssertionError;
import oracle.iam.platform.saml.AssertionBundle;
import oracle.iam.platform.saml.AssertionMessage;
import oracle.iam.platform.saml.AssertionException;

////////////////////////////////////////////////////////////////////////////
// final class SignatureValidator
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** XML Digital Signature Validator class of Access Management Framework.
 ** <p>
 ** A class that implements the settings parser from IdP Metadata.
 ** <p>
 ** This class does not validate in any way the URL that is introduced, make
 ** sure to validate it properly before use it in a metadata method.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class SignatureValidator {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Local Name of the Signature element. */
  public static final String               LOCAL     = "AuthnContext";
  
  /** the XML processing mechanism and representation type */
  public static final String               MECHAMISM = "DOM";

  // create signature validator object.
  private static final XMLSignatureFactory FACTORY   = XMLSignatureFactory.getInstance(MECHAMISM);

  private final static Logger              LOGGER    = Logger.create(SignatureValidator.class.getName());

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>SignatureValidator</code>.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new SignatureValidator()" and enforces use of the public method below.
   */
  private SignatureValidator() {
    // should never be instantiated
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate
  /**
   ** Validates a signature according to the
   ** <a href="http://www.w3.org/TR/xmldsig-core/#sec-CoreValidation">Core validation processing rules</a>
   ** leveraging the specified public key and node. 
   ** <br>
   ** This method validates the signature using the existing state, by
   ** unmarshalling the <code>XMLSignature</code> using the location information
   ** specified in the given node.
   ** <p>
   ** <b>Important</b>:
   ** <br>
   ** The method expects, that the given <code>document</code> is the enclosing
   ** element of the XML Digital Signatur element. The implementation lookup the
   ** signature element itself by evaluation the xpath query containing the
   ** experssion <code>dsig:Signature</code>. Therefore the namespace prefix
   ** <code>dsig</code> <b>must</b> be mapped to the namespace
   ** <code>http://www.w3.org/2000/09/xmldsig#</code> at the namepsace context
   ** of the xpath factory.
   ** <p>
   ** Step:
   ** <ol>
   **   <li>Verify the digital signature of the <code>SignedInfo</code> element.
   ** </ol>
   ** <br>
   ** A <code>NullPointerException</code> is thrown if either
   ** {@link PublicKey publicKey} or {@link Document document} is
   ** <code>null</code>.
   **
   ** 
   ** @param  publicKey          the validating key for the desired signature.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Element}.
   ** @param  document           the starting context (a node, for example) of
   **                            XML Digital Signatur element.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Document}.
   **
   ** @return                    <code>true</code> if the signature passed core
   **                            validation; otherwise <code>false</code>.
   **
   ** @throws AssertionException   if an unexpected error occurs during
   **                              validation that prevented the validation
   **                              operation from completing.
   ** @throws NullPointerException if either <code>publicKey</code> or
   **                              <code>document</code> is <code>null</code>.
   */
  public static boolean validate(final PublicKey publicKey, final Document document)
    throws AssertionException {

    // all information related to the XML digital signature is stored in the
    // <Signature> element, so we need to get the <Signature> element first.
    // Since the <Signature> element is stored as a direct child element of the
    // document root element when generating the signature, so here is the
    // search based on the element name "Signature", the first element found is
    // the <Signature> element
    final NodeList list = Objects.requireNonNull(document, AssertionBundle.string(AssertionError.ARGUMENT_IS_NULL, "document"))
      .getElementsByTagNameNS(XMLSignature.XMLNS, "Signature");
    if (list.getLength() == 0)
      throw AssertionException.abort(AssertionBundle.string(AssertionError.SIGNATURE_MISSING));

    Node signatureNode = list.item(0);
    try {
      // in the process of verifying the signature, we need to create a
      // DOMValidateContext object to specify the context information, and the
      // parameters are the verification public key and the <Signature> element
      // obtained earlier
      final DOMValidateContext context = new DOMValidateContext(
        Objects.requireNonNull(publicKey, AssertionBundle.string(AssertionError.ARGUMENT_IS_NULL, "publicKey"))
      , signatureNode
      );
      // use the unmarshalXMLSignature method of the XMLSignatureFactory class
      // to construct an XMLSignature object from the DOM node to prepare for
      // the next step of verifying the signature.
      final XMLSignature signature = FACTORY.unmarshalXMLSignature(context);
      // all the information required to verify the signature is ready, start to
      // verify using the interface provided by the XMLSignature object
      boolean core = signature.validate(context);
      if (!core) {
        LOGGER.error(AssertionBundle.string(AssertionError.SIGNATURE_CORE_FAILED));
        // Verification of <SignedInfo> signature value
        // 1) Obtain the public key of the data sender used to verify the
        //    digital signature from the <KeyValue> element in the <KeyInfo>
        //    element or according to the information specified in the
        //    <KeyInfo> element.
        // 2) Use the verification key to decrypt the encrypted signature value
        //    in the <SignatureValue> element to obtain the value D
        // 3) Use the signature algorithm specified by the <SignatureMethod>
        //    element to calculate the digest value of the regularized
        //    <SignedInfo> element to obtain the value D'
        // 4) Judge whether D and D' match, if they do not match, the
        //    verification fails
        boolean sv = signature.getSignatureValue().validate(context);
        LOGGER.info(AssertionBundle.string(AssertionMessage.SIGNATURE_CORE_STATUS, AssertionBundle.string(sv ? AssertionMessage.SIGNATURE_STATUS_VALIDATED : AssertionMessage.SIGNATURE_STATUS_NOTVALIDATED)));
        // each <Reference> in <SignedInfo> is verified
        // perform the following verification steps for each <Reference> in
        // <SignedInfo>:
        // 1) Apply the specified data conversion method to obtain the
        //    referenced data object
        // 2) Use the specified digest generation algorithm to generate digest
        //    values
        // 3) Compare the generated digest value with the digest value
        //    contained in the <DigestValue> element in <Reference>.
        //    If it does not match, the verification will fail
        final List<?> refs = signature.getSignedInfo().getReferences();
        for (int i = 0; i < refs.size(); i++) {
          final boolean valid = ((Reference)refs.get(i)).validate(context);
          LOGGER.info(AssertionBundle.string(AssertionMessage.SIGNATURE_REFRENCE_STATUS, i, AssertionBundle.string(valid ? AssertionMessage.SIGNATURE_STATUS_VALID : AssertionMessage.SIGNATURE_STATUS_INVALID)));
        }
      }
      else {
        LOGGER.info(AssertionBundle.string(AssertionMessage.SIGNATURE_CORE_PASSED));
      }
      return true;
    }
    catch (XMLSignatureException e) {
      throw AssertionException.abort(e);
    }
    catch (MarshalException e) {
      throw AssertionException.general(e);
    }
    catch (Exception e) {
      throw AssertionException.unhandled(e);
    }
  }
}