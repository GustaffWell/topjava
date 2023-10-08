<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Edit meal</title>
</head>
<body>
<form method="post" action="meals" name="frmAddMeal">
    <input type="hidden" name="mealId" value="${meal.getId() == null ? -1 : meal.getId()}">
    <br><br>
    DateTime <input type="datetime-local" name="date" value="${meal.getDateTime()}">
    <br><br>
    Description <input type="text" name="description" value="${meal.getDescription()}">
    <br><br>
    Calories <input type="number" name="calories" value="${meal.getCalories()}">
    <br><br>
    <input type="submit" value="Save"> <input type="button" value="Cancel" onclick="window.location='meals'">
</form>

</body>
</html>
