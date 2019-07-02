app.controller('searchController', function ($scope, $location, searchService) {

    //构建搜索条件
    //  关键字  商品分类  品牌  规格
    $scope.searchMap = {
        'keywords': '',//关键字
        'category': '',//商品分类
        'brand': '',//商品品牌
        'spec': {},//商品规格列表
        'price': '',//商品价格
        'pageNo': 1,//分页，默认第一页
        'pageSize': 10,//每页显示10条
        'sort': '',//排序字段
        'sortField': ''//排序关键字
    };
//################################总开关搜索#######################################################
    $scope.search = function () {
        $scope.searchMap.pageNo = parseInt($scope.searchMap.pageNo);//将当前页码格式，传到后条转换成int
        searchService.searchItem($scope.searchMap).success(
            function (response) {
                $scope.resultMap = response;//返回搜索结果
                buildPageLabel();//构建分页栏
            }
        );
    };
//#########################构建分页栏##############################################################
    buildPageLabel = function () {
        var firstPage = 1;//开始页码
        var lastPage = $scope.resultMap.totalPages;//截止页码
        var currentPage = $scope.searchMap.pageNo;//当前页
        $scope.firstDot = true;//设置前边有点
        $scope.lastDot = true;//后边有点
        if (lastPage > 5) {//页码大于5
            if (currentPage <= 3) {//如果当前页码小于等于3，显示前5页
                lastPage = 5;//截止页等于5
                $scope.firstDot = false;
            } else if (currentPage >= lastPage - 2) {//如果当前页码大于截止页码减2，显示后5页
                firstPage = lastPage - 4;//开始页等于截止页减四
                $scope.lastDot = false;
            } else {//显示中间5条
                firstPage = currentPage - 2;
                lastPage = currentPage + 2;
            }
        } else {
            $scope.firstDot = false;//设置前边无点
            $scope.lastDot = false;//后边无点
        }
        $scope.pageLabel = [];//初始化pageLabel为集合，
        for (var i = firstPage; i <= lastPage; i++) {
            $scope.pageLabel.push(i)
        }
    };
//#######################切换页面##################################################################
    $scope.changePage = function (pageNo) {
        if (pageNo < 1 || pageNo > $scope.resultMap.totalPages) {
            return;
        }
        $scope.searchMap.pageNo = pageNo;
        $scope.search();
    };
    //上一页，和最后一页样式
    $scope.isTopPage = function () {
        if ($scope.searchMap.pageNo == 1) {
            return true;
        } else {
            return false;
        }
    }
    //判断是否为最后一页
    $scope.isEndPage = function () {
        if ($scope.searchMap.pageNo == $scope.resultMap.totalPages) {
            return true;
        } else {
            return false;
        }
    }
//##################价格等等排序查询############################################################
    $scope.searchSort = function (sortField, sort) {
        $scope.searchMap.sortField = sortField;
        $scope.searchMap.sort = sort;
        //然后调用查询方法
        $scope.search();
    }
//####################隐藏品牌列表##############################################################
    $scope.keywordsIsBrand = function () {
        for (var i = 0; i < $scope.resultMap.brandList.length; i++) {
            if ($scope.searchMap.keywords.contains($scope.resultMap.brandList[i].text)) {
                return true;
            }
        }
        return false;
    };

//####################搜索选项#####################################################
    //添加搜索选项，改变searchMaP的值，添加查询条件
    $scope.addSearchItem = function (key, value) {
        if (key == 'category' || key == 'brand' || key == 'price') {
            $scope.searchMap[key] = value;
        } else {
            $scope.searchMap.spec[key] = value;
        }
        $scope.searchMap.pageNo = 1;
        //查询后台数据进行分类查询
        $scope.search();
    };
    //移除搜索选项，,将查询条件移除
    $scope.removeSearchItem = function (key) {
        if (key == 'category' || key == 'brand' || key == 'price') {
            $scope.searchMap[key] = '';
        } else {
            delete $scope.searchMap.spec[key];
        }
        $scope.searchMap.pageNo = 1;
        //查询后台数据进行分类查询
        $scope.search();
    }
//################################################################################
    //接受跳转页的关键字
    $scope.acceptKeywords = function () {
        $scope.searchMap.keywords = $location.search()['keywords']//首页方法
        $scope.search();//搜索页方法
    }

});