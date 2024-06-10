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
        <div class="left">
            <div class="title">Lista produktów</div>
        </div>
        <div class="right">
            <a href=""> <i class='bx bx-cart'><span>${cartProductSize}</span></i></a>
        </div>
    </div>

    <div class="listProduct">
        <c:forEach items="${productList}" var="product">
            <div class="product">
                <h2>${product.name}</h2>
                <p class="price">Cena: ${product.price} zł</p>
                <div class="quantity">
                        <input type="hidden" name="productId" value="${product.id}">
                        <input type="number" name="quantity" value="1" min="1" max="10">
                </div>
                <button>Dodaj do koszyka</button>
            </div>
        </c:forEach>
    </div>
</div>
</body>
</html>
