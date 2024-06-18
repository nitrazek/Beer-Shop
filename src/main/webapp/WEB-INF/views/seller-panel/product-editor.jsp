<%--
  Created by IntelliJ IDEA.
  User: pawel
  Date: 15.06.2024
  Time: 17:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Dodaj/edytuj produkt</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/editor.css">
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel="stylesheet">
</head>
<body>
<header>
    <i class='bx bx-beer'></i> eBrowarek
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
                <div class="column-title">Nazwa produktu</div>
                <input class="text-input" type="text">
                <div class="column-title">Kategoria</div>
                <select class="text-input">
                    <option value="piwo">Piwo</option>
                    <option value="wino">Wino</option>
                    <option value="wódka">Wódka</option>
                    <option value="likier">Likier</option>
                    <option value="inne">Inne</option>
                </select>
                <div class="column-title">Cena</div>
                <input class="price-input" type="number" step="0.01" min="0">  <span class="price"> zł</span>
                <div class="footer"><button>Zapisz</button></div>
                <div class="footer"><button>Anuluj</button></div>
            </div>
        </div>
    </div>

</div>

</body>
</html>
