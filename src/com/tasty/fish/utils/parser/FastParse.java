package com.tasty.fish.utils.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FastParse {
    static private Map<String, Op> opMap;
    private ArrayList<MutableFloating> simpleMap;
    private IExpressionNode rootNode = null;
    private MutableFixed _t = new MutableFixed(0);

    //region Operator definitions

    public interface Op {
        double Ex(double a, double b);
    }
    static {
        opMap = new HashMap<String, Op>();
        opMap.put("<=", new Op() {
            public double Ex(double a, double b) {
                return a <= b ? 1 : 0;
            }
        });
        opMap.put(">=", new Op() {
            public double Ex(double a, double b) {
                return a >= b ? 1 : 0;
            }
        });
        opMap.put("!=", new Op() {
            public double Ex(double a, double b) {
                return a != b ? 1 : 0;
            }
        });
        opMap.put("==", new Op() {
            public double Ex(double a, double b) {
                return a == b ? 1 : 0;
            }
        });
        opMap.put("|",  new Op() {
            public double Ex(double a, double b) {
                return (long) a | (long) b;
            }
        });
        opMap.put("&",  new Op() {
            public double Ex(double a, double b) {
                return (long)a & (long)b;
            }
        });
        opMap.put("^",  new Op() {
            public double Ex(double a, double b) {
                return (long) a ^ (long) b;
            }
        });
        opMap.put("%",  new Op() {
            public double Ex(double a, double b) {
                return a % b;
            }
        });
        opMap.put("+",  new Op() {
            public double Ex(double a, double b) {
                return a + b;
            }
        });
        opMap.put("-",  new Op() {
            public double Ex(double a, double b) {
                return  a - b;
            }
        });
        opMap.put("*",  new Op() {
            public double Ex(double a, double b) {
                return a * b;
            }
        });
        opMap.put("/",  new Op() {
            public double Ex(double a, double b) {
                return a / b;
            }
        });
        opMap.put(">>", new Op() {
            public double Ex(double a, double b) {
                return ((long) a >> (long) b);
            }
        });
        opMap.put("<<", new Op() {
            public double Ex(double a, double b) {
                return ((long) a << (long) b);
            }
        });
    }

    //endregion

    //region Operator precedence definitions
    private String[][] operators = { { "|" }, { "^" }, { "&" }, { "==", "!=" },
            { ">=", "<=" }, { ">>", "<<" }, { "+", "-" }, { "*", "/", "%" } };

    private String[] fixedPointOperators = { "&", "|", "^", ">>", "<<" };

    private char lb = '(', rb = ')';
    //endregion

    //region Parsing definitions

    public enum parseType {
        SubExpression, Value, Variable
    }
    private enum direction {
        left_to_right, right_to_left
    }
    //endregion

    //region Regex
    private Pattern num = Pattern.compile("[0-9]+");
    private Pattern hex = Pattern.compile("\\b0[xX][0-9a-fA-F]+\\b");
    private Pattern var = Pattern.compile("[a-zA-Z0-9]+");
    //endregion

    public FastParse() {
        simpleMap = new ArrayList<MutableFloating>();
        for (int i = 0; i < 4; ++i)
            simpleMap.add(new MutableFloating(0d));
    }

    //region Bracket matching
    private int findClosingBracket(String s, int index) {
        return findClosingBracket(s, index, direction.left_to_right);
    }

    private int findClosingBracket(String s, int index, direction d) {
        int depth = 0;
        int strlen = s.length();
        switch (d) {
        case left_to_right:
            for (int i = index; i < strlen; ++i) {
                char c = s.charAt(i);
                if (c == lb)
                    ++depth;
                else if (c == rb)
                    --depth;
                if (depth == 0) {
                    return i;
                }
            }
            break;
        case right_to_left:
            for (int i = index; i >= 0; --i) {
                char c = s.charAt(i);
                if (c == rb)
                    ++depth;
                else if (c == lb)
                    --depth;
                if (depth == 0) {
                    return i;
                }
            }
            break;
        default:
            break;
        }
        return -1;
    }
    //endregion

    //region Parsing methods
    private int[] findNextOperator(String s) {
        int strlen = s.length();
        for (String[] ops : operators) {
            for (int i = strlen - 1; i >= 0; --i) {
                for (String op : ops) {
                    int oplen = op.length();
                    String c = s.substring(i, (i + oplen > strlen) ? strlen : i
                            + oplen);
                    if (c.charAt(0) == rb) {
                        i = findClosingBracket(s, i, direction.right_to_left);
                    }
                    if (i < 0)
                        return null;

                    boolean found = c.compareTo(op) == 0;
                    if (found) {
                        return new int[] { i, i + oplen };
                    }
                }
            }
        }
        return null;
    }

    private parseType determineParseType(String s) {
        Matcher numMatcher = num.matcher(s);
        Matcher hexMatcher = hex.matcher(s);
        Matcher varMatcher = var.matcher(s);
        if (numMatcher.matches() || hexMatcher.matches())
            return parseType.Value;
        if (varMatcher.matches())
            return parseType.Variable;
        return parseType.SubExpression;
    }

    private IExpressionNode parse(String s) throws ExpressionParsingException {
        IExpressionNode p0 = null, p1 = null;

        s = s.replace(" ", "");
        String[] subExp = null;

        s = removeRedundantBrackets(s);
        parseType t = determineParseType(s);
        switch (t) {
        case SubExpression:
            int[] result = findNextOperator(s);
            if (result != null) {
                subExp = splitExpression(s, result[0], result[1]);
                p0 = parse(subExp[0]);
                p1 = parse(subExp[2]);
                String opStr = subExp[1];
                Op operator = opMap.get(opStr);
                if ((p0 != null && p1 != null) && (
                        !requiresFixedArgument(opStr) ||
                            (p0.isFixed() && p1.isFixed())
                        )
                    )
                {
                    return new OperatorNode(operator, p0, p1);
                }
                else
                    throw new ExpressionParsingException("Operator " + opStr + " cannot have floating arguments.");
            }
            break;
        case Value:
            int value = parseValue(s);
            return new ValueNode(value);
        case Variable:
            FixedVariableNode fixedNode = getFixedNode(s);
            FloatingVariableNode floatingNode = getFloatingNode(s);
            return fixedNode != null ? fixedNode :
                   floatingNode != null ? floatingNode :
                   null;
        default:
            break;
        }
        return null;
    }

    private int parseValue(String s) {
        if (s.length() > 9) {
            s = s.substring(0, 9);
        }
        if (hex.matcher(s).matches()) {
            s = s.replaceAll("\\b0[xX]", "");
            return Integer.parseInt(s, 16);
        } else {
            return Integer.parseInt(s);
        }
    }

    private boolean requiresFixedArgument(String operator){
        for(String o : fixedPointOperators){
            if(o.compareTo(operator) == 0)
                return true;
        }
        return false;
    }
    //endregion

    //region String operations
    private String removeRedundantBrackets(String s) {
        for (int i = 0; i < s.length(); ++i) {
            char c = s.charAt(i);
            if (c == lb) {
                int closing = findClosingBracket(s, i);
                if (i == 0 && closing == s.length() - 1) {
                    s = s.substring(i + 1, closing);
                    i = -1;
                }
            }
        }
        return s;
    }

    private String[] splitExpression(String s, int begin, int end) {
        String s0 = s.substring(0, begin);
        String s1 = s.substring(begin, end);
        String s2 = s.substring(end);
        return new String[] { s0, s1, s2 };
    }
    //endregion

    //region Getters & Setters
    public void setTime(long value) {
        _t.Value = value;
    }

    public void setVariable(int key, double value) {
        simpleMap.get(key + 1).Value = value;
    }

    private FloatingVariableNode getFloatingNode(String key) {
        MutableFloating found = null;
        if (key.compareTo("p1") == 0)
            found =  simpleMap.get(1);
        else if (key.compareTo("p2") == 0)
            found =  simpleMap.get(2);
        else if (key.compareTo("p3") == 0)
            found =  simpleMap.get(3);
        return found != null ? new FloatingVariableNode(found) : null;
    }

    private FixedVariableNode getFixedNode(String key){
        if (key.compareTo("t") == 0)
            return new FixedVariableNode(_t);
        return null;
    }
    //endregion

    public double evaluate() {
        return rootNode != null ? rootNode.eval() : 0;
    }

    public void tryParse(String expression) throws ExpressionParsingException {
        rootNode = parse(expression);
    }
}