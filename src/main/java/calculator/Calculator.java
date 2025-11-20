package calculator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Вычислитель математических выражений с поддержкой различных операций и функций.
 *
 * <p>Класс предназначен для вычисления значений математических выражений, включая:
 * <ul>
 *   <li>Базовые арифметические действия: сложение (+), вычитание (-), умножение (*), деление (/)</li>
 *   <li>Возведение в степень (^) и вычисление факториала (!)</li>
 *   <li>Тригонометрические функции: sin, cos, tg, ctg</li>
 *   <li>Экспоненциальные и логарифмические функции: exp, log</li>
 *   <li>Выражения со скобками () и модулем ||</li>
 *   <li>Работу с переменными (буквенными обозначениями: a, b, x, y и др.)</li>
 * </ul>
 *
 * <p><b>Примеры вычислений:</b>
 * <pre>
 * {@code
 * // Простое арифметическое выражение
 * Calculator calc1 = new Calculator("2 + 3 * 4");
 * double res1 = calc1.getRes(); // Результат: 14.0
 *
 * // Выражение с математическими функциями
 * Calculator calc2 = new Calculator("sin(0) + cos(0)");
 * double res2 = calc2.getRes(); // Результат: 1.0
 *
 * // Выражение с переменными
 * Calculator calc3 = new Calculator("a + (b * 2)");
 * calc3.setVariable("a", 5.0);
 * calc3.setVariable("b", 2.0);
 * double res3 = calc3.getRes(); // Результат: 9.0
 * }
 * </pre>
 *
 * @author Анастасия
 * @version 1.0
 */
public class Calculator
{
    /** Оригинальная строка выражения */
    private String expressionString;

    /** Список чисел в выражении */
    private List<String> numbers;

    /** Список операторов и функций в выражении */
    private List<String> operators;

    /** Карта значений переменных */
    private Map<String, Double> variables;

    /** Приоритет операторов */
    private static final Map<String, Integer> operatorsPrecedence = Map.of(
            "!", 4,
            "^", 3,
            "*", 2,  "/", 2,
            "+", 1, "-", 1
    );

    /** Поддерживаемые математические функции */
    private static final Set<String> SUPPORTED_FUNCTIONS = Set.of(
            "sin", "cos", "tg", "ctg", "exp", "log", "-sin", "-cos", "-tg", "-ctg", "-exp", "-log"
    );

    /** Индекс конца обработанной строки */
    private Integer endStringIndex;

    /** Счетчик переменных в выражении */
    private Integer countVariables;

    /** Результат вычисления выражения */
    private Double res;

