//一 、引入品牌服务
app.service("brandService", function ($http) {
    //1..查询品牌列表
    this.findAll = function () {
        return $http.get("../brand/findAll.do");
    };
    //2.实现分页
    this.findPage = function (pageNum, pageSize) {
        return $http.get('../brand/findPage.do?pageNum=' + pageNum + '&pageSize=' + pageSize);
    };
    //3.修改
    this.findOne = function (id) {
        return $http.get("../brand/findOne.do?id=" + id);
    };
    //4.添加
    this.addBrand = function (entity) {
        return $http.post('../brand/addBrand.do', entity);
    };
    //更新
    this.updateBrand = function (entity) {
        return $http.post('../brand/updateBrand.do', entity);
    };
    //5.删除
    this.delBrand = function (ids) {
        return $http.get('../brand/delBrand.do?ids=' + ids);
    };
    //6.搜索
    this.search = function (pageNum, pageSize, searchEntity) {
        return $http.post('../brand/search.do?pageNum=' + pageNum + '&pageSize=' + pageSize, searchEntity)
    };
    //7.品牌下拉列表
    this.selectOptionList=function () {
        return $http.get('../brand/selectOptionList.do');
    }
});