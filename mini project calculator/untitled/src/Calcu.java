import java.util.Stack;
import java.util.ArrayDeque;
import java.util.Deque;
public class Calcu {



    static class Stack {
        private Deque<String> items;

        public Stack() {
            items = new ArrayDeque<>();
        }

        public boolean isEmpty() {
            return items.isEmpty();
        }

        public void push(String item) {
            items.push(item);
        }

        public String pop() {
            if (isEmpty()) {
                return null;
            } else {
                return items.pop();
            }
        }

        public String top() {
            if (isEmpty()) {
                return null;
            } else {
                return items.peek();
            }
        }

        public int size() {
            return items.size();
        }
    }

    public static class Calculator {
        private static boolean isNumber1(char ch) {
            return "123456789.".contains(String.valueOf(ch));
        }

        private static   boolean isNumber(String str) {
            try {
                Double.parseDouble(str);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }

        private static boolean isOperator(char ch) {
            return "+-*/^!".contains(String.valueOf(ch));
        }

        private static   int precedence(String op) {
            if (op.equals("^")) {
                return 3;
            } else if (op.equals("*") || op.equals("/")) {
                return 2;
            } else if (op.equals("+") || op.equals("-")) {
                return 1;
            }else if (op.equals("!")) {
                return 4;}
             else {
                return 0;
            }
        }

        private static   double operate(double a, double b, String op) {
            switch (op) {
                case "+":
                    return a + b;
                case "-":
                    return a - b;
                case "*":
                    return a * b;
                case "/":
                    if (b == 0) {
                        return Double.NaN;
                    } else {
                        return a / b;
                    }
                case "^":
                    return Math.pow(a, b);
                default:
                    return Double.NaN;
            }
        }

        private static String[] infixToPostfix(String expression) {
            Stack stackInput = new Stack();
            Stack stackOutput = new Stack();
            Stack stackPostfix = new Stack();
            StringBuilder postfix = new StringBuilder();
            StringBuilder num = new StringBuilder();

            for (char ch : expression.toCharArray()) {
                if (isNumber1(ch)) {
                    num.append(ch);
                } else if (isOperator(ch)) {
                    if (num.length() > 0) {
                        stackOutput.push(num.toString());
                        num = new StringBuilder();
                    }
                    int index = expression.indexOf(ch);
                    if (ch == '-' && (index==0 || expression.charAt(index - 1) == '(')) {
                        stackOutput.push("0");
                    }
                    while (!stackInput.isEmpty() && precedence(stackInput.top()) >= precedence(String.valueOf(ch))) {
                        stackOutput.push(stackInput.pop());
                    }
                    stackInput.push(String.valueOf(ch));
                } else if (ch == '(') {
                    if (num.length() > 0) {
                        stackOutput.push(num.toString());
                        num = new StringBuilder();
                    }
                    stackInput.push(String.valueOf(ch));
                } else if (ch == ')') {
                    if (num.length() > 0) {
                        stackOutput.push(num.toString());
                        num = new StringBuilder();
                    }
                    while (!stackInput.isEmpty() && !stackInput.top().equals("(")) {
                        stackOutput.push(stackInput.pop());
                    }
                    if (stackInput.isEmpty()) {
                        return null;
                    }
                    stackInput.pop();
                } else if (ch == ' ') {
                    continue;
                } else {
                    return null;
                }
            }

            if (num.length() > 0) {
                stackOutput.push(num.toString());
            }

            while (!stackInput.isEmpty()) {
                if (stackInput.top().equals("(")) {
                    return null;
                }


                stackOutput.push(stackInput.pop());
            }

            String[] result = new String[stackOutput.size()];
            int i = stackOutput.size() - 1;
            while (!stackOutput.isEmpty()) {
                result[i] = stackOutput.pop();
                i--;
            }

            return result;
        }

        private static Double calculate(String[] exp) {
            Stack stackOperand = new Stack();

            for (String token : exp) {
                if (isNumber(token)) {
                    stackOperand.push(token);
                } else if (isOperator(token.charAt(0))) {
                    double b = Double.parseDouble(stackOperand.pop());
                    double a = Double.parseDouble(stackOperand.pop());

                    if (a == Double.NaN || b == Double.NaN) {
                        return null;
                    }

                    double result = operate(a, b, token);

                    if (result == Double.NaN) {
                        return null;
                    }

                    stackOperand.push(String.valueOf(result));
                } else {
                    return null;
                }
            }

            double result = Double.parseDouble(stackOperand.pop());

            if (!stackOperand.isEmpty()){
                return null;
            }

            return result;
        }

        public static void main(String[] args) {
            java.util.Scanner scanner = new java.util.Scanner(System.in);
            String n = scanner.nextLine();

            String[] postfix = infixToPostfix(n);

            if (postfix == null) {
                System.out.println("error");
            } else {
                Double result = calculate(postfix);

                if (result == null) {
                    System.out.println("error");
                } else {
                    System.out.println("the result is " + result);
                }
            }
        }
    }
}
