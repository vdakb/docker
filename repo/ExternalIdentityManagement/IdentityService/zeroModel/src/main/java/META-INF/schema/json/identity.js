var identity_Module_Factory = function () {
  var identity = {
    name: 'identity',
    defaultElementNamespaceURI: 'http:\/\/www.oracle.com\/schema\/igs',
    typeInfos: [{
        localName: 'Account',
        typeName: 'account',
        baseTypeInfo: '.Entity',
        propertyInfos: [{
            name: 'entitlements',
            minOccurs: 0,
            collection: true,
            typeInfo: '.Account.Entitlements'
          }, {
            name: 'action',
            required: true,
            values: ['create', 'delete', 'modify', 'enable', 'disable'],
            attributeName: {
              localPart: 'action'
            },
            type: 'attribute'
          }]
      }, {
        localName: 'MemberOf',
        typeName: 'memberOf',
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
        localName: 'Identity',
        typeName: 'identity',
        baseTypeInfo: '.Entity',
        propertyInfos: [{
            name: 'roles',
            minOccurs: 0,
            collection: true,
            typeInfo: '.MemberOf'
          }, {
            name: 'applications',
            minOccurs: 0,
            collection: true,
            typeInfo: '.Application'
          }]
      }, {
        localName: 'Account.Entitlements',
        typeName: null,
        propertyInfos: [{
            name: 'actions',
            minOccurs: 0,
            collection: true,
            typeInfo: '.Entitlement'
          }, {
            name: 'namespace',
            required: true,
            typeInfo: 'Token',
            attributeName: {
              localPart: 'namespace'
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
        localName: 'Application',
        typeName: 'application',
        propertyInfos: [{
            name: 'accounts',
            minOccurs: 0,
            collection: true,
            typeInfo: '.Account'
          }, {
            name: 'application',
            required: true,
            typeInfo: 'Token',
            attributeName: {
              localPart: 'application'
            },
            type: 'attribute'
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
    identity: identity
  };
};
if (typeof define === 'function' && define.amd) {
  define([], identity_Module_Factory);
}
else {
  var identity_Module = identity_Module_Factory();
  if (typeof module !== 'undefined' && module.exports) {
    module.exports.identity = identity_Module.identity;
  }
  else {
    var identity = identity_Module.identity;
  }
}