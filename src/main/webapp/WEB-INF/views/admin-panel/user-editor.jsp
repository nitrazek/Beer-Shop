<%--
  Created by IntelliJ IDEA.
  User: pawel
  Date: 15.06.2024
  Time: 17:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Dodaj/edytuj użytkownika</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/editor.css">
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel="stylesheet">
</head>
<body>
<header>
    <i class='bx bx-beer'></i> eBrowarek
</header>
<div class="container">
    <div class="header2">
        <div class="left">
            <div class="title">Dodaj/edytuj użytkownika</div>
        </div>
    </div>

    <div class="wrapper">
        <div class="input-group">
            <div class="input-field">
                <div class="column-title">Nazwa użytkownika</div>
                <input class="text-input" type="text">
                <div class="column-title">Email</div>
                <input class="text-input" type="text">
                <div class="column-title">Hasło</div>
                <input class="text-input" type="password">
                <div class="column-title">Powtórz hasło</div>
                <input class="text-input" type="password">
                <div class="column-title">Rola</div>
                <div class="radio-group">
                    <label><input type="radio" name="role" value="Sprzedawca"> Sprzedawca</label><br/>
                    <label><input type="radio" name="role" value="Klient"> Klient</label>
                </div>
                <div class="footer"><button>Zapisz</button></div>
                <div class="footer"><button>Anuluj</button></div>
            </div>
        </div>
    </div>

</div>

</body>
</html>
