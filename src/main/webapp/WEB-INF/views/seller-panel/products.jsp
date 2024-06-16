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
    <title>Lista produktów</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/panel.css">
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel="stylesheet">

</head>
<body>
<header>
    <i class='bx bx-beer'></i> eBrowarek
</header>
<div class="container">
    <div class="header2">
        <div class="left">
            <div class="title">Lista produktów</div>
        </div>
    </div>

    <div class="filters">
        <div class="header">Filtry</div>
        <div class="input-group">
            <div class="input-field">
                <div class="column-title">Nazwa produktu</div>
                <input class="text-input" type="text">
            </div>
            <div class="input-field">
                <div class="column-title">Kategoria</div>
                <div class="checkbox-group">
                    <label><input type="checkbox" name="role" value="piwo"> Piwo</label>
                </div>
            </div>
        </div>
        <div class="footer"><button>Szukaj produktu</button></div>
    </div>

    <div class="user-list">
        <table>
            <thead>
            <tr>
                <th>Nazwa produktu</th>
                <th>Kategoria</th>
                <th>Cena</th>
                <th>Akcje</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td>Żubr</td>
                <td>Piwo</td>
                <td>4.50 zł</td>
                <td>
                    <button class="button edit-button">Edytuj</button>
                    <button class="button delete-button">Usuń</button>
                </td>
            </tr>
            <tr>
                <td>Tyskie</td>
                <td>Piwo</td>
                <td>4.99 zł</td>
                <td>
                    <button class="button edit-button">Edytuj</button>
                    <button class="button delete-button">Usuń</button>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>

</body>
</html>
