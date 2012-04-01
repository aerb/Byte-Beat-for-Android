package com.tasty.fish;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FastParse {
	private Map<String, Float> varMap = new HashMap<String,Float>();
	private Map<String, Op> opMap = new HashMap<String, Op>();
	
	public interface Op {
		int Ex(float a, float b);
	}

	public void SetVariable(String key, float value){
		varMap.put(key, new Float(value));
	}
	
	public void SetVariable(String key, Float value){
		varMap.put(key, value);
	}

	private String[] operators = {"|","^","&",">>","<<","+","-","*","/","%"};
	public void InitializeOperators(){
		opMap.put("|", new Op() {
			public int Ex(float a, float b) {
				return (int)a | (int)b;
			}
		});
		opMap.put("^", new Op() {
			public int Ex(float a, float b) {
				return (int)a ^ (int)b;
			}
		});
		opMap.put("&", new Op() {
			public int Ex(float a, float b) {
				return (int)a & (int)b;
			}
		});
		opMap.put("%", new Op() {
			public int Ex(float a, float b) {
				return (int)(a % b);
			}
		});
		opMap.put("+", new Op() {
			public int Ex(float a, float b) {
				return (int)(a + b);
			}
		});
		opMap.put("-", new Op() {
			public int Ex(float a, float b) {
				return (int)(a - b);
			}
		});
		opMap.put("*", new Op() {
			public int Ex(float a, float b) {
				return (int)(a * b);
			}
		});
		opMap.put("/", new Op() {
			public int Ex(float a, float b) {
				return (int)(a / b);
			}
		});
		opMap.put(">>", new Op() {
			public int Ex(float a, float b) {
				return (int)a >> (int)b;
			}
		});
		opMap.put("<<", new Op() {
			public int Ex(float a, float b) {
				return (int)a << (int)b;
			}
		});
	}
	
	public FastParse() {
		InitializeOperators();
	}

	public String ob = "(";
	public String cb = ")";

	public int FindClosingBracket(String s, int index) {
		int depth = 0;
		for (int i = index; i < s.length(); ++i) {
			String c = s.substring(i, i + 1);
			if (c.compareTo(ob) == 0)
				++depth;
			else if (c.compareTo(cb) == 0)
				--depth;
			if (depth == 0){
				return i;
			}
		}
		return -1;
	}

	Pattern num = Pattern.compile("[0-9()]+");
	Pattern hex = Pattern.compile("\\b0[xX][0-9a-fA-F]+\\b");
	Pattern var = Pattern.compile("[a-zA-Z0-9()]+");
	
	public enum parseType { Expression, Value, Variable };
	
	public parseType DetermineParseType(String s){
		Matcher numMatcher = num.matcher(s);
		Matcher hexMatcher = hex.matcher(s);
		Matcher varMatcher = var.matcher(s);
		if(numMatcher.matches() || hexMatcher.matches())
			return parseType.Value;
		if(varMatcher.matches())
			return parseType.Variable;
		return parseType.Expression;
	}
	
	
	public String RemoveRedundantBrackets(String s){
		for(int i = 0; i < s.length(); ++i){
			String c = s.substring(i, i+1);
			if(c.compareTo(ob) == 0){
				int closing = FindClosingBracket(s, i);
				if (i == 0 && closing == s.length()-1){
					s = s.substring(i+1,closing);
					i = -1;
				}
			}
		}
		return s;
	}
	
	public int[] FindNextOperator(String s) {
		for (String op : operators) {
			for (int i = 0; i < s.length(); ++i) {
				int opSize = (op.length());
				String c = s.substring(i, (i + opSize  >= s.length()) ? i : i + opSize );
				if(c.contains(ob)){
					i = FindClosingBracket(s, i);
				}
				if (i < 0)
					return new int[] {};

			    boolean found = c.compareTo(op) == 0;
			    if(found){
			    	return new int[] { i, i + opSize };
			    }
			}
		}
		return null;
	}
	
	public String[] SplitExpression(String s, int begin, int end){
		String s0 = s.substring(0, begin);
		String s1 = s.substring(begin, end);
		String s2 = s.substring(end);
		return new String[] {s0,s1,s2};
	}

	public ParseNode Parse(String s) {
		ParseNode p0 = null, p1 = null;
		
		s = s.replace(" ", "");
		String[] subExp = null;
		
		s = RemoveRedundantBrackets(s);
		parseType t = DetermineParseType(s);
		switch(t){
			case Expression:
				int[] result = FindNextOperator(s);
				if (result != null) {
					subExp = SplitExpression(s,result[0], result[1]);
					p0 = Parse(subExp[0]);
					p1 = Parse(subExp[2]);
					return (p0 != null && p1 != null) ? new ParseNode(opMap.get(subExp[1]), p0, p1) : null;
				}
				break;
			case Value:
				int value = parseValue(s);
				return new ParseNode(value);
			case Variable:
				return new ParseNode(s);
			default:
				break;
		}
		
		return null;
	}
	
	public int parseValue(String s){
		if(hex.matcher(s).matches()){
			s = s.replaceAll("\\b0[xX]", "");
			return Integer.parseInt(s,16);
		}
		else 
			return Integer.parseInt(s);
	}
	
	public class ParseNode
	{
		private Op o;
		private ParseNode p0;
		private ParseNode p1;
		private int intValue;
		private String variableKey = null;
		private parseType ptype = null;
		
		public ParseNode(Op o, ParseNode p0, ParseNode p1){
			this.o = o;
			this.p0 = p0;
			this.p1 = p1;
			ptype = parseType.Expression;
		}
		
		public ParseNode(int value) {
			intValue = value;
			ptype = parseType.Value;
		}

		public ParseNode(String s) {
			variableKey  = s;
			ptype = parseType.Variable;
		}

		public float Eval(){
			switch(ptype){
				case Expression:
					return o.Ex(p0.Eval(), p1.Eval());
				case Value:
					return intValue;
				case Variable:
					return varMap.get(variableKey).floatValue();
				default:
					break;
			}
			return 0;
		}
	}
}
