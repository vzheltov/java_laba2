package ru.university;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

/**
 * Тесты для проверки корректности вычисления выражений.
 */
class ExpressionEvaluatorTest {

    private static final double DELTA = 1e-9; 

    @Test
    void testSimpleArithmetic() {
        Expression exp = new ExpressionBuilder("2 + 3 * 4 - 8 / 2").build();
        assertEquals(2.0 + 3.0 * 4.0 - 8.0 / 2.0, exp.evaluate(), DELTA); // 2 + 12 - 4 = 10
    }

    @Test
    void testWithParentheses() {
        Expression exp = new ExpressionBuilder("(2 + 3) * (10 - 6)").build();
        assertEquals(20.0, exp.evaluate(), DELTA); // 5 * 4 = 20
    }

    @Test
    void testWithVariables() {
        Expression exp = new ExpressionBuilder("a * b - c")
            .variables("a", "b", "c")
            .build();
            
        exp.setVariable("a", 5);
        exp.setVariable("b", 4);
        exp.setVariable("c", 10);
        
        assertEquals(10.0, exp.evaluate(), DELTA); 
    }

    @Test
    void testInvalidExpressionThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ExpressionBuilder("3 * * 2").build().evaluate();
        });
    }

    @Test
    void testDivisionByZeroThrowsException() {

        Expression exp = new ExpressionBuilder("10 / a").variable("a").build();
        exp.setVariable("a", 0);

        assertThrows(ArithmeticException.class, () -> {
            exp.evaluate();
        });
    }
}