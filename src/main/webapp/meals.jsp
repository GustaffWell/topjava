<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="ru">
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<hr>
<button onclick="window.location='meals?action=insert'">Add meal</button>
<br><br>
<table border=1>
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
        <th colspan="2">Action</th>
    </tr>
    <c:forEach var="meal" items="${mealList}">
        <tr style="color:${meal.isExcess() ? 'red' : 'green'}">
            <td>${meal.dateTime.format(dateTimeFormatter)}</td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
            <td>
                <button onclick="window.location='meals?action=edit&mealId=${meal.id}'">Update</button>
            </td>
            <td>
                <button onclick="window.location='meals?action=delete&mealId=${meal.id}'">Delete</button>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