    /**
     * Анализирует математическое выражение и разделяет его на составляющие: числа, операторы и функции.
     *
     * @param expression математическое выражение для анализа
     * @param endingSymbol символ, обозначающий конец обработки (пробел, закрывающая скобка ')' или символ модуля '|')
     * @throws IllegalArgumentException если в выражении обнаружены синтаксические ошибки или недопустимые символы
     */
    private void parseExpression(String expression, char endingSymbol)
    {
        StringBuilder num = new StringBuilder();
        boolean waitFunc = false;
        int unaryCount = 0;
        String previous = " ", func = "";

        for (int i = 0; i < expression.length(); i++)
        {
            char c = expression.charAt(i);

            if (!waitFunc)
            {
                // Обработка числа
                if (Character.isDigit(c) || c == '.')
                {
                    if (c == '.' && num.toString().contains("."))
                        throw new IllegalArgumentException("Multiple dots in a number");
                    num.append(c);
                }
                else // Встречено не число
                {

                    // Добавление числа
                    if (num.length() > 0 && !num.toString().equals("-"))
                    {
                        numbers.add(num.toString());
                        num.setLength(0);
                    }

                    // Обработка унарного минуса
                    if (c == '-' && (numbers.isEmpty() || operators.size() >= numbers.size()))
                        num.append('-');

                        // Обработка операций
                    else if (c == '*' || c == '/' || c == '-' || c == '+' || c == '^' || c == '!')
                    {
                        if (numbers.isEmpty())
                            throw new IllegalArgumentException("An expression cannot start with an operator " + c);
                        if (operatorsPrecedence.getOrDefault(previous, 0) != 0 && !previous.equals("!"))
                            throw new IllegalArgumentException("Invalid operator sequence: " + previous + c);

                        if (c == '!')
                            ++unaryCount;
                        operators.add(String.valueOf(c));
                    }
                    // Обработка выражения внутри скобок или модуля
                    else if (c == '(' || (c == '|' && endingSymbol != '|'))
                    {
                        char endSymb = c;
                        if (c == '(')
                            endSymb = ')';
                        String remaining = expression.substring(i + 1);
                        Calculator obj = new Calculator(remaining, endSymb);

                        // Внутри скобочек или модуля не было переменных
                        if (obj.countVariables == 0)
                        {
                            double result = obj.getRes();
                            if (c == '|')
                                result = Math.abs(result);

                            if (num.toString().equals("-")) {
                                result = -result;
                                num.setLength(0);
                            }

                            numbers.add(String.valueOf(result));
                        }
                        // Внутри скобочек или внутри модуля были переменные
                        else
                        {
                            if (num.toString().equals("-"))
                            {
                                numbers.add("-" + String.valueOf(c));
                                operators.add("-" + String.valueOf(c));
                                num.setLength(0);
                            }
                            else
                            {
                                numbers.add(String.valueOf(c));
                                operators.add(String.valueOf(c));
                            }
                            numbers.addAll(obj.numbers);
                            operators.addAll(obj.operators);
                            numbers.add(String.valueOf(endSymb));
                            operators.add(String.valueOf(endSymb));
                            countVariables += obj.countVariables;
                        }
                        i = i + obj.endStringIndex + 1;
                    }
                    else if (c == endingSymbol)
                    {
                        // Обработка вложенного модуля
                        if (endingSymbol == '|' && numbers.isEmpty())
                        {
                            String remaining = expression.substring(i + 1);
                            Calculator obj = new Calculator(remaining, c);

                            if (obj.countVariables == 0)
                            {
                                double result = Math.abs(obj.getRes());
                                numbers.add(String.valueOf(result));
                            }
                            else
                            {
                                if (num.toString().equals("-"))
                                {
                                    numbers.add("-|");
                                    operators.add("-|");
                                    num.setLength(0);
                                }
                                else
                                {
                                    numbers.add("|");
                                    operators.add("|");
                                }
                                numbers.addAll(obj.numbers);
                                operators.addAll(obj.operators);
                                numbers.add("|");
                                operators.add("|");
                                countVariables += obj.countVariables;
                            }
                            i = i + obj.endStringIndex + 1;
                        }
                        else
                        {
                            endStringIndex = i;
                            return;
                        }
                    }
                    // Функция или переменная
                    else if (('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z'))
                    {
                        waitFunc = true;
                        func = String.valueOf(c);
                    }
                    else
                        throw new IllegalArgumentException("Unknown symbol: " + c);
                }

                if (!waitFunc)
                    previous = String.valueOf(c);
            }
            // Собираем название функции или переменной
            else
            {
                if (('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z'))
                    func = func + String.valueOf(c);
                else
                {
                    waitFunc = false;
                    if (SUPPORTED_FUNCTIONS.contains(func))
                    {
                        if ((!previous.equals("+")) && (!previous.equals("-")) && (!previous.equals("*")) && (!previous.equals("/")) && (!previous.equals("^")) && (!previous.equals(" ")))
                            throw new IllegalArgumentException("Invalid operator sequence: " + previous + func);
                        if  (num.toString().equals("-"))
                        {
                            num.setLength(0);
                            func = "-" + func;
                        }
                        operators.add(func);
                        ++unaryCount;
                    }
                    // Переменная
                    else
                    {
                        if (previous.equals("!") || previous.equals("0") || previous.equals("1") || previous.equals("2") || previous.equals("3") || previous.equals("4") || previous.equals("5") || previous.equals("6") || previous.equals("7") || previous.equals("8") || previous.equals("9"))
                            throw new IllegalArgumentException("Incorrect use of a variable");
                        if  (num.toString().equals("-"))
                        {
                            num.setLength(0);
                            func = "-" + func;
                        }
                        numbers.add(func);
                        ++countVariables;
                    }
                    --i;
                    previous = func;
                }
            }
        }

        if (num.length() > 0)
            numbers.add(num.toString());
        if (waitFunc && !SUPPORTED_FUNCTIONS.contains(func))
        {
            if (previous.equals("!") || previous.equals("0") || previous.equals("1") || previous.equals("2") || previous.equals("3") || previous.equals("4") || previous.equals("5") || previous.equals("6") || previous.equals("7") || previous.equals("8") || previous.equals("9"))
                throw new IllegalArgumentException("Incorrect use of a variable");
            if  (num.toString().equals("-"))
            {
                num.setLength(0);
                func = "-" + func;
            }
            numbers.add(func);
            ++countVariables;
        }

        if (endingSymbol == ')')
            throw new IllegalArgumentException("Met '(', for which there is no ')'");
        if (endingSymbol == '|')
            throw new IllegalArgumentException("Met '|', for which there is no '|'");
        if (numbers.size() == operators.size() - unaryCount)
            throw new IllegalArgumentException("Incorrect use of operators was encountered in the expression");
    }

