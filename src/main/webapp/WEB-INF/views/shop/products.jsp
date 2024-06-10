<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>eBrowarek - Lista produktów</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/product.css">
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel="stylesheet">
</head>
<body>
<div class="container">
    <header>
        <div class="title">Lista produktów</div>
        <i class='bx bx-cart'><span>0</span></i>
    </header>
    <div class="listProduct">
        <c:forEach items="${productList}" var="product">
            <div class="product">
                <h2>${product.name}</h2>
                <p>Cena: <fmt:formatNumber value="${product.price}" type="number"/> PLN</p>
            </div>
        </c:forEach>
    </div>
    </div>
    </body>
</html>
