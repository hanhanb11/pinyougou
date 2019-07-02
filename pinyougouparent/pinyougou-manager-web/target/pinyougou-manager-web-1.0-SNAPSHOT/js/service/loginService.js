//一 、获取登陆用户姓名
app.service("loginService", function ($http) {
    //1..查询品牌列表
    this.showName = function () {
        return $http.get("../login/name.do");
    };

});