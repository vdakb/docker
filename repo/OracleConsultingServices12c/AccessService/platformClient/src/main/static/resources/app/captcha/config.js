define(
  function() {
    var config = {
      routes: {
        generate: '/ocs/v1/captcha/generate'
      , validate: '/ocs/v1/captcha/verify'
      }
    , loading: false
    , loaded: false
    , nonce: ''
    , load: function() {}
//    , loaded: function() {}
    };
    // update and return the random nonce
    config.applyRandomNonce = function() {
      return (config.nonce = Math.random().toString(36).substring(2));
    };
    return config;
  }
);