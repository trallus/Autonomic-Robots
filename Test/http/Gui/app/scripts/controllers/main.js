'use strict';
angular.module('surfaceApp')
        .controller('MainCtrl', function($scope) {
            //save all data
            $scope.customer = {};
            $scope.tests = [];

            //get login data
            $scope.data = function(user) {
                $scope.customer = angular.copy(user);

            };


            // get register data
            $scope.registerData = function(user) {
                if (user.password === user.confirmPassword && user.password !== 0 && user.confirmPassword !== 0) {
                    $scope.customer = angular.copy(user);
                    $scope.tests.push($scope.customer);

                }

            };
            // show the side you choose
            $scope.showSide = function(side) {
                $scope.setSide = side;
                $scope.form = side;
            };


        });
