//控制层 
app.controller('userController', function ($scope, $controller, userService) {


    //注册完善

    $scope.reg = function () {
        if ($scope.entity.password != $scope.password) {//比较两次输入的密码是否一致
            $scope.entity.password ="";
            alert("密码不一样")
            return;
        }
        userService.add($scope.entity,$scope.smsCode).success(
            function (response) {
                    alert(response.message);
            });
    }


    //发送验证码
    $scope.send = function () {
        if ($scope.entity.phone==null || $scope.entity.phone==" ") {//比较两次输入的密码是否一致
            alert("请输入手机号！")
            return;
        }
        alert($scope.entity.phone);

        userService.sendCode($scope.entity.phone).success(

            function (response) {
                    alert(response.message);
            });
    }

});	
