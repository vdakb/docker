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
    Subsystem   :   Foundation Shared Library

    File        :   TableFormatter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    TableFormatter.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.foundation.logging;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import java.io.Writer;
import java.io.PrintWriter;
import java.io.OutputStream;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;

import java.io.StringWriter;

import oracle.iam.identity.icf.foundation.SystemConstant;

////////////////////////////////////////////////////////////////////////////////
// class TableFormatter
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** A class which can be used to construct tables of information to be displayed
 ** in a terminal. Once built the table can be output using a
 ** {@link Serializer}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TableFormatter {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The default string which should be used to separate one column from the
   ** next (not including padding).
   */
  static final String       SEPARATOR_COLUMN_DEFAULT  = SystemConstant.EMPTY;

  /**
   ** The default character which should be used to separate the table heading
   ** row from the rows beneath.
   */
  static final char         SEPARATOR_HEADING_DEFAULT = '-';

  /**
   ** The default padding which will be used to separate a cell's contents from
   ** its adjacent column separators.
   */
  static final int          PADDING_DEFAULT           = 1;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // current column number in the current row where 0 represents the left-most
  // column in the table
  private int               column                    = 0;

  // current number of rows in the table.
  private int               height                    = 0;

  // current number of columns in the table.
  private int               width                     = 0;

  // list of column headings.
  private List<String>      header                    = new ArrayList<String>();

  // list of table rows.
  private List<List<String>> rows                     = new ArrayList<List<String>>();

  // current width of each column.
  private List<Integer>     columns                   = new ArrayList<Integer>();

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // abstract class Serializer
  // ~~~~~~~~ ~~~~~ ~~~~~~~~~~
  /**
   ** An interface for serializing tables.
   ** <p>
   ** The default implementation for each method is to do nothing.
   ** <br>
   ** Implementations must override methods as required.
   */
  public static abstract class Serializer {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Serializer</code> that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    protected Serializer() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: header
    /**
     ** Prints a column header contents.
     **
     ** @param  s                the column header contents.
     */
    public void header(@SuppressWarnings("unused") final String s) {
      // intentionally left blank
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: column
    /**
     ** Prints a table column.
     **
     ** @param  s                the column contents.
     */
    public void column(@SuppressWarnings("unused")final String s) {
      // intentionally left blank
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: column
    /**
     ** Defines a column width in the table.
     **
     ** @param  width            the width of the column in characters.
     */
    public void column(@SuppressWarnings("unused")final int width) {
      // intentionally left blank
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: tableStart
    /**
     ** Start a new table having the specified number of rows and columns.
     **
     ** @param  height           the number of rows in the table.
     ** @param  width            te number of columns in the table.
     */
    public void tableStart(@SuppressWarnings("unused") final int height, @SuppressWarnings("unused") final int width) {
      // intentionally left blank
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: tableEnd
    /**
     ** Finish printing the table.
     */
    public void tableEnd() {
      // intentionally left blank
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: headerStart
    /**
     ** Prepare to start printing the column headings.
     */
    public void headerStart() {
      // intentionally left blank
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: headerEnd
    /**
     ** Finish printing the column headings.
     */
    public void headerEnd() {
      // intentionally left blank
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: rowStart
    /**
     ** Prepare to start printing a new row of the table.
     */
    public void rowStart() {
      // intentionally left blank
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: rowEnd
    /**
     ** Finish printing the current row of the table.
     */
    public void rowEnd() {
      // intentionally left blank
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: contentStart
    /**
     ** Prepare to start printing the table contents.
     */
    public void contentStart() {
      // intentionally left blank
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: contentEnd
    /**
     ** Finish printing the table contents.
     */
    public void contentEnd() {
      // intentionally left blank
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // abstract class Printer
  // ~~~~~~~~ ~~~~~ ~~~~~~~
  /**
   ** An interface for incrementally configuring a table serializer.
   ** <p>
   ** Once configured, the table printer can be used to create a new
   ** {@link Serializer} instance using the {@link #serializer()} method.
   */
  public static abstract class Printer {

    // indicates whether or not the headings should be output.
    protected boolean     heading = true;

    // the output destination.
    protected PrintWriter writer  = null;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Creates a new table printer for the specified writer.
     ** <br>
     ** The table printer will have the following initial settings:
     ** <ul>
     **   <li>headings will be displayed
     **   <li>no separators between columns
     **   <li>columns are padded by one character
     ** </ul>
     **
     ** @param  writer           the writer to output tables to.
     */
    protected Printer(final Writer writer) {
      // ensure inheritance
      super();
      
      // initialize instance
      this.writer = new PrintWriter(writer);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: heading
    /**
     ** Specify whether the column headings should be displayed or not.
     **
     ** @param  state            <code>true</code> if column headings should be
     **                          displayed; otherwise <code>false</code>.
     */
    public void heading(final boolean state) {
      this.heading = state;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: serializer
    /**
     ** Creates a new table serializer based on the configuration of the table
     ** printer.
     **
     ** @return                  a new table serializer based on the
     **                          configuration of the table printer.
     */
    public abstract Serializer serializer();
  }

  //////////////////////////////////////////////////////////////////////////////
  // final class Text
  // ~~~~~ ~~~~~ ~~~~
  /**
   ** An interface for incrementally configuring a table serializer.
   ** <p>
   ** Once configured, the table printer can be used to create a new
   ** {@link Serializer} instance using the {@link #serializer()} method.
   */
  public static final class Text extends Printer {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    // number of characters the table should be indented.
    private int                         indent                      = 0;

    // string which should be used to separate one column from the next (not
    // including padding).
    private String                      columnSeparator             = SEPARATOR_COLUMN_DEFAULT;

    // character which should be used to separate the table heading row from the
    // rows beneath.
    private char                        headingSeparator            = SEPARATOR_HEADING_DEFAULT;

    // column where the heading separator should begin.
    private int                         headingSeparatorStartColumn = 0;

    // table indicating whether or not a column is fixed width.
    private final Map<Integer, Integer> fixedColumns                = new HashMap<Integer, Integer>();

    // padding which will be used to separate a cell's contents from its
    // adjacent column separators.
    private int                         padding                     = PADDING_DEFAULT;

    // total permitted width for the table which expandable columns can use up.
    private int                         totalWidth                  = SystemConstant.TERMINAL_LINE_WIDTH_MAX;

    ////////////////////////////////////////////////////////////////////////////
    // Member classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // class Serializer
    // ~~~~~ ~~~~~~~~~~
    /**
     ** Default Table serializer implementation.
     */
    private final class Serializer extends TableFormatter.Serializer {

      //////////////////////////////////////////////////////////////////////////
      // instance attributes
      //////////////////////////////////////////////////////////////////////////

      // current column being output
      private int                 column  = 0;

      // width of the table in columns
      private int                 total   = 0;

      // real column widths taking into account size constraints but not
      // including padding or separators
      private final List<Integer> columns = new ArrayList<Integer>();

      // cells in the current row
      private final List<String>  current = new ArrayList<String>();

      // padding to use for indenting the table
      private final String        indent;

      //////////////////////////////////////////////////////////////////////////
      // Constructors
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: Ctor
      private Serializer() {
        // ensure inheritance
        super();

        // compute the indentation padding.
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < Text.this.indent; i++)
          builder.append(SystemConstant.BLANK);

        this.indent = builder.toString();
      }

      //////////////////////////////////////////////////////////////////////////
      // Methods grouped by functionality
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: header (overriden)
      /**
       ** Prints a column heading.
       **
       ** @param  s              the column heading contents.
       */
      @Override
      public void header(final String s) {
        if (Text.this.heading) {
          column(s);
        }
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: column (overriden)
      /**
       ** Prints a table column cell.
       **
       ** @param  s              column cell.
       */
      @Override
      public void column(final String s) {
        this.current.add(s);
        this.column++;
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: column (overriden)
      /**
       ** Defines a column in the table.
       **
       ** @param  width          the width of the column in characters.
       */
      @Override
      public void column(final int width) {
        this.columns.add(width);
        this.total++;
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: tableEnd (overriden)
      /**
       ** Finish printing the table.
       */
      @Override
      public void tableEnd() {
        Text.this.writer.flush();
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: headerStart (overriden)
      /**
       ** Prepare to start printing the column headings.
       */
      @Override
      public void headerStart() {
        effectiveWidth();
        this.column = 0;
        this.current.clear();
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: headerEnd (overriden)
      /**
       ** Finish printing the column headings.
       */
      @Override
      public void headerEnd() {
        if (Text.this.heading) {
          rowEnd();
          // print the header separator.
          StringBuilder builder = new StringBuilder(this.indent);
          for (int i = 0; i < this.total; i++) {
            int width = this.columns.get(i);
            if (this.total > 1) {
              if (i == 0 || i == (this.total - 1)) {
                // only one lot of padding for first and last columns
                width += Text.this.padding;
              }
              else {
                width += Text.this.padding * 2;
              }
            }

            for (int j = 0; j < width; j++) {
              if (Text.this.headingSeparatorStartColumn > 0) {
                if (i < Text.this.headingSeparatorStartColumn) {
                  builder.append(SystemConstant.BLANK);
                }
                else if (i == Text.this.headingSeparatorStartColumn && j < Text.this.padding) {
                  builder.append(SystemConstant.BLANK);
                }
                else {
                  builder.append(Text.this.headingSeparator);
                }
              }
              else {
                builder.append(Text.this.headingSeparator);
              }
            }

            if ((i >= Text.this.headingSeparatorStartColumn) && i < (this.total - 1)) {
              builder.append(Text.this.columnSeparator);
            }
          }
          Text.this.writer.println(builder.toString());
        }
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: rowStart (overriden)
      /**
       ** Prepare to start printing a new row of the table.
       */
      @Override
      public void rowStart() {
        this.column = 0;
        this.current.clear();
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: rowEnd (overriden)
      /**
       ** Finish printing the current row of the table.
       */
      @Override
      public void rowEnd() {
        boolean remain;
        do {
          final StringBuilder builder = new StringBuilder(this.indent);
          remain = false;
          for (int i = 0; i < this.current.size(); i++) {
            int    width    = this.columns.get(i);
            String contents = this.current.get(i);

            // determine what parts of contents can be displayed on this line
            String head;
            String tail = null;
            if (contents == null) {
              // this cell has been displayed fully
              head = SystemConstant.EMPTY;
            }
            else if (contents.length() > width) {
              // we're going to have to split the cell on next word boundary
              int endIndex = contents.lastIndexOf(SystemConstant.BLANK, width);
              if (endIndex == -1) {
                endIndex = width;
                head = contents.substring(0, endIndex);
                tail = contents.substring(endIndex);
              }
              else {
                head = contents.substring(0, endIndex);
                tail = contents.substring(endIndex + 1);
              }
            }
            else {
              // the contents fits ok
              head = contents;
            }
            // add this cell's contents to the current line
            if (i > 0) {
              // add right padding for previous cell
              for (int j = 0; j < Text.this.padding; j++) {
                builder.append(SystemConstant.BLANK);
              }
              // add separator.
              builder.append(Text.this.columnSeparator);
              // add left padding for this cell
              for (int j = 0; j < Text.this.padding; j++) {
                builder.append(SystemConstant.BLANK);
              }
            }
            // add cell contents
            builder.append(head);
            // now pad with extra space to make up the width only if it's not the
            // last cell
            if (i != this.current.size() - 1) {
              for (int j = head.length(); j < width; j++)
                builder.append(' ');
            }
            // update the row contents.
            this.current.set(i, tail);
            if (tail != null)
              remain = true;
          }
          // output the line.
          Text.this.writer.println(builder.toString());
        } while (remain);
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: effectiveWidth
      /**
       ** We need to calculate the effective width of each column.
       */
      private void effectiveWidth() {
        // first calculate the minimum width so that we know how much expandable
        // columns can expand.
        int minWidth = Text.this.indent;
        int expandableColumnSize = 0;
        for (int i = 0; i < this.total; i++) {
          int actualSize = this.columns.get(i);
          if (Text.this.fixedColumns.containsKey(i)) {
            int requestedSize = Text.this.fixedColumns.get(i);
            if (requestedSize == 0) {
              expandableColumnSize += actualSize;
            }
            else {
              this.columns.set(i, requestedSize);
              minWidth += requestedSize;
            }
          }
          else {
            minWidth += actualSize;
          }

          // must also include padding and separators.
          if (i > 0)
            minWidth += Text.this.padding * 2 + Text.this.columnSeparator.length();
        }

        if (minWidth > Text.this.totalWidth) {
          // the table is too big: leave expandable columns at their requested
          // width, as there's not much else that can be done.
        }
        else {
          int available = Text.this.totalWidth - minWidth;
          if (expandableColumnSize > available) {
            // only modify column sizes if necessary.
            for (int i = 0; i < total; i++) {
              int actualSize = this.columns.get(i);
              if (Text.this.fixedColumns.containsKey(i)) {
                int requestedSize = Text.this.fixedColumns.get(i);
                if (requestedSize == 0) {
                  // calculate size based on requested actual size as a
                  // proportion of the total.
                  requestedSize = ((actualSize * available) / expandableColumnSize);
                  this.columns.set(i, requestedSize);
                }
              }
            }
          }
        }
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Creates a new text table printer for the specified output stream.
     ** <br>
     ** The text table printer will have the following initial settings:
     ** <ul>
     **   <li>headings will be displayed
     **   <li>":" separators between columns
     **   <li>columns are padded by one character
     ** </ul>
     **
     ** @param  stream           the stream to output tables to.
     */
    public Text(final OutputStream stream) {
      // ensure inheritance
      this(new BufferedWriter(new OutputStreamWriter(stream)));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Creates a new text table printer for the specified writer.
     ** <br>
     ** The tabbed table printer will have the following initial settings:
     ** <ul>
     **   <li>headings will be displayed
     **   <li>no separators between columns
     **   <li>columns are padded by one character
     ** </ul>
     **
     ** @param  writer         the writer to output tables to.
     */
    public Text(final Writer writer) {
      // ensure inheritance
      super(writer);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   padding
    /**
     ** Sets the padding which will be used to separate a cell's contents from
     ** its adjacent column separators.
     **
     ** @param  padding          the padding.
     **
     ** @return                  this instance for method chaining purpose.
     */
    public Text padding(int padding) {
      this.padding = padding;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   totalWidth
    /**
     ** Sets the total permitted width for the table which expandable columns
     ** can use up.
     **
     ** @param  totalWidth       the total width.
     **
     ** @return                  this instance for method chaining purpose.
     */
    public Text totalWidth(int totalWidth) {
      this.totalWidth = totalWidth;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   indent
    /**
     ** Sets the amount of characters that the table should be indented.
     ** <br>
     ** By default the table is not indented.
     **
     ** @param  indent           the number of characters the table should be
     **                          indented.
     **
     ** @return                  this instance for method chaining purpose.
     **
     ** @throws IllegalArgumentException if indent is less than <code>0</code>.
     */
    public Text indent(int indent)
      throws IllegalArgumentException {

      // prevent bogus input
      if (indent < 0)
        throw new IllegalArgumentException("Negative indentation width " + indent);

      this.indent = indent;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: headingSeparator
    /**
     ** Sets the heading separator which should be used to separate the table
     ** heading row from the rows beneath.
     **
     ** @param  separator        the heading separator to set.
     **
     ** @return                  this instance for method chaining purpose.
     */
    public Text headingSeparator(final char separator) {
      this.headingSeparator = separator;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   headingSeparatorStartColumn
    /**
     ** Sets the heading separator start column.
     ** <br>
     ** The heading separator will only be display in the specified column and
     ** all subsequent columns. Usually this should be left at zero (the
     ** default) but sometimes it useful to indent the heading separate in order
     ** to provide additional emphasis (for example in menus).
     **
     ** @param  column           the heading separator start column.
     **
     ** @return                  this instance for method chaining purpose.
     */
    public Text headingSeparatorStartColumn(final int column) {
      // prevent bogus input
      if (column < 0)
        throw new IllegalArgumentException("Negative start column " + column);

      this.headingSeparatorStartColumn = column;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: columnSeparator
    /**
     ** Sets the column separator which should be used to separate one column
     ** from the next (not including padding).
     **
     ** @param  separator        the column separator to set.
     **
     ** @return                  this instance for method chaining purpose.
     */
    public Text columnSeparator(final String separator) {
      this.columnSeparator = separator;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: columnWidth
    /**
     ** Set the maximum width for a column.
     ** <br>
     ** If a cell is too big to fit in its column then it will be wrapped.
     **
     ** @param  column           the column to make fixed width (<code>0</code>
     **                          is the first column).
     ** @param  width            the width of the column (this should not
     **                          include column separators or padding), or
     **                          <code>0</code> to indicate that this column
     **                          should be expandable.
     **
     ** @return                  this instance for method chaining purpose.
     **
     ** @throws IllegalArgumentException if column is less than <code>0</code>.
     */
    public Text columnWidth(final int column, final int width)
      throws IllegalArgumentException {

      // prevent bogus input
      if (column < 0)
        throw new IllegalArgumentException("Negative column " + column);

      // prevent bogus input
      if (width < 0)
        throw new IllegalArgumentException("Negative width " + width);

      this.fixedColumns.put(column, width);
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: serializer (TableFormatter.Printer)
    /**
     ** Creates a new table serializer based on the configuration of this printer.
     **
     ** @return                  a new table serializer based on the
     **                          configuration of this printer.
     */
    @Override
    public final TableFormatter.Serializer serializer() {
      return new Serializer();
    }
  }

  ////////////////////////////////////////////////////////////////////////////
  // final class Tabbed
  // ~~~~~ ~~~~~ ~~~~~~

  /**
   ** An implementation for creating a text based table. Tables have
   ** configurable column widths, padding, and column separators.
   */
  public static final class Tabbed extends Printer {
    //////////////////////////////////////////////////////////////////////////////
    // Member classes
    //////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // class TabbedSerializer
    // ~~~~~ ~~~~~~~~~~~~~~~~
    /**
     ** Tabbed Table serializer implementation.
     */
    private final class Serializer extends TableFormatter.Serializer {

      //////////////////////////////////////////////////////////////////////////
      // instance attributes
      //////////////////////////////////////////////////////////////////////////

      // the current column being output
      private int column    = 0;

      // counts the number of separators that should be output the next time a
      // non-empty cell is displayed. The tab separators are not displayed
      // immediately so that we can avoid displaying unnecessary trailing
      // separators.
      private int separator = 0;

      //////////////////////////////////////////////////////////////////////////
      // Constructors
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: Ctor
      private Serializer() {
        // ensure inheritance
        super();
      }

      //////////////////////////////////////////////////////////////////////////
      // Methods grouped by functionality
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: header (overriden)
      /**
       ** Prints a column heading.
       **
       ** @param  s              the column heading contents.
       */
      @Override
      public void header(final String s) {
        if (Tabbed.this.heading) {
          column(s);
        }
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: column (overriden)
      /**
       ** Prints a table column.
       **
       ** @param  s              the column contents.
       */
      @Override
      public void column(final String s) {
        // avoid printing tab separators for trailing empty cells
        if (s.length() == 0) {
          this.separator++;
        }
        else {
          for (int i = 0; i < this.separator; i++) {
            Tabbed.this.writer.print('\t');
          }
          this.separator = 1;
        }

        // replace all new-lines and tabs with a single space
        Tabbed.this.writer.print(s.replaceAll("[\\t\\n\\r]", " "));
        this.column++;
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: tableEnd (overriden)
      /**
       ** Finish printing the table.
       */
      @Override
      public void tableEnd() {
        Tabbed.this.writer.flush();
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: headerStart (overriden)
      /**
       ** Prepare to start printing the column headings.
       */
      @Override
      public void headerStart() {
        this.column    = 0;
        this.separator = 0;
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: headerEnd (overriden)
      /**
       ** Finish printing the column headings.
       */
      @Override
      public void headerEnd() {
        if (Tabbed.this.heading)
          Tabbed.this.writer.println();
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: rowStart (overriden)
      /**
       ** Prepare to start printing a new row of the table.
       */
      @Override
      public void rowStart() {
        this.column    = 0;
        this.separator = 0;
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: rowEnd (overriden)
      /**
       ** Finish printing the current row of the table.
       */
      @Override
      public void rowEnd() {
        Tabbed.this.writer.println();
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Creates a new tabbed table printer for the specified output stream.
     ** <br>
     ** The tabbed table printer will have the following initial settings:
     ** <ul>
     **   <li>headings will be displayed
     **   <li>no separators between columns
     **   <li>columns are padded by one character
     ** </ul>
     **
     ** @param  stream           the stream to output tables to.
     */
    public Tabbed(final OutputStream stream) {
      // ensure inheritance
      this(new BufferedWriter(new OutputStreamWriter(stream)));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Creates a new tabbed table printer for the specified writer.
     ** <br>
     ** The tabbed table printer will have the following initial settings:
     ** <ul>
     **   <li>headings will be displayed
     **   <li>no separators between columns
     **   <li>columns are padded by one character
     ** </ul>
     **
     ** @param  writer           the writer to output tables to.
     */
    public Tabbed(final Writer writer) {
      // ensure inheritance
      super(writer);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   serializer (TableFormatter.Printer)
    /**
     ** Creates a new table serializer based on the configuration of this
     ** printer.
     **
     ** @return                  a new table serializer based on the
     **                          configuration of this printer.
     */
    @Override
    public final TableFormatter.Serializer serializer() {
      return new Serializer();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TableFormatter</code> handler that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TableFormatter() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   header
  /**
   ** Appends a new blank column heading to the header row.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public TableFormatter header() {
    return header("");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   header
  /**
   ** Appends a new column heading to the header row.
   **
   ** @param  value              the column heading value to append.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public TableFormatter header(final String value) {
    this.header.add(value);

    // update statistics.
    if (this.header.size() > this.width) {
      this.width = this.header.size();
      this.columns.add(value.length());
    }
    else if (this.columns.get(this.header.size() - 1) < value.length()) {
      this.columns.set(this.header.size() - 1, value.length());
    }
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   column
  /**
   ** Appends a new blank column to the current row.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public TableFormatter column() {
    return column(SystemConstant.EMPTY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   column
  /**
   ** Appends a new column to the current row containing the provided boolean
   ** value.
   **
   ** @param  value              the boolean value to append.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public TableFormatter column(boolean value) {
    return column(String.valueOf(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   column
  /**
   ** Appends a new column to the current row containing the provided byte
   ** value.
   **
   ** @param  value              the byte value to append.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public TableFormatter column(final byte value) {
    return column(String.valueOf(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   column
  /**
   ** Appends a new column to the current row containing the provided char
   ** value.
   **
   ** @param  value              the char value to append.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public TableFormatter column(final char value) {
    return column(String.valueOf(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   column
  /**
   ** Appends a new column to the current row containing the provided int value.
   **
   ** @param  value              the int value to append.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public TableFormatter column(final int value) {
    return column(String.valueOf(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   column
  /**
   ** Appends a new column to the current row containing the provided long
   ** value.
   **
   ** @param  value              the long value to append.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public TableFormatter column(final long value) {
    return column(String.valueOf(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   column
  /**
   ** Appends a new column to the current row containing the provided double
   ** value.
   **
   ** @param  value              the double value to append.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public TableFormatter column(final double value) {
    return column(String.valueOf(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   column
  /**
   ** Appends a new column to the current row containing the provided float
   ** value.
   **
   ** @param  value              the float value to append.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public TableFormatter column(final float value) {
    return column(String.valueOf(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   column
  /**
   ** Appends a new column to the current row containing the provided object
   ** value.
   **
   ** @param  value              the float value to append.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public TableFormatter column(final Object value) {
    // make sure that the first row has been created.
    if (this.height == 0) {
      row();
    }

    // Create the cell.
    String s = String.valueOf(value);
    this.rows.get(this.height - 1).add(s);
    this.column++;

    // Update statistics.
    if (this.column > this.width) {
      this.width = this.column;
      this.columns.add(s.length());
    }
    else if (this.columns.get(this.column - 1) < s.length()) {
      this.columns.set(this.column - 1, s.length());
    }
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   row
  /**
   ** Appends a new row to the table.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public TableFormatter row() {
    this.rows.add(new ArrayList<String>());
    this.height++;
    this.column = 0;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   string
  /**
   ** Prints the table in its current state using the provided table printer.
   **
   ** @return                    the printed table in its current state.
   */
  public String string() {
    final StringWriter writer = new StringWriter();
    print(new Text(writer).columnSeparator(":"));
    return writer.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   print
  /**
   ** Prints the table in its current state using the provided table printer.
   **
   ** @param  builder            the table printer.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public TableFormatter print(final StringBuilder builder) {
    final StringWriter writer = new StringWriter();
    print(new Text(writer).columnSeparator(":"));
    builder.append(writer.toString());
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   print
  /**
   ** Prints the table in its current state using the provided table printer.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public TableFormatter print() {
    return print(new Text(System.out).columnSeparator(":"));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   print
  /**
   ** Prints the table in its current state using the provided table printer.
   **
   ** @param  printer            the table printer.
   **
   ** @return                    this instance for method chaining purpose.
   */
  public TableFormatter print(final Printer printer) {
    // create a new printer instance
    final Serializer serializer = printer.serializer();
    // output the table
    serializer.tableStart(this.height, this.width);
    for (int i = 0; i < this.width; i++) {
      serializer.column(this.columns.get(i));
    }

    // column headings
    serializer.headerStart();
    for (String s : this.header) {
      serializer.header(s.toString());
    }
    serializer.headerEnd();

    // table contents
    serializer.contentStart();
    for (List<String> row : this.rows) {
      serializer.rowStart();

      // print each cell in the row, padding missing trailing cells
      for (int i = 0; i < this.width; i++) {
        if (i < row.size()) {
          serializer.column(row.get(i));
        } else {
          serializer.column(SystemConstant.EMPTY);
        }
      }
      serializer.rowEnd();
    }
    serializer.contentEnd();
    serializer.tableEnd();
    return this;
  }
}