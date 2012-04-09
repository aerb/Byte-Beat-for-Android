package com.tasty.fish.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FastParse {
    static private Map<String, Op> opMap;

    public interface Op {
        double Ex(double a, double b);
    }

    static {
        opMap = new HashMap<String, Op>();
        opMap.put("|", new Op() {
            public double Ex(double a, double b) {
                return (long) a | (long) b;
            }
        });
        opMap.put("^", new Op() {
            public double Ex(double a, double b) {
                return (long) a ^ (long) b;
            }
        });
        opMap.put("&", new Op() {
            public double Ex(double a, double b) {
                return (long) a & (long) b;
            }
        });
        opMap.put("%", new Op() {
            public double Ex(double a, double b) {
                return (long) (a % b);
            }
        });
        opMap.put("+", new Op() {
            public double Ex(double a, double b) {
                return (long) (a + b);
            }
        });
        opMap.put("-", new Op() {
            public double Ex(double a, double b) {
                return (long) (a - b);
            }
        });
        opMap.put("*", new Op() {
            public double Ex(double a, double b) {
                return (long) (a * b);
            }
        });
        opMap.put("/", new Op() {
            public double Ex(double a, double b) {
                return (long) (a / b);
            }
        });
        opMap.put(">>", new Op() {
            public double Ex(double a, double b) {
                return (long) ((long) a >> (long) b);
            }
        });
        opMap.put("<<", new Op() {
            public double Ex(double a, double b) {
                return (long) ((long) a << (long) b);
            }
        });
    }

    private class MutableDouble {
        public double Value;

        public MutableDouble(Double d) {
            Value = d;
        }
    }

    private class ParseNode {
        private Op o;
        private ParseNode p0;
        private ParseNode p1;
        private int intValue;
        private MutableDouble variableValue = null;
        private parseType ptype = null;

        private ParseNode(int value) {
            intValue = value;
            ptype = parseType.Value;
        }

        public ParseNode(Op o, ParseNode p0, ParseNode p1) {
            this.o = o;
            this.p0 = p0;
            this.p1 = p1;
            ptype = parseType.Expression;
        }

        private ParseNode(MutableDouble d) {
            variableValue = d;
            ptype = parseType.Variable;
        }

        private double eval() {
            switch (ptype) {
            case Expression:
                return o.Ex(p0.eval(), p1.eval());
            case Value:
                return intValue;
            case Variable:
                return variableValue.Value;
            default:
                break;
            }
            return 0;
        }
    }

    private Map<String, Double> varMap;
    private ArrayList<MutableDouble> simpleMap;

    private String[][] operators = { { "|" }, { "^" }, { "&" }, { ">>", "<<" },
            { "+", "-" }, { "*", "/", "%" } };
    private char lb = '(', rb = ')';

    private Pattern num = Pattern.compile("[0-9]+");
    private Pattern hex = Pattern.compile("\\b0[xX][0-9a-fA-F]+\\b");
    private Pattern var = Pattern.compile("[a-zA-Z0-9]+");

    private ParseNode rootNode = null;

    private enum parseType {
        Expression, Value, Variable
    }

    private enum direction {
        left_to_right, right_to_left
    }

    public FastParse() {
        varMap = new HashMap<String, Double>();
        simpleMap = new ArrayList<MutableDouble>();
        for (int i = 0; i < 4; ++i)
            simpleMap.add(new MutableDouble(0d));
    }

    private parseType determineParseType(String s) {
        Matcher numMatcher = num.matcher(s);
        Matcher hexMatcher = hex.matcher(s);
        Matcher varMatcher = var.matcher(s);
        if (numMatcher.matches() || hexMatcher.matches())
            return parseType.Value;
        if (varMatcher.matches())
            return parseType.Variable;
        return parseType.Expression;
    }

    public double evaluate() {
        return rootNode != null ? rootNode.eval() : 0;
    }

    private MutableDouble getMutableVariable(String key) {
        if (key.compareTo("t") == 0)
            return simpleMap.get(0);
        else if (key.compareTo("p1") == 0)
            return simpleMap.get(1);
        else if (key.compareTo("p2") == 0)
            return simpleMap.get(2);
        else if (key.compareTo("p3") == 0)
            return simpleMap.get(3);

        return null;
    }

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

    private ParseNode parse(String s) {
        ParseNode p0 = null, p1 = null;

        s = s.replace(" ", "");
        String[] subExp = null;

        s = removeRedundantBrackets(s);
        parseType t = determineParseType(s);
        switch (t) {
        case Expression:
            int[] result = findNextOperator(s);
            if (result != null) {
                subExp = splitExpression(s, result[0], result[1]);
                p0 = parse(subExp[0]);
                p1 = parse(subExp[2]);
                return (p0 != null && p1 != null) ? new ParseNode(
                        opMap.get(subExp[1]), p0, p1) : null;
            }
            break;
        case Value:
            int value = parseValue(s);
            return new ParseNode(value);
        case Variable:
            MutableDouble d = getMutableVariable(s);
            return (d != null) ? new ParseNode(d) : null;
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

    public void setT(double value) {
        simpleMap.get(0).Value = value;
    }

    public void setVariable(int key, double value) {
        simpleMap.get(key + 1).Value = value;
    }

    private String[] splitExpression(String s, int begin, int end) {
        String s0 = s.substring(0, begin);
        String s1 = s.substring(begin, end);
        String s2 = s.substring(end);
        return new String[] { s0, s1, s2 };
    }

    public boolean tryParse(String expression) {
        rootNode = parse(expression);
        return rootNode != null;
    }
}