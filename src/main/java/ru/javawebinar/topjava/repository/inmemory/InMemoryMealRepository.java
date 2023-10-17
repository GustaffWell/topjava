package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);
    private final Comparator<Meal> dateTimeMealComparator = Comparator.comparing(Meal::getDateTime).reversed();

    {
        MealsUtil.adminMeals.forEach(meal -> save(1, meal));
        MealsUtil.userMeals.forEach(meal -> save(2, meal));
    }

    @Override
    public Meal save(int userId, Meal meal) {
        log.info("save {}", meal);

        Map<Integer, Meal> userMeals = repository.computeIfAbsent(userId, newUserId -> new ConcurrentHashMap<>());
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
        return userMeals != null && userMeals.remove(mealId) != null;
    }

    @Override
    public Meal get(int userId, int mealId) {
        log.info("get {}", mealId);
        Map<Integer, Meal> userMeals = repository.get(userId);
        return userMeals != null ? userMeals.get(mealId) : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("getAll {}", userId);
        return filteredByPredicate(userId, meal -> true);
    }

    @Override
    public List<Meal> getFiltered(int userId, LocalDate startDate, LocalDate endDate) {
        log.info("getFiltered {}", userId);
        return filteredByPredicate(userId, meal -> DateTimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalDate(),
                startDate, endDate));
    }

    private List<Meal> filteredByPredicate(int userId, Predicate<Meal> filter) {
        Map<Integer, Meal> userMeals = repository.get(userId);
        return userMeals == null ? Collections.emptyList() : userMeals.values().stream()
                .filter(filter)
                .sorted(dateTimeMealComparator)
                .collect(Collectors.toList());
    }
}

