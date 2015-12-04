<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="com.github.loafer.bean.User" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=Edge">
        <title></title>
    </head>
    <body>
        <h3>Hello, <%=((User)request.getSession().getAttribute("user")).getName()%></h3>
    </body>
</html>
