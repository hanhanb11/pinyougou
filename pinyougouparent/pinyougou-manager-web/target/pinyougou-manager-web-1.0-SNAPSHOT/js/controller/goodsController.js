 //控制层 
app.controller('goodsController' ,function($scope,$controller,$location   ,goodsService,itemCatService){
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		goodsService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		goodsService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	//#######################################################################################################
	//查询实体 与前端回写实体
	$scope.findOne=function(){
        //修改商品信息，回写数据
        var id = $location.search()['id'];
        //alert(id)
        if(id==null){
            return;
        }
		goodsService.findOne(id).success(
			function(response) {
                $scope.entity = response;
                // //富文本，获取商品介绍
                // editor.html($scope.entity.tbGoodsDesc.introduction);
                // //获取商品图片进行JSON转换
                // $scope.entity.tbGoodsDesc.itemImages = JSON.parse($scope.entity.tbGoodsDesc.itemImages);
                // //获取商品扩展属性进行JSON转换
                // $scope.entity.tbGoodsDesc.customAttributeItems = JSON.parse($scope.entity.tbGoodsDesc.customAttributeItems);
                // //获取商品规格和规格选项进行JSON转换
                // $scope.entity.tbGoodsDesc.specificationItems = JSON.parse($scope.entity.tbGoodsDesc.specificationItems);
                // //获取SKU列表信息进行JSON转换
                // for (var i = 0; i < $scope.entity.itemList.length; i++) {
                //     $scope.entity.itemList[i].spec = JSON.parse($scope.entity.itemList[i].spec);
                // }
            }
		);				
	}
//#################################################################################################################
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象  				
		if($scope.entity.id!=null){//如果有ID
			serviceObject=goodsService.update( $scope.entity ); //修改  
		}else{
			serviceObject=goodsService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询 
		        	$scope.reloadList();//重新加载
				}else{
					alert(response.message);
				}
			}		
		);				
	}

//#################################################################################################################
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		goodsService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
					$scope.selectIds=[];
				}						
			}		
		);				
	}
//#################################################################################################################
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		goodsService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
//#################################################################################################################
	//商家状态审核
	$scope.status=['未审核','审核通过','审核未通过','已关闭'];
    $scope.ItemCatList=[];
	$scope.findItemCatList=function () {
		itemCatService.findAll().success(
			function (response) {
                for(var i =0;i<response.length;i++){
                    $scope.ItemCatList[response[i].id]=response[i].name;
				}
        });
    }
//#################################################################################################################
    //商家状态审核和驳回
	$scope.updateStatus1=function (status) {
		goodsService.updateStatus($scope.selectIds,status).success(
			function (response) {
				if(response.success){
					$scope.reloadList();//刷新页面
                    $scope.selectIds=[];//选中的ids清空
				}else{
					alert(response.message);
				}
            }
		);
    }


});	
