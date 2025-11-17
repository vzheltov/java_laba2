package ru.university;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

/**
 * Класс для вычисления математических выражений.
 * Поддерживает числа, основные арифметические операции, скобки и переменные.
 * 
 * @author Желтов Владимир
 * @version 1.0
 */
public class ExpressionEvaluator {

    /**
     * Точка входа в программу.
     * Запрашивает у пользователя выражение, находит в нем переменные,
     * запрашивает их значения и вычисляет результат.
     * 
     * @param args Аргументы командной строки (не используются).
     */
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Введите математическое выражение (например, '2 * x + (y - 3) / 2'):");
            String expressionString = scanner.nextLine();

            // 1. Находим все переменные в выражении
            Set<String> variables = findVariables(expressionString);
            Map<String, Double> variableValues = new HashMap<>();

            // 2. Запрашиваем у пользователя значения для каждой переменной
            if (!variables.isEmpty()) {
                System.out.println("Пожалуйста, введите значения для переменных:");
                for (String varName : variables) {
                    System.out.print(varName + " = ");
                    while (!scanner.hasNextDouble()) {
                        System.out.println("Ошибка: введите корректное число.");
                        System.out.print(varName + " = ");
                        scanner.next(); 
                    }
                    double value = scanner.nextDouble();
                    variableValues.put(varName, value);
                }
            }

            // 3. Создаем и вычисляем выражение с помощью exp4j
            try {
                ExpressionBuilder builder = new ExpressionBuilder(expressionString);
                if (!variables.isEmpty()) {
                    builder.variables(variables);
                }
                
                Expression expression = builder.build();
                
                
                if (!variableValues.isEmpty()) {
                    expression.setVariables(variableValues);
                }

                double result = expression.evaluate();
                System.out.println("Результат: " + result);

            } catch (IllegalArgumentException e) {
                System.err.println("Ошибка в синтаксисе выражения: " + e.getMessage());
            } catch (ArithmeticException e) {
                System.err.println("Арифметическая ошибка (например, деление на ноль): " + e.getMessage());
            }

        } catch (Exception e) {
            System.err.println("Произошла непредвиденная ошибка: " + e.getMessage());
        }
    }

    /**
     * Находит все имена переменных (последовательности букв) в строке выражения.
     * 
     * @param expressionString Строка с математическим выражением.
     * @return Множество (Set) уникальных имен переменных.
     */
    private static Set<String> findVariables(String expressionString) {
        Set<String> variables = new HashSet<>();
        Pattern pattern = Pattern.compile("[a-zA-Z]+");
        Matcher matcher = pattern.matcher(expressionString);

        while (matcher.find()) {
            variables.add(matcher.group());
        }
        return variables;
    }
}