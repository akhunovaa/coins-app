<%@ page language="java" contentType="text/html; charset=utf8"
         pageEncoding="utf8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>Менеджер Бота</title>
</head>
<body>

<h2>Менеджер</h2>

<h3>Последние цены</h3>
<c:if test="${!empty listStatistics}">
    <table class="data">
        <tr>
            <th>ASK цена на первой бирже</th>
            <th>ASK цена на второй бирже</th>
            <th>BID цена на первой бирже</th>
            <th>BID цена на второй бирже</th>
            <th>ASK маржа первая биржа</th>
            <th>ASK маржа вторая биржа</th>
            <th>BID маржа первая биржа</th>
            <th>BID маржа вторая биржа</th>

        </tr>
        <c:forEach items="${listStatistics}" var="statistics">
            <tr>
                <td>${statistics.getFirstAskPrice()}</td>
                <td>${statistics.getSecondAskPrice()}</td>
                <td>${statistics.getFirstBidPrice()}</td>
                <td>${statistics.getSecondBidPrice()}</td>
                <td>${statistics.getAskMargeOne()}</td>
                <td>${statistics.getAskMargeTwo()}</td>
                <td>${statistics.getBidMargeOne()}</td>
                <td>${statistics.getBidMargeTwo()}</td>
            </tr>
        </c:forEach>
    </table>
</c:if>

<c:url var="logoutUrl" value="/logout" />
<form action="${logoutUrl}" id="logout" method="post">
    <input type="hidden" name="${_csrf.parameterName}"
           value="${_csrf.token}" />
    <input type="submit" name="submit" value="Log Out">
</form>
</body>

</html>
