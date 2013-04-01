package com.tasty.fish.utils.parser;

import com.tasty.fish.utils.parser.node.*;
import com.tasty.fish.utils.parser.operators.Definitions;
import com.tasty.fish.utils.parser.operators.Iop;
import com.tasty.fish.utils.parser.utils.ExpressionParsingException;
import com.tasty.fish.utils.parser.utils.MutableFixed;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FastParse {

    private Map<String, Iop> _opMap = Definitions.getOperatorDefinitions();

    private HashMap<String ,MutableFixed> _parametersMap;

    private IExpressionNode rootNode = null;

    //region Operator precedence definitions
    private String[][] operators = { { "|" }, { "^" }, { "&" }, { "==", "!=" },
            { ">=", "<=" }, { ">>", "<<" }, { "+", "-" }, { "*", "/", "%" } };

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

    public FastParse(String[] parameterKeys) {
        _parametersMap = new HashMap<String, MutableFixed>();
        for(String k : parameterKeys)
            _parametersMap.put(k, new MutableFixed(0));
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
                Iop operator = _opMap.get(opStr);
                if ((p0 != null && p1 != null))
                {
                    return new OperatorNode(operator, p0, p1);
                }
                else
                    throw new ExpressionParsingException("Operator " + opStr + " has null inputs");
            }
            break;
        case Value:
            int value = parseValue(s);
            return new ValueNode(value);
        case Variable:
            return getVariableNode(s);
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
    public Map<String, MutableFixed> getParametersMap(){
        return _parametersMap;
    }

    private VariableNode getVariableNode(String key) {
        MutableFixed found = _parametersMap.get(key);
        return found != null ? new VariableNode(found) : null;
    }
    //endregion

    public long evaluate() {
        return rootNode != null ? rootNode.eval() : 0;
    }

    public void tryParse(String expression) throws ExpressionParsingException {
        rootNode = parse(expression);
    }
}