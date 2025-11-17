package calculator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Комплексный тестовый класс для проверки функциональности класса {@link Calculator}.
 */
public class CalculatorTest
{
    @Test
    void testSimpleExpressions()
    {
        assertEquals(7.0, new Calculator("7").getRes());
        assertEquals(12.0, new Calculator("4+8").getRes());
        assertEquals(3.0, new Calculator("9-6").getRes());
        assertEquals(24.0, new Calculator("6*4").getRes());
        assertEquals(5.0, new Calculator("15/3").getRes());
        assertEquals(27.0, new Calculator("3^3").getRes());
    }

    @Test
    void testOperatorPrecedence()
    {
        assertEquals(17.0, new Calculator("3+2*7").getRes());
        assertEquals(35.0, new Calculator("(4+3)*5").getRes());
        assertEquals(22.0, new Calculator("3*4+5*2").getRes());
        assertEquals(2.0, new Calculator("12-2*5").getRes());
        assertEquals(36.0, new Calculator("3^2*4").getRes());
    }

    @Test
    void testUnaryMinus()
    {
        assertEquals(-8.0, new Calculator("-8").getRes());
        assertEquals(2.0, new Calculator("-5+7").getRes());
        assertEquals(-15.0, new Calculator("3*-5").getRes());
        assertEquals(10.0, new Calculator("(-3+5)*5").getRes());
    }

    @Test
    void testParentheses()
    {
        assertEquals(28.0, new Calculator("(3+4)*4").getRes());
        assertEquals(18.0, new Calculator("(2+4)*(5-2)").getRes());
        assertEquals(8.0, new Calculator("((8))").getRes());
        assertEquals(15.0, new Calculator("(2+(3+(4+6)))").getRes());
        assertEquals(30.0, new Calculator("3*(4+(2*3))").getRes());
    }

    @Test
    void testComplexExpressions()
    {
        assertEquals(7.5, new Calculator("3+4*2-7/2").getRes());
        assertEquals(512.0, new Calculator("2^3^2").getRes());
        assertEquals(12.0, new Calculator("16/4*3").getRes());
        assertEquals(6.25, new Calculator("2.5*2.5").getRes());
    }

    @Test
    void testDecimalNumbers()
    {
        assertEquals(7.5, new Calculator("3.2+4.3").getRes());
        assertEquals(3.0, new Calculator("1.5*2").getRes());
        assertEquals(2.5, new Calculator("7.5/3.0").getRes());
    }

    @Test
    void testSpacesInExpression()
    {
        assertEquals(17.0, new Calculator("3 + 2 * 7").getRes());
        assertEquals(28.0, new Calculator("( 3 + 4 ) * 4").getRes());
        assertEquals(64.0, new Calculator(" 4 ^ 3 ").getRes());
    }

    @Test
    void testEdgeCases()
    {
        assertEquals(0.0, new Calculator("0").getRes());
        assertEquals(8.0, new Calculator("0+8").getRes());
        assertEquals(8.0, new Calculator("8+0").getRes());
        assertEquals(4.0, new Calculator("2*2").getRes());
        assertEquals(1.0, new Calculator("3^0").getRes());
    }

    @Test
    void testDivisionByZero()
    {
        assertThrows(ArithmeticException.class, () -> {
            new Calculator("8/0").getRes();
        });
        assertThrows(ArithmeticException.class, () -> {
            new Calculator("|5/0|").getRes();
        });
        assertThrows(ArithmeticException.class, () -> {
            new Calculator("|0/0|").getRes();
        });
    }

    @Test
    void testInvalidCharacters()
    {
        assertThrows(IllegalArgumentException.class, () -> {
            new Calculator("4@2");
        });
    }

    @Test
    void testMultipleDotsInNumber()
    {
        assertThrows(IllegalArgumentException.class, () -> {
            new Calculator("3.2.1");
        });
    }

    @Test
    void testUnbalancedParentheses()
    {
        assertThrows(IllegalArgumentException.class, () -> {
            new Calculator("(3+5");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Calculator("4+2)");
        });
    }

    @Test
    void testEmptyExpression()
    {
        assertThrows(IllegalArgumentException.class, () -> {
            new Calculator("");
        });
    }

    @Test
    void testPowerOperations()
    {
        assertEquals(16.0, new Calculator("2^4").getRes());
        assertEquals(256.0, new Calculator("4^4").getRes());
        assertEquals(1.0, new Calculator("7^0").getRes());
        assertEquals(0.125, new Calculator("2^-3").getRes());
    }