    /**
     * Выполняет вычисление математического выражения на основе подготовленных данных.
     * Операции выполняются с учетом их приоритета и вложенности.
     * Для переменных, значения которых не заданы, запрашивает ввод у пользователя.
     *
     * @throws IllegalArgumentException если выражение состоит только из операторов
     *         или содержит неопределенные переменные
     * @throws ArithmeticException при попытке деления на ноль
     */
    private void calculated()
    {
        if (numbers.isEmpty())
            throw new IllegalArgumentException("The expression contains only operators");
        Set<String> unresolvedVariables = getUnresolvedVariables();
        if (!unresolvedVariables.isEmpty())
            requestVariablesFromUser(unresolvedVariables);

        List<Double> numbersCopy = new ArrayList<>();
        List<String> operatorsCopy = new ArrayList<>(operators);


        for (String numberStr : numbers)
        {
            try
            {
                numbersCopy.add(Double.parseDouble(numberStr));
            }
            catch (NumberFormatException e)
            {
                if (variables.containsKey(numberStr))
                    numbersCopy.add(variables.get(numberStr));
                else if (numberStr.equals("(") || numberStr.equals("-(") || numberStr.equals("|") || numberStr.equals("-|"))
                {
                    String exprStr = expressionToString();
                    Calculator expr = new Calculator(exprStr);
                    res = expr.getRes();
                    return;
                }
                else
                    throw new IllegalArgumentException("Unknown variable: " + numberStr);
            }
        }


        int i = 0;
        while (operatorsCopy.size() != 0)
        {
            String currentOp = operatorsCopy.get(i), nextOp = (i + 1 < operatorsCopy.size()) ? operatorsCopy.get(i + 1) : null;

            if (nextOp != null && hasHigherPrecedence(nextOp, currentOp))
                ++i;
            else
            {
                String operator = operatorsCopy.get(i);
                double left = numbersCopy.get(i), right = 0;

                if (!operator.equals("!") && !SUPPORTED_FUNCTIONS.contains(operator))
                    right = numbersCopy.get(i + 1);

                double result = switch (operator)
                {
                    case "+" -> left + right;
                    case "-" -> left - right;
                    case "*" -> left * right;
                    case "/" ->
                    {
                        if (right == 0) throw new ArithmeticException("Division by zero");
                        yield left / right;
                    }
                    case "^" -> Math.pow(left, right);
                    case "!" -> factorial(left);
                    case "sin" -> Math.sin(left);
                    case "cos" -> Math.cos(left);
                    case "tg" -> Math.tan(left);
                    case "ctg" -> 1.0/Math.tan(left);
                    case "exp" -> Math.exp(left);
                    case "log" -> Math.log(left);
                    case "-sin" -> -1*Math.sin(left);
                    case "-cos" -> -1*Math.cos(left);
                    case "-tg" -> -1*Math.tan(left);
                    case "-ctg" -> -1.0/Math.tan(left);
                    case "-exp" -> -1*Math.exp(left);
                    case "-log" -> -1*Math.log(left);
                    default -> throw new IllegalArgumentException("Unknown operator: " + operator);
                };

                numbersCopy.set(i, result);
                if (!operator.equals("!") && !SUPPORTED_FUNCTIONS.contains(operator))
                    numbersCopy.remove(i + 1);
                operatorsCopy.remove(i);
                if (i > 0)
                    --i;
            }
        }
        res = numbersCopy.get(0);
    }

    /**
     * Формирует строковое представление выражения с подстановкой значений переменных.
     * Заменяет все переменные на их числовые значения, сохраняя при этом математические функции без изменений.
     *
     * @return выражение в виде строки с вычисленными значениями переменных
     */
    private String expressionToString()
    {
        String result = expressionString;
        for (Map.Entry<String, Double> entry : variables.entrySet()) {
            String key = entry.getKey();
            String value = String.valueOf(entry.getValue());
            String regex = "(?<!sin|cos|tg|ctg|log|exp)(?<![a-zA-Z_])" + Pattern.quote(key) + "(?!\\w)";
            result = result.replaceAll(regex, value);
        }
        return result;
    }

    /**
     * Определяет перечень переменных в выражении, для которых не заданы значения.
     * Идентифицирует переменные, которые не являются числами и отсутствуют в списке заданных переменных.
     * Игнорирует служебные символы (скобки, знак модуля и другие операторы).
     *
     * @return набор имен переменных, требующих определения значений
     */
    private Set<String> getUnresolvedVariables()
    {
        Set<String> unresolved = new HashSet<>();
        for (String item : numbers)
            if (!isNumeric(item) && !variables.containsKey(item) && !item.equals("(") && !item.equals("-(") && !item.equals(")") && !item.equals("-|") && !item.equals("|"))
                unresolved.add(item);

        return unresolved;
    }

