(function(window, angular, undefined) {
    "use strict";
    var app = angular.module('Search', [
        'SearchServer'
    ])

    app.controller('SearchCtrl', ['$scope', '$location', 'ServerFuncs', 'ServerParams', '$log', '$window', '$filter', function($scope, $location, ServerFuncs, ServerParams, $log, $window, $filter)  {
        $scope.items = [];
        $scope.count = 0;
        $scope.searchTmp = ServerParams.search;
        $scope.searchText = ServerParams.search;
        $scope.host = ServerParams.host;

        $scope.findIndex = function(id, array){
            for(var i = 0; i < array.length; i += 1) {
                if(array[i]['id'] === id) {
                    return i;
                }
            }
            return -1;
        };

        $scope.setPage = function(page) {
            $scope.page = page;
            $scope.fetchItems(1);
        };

        $scope.search = function() {
            $scope.searchText = $scope.searchTmp;
            $scope.setPage(1);
        };

        $scope.fetchItems = function(page) {
            $scope.currentPage = page;
            ServerFuncs.fetchItems({
                page: $scope.currentPage, limit: $scope.limit, search: $scope.searchText
            });
        };

        $scope.$watch('page', function(page, old) {
            if (page === old) return;
            $scope.fetchItems(page);
        });

        $scope.$watch('', function (value) {
            $scope.total = value;
        });

        $scope.$on('after-fetch-items', function(event, data) {
            $scope.$apply(function() {
                $scope.count = data.count;
                $scope.items = data.items;
            });
        });

        // init
        var page = parseInt($location.search().page, $scope.count) || 1;
        $scope.fetchItems(page);
    }])
})(this, angular);
