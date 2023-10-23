package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int USER_MEAL_1_ID = START_SEQ + 3;
    public static final int USER_ID = START_SEQ;
    public static final int NOT_FOUND_MEAL_ID = 10;

    public static final Meal USER_MEAL_1 = new Meal(USER_MEAL_1_ID,
            LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0),
            "Завтрак", 500);
    public static final Meal USER_MEAL_2 = new Meal(USER_MEAL_1_ID + 1,
            LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0),
            "Обед", 1000);
    public static final Meal USER_MEAL_3 = new Meal(USER_MEAL_1_ID + 2,
            LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0),
            "Ужин", 500);
    public static final Meal USER_MEAL_4 = new Meal(USER_MEAL_1_ID + 3,
            LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0),
            "Еда на граничное значение", 100);
    public static final Meal USER_MEAL_5 = new Meal(USER_MEAL_1_ID + 4,
            LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0),
            "Завтрак", 1000);
    public static final Meal USER_MEAL_6 = new Meal(USER_MEAL_1_ID + 5,
            LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0),
            "Обед", 500);
    public static final Meal USER_MEAL_7 = new Meal(USER_MEAL_1_ID + 6,
            LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0),
            "Ужин", 410);
    public static final Meal USER_MEAL_8 = new Meal(USER_MEAL_1_ID + 7,
            LocalDateTime.of(2023, Month.JANUARY, 31, 20, 0),
            "Ужин", 410);

    public static Meal getNew() {
        return new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "new meal", 500);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(USER_MEAL_1);
        updated.setCalories(100);
        updated.setDescription("update");
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }
}