    @Test
    void testAbsoluteValue()
    {
        assertEquals(7.0, new Calculator("|7|").getRes());
        assertEquals(7.0, new Calculator("|-7|").getRes());
        assertEquals(0.0, new Calculator("|0|").getRes());
        assertEquals(2.71, new Calculator("|-2.71|").getRes());
        assertEquals(2.71, new Calculator("|2.71|").getRes());
    }

    @Test
    void testAbsoluteValueWithOperations()
    {
        assertEquals(4.0, new Calculator("|3-7|").getRes());
        assertEquals(9.0, new Calculator("|4+5|").getRes());
        assertEquals(12.0, new Calculator("|3*4|").getRes());
        assertEquals(3.0, new Calculator("|12/4|").getRes());
        assertEquals(27.0, new Calculator("|-3^3|").getRes());
    }

    @Test
    void testAbsoluteValueWithParentheses()
    {
        assertEquals(4.0, new Calculator("|(3-7)|").getRes());
        assertEquals(21.0, new Calculator("|(2+5)*3|").getRes());
        assertEquals(8.0, new Calculator("||-8||").getRes());
        assertEquals(9.0, new Calculator("3*|2-5|").getRes());
    }

    @Test
    void testAbsoluteValueComplexExpressions()
    {
        assertEquals(8.0, new Calculator("|3-8| + |4-1|").getRes());
        assertEquals(5.0, new Calculator("||-5||").getRes());
        assertEquals(24.0, new Calculator("|4-10| * |2+2|").getRes());
    }

    @Test
    void testAbsoluteValuePrecedence()
    {
        assertEquals(12.0, new Calculator("|3-7| * 3").getRes());
        assertEquals(2.0, new Calculator("|5-7| + 0").getRes());
        assertEquals(24.0, new Calculator("|4-10| * |2+2|").getRes());
    }

    @Test
    void testAbsoluteValueWithUnaryMinus()
    {
        assertEquals(-8.0, new Calculator("-|8|").getRes());
        assertEquals(-8.0, new Calculator("-|-8|").getRes());
        assertEquals(8.0, new Calculator("|-|-8||").getRes());
        assertEquals(-4.0, new Calculator("-|5-9|").getRes());
        assertEquals(12.0, new Calculator("|-|-6|-6|").getRes());
    }

    @Test
    void testValueWithUnaryMinus()
    {
        assertEquals(-9.0, new Calculator("-(9)").getRes());
        assertEquals(9.0, new Calculator("-(-9)").getRes());
        assertEquals(9.0, new Calculator("(-(-9))").getRes());
        assertEquals(4.0, new Calculator("-(3-7)").getRes());
    }

    @Test
    void testMixedAbsoluteOperations()
    {
        assertEquals(13.0, new Calculator("|3-7| + |4+5|").getRes());
        assertEquals(3.0, new Calculator("|5-9| - |2-3|").getRes());
        assertEquals(24.0, new Calculator("|4-8| * |3+3|").getRes());
        assertEquals(4.0, new Calculator("|12-4| / |1+1|").getRes());
    }

    @Test
    void testInvalidUnaryOperators()
    {
        assertThrows(IllegalArgumentException.class, () -> {
            new Calculator("++5");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Calculator("+6");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Calculator("/7");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Calculator("*8");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Calculator("^9");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Calculator("3++4");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Calculator("3**4");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Calculator("5//2");
        });
    }

    @Test
    void testInvalidOperatorCombinations()
    {
        assertThrows(IllegalArgumentException.class, () -> {
            new Calculator("3+*4");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Calculator("5-+3");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Calculator("4*/2");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Calculator("3^/2");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Calculator("2^!3");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Calculator("4+");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Calculator("7-");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Calculator("6*");
        });
    }

    @Test
    void testEmptyOperators()
    {
        assertThrows(IllegalArgumentException.class, () -> {
            new Calculator("+");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Calculator("*");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Calculator("/");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Calculator("++");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Calculator("+-");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Calculator("*/");
        });
    }

    @Test
    void testInvalidFactorialPosition()
    {
        assertThrows(IllegalArgumentException.class, () -> {
            new Calculator("!4");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Calculator("+!4");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Calculator("!");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Calculator("(!4)");
        });
    }

    @Test
    void testFactorial()
    {
        assertEquals(1.0, new Calculator("0!").getRes());
        assertEquals(1.0, new Calculator("1!").getRes());
        assertEquals(2.0, new Calculator("2!").getRes());
        assertEquals(6.0, new Calculator("3!").getRes());
        assertEquals(24.0, new Calculator("4!").getRes());
        assertEquals(120.0, new Calculator("5!").getRes());
    }

