app.controller('contentController', function ($scope, contentService) {

    //################################################################
    $scope.contentList = [];//广告列表
    $scope.findByCategoryId = function (categoryId) {
        contentService.findByCategoryId(categoryId).success(
            function (response) {
                $scope.contentList[categoryId] = response;

            });
    }


    //################################################################
    //首页跳转搜搜页，并完成自动搜索//传递参数
    $scope.search = function () {
        location.href ='http://localhost:9104/search.html#?keywords='+$scope.keywords;
    }

});