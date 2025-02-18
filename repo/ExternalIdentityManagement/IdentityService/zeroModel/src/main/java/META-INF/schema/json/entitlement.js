var entitlement_Module_Factory = function () {
  var entitlement = {
    name: 'entitlement',
    defaultElementNamespaceURI: 'http:\/\/www.oracle.com\/schema\/igs',
    typeInfos: [{
        localName: 'Entitlement',
        typeName: 'entitlement',
        propertyInfos: [{
            name: 'attributes',
            minOccurs: 0,
            collection: true,
            typeInfo: '.Attribute'
          }, {
            name: 'action',
            required: true,
            values: ['assign', 'revoke', 'modify'],
            attributeName: {
              localPart: 'action'
            },
            type: 'attribute'
          }]
      }, {
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
    entitlement: entitlement
  };
};
if (typeof define === 'function' && define.amd) {
  define([], entitlement_Module_Factory);
}
else {
  var entitlement_Module = entitlement_Module_Factory();
  if (typeof module !== 'undefined' && module.exports) {
    module.exports.entitlement = entitlement_Module.entitlement;
  }
  else {
    var entitlement = entitlement_Module.entitlement;
  }
}