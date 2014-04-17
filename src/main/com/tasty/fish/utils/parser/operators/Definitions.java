package com.tasty.fish.utils.parser.operators;

import java.util.HashMap;
import java.util.Map;

public class Definitions {
    public static Map<String, Iop> getOperatorDefinitions() {
        return _ops;
    }

    private static final HashMap<String, Iop> _ops;

    static {
        _ops = new HashMap<String, Iop>();
        _ops.put("<=", new Iop() {
            public long Ex(long a, long b) {
                return a <= b ? 1 : 0;
            }
        });
        _ops.put(">=", new Iop() {
            public long Ex(long a, long b) {
                return a >= b ? 1 : 0;
            }
        });
        _ops.put("!=", new Iop() {
            public long Ex(long a, long b) {
                return a != b ? 1 : 0;
            }
        });
        _ops.put("==", new Iop() {
            public long Ex(long a, long b) {
                return a == b ? 1 : 0;
            }
        });
        _ops.put("|", new Iop() {
            public long Ex(long a, long b) {
                return a | b;
            }
        });
        _ops.put("&", new Iop() {
            public long Ex(long a, long b) {
                return a & b;
            }
        });
        _ops.put("^", new Iop() {
            public long Ex(long a, long b) {
                return a ^ b;
            }
        });
        _ops.put("%", new Iop() {
            public long Ex(long a, long b) {
                return b != 0 ? a % b : 0;
            }
        });
        _ops.put("+", new Iop() {
            public long Ex(long a, long b) {
                return a + b;
            }
        });
        _ops.put("-", new Iop() {
            public long Ex(long a, long b) {
                return a - b;
            }
        });
        _ops.put("*", new Iop() {
            public long Ex(long a, long b) {
                return a * b;
            }
        });
        _ops.put("/", new Iop() {
            public long Ex(long a, long b) {
                return b != 0 ? a / b : Long.MAX_VALUE;
            }
        });
        _ops.put(">>", new Iop() {
            public long Ex(long a, long b) {
                return a >> b;
            }
        });
        _ops.put("<<", new Iop() {
            public long Ex(long a, long b) {
                return a << b;
            }
        });
    }
}
