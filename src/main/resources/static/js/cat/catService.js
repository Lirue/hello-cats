(function () {
    'use-strict';

    angular.module('helloCats')
        .factory('Cats', Cats);

    Cats.$inject = ['$resource'];

    function Cats($resource){
        return $resource('cats', {}, {
            delete: {
                method: 'DELETE',
                url: 'cats/:id',
                params: {id: '@id'}
            },
            list: {
                method: 'GET',
                isArray: true
            },
            save: {
                method: 'POST'
            },
            update: {
                method: 'PUT',
                url: 'cats/:id',
                params: {id: '@id'}
            }
        });

    }
})();