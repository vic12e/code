/**
 * Created by Nghi Tran on 4/22/2015.
 */
controllers.controller('userController', ['$scope', '$http', function($scope, $http){
    // Models (i.e. data)
    $scope.users = [];
    $scope.newUser = {};
    //to use change password
    $scope.resetUser = {};

    load();
    // Use $http service to load the data
    function load() {
        $http.get('userList').success(function(dataJSON){

            $scope.users = dataJSON;
            $scope.teams = dataJSON[0].teams;

            _setIndexes();
        });
    }

    $scope.addUser = function() {

        // Note: $scope.newUser is set through two-way data binding with
        // the new user form in the view
        var newUser = $scope.newUser;
        newUser.id = 0;
        newUser.state = "normal";
        newUser.index = $scope.users.length;

        $http.post('addUser', newUser)
            .success(function(data, status, headers, config) {
                load();
        });
        $scope.newUser = {};
    };

    $scope.deleteUser = function(user) {
        if (user.state == "deleted") {
            $http.delete ('deleteUser/' + user.id)
                .success(function (response) {
                    load();
            })
        } else {
            user.state = "deleted";
        }
    };

    $scope.undoDelete = function(user) {
        user.state = "normal";
    };

    $scope.editUser = function(user) {
        user.state = "edit";
    };

    $scope.saveUser = function(user) {
        // Probably have some Ajax post or patch request
        $http.post('addUser', user)
            .success(function(data, status, headers, config) {
                load();
            });
        user.state = "normal";
    };

    $scope.cancelEdit = function(user) {
        load();
        user.state = "normal";
    };
    $scope.resetPassword = function() {
        if ($scope.resetUser.newPassword === $scope.resetUser.reNewPassword) {
            $scope.match = true;
            var resetUser = $scope.resetUser;
            $http.post('resetPassword', resetUser)
                .success(function (dataJSON, $location) {
                    $scope.redirect = dataJSON.redirect;
                    $scope.message = dataJSON.message;
                    $location.patch( $scope.redirect);
                    $scope.resetUser = {};
                })
        } else {
            $scope.match = false;
        }
    };

    // Internal Methods
    function _setIndexes() {
        $scope.users.forEach(function(user, index) {
            user.index = index;
        });
    }


}]);
