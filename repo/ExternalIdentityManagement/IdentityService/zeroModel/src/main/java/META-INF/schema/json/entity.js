var entity_Module_Factory = function () {
  var entity = {
    name: 'entity',
    defaultElementNamespaceURI: 'http:\/\/www.oracle.com\/schema\/igs',
    typeInfos: [{
        localName: 'Entity',
        typeName: 'entity',
        propertyInfos: [{
            name: 'attributes',
            minOccurs: 0,
            collection: true,
            typeInfo: '.Attribute'
          }, {
            name: 'id',
            required: true,
            typeInfo: 'Token',
            attributeName: {
              localPart: 'id'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'Attribute',
        typeName: 'attribute',
        propertyInfos: [{
            name: 'id',
            required: true,
            typeInfo: 'Token',
            attributeName: {
              localPart: 'id'
            },
            type: 'attribute'
          }, {
            name: 'enc',
            typeInfo: '.Encode',
            attributeName: {
              localPart: 'enc'
            },
            type: 'attribute'
          }]
      }, {
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
    entity: entity
  };
};
if (typeof define === 'function' && define.amd) {
  define([], entity_Module_Factory);
}
else {
  var entity_Module = entity_Module_Factory();
  if (typeof module !== 'undefined' && module.exports) {
    module.exports.entity = entity_Module.entity;
  }
  else {
    var entity = entity_Module.entity;
  }
}