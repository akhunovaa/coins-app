<%@ page language="java" contentType="text/html; charset=utf8"
         pageEncoding="utf8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <title>Авторизаци</title>
</head>
<body>

<a href="<c:url value="/index" />">
    Контакты />
</a><br/>

<c:if test="${not empty param.error}">
    <font color="red"> Ошибка авторизации />
        : ${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message} </font>
</c:if>
<form method="POST" action="<c:url value="/j_spring_security_check" />">
    <table>
        <tr>
            <td align="right">Логин:</td>
            <td><input type="text" name="j_username" /></td>
        </tr>
        <tr>
            <td align="right">Пароль:</td>
            <td><input type="password" name="j_password" /></td>
        </tr>
        <tr>
            <td align="right">Запомнить?</td>
            <td><input type="checkbox" name="_spring_security_remember_me" /></td>
        </tr>
        <tr>
            <td colspan="2" align="right"><input type="submit" value="Login" />
                <input type="reset" value="Reset" /></td>
        </tr>
    </table>
</form>
</body>
</html>