/**!
 ** formValidation.js
 **
 ** FormValidation (http://formvalidation.io)
 ** The best jQuery plugin to validate form fields. Support Bootstrap,
 ** Foundation, Pure, SemanticUI, UIKit and custom frameworks
 **
 ** @version     v0.6.2-dev, built on 2015-03-13 8:15:45 AM
 ** @author      https://twitter.com/nghuuphuoc
 ** @copyright   (c) 2013 - 2015 Nguyen Huu Phuoc
 ** @license     http://formvalidation.io/license/
 */
if (typeof jQuery === 'undefined') {
  throw new Error('Validation framework requires jQuery');
}

(function($) {
  var version = $.fn.jquery.split(' ')[0].split('.');
  if ((+version[0] < 3 && +version[1] < 4) || (+version[0] === 3 && +version[1] === 4 && +version[2] < 1)) {
    throw new Error('Validation framework requires jQuery version 3.4.1 or higher');
}
}(jQuery));

window.Form = {
  // add-ons
  addon:     {}
  // supported frameworks
, framework: {}
  // i18n
, i18n:      {}
  // available validators
, validator: {}
};

(function($) {
  Form.Base = function(form, options, namespace) {
    this.$form      = $(form);
    this.options    = $.extend({}, $.fn.formValidation.DEFAULT_OPTIONS, options);
    this._namespace = namespace || 'fv';

    // array of invalid fields
    this.$invalid = $([]);
    // the submit button which is clicked to submit form
    this.$submit  = null;
    this.$hidden  = null;

    this._init();
  };
  Form.Base.prototype = {
    constructor: Form.Base
    /**
     ** Inline initialization.
     */
  , _init: function() {
    }
    /**
     ** Whether the number of characters of field value exceed the threshold.
     **
     ** @param  $field          The field element.
     **                         Allowed object is {jQuery}.
     **
     ** @returns                true if the characters of the field{Boolean}
     */
  , _exceed: function() {
    }
  }
}(jQuery));