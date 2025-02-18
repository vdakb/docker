var core_Module_Factory = function () {
  var core = {
    name: 'core',
    typeInfos: [{
        type: 'enumInfo',
        localName: 'Risk',
        baseTypeInfo: 'Token',
        values: ['none', 'low', 'medium', 'high']
      }, {
        type: 'enumInfo',
        localName: 'Encode',
        baseTypeInfo: 'Token',
        values: ['none', 'base16', 'base64']
      }],
    elementInfos: []
  };
  return {
    core: core
  };
};
if (typeof define === 'function' && define.amd) {
  define([], core_Module_Factory);
}
else {
  var core_Module = core_Module_Factory();
  if (typeof module !== 'undefined' && module.exports) {
    module.exports.core = core_Module.core;
  }
  else {
    var core = core_Module.core;
  }
}