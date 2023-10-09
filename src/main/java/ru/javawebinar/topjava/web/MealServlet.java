package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.dao.InMemoryMealDao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.TimeUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static final String MEAL_LIST = "meals.jsp";
    private static final String INSERT_OR_EDIT = "meal.jsp";
    private MealDao mealDao;

    @Override
    public void init() throws ServletException {
        mealDao = new InMemoryMealDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String forward = "";
        String actionParameter = request.getParameter("action");
        String action = actionParameter == null ? "" : actionParameter;
        switch (action) {
            case "delete": {
                Integer mealId = getId(request);
                mealDao.delete(mealId);
                response.sendRedirect("meals");
                return;
            }
            case "insert": {
                forward = INSERT_OR_EDIT;
                LocalDateTime currentDateTime = LocalDateTime.now();
                request.setAttribute("currentDateTime", LocalDateTime.of(currentDateTime.getYear(),
                        currentDateTime.getMonth(),
                        currentDateTime.getDayOfMonth(),
                        currentDateTime.getHour(),
                        currentDateTime.getMinute()));
                break;
            }
            case "edit": {
                forward = INSERT_OR_EDIT;
                Integer mealId = getId(request);
                Meal meal = mealDao.getById(mealId);
                request.setAttribute("meal", meal);
                break;
            }
            default: {
                forward = MEAL_LIST;
                request.setAttribute("mealList", getActualList());
                request.setAttribute("dateTimeFormatter", TimeUtil.DATE_TIME_FORMATTER);
            }
        }
        log.debug("from method doGet forward {}", forward);
        request.getRequestDispatcher(forward).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Integer mealId = getId(request);
        LocalDateTime mealDateTime = LocalDateTime.parse(request.getParameter("date"));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        Meal meal = new Meal(mealId, mealDateTime, description, calories);
        mealDao.save(meal);
        log.debug("from method doPost forward {}", MEAL_LIST);
        response.sendRedirect("meals");
    }

    private List<MealTo> getActualList() {
        return MealsUtil.filteredByStreams(mealDao.getAll(), LocalTime.MIN, LocalTime.MAX, MealsUtil.CALORIES_PER_DAY);
    }

    private Integer getId(HttpServletRequest request) {
        String idString = request.getParameter("mealId");
        return idString.equals("") ? null : Integer.parseInt(idString);
    }
}
