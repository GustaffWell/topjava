package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealDao {
    List<Meal> getAll();
    Meal save(Meal meal);
    Meal getById(Integer id);
    void delete(Integer id);
}
