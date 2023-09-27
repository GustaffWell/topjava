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

        List<UserMealWithExcess> mealsTo = filteredByCyclesOptional2(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExcess> mealsTo = new ArrayList<>();
        Map<LocalDate, List<UserMeal>> map = new HashMap<>();
        for (UserMeal meal : meals) {
           LocalDate key = meal.getDateTime().toLocalDate();
           if (!map.containsKey(key)) {
               List<UserMeal> userMealsPerDay = new ArrayList<>();
               userMealsPerDay.add(meal);
               map.put(key, userMealsPerDay);
           } else {
               map.get(key).add(meal);
           }
        }
        for (List<UserMeal> mealList : map.values()) {
            int calories = 0;
            boolean excess = false;
            for (UserMeal userMeal : mealList) {
                calories += userMeal.getCalories();
            }
            if (calories > caloriesPerDay) {
                excess = true;
            }
            for (UserMeal userMeal : mealList) {
                LocalTime time = userMeal.getDateTime().toLocalTime();
                if (time.isAfter(startTime) && time.isBefore(endTime)) {
                    mealsTo.add(new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), excess));
                }
            }
        }
        return mealsTo;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return meals.stream()
                .collect(Collectors.groupingBy(m -> m.getDateTime().toLocalDate()))
                .values()
                .stream()
                .map(v -> {
                    boolean excess = v.stream().mapToInt(UserMeal::getCalories).sum() > caloriesPerDay;
                    return v.stream()
                            .filter(m -> m.getDateTime().toLocalTime().isAfter(startTime) && m.getDateTime().toLocalTime().isBefore(endTime))
                            .map(m -> new UserMealWithExcess(m.getDateTime(), m.getDescription(), m.getCalories(), excess))
                            .collect(Collectors.toList());
                })
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public static List<UserMealWithExcess> filteredByCyclesOptional2(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExcess> mealsTo = new ArrayList<>();
        Map<LocalDate, Integer> map = new HashMap<>();
        helpWithRecursion(meals, map, mealsTo, startTime, endTime, caloriesPerDay, 0);
        return mealsTo;
    }

    private static void helpWithRecursion(List<UserMeal> meals,
                                          Map<LocalDate, Integer> map,
                                          List<UserMealWithExcess> mealsTo,
                                          LocalTime startTime,
                                          LocalTime endTime,
                                          int caloriesPerDay,
                                          int i) {
        UserMeal meal = meals.get(i);
        LocalDate key = meal.getDateTime().toLocalDate();
        if (!map.containsKey(key)) {
            int calories = meal.getCalories();
            map.put(key, calories);
        } else {
            map.put(key, map.get(key) + meal.getCalories());
        }
        if (i < meals.size() - 1){
            helpWithRecursion(meals, map, mealsTo, startTime, endTime, caloriesPerDay, i + 1);
        }
        LocalTime time = meal.getDateTime().toLocalTime();
        boolean excess = map.get(key) > caloriesPerDay;
        if (time.isAfter(startTime) && time.isBefore(endTime)) {
            UserMealWithExcess userMealWithExcess = new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
            mealsTo.add(userMealWithExcess);
        }
    }
}
