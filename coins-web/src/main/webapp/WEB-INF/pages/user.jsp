<%@ page language="java" contentType="text/html; charset=utf8"
         pageEncoding="utf8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>Менеджер пользователей</title>
</head>
<body>

<h2>Менеджер</h2>

<form:form method="post" action="add" commandName="user">

    <table>
        <tr>
            <td> Имя пользователя</td>
            <td><form:input path="firstname" /></td>
        </tr>
        <tr>
            <td> Фамилия</td>
            <td><form:input path="lastname" /></td>
        </tr>
        <tr>
            <td> e-mail</td>
            <td><form:input path="email" /></td>
        </tr>
        <tr>
            <td>nickname</td>
            <td><form:input path="nickname" /></td>
        </tr>
        <tr>
            <td colspan="2"><input type="submit"
                                   value="Отправить" /></td>
        </tr>
    </table>
</form:form>

<h3>Пользователи</h3>
<c:if test="${!empty listUser}">
    <table class="data">
        <tr>
            <th>Имя</th>
            <th>Фамилия</th>
            <th>Никнэйм</th>
            <th>e-mail</th>
        </tr>
        <c:forEach items="${listUser}" var="user">
            <tr>
                <td>${user.lastname}, ${user.firstname}</td>
                <td>${user.email}</td>
                <td>${user.nickname}</td>
                <td><a href="delete/${user.id}">Удалить</a></td>
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
