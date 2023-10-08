package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.service.MealServiceImpl;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;

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
    private MealService mealService;

    public MealServlet() {
        super();
        mealService = new MealServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String forward = "";
        String action = request.getParameter("action") == null ? "" : request.getParameter("action");
        if (action.equalsIgnoreCase("delete")) {
            forward = MEAL_LIST;
            int mealId = Integer.parseInt(request.getParameter("mealId"));
            mealService.delete(mealId);
            request.setAttribute("mealList", getActualList());
        } else if (action.equalsIgnoreCase("insert")){
            forward = INSERT_OR_EDIT;
        } else if (action.equalsIgnoreCase("edit")) {
            forward = INSERT_OR_EDIT;
            int mealId = Integer.parseInt(request.getParameter("mealId"));
            Meal meal = mealService.getById(mealId);
            request.setAttribute("meal", meal);
        } else {
            forward = MEAL_LIST;
            request.setAttribute("mealList", getActualList());
        }
        request.setAttribute("dateTimeFormatter", MealsUtil.dateTimeFormatter);
        log.debug("from method doGet forward {}", forward);
        request.getRequestDispatcher(forward).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        int mealId = Integer.parseInt(request.getParameter("mealId"));
        LocalDateTime mealDateTime = LocalDateTime.parse(request.getParameter("date"));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        Meal meal = new Meal(mealId, mealDateTime, description, calories);
        mealService.saveOrUpdate(meal);
        request.setAttribute("dateTimeFormatter", MealsUtil.dateTimeFormatter);
        request.setAttribute("mealList", getActualList());
        log.debug("from method doPost forward {}", MEAL_LIST);
        request.getRequestDispatcher(MEAL_LIST).forward(request, response);
    }

    private List<MealTo> getActualList() {
        return MealsUtil.filteredByStreams(mealService.getAll(),
                LocalTime.of(0, 0),
                LocalTime.of(23, 59, 59),
                MealsUtil.caloriesPerDay);
    }
}
