<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <title>eBrowarek - Lista produktów</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/product.css">
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel="stylesheet">
</head>
<body>
<header>
    <div class="header-content">
        <span><i class='bx bx-beer'></i> eBrowarek</span>
        <nav>
            <p><a href="${pageContext.request.contextPath}/shop/orders"><i class='bx bx-list-check' ></i> Historia zamówień</a>
            </p>

            <p><a href="${pageContext.request.contextPath}/seller/products"><i class='bx bx-store'></i> Panel sprzedawcy</a>
            </p>
            <p><a href="${pageContext.request.contextPath}/admin/users"><i class='bx bx-crown'></i> Panel administratora</a>
            </p>
            <p><a href="${pageContext.request.contextPath}/logout"><i class='bx bx-log-out'></i> Wyloguj się</a></p>
        </nav>
    </div>

</header>
<div class="container">
    <div class="header2">
        <div class="title">Lista produktów</div>
        <div class="right">
            <form method="post">
                <p><a href="${pageContext.request.contextPath}/shop/cart"> <i class='bx bx-cart'><fmt:formatNumber
                        value="${cartProductSize}" type="number"/></i></a></p>
            </form>
        </div>
    </div>

    <button type="button" onclick="toggleFilters()">Pokaż/ukryj filtry</button>
    <c:if test="${not empty errors.param}">
        <td><span class="error">BŁĄD: ${errors.param} <br/></span></td>
    </c:if>

    <div id="filters" style="display: none;">
        <div class="filterList" id="filterList">
            <form method="post">
                <h2>Filtry:</h2>
                <input type="hidden" name="action" value="filterProducts">
                <div style="display: flex;">
                    <div style="flex: 1;">
                        <div>
                            <p class="price">Nazwa:</p>
                        </div>
                        <div>
                            <input width="400px" name="contains"/>
                        </div>
                        <div>
                            <p class="price">Minimalna cena (PLN):</p>
                        </div>
                        <div>
                            <input width="150px" type="number" name="minValue" step="0.01" min="0" max="10000">
                        </div>
                        <div>
                            <p class="price">Maksymalna cena (PLN):</p>
                        </div>
                        <div>
                            <input width="150px" type="number" name="maxValue" step="0.01" min="0" max="10000">
                        </div>
                    </div>
                    <div style="flex: 1;">
                        <div>
                            <p class="price">Kategorie:</p>
                            <c:forEach items="${categoryList}" var="productCategory">
                                <div class="checkbox-container">
                                    <input type="radio" name="category" value="${fn:escapeXml(productCategory)}">
                                    <label>${productCategory}</label>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
                <div style="text-align: center; margin-top: 20px;">
                    <button class="filterButton" name="filterButton" type="submit">Filtruj produkty</button>
                </div>
            </form>
        </div>
    </div>


    <div class="listProduct">
        <c:forEach items="${productList}" var="product">
            <div class="product">
                <h2>${product.name}</h2>
                <p class="price">Kategoria:<br/> ${product.category}</p>
                <p class="price">Cena: ${product.price} PLN</p>
                <div class="quantity">
                    <form method="post">
                        <input type="hidden" name="action" value="addToCart">
                        <input type="hidden" name="productId" value="${product.id}">
                        <p class="price">Ilość: </p>
                        <input type="number" name="quantity" value="1" min="1" max="99">
                        <button name="addToCartButton" type="submit">Dodaj do koszyka</button>
                    </form>
                </div>
            </div>
        </c:forEach>
    </div>
</div>
<script>
    function toggleFilters() {
        var filters = document.getElementById("filters");
        filters.style.display = (filters.style.display === "block") ? "none" : "block";
    }


</script>

</body>
</html>
