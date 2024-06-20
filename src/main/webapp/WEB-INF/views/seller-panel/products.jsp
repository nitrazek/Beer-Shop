<%--
  Created by IntelliJ IDEA.
  User: pawel
  Date: 15.06.2024
  Time: 17:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
<head>
    <title>Lista produktów</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/panel.css">
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel="stylesheet">

</head>
<body>
<script>
    var categoryNames = {
        'BEER': 'Piwo',
        'BEER_GLASSWARE': 'Szkło do piwa',
        'HOMEBREWING_EQUIPMENT': 'Sprzęt do warzenia piwa',
        'BEER_ACCESSORY': 'Akcesoria do piwa',
        'BEER_MERCHANDISE': 'Inne'
    };

    function setCategoryLabel(productCategory) {
        var labelId = "categoryLabel-" + productCategory;
        var labelElement = document.getElementById(labelId);
        if (labelElement) {
            labelElement.textContent = categoryNames[productCategory];
        }
    }
</script>
<header>
    <div class="header-content">
        <span><i class='bx bx-beer'></i> eBrowarek</span>
        <nav>
            <p><a href="${pageContext.request.contextPath}/shop/products"><i class='bx bx-shopping-bag'></i> Sklep</a>
            </p>
            <p><a href="${pageContext.request.contextPath}/shop/orders"><i class='bx bx-list-check'></i> Historia
                zamówień</a>
            </p>
            <c:if test="${navRole=='ADMIN'}">
                <p><a href="${pageContext.request.contextPath}/admin/users"><i class='bx bx-crown'></i> Panel
                    administratora</a>
                </p>
            </c:if>
            <p><a href="${pageContext.request.contextPath}/logout"><i class='bx bx-log-out'></i> Wyloguj się</a></p>
        </nav>
    </div>
</header>

<div class="container">
    <div class="header2">
        <div class="left">
            <div class="title">Lista produktów</div>
        </div>
    </div>

    <div class="filters">
        <form method="post">
            <div class="header">Filtry</div>
            <div class="input-group">
                <div class="input-field">
                    <div class="column-title">Nazwa produktu</div>
                    <input name="contains" class="text-input" type="text">
                </div>

                <div class="input-field">
                    <div class="column-title">Minimalna cena produktu (PLN)</div>
                    <input class="text-input" type="number" name="minValue" step="0.01" min="0" max="10000">
                </div>

                <div class="input-field">
                    <div class="column-title">Maksymalna cena produktu (PLN)</div>
                    <input class="text-input" type="number" name="maxValue" step="0.01" min="0" max="10000">
                </div>

            </div>

            <div class="input-field">
                <div class="column-title">Kategoria</div>
                <div class="checkbox-group">
                    <c:forEach items="${categoryList}" var="productCategory">
                        <input type="radio" name="category" value="${fn:escapeXml(productCategory)}">
                        <label id="categoryLabel-${fn:escapeXml(productCategory)}"></label>
                        <script>
                            setCategoryLabel("${fn:escapeXml(productCategory)}");
                        </script>
                    </c:forEach>
                </div>
            </div>

            <c:if test="${not empty errors.param}">
                <td><span class="error">${errors.param}</span></td>
            </c:if>

            <div class="footer">
                <button name="filterButton" type="submit">Szukaj produktu</button>
            </div>
        </form>
        <div class="footer"><a href="${pageContext.request.contextPath}/seller/editor">
            <button name="addProductButton">Dodaj nowy produkt</button>
        </a></div>
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
            <c:forEach items="${productList}" var="product">
                <tr>
                    <td>${product.name}</td>
                    <td><script>document.write(categoryNames['${fn:escapeXml(product.category)}']);</script></td>
                    <td>${product.price} zł</td>
                    <td>
                        <a href="${pageContext.request.contextPath}/seller/editor?productId=${product.id}">
                            <button class="button edit-button" name="editAccountButton">Edytuj</button>
                        </a>
                        <form method="post">
                            <input type="hidden" name="productId" value="${product.id}">
                            <button type="submit" name="deleteProductButton" class="button delete-button">Usuń</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>

</body>
</html>