//二、引入控制器，传给页面数据
app.controller("indexController", function ($scope,  loginService) {

    $scope.showLoginName = function () {
        alert("...............")
        //请求地址。相当于ajax请求
        loginService.showName().success(
            function (respose) {
                $scope.loginName = respose.loginName;
            }
        );
    };


});