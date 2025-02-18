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
    Subsystem   :   Common Shared LDAP Facilities

    File        :   DSMLReader.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DSMLReader.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.utility.file;

import java.util.List;
import java.util.ArrayList;

import java.io.File;
import java.io.DataInputStream;
import java.io.FileInputStream;

import javax.naming.Binding;

import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;

import oracle.iam.identity.foundation.ldap.DirectoryException;
import oracle.iam.identity.foundation.ldap.DirectoryFileReader;

////////////////////////////////////////////////////////////////////////////////
// class DSMLReader
// ~~~~~ ~~~~~~~~~~
/**
 ** Reads, parses and converts DSML into LDAPMessages.
 ** <p>
 ** Reads DSML, Directory Service Markup Language, from files, streams and
 ** readers, and returns LDAPMessages.
 ** <br>
 ** <b>Note</b> that some XML applications will have DSML pre-parsed into DOM
 ** objects, in which case DOMReader should be used.
 **
 ** @see DSMLWriter
 ** @see DirectoryFileReader
 */
public class DSMLReader extends    DirectoryFileReader
                        implements ContentHandler
                        ,          ErrorHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /*
   * The following values are valid states for the parser: the tags they
   * represent are in comments to the right
   */
  private static final int START                       = 0;

  /* The following are possible states from filter, compare and search */
  private static final int ASSERTION                   = 10; //<assertion>
  private static final int VALUE                       = 11; //<value>
  private static final int ATTRIBUTES                  = 12; //<attributes>
  private static final int ATTRIBUTE                   = 13; //<attribute>
  private static final int FILTER                      = 14; //<filter>
  private static final int AND                         = 15; //<and>
  private static final int OR                          = 16; //<or>
  private static final int NOT                         = 17; //<not>
  private static final int EQUALITY_MATCH              = 18; //<equalityMatch>
  private static final int SUBSTRINGS                  = 19; //<substrings>
  private static final int GREATER_OR_EQUAL            = 20; //<greaterOrEqual>
  private static final int LESS_OR_EQUAL               = 21; //<lessOrEqual>
  private static final int PRESENT                     = 22; //<present>
  private static final int APPROXIMATE_MATCH           = 23; //<approxMatch>
  private static final int EXTENSIBLE_MATCH            = 24; //<extensibleMatch>
  private static final int INITIAL                     = 25; //<initial>
  private static final int ANY                         = 26; //<any>
  private static final int FINAL                       = 27; //<final>

  /* miscelaneous tags :*/
  private static final int ADD_ATTRIBUTE               = 28; //<attr>
  private static final int MODIFICATION                = 29; //<modification>
  private static final int X_NAME                      = 30; //<requestName>
  private static final int X_VALUE                     = 31; //<requestValue>

  private static final int CONTROL                     = 32; //<control>

  /* for Add Response */
  private static final int ADD_RESPONSE                = 35; //<addResponse>
  private static final int LDAP_RESPONSE               = 36; //Generic Response Type.
  private static final int RESULT_CODE                 = 37; //<resultCode>
  private static final int REFERRAL_LIST               = 39; //<referral>
  private static final int ERROR_MESSAGE               = 38; //<errorMessage>
  private static final int ERROR_RESPONSE              = 53; //<errorResponse>
  private static final int MESSAGE                     = 54; //<errorResponse>

  /* for Search Response */
  private static final int SEARCH_RESPONSE             = 40; //<searchResponse>
  private static final int SEARCH_RESULT_ENTRY         = 41; //<searchResultEntry>
  private static final int SEARCH_RESULT_REFERENCE     = 42; //<searchResultReference>
  private static final int SEARCH_RESULT_REFERENCE_REF = 43;
  private static final int SEARCH_RESULT_DONE          = 44; //<searchResultDone>

  /* Extended Response */
  private static final int EXTENDED_RESPONSE           = 45; //<extendedResponse>
  private static final int EXTENDED_RESPONSE_NAME      = 46; //<responseName>
  private static final int EXTENDED_RESPONSE_RESPONSE  = 47; //<response>

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** state contains the internal parsing state */
  private int           state           = START;

  /** used to store previous state for controls */
  private int           prevstate       = 0;

  /**
   ** used for Extended response, Since Extended Response add ldap response
   ** state have a common code, this code helps to separate the two.
   */
  private boolean       isextendedstate = false;

  /** holds the content in a value tag */
  private StringBuilder value;

  /** Multiple <value> contents will be stored in this list:*/
  private List<String>  attributeValues = new ArrayList<String>();

  /** attributeNames is used for compare and search attribute names: */
  private List<String>  attributeNames = new ArrayList<String>();

   /** modlist is used for modifications in the ModRequest operation */
  private List<String>  modlist        = new ArrayList<String>();

  /* Response Variable */
  private int           responsetype   = 0;
  // Used to store Response type for creation of response.
  private int           responsecode   = 0;
  private String        responseDesc   = null;
  private String        errorMessage   = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a reader that reads from a file containing XML with DSML tags from
   ** stdin.
   **
   ** @throws DirectoryException if the file specified by {@link #STANDARD}
   **                            could not be found.
   */
  public DSMLReader()
    throws DirectoryException {

    // ensure inheritance
    super(STANDARD);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DSMLReader</code> object to read the DSML from the
   ** specified {@link File} path.
   **
   ** @param  file               the {@link File} path of the DSML file to read.
   **
   ** @throws DirectoryException if the specified file could not be found.
   */
  public DSMLReader(final File file)
    throws DirectoryException {

    // ensure inheritance
    super(file);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DSMLReader</code> object to read the DSML from the
   ** specified file.
   **
   ** @param  file               the name of the DSML file to read.
   **
   ** @throws DirectoryException if the specified file could not be found.
   */
  public DSMLReader(final String file)
    throws DirectoryException {

    // ensure inheritance
    super(file);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DSMLReader</code> object to read entries from a
   ** stream as DSML.
   **
   ** @param  stream             input stream providing the DSML data.
   */
  public DSMLReader(final FileInputStream stream) {
    this(new DataInputStream(stream));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DSMLReader</code> object to read the DSML data from an
   ** input stream.
   **
   ** @param  stream             input stream providing the DSML data.
   */
  public DSMLReader(final DataInputStream stream) {
    super(stream);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDocumentLocator (ContentHandler)
  /**
   ** Receive a Locator object for document events.
   ** <p>
   ** By default, do nothing. Application writers may override this method in a
   ** subclass if they wish to store the locator for use with other document
   ** events.
   **
   ** @param  locator            a locator for all SAX document events.
   **
   ** @see    org.xml.sax.ContentHandler#setDocumentLocator
   ** @see    org.xml.sax.Locator
   */
  @Override
  public void setDocumentLocator(final Locator locator) {
    // no op
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   startDocument (ContentHandler)
  /**
   ** Receive notification of the beginning of the document.
   ** <p>
   ** By default, do nothing. Application writers may override this method in a
   ** subclass to take specific actions at the beginning of a document (such as
   ** allocating the root node of a tree or creating an output file).
   **
   ** @throws SAXException       any SAX exception, possibly wrapping another
   **                            exception.
   **
   ** @see    org.xml.sax.ContentHandler#startDocument
   */
  @Override
  public void startDocument()
    throws SAXException {
    // no op
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endDocument (ContentHandler)
  /**
   ** Receive notification of the end of the document.
   ** <p>
   ** By default, do nothing. Application writers may override this method in a
   ** subclass to take specific actions at the end of a document (such as
   ** finalising a tree or closing an output file).
   **
   ** @throws SAXException       any SAX exception, possibly wrapping another
   **                            exception.
   **
   ** @see    org.xml.sax.ContentHandler#endDocument
   */
  @Override
  public void endDocument()
    throws SAXException {
    // no op
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   startPrefixMapping (ContentHandler)
  /**
   ** Receive notification of the start of a Namespace mapping.
   ** <p>
   ** By default, do nothing. Application writers may override this method in a
   ** subclass to take specific actions at the start of each Namespace prefix
   ** scope (such as storing the prefix mapping).
   **
   ** @param  prefix             the Namespace prefix being declared.
   ** @param  uri                the Namespace URI mapped to the prefix.
   **
   ** @throws SAXException       any SAX exception, possibly wrapping another
   **                            exception.
   **
   ** @see    org.xml.sax.ContentHandler#startPrefixMapping
   */
  @Override
  public void startPrefixMapping(final String prefix, final String uri)
    throws SAXException {
    // no op
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endPrefixMapping (ContentHandler)
  /**
   ** Receive notification of the end of a Namespace mapping.
   ** <p>
   ** By default, do nothing. Application writers may override this method in a
   ** subclass to take specific actions at the end of each prefix mapping.
   **
   ** @param  prefix             the Namespace prefix being declared.
   **
   ** @throws SAXException       any SAX exception, possibly wrapping another
   **                            exception.
   **
   ** @see    org.xml.sax.ContentHandler#endPrefixMapping
   */
  @Override
  public void endPrefixMapping(final String prefix)
    throws SAXException {
    // no op
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   startElement (ContentHandler)
  /**
   ** Receive notification of the start of an element.
   ** <p>
   ** By default, do nothing. Application writers may override this method in a
   ** subclass to take specific actions at the start of each element (such as
   ** allocating a new tree node or writing output to a file).
   **
   ** @param  uri                the Namespace URI, or the empty string if the
   **                            element has no Namespace URI or if Namespace
   **                            processing is not being performed.
   ** @param  localName          the local name (without prefix), or the empty
   **                            string if Namespace processing is not being
   **                            performed.
   ** @param  qualifiedName      the qualified name (with prefix), or the empty
   **                            string if qualified names are not available.
   ** @param  attributes         the attributes attached to the element. If
   **                            there are no attributes, it shall be an empty
   **                            Attributes object.
   **
   ** @throws SAXException       any SAX exception, possibly wrapping another
   **                            exception.
   **
   ** @see    org.xml.sax.ContentHandler#startElement
   */
  @Override
  public void startElement(final String uri, final String localName, final String qualifiedName, final Attributes attributes)
    throws SAXException {
/*
    Integer elementTag = (Integer)requestTags.get(localName);
    if (elementTag == null) {
      if (state != START) {
        //Ignore tags outside of DSML tags
        throw new SAXNotRecognizedException("Element name, \"" + qualifiedName + "\" not recognized");
      }
      else {
        return;
      }
    }
    int tag = elementTag.intValue();
    if (tag == CONTROL) {
      handleControl(attributes);
      this.prevstate = this.state;
      this.state     = CONTROL;
    }
    else {
      switch (state) {
        // The following values are valid states for the parser:
        case START : // we can now read a Batch_Request tag or Batch_Response tag
                     if (tag == BATCH_REQUEST || tag == BATCH_RESPONSE) {
                       this.state = tag;
                       parseTagAttributes(tag, attributes);
                     }
                     else
                       throw new SAXException("Invalid beginning tag :" + qualifiedName);
                     break;
      case BATCH_REQUEST : this.state = tag;
                           if (tag == ADD_REQUEST) {
                             attrSet = new LDAPAttributeSet();
                           }
                           if (tag == MODIFY_REQUEST) {
                             this.modlist.clear();
                          }
                          parseTagAttributes(tag, attributes);
                           break;
      case BATCH_RESPONSE : if (tag == ADD_RESPONSE) {
                              // process AddResponse.
                              this.responsetype = LDAPMessage.ADD_RESPONSE;
                              this.state        = LDAP_RESPONSE;
                              // handling as a generic LdapResponse.
                              parseTagAttributes(LDAP_RESPONSE, attributes);
                            }
                            else if (tag == SEARCH_RESPONSE) {
                              this.responsetype = LDAPMessage.SEARCH_RESPONSE;
                              parseTagAttributes(tag, attributes);
                              this.state        = SEARCH_RESPONSE;
                              searchResponseid  = requestID;
                            }
                            else if (tag == EXTENDED_RESPONSE) {
                              this.responsetype = LDAPMessage.EXTENDED_RESPONSE;
                              parseTagAttributes(LDAP_RESPONSE, attributes);
                              this.state = EXTENDED_RESPONSE;
                            }
                            else if (tag == MODIFY_RESPONSE) {
                              // Process Modify Response.
                              this.responsetype = LDAPMessage.MODIFY_RESPONSE;
                              this.state        = LDAP_RESPONSE;
                              // Handling as a generic LdapResponse.
                              parseTagAttributes(LDAP_RESPONSE, attributes);
                            }
                            else if (tag == DEL_RESPONSE) {
                              // Process Delete Response.
                              this.responsetype = LDAPMessage.DEL_RESPONSE;
                              this.state        = LDAP_RESPONSE;
                              // Handling as a generic LdapResponse.
                              parseTagAttributes(LDAP_RESPONSE, attributes);
                            }
                            else if (tag == MODIFYDN_RESPONSE) {
                              // Process Modify DN Response.
                              this.responsetype = LDAPMessage.MODIFY_RDN_RESPONSE;
                              this.state        = LDAP_RESPONSE;
                              // Handling as a generic LdapResponse.
                              parseTagAttributes(LDAP_RESPONSE, attributes);
                            }
                            else if (tag == COMPARE_RESPONSE) {
                              // Process Compare Response.
                              this.responsetype = LDAPMessage.COMPARE_RESPONSE;
                              this.state        = LDAP_RESPONSE;
                              // Handling as a generic LdapResponse.
                              parseTagAttributes(LDAP_RESPONSE, attributes);
                            }
                            else if (tag == ERROR_RESPONSE) {
                              // Process Compare Response.
                              this.responsetype = LDAPMessage.ABANDON_REQUEST;
                              this.state        = ERROR_RESPONSE;
                              // Handling as a generic LdapResponse.
                              parseTagAttributes(ERROR_RESPONSE, attributes);
                            }
                            else
                              throw new SAXException("invalid tag: " + localName);
                            referrallist.clear();
                            break;
      case SEARCH_RESPONSE : if (tag == SEARCH_RESULT_DONE) {
                               // Generic Ldap Result for a Ldap Response
                               this.state        = LDAP_RESPONSE;
                               this.responsetype = LDAPMessage.SEARCH_RESULT;
                               // Handling as a generic LdapResponse.
                               parseTagAttributes(LDAP_RESPONSE, attributes);
                             }
                             else if (tag == SEARCH_RESULT_REFERENCE) {
                               // referals objects to track.
                               referrallist.clear();
                               // Use the REFERRAL LIST STATE
                               this.state = SEARCH_RESULT_REFERENCE;
                             }
                             else if (tag == SEARCH_RESULT_ENTRY) {
                               this.state = SEARCH_RESULT_ENTRY;
                               // Parse the request Batch ID
                               parseTagAttributes(SEARCH_RESULT_ENTRY, attributes);
                               attrSet = new LDAPAttributeSet();
                             }
                             break;
      case SEARCH_RESULT_ENTRY : // Attribute is same as the add request
                                 if (tag == ADD_ATTRIBUTE) {
                                   // Tag
                                   this.state = tag;
                                   this.attributeValues.clear();
                                   attrName = attributes.getValue("name");
                                   isAddRequest = false;
                                 }
                                 break;
      case SEARCH_RESULT_REFERENCE : if (tag == SEARCH_RESULT_REFERENCE_REF) {
                                       // nothing to do, just cleanup value.
                                       if (this.value == null) {
                                         this.value = new StringBuilder();
                                       }
                                       else {
                                         // cleanup value.
                                         this.value.delete(0, this.value.length());
                                       }
                                       this.state = SEARCH_RESULT_REFERENCE_REF;
                                     }
                                     break;
      case EXTENDED_RESPONSE         : if (this.value == null) {
                                         this.value = new StringBuilder();
                                       }
                                       else {
                                         // cleanup value.
                                         this.value.delete(0, this.value.length());
                                       }
                                       if (tag == EXTENDED_RESPONSE_NAME) {
                                         this.state = EXTENDED_RESPONSE_NAME;
                                       }
                                       if (tag == EXTENDED_RESPONSE_RESPONSE) {
                                         this.state = EXTENDED_RESPONSE_RESPONSE;
                                         String temp = attributes.getValue("xsi:type");
                                         if (temp != null && temp.equals("xsd:base64Binary")) {
                                           this.isBase64 = true;
                                         }
                                         else {
                                           this.isBase64 = false;
                                         }
                                       }
                                       // no break, extended response , extendeds generic response.
                                       this.isextendedstate = true;
      case LDAP_RESPONSE             : // Process Generic Ldap Response.
                                       if (tag == RESULT_CODE) {
                                         // Mandatory
                                         if (attributes.getValue("code") == null)
                                           throw new SAXException("Response Code not provided");

                                         responsecode = (new Integer(attributes.getValue("code"))).intValue();
                                         responseDesc = attributes.getValue("descr");
                                       }
                                       else if (tag == ERROR_MESSAGE || tag == MESSAGE) {
                                         if (this.value == null) {
                                           this.value = new StringBuilder();
                                         }
                                         else {
                                           // cleanup value.
                                           this.value.delete(0, this.value.length());
                                         }
                                         this.state = tag;
                                       }
                                       else if (tag == REFERRAL_LIST) {
                                         // nothing to do, just cleanup value.
                                         if (this.value == null) {
                                           this.value = new StringBuilder();
                                         }
                                         else {
                                           // cleanup value.
                                           this.value.delete(0, this.value.length());
                                         }
                                         this.state = tag;
                                       }
                                       break;
      case ERROR_RESPONSE            : if (this.value == null) {
                                         this.value = new StringBuilder();
                                       }
                                       else {
                                         // cleanup value.
                                         this.value.delete(0, this.value.length());
                                       }
                                       this.state = tag;
                                       break;
      case SEARCH_REQUEST            : if ((isParallel == true && isUnordered == true) && requestID == null)
                                         throw new SAXException("requestID not provided");

                                       if (tag == ATTRIBUTES) {
                                         this.attributeNames.clear();
                                         this.attributeValues.clear();
                                         this.state = tag;
                                       }
                                       else if (tag == FILTER) {
                                         this.state = FILTER;
                                         filter = new RfcFilter();
                                       }
                                       else
                                         throw new SAXException("invalid searchRequest tag: " + localName);
                                       break;
      case AUTH_REQUEST              :
      case MODIFY_REQUEST            : if ((isParallel == true && isUnordered == true) && requestID == null)
                                         throw new SAXException("requestID not provided");

                                       if (tag == MODIFICATION) {
                                         this.state = tag;
                                         this.attributeValues.clear();
                                         String tempID = requestID;
                                         parseTagAttributes(tag, attributes);
                                         requestID = tempID;
                                         tempID = null;
                                       }
                                      else
                                        throw new SAXException("invalid modifyRequest tag: " + localName);
                                      break;
      case ADD_REQUEST              : if ((isParallel == true && isUnordered == true) && requestID == null)
                                         throw new SAXException("requestID not provided");

                                      if (tag == ADD_ATTRIBUTE) {
                                         this.state = tag;
                                         this.attributeValues.clear();
                                         attrName = attributes.getValue("name");
                                         isAddRequest = true;
                                      }
                                      else
                                        // I may not have to check for this if decide to validate
                                        throw new SAXException("invalid addRequest tag: " + localName);
                                      break;
      case DELETE_REQUEST           : if ((isParallel == true && isUnordered == true) && requestID == null)
                                         throw new SAXException("requestID not provided");

                                      break;
      case MODIFY_DN_REQUEST        : if ((isParallel == true && isUnordered == true) && requestID == null)
                                         throw new SAXException("requestID not provided");

                                      break;
      case COMPARE_REQUEST          : if ((isParallel == true && isUnordered == true) && requestID == null)
                                         throw new SAXException("requestID not provided");

                                      this.attributeValues.clear();
                                      if (tag == ASSERTION) {
                                        attrName = attributes.getValue("name");
                                        this.state = tag;
                                      }
                                      else
                                        throw new SAXException("invalid compareRequest tag: " + localName);
                                      break;
                                     // Tags with multiple names but no value tags embedded
      case ATTRIBUTES              : //list of attribute names
                                     if (tag == ATTRIBUTE) {
                                       // add a search attributes name
                                       this.attributeNames.add(attributes.getValue("name"));
                                       this.state = tag;
                                     }
                                     else
                                       throw new SAXException("invalid attributes tag: " + localName);
                                     break;
      // Substring tag can contain initial, any, or final tags
      case SUBSTRINGS              : if ((tag == INITIAL) || (tag == ANY) || (tag == FINAL)) {
                                       this.state = tag;
                                       this.value = new StringBuilder();
                                     }
                                     else
                                       throw new SAXException("invalid substrings tag: " + localName);
                                     break;
      case FILTER                  :
      case AND                     :
      case OR                      :
      case NOT                     : handleFilter(tag, attributes, localName);
                                     this.state = tag;
                                     break;
      case EXTENDED_REQUEST        : this.attributeValues.clear();
                                     if (tag == X_NAME || tag == X_VALUE) {
                                       this.state = tag;
                                       this.value = new StringBuilder();
                                     }
                                     break;

      // Tags with <value> tags expected:
      case CONTROL                 :
      case MODIFICATION            :
      case ADD_ATTRIBUTE           :
      case ASSERTION               :
      // The following states are in a filter tag and should contain values
      case EQUALITY_MATCH          :
      case GREATER_OR_EQUAL        :
      case LESS_OR_EQUAL           :
      case PRESENT                 :
      case APPROXIMATE_MATCH       :
      case EXTENSIBLE_MATCH        : if (tag == VALUE) {
                                       // remember our current state so we can return to it after
                                       // the value is parsed
                                       valueState = this.state;
                                       this.state = tag;
                                       this.value = new StringBuilder();
                                       String temp = attributes.getValue("xsi:type");
                                       if (temp != null && temp.equals("xsd:base64Binary")) {
                                         this.isBase64 = true;
                                       }
                                       else {
                                         this.isBase64 = false;
                                       }
                                     }
                                     else
                                       throw new SAXException("invalid tag: " + localName);
                                     break;
      default                      : throw new SAXException("invalid tag: " + localName);
      }
    }
    return;
*/
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endElement (ContentHandler)
  /**
   ** Receive notification of the end of an element.
   ** <p>
   ** By default, do nothing. Application writers may override this method in a
   ** subclass to take specific actions at the end of each element (such as
   ** finalising a tree node or writing output to a file).
   **
   ** @param  uri                the Namespace URI, or the empty string if the
   **                            element has no Namespace URI or if Namespace
   **                            processing is not being performed.
   ** @param  localName          the local name (without prefix), or the empty
   **                            string if Namespace processing is not being
   **                            performed.
   ** @param  qualifiedName              the qualified name (with prefix), or the empty
   **                            string if qualified names are not available.
   **
   ** @throws SAXException       any SAX exception, possibly wrapping another
   **                            exception.
   **
   ** @see    org.xml.sax.ContentHandler#endElement
   */
  @Override
  public void endElement(final String uri, final String localName, final String qualifiedName)
    throws SAXException {
    // no op
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   characters (ContentHandler)
  /**
   ** Receive notification of character data inside an element.
   ** <p>
   ** SAX calls this method to pass in character data stored between the start
   ** and end tags of a particular element.
   ** <p>
   ** Application writers may override this method to take specific actions for
   ** each chunk of character data (such as adding the data to a node or buffer,
   ** or printing it to a file).
   **
   ** @param  ch                 the characters.
   ** @param  start              the start position in the character array.
   ** @param  length             the number of characters to use from the
   **                            character array.
   **
   ** @see    org.xml.sax.ContentHandler#characters
   */
  @Override
  public void characters(final char[] ch, final int start, final int length) {
    if (this.state == INITIAL || this.state == ANY || this.state == FINAL || this.state == X_NAME || this.state == X_VALUE || this.state == VALUE || this.state == ERROR_MESSAGE || this.state == MESSAGE || this.state == ERROR_RESPONSE || this.state == EXTENDED_RESPONSE_NAME || this.state == EXTENDED_RESPONSE_RESPONSE || this.state == REFERRAL_LIST || this.state == SEARCH_RESULT_REFERENCE_REF)
      this.value.append(ch, start, length);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ignorableWhitespace (ContentHandler)
  /**
   ** Receive notification of ignorable whitespace in element content.
   ** <p>
   ** By default, do nothing. Application writers may override this method to
   ** take specific actions for each chunk of ignorable whitespace (such as
   ** adding data to a node or buffer, or printing it to a file).
   **
   ** @param  ch                 the whitespace characters.
   ** @param  start              the start position in the character array.
   ** @param  length             the number of characters to use from the
   **                            character array.
   **
   ** @throws SAXException       any SAX exception, possibly wrapping another
   **                            exception.
   **
   ** @see    org.xml.sax.ContentHandler#ignorableWhitespace
   */
  @Override
  public void ignorableWhitespace(final char[] ch, final int start, final int length)
    throws SAXException {
    // no op
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processingInstruction (ContentHandler)
  /**
   ** Receive notification of a processing instruction.
   ** <p>
   ** By default, do nothing. Application writers may override this method in a
   ** subclass to take specific actions for each processing instruction, such as
   ** setting status variables or invoking other methods.
   **
   ** @param  target             the processing instruction target.
   ** @param  data               the processing instruction data, or
   **                            <code>null</code> if none is supplied.
   **
   ** @throws SAXException       any SAX exception, possibly wrapping another
   **                            exception.
   **
   ** @see    org.xml.sax.ContentHandler#processingInstruction
   */
  public void processingInstruction(final String target, final String data)
    throws SAXException {
    // no op
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   skippedEntity (ContentHandler)
  /**
   ** Receive notification of a skipped entity.
   ** <p>
   ** By default, do nothing. Application writers may override this method in a
   ** subclass to take specific actions for each skipped entity.
   **
   ** @param  name               the name of the skipped entity.
   **
   ** @throws SAXException       any SAX exception, possibly wrapping another
   **                            exception.
   **
   ** @see    org.xml.sax.ContentHandler#skippedEntity
   */
  @Override
  public void skippedEntity(final String name)
    throws SAXException {
    // no op
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warning (ErrorHandler)
  /**
   ** Receive notification of a parser warning.
   ** <p>
   ** The default implementation does nothing. Application writers may override
   ** this method in a subclass to take specific actions for each warning, such
   ** as inserting the message in a log file or printing it to the console.
   **
   ** @param  e                  the warning information encoded as an
   **                            exception.
   **
   ** @throws SAXException       any SAX exception, possibly wrapping another
   **                            exception.
   **
   ** @see    org.xml.sax.ErrorHandler#warning
   ** @see    org.xml.sax.SAXParseException
   */
  @Override
  public void warning(SAXParseException e)
    throws SAXException {
    // no op
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   error (ErrorHandler)
  /**
   ** Receive notification of a recoverable parser error.
   ** <p>
   ** The default implementation does nothing. Application writers may override
   ** this method in a subclass to take specific actions for each error, such as
   ** inserting the message in a log file or printing it to the console.
   **
   ** @param  e                  the error information encoded as an exception.
   **
   ** @throws SAXException       any SAX exception, possibly wrapping another
   **                            exception.
   **
   ** @see    org.xml.sax.ErrorHandler#error
   ** @see    org.xml.sax.SAXParseException
   */
  public void error(SAXParseException e)
    throws SAXException {
    // no op
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fatalError (ErrorHandler)
  /**
   ** Report a fatal XML parsing error.
   ** <p>
   ** The default implementation throws a SAXParseException. Application writers
   ** may override this method in a subclass if they need to take specific
   ** actions for each fatal error (such as collecting all of the errors into a
   ** single report): in any case, the application must stop all regular
   ** processing when this method is invoked, since the document is no longer
   ** reliable, and the parser may no longer report parsing events.
   **
   ** @param  e                  the error information encoded as an exception.
   **
   ** @throws SAXException       any SAX exception, possibly wrapping another
   **                            exception.
   **
   ** @see    org.xml.sax.ErrorHandler#fatalError
   * @see org.xml.sax.SAXParseException
   */
  public void fatalError(final SAXParseException e)
    throws SAXException {

    throw e;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   nextRecord (DirectoryFileReader)
  /**
   ** Returns the next record in the LDIF data.
   ** <p>
   ** You can call this method repeatedly to iterate through all records in the
   ** LDIF data.
   **
   ** @return                    the next record as a {@link Binding} object
   **                            or <code>null</code> if there are no more
   **                            records.
   **
   ** @throws DirectoryException if an I/O error occurs
   **
   ** @see    Binding
   */
  @Override
  public Binding nextRecord()
    throws DirectoryException {

    return null;
  }
}