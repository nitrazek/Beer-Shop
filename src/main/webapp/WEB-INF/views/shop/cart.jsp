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

    <script>
        function updateTotal() {
            let items = document.querySelectorAll('.cart-item:not(.total)');
            let totalPrice = 0;

            items.forEach(item => {
                let priceElement = item.querySelector('.item-price');
                let quantityElement = item.querySelector('.item-quantity input');
                let itemTotalPriceElement = item.querySelector('.item-total-price');
                if (priceElement && quantityElement && itemTotalPriceElement) {
                    let price = parseFloat(priceElement.dataset.price);
                    let quantity = parseInt(quantityElement.value);
                    if (!isNaN(price) && !isNaN(quantity)) {
                        let itemTotalPrice = price * quantity;
                        totalPrice += itemTotalPrice;
                        itemTotalPriceElement.innerText = itemTotalPrice.toFixed(2) + ' PLN';
                    }
                }
            });

            document.getElementById('total-price').innerText = totalPrice.toFixed(2) + ' PLN';
        }

        function removeItem(event) {
            let item = event.target.closest('.cart-item');
            if (item) {
                item.remove();
                updateTotal();
            }
        }

        document.addEventListener('DOMContentLoaded', () => {
            document.querySelectorAll('.item-quantity input').forEach(input => {
                input.addEventListener('change', updateTotal);
            });

            document.querySelectorAll('.item-remove').forEach(button => {
                button.addEventListener('click', removeItem);
            });

            updateTotal();
        });
    </script>
</head>
<body>
<header>
    <i class='bx bx-beer'></i> eBrowarek
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
                    <div class="item-price" data-price="${cartProduct.product.price}">Cena: ${cartProduct.product.price} PLN</div>
                </div>
                <div class="item-remove">USUŃ Z KOSZYKA</div>
                <div class="item-quantity">
                    <span>Ilość:</span>
                    <input type="number" value=${cartProduct.amount} min="1">
                </div>
                <div class="item-total-price">0.00 PLN</div>
            </div>
        </c:forEach>
        <div class="cart-item total">
            <div class="item-info">
                <div class="item-name total-price">RAZEM</div>
            </div>
            <div class="total-price">
                <span id="total-price">0.00 PLN</span>
            </div>
        </div>
    </div>
    <a href="/beershop/shop/products"><button>Kontynuuj zakupy</button></a>
    <button>Złóż zamówienie</button>

</div>

</body>
</html>

