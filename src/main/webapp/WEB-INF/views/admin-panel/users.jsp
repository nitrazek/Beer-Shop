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
    <title>Lista użytkowników</title>
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
            <div class="title">Lista użytkowników</div>
        </div>
    </div>

    <div class="filters">
        <div class="header">Filtry</div>
        <div class="input-group">
            <div class="input-field">
                <div class="column-title">Nazwa użytkownika</div>
                <input class="text-input" type="text">
            </div>
            <div class="input-field">
                <div class="column-title">Email</div>
                <input class="text-input" type="text">
            </div>
            <div class="input-field">
                <div class="column-title">Rola</div>
                <div class="checkbox-group">
                    <label><input type="checkbox" name="role" value="Administrator"> Administrator</label>
                    <label><input type="checkbox" name="role" value="Sprzedawca"> Sprzedawca</label>
                    <label><input type="checkbox" name="role" value="Klient"> Klient</label>
                </div>
            </div>
        </div>
        <div class="footer"><button>Szukaj użytkownika</button></div>
    </div>

    <div class="user-list">
        <table>
            <thead>
            <tr>
                <th>Nazwa użytkownika</th>
                <th>Email</th>
                <th>Rola</th>
                <th>Akcje</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td>admin</td>
                <td>admin@ebrowarek.pl</td>
                <td>Administrator</td>
                <td>
                    <button class="button edit-button">Edytuj</button>
                    <button class="button delete-button">Usuń</button>
                </td>
            </tr>
            <tr>
                <td>handlarz1</td>
                <td>handlarz1@ebrowarek.pl</td>
                <td>Sprzedawca</td>
                <td>
                    <button class="button edit-button">Edytuj</button>
                    <button class="button delete-button">Usuń</button>
                </td>
            </tr>
            <tr>
                <td>klient</td>
                <td>sałajajaj@sw.xd</td>
                <td>Klient</td>
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
