package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.adminMeals.forEach(meal -> save(0, meal));
        MealsUtil.userMeals.forEach(meal -> save(1, meal));
    }

    @Override
    public Meal save(int userId, Meal meal) {
        log.info("save {}", meal);
        if (meal == null)
            return null;
        repository.putIfAbsent(userId, new ConcurrentHashMap<>());
        Map<Integer, Meal> userMeals = repository.get(userId);
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
        return repository.containsKey(userId) && repository.get(userId).remove(mealId) != null;
    }

    @Override
    public Meal get(int userId, int mealId) {
        log.info("get {}", mealId);
        return repository.containsKey(userId) ? repository.get(userId).get(mealId) : null;
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        log.info("getAll {}", userId);
        return repository.containsKey(userId) ?
                repository.get(userId).values().stream()
                        .sorted((o1, o2) -> o2.getDateTime().compareTo(o1.getDateTime()))
                        .collect(Collectors.toList()) : null;
    }
}

