package com.tasty.fish.utils.parser;

public interface IExpressionNode {

    // For the time being, leaving interface as double and casting if necessary.
    // This should have less overhead then using wrapper types for primitives,
    // however this assumption should be re-evaluated.
    boolean isFixed();
    double eval();
}
