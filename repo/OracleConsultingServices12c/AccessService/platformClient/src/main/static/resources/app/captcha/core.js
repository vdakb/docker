define(
  function() {
    'use strict';
    var _refresh = function(config) {
      var core     = this;
      var endpoint = this;
      // loading state
      config.applyRandomNonce();
      config.loading = true;
      // URL must invoked after nonce is applied
      endpoint = _endpoint(config);
      config.load(core);
    };
    _endpoint = function(config) {
      var url = "http://localhost:8080/captcha-ws" + config.routes.start + "/" + config.numberOfImages;
      return _addUrlParams( config, url );
    };
    return function(config) {
      refresh = function() {
        return _refresh.call(this, config);
      };
      loading = function() {
        return config.loading;
      };
      loaded = function() {
        return config.loaded;
      };
      core = {
        refresh: refresh
      , loading: loading
      , loaded: loaded
      }
      return core;
    }
  }
);