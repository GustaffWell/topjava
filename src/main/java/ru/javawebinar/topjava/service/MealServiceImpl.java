package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.dao.MealDAO;
import ru.javawebinar.topjava.dao.MealInMemoryDAO;
import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public class MealServiceImpl implements MealService{
    private MealDAO dao = new MealInMemoryDAO();

    @Override
    public List<Meal> getAll() {
        return dao.getAll();
    }

    @Override
    public void saveOrUpdate(Meal meal) {
        dao.saveOrUpdate(meal);
    }

    @Override
    public Meal getById(int id) {
        return dao.getById(id);
    }

    @Override
    public void delete(int id) {
        dao.delete(id);
    }
}
