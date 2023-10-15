<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<%--<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>--%>
<html>
<head>
    <title>Meal list</title>
    <style>
        .normal {
            color: green;
        }

        .excess {
            color: red;
        }

        .dateTimeFilterTitle {
            font-size: 12px;
        }

        .dateTimeFilter {
            display: flex;
        }

        .dateTimeFilterGroup {
            margin-right: 50px;
        }
    </style>
</head>
<body>
<section>
    <h3><a href="index.html">Home</a></h3>
    <hr/>
    <h2>Meals</h2>
    <hr>
    <form method="get" action="meals">
        <div class="dateTimeFilter">
            <div class="dateTimeFilterGroup">
                <h3 class="dateTimeFilterTitle">От даты(включая)</h3>
                <input type="date" name="startDate" value="${startDate}">
            </div>
            <div class="dateTimeFilterGroup">
                <h3 class="dateTimeFilterTitle">До даты(включая)</h3>
                <input type="date" name="endDate" value="${endDate == null ? endDate : endDate.minusDays(1)}">
            </div>
            <div class="dateTimeFilterGroup">
                <h3 class="dateTimeFilterTitle">От времени(включая)</h3>
                <input type="time" name="startTime" value="${startTime}">
            </div>
            <div class="dateTimeFilterGroup">
                <h3 class="dateTimeFilterTitle">До времени(исключая)</h3>
                <input type="time" name="endTime" value="${endTime}">
            </div>
        </div>
        <br>
        <button type="submit">Filter</button>
    </form>
    <hr>
    <a href="meals?action=create">Add Meal</a>
    <br><br>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${requestScope.meals}" var="meal">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.to.MealTo"/>
            <tr class="${meal.excess ? 'excess' : 'normal'}">
                <td>
                        <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                        <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                        <%--${fn:replace(meal.dateTime, 'T', ' ')}--%>
                        ${fn:formatDateTime(meal.dateTime)}
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?action=update&id=${meal.id}">Update</a></td>
                <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>