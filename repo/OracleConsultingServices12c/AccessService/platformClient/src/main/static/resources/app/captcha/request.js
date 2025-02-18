define(
  ["require"]
, function(require) {
    return {
      css: function () {
        return require.toUrl("css/style.css");
      }
    , generate: function () {
        return require.toUrl("/ocs/v1/captcha/generate");
      }
    , validate: function () {
        return require.toUrl("/ocs/v1/captcha/verify");
      }
    };
  }
);