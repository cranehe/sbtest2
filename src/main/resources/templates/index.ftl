<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
hello spring boot,database access test<br>
<#list blogs as blog>
    ${blog.title}<br>
    ${blog.author}
</#list>
</body>
</html>