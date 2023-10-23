package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal meal = service.get(USER_MEAL_1_ID, USER_ID);
        assertMatch(meal, USER_MEAL_1);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND_MEAL_ID, USER_ID));
    }

    @Test
    public void delete() {
        service.delete(USER_MEAL_1_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(USER_MEAL_1_ID, USER_ID));
    }

    @Test
    public void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND_MEAL_ID, USER_ID));
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> filteredUser1Meals = service.getBetweenInclusive(LocalDate.of(2020, 1, 30),
                LocalDate.of(2020, 1, 31), USER_ID);
        List<Meal> user1TestDataList = Stream.of(USER_MEAL_1, USER_MEAL_2, USER_MEAL_3, USER_MEAL_4,
                        USER_MEAL_5, USER_MEAL_6, USER_MEAL_7)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed()).collect(Collectors.toList());
        assertMatch(filteredUser1Meals, user1TestDataList);
    }

    @Test
    public void getAll() {
        List<Meal> allUser1Meals = service.getAll(USER_ID);
        List<Meal> user1TestDataList = Stream.of(USER_MEAL_1, USER_MEAL_2, USER_MEAL_3, USER_MEAL_4,
                        USER_MEAL_5, USER_MEAL_6, USER_MEAL_7, USER_MEAL_8)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed()).collect(Collectors.toList());
        assertMatch(allUser1Meals, user1TestDataList);
    }

    @Test
    public void update() {
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        assertMatch(service.get(USER_MEAL_1_ID, USER_ID), getUpdated());
    }

    @Test
    public void create() {
        Meal created = service.create(getNew(), USER_ID);
        Integer newId = created.getId();
        Meal newMeal = getNew();
        newMeal.setId(newId);
        assertMatch(created, newMeal);
        assertMatch(service.get(newId, USER_ID), newMeal);
    }

    @Test
    public void duplicateDateTimeCreate() {
        assertThrows(DataAccessException.class, () ->
                service.create(new Meal(USER_MEAL_1.getDateTime(), "duplicate dateTime", 500), USER_ID));
    }
}