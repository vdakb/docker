define(['jquery', 'bootstrap']
, function($, _bootstrap) {
    'use strict';
    $(document).ready(
      function() {
        $("#challenge-validate").click(
          function() {
            var bundle = require("i18n!app/nls/bundle")
            console.log(bundle);
            // "Resposta incorreta. Tente novamente!"
            alert(bundle.root.challenge.accepted);
          }
        )
        $("#challenge-refresh").click(
           function(e) {
             e.stopPropagation();
             $('#challenge-preview').attr('src', $('#challenge-preview').attr('src'));
             $("#captcha").modal('show');
          }
        )
      }
    );
  }
);