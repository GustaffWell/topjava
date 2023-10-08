package ru.javawebinar.topjava.dao;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.MealServlet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.slf4j.LoggerFactory.getLogger;

public class MealInMemoryDAO implements MealDAO{
    private static final Logger log = getLogger(MealServlet.class);
    static AtomicInteger idCounter = new AtomicInteger(0);
    static Map<Integer, Meal> mealMap = new ConcurrentHashMap<>();
    static {
        fillWithTestData(mealMap);
    }

    @Override
    public List<Meal> getAll() {
        log.debug("called method getAll from MealDAO");
        return new ArrayList<>(mealMap.values());
    }

    @Override
    public void saveOrUpdate(Meal meal) {
        log.debug("called method saveOrUpdate from MealDAO");
        if (meal.getId() == -1) {
            int newId = idCounter.getAndIncrement();
            mealMap.put(newId, new Meal(newId, meal.getDateTime(), meal.getDescription(), meal.getCalories()));
        } else {
            mealMap.put(meal.getId(), meal);
        }
    }

    @Override
    public Meal getById(int id) {
        log.debug("called method getById from MealDAO");
        return mealMap.get(id);
    }

    @Override
    public void delete(int id) {
        log.debug("called method delete from MealDAO");
        mealMap.remove(id);
    }

    private static void fillWithTestData(Map<Integer, Meal> mealMap) {
        for (Meal meal : MealsUtil.getTestList()) {
            mealMap.put(idCounter.getAndIncrement(), meal);
        }
    }
}
