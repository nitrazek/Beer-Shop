<%--
  Created by IntelliJ IDEA.
  User: pawel
  Date: 05.06.2024
  Time: 18:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Rejestracja</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel="stylesheet">
</head>
<body>

<div class="wrapper">
    <form action="post">
        <h1>Zarejestruj się</h1>
        <div class="input-box">
            <input type="text" placeholder="Login" required>
            <i class="bx bxs-user"></i>
        </div>

        <div class="input-box">
            <input type="text" placeholder="Email" required>
            <i class="bx bxs-user"></i>
        </div>

        <div class="input-box">
            <input type="password" placeholder="Hasło" required>
            <i class="bx bxs-lock-alt"></i>
        </div>

        <div class="input-box">
            <input type="password" placeholder="Powtórz hasło" required>
            <i class="bx bxs-lock-alt"></i>
        </div>

        <button type="submit" class="btn">Zarejestruj się</button>

        <div class="register-link">
            <p>Masz już konto? <a href="/beershop/login">Zaloguj się</a></p>
        </div>
    </form>
</div>
</body>
</html>
