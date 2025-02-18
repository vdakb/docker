// configure loading modules from the lib directory, except for 'app' ones,
// which are in a sibling directory.
require.config(
  { baseUrl: 'resources/lib'
  , paths: {
      // the left side is the module ID, the right side is the path to the
      // jQuery file, relative to baseUrl.
      // Also, the path should NOT include the '.js' file extension.
      // This example is using jQuery 3.4.1 located at lib/jquery.min.js,
      // relative to the HTML page.
      jquery:    'jquery.min'
    , popper:    'popper.min'
    , bootstrap: 'bootstrap.min'
	    // app path is relative to baseUrl
    , app: '../app'
    }
    // only use shim config for non-AMD scripts, scripts that do not already
    // call define().
    // The shim config will not work correctly if used on AMD scripts, in
    // particular, the exports and init config will not be triggered, and the
    // dependencies (deps) config will be confusing for those cases.
  , shim: {
      // these script dependencies should be loaded before loading bootstrap.js
      'bootstrap': {
        deps: ['jquery','popper']
        // once loaded, use the global 'Backbone' as the module value.
      , exports: 'Bootstrap'
      }
    }    
  , map: {
      // Bootsrap is hardcoded referencing popper.js and lookup this file in the
      // root of the web application
      // it is inappropriate to change the source code hence we advising
      // requirejs to delegate the request
      '*': {
        'popper.js': 'popper'
      }
    }
  , config: {
      // set the config for the i18n plugin
      i18n: {
        locale: 'fr'
      }
    }
  }
);
// start loading the main app file
// put all the application logic in there
//requirejs(['app/main']);
require(['app/view'], function($){});
