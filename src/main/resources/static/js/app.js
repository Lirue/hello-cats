var app = angular.module('helloCats', ['ngRoute','ngResource', 'ui.bootstrap', 'xeditable']);
app.config(function($routeProvider){
    $routeProvider
        .when('/cats',{
            controller: 'CatsController',
            templateUrl: 'cat/cats.html'
        })
        .otherwise(
            { redirectTo: '/'}
        );
});
app.run(function(editableOptions, editableThemes) {
    editableThemes.bs3.inputClass = 'input-sm';
    editableOptions.theme = 'bs3';
});
app.run(function($rootScope, $location) {
    $rootScope.isActive = function(viewLocation) {
        return viewLocation === $location.path();
    };
});