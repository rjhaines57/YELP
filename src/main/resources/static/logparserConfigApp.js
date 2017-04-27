/**
 * 
 */

// Define the `phonecatApp` module
var phonecatApp = angular.module('logparserConfigApp', ['ui.bootstrap']);

phonecatApp.controller('ModalInstanceCtrl', function ($uibModalInstance, items) {
    var vm = this;
    // I want to get the vehicle data pass to populate the form myModalContent.html
    vm.vehicle = items;
});



// Define the `PhoneListController` controller on the `phonecatApp` module
phonecatApp.controller("logparserConfigController", function PhoneListController($scope,$http,$uibModal) {
	
    $http.get('getconfig').then(function(response) {
                   $scope.config = response.data;
    });
    
    $scope.dropboxitemselected = function (item) {
    	
        alert("drop box item selected:"+item);
        item.eventName='wibblesnipsnip';
    }
    
    $scope.status = {
    	    isopen: false
    	  };

    	  $scope.toggled = function(open) {
    	    console.log('Dropdown is now: ', open);
    	  };

    	  $scope.toggleDropdown = function($event) {
    	    $event.preventDefault();
    	    $event.stopPropagation();
    	    $scope.status.isopen = !$scope.status.isopen;
    	  };
    	  
    	  
    	  $scope.openModal = function (event) {

              var modalInstance = $uibModal.open({
                  templateUrl: 'modals\editSimpleChecker.htm',
                  controller: 'ModalInstanceCtrl as VM',
                  size: 'lg',
                  resolve: {
                      items: function () {
                          return vm.vehicle;
                      }
                  }
              });
          };
    	  
});