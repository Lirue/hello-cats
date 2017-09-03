(function () {
    'use-strict';

    angular.module('helloCats')
        .controller('CatsController', CatsController);

    CatsController.$inject = ['$scope', "Cats"];

    function CatsController($scope, Cats) {
        $scope.cat = {};
        $scope.genderTypes = ['Female', 'Male'];
        $scope.successMessage = '';
        $scope.errorMessage = '';
        $scope.getAllCats = getAllCats();
        $scope.submit = submit;
        $scope.resetForm = resetForm;
        $scope.updateCat=updateCat;
        $scope.deleteCat = deleteCat;
        $scope.capitalize = capitalize;
        $scope.checkFirstName = checkFirstName;
        $scope.checkLastName = checkLastName;
        $scope.checkAge = checkAge;
        $scope.onlyIntegers = /^\d+$/;

        function getAllCats() {
            Cats.list().$promise.then(
                function (result) {
                    $scope.cats = result;
                },
                function (reason) {
                    console.log(reason);
                });
        }

        function submit() {
            console.log('Submitting');
            console.log('Saving New Cat', $scope.cat);
            Cats.save({firstName : $scope.cat.firstname, lastName : $scope.cat.lastname, age : $scope.cat.age, gender : $scope.cat.gender.toUpperCase()}).$promise.then(
                function(result){
                    $scope.successMessage = 'Cat added successfully';
                    $scope.cat={};
                    $scope.myForm.$setPristine();
                    getAllCats();
                },
                function(reason){
                    console.log(reason);
                });
        }

        function resetForm(){
            $scope.cat={};

            $scope.myForm.$setPristine();
            $scope.myForm.$setUntouched();
        }

        function updateCat(id, cat){
            cat.id = id;
            cat.gender = cat.gender.toUpperCase();
            console.log('Updating a Cat with id ', id);
            console.log('Cat: ', cat);
            Cats.update(id, cat).$promise.then(
                function(result){
                    $scope.successMessage = 'Cat updated successfully';
                    getAllCats();
                },
                function(reason){
                    console.log(reason);
                });
        }

        function deleteCat(id) {
            console.log('Deleting a Cat with id', id);
            Cats.delete({id: id}).$promise.then(
                function(result){
                    $scope.successMessage = 'Cat deleted successfully';
                    getAllCats();
                },
                function(reason){
                    console.log(reason);
                });
        }

        function capitalize(string) {
            return string.charAt(0).toUpperCase() + string.slice(1).toLowerCase();
        }

        //VALIDATION
        //Manual verification since there is no validation tool for x-editable
        function checkFirstName(firstName){
            if (firstName===""){
                return "First name is required."
            } else if(firstName.length < 2){
                return "First name is too short."
            } else if(firstName.length > 25){
                return "First name is too long."
            }
        }

        function checkLastName(lastName){
            if (lastName===""){
                return "Last name is required."
            } else if(lastName.length < 2){
                return "First name is too short."
            } else if(lastName.length > 25){
                return "Last name is too long."
            }
        }

        function checkAge(age){
            if (age===""){
                return "Age is required."
            } else if(age < 0){
                return "Age must be higher than 0."
            } else if(age > 100){
                return "Age must be lower than 100."
            } else if(age % 1 !== 0){
                return "Age must be an integer"
            }
        }
    }
})();