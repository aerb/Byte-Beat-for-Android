package com.tasty.fish.domain;

import com.tasty.fish.utils.FastParse;

public class ByteBeatExpression {

	private FastParse _parser;
	private double[] _args = {0,0,0};
	private float _timescale = 0;
	private String _expression;
	private float _t = 0;
	private String _name;

	
	public ByteBeatExpression(String name, String expression, float timescale, float a1, float a2, float a3){
		_name = name;
		_timescale = timescale;
		_args[0] = a1;
		_args[1] = a2;
		_args[2] = a3;
		_expression = expression;
	}
	
	public String expressions(){
		return _expression;
	}
	
	public boolean tryParse(){
		_parser = new FastParse();
		_parser.setVariable("t", 0);
		_parser.setVariable("p1", _args[0]);
		_parser.setVariable("p2", _args[1]);
		_parser.setVariable("p3", _args[2]);
		return  _parser.tryParse(_expression);
	}

	public byte getNext() {
		_t += _timescale;
		_parser.setVariable("t", (int)_t);
		return (byte)_parser.evaluate();
	}

	public int getTime() {
		return (int)_t;
	}

	public void updateTimeScale(float inc) {
		_timescale = inc;
	}

	public void updateArgument(int i, float x) {
		if(i < 3 && i >= 0)
			_args[i] = x;
	}

	public void resetTime() {
		_t = 0;
	}
	
	public String toString(){
		return _name;
	}

	public float getSpeed() {
		return _timescale;
	}
	
	public double getArgs(int i) {
		return _args[i];
	}

}
