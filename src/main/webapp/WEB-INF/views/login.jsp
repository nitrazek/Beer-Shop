<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
<head>
    <title>Logowanie</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel="stylesheet">
</head>
<body>

<div class="wrapper">
    <form method="post">
        <h1>Zaloguj się</h1>
        <div class="input-box">
            <input name="login" value="${fn:escapeXml(login)}" type="text" placeholder="Login" required>
            <i class="bx bxs-user"></i>
        </div>
        <div class="input-box">
            <input name="password" type="password" placeholder="Hasło" required>
            <i class="bx bxs-lock-alt"></i>
        </div>

        <button type="submit" class="btn">Zaloguj się</button>
    </form>
    <div class="register-link">
        <p>Nie masz konta? <a href="${pageContext.request.contextPath}/register">Zarejestruj się</a></p>
    </div>
    <c:if test = "${not empty errors.login}">
    <td><span class="error">${errors.login}</span></td>
    </c:if>
    <c:if test = "${not empty errors.password}">
    <td><span class="error">${errors.password}</span></td>
    </c:if>

</div>
</body>
</html>
