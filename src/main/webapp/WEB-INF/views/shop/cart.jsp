<%--
  Created by IntelliJ IDEA.
  User: pawel
  Date: 10.06.2024
  Time: 22:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Koszyk</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/cart.css">
    <!--  <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel="stylesheet"> -->
</head>
<body>
<header>
    <div class="header-content">
        <span><i class='bx bx-beer'></i> eBrowarek</span>
        <nav>
            <p><a href="${pageContext.request.contextPath}/shop/orders"><i class='bx bx-list-check'></i> Historia
                zamówień</a>
            </p>
            <c:if test="${navRole != 'CLIENT'}">
                <p><a href="${pageContext.request.contextPath}/seller/products"><i class='bx bx-store'></i> Panel
                    sprzedawcy</a>
                </p>
            </c:if>
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
            <div class="title">Twój koszyk</div>
        </div>
    </div>

    <div class="cart">
        <c:forEach items="${cartProductList}" var="cartProduct">
            <div class="cart-item">
                <div class="item-info">
                    <div class="item-name">${cartProduct.product.name}</div>
                    <div class="item-price" data-price="${cartProduct.product.price}">
                        Cena: ${cartProduct.product.price}
                        PLN
                    </div>
                </div>
                <form method="post">
                    <input type="hidden" name="productId" value="${cartProduct.product.id}">
                    <button class="item-remove" name="removeProductButton">USUŃ Z KOSZYKA</button>
                </form>
                <form method="post">
                    <div class="item-quantity">
                        <input type="hidden" name="productId" value="${cartProduct.product.id}">
                        <span>Ilość:</span>
                        <input type="number" name="quantity" value=${cartProduct.amount} min="1"
                               onchange="this.form.submit()">
                    </div>
                </form>
                <div class="item-total-price">${cartProduct.amount * cartProduct.product.price} PLN</div>
            </div>
        </c:forEach>
        <div class="cart-item total">
            <div class="item-info">
                <div class="item-name total-price">RAZEM</div>
            </div>
            <div class="total-price">
                <span id="total-price">${totalPrice} PLN</span>
            </div>
        </div>
    </div>
    <c:if test="${not empty errors.param}">
        <td><span class="error">BŁĄD: ${errors.param} <br/></span></td>
    </c:if>
    <a href="${pageContext.request.contextPath}/shop/products">
        <button>Kontynuuj zakupy</button>
    </a>
    <form method="post">
        <button name="placeOrder">Złóż zamówienie</button>
    </form>

</div>

</body>
</html>

