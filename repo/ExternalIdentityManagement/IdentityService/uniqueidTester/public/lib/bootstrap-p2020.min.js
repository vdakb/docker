var app = function() {
  // GLOBAL HELPERS
  // ======================
  this.is_touch_device = function() {
    return !!('ontouchstart' in window) || !!('onmsgesturechange' in window);
  };
};
var app = new app();
// APP UI SETTINGS
// ======================
app.ui = {
  // minimal scrolling to show scrollTop button
  scrollTop: 0
};
// hide sidebar on small screen
$(window).on('load resize scroll'
, function() {
    if ($(this).width() < 992) {
     $('body').addClass('sidebar-mini');
    }
  }
);
$(function() {
  // LAYOUT SETTINGS
  // ======================
  // user panel toggle action
  $(".user-panel").on("click",
    function() {
      $(".user-menu").slideToggle();
    }
  );
  // sidebar toggle action
  $('.js-sidebar-toggler').click(
    function() {
      $('body').toggleClass('sidebar-mini');
    }
  );
  // fixed navbar action
  $('#fixed-navbar').change(
    function() {
      if ($(this).is(':checked'))
        $('body').addClass('fixed-navbar');
      else
        $('body').removeClass('fixed-navbar');
    }
  );
  // fixed layout action
  $('#fixed-layout').change(
    function() {
      if ($(this).is(':checked')) {
        $('body').addClass('fixed-layout');
      }
      else {
        $('body').removeClass('fixed-layout');
      }
    }
  );
  // boxed layout action
  $("[name='layout-style']").change(
    function() {
      var value = +$(this).val();
      if (value)
        $('body').addClass('boxed-layout');
      else
        $('body').removeClass('boxed-layout');
    }
  );
});