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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Generic Directory Connector

    File        :   DirectoryChangeLog.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DirectoryChangeLog.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import javax.naming.NamingException;

import javax.naming.directory.Attributes;
import javax.naming.directory.SearchResult;
import javax.naming.directory.SearchControls;

import javax.naming.ldap.Rdn;
import javax.naming.ldap.LdapContext;

import org.identityconnectors.common.Base64;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.SyncToken;
import org.identityconnectors.framework.common.objects.SyncDelta;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.SyncDeltaType;
import org.identityconnectors.framework.common.objects.ConnectorObject;
import org.identityconnectors.framework.common.objects.SyncDeltaBuilder;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.SyncResultsHandler;
import org.identityconnectors.framework.common.objects.ConnectorObjectBuilder;

import oracle.iam.identity.icf.foundation.SystemException;
import oracle.iam.identity.icf.foundation.OperationContext;

import oracle.iam.identity.icf.foundation.utility.StringUtility;
import oracle.iam.identity.icf.foundation.utility.CollectionUtility;

import oracle.iam.identity.icf.resource.DirectoryBundle;

////////////////////////////////////////////////////////////////////////////////
// class DirectoryChangeLog
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** <code>DirectoryChangeLog</code> provides the capabilities to control a LDAP
 ** synchroniaztion strategy based on changeLog access.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DirectoryChangeLog extends DirectorySynchronization {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The list of attribute operations supported by the "modify" LDIF change
   ** type.
   */
  private static final Set<String> OPERATION = CollectionUtility.set("add", "delete", "replace");

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private Number number;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Number
  // ~~~~~ ~~~~~~
  /**
   ** <code>Number</code> control the change numbers of the ChangeLog provided
   ** by a <code>Directory Service</code>.
   */
  static class Number {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final Object first;
    private final Object last;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Creates a <code>Number</code> pointer to a <code>ChangeLog</code>.
     */
    public Number(final Object first, final Object last) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.first = first;
      this.last = last;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: first
    /**
     ** Returns the first changeLog number of the range.
     **
     ** @return                    the first changeLog number of the range.
     **                            <br>
     **                            Possible object is {@link Object}.
     */
    public final Object first() {
      return this.first;
    }

    ////////////////////////////////////////////////////////////////////////////
   // Method: last
    /**
     ** Returns the last changeLog number of the range.
     **
     ** @return                    the last changeLog number of the range.
     **                            <br>
     **                            Possible object is {@link Object}.
     */
    public final Object last() {
      return this.last;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Parser
  // ~~~~~ ~~~~~~~~~
  /**
   ** <code>Parser</code> provides the capabilities to parse the LDIF format of
   ** a <code>ChangeLog</code> entry.
   */
  static class Parser implements Iterable<Parser.Entry> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attribuets
    ////////////////////////////////////////////////////////////////////////////
    
    private final String ldif;

    ////////////////////////////////////////////////////////////////////////////
    // Member classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // class Entry
    // ~~~~~ ~~~~~
    static class Entry {

      //////////////////////////////////////////////////////////////////////////
      // Constructors
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: Ctor
      /**
       ** Creates a new LDIF separator.
       */
      Entry() {
        // ensure inheritance
        super();
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // class Separator
    // ~~~~~ ~~~~~~~~~
    static class Separator extends Entry {

      //////////////////////////////////////////////////////////////////////////
      // static final attributes
      //////////////////////////////////////////////////////////////////////////

      static final Separator instance = new Separator();

      //////////////////////////////////////////////////////////////////////////
      // Constructors
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: Ctor
      /**
       ** Creates a new LDIF separator.
       */
      Separator() {
        // ensure inheritance
        super();
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // class ChangeSeparator
    // ~~~~~ ~~~~~~~~~~~~~~~
    static class ChangeSeparator extends Entry {

      //////////////////////////////////////////////////////////////////////////
      // static final attributes
      //////////////////////////////////////////////////////////////////////////

      static final ChangeSeparator instance = new ChangeSeparator();

      //////////////////////////////////////////////////////////////////////////
      // Constructors
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: Ctor
      /**
       ** Creates a new LDIF change separator.
       */
      ChangeSeparator() {
        // ensure inheritance
        super();
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // class Pair
    // ~~~~~ ~~~~
    static class Pair extends Entry {

      //////////////////////////////////////////////////////////////////////////
      // instnace attribute
      //////////////////////////////////////////////////////////////////////////

      private final String name;
      private final String value;

      //////////////////////////////////////////////////////////////////////////
      // Constructors
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: Ctor
      /**
       ** Creates new a named/value <code>Pair</code> with the provided
       ** properties.
       */
      Pair(final String name, final String value) {
        // ensure inheritance
        super();
                
        // initialize instance attribuets
        this.name  = name;
        this.value = value;
      }
    }
    
    static class EntryIterator implements Iterator<Entry> {

      //////////////////////////////////////////////////////////////////////////
      // instnace attribute
      //////////////////////////////////////////////////////////////////////////

      private Entry last;
      private Entry cursor;
      private final Iterator<String> delegate;

      //////////////////////////////////////////////////////////////////////////
      // Constructors
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: Ctor
      /**
       ** Creates new a {@link Iterator} with the provided
       ** properties.
       */
      public EntryIterator(final List<String> lines) {
        this.delegate = lines.iterator();
      }

      //////////////////////////////////////////////////////////////////////////
      // Methods of implemented interfaces
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: hasNext (Iterator)
      /**
       ** Returns <code>true</code> if this iterator has more elements when
       ** traversing the list direction. (In other words, returns
       ** <code>true</code> if {@link #next} would return an element rather than
       ** throwing an exception.)
       **
       ** @return                <code>true</code> if the list iterator has
       **                        more elements when traversing the list.
       */
      @Override
      public final boolean hasNext() {
        if (this.cursor == null) {
          this.cursor = shift();
        }
        return this.cursor != null;
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: next (Iterator)
      /**
       ** Returns the next element in the list
       **
       ** @return                the next element in the list.
       **
       ** @throws NoSuchElementException if the iteration has no next element.
       */
      @Override
      public final Entry next() {
        if (this.cursor == null) {
          this.cursor = shift();
        }
        if (this.cursor == null)
          throw new NoSuchElementException();

        Entry result = this.cursor;
        this.cursor = null;
        return result;
      }

      //////////////////////////////////////////////////////////////////////////
      // Methods group by functionality
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: next
      private Entry shift() {
        Entry result = null;
        while (result == null && this.delegate.hasNext()) {
          final String line = this.delegate.next();
          if (line.trim() .length() == 0 && this.last != ChangeSeparator.instance) {
            result = ChangeSeparator.instance;
          }
          else if (line.startsWith("-") && this.last != Separator.instance) {
            result = Separator.instance;
          }
          else {
            int index = line.indexOf(':');
            if (index > 0) {
              result = new Pair(line.substring(0, index).trim(), line.substring(index + 1).trim());
            }
          }
        }
        // always send a change separator as the last thing.
        if (result == null && this.last != ChangeSeparator.instance) {
          result = ChangeSeparator.instance;
        }
        this.last = result;
        return result;
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Parser</code> to obtain the LDIF entries from the
     ** specified LDIF string.
     **
     ** @param  ldif             the LDIF string collection.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    Parser(final String ldif) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.ldif = ldif;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: iterator (Iterable)
    public Iterator<Entry> iterator() {
      return new EntryIterator(unfolded());
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped  by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: unfolded
    private List<String> unfolded() {
      String[]     lines  = ldif.split("\n", -1);
      List<String> result = new ArrayList<String>(lines.length);
      StringBuilder builder = null;
      for (String line : lines) {
        if (line.startsWith(" ")) {
          String content = line.substring(1);
          if (builder == null) {
            builder = new StringBuilder(content);
          }
          else {
            builder.append(content);
          }
        }
        else {
          if (builder != null) {
            result.add(builder.toString());
          }
          builder = new StringBuilder(line);
        }
      }
      if (builder != null) {
        result.add(builder.toString());
      }
      return result;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a <code>DirectoryChangeLog</code> synchronization that provides
   ** the capabilities to control the query base on the
   ** <code>Directory Service ChangeLog</code>.
   **
   ** @param  context            the Directory Service endpoint connection which
   **                            is used to by this {@link DirectoryOperation}.
   **                            <br>
   **                            Allowed object is {@link DirectoryEndpoint}.
   ** @param  type               the logical object class to search the entry in
   **                            the Directory Service that belong to
   **                            <code>context</code>.
   **                            <br>
   **                            Allowed object is {@link ObjectClass}
   ** @param  control            the options that affect the way this operation
   **                            is run.
   **                            <br>
   **                            Allowed object is {@link OperationContext}.
   **
   ** @throws SystemException    if the appropriate Request Controls cannot be
   **                            initialized.
   */
  public DirectoryChangeLog(final DirectoryEndpoint context, final ObjectClass type, final OperationContext control)
    throws SystemException {

    // ensure inheritance
    super(context, type, DirectoryName.quiet(context.changeLogContainer()), SearchControls.ONELEVEL_SCOPE, control);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   number
  /**
   ** Lazy initailization of the changeLog range number.
   **
   ** @return                    a proper initailaized changeLog {@link Number}.
   **                            <br>
   **                            Possible object is {@link Number}.
   **
   ** @throws SystemException    if its fails to obtain the first and last
   **                            change number from the  connected Directory
   **                            Service or one of them isn't available.
   */
  public Number number()
    throws SystemException {

     if (this.number == null) {
      if (this.endpoint.serviceType() == DirectoryEndpoint.Type.OID) {
        // special treatment for OID because :
        // OID does not expose first change number
        // We can not ask for 2 rootDSE attributes at the same time
        // (it won't return lastChangeNumber)
        this.number = workaround();
      }
      else {
        this.number = standard();
      }
    }
    return this.number;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   syncDeltaType
  /**
   ** Converts a LDAP type of change to the ICF notation.
   **
   ** @param  changeType         the type of chnage to convert.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the ICF notation for the passed string.
   **                            <br>
   **                            Possible object is {@link SyncDeltaType}.
   */
  protected SyncDeltaType syncDeltaType(final String changeType) {
    return ("delete".equalsIgnoreCase(changeType)) ? SyncDeltaType.DELETE : SyncDeltaType.CREATE_OR_UPDATE;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lastToken (DirectorySynchronization)
  /**
   ** Returns the token corresponding to the most recent synchronization
   ** event.
   ** <p>
   ** An application that wants to receive synchronization events
   ** "starting now" -- i.e., wants to receive only native changes that occur
   ** after this method is called -- should call this method and then pass the
   ** resulting token into the sync() method.
   **
   ** @return                    the token corresponding to the most recent
   **                            synchronization event.
   **                            <br>
   **                            Possible object is {@link SyncToken}
   **
   ** @throws SystemException    if its fails to obtain the first and last
   **                            change number from the root DSE of the
   **                            connected Directory Service or one of them
   **                            isn't available.
   */
  public final SyncToken lastToken()
    throws SystemException {

    if (this.number == null)
      number();

    return new SyncToken(this.number == null ? 0 : this.number.last());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute (DirectorySynchronization)
    /**
   ** Request synchronization events--i.e., native changes to target objects.
   ** <p>
   ** This method will call the specified {@link SyncResultsHandler handler}
   ** once to pass back each matching synchronization event. Once this method
   ** returns, this method will no longer invoke the specified handler.
   ** <p>
   ** Each synchronization event contains a token that can be used to resume
   ** reading events starting from that point in the event stream. In typical
   ** usage, a client will save the token from the final synchronization event
   ** that was received from one invocation of this sync() method and then pass
   ** that token into that client's next call to this sync() method. This
   ** allows a client to "pick up where he left off" in receiving
   ** synchronization events. However, a client can pass the token from any
   ** synchronization event into a subsequent invocation of this sync()
   ** method. This will return synchronization events (that represent native
   ** changes that occurred) immediately subsequent to the event from which
   ** the client obtained the token.
   ** <p>
   ** A client that wants to read synchronization events "starting now" can
   ** call latestToken() and then pass that token into this method.
   **
   ** @param  token              the token representing the last token from the
   **                            previous sync. The {@link SyncResultsHandler}
   **                            will return any number of
   **                            <code>SyncDelta</code> objects, each of which
   **                            contains a token.
   **                            <br>
   **                            Should be <code>null</code> if this is the
   **                            client's first call to the <code>sync()</code>
   **                            method for this connector.
   **                            <br>
   **                            Allowed object is {@link SyncToken}.
   ** @param  handler            the result handler.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link SyncResultsHandler}.
   **
   ** @throws SystemException    if its fails to get to get access to the
   **                            ChangeLog of the Directory Service.
   */
  @Override
  public final void execute(final SyncToken token, final SyncResultsHandler handler)
    throws SystemException {

    final String        changeLogChangeNumber = this.endpoint.changeLogChangeNumber();
    final Set<String>   changeLogAttributes   = CollectionUtility.set("changelogUidAttr", changeLogChangeNumber, this.endpoint.changeLogTargetDN(), this.endpoint.changeLogChangeType(), "changes", "newRdn", "deleteOldRdn", "newSuperior");
    // using final arrays in order to access them from inside the anonymous
    // SearchResultHandler class below.
    final int[]     changeNumber = { startChangeNumber(token) };
    final boolean[] changeResult = { false };
    try {
      // connect to the Directory Service
      do {
        final String             filter = filter(changeLogChangeNumber, changeNumber[0], this.control.limit);
        final DirectorySearch    search = DirectorySearch.simple(this.endpoint, this.control.limit, filter, changeLogAttributes);
        // connect the search to the Directory Service
        search.endpoint.connect();
        try {
          final List<SearchResult> result = search.next();
          for (SearchResult entry : result) {
            int currentNumber = convertToInt(DirectoryEntry.value(entry, changeLogChangeNumber), -1);
            if (currentNumber > changeNumber[0]) {
              changeNumber[0] = currentNumber;
            }
//          SyncDelta delta = syncDelta(this.base, entry, currentNumber, changeLogAttributes, control);
            final SyncDelta delta = syncDelta(entry, currentNumber);
            if (delta != null) {
              changeResult[0] = handler.handle(delta);
            }
          }
        }
        finally {
          search.endpoint.disconnect();
        }
      } while (changeResult[0]);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////
    
  //////////////////////////////////////////////////////////////////////////////
  // Method:   standard
  /**
   ** Returns the first and last change number from the root DSE of the
   ** connected Directory Service.
   **
   ** @return                    the first and last change number obtained from
   **                            the root DSE of the connected Directory
   **                            Service.
   **                            <br>
   **                            Possible object is {@link Number}.
   **
   ** @throws SystemException    if its fails to obtain the first and last
   **                            change number from the root DSE of the
   **                            connected Directory Service or one of them
   **                            isn't available.
   */
  private Number standard()
    throws SystemException {

    // request all attributes from the rootDSE
    final String[]    emit = {this.endpoint.changeLogNumberFirst(), this.endpoint.changeLogNumberLast()};
    final LdapContext root = this.endpoint.rootDSE();
    try {
      final Attributes attr = root.getAttributes(DirectoryName.ROOT, emit);
      final String first = DirectoryEntry.value(attr, emit[0]);
      if (first == null)
        throw DirectoryException.changeLogNumber(emit[0]);
        
      // obtain the last change number
      final String last = DirectoryEntry.value(attr, emit[1]);
      if (last == null)
        throw DirectoryException.changeLogNumber(emit[1]);

      this.number = new Number(convertToInt(first, 0), convertToInt(last, 0));
    }
    catch (NamingException e) {
      throw SystemException.unhandled(e);
    }
    finally {
      try {
        root.close();
      }
      catch (NamingException e) {
        throw SystemException.unhandled(e);
      }
    }
    return this.number;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   workaround
  /**
   ** Returns the first and last change number from the root DSE of the
   ** connected Oracle Internet Directory.
   **
   ** @return                    the first and last change number obtained from
   **                            the root DSE of the connected Oracle Internet
   **                            Directory.
   **                            <br>
   **                            Possible object is {@link Number}.
   **
   ** @throws SystemException    if its fails to obtain the first and last
   **                            change number from the connected Oracle
   **                            Internet Directory or one of them isn't
   **                            available.
   */
  private Number workaround()
    throws SystemException {

    final String      base = this.endpoint.changeLogContainer();
    final String[]    emit = {this.endpoint.changeLogNumberFirst(), this.endpoint.changeLogNumberLast()};
    final LdapContext root = this.endpoint.rootDSE();
    try {
      final Attributes attrs = root.getAttributes("", emit);

      // obtain the last change number
      final String last = DirectoryEntry.value(attrs, emit[1]);
      if (last == null)
        throw DirectoryException.changeLogNumber(emit[1]);

      // will read the first entry in the changelog
      // this is a workaround for OID that do not expose the firstChangeNumber
      // attribute in rootDSE
      final DirectoryLookup search = DirectoryLookup.build(this.endpoint, this.type, DirectoryName.build(base), SearchControls.ONELEVEL_SCOPE, "(objectClass=changeLogEntry)", CollectionUtility.set("changeNumberAttr"));
      final SearchResult    result = search.execute();
      // obtain the last change number
      final String first = DirectoryEntry.value(result, "changeNumberAttr");
      if (first == null)
        throw DirectoryException.changeLogNumber(emit[0]);

      this.number = new Number(convertToInt(first, 0), convertToInt(last, 0));
    }
    catch (NamingException e) {
      throw SystemException.unhandled(e);
    }
    finally {
      try {
        root.close();
      }
      catch (NamingException e) {
        throw SystemException.unhandled(e);
      }
    }
    return this.number;
  }

  private int startChangeNumber(final SyncToken lastToken)
    throws SystemException {

    final Integer token = lastToken != null ? (Integer)lastToken.getValue() : null;
    if (token == null) {
      return (Integer)number().first;
    }
    // since the token value is the last value.
//    return token + 1;
    return token;
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   convertToInt
  /**
   ** Converting the string <code>number</code> to an integger value.
   **
   ** @param  number             the string to convert to an interger.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  defaultValue       the value to return if <code>number</code>
   **                            could not converted.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the string converted to an integer.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  private static int convertToInt(final String number, final int defaultValue) {
    int result = defaultValue;
    if (number != null && number.length() > 0) {
      int dot = number.indexOf('.');
      try {
        result = Integer.parseInt((dot > 0) ? number.substring(0, dot) : number);
      }
      catch (NumberFormatException e) {
        // intentionally left blank
        ;
      }
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   syncDelta
  /**
   ** Generates the appropriate instance to return the created/modified
   ** entries found in the Directory service.
   **
   **
   ** @return the                filter string
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws SystemException    if the modifier or creator attribute could not
   **                            be extracted.
   */
  private SyncDelta syncDelta(final SearchResult result, final int changeNumber)
    throws SystemException {

    info(DirectoryBundle.string(DirectoryMessage.CHANGELOG_LOGENTRY_BEGIN, changeNumber));
    final String targetDN = DirectoryEntry.value(result, this.endpoint.changeLogTargetDN());
    if (targetDN == null) {
      error("", DirectoryBundle.string(DirectoryMessage.CHANGELOG_TARGETDN_NULL));
      return null;
    }
    DirectoryName cursor = DirectoryName.quiet(targetDN);
    // skip changes of the changelog it self
    if (this.base.equals(cursor)) {
      warning(DirectoryBundle.string(DirectoryMessage.CHANGELOG_TARGETDN_BASE, this.base));
      return null;
    }

    final String           changeType = DirectoryEntry.value(result, this.endpoint.changeLogChangeType());
    final SyncDeltaType    deltaType  = syncDeltaType(changeType);
    final SyncDeltaBuilder builder    = new SyncDeltaBuilder();
    builder.setToken(new SyncToken(changeNumber));
    builder.setDeltaType(deltaType);

    if (SyncDeltaType.DELETE.equals(deltaType)) {
      info(DirectoryBundle.string(DirectoryMessage.CHANGELOG_CHGTYPE_DELETE, changeNumber));
      final Rdn                    cursorRDN  = cursor.prefix().getRdn(0);
      final Uid                    identifier = createIdentifier(targetDN, result);
      final ConnectorObjectBuilder object     = new ConnectorObjectBuilder();
      object.setObjectClass(this.type);
      object.setUid(identifier);
      object.setName(cursor.toString());
      // we add the RDN as a key-value pair to the object
      // It is ok if it gets overwritten afterwards
      object.addAttribute(AttributeBuilder.build(cursorRDN.getType(), cursorRDN.getValue()));
      builder.setObject(object.build());
      builder.setUid(identifier);
      return builder.build();
    }

    final String                    entry   = DirectoryEntry.value(result, "changes");
    final Map<String, List<Object>> changes = changes(changeType, entry);
    // the modifiers name is in the changes attribute applying the filter to the
    // changes attribute
    if (filterByModifier(changes)) {
      warning(DirectoryBundle.string(DirectoryMessage.CHANGELOG_FILTER_MODIFIER));
      return null;
    }

    // always specify the attributes to get.
    // This will return attributes with empty values when the attribute is not
    // present, allowing the client to detect that the attribute has been
    // removed.
    final Set<String> returning = CollectionUtility.caseInsensitiveSet();
    if (returning.isEmpty()) {
      returning.addAll(this.control.emit);
    }
    // we add the attributes that are coming from the config if nothing is
    // provided by the control option
    if (returning.isEmpty()) {
      returning.add(this.endpoint.schemaSupport(this.type).prefix);
    }
    // do not retrieve the password attribute from the entry
    // (usually it is an unusable hashed value anyway).
    // We will use the one from the change log below.
    returning.remove(this.endpoint.accountCredential());

    // if the change type was modrdn, we need to compute the DN that the entry
    // was modified to.
    DirectoryName moved = cursor;
    // when applying a moddn operation with new superior in OID, the
    // changetype appears as "moddn" in the chnagelog
    if ("modrdn".equalsIgnoreCase(changeType) || "moddn".equalsIgnoreCase(changeType)) {
      String newRdn = DirectoryEntry.value(result, "newRdn");
      if (StringUtility.blank(newRdn)) {
        // in case of OID the newRdn attribute is stored in the changes
        // attribute
        newRdn = (String)changes.get("newRdn").get(0);
      }
      if (StringUtility.blank(newRdn)) {
        error("", DirectoryBundle.string(DirectoryMessage.CHANGELOG_TARGETRDN_NOTEXISTS));
        return null;
      }
      String newSuperior = DirectoryEntry.value(result, "newSuperior");
      // in case of OID the newSuperior attribute is stored in the changes
      // attribute under the newSupDn attribute
      if (StringUtility.blank(newSuperior)) {
        newSuperior = changes.containsKey("newSupDN") ? (String) changes.get("newSupDN").get(0) : null;
      }
      cursor = DirectoryName.move(cursor, DirectoryName.build(newSuperior));
    }
    final boolean  removeEnabled     = returning.add(this.endpoint.entryStatusAttribute());
    // if objectClass is not in the list of attributes to return, prepare to
    // remove it later.
    final boolean  removeObjectClass = returning.add(DirectoryEndpoint.OBJECT_CLASS_DEFAULT);

//    final DirectoryFilter filter = DirectoryFilter.entry(newTargetDN).with(null);//getModifiedEntrySearchFilter());
    ConnectorObject object = DirectoryLookup.entry(this.endpoint, this.type, cursor, returning);
    if (object == null) {
      info(DirectoryBundle.string(DirectoryMessage.CHANGELOG_TARGETDN_NOTEXISTS));
      return null;
    }
    Attribute objectClass = object.getAttributeByName(DirectoryEndpoint.OBJECT_CLASS_DEFAULT);
//    List<String> objectClasses = checkedListByFilter(nullAsEmpty(objectClass.getValue()), String.class);
//    if (filterOutByObjectClasses(objectClasses)) {
//      info(DirectoryBundle.string(DirectoryMessage.CHANGELOG_FILTER_OBJECTCLASS));
//      return null;
//    }
    if (removeEnabled || removeObjectClass) {
      ConnectorObjectBuilder objectBuilder = new ConnectorObjectBuilder();
      objectBuilder.setObjectClass(object.getObjectClass());
      objectBuilder.setUid(object.getUid());
      objectBuilder.setName(object.getName());
      // we add the RDN as a key-value pair to the object
      // It is ok if it gets overwritten afterwards
      Rdn movedRDN  = moved.prefix().getRdn(0);
      objectBuilder.addAttribute(AttributeBuilder.build(movedRDN.getType(), movedRDN.getValue()));

      if (removeObjectClass || removeEnabled) {
        for (Attribute attr : object.getAttributes()) {
          if ((removeObjectClass && attr == objectClass) || (removeEnabled && StringUtility.equalIgnoreCase(this.endpoint.entryStatusAttribute(), attr.getName()))) {
            // do nothing
          }
          else {
            objectBuilder.addAttribute(attr);
          }
        }
      }
      object = objectBuilder.build();
    }
//    log.ok("Creating sync delta for created or updated entry");
    if ("modrdn".equalsIgnoreCase(changeType)) {
      Uid previousUid = createIdentifier(targetDN, result);
      builder.setPreviousUid(previousUid);
    }
    builder.setUid(object.getUid());
    builder.setObject(object);
    return builder.build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createIdentifier
  /**
   ** Returns either a UID based on the DN or on the server unique ID of the
   ** entry.
   ** <br>
   ** The algorithm uses the values provided in the config for uidAttribute and
   ** changelogUidAttribute.
   ** <br>
   ** That method must be used when the entry is expected to have already been
   ** removed from the server (ie. DELETE and MODRDN operations)
   **
   ** @param  changeLogEntry     the changelog entry
   **
   ** @return                    a UID object
   */
  private Uid createIdentifier(final String targetDN, final SearchResult changeLogEntry)
    throws SystemException {

    String identifier = null;
    String attribute  = this.endpoint.schemaSupport(this.type).prefix;
    // we can only set the previous Uid if it is the entry DN, which is readily
    // available.
    if (DirectoryEntry.distinguishedName(attribute)) {
      identifier = targetDN;
    }
    else {
      String entryIdentifier = this.entryIdentifier;
      identifier = DirectoryEntry.value(changeLogEntry, DirectoryEntry.distinguishedName(entryIdentifier) ? "targetDN" : entryIdentifier);
      if (identifier == null) {
        // we only support the DN and nsuniqueid kind of attributes
        warning(DirectoryBundle.string(DirectoryMessage.CHANGELOG_CHGTYPE_ATTRIBUTE));
//        throw new ConnectorException(conn.format("UnsupportedUidAttribute", null, changelogUidAttr, changeLogEntry.getDN()));
      }
    }
    return new Uid(identifier);
  }

  protected Map<String, List<Object>> changes(final String changeType, final String ldif) {
    final Iterator<Parser.Entry>    lines  = new Parser(ldif).iterator();
    final Map<String, List<Object>> result = new HashMap<String, List<Object>>();
    if ("modify".equalsIgnoreCase(changeType)) {
      while (lines.hasNext()) {
        Parser.Entry line = lines.next();
        // we only expect one change, so ignore any change separators.
        if (line instanceof Parser.Separator || line instanceof Parser.ChangeSeparator) {
          continue;
        }
        final Parser.Pair operation = (Parser.Pair)line;
//        String operation = pair.name;
//        String attrName  = pair.value;
        if (OPERATION.contains(operation.name)) {
          List<Object> values = new ArrayList<Object>();
          while (lines.hasNext()) {
            line = lines.next();
            if (line instanceof Parser.Separator) {
              break;
            }
            if (line instanceof Parser.ChangeSeparator) {
              continue; // OID changelog...
            }
            final Parser.Pair pair = (Parser.Pair)line;
            Object value = decode(pair);
            if (value != null) {
              values.add(value);
            }
          }
          if (!values.isEmpty()) {
            result.put(operation.value, values);
          }
          else if ("delete".equalsIgnoreCase(operation.name)) {
            result.put(operation.value, null);
          }
        }
      }
    }
    else if ("add".equalsIgnoreCase(changeType)) {
      while (lines.hasNext()) {
        Parser.Entry line = lines.next();
        // We only expect one change, so ignore any change separators.
        if (line instanceof Parser.Separator || line instanceof Parser.ChangeSeparator) {
          continue;
        }
        Parser.Pair pair = (Parser.Pair)line;
        Object value = decode(pair);
        if (value != null) {
          List<Object> values = result.get(pair.name);
          if (values == null) {
            values = new ArrayList<Object>();
            result.put(pair.name, values);
          }
          values.add(value);
        }
      }
    }
    else if ("modrdn".equalsIgnoreCase(changeType) || "moddn".equalsIgnoreCase(changeType)) {
      while (lines.hasNext()) {
        Parser.Entry line = lines.next();
        // we only expect one change, so ignore any change separators.
        if (line instanceof Parser.Separator || line instanceof Parser.ChangeSeparator) {
          continue;
        }
        Parser.Pair pair = (Parser.Pair)line;
        List<Object> values = new ArrayList<Object>();
        values.add(pair.value);
        result.put(pair.name, values);
      }
    }
    // returning an empty map when changeType is "delete".
    return result;
  }

  protected Object decode(Parser.Pair pair) {
    if (pair.value.startsWith(":")) {
      // this is a Base64 encoded value...
      String base64 = pair.value.substring(1).trim();
      try {
        return Base64.decode(base64);
        // TODO:
        // the adapter had code here to convert the byte array to a string if
        // the attribute was of a string type.
        // Since that information is in the schema and we don't have access to
        // the resource schema, leaving that functionality out for now.
      }
      catch (Exception e) {
//        error("", "Could not decode attribute {0} with Base64 value {1}", pair.name, pair.value);
        return null;
      }
    }
    else {
      return pair.value;
    }
  }


  private String filter(final String changeNumber, final int start, final int blockSize)
    throws SystemException {

    boolean             filterWithOrInsteadOfAnd = true; // must be configurable
    boolean             filterByLogEntryOClass   = true; // must be configurable
    final StringBuilder result                   = new StringBuilder();
    if (filterWithOrInsteadOfAnd) {
      if (filterByLogEntryOClass) {
        result.append("(&(objectClass=changeLogEntry)");
      }
      result.append("(|(");
      result.append(changeNumber);
      result.append('=');
      result.append(start);
      result.append(')');

      int endChangeNumber = start + blockSize;
      for (int i = start + 1; i <= endChangeNumber; i++) {
        result.append("(");
        result.append(changeNumber);
        result.append('=');
        result.append(i);
        result.append(')');
      }

      result.append(')');
      if (filterByLogEntryOClass) {
        result.append(')');
      }
    }
    else {
      if (filterByLogEntryOClass) {
        result.append("(objectClass=changeLogEntry)");
      }
      result.append("(");
      result.append(changeNumber);
      result.append(">=");
      result.append(start);
      result.append(')');

      int endChangeNumber = start + blockSize;
      for (int i = start + 1; i <= endChangeNumber; i++) {
        result.append("(");
        result.append(changeNumber);
        result.append("<=");
        result.append(i);
        result.append(')');
      }

      if (filterByLogEntryOClass) {
        result.append(')');
      }
    }
    return result.toString();
  }
}