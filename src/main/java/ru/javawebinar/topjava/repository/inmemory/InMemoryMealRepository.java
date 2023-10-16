package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);
    private final Comparator<Meal> dateTimeMealComparator = Comparator.comparing(Meal::getDateTime);

    {
        MealsUtil.adminMeals.forEach(meal -> save(0, meal));
        MealsUtil.userMeals.forEach(meal -> save(1, meal));
    }

    @Override
    public Meal save(int userId, Meal meal) {
        log.info("save {}", meal);

        Map<Integer, Meal> userMeals = repository.computeIfAbsent(userId, (map) -> new ConcurrentHashMap<>());
        if (meal.isNew()) {
            meal.setId(counter.getAndIncrement());
            userMeals.put(meal.getId(), meal);
            return meal;
        }
        return userMeals.computeIfPresent(meal.getId(), (mealId, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int userId, int mealId) {
        log.info("delete {}", mealId);
        Map<Integer, Meal> userMeals = repository.get(userId);
        if (!(userMeals == null)) {
            return userMeals.remove(mealId) != null;
        }
        return false;
    }

    @Override
    public Meal get(int userId, int mealId) {
        log.info("get {}", mealId);
        Map<Integer, Meal> userMeals = repository.get(userId);
        if (!(userMeals == null)) {
            return userMeals.get(mealId);
        }
        return null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("getAll {}", userId);
        Map<Integer, Meal> userMeals = repository.get(userId);
        if (userMeals == null) {
            return Collections.emptyList();
        }
        return userMeals.values().stream()
                .sorted(dateTimeMealComparator.reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<MealTo> getFiltered(int userId, LocalDate startDate, LocalDate endDate,
                                    LocalTime startTime, LocalTime endTime) {
        log.info("getFiltered {}", userId);
        return new ArrayList<>(MealsUtil.getFilteredTos(getAll(userId), SecurityUtil.authUserCaloriesPerDay(),
                startDate, endDate, startTime, endTime));
    }
}