    /**
     * Проверяет, может ли строка быть преобразована в число типа Double.
     *
     * @param str строка для проверки
     * @return true если строка может быть преобразована в число, false в противном случае
     */
    private boolean isNumeric(String str)
    {
        try
        {
            Double.parseDouble(str);
            return true;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }

    /**
     * Запрашивает ввод значений для переменных через консоль в интерактивном режиме.
     * Для переменных с отрицательным знаком автоматически применяет отрицание к введенному значению.
     * Циклически запрашивает данные до получения корректного числового значения для каждой переменной.
     *
     * @param unresolvedVariables набор переменных, требующих определения числовых значений
     */
    private void requestVariablesFromUser(Set<String> unresolvedVariables)
    {
        Scanner scanner = new Scanner(System.in);

        for (String varName : unresolvedVariables) {
            String displayName = varName.startsWith("-") ? varName.substring(1) : varName;

            System.out.print("Enter value for variable '" + displayName + "': ");
            while (true)
            {
                try
                {
                    String input = scanner.nextLine().trim();
                    if (input.isEmpty())
                    {
                        System.out.print("Value cannot be empty. Enter value for '" + displayName + "': ");
                        continue;
                    }
                    double value = Double.parseDouble(input);
                    if (varName.charAt(0) == '-')
                        value = -value;

                    variables.put(varName, value);
                    break;
                }
                catch (NumberFormatException e)
                {
                    System.out.print("Invalid number. Please enter a valid value for '" + displayName + "': ");
                }
            }
        }
    }

    /**
     * Сравнивает приоритет выполнения двух операторов.
     * Математические функции обладают наивысшим приоритетом вычисления.
     * Для остальных операторов сравнение осуществляется на основе таблицы приоритетов.
     *
     * @param op1 оператор, приоритет которого проверяется
     * @param op2 оператор, приоритет которого сравнивается
     * @return true если op1 имеет более высокий приоритет выполнения, чем op2
     */
    private boolean hasHigherPrecedence(String op1, String op2)
    {
        if (SUPPORTED_FUNCTIONS.contains(op1))
            return true;
        if (SUPPORTED_FUNCTIONS.contains(op2))
            return false;
        if (operatorsPrecedence.get(op1) == 3 && operatorsPrecedence.get(op2) != 4)
            return true;
        return operatorsPrecedence.get(op1) > operatorsPrecedence.get(op2);
    }

    /**
     * Вычисляет факториал переданного числа.
     * Факториал вычисляется только для неотрицательных целых чисел.
     *
     * @param n число для вычисления факториала
     * @return результат вычисления факториала
     * @throws IllegalArgumentException если число отрицательное или не является целым
     */
    private double factorial(double n)
    {
        if (n < 0)
            throw new IllegalArgumentException("Factorial is not defined for negative numbers");
        if (n % 1 != 0)
            throw new IllegalArgumentException("Factorial is defined only for integers");

        int integerN = (int) n;
        if (integerN == 0 || integerN == 1)
            return 1;

        double result = 1;
        for (int i = 2; i <= integerN; i++)
            result *= i;

        return result;
    }

    /**
     * Служебный конструктор для рекурсивной обработки составных выражений.
     * Применяется для вычисления вложенных выражений внутри скобок и модулей.
     *
     * @param expression математическое выражение для обработки
     * @param endingSymbol символ, обозначающий границу обработки выражения
     */
    private Calculator(String expression, char endingSymbol)
    {
        this.numbers = new ArrayList<>();
        this.operators = new ArrayList<>();
        this.variables = new HashMap<>();
        this.countVariables = 0;
        this.expressionString = expression;
        String cleanExpression = expression.replaceAll("\\s+", "");
        endStringIndex = cleanExpression.length();
        this.parseExpression(cleanExpression, endingSymbol);
    }

    /**
     * Основной конструктор для создания вычислителя выражений.
     *
     * @param expression математическое выражение для вычисления
     */
    public Calculator(String expression)
    {
        this(expression, ' ');
    }

    /**
     * Возвращает результат вычисления выражения.
     * Если в выражении есть переменные без значений, запрашивает их у пользователя.
     *
     * @return результат вычисления
     */
    public double getRes()
    {
        calculated();
        return res;
    }

    /**
     * Устанавливает значение для переменной и автоматически создает соответствующую отрицательную переменную.
     * @param k имя переменной (должно быть без префикса минуса)
     * @param v значение переменной
     */
    public void setVariable(String k, Double v)
    {
        this.variables.put(k, v);
        this.variables.put("-" + k, -1 * v);
    }

    /**
     * Заменяет всю текущую карту переменных на предоставленную.
     * @param variables новая карта переменных, где ключ - имя переменной, значение - числовое значение
     */
    public void setVariables(Map<String, Double> variables)
    {
        this.variables = variables;
    }
}