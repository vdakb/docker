var inventory = angular.module(
  'inventoryModule'
, []
).config(
  [ '$httpProvider'
    , function($httpProvider) {
        $httpProvider.defaults.withCredentials = true;
    }
  ]
);
inventory.controller(
  'inventoryController'
, function ($scope, $http){
    /*
    $http.get('https://icd.cinnamonstar.dev/inventory-service/resources/inventory/parts').success(
    $http.get('http://icd.cinnamonstar.dev:8008/inventory-service/resources/inventory/parts').success(
	  */
    $http.get('http://ipd.cinnamonstar.net:1080/inventory-service/resources/inventory/parts').then(
      function(response) {
		console.log('get', response)
        $scope.parts = response.data.result;
      }
    );
    $scope.orderPart = function(part) {
      var config = {params:{part:part}};
      console.log(config.params.part.uniqueid);
      $scope.submitResult = "Order successfully placed for part id " + config.params.part.uniqueid;
    };
  }
);