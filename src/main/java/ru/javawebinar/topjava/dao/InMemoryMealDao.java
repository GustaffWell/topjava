package ru.javawebinar.topjava.dao;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.slf4j.LoggerFactory.getLogger;

public class InMemoryMealDao implements MealDao {
    private static final Logger log = getLogger(InMemoryMealDao.class);
    private AtomicInteger idCounter = new AtomicInteger(0);
    private Map<Integer, Meal> mealMap = new ConcurrentHashMap<>();

    {
        fillWithTestData();
    }

    @Override
    public List<Meal> getAll() {
        log.debug("called method getAll from MealDAO");
        return new ArrayList<>(mealMap.values());
    }

    @Override
    public Meal save(Meal meal) {
        log.debug("called method save from MealDAO");
        if (meal.getId() == null) {
            int newId = idCounter.getAndIncrement();
            Meal newMeal = new Meal(newId, meal.getDateTime(), meal.getDescription(), meal.getCalories());
            mealMap.put(newId, newMeal);
            return newMeal;
        } else {
            return mealMap.computeIfPresent(meal.getId(), (k, v) -> meal);
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

    private void fillWithTestData() {
        for (Meal meal : MealsUtil.getTestList()) {
            save(meal);
        }
    }
}
