package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealDAO {
    public List<Meal> getAll();
    public void saveOrUpdate(Meal meal);
    public Meal getById(int id);
    public void delete(int id);
}
