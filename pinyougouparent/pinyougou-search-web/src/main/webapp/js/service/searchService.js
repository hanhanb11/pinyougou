app.service('searchService',function ($http) {
   // 查询

    this.searchItem=function (searchMap) {
        return $http.post('itemSearch/search.do',searchMap);
    }


});