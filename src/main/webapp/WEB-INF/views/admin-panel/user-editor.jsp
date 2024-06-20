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
    <title>Dodaj/edytuj użytkownika</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/editor.css">
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel="stylesheet">
</head>
<body>
<header>
    <div class="header-content">
        <span><i class='bx bx-beer'></i> eBrowarek</span>
        <nav>
            <p><a href="${pageContext.request.contextPath}/shop/products"><i class='bx bx-shopping-bag'></i> Sklep</a>
            </p>
            <p><a href="${pageContext.request.contextPath}/shop/orders"><i class='bx bx-list-check'></i> Historia
                zamówień</a>
            </p>
            <p><a href="${pageContext.request.contextPath}/seller/products"><i class='bx bx-store'></i> Panel sprzedawcy</a>
            </p>
            <p><a href="${pageContext.request.contextPath}/logout"><i class='bx bx-log-out'></i> Wyloguj się</a></p>
        </nav>
    </div>
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
                <form method="post">
                    <c:if test="${not empty errors.id}">
                        <td><span class="error">BŁĄD: ${errors.id} <br/></span></td>
                    </c:if>
                    <input type="hidden" name="accountId" value="${account.id}">
                    <div class="column-title">Nazwa użytkownika</div>
                    <input class="text-input" name="accountLogin" value="${account.login}" type="text"/>
                    <c:if test="${not empty errors.login}">
                        <td><span class="error">BŁĄD: ${errors.name} <br/></span></td>
                    </c:if>
                    <div class="column-title">Email</div>
                    <input class="text-input" name="accountEmail" value="${account.email}" type="text"/>
                    <c:if test="${not empty errors.email}">
                        <td><span class="error">BŁĄD: ${errors.name} <br/></span></td>
                    </c:if>
                    <div class="column-title">Hasło</div>
                    <input class="text-input" name="accountPassword" type="password"/>
                    <div class="column-title">Powtórz hasło</div>
                    <input class="text-input" name="accountRetypedPassword" type="password"/>
                    <c:if test="${not empty errors.password}">
                        <td><span class="error">BŁĄD: ${errors.password} <br/></span></td>
                    </c:if>
                    <div class="column-title">Rola</div>
                    <div class="radio-group">
                        <label><input type="radio" name="accountRole" value="SELLER" id="sprzedawca-radio">
                            Sprzedawca</label><br/>
                        <label><input type="radio" name="accountRole" value="CLIENT" id="klient-radio"> Klient</label>
                    </div>
                    <c:if test="${not empty errors.role}">
                        <td><span class="error">BŁĄD: ${errors.role} <br/></span></td>
                    </c:if>
                    <div class="footer">
                        <button>Zapisz</button>
                    </div>
                </form>
                <div class="footer"><a href="${pageContext.request.contextPath}/admin/users">
                    <button>Anuluj</button>
                </a></div>
            </div>
        </div>
    </div>

</div>
<script>
    document.addEventListener('DOMContentLoaded', function () {
        var role = '${account.role}';

        if (role === 'SELLER') {
            document.getElementById('sprzedawca-radio').checked = true;
        } else if (role === 'CLIENT') {
            document.getElementById('klient-radio').checked = true;
        }
    });

</script>
</body>
</html>
