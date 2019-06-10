//二、引入控制器
app.controller("brandController", function ($scope, $controller, brandService) {
    //引入子控制器
    $controller("baseController", {$scope: $scope});
    //第1步.查询品牌列表
    $scope.findAll = function () {
        //请求地址。相当于ajax请求
        brandService.findAll().success(
            function (respose) {
                $scope.list = respose;
            }
        );
    };
    //################baseController.js###################
    //实现分页
    $scope.findPage = function (pageNum, pageSize) {
        //发送请求并传入当前页数和每页显示的条数
        brandService.findPage(pageNum, pageSize).success(
            function (response) {
                $scope.list = response.rows;//显示当前页展示数据
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    };
    //########分页结束#########
    //3.保存商品品牌实体类
    $scope.saveBrand = function () {
        var object = null;//定义对象
        if ($scope.entity.id != null) {
            object = brandService.updateBrand($scope.entity);//添加操作
        } else {
            object = brandService.addBrand($scope.entity);//修改操作
        }
        object.success(
            function (response) {
                if (response.success) {
                    $scope.reloadList();//保存成功，刷新品牌列表页面
                } else {
                    alert(response.message);//保存失败
                }
            }
        );
    };
    //4。 //################baseController.js###################
    // 4.1#########修改开始###########
    $scope.findOne = function (id) {
        //发送请求
        brandService.findOne(id).success(
            //接受传来的数据
            function (response) {
                $scope.entity = response;
            }
        );
    };
    //4。#########修改结束###########
    //4.2删除
    $scope.delBrand = function () {
        brandService.delBrand($scope.selectIds).success(
            function (response) {
                if (response.success) {
                    if (confirm("您确定要删除吗？")) {
                        $scope.reloadList();
                    }
                    alert(response.message);//删除成功，刷新品牌列表页面
                } else {
                    alert(response.message);//删除失败
                }
            }
        );
    };
    //5.条件查询：
    $scope.searchEntity = {};//
    $scope.search = function (pageNum, pageSize) {
        //发送请求并传入当前页数和每页显示的条数
        brandService.search(pageNum, pageSize, $scope.searchEntity).success(
            function (response) {
                $scope.list = response.rows;//显示当前页展示数据
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    };
});