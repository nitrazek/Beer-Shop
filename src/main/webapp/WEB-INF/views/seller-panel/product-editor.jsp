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
    <title>Dodaj/edytuj produkt</title>
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
            <p><a href="${pageContext.request.contextPath}/admin/users"><i class='bx bx-crown'></i> Panel administratora</a>
            </p>
            <p><a href=""><i class='bx bx-log-out'></i> Wyloguj się</a></p>
        </nav>
    </div>

</header>
<div class="container">
    <div class="header2">
        <div class="left">
            <div class="title">Dodaj/edytuj produkt</div>
        </div>
    </div>

    <div class="wrapper">
        <div class="input-group">
            <div class="input-field">
                <form method="post">
                    <input type="hidden" name="productId" value="${product.id}">
                    <c:if test="${not empty errors.id}">
                        <td><span class="error">BŁĄD: ${errors.id} <br/></span></td>
                    </c:if>
                    <div class="column-title">Nazwa produktu</div>
                    <input class="text-input" name="productName" value="${product.name}" type="text">
                    <c:if test="${not empty errors.name}">
                        <td><span class="error">BŁĄD: ${errors.name} <br/></span></td>
                    </c:if>
                    <div class="column-title">Kategoria</div>
                    <c:forEach items="${categoryList}" var="productCategory">
                        <div class="checkbox-container">
                            <input type="radio" name="productCategory" value="${fn:escapeXml(productCategory)}">
                            <label>${productCategory}</label>
                        </div>
                    </c:forEach>
                    <c:if test="${not empty errors.category}">
                        <td><span class="error">BŁĄD: ${errors.category} <br/></span></td>
                    </c:if>
                    <div class="column-title">Cena</div>
                    <input class="price-input" type="number" step="0.01" min="0" value="${product.price}"
                           name="productPrice"> <span
                        class="price"> zł</span>
                    <c:if test="${not empty errors.price}">
                        <td><span class="error">BŁĄD: ${errors.price} <br/></span></td>
                    </c:if>
                    <div class="footer">
                        <button>Zapisz</button>
                    </div>
                </form>
                <div class="footer"><a href="/beershop/seller/products">
                    <button>Anuluj</button>
                </a></div>
            </div>

        </div>
    </div>

</div>

</body>
</html>