    @Test
    void testFactorialWithOperations()
    {
        assertEquals(27.0, new Calculator("4! + 3").getRes());
        assertEquals(18.0, new Calculator("3! * 3").getRes());
        assertEquals(30.0, new Calculator("4! + 3!").getRes());
        assertEquals(64.0, new Calculator("2^3!").getRes());
    }

    @Test
    void testFactorialWithParentheses()
    {
        assertEquals(720.0, new Calculator("(2+4)!").getRes());
        assertEquals(24.0, new Calculator("(1+3)!").getRes());
    }

    @Test
    void testFactorialInvalidCases()
    {
        assertThrows(IllegalArgumentException.class, () -> {
            new Calculator("(-2)!").getRes();
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Calculator("3.5!").getRes();
        });
    }

    @Test
    void testMixedFactorialAndAbsolute()
    {
        assertEquals(6.0, new Calculator("|-3|!").getRes());
        assertEquals(24.0, new Calculator("|3-7|!").getRes());
        assertEquals(1.0, new Calculator("|2-4|! / 2").getRes());
    }

    @Test
    void testDoubleNumbersBasic()
    {
        assertEquals(4.2, new Calculator("2.1 + 2.1").getRes());
        assertEquals(0.75, new Calculator("2.25 / 3.0").getRes());
        assertEquals(3.75, new Calculator("1.25 * 3.0").getRes());
        assertEquals(-0.8, new Calculator("1.2 - 2.0").getRes());
    }

    @Test
    void testDoublePrecision()
    {
        assertEquals(0.3, new Calculator("0.15 + 0.15").getRes(), 0.0000001);
        assertEquals(0.25, new Calculator("1.0 / 4.0").getRes(), 0.0000001);
        assertEquals(0.3333333333, new Calculator("1.0 / 3.0").getRes(), 0.0000001);
    }

    @Test
    void testLargeDoubleNumbers()
    {
        assertEquals(20000000000.0, new Calculator("20000000000.0").getRes());
        assertEquals(40000000000.0, new Calculator("20000000000.0 * 2.0").getRes());
        assertEquals(0.0000000008, new Calculator("0.0000000008").getRes());
    }

    @Test
    void testDoubleWithOperations()
    {
        assertEquals(6.28318, new Calculator("3.14159 * 2.0").getRes(), 0.00001);
        assertEquals(4.2, new Calculator("10.5 / 2.5").getRes());
        assertEquals(8.75, new Calculator("2.5 * 3.5").getRes());
        assertEquals(5.25, new Calculator("7.5 - 2.25").getRes());
    }

    @Test
    void testDoubleWithParentheses()
    {
        assertEquals(12.0, new Calculator("(2.5 + 3.5) * 2.0").getRes());
        assertEquals(9.0, new Calculator("1.5 + (2.5 * 3.0)").getRes());
        assertEquals(5.5, new Calculator("(2.0 + 3.5) * (1.5 - 0.5)").getRes());
    }

    @Test
    void testDoubleWithPower()
    {
        assertEquals(6.25, new Calculator("2.5 ^ 2.0").getRes(), 0.0000001);
        assertEquals(3.375, new Calculator("1.5 ^ 3.0").getRes(), 0.0000001);
        assertEquals(0.01, new Calculator("10.0 ^ -2.0").getRes(), 0.0000001);
    }

    @Test
    void testDoublePrecisionLimits()
    {
        double result1 = new Calculator("0.15 + 0.15").getRes();
        assertEquals(0.3, result1, 0.0000000001);

        double result2 = new Calculator("2.0 - 1.9").getRes();
        assertEquals(0.1, result2, 0.0000000001);

        double result3 = new Calculator("1.0 / 8.0").getRes();
        assertEquals(0.125, result3, 0.0000000001);
    }

    @Test
    void testDoubleWithUnaryMinus()
    {
        assertEquals(-2.71, new Calculator("-2.71").getRes());
        assertEquals(1.86, new Calculator("-1.14 + 3.0").getRes());
        assertEquals(-7.5, new Calculator("2.5 * -3.0").getRes());
        assertEquals(-2.8, new Calculator("-(1.3 + 1.5)").getRes());
    }

