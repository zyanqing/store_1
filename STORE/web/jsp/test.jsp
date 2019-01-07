<%--
  Created by IntelliJ IDEA.
  User: anjubao
  Date: 2018/12/4
  Time: 上午8:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <script src="${pageContext.request.contextPath}/js/jquery-1.11.3.min.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/js/bootstrap.min.js" type="text/javascript"></script>
    <title>Title</title>
</head>
<body>

<c:forEach items="${news}" var="p">
    <div class="col-md-2" style="text-align:center;height:200px;padding:10px 0px;">
        <a href="${pageContext.request.contextPath}/ProductServlet?method=findProductByPid&pid=${p.pid}">
            <img src="${pageContext.request.contextPath}/${p.pimage}" width="130" height="130" style="display: inline-block;">
        </a>
        <p><a href="${pageContext.request.contextPath}/ProductServlet?method=findProductByPid&pid=${p.pid}" style='color:#666'>${p.pname}</a></p>
        <p><font color="#E4393C" style="font-size:16px">&yen;${p.shop_price}</font></p>
    </div>
</c:forEach>

</body>
</html>
