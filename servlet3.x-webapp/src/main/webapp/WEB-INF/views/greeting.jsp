<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=Edge">
        <title></title>
    </head>
    <body>
        <h2>Welcome Everyone!</h2>
        <ul>
            <li>
                <h3>Page Session</h3>
                <div>
                    Hello, <%= null == session.getAttribute("name")?"World" : session.getAttribute("name")%>
                </div>
                <div>Session ID: <%=session.getId()%></div>
            </li>
            <li>
                <h3>request.getSession()</h3>
                <div>
                    Hello, <%= null == request.getSession().getAttribute("name")? "World" : request.getSession().getAttribute("name")%>
                </div>
                <div>Session ID: <%=request.getSession().getId()%></div>
            </li>
        </ul>
    </body>
</html>
