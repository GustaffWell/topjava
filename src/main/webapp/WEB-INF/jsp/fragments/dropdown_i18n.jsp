<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="dropdown">
    <a class="dropdown-toggle nav-link my-1 ml-2" data-toggle="dropdown" style="color: cadetblue">${pageContext.response.locale}</a>
    <div class="dropdown-menu">
        <a class="dropdown-item" href="${requestScope['javax.servlet.forward.request_uri']}?lang=en">English</a>
        <a class="dropdown-item" href="${requestScope['javax.servlet.forward.request_uri']}?lang=ru">Русский</a>
    </div>
</div>
