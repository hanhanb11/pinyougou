<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
</head>
<body>
<h2>--学生列表--</h2><br>

         <#list stu as student>
              ${student_index}
            姓名：${student.name}
            学号：${student.num}<br/>
         </#list>
    内嵌指令1:一共${stu?size}条记录<br>
    内嵌指令2:<br>
    <#assign text="{'bank':'中国银行','accout':'123456'}">
    <#assign data=text?eval><#-- JSON 转换成对象-->
    银行：${data.bank}<br>
    账户：${data.accout}<br>
    当前日期：${today?date}<br>
    当前时间：${today?time}<br>
    当前日期加时间：${today?datetime}<br>
    日期格式化：${today?string('yyyy年MM月')}<br>
    当前积分:${point?c}<br>
    <#-- 空值不存在运算 -->
    <#-- 1 -->
    <#if QQ??>
        QQ存在 ${QQ}<br>
    <#else>
        QQ不存在<br>
    </#if>
    <#-- 2 -->
    ${aa!'aa没有被赋值'}<br>


</body>
</html>