<%--
    Document   : index
    Created on : Sep 3, 2017, 5:25:07 PM
    Author     : pham
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Async Servlet2 Page</title>
    </head>
    <body>
        <h1>SHOUT-OUT!</h1>
        <%-- Form area --%>
        <form method="POST" action="shoutServlet">
            <table>
                <tr>
                    <td>Your name:</td>
                    <td><input type="text" id="name" name="name"/></td>
                </tr>
                <tr>
                    <td>Your shout:</td>
                    <td><input type="text" id="message" name="message" /></td>
                </tr>
                <tr>
                    <td><input type="button" onclick="postMessage();" value="SHOUT" /></td>
                </tr>
            </table>
        </form>
        <h2> Current Shouts </h2>
        <%-- Content area --%>
        <div id="content">
            <% if (application.getAttribute("messages") != null) {%>
            <%= application.getAttribute("messages")%>
            <% }%>
        </div>
        <script type="text/javascript" src="<c:url value='/js/messageRetriever.js'></c:url>"></script>
    </body>
</html>
