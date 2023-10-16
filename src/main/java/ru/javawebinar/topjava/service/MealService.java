package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

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

    public Meal create(int userId, Meal meal) {
        return repository.save(userId, meal);
    }

    public void delete(int userId, int mealId) {
        checkNotFoundWithId(repository.delete(userId, mealId), mealId);
    }

    public Meal get(int userId, int mealId) {
        return checkNotFoundWithId(repository.get(userId, mealId), mealId);
    }

    public List<Meal> getAll(int userId) {
        return repository.getAll(userId);
    }

    public List<Meal> getFiltered(int userId, LocalDate startDate, LocalDate endDate,
                                    LocalTime startTime, LocalTime endTime) {
        return repository.getFiltered(userId, startDate, endDate, startTime, endTime);
    }
}