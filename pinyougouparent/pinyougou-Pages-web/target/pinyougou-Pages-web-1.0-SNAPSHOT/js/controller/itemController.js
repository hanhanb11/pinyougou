//二、引入控制器
app.controller("itemController", function ($scope) {
   //1.购物车数量加减
   $scope.num=1;
   //定义方法，按+加1
   $scope.addNum=function(x){
		   $scope.num+=x;   
		if($scope.num<0){
			$scope.num=0;
		}		   
   }
   
//2.############用户选择品牌的sku属性#################
$scope.specification={};//初始化容器
$scope.changeAttribute=function(key,value){//用于存储用户选择的规格
	$scope.specification[key]=value;
	
}
//3.判断是否被选中
$scope.isSelected=function(key,value){
	if($scope.specification[key]==value){
		selectChange();//调用5.2方法
		return true;
	}else{
		return false;
	}
}
//4.加载SKU规格列表
$scope.sku={}
$scope.loadSku=function(){
	$scope.sku=skuList[0];
	$scope.specification= JSON.parse(JSON.stringify($scope.sku.spec))//页面显示默认选中
	
}
//5.选择规格，更新规格
//5.1判断两个对象值匹配不匹配，相互判断
	matchObject=function(map1,map2){
		for(var k in map1){
			if(	map1[k]!= map2[k]){
				return false;
			}
		}
		for(var k in map2){
			if(	map2[k]!= map1[k]){
				return false;
			}			
		}		
		return true;	
	}
  //5.2选中改变
 selectChange=function(){
	  for(var i=0;i<skuList.length;i++){
		  if(matchObject($scope.specification,skuList[i].spec)){
			  $scope.sku=skuList[i];
			  return;
		  }
	  }
		 $scope.sku={id:0,title:'----',};  
		  
  }
  //6.添加购物车
  $scope.addToCart=function(){
	  alert('SKUID:'+$scope.sku.id);
  }
  

});

