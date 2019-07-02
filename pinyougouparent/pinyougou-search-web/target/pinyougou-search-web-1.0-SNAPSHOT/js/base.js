<!--引入模块-->
//pagination是引入的angularjs插件中的pagination.js中的模块名
//pinyougou是当前模块
var app = angular.module("pinyougou", []);

//设置过滤器
app.filter('trustHtml',['$sce',function ($sce) {
    return function (data) {//data传入de要过滤的内容
        return $sce.trustAsHtml(data);//返回过滤后的内容
    }

}]);