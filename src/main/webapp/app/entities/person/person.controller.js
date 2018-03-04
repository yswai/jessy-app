(function() {
    'use strict';

    angular
        .module('somethingApp')
        .controller('PersonController', PersonController);

    PersonController.$inject = ['Person', 'ParseLinks', 'AlertService', 'paginationConstants'];

    function PersonController(Person, ParseLinks, AlertService, paginationConstants) {

        var vm = this;

        vm.people = [];
        vm.keyword = '';
        vm.loadPage = loadPage;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.page = 0;
        vm.links = {
            last: 0
        };
        vm.predicate = 'id';
        vm.reset = reset;
        vm.loadAll = loadAll;
        vm.keywordRefresh = keywordRefresh;
        vm.reverse = true;

        loadAll();

        function loadAll () {
            Person.query({
                page: vm.page,
                size: vm.itemsPerPage,
                sort: sort(),
                keyword: vm.keyword
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                for (var i = 0; i < data.length; i++) {
                    vm.people.push(data[i]);
                }
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function keywordRefresh () {
            vm.page = 0;
            Person.query({
                page: vm.page,
                size: vm.itemsPerPage,
                sort: sort(),
                keyword: vm.keyword
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }

            function onSuccess(data, headers) {
                vm.people = data;
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function reset () {
            vm.page = 0;
            vm.people = [];
            loadAll();
        }

        function loadPage(page) {
            vm.page = page;
            loadAll();
        }
    }
})();
