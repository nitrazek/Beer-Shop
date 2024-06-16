<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>eBrowarek - Lista produktów</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/product.css">
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel="stylesheet">
</head>
<body>
<header>
    <i class='bx bx-beer'></i> eBrowarek
</header>
<div class="container">
    <div class="header2">
            <div class="title">Lista produktów</div>
        <div class="right">
            <p><a href="/beershop/shop/cart"> <i class='bx bx-cart'><span>${cartProductSize}</span></i></a></p>
        </div>
    </div>

    <button type="button" onclick="toggleFilters()">Pokaż/ukryj filtry</button>

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
                            <input width="150px" type="number" name="minValue" min="0" max="100">
                        </div>
                        <div>
                            <p class="price">Maksymalna cena (PLN):</p>
                        </div>
                        <div>
                            <input width="150px" type="number" name="maxValue" min="0" max="100">
                        </div>
                    </div>
                    <div style="flex: 1;">
                        <div>
                            <p class="price">Kategorie:</p>
                            <c:forEach items="${categoryList}" var="productCategory">
                                <div class="checkbox-container">
                                    <input type="checkbox" name="category" value="${productCategory}">
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
                <p class="price">Cena: ${product.price} PLN</p>
                <div class="quantity">
                    <form method="post">
                        <input type="hidden" name="action" value="addToCart">
                        <input type="hidden" name="productId" value="${product.id}">
                        <p class="price">Ilość: </p>
                        <input type="number" name="quantity" value="1" min="1" max="99">
                        <button  name="addToCartButton" type="submit">Dodaj do koszyka</button>
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
