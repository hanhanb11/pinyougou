app.controller("baseController", function ($scope) {
    //第2.步分页控件配置########分页开始#########
    $scope.paginationConf = {//当前页面默认值
        currentPage: 1,//当前页ma
        totalItems: 10,//总记录数
        itemsPerPage: 10,//每页记录数
        perPageOptions: [10, 20, 30, 40, 50, 100],//分页选项，可以选择每页显示多少条
        onChange: function () {//分页选项，当页码变更时，触发该方法
            $scope.reloadList();//调用方法
        }
    };

    //刷新列表 更新复选框
    $scope.reloadList = function () {
        //传入当前页和当前页记录数
        $scope.search($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
    };

    //4.1获取复选框id
    $scope.selectIds = [];//用户选中的id集合
    $scope.updateSelect = function ($event, id) {
        if ($event.target.checked) {//获取到复选框后添加
            $scope.selectIds.push(id);//push方法向集合添加元素
        } else {
            var index = $scope.selectIds.indexOf(id);//查找值的位置
            $scope.selectIds.splice(index, 1);//参数1：移除的位置，参数2：移除的个数
        }
    };

    //优化模板列表显示数据
    $scope.jsonToString = function (jsonString, key) {
        var json = JSON.parse(jsonString);
        var value = "";
        for (var i = 0; i < json.length; i++) {
            if (i > 0) {
               value += "/";
            }
            value += json[i][key];
        }
        return value;
    };


});