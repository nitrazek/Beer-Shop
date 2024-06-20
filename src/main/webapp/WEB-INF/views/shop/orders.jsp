<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <title>Historia zamówień</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/orders.css">
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel="stylesheet">
</head>
<body>
<header>
    <div class="header-content">
        <span><i class='bx bx-beer'></i> eBrowarek</span>
        <nav>
            <p><a href="${pageContext.request.contextPath}/shop/products"><i class='bx bx-shopping-bag'></i> Sklep</a>
            </p>
            <c:if test="${navRole != 'CLIENT'}">
                <p><a href="${pageContext.request.contextPath}/seller/products"><i class='bx bx-store'></i> Panel
                    sprzedawcy</a></p>
            </c:if>
            <c:if test="${navRole=='ADMIN'}">
                <p><a href="${pageContext.request.contextPath}/admin/users"><i class='bx bx-crown'></i> Panel
                    administratora</a></p>
            </c:if>
            <p><a href="${pageContext.request.contextPath}/logout"><i class='bx bx-log-out'></i> Wyloguj się</a></p>
        </nav>
    </div>
</header>

<div class="container">
    <div class="header2">
        <div class="title">Historia zamówień</div>
    </div>
    <div class="orders-container">
        <c:if test="${orderList.stream().count()==0}">
            Brak zamówień
        </c:if>
        <c:forEach items="${orderList}" var="order">
            <div class="order">
                <button class="order-header" onclick="toggleDetails(this)">Zamówienie #${order.id} (${order.totalPrice}
                    PLN)
                </button>
                <div class="order-details" style="display:none;">
                    <p>Data złożenia zamówienia:
                        <c:set var="formattedDate" value="${fn:substring(order.creationDate, 0, 19)}"/>
                        <fmt:parseDate value="${formattedDate}" var="parsedDate" pattern="yyyy-MM-dd'T'HH:mm:ss"/>
                        <fmt:formatDate value="${parsedDate}" pattern="dd-MM-yyyy HH:mm:ss"/></p>
                    <p>Zamówione produkty:</p>
                    <table>
                        <thead>
                        <tr>
                            <th>Nazwa produktu</th>
                            <th>Ilość</th>
                            <th>Cena łączna</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${order.orderProducts}" var="cartProduct">
                            <tr>
                                <td>${cartProduct.product.name} (${cartProduct.product.price} PLN/szt)</td>
                                <td>${cartProduct.amount}</td>
                                <td>${cartProduct.product.price * cartProduct.amount} PLN</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </c:forEach>
    </div>
    <script>
        function toggleDetails(element) {
            var details = element.nextElementSibling;
            if (details.style.display === "none" || details.style.display === "") {
                details.style.display = "block";
            } else {
                details.style.display = "none";
            }
        }
    </script>
</div>
</body>
</html>
