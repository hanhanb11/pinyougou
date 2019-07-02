//服务层
app.service('uploadService',function($http){
	    	
	//上传文件
	this.uploadFile=function(){
		var formData = new FormData();
		//文件上传框的name 为file
        formData.append('file',file.files[0]);
		return $http({

            method:'POST',
			url:'../upload.do',
			data:formData,
			headers:{'Content-Type':undefined},//设定文件上传格式
            transformRequest:angular.identity

			//transformRequest:angular.identity//

		});
	}

});
