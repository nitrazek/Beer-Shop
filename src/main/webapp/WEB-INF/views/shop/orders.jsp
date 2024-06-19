<%--
  Created by IntelliJ IDEA.
  User: pawel
  Date: 19.06.2024
  Time: 16:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
head>
<title>Historia zamówień</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/orders.css">
<link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel="stylesheet">
</head>
<body>
<header>
    <div class="header-content">
        <span><i class='bx bx-beer'></i> eBrowarek</span>
        <nav>
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
        <div class="title">Historia zamówień</div>
    </div>
    <ul id="orders">
        <c:forEach items="${orderList}" var="order">
            <li>
                <div class="order-header" onclick="toggleDetails(this)">${order.id}</div>
                <div class="order-details">
                    <p>${order.creationDate}</p>
                    <p>${order.totalPrice}</p>
                    <p>Ilość zamówionych produktów: ${order.orderProducts.stream().count()}</p>
                </div>
            </li>
        </c:forEach>
    </ul>
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
