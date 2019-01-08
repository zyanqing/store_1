<%--
  Created by IntelliJ IDEA.
  User: anjubao
  Date: 2019/1/8
  Time: 下午2:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>$Title$</title>
  </head>
  <body>
  <form action="TestServlet" method="post" enctype="multipart/form-data">
    账户:<input type="text" name="username"/><br/>
    密码:<input type="text" name="password"/><br/>
    头像:<input type="file" name="userhead"/><br/>
    <button>提交数据</button>
  </form>
  </body>
</html>
