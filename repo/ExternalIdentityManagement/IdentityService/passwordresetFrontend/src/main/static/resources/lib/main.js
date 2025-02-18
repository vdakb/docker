(function () {
        'use strict';
        window.addEventListener(
            'load'
            , function () {
                // fetch all the forms we want to apply custom validation styles to
                const forms = document.getElementsByClassName('needs-validation');
                // loop over them and prevent submission
                const validation = Array.prototype.filter.call(
                    forms
                    , function (form) {
                        // jsf cannot accept attribute novalidate on form, so add it here
                        form.noValidate = true;
                        form.addEventListener(
                            'submit'
                            , function (event) {
                                if (form.checkValidity() === false) {
                                    event.preventDefault();
                                    event.stopPropagation();
                                }
                                form.classList.add('was-validated');
                            }
                            , false
                        );
                    }
                );

                // fetch all the required form fields
                const inputs = document.querySelectorAll('form input.mandatory');
                const requirements = Array.prototype.filter.call(
                    inputs
                    , function (input) {
                        // jsf cannot accept attribute required on input, so add it here
                        input.required = true
                    }
                );

                // tooltips initialization
                $('[data-toggle="tooltip"]').tooltip();


                // tooltip hint for form fields
                Array.from(document.getElementsByClassName('help-sign'))
                    .forEach(source => {
                       const id   = source.dataset.hint;
                       const item = document.getElementById(id);

                       source.addEventListener("mouseover", () => popup(item), false);
                       source.addEventListener("mouseout",  () => popup(item), false);

                       position(item, source);
                    });
            }
            , false
        );
        window.addEventListener(
            'resize',
            function () {
                // reevaluate tooltip hints position
                Array.from(document.getElementsByClassName('help-sign'))
                    .forEach(source => {
                        const id   = source.dataset.hint;
                        const item = document.getElementById(id);

                        position(item, source);
                    });
            }, false
        );
    }
)();

function popup(item) {
    item.classList.toggle('popup-visible');
}

function position(item, source) {
    const arrow          = item.querySelector('.arrow');
    const itemRect       = item.getBoundingClientRect();
    const arrowRect      = arrow.getBoundingClientRect();
    const sourceRect     = source.getBoundingClientRect()

    const x              = (sourceRect.x - itemRect.width / 2) + (sourceRect.width / 2);
    const y              = (sourceRect.y - itemRect.height) - arrowRect.height - 5;

    item.style.transform = 'translate3d('+ x +'px,' + y + 'px, 0px)';
    arrow.style.left     = itemRect.width / 2 - arrowRect.width / 2 + 'px';
}

function reloadImage(contextRoot){
    const date = new Date();
    $("#image-captcha").attr("src", contextRoot + "/captcha?" + date.getTime());
}

function reloadAudio(contextRoot){
    const date = new Date();
    $("#audio-captcha").attr("value", contextRoot + "/audio.wav?" + date.getTime());
}

function toggleCaptcha() {
    Array.from(document.getElementsByClassName('captcha-container'))
        .forEach(element => element.classList.toggle('captcha-hidden'));
}