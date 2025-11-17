package calculator;

/**
 * Демонстрационный класс для показа основных возможностей {@link Calculator}.
 *
 * <p>Класс демонстрирует ключевые функции вычислителя математических выражений:
 * <ul>
 *   <li>Базовые арифметические операции с приоритетом</li>
 *   <li>Математические функции (тригонометрические, экспоненциальные, логарифмические)</li>
 *   <li>Работу с переменными и установку их значений</li>
 *   <li>Сложные комбинированные выражения со скобками и модулями</li>
 * </ul>
 *
 * <p><b>Демонстрируемые примеры:</b>
 * <pre>
 * {@code
 * "2 + 3 * 4"                      // Приоритет операторов
 * "sin(1.57) + cos(0)"             // Математические функции
 * "a + b * 2"                      // Переменные с ручной установкой значений
 * "(x + y) * |z|^x - exp(w)"       // Комплексное выражение со всеми возможностями
 * }
 * </pre>
 *
 * @author ilabe
 * @version 1.0
 * @see Calculator
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
     *   <li>Работа с переменными через метод {@link Calculator#setVariable(String, Double)}</li>
     *   <li>Комплексное выражение, объединяющее все возможности</li>
     * </ol>
     *
     * @param args аргументы командной строки (не используются)
     *
     * @see Calculator#Calculator(String)
     * @see Calculator#getRes()
     * @see Calculator#setVariable(String, Double)
     */
    public static void main(String[] args)
    {
        System.out.println("Демонстрация Calculator");
        System.out.println("=" .repeat(60));

        System.out.println("1. Базовые операции:");
        Calculator calculator1 = new Calculator("2 + 3 * 4");
        System.out.println("   2 + 3 * 4 = " + calculator1.getRes());

        System.out.println("2. Математические функции:");
        Calculator calculator2 = new Calculator("sin(1.57) + cos(0)");
        System.out.println("   sin(1.57) + cos(0) = " + calculator2.getRes());

        System.out.println("3. Переменные:");
        Calculator calculator3 = new Calculator("a + b * 2");
        calculator3.setVariable("a", 5.0);
        calculator3.setVariable("b", 3.0);
        System.out.println("   a + b * 2 = " + calculator3.getRes() + " (при a=5, b=3)");

        System.out.println("4. Сложное выражение:");
        Calculator calculator4 = new Calculator("(x + y) * |z|^x - exp(w)");
        calculator4.setVariable("x", 3.0);
        calculator4.setVariable("y", 3.0);
        calculator4.setVariable("z", -2.0);
        calculator4.setVariable("w", 0.0);
        System.out.println("   (x + y) * |z|^x - exp(w) = " + calculator4.getRes() + " (при x=3, y=3, z=-2, w=0)");

        System.out.println("=" .repeat(60));
    }
}