    @Test
    void testComplexDoubleExpressions()
    {
        assertEquals(46.0, new Calculator("(2.5 + 3.5) * 2.0 ^ 3.0 - 4.2 / 2.1").getRes(), 0.0000001);
        assertEquals(10.5, new Calculator("|-3.5| * (1.5 + 1.5) / 1.0").getRes(), 0.0000001);
    }

    // Новые тесты для сложных вложенных выражений
    @Test
    void testComplexNestedExpressions() {
        assertEquals(54.0, new Calculator("2*(3+(4*(5+1)))").getRes());
        assertEquals(100.0, new Calculator("((10+5)*2)^2/9").getRes());
        assertEquals(13.0, new Calculator("|3-10| + (2*|1-4|)").getRes());
    }

    @Test
    void testMixedOperationsWithFunctions() {
        assertEquals(1.0, new Calculator("exp(0) * log(2.71828)").getRes(), 0.0001);
        assertEquals(2.0, new Calculator("tg(0.7854) + ctg(0.7854)").getRes(), 0.0001);
    }

    @Test
    void testAdvancedVariableExpressions() {
        Calculator calculator = new Calculator("(x^2 + y^2) / z");
        calculator.setVariable("x", 3.0);
        calculator.setVariable("y", 4.0);
        calculator.setVariable("z", 5.0);
        assertEquals(5.0, calculator.getRes());
    }

    @Test
    void testMultipleNestedFunctions() {
        assertEquals(0.0, new Calculator("sin(cos(1.5708))").getRes(), 0.0001);
        assertEquals(1.0, new Calculator("exp(log(5)) / 5").getRes(), 0.0001);
    }

    @Test
    void testExpressionWithAllFeatures() {
        Calculator calculator = new Calculator("(|x - y|! + sin(z)) * exp(w) / log(v)");
        calculator.setVariable("x", 5.0);
        calculator.setVariable("y", 2.0);
        calculator.setVariable("z", 1.5708);
        calculator.setVariable("w", 0.0);
        calculator.setVariable("v", 2.71828);
        assertEquals(7.0, calculator.getRes(), 0.1);
    }

    // Остальные тесты с переменными (обновленные выражения)
    @Test
    void testSimpleVariableExpression()
    {
        Calculator calculator = new Calculator("x + y");
        calculator.setVariable("x", 7.0);
        calculator.setVariable("y", 5.0);
        assertEquals(12.0, calculator.getRes());
    }

    @Test
    void testVariableWithConstants()
    {
        Calculator calculator = new Calculator("a * 3 + b");
        calculator.setVariable("a", 5.0);
        calculator.setVariable("b", 2.0);
        assertEquals(17.0, calculator.getRes());
    }

    @Test
    void testVariableInComplexExpression() {
        Calculator calculator = new Calculator("(m + n) * p");
        calculator.setVariable("m", 3.0);
        calculator.setVariable("n", 4.0);
        calculator.setVariable("p", 5.0);
        assertEquals(35.0, calculator.getRes());
    }

    @Test
    void testVariableWithFunctions() {
        Calculator calculator = new Calculator("sin(x) + cos(y)");
        calculator.setVariable("x", 0.0);
        calculator.setVariable("y", 0.0);
        assertEquals(1.0, calculator.getRes(), 0.0001);
    }

    @Test
    void testMultipleSameVariable()
    {
        Calculator calculator = new Calculator("a * a + 2*a");
        calculator.setVariable("a", 4.0);
        assertEquals(24.0, calculator.getRes());
    }

    @Test
    void testVariableWithPower()
    {
        Calculator solver = new Calculator("x^2 + y^3");
        solver.setVariable("x", 2.0);
        solver.setVariable("y", 3.0);
        assertEquals(31.0, solver.getRes());
    }

    @Test
    void testVariableWithAbsoluteValue() {
        Calculator calculator = new Calculator("|a| + |b|");
        calculator.setVariable("a", -6.0);
        calculator.setVariable("b", 4.0);
        assertEquals(10.0, calculator.getRes());
    }

    @Test
    void testVariableWithFactorial()
    {
        Calculator calculator = new Calculator("n! + m! + n");
        calculator.setVariable("n", 3.0);
        calculator.setVariable("m", 2.0);
        assertEquals(11.0, calculator.getRes());
    }

    @Test
    void testNegativeVariables()
    {
        Calculator calculator = new Calculator("p + q");
        calculator.setVariable("p", -7.0);
        calculator.setVariable("q", -3.0);
        assertEquals(-10.0, calculator.getRes());
    }

