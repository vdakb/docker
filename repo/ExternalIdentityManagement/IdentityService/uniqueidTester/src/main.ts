import {createApp}         from 'vue';

/* import font awesome icon component */
import {FontAwesomeIcon}   from '@fortawesome/vue-fontawesome';
/* import specific styles */
import { fas }             from '@fortawesome/free-solid-svg-icons'
import { fab }             from '@fortawesome/free-brands-svg-icons';
import { far }             from '@fortawesome/free-regular-svg-icons';
/* import specific icons */
import {faHome}            from '@fortawesome/free-solid-svg-icons';
import {faFlag}            from '@fortawesome/free-solid-svg-icons';
import {faNavicon}         from '@fortawesome/free-solid-svg-icons';
import {faPowerOff}        from '@fortawesome/free-solid-svg-icons';
import {faHandshake}       from '@fortawesome/free-solid-svg-icons';
import {faHandPointUp}     from '@fortawesome/free-solid-svg-icons';
import {faEarthEurope}     from '@fortawesome/free-solid-svg-icons';
/* import the fontawesome core */
import {dom, library}      from '@fortawesome/fontawesome-svg-core';
/* add icons to the library */
library.add(fas, fab, far, faEarthEurope, faFlag, faHandPointUp, faHandshake, faHome, faNavicon, faPowerOff);
dom.watch();

import App                 from './app.vue'; 
import router              from './router';
import i18n                from './locale';

import oidc                from './authn';
oidc.startup();
/*

.then(
  ok => {
    if (ok) {
      const app = createApp(App)
      .use(i18n)
      .use(router)
      //make font awesome available
      .component('font-awesome-icon', FontAwesomeIcon);
      app.config.globalProperties.$oidc = authenticator;
      app.mount('#app')
    }
    else {
      console.log('Startup was not failed')
    }
  }
)
*/
createApp(App)
  //install i18n instance to make the whole app i18n-aware
  .use(i18n)
  .use(router)
  //make font awesome available
  .component('font-awesome-icon', FontAwesomeIcon)
  .mount('#app')
;