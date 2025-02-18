var organization_Module_Factory = function () {
  var organization = {
    name: 'organization',
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
        localName: 'Organization',
        typeName: 'organization',
        baseTypeInfo: '.Entity'
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
    organization: organization
  };
};
if (typeof define === 'function' && define.amd) {
  define([], organization_Module_Factory);
}
else {
  var organization_Module = organization_Module_Factory();
  if (typeof module !== 'undefined' && module.exports) {
    module.exports.organization = organization_Module.organization;
  }
  else {
    var organization = organization_Module.organization;
  }
}