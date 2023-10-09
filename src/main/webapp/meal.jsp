<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Edit meal</title>
</head>
<body>
<h2>${param.action == "insert" ? "Add meal" : "Edit meal"}</h2>
<form method="post" action="meals" name="frmAddMeal">
    <input type="hidden" name="mealId" value="${meal.id}">
    <br><br>
    DateTime <input type="datetime-local" name="date" value="${param.action == "insert"? currentDateTime : meal.dateTime}">
    <br><br>
    Description <input type="text" name="description" value="${meal.description}">
    <br><br>
    Calories <input type="number" name="calories" value="${meal.calories}">
    <br><br>
    <input type="submit" value="Save"> <input type="button" value="Cancel" onclick="window.location='meals'">
</form>
</body>
</html>
