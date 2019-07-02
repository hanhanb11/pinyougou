<html>
<head>
    <title>freemarkerDemo</title>
    <meta charset="utf_8">
    <#include 'head.ftl'>
    <#include 'stu.ftl'>
</head>
<body>
<#-- 我是一个注释，不会输出-->
<!-- html注释-->
<br><br>
<h1>-------------------------------------</h1>
${name},你好,${message}<br>

<#assign linkName="周先生"><br>
${linkName}<br>

<#if success=true>
你已通过实名认证
<#else>
你game mover
</#if>

</body>
</html>