package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;

public class MealToTestData {
    public static final MatcherFactory.Matcher<MealTo> MEAL_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(MealTo.class);

    public static final MealTo mealTo1 = toMealTo(meal1, false);
    public static final MealTo mealTo2 = toMealTo(meal2, false);
    public static final MealTo mealTo3 = toMealTo(meal3, false);
    public static final MealTo mealTo4 = toMealTo(meal4, true);
    public static final MealTo mealTo5 = toMealTo(meal5, true);
    public static final MealTo mealTo6 = toMealTo(meal6, true);
    public static final MealTo mealTo7 = toMealTo(meal7, true);

    public static final List<MealTo> mealTos = List.of(mealTo7, mealTo6, mealTo5, mealTo4, mealTo3, mealTo2, mealTo1);
    public static final List<MealTo> mealFilteredTos = List.of(mealTo6, mealTo5, mealTo4, mealTo2, mealTo1);

    private static MealTo toMealTo(Meal meal, boolean excess) {
        return new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }
}
