package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

//        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
//        mealsTo.forEach(System.out::println);

//        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));

//        List<UserMealWithExcess> mealsTo = filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
//        mealsTo.forEach(System.out::println);

//        List<UserMealWithExcess> mealsTo = filteredByStreamsOptional2(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
//        mealsTo.forEach(System.out::println);

        List<UserMealWithExcess> mealsTo = filteredByCyclesOptional2(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesPerDayMap = new HashMap<>();
        for (UserMeal meal : meals) {
           LocalDate mealDate = meal.getDateTime().toLocalDate();
           caloriesPerDayMap.merge(mealDate, meal.getCalories(), Integer::sum);
        }
        List<UserMealWithExcess> mealsTo = new ArrayList<>();
        for (UserMeal meal : meals) {
            LocalTime mealTime = meal.getDateTime().toLocalTime();
            if (TimeUtil.isBetweenHalfOpen(mealTime, startTime, endTime)) {
                LocalDate mealDate = meal.getDateTime().toLocalDate();
                boolean excess = caloriesPerDayMap.get(mealDate) > caloriesPerDay;
                mealsTo.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess));
            }
        }
        return mealsTo;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesPerDayMap = meals.stream()
                .collect(Collectors.toMap(m -> m.getDateTime().toLocalDate(), UserMeal::getCalories, Integer::sum));
        return meals.stream()
                .filter(m -> {
                    LocalTime mealTime = m.getDateTime().toLocalTime();
                    return TimeUtil.isBetweenHalfOpen(mealTime, startTime, endTime);
                })
                .map(m -> {
                    LocalDate mealDate = m.getDateTime().toLocalDate();
                    boolean excess = caloriesPerDayMap.get(mealDate) > caloriesPerDay;
                    return new UserMealWithExcess(m.getDateTime(), m.getDescription(), m.getCalories(), excess);
                })
                .collect(Collectors.toList());
    }
    public static List<UserMealWithExcess> filteredByStreamsOptional2(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return meals.stream()
                .collect(Collectors.groupingBy(m -> m.getDateTime().toLocalDate()))
                .values()
                .stream()
                .map(v -> {
                    boolean excess = v.stream().mapToInt(UserMeal::getCalories).sum() > caloriesPerDay;
                    return v.stream()
                            .filter(m -> TimeUtil.isBetweenHalfOpen(m.getDateTime().toLocalTime(), startTime, endTime))
                            .map(m -> new UserMealWithExcess(m.getDateTime(), m.getDescription(), m.getCalories(), excess))
                            .collect(Collectors.toList());
                })
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public static List<UserMealWithExcess> filteredByCyclesOptional2(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExcess> mealsTo = new ArrayList<>();
        Map<LocalDate, Integer> caloriesPerDayMap = new HashMap<>();
        filteredByRecursiveCycle(meals, caloriesPerDayMap, mealsTo, startTime, endTime, caloriesPerDay, 0);
        return mealsTo;
    }

    private static void filteredByRecursiveCycle(List<UserMeal> meals,
                                          Map<LocalDate, Integer> caloriesPerDayMap,
                                          List<UserMealWithExcess> mealsTo,
                                          LocalTime startTime,
                                          LocalTime endTime,
                                          int caloriesPerDay,
                                          int i) {
        UserMeal meal = meals.get(i);
        LocalDate mealDate = meal.getDateTime().toLocalDate();
        caloriesPerDayMap.merge(mealDate, meal.getCalories(), Integer::sum);
        if (i < meals.size() - 1){
            filteredByRecursiveCycle(meals, caloriesPerDayMap, mealsTo, startTime, endTime, caloriesPerDay, i + 1);
        }
        LocalTime mealTime = meal.getDateTime().toLocalTime();
        boolean excess = caloriesPerDayMap.get(mealDate) > caloriesPerDay;
        if (TimeUtil.isBetweenHalfOpen(mealTime, startTime, endTime)) {
            UserMealWithExcess userMealWithExcess = new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
            mealsTo.add(userMealWithExcess);
        }
    }
}
