angular.module('job4j').directive('header', [function () {

    return {
        link: function ($scope) {
            $scope.log_url = CONTEXT;
        },
        templateUrl: CONTEXT + 'templates/header.html?v=19.10.2018.1'
    }
}]);