    @Test
    void testDecimalVariables()
    {
        Calculator calculator = new Calculator("x * y");
        calculator.setVariable("x", 3.5);
        calculator.setVariable("y", 2.5);
        assertEquals(8.75, calculator.getRes());
    }

    @Test
    void testVariablePrecedence()
    {
        Calculator calculator = new Calculator("a + b * c");
        calculator.setVariable("a", 2.0);
        calculator.setVariable("b", 3.0);
        calculator.setVariable("c", 4.0);
        assertEquals(14.0, calculator.getRes());
    }

    @Test
    void testVariableWithParentheses() {
        Calculator calculator = new Calculator("(x + y) * (z - w)");
        calculator.setVariable("x", 6.0);
        calculator.setVariable("y", 4.0);
        calculator.setVariable("z", 8.0);
        calculator.setVariable("w", 3.0);
        assertEquals(50.0, calculator.getRes());
    }

    @Test
    void testSingleVariable()
    {
        Calculator calculator = new Calculator("value");
        calculator.setVariable("value", 55.0);
        assertEquals(55.0, calculator.getRes());
    }

    @Test
    void testVariableReuse()
    {
        Calculator calculator = new Calculator("x + x + x + x");
        calculator.setVariable("x", 5.0);
        assertEquals(20.0, calculator.getRes());
    }

    @Test
    void testVariableUpdate()
    {
        Calculator calculator = new Calculator("var + 2");
        calculator.setVariable("var", 8.0);
        assertEquals(10.0, calculator.getRes());

        calculator.setVariable("var", 12.0);
        assertEquals(14.0, calculator.getRes());
    }

    @Test
    void testComplexVariableExpression() {
        Calculator calculator = new Calculator("a * (b + c) - d / e");
        calculator.setVariable("a", 3.0);
        calculator.setVariable("b", 4.0);
        calculator.setVariable("c", 5.0);
        calculator.setVariable("d", 18.0);
        calculator.setVariable("e", 2.0);
        assertEquals(18.0, calculator.getRes());
    }

    @Test
    void testVariableInNestedFunctions() {
        Calculator calculator = new Calculator("sin(angle) * cos(angle)");
        calculator.setVariable("angle", 0.7854);
        assertEquals(0.5, calculator.getRes(), 0.0001);
    }

    @Test
    void testMultipleVariablesWithOperations()
    {
        Calculator calculator = new Calculator("p + q - r * s / t");
        calculator.setVariable("p", 15.0);
        calculator.setVariable("q", 5.0);
        calculator.setVariable("r", 6.0);
        calculator.setVariable("s", 2.0);
        calculator.setVariable("t", 3.0);
        assertEquals(16.0, calculator.getRes());
    }

    @Test
    void testVariableWithExponentiation()
    {
        Calculator calculator = new Calculator("num^power");
        calculator.setVariable("num", 4.0);
        calculator.setVariable("power", 3.0);
        assertEquals(64.0, calculator.getRes());
    }

    @Test
    void testBulkVariablesSetting()
    {
        Calculator calculator = new Calculator("principal * rate * time");
        Map<String, Double> vars = new HashMap<>();
        vars.put("principal", 2000.0);
        vars.put("rate", 0.06);
        vars.put("time", 3.0);
        calculator.setVariables(vars);
        assertEquals(360.0, calculator.getRes());
    }

    @Test
    void testVariableWithUnaryMinus()
    {
        Calculator calculator = new Calculator("- a + b");
        calculator.setVariable("a", 8.0);
        calculator.setVariable("b", 15.0);
        assertEquals(7.0, calculator.getRes());
    }

    @Test
    void testPhysicsFormulaWithVariables()
    {
        // Кинетическая энергия: (1/2)mv²
        Calculator calculator = new Calculator("0.5 * m * velocity^2");
        calculator.setVariable("m", 10.0);
        calculator.setVariable("velocity", 5.0);
        assertEquals(125.0, calculator.getRes());
    }

    @Test
    void testGeometryFormulaWithVariables()
    {
        // Площадь прямоугольника
        Calculator calculator = new Calculator("length * width");
        calculator.setVariable("length", 7.0);
        calculator.setVariable("width", 4.0);
        assertEquals(28.0, calculator.getRes());
    }

    @Test
    void testFinancialFormulaWithVariables()
    {
        Calculator solver = new Calculator("P * r * t");
        solver.setVariable("P", 1000.0);
        solver.setVariable("r", 0.08);
        solver.setVariable("t", 5.0);
        assertEquals(400.0, solver.getRes());
    }
}