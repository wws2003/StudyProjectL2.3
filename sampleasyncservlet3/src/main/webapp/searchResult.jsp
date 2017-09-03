<%--
    Document   : searchResult
    Created on : Sep 3, 2017, 11:05:41 PM
    Author     : pham
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Search result Page</title>
    </head>
    <body>
        <h1>Found search <c:out value="${searchResults.size()}"></c:out> results</h1>
            <ul>
            <c:forEach items="${searchResults}" var="searchResult">
                <li><c:out value="${searchResult}"></c:out></li>

            </c:forEach>
        </ul>
    </body>
</html>
