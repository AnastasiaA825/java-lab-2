package calculator;

/**
 * <p>Класс демонстрирует ключевые функции вычислителя математических выражений:
 * <ul>
 *   <li>Базовые арифметические операции с приоритетом</li>
 *   <li>Математические функции (тригонометрические, экспоненциальные, логарифмические)</li>
 *   <li>Работу с переменными и установку их значений</li>
 *   <li>Сложные комбинированные выражения со скобками и модулями</li>
 * </ul>
 *
 *  @author Анастасия
 *  @version 1.0
 */
public class Main
{
    /**
     * Основной метод демонстрации функциональности Calculator.
     * Выполняет последовательное вычисление нескольких выражений,
     * показывая различные возможности класса.
     *
     * <p><b>Выполняемые демонстрации:</b>
     * <ol>
     *   <li>Базовые арифметические операции с проверкой приоритета</li>
     *   <li>Математические функции (синус, косинус, экспонента)</li>
     *   <li>Работа с переменными</li>
     *   <li>Комплексное выражение, объединяющее все возможности</li>
     * </ol>
     */
    public static void main(String[] args)
    {
        System.out.println("Демонстрация Calculator");
        System.out.println("=" .repeat(60));

        System.out.println("1. Базовые операции:");
        Calculator calculator1 = new Calculator("1 + 2 * 3");
        System.out.println("   1 + 2 * 3 = " + calculator1.getRes());

        System.out.println("2. Математические функции:");
        Calculator calculator2 = new Calculator("sin(1.5) + cos(0)");
        System.out.println("   sin(1.5) + cos(0) = " + calculator2.getRes());

        System.out.println("3. Переменные:");
        Calculator calculator3 = new Calculator("a + b * 3");
        calculator3.setVariable("a", 7.0);
        calculator3.setVariable("b", 10.0);
        System.out.println("   a + b * 3 = " + calculator3.getRes() + " (при a=7, b=10)");

        System.out.println("4. Сложное выражение:");
        Calculator calculator4 = new Calculator("(a + b) * |c|^b - exp(d)");
        calculator4.setVariable("a", 5.0);
        calculator4.setVariable("b", -4.0);
        calculator4.setVariable("c", 12.0);
        calculator4.setVariable("d", 0.0);
        System.out.println("   (a + b) * |c|^b - exp(d) = " + calculator4.getRes() + " (при a=5, b=-4, c=12, d=0)");

        System.out.println("=" .repeat(60));
    }
}