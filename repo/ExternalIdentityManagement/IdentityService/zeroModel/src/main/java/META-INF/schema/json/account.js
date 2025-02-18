var account_Module_Factory = function () {
  var account = {
    name: 'account',
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
    account: account
  };
};
if (typeof define === 'function' && define.amd) {
  define([], account_Module_Factory);
}
else {
  var account_Module = account_Module_Factory();
  if (typeof module !== 'undefined' && module.exports) {
    module.exports.account = account_Module.account;
  }
  else {
    var account = account_Module.account;
  }
}