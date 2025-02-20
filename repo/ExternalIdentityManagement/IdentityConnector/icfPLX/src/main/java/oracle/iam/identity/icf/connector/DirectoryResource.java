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

    File        :   DirectoryResource.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DirectoryResource.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector;

import java.util.Set;
import java.util.List;
import java.util.HashSet;
import java.util.TimeZone;
import java.util.GregorianCalendar;

import java.text.SimpleDateFormat;

import javax.naming.directory.SearchResult;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.SyncToken;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.SyncResultsHandler;
import org.identityconnectors.framework.common.objects.OperationalAttributes;

import oracle.iam.identity.icf.foundation.SystemException;
import oracle.iam.identity.icf.foundation.OperationContext;

import oracle.iam.identity.icf.resource.DirectoryBundle;

import org.identityconnectors.framework.common.objects.ConnectorObject;
import org.identityconnectors.framework.common.objects.SyncDelta;
import org.identityconnectors.framework.common.objects.SyncDeltaBuilder;
import org.identityconnectors.framework.common.objects.SyncDeltaType;

////////////////////////////////////////////////////////////////////////////////
// class DirectoryResource
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** <code>DirectoryResource</code> provides the capabilities to control a query
 ** based on access operational timestamp attributes of an entry in the
 ** Directory Service.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DirectoryResource extends DirectorySynchronization {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** UTC TimeZone is assumed to never change over JVM lifetime */
  private static final TimeZone    TIME_ZONE_UTC = TimeZone.getTimeZone("UTC");

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method: Ctor
  /**
   ** Creates a <code>DirectoryResource</code> synchronization that provides the
   ** capabilities to control the query base on the operational timestamp
   ** attributes an entry in the Directory Service.
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
   ** @param  base               the base DN to search from
   **                            <br>
   **                            Allowed object is {@link DirectoryName}.
   ** @param  scope              the search scope that control the search.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  control            the options that affect the way this operation
   **                            is run.
   **                            <br>
   **                            Allowed object is {@link OperationContext}.
   **
   ** @throws SystemException    if the passed properties didn't meet the
   **                            requirements of this operation.
   */
  public DirectoryResource(final DirectoryEndpoint context, final ObjectClass type, final DirectoryName base, final int scope, final OperationContext control) 
    throws SystemException {

    // ensure inheritance
    super(context, type, base, scope, control);
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
   ** @throws SystemException    if its fails to get to obtain the most recent
   **                            synchronization event.
   */
  @Override
  public final SyncToken lastToken()
    throws SystemException {

    final GregorianCalendar calendar = new GregorianCalendar(TIME_ZONE_UTC);
    return new SyncToken(generalizedTime(calendar.getTimeInMillis(), false));
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

    final Set<String> emit = new HashSet<String>();
    emit.add(this.entryIdentifier);
    emit.add(this.entryCreatorAttribute);
    emit.add(this.entryCreatedAttribute);
    emit.add(this.entryModifierAttribute);
    emit.add(this.entryModifiedAttribute);
    for (String cursor : this.control.emit) {
      // handle special attributes appropriatly
      if (Uid.NAME.equals(cursor) || Name.NAME.equals(cursor) || OperationalAttributes.ENABLE_NAME.equals(cursor) || OperationalAttributes.PASSWORD_NAME.equals(cursor))
        continue;

      // go an extra mile to exclude the embedded object like groups, tenants
      // or spaces from the query
      else if (ObjectClass.GROUP.getObjectClassValue().equals(cursor)) {
        this.groups.add(ObjectClass.GROUP);
      }
      else if (DirectorySchema.ROLE.getObjectClassValue().equals(cursor)) {
        this.groups.add(DirectorySchema.ROLE);
      }
      else {
        emit.add(cursor);
      }
    }

    final String        timeStamp = (String)token.getValue();
    final boolean       and       = this.control.filter != null;
    final StringBuilder filter    = new StringBuilder();
    if (and) {
      filter.append("(&");
      filter.append("(").append(this.control.filter).append(")");
    }
    filter.append("(|");
    filter.append("(").append(this.entryCreatedAttribute).append(">=").append(timeStamp).append(")");
    filter.append("(").append(this.entryModifiedAttribute).append(">=").append(timeStamp).append(")");
    filter.append(")");

    if (and) {
      filter.append(")");
    }
    final DirectorySearch search = DirectorySearch.strategy(this.endpoint, this.type, this.base, this.scope, this.control.limit, filter.toString(), emit, null);
    final Handler         xxxxxx = new Handler() {
      public boolean handle(final SearchResult result)
        throws SystemException {

        final SyncDelta delta = syncDelta(result);
        return (delta == null) ? true : handler.handle(delta);
      }
    };
    try {
      // connect to the Directory Service
      this.endpoint.connect(this.endpoint.base());
      do {
        final List<SearchResult> result = search.next();
        for (SearchResult entry : result) {
          if (!xxxxxx.handle(entry))
            break;
        }
      } while (search.hasMore());
    }
    finally {
      // relase the connection
      this.endpoint.disconnect();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////
    
  //////////////////////////////////////////////////////////////////////////////
  // Method:   generalizedTime
  /**
   ** Converts a date (in milliseconds) into a string that is understood by
   ** LDAP.
   **
   ** @param  milliSecond        the date in milliseconds.
   **                            <br>
   **                            Allowed object is <code>long</code>.
   ** @param  precision          <code>true</code> if the milliseconds must be
   **                            part of the returned generalized string,
   **                            <code>false</code> if the milliseconds must not
   **                            be present in the generalized time string.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the date as an RFC4517 compliant generalized
   **                            time string.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  protected String generalizedTime(final long milliSecond, final boolean precision) {
    String format = this.endpoint.timestampFormat();
    if (!precision)
      format = format.replace(".SSS", "");

    final SimpleDateFormat formatter = new SimpleDateFormat(format);
    return formatter.format(milliSecond);
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
  private SyncDelta syncDelta(final SearchResult result)
    throws SystemException {

    // check below could have been done by modifying the filter.
    // this way is preferred because we are reusing common code shared between
    // all the implementations of the Sync strategies.
    if (filterByModifier(result)) {
      warning(DirectoryBundle.string(DirectoryMessage.CHANGELOG_FILTER_MODIFIER));
      return null;
    }

    String timeStamp = DirectoryEntry.value(result, this.entryModifiedAttribute);
    if (timeStamp == null) {
      timeStamp = DirectoryEntry.value(result, this.entryCreatedAttribute);
    }

    final ConnectorObject object   = connectorObject(result);
    final SyncDeltaBuilder builder = new SyncDeltaBuilder();
    builder.setDeltaType(SyncDeltaType.CREATE_OR_UPDATE);
    builder.setToken(new SyncToken(timeStamp));
    builder.setUid(object.getUid());
    builder.setObject(object);
    return builder.build();
  }
}
