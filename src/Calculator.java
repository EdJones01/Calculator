import java.util.Stack;

public class Calculator {
    public static double evaluateExpression(String expression) {
        expression = expression.replaceAll("\\s+", "");

        Stack<Double> operands = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);
            if (Character.isDigit(ch)) {
                StringBuilder sb = new StringBuilder();
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    sb.append(expression.charAt(i));
                    i++;
                }
                i--;
                operands.push(Double.parseDouble(sb.toString()));
            } else if (ch == '(') {
                operators.push(ch);
            } else if (ch == ')') {
                while (operators.peek() != '(') {
                    char operator = operators.pop();
                    double operand2 = operands.pop();
                    double operand1 = operands.pop();
                    double result = applyOperator(operator, operand1, operand2);
                    operands.push(result);
                }
                operators.pop();
            } else if (isOperator(ch)) {
                while (!operators.isEmpty() && operators.peek() != '(' && hasHigherPrecedence(operators.peek(), ch)) {
                    char operator = operators.pop();
                    double operand2 = operands.pop();
                    double operand1 = operands.pop();
                    double result = applyOperator(operator, operand1, operand2);
                    operands.push(result);
                }
                operators.push(ch);
            }
        }

        while (!operators.isEmpty()) {
            char operator = operators.pop();
            double operand2 = operands.pop();
            double operand1 = operands.pop();
            double result = applyOperator(operator, operand1, operand2);
            operands.push(result);
        }

        return operands.pop();
    }

    public static boolean isOperator(char ch) {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '%' || ch == '^';
    }

    private static boolean hasHigherPrecedence(char operator1, char operator2) {
        int precedence1 = getPrecedence(operator1);
        int precedence2 = getPrecedence(operator2);
        return precedence1 >= precedence2;
    }

    private static int getPrecedence(char operator) {
        return switch (operator) {
            case '+', '-' -> 1;
            case '*', '/', '%' -> 2;
            case '^' -> 3;
            default -> 0;
        };
    }

    private static double applyOperator(char operator, double operand1, double operand2) {
        switch (operator) {
            case '+' -> {
                return operand1 + operand2;
            }
            case '-' -> {
                return operand1 - operand2;
            }
            case '*' -> {
                return operand1 * operand2;
            }
            case '/' -> {
                if (operand2 == 0)
                    throw new ArithmeticException("Cannot divide by zero");
                return operand1 / operand2;
            }
            case '%' -> {
                if (operand2 == 0)
                    throw new ArithmeticException("Cannot divide by zero");
                return operand1 % operand2;
            }
            case '^' -> {
                return Math.pow(operand1, operand2);
            }
            default -> {
                return 0;
            }
        }
    }
}