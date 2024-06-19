<%--
  Created by IntelliJ IDEA.
  User: pawel
  Date: 15.06.2024
  Time: 17:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

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
        <form method="post">
            <div class="input-group">
                <div class="input-field">
                    <div class="column-title">Nazwa użytkownika</div>
                    <input class="text-input" type="text" name="accountLogin">
                </div>
                <div class="input-field">
                    <div class="column-title">Email</div>
                    <input class="text-input" type="text" name="accountEmail">
                </div>
                <div class="input-field">
                    <div class="column-title">Rola</div>
                    <div class="checkbox-group">
                        <label><input type="checkbox" name="adminRole" value="ADMIN"> Administrator</label>
                        <label><input type="checkbox" name="dealerRole" value="DEALER"> Sprzedawca</label>
                        <label><input type="checkbox" name="clientRole" value="CLIENT"> Klient</label>
                    </div>
                </div>
            </div>
            <c:if test="${not empty errors.param}">
            <td><span class="error">${errors.param}</span></td>
            </c:if>
            <div class="footer">
                <button name="filterButton">Szukaj użytkownika</button>
            </div>
            <div class="footer"><a href="${pageContext.request.contextPath}/admin/editor">
                <button name="addAccountButton">Dodaj nowego użytkownika</button>
            </a></div>
        </form>
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
            <c:forEach items="${accountList}" var="account">
                <tr>
                    <td>${account.login}</td>
                    <td>${account.email}</td>
                    <td>${account.role}</td>
                    <td>
                        <c:if test="${account.role != 'ADMIN'}">
                            <a href="${pageContext.request.contextPath}/admin/editor?userId=${account.id}">
                                <button class="button edit-button" name="editAccountButton">Edytuj</button>
                            </a>
                            <button class="button delete-button" name="deleteAccountButton">Usuń</button>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>

</body>
</html>
