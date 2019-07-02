//控制层
app.controller('goodsController', function (
                    $scope,$controller, $location,goodsService, uploadService,
                    itemCatService, typeTemplateService) {
    $controller('baseController', {$scope: $scope});//继承
    //读取列表数据绑定到表单中
    $scope.findAll = function () {
        goodsService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        );
    }
    //分页
    $scope.findPage = function (page, rows) {
        goodsService.findPage(page, rows).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }
//#######################################################################################################################
    //查询实体
    $scope.findOne = function () {
        //修改商品信息，回写数据
        var id = $location.search()['id'];
        alert(id);
      if(id==null){
          return;
      }
        goodsService.findOne(id).success(

            function (response) {
                $scope.entity = response;
                //富文本，获取商品介绍
                editor.html( $scope.entity.tbGoodsDesc.introduction);
                //获取商品图片进行JSON转换
                $scope.entity.tbGoodsDesc.itemImages = JSON.parse($scope.entity.tbGoodsDesc.itemImages);
                //获取商品扩展属性进行JSON转换
                $scope.entity.tbGoodsDesc.customAttributeItems = JSON.parse($scope.entity.tbGoodsDesc.customAttributeItems);
                //获取商品规格和规格选项进行JSON转换
                $scope.entity.tbGoodsDesc.specificationItems =JSON.parse($scope.entity.tbGoodsDesc.specificationItems);
                //获取SKU列表信息进行JSON转换
                for(var i=0;i<$scope.entity.itemList.length;i++){
                $scope.entity.itemList[i].spec=JSON.parse($scope.entity.itemList[i].spec);
                }
            }
        );
    }
    //定义方法读取复选框是否勾选
    $scope.checkAttributeValue=function(specName,optionName){//specName keyValue
        var items =  $scope.entity.tbGoodsDesc.specificationItems;
        //在list集合中根据key的值查询对象
        var object =$scope.selectObjectByKey(items,'attributeName',specName);
        if(object!=null){
            if(object.attributeValue.indexOf(optionName)>=0){//如果能查询奥规格选项值
                return true;
            }else {
                return false;
            }
        }else{
            return false;
        }
    }
    //#######################################################################################################################
    //保存
    $scope.save=function(){
        $scope.entity.tbGoodsDesc.introduction = editor.html();//富文本新增数据
    	var serviceObject;//服务层对象
    	if($scope.entity.tbGoods.id!=null){//如果有ID
    		serviceObject=goodsService.update( $scope.entity ); //修改
    	}else{
    		serviceObject=goodsService.add( $scope.entity  );//增加
    	}
    	serviceObject.success(
    		function(response){
    			if(response.success){
                    //提示信息添加成功
                    alert(response.message);
                    //添加成功后，页面清空
                    // $scope.entity = {};
                    // editor.html("");//清空富文本编辑器
                    location.href='goods.html';
    			}else{
    				alert(response.message);
    			}
    		}
    	);
    }
    // //插入数据
    // $scope.add = function () {
    //     $scope.entity.tbGoodsDesc.introduction = editor.html();
    //     alert(editor.html());
    //     goodsService.add($scope.entity).success(
    //         function (response) {
    //             if (response.success) {
    //                 //提示信息添加成功
    //                 alert("新增成功");
    //                 //添加成功后，页面清空
    //                 $scope.entity = {};
    //                 editor.html("");//清空富文本编辑器
    //             } else {
    //             }
    //         }
    //     );
    // };
//###########################################################################################################
    //批量删除
    $scope.dele = function () {
        //获取选中的复选框
        goodsService.dele($scope.selectIds).success(
            function (response) {
                if (response.success) {
                    $scope.reloadList();//刷新列表
                    $scope.selectIds = [];
                }
            }
        );
    };
//###########################################################################################################
    $scope.searchEntity = {};//定义搜索对象
    //搜索
    $scope.search = function (page, rows) {
        goodsService.search(page, rows, $scope.searchEntity).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    };
//###########################################################################################################
    //上传图片
    $scope.uploadFile = function () {
        uploadService.uploadFile().success(
            function (response) {
                if (response.success) {
                    //上传成功
                    $scope.image_entity.url = response.message;//后台返回结果赋值给url
                } else {
                    alert(response.message);
                }
            });
    };
    //添加图片列表itemImages,规格列表specificationItems
    $scope.entity = {goods: {}, tbGoodsDesc: {itemImages: [], specificationItems: []}};
    $scope.add_item_images = function () {
        //向集合中添加图片，显示在列表
        $scope.entity.tbGoodsDesc.itemImages.push($scope.image_entity);
    }
    //移除图片
    $scope.remove_image_entity = function (index) {
        //向集合中添加图片，显示在列表
        $scope.entity.tbGoodsDesc.itemImages.splice(index, 1);
    }
//###########################################################################################################
    //实现一级下拉列表
    $scope.selectItemCat1List = function () {
        itemCatService.findByParentId(0).success(
            function (response) {
                $scope.itemCat1List = response;
            }
        );
    }
    //实现二级下拉列表,,$watch ,监控entity.tbGoods.category1Id
    $scope.$watch('entity.tbGoods.category1Id', function (newValue, oldValue) {
        itemCatService.findByParentId(newValue).success(
            function (response) {
                $scope.itemCat2List = response;
            }
        );
    })
    //实现三级下拉列表,$watch ,监控entity.tbGoods.category2Id
    $scope.$watch('entity.tbGoods.category2Id', function (newValue, oldValue) {
        itemCatService.findByParentId(newValue).success(
            function (response) {
                $scope.itemCat3List = response;
            }
        );
    });
    //###########################################################################################################
    //显示模板Id
    $scope.$watch('entity.tbGoods.category3Id', function (newValue, oldValue) {
        itemCatService.findOne(newValue).success(
            function (response) {
                $scope.entity.tbGoods.typeTemplateId = response.typeId;
            });
    });
    //展示品牌下拉列表，监控模板ID 和 展示商品扩展属性 规格列表
    $scope.$watch('entity.tbGoods.typeTemplateId', function (newValue, oldValue) {
        typeTemplateService.findOne(newValue).success(
            function (response) {
                //后台传来数据response赋值一个变量typeTemplate
                $scope.typeTemplate = response;
                //  将字符串转换成JSON对象
                $scope.typeTemplate.brandIds = JSON.parse($scope.typeTemplate.brandIds);
                // $scope.customAttributeItems =JSON.parse($scope.typeTemplate.customAttributeItems);
                //展示商品扩展属性
                if($location.search()['id']==null){
                    //如果商品id为空时，则是添加，在修改findOne中有冲突
                    $scope.entity.tbGoodsDesc.customAttributeItems = JSON.parse($scope.typeTemplate.customAttributeItems);
                }
                // alert($scope.entity.tbGoodsDesc.customAttributeItems);
            });
        //查询规格
        typeTemplateService.findSpecList(newValue).success(
            function (response) {
                $scope.specList = response;
            });
    });
//###########################################################################################################
    $scope.updateSpecAttribute = function ($event, name, value) {
        //调用方法
        var object = $scope.selectObjectByKey($scope.entity.tbGoodsDesc.specificationItems, 'attributeName', name);
        //alert(object);
        if (object != null) {
            if ($event.target.checked) {//选中时添加到集合中
                object.attributeValue.push(value);
            } else {//取消选中复选框时
                object.attributeValue.splice(object.attributeValue.indexOf(value), 1);
                if (object.attributeValue.length == 0) {
                    //如果未选中任何元素，则移除选项内容，如果选项都取消了，则移除该条记录
                    $scope.entity.tbGoodsDesc.specificationItems.splice(
                        $scope.entity.tbGoodsDesc.specificationItems.indexOf(object), 1);
                }
            }
        } else {//如果刚开始未选中，则添加属性
            $scope.entity.tbGoodsDesc.specificationItems.push({"attributeName": name, "attributeValue": [value]});
        }
    };
//###########################################################################################################
    //创建SKU列表
    $scope.createItemList = function () {
        //初始化列表
        $scope.entity.itemList = [{spec: {}, price: 0, num: 99999, status: '0', isDefault: '0'}];
        //定义一个变量items
        var items = $scope.entity.tbGoodsDesc.specificationItems;
        //alert("items.."+JSON.stringify($scope.entity.tbGoodsDesc.specificationItems));//测试
        for (var i = 0; i < items.length; i++) {
            //调用深克隆的方法
            //进行封装赋值给默认集合
            $scope.entity.itemList = addColumn($scope.entity.itemList, items[i].attributeName, items[i].attributeValue);
        }
    };
    //定义一个方法，进行深克隆，
    addColumn = function (list, ColumnName, ColumnValues) {
        //alert(ColumnName)
        var newList = [];
        for (var i = 0; i < list.length; i++) {
            var oldRow = list[i];  //克隆的行
            for (var j = 0; j < ColumnValues.length; j++) {
                //进行克隆
                var newRow = JSON.parse(JSON.stringify(oldRow));
                newRow.spec[ColumnName] = ColumnValues[j];
                //alert(ColumnValues[j])
                newList.push(newRow);
            }
        }
        //alert(newList+"............newList");       //测试
        return newList;
    }
//###########################################################################################################
    //显示审核状态,绑定一个变量，将其赋值，
    // $scope.auditStatus = ['未审核', '审核通过', '审核未通过', '已关闭'];
    //显示商品列表一级分类，二级分类，三级分类名称，定义一个方法，先查询出全部商品集合
    //初始化商品分类集合
    $scope.ItemCatList =[];
    $scope.findItemList = function () {
        itemCatService.findAll().success(
            function (response) {
                for (var i=0;i<response.length;i++){
                    $scope.ItemCatList[response[i].id]=response[i].name;

                }
            }
        );
    }
    //显示审核状态,绑定一个变量，将其赋值，
    $scope.auditstatus = ['未审核', '审核通过', '审核未通过', '已关闭','上架','已下架'];
    //商品上下架状态

    //实现下拉列表,,$watch ,监控searchEntity.auditStatus.页面传回来的值
    $scope.$watch('searchEntity.auditStatus', function (newValue) {
        $scope.reloadList();
    })
//###########################################################################################################
    //商品上架下架
    $scope.updateIsMarketable=function (isMarketable) {
        goodsService.updateIsMarketable($scope.selectIds,isMarketable).success(
            function (response) {
            if (response.success){
                $scope.reloadList();//刷新页面
                $scope.selectIds=[];
            }else {
                alert(response.message);
            }


        });
    }





});