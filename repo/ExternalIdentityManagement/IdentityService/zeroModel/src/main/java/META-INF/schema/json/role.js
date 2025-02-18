var role_Module_Factory = function () {
  var role = {
    name: 'role',
    defaultElementNamespaceURI: 'http:\/\/www.oracle.com\/schema\/igs',
    typeInfos: [{
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
        localName: 'Role',
        typeName: 'role',
        baseTypeInfo: '.Entity',
        propertyInfos: [{
            name: 'identities',
            minOccurs: 0,
            collection: true,
            typeInfo: '.Member'
          }, {
            name: 'organizations',
            minOccurs: 0,
            collection: true,
            typeInfo: '.Publication'
          }]
      }, {
        localName: 'Member',
        typeName: 'member',
        propertyInfos: [{
            name: 'id',
            typeInfo: 'Token',
            attributeName: {
              localPart: 'id'
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
        localName: 'Publication',
        typeName: 'publication',
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
    role: role
  };
};
if (typeof define === 'function' && define.amd) {
  define([], role_Module_Factory);
}
else {
  var role_Module = role_Module_Factory();
  if (typeof module !== 'undefined' && module.exports) {
    module.exports.role = role_Module.role;
  }
  else {
    var role = role_Module.role;
  }
}