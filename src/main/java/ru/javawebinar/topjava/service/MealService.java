package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.inmemory.InMemoryMealRepository;

import java.util.Collection;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {

    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public void update(int userId, Meal meal) {
        checkNotFoundWithId(repository.save(userId, meal), meal.getId());
    }

    public Meal create(int userId ,Meal meal) {
        return repository.save(userId, meal);
    }

    public boolean delete(int userId, int mealId) {
        checkNotFoundWithId(repository.delete(userId, mealId), mealId);
        return true;
    }

    public Meal get(int userId, int mealId) {
        return checkNotFoundWithId(repository.get(userId, mealId), mealId);
    }

    public Collection<Meal> getAll(int userId) {
        return checkNotFoundWithId(repository.getAll(userId), userId);
    }
}