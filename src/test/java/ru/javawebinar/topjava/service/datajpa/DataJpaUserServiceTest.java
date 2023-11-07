package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.Collections;

import static ru.javawebinar.topjava.MealTestData.MEAL_MATCHER;
import static ru.javawebinar.topjava.Profiles.DATAJPA;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(DATAJPA)
public class DataJpaUserServiceTest extends AbstractUserServiceTest {

    @Test
    public void getWithMeals() {
        User user = service.getWithMeals(USER_ID);
        USER_MATCHER.assertMatch(UserTestData.user, user);
        MEAL_MATCHER.assertMatch(MealTestData.meals, user.getMeals());
    }

    @Test
    public void getWithNoMeals() {
        User user = service.getWithMeals(GUEST_ID);
        MEAL_MATCHER.assertMatch(Collections.emptyList(), user.getMeals());
    }

    @Test(expected = NotFoundException.class)
    public void getWithMealsNotFound() {
        service.getWithMeals(1);
    }
}
