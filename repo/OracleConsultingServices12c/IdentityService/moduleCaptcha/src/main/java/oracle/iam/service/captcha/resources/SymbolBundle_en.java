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

    Copyright © 2020. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Service Extension
    Subsystem   :   Captcha Service Feature

    File        :   SymbolBundle_en.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    SymbolBundle_en.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.service.captcha.resources;

import oracle.hst.platform.core.utility.ListResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class SymbolBundle_en
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** Java Resource Bundle for national language support.
 ** <p>
 ** This declares the translated objects by resource keys for message output.
 ** <br>
 ** Used for translation resources to
 ** <ul>
 **   <li>language code english
 **   <li>country  code common
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class SymbolBundle_en extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String CONTENT[][] = {
    {"init.image.config",             "Unable to initialize Service. Failed to find configuration %1$s." }
  , {"init.image.failed",             "Unable to initialize Service. Failed to load image resources." }

  , {"request.action.invalid",        "Invalid action %1$s in request." }

  , {"start.option.invalid",          "Invalid value for number of options to display: %1$d. The default value %2$s is used." }

  , {"stream.index.invalid",          "Invalid image index specified in request: %1$d" }
  , {"stream.request.invalid",        "Index %1$d not valid for requested image." }
  , {"stream.source.invalid",         "Could not find image %1$s." }

  , {"captcha.challenge.text",        "Click or touch the symbol %1$s." }
  , {"captcha.challenge.success",     "Image was valid." }
  , {"captcha.challenge.failed",      "Image was NOT valid." }

  , {"captcha.image.airplane",        "Airplan" }
  , {"captcha.image.balloons",        "Baloons" }
  , {"captcha.image.camera",          "Camera" }
  , {"captcha.image.car",             "Car" }
  , {"captcha.image.cat",             "Cat" }
  , {"captcha.image.chair",           "Chair" }
  , {"captcha.image.clip",            "Clip" }
  , {"captcha.image.clock",           "Clock" }
  , {"captcha.image.cloud",           "Cloud" }
  , {"captcha.image.computer",        "Computer" }
  , {"captcha.image.envelope",        "Envelope" }
  , {"captcha.image.eye",             "Eye" }
  , {"captcha.image.flag",            "Flag" }
  , {"captcha.image.folder",          "Folder" }
  , {"captcha.image.foot",            "Foot" }
  , {"captcha.image.graph",           "Graph" }
  , {"captcha.image.house",           "House" }
  , {"captcha.image.key",             "Key" }
  , {"captcha.image.leaf",            "Leaf" }
  , {"captcha.image.lightbulb",       "Light Bulb" }
  , {"captcha.image.lock",            "Lock" }
  , {"captcha.image.magnifyingglass", "Magnifying Glass" }
  , {"captcha.image.man",             "Man" }
  , {"captcha.image.musicnote",       "Music Note" }
  , {"captcha.image.pants",           "Pants" }
  , {"captcha.image.pencil",          "Pencil" }
  , {"captcha.image.printer",         "Printer" }
  , {"captcha.image.robot",           "Robot" }
  , {"captcha.image.scissors",        "Scissors" }
  , {"captcha.image.sunglasses",      "Sunglasses" }
  , {"captcha.image.tag",             "Tag" }
  , {"captcha.image.tree",            "Tree" }
  , {"captcha.image.truck",           "Truck" }
  , {"captcha.image.tshirt",          "T-Shirt" }
  , {"captcha.image.umbrella",        "Umbrella" }
  , {"captcha.image.woman",           "Woman" }
  , {"captcha.image.world",           "World" }
  };

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getContents (ListResourceBundle)
  /**
   ** Returns an array, where each item in the array is a pair of objects.
   ** <br>
   ** The first element of each pair is the key, which must be a
   ** <code>String</code>, and the second element is the value associated with
   ** that key.
   **
   ** @return                    an array, where each item in the array is a
   **                            pair of objects.
   */
  public Object[][] getContents() {
    return CONTENT;
  }
}