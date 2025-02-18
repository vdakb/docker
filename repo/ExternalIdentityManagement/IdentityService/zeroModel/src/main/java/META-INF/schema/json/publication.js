var publication_Module_Factory = function () {
  var publication = {
    name: 'publication',
    typeInfos: [{
        localName: 'Publication',
        typeName: {
          namespaceURI: 'http:\/\/www.oracle.com\/schema\/igs',
          localPart: 'publication'
        },
        propertyInfos: [{
            name: 'id',
            typeInfo: 'Token',
            attributeName: {
              localPart: 'id'
            },
            type: 'attribute'
          }, {
            name: 'hierarchy',
            typeInfo: 'Boolean',
            attributeName: {
              localPart: 'hierarchy'
            },
            type: 'attribute'
          }, {
            name: 'action',
            values: ['assign', 'revoke'],
            attributeName: {
              localPart: 'action'
            },
            type: 'attribute'
          }]
      }, {
        type: 'enumInfo',
        localName: 'Encode',
        baseTypeInfo: 'Token',
        values: ['none', 'base16', 'base64']
      }, {
        type: 'enumInfo',
        localName: 'Risk',
        baseTypeInfo: 'Token',
        values: ['none', 'low', 'medium', 'high']
      }],
    elementInfos: []
  };
  return {
    publication: publication
  };
};
if (typeof define === 'function' && define.amd) {
  define([], publication_Module_Factory);
}
else {
  var publication_Module = publication_Module_Factory();
  if (typeof module !== 'undefined' && module.exports) {
    module.exports.publication = publication_Module.publication;
  }
  else {
    var publication = publication_Module.publication;
  }
}