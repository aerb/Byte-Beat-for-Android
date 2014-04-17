package com.tasty.fish.domain.implementation;

import com.tasty.fish.domain.IExpressionEvaluator;

import java.util.ArrayList;
import java.util.List;

public class DefaultExpressions {

    public static List<ByteBeatExpression> get() {
        return _defaults;
    }

    private static ArrayList<ByteBeatExpression> _defaults;
    static {
        _defaults = new ArrayList<ByteBeatExpression>();
        _defaults.add(
                new ByteBeatExpression(
                        "miiro",
                        "((p0*t)/50)*5&(((p1*t)/50)>>7)|((p2*t*3)/50)&(t*4>>10)",
                        0.5,
                        50,
                        50,
                        50
                ));
        _defaults.add(
                new ByteBeatExpression(
                        "bleullama-fun",
                        "((t%((p0*777)/50))|((p2*t)/50))&((0xFF*p1)/50)-t",
                        0.5,
                        50,
                        50,
                        50
            ));
        _defaults.add(
                new ByteBeatExpression(
                        "harism",
                        "((((p0*t)/50)>>1%((p1*128)/50))+20)*3*t>>14*t>>((p2*18)/50)",
                        0.5,
                        50,
                        50,
                        50
                ));
        _defaults.add(
                new ByteBeatExpression(
                        "tangent128",
                        "t*(((t>>9)&((p2*10)/50))|((((p1*t)/50)>>11)&24)^((t>>10)&15&(((p0*t)/50)>>15)))",
                        0.5,
                        50,
                        50,
                        50
                ));
        _defaults.add(
                new ByteBeatExpression(
                        "xpansive",
                        "((((p0*t)/50)*(((p1*t)/50)>>8|t>>9)&((p2*46)/50)&t>>8))^(t&t>>13|t>>6)",
                        0.5,
                        50,
                        50,
                        50
                ));
        _defaults.add(
                new ByteBeatExpression(
                        "tejeez",
                        "(((p0*t)/50)*(((p1*t)/50)>>5|t>>8))>>(((p2*t)/50)>>16)",
                        0.5,
                        50,
                        50,
                        50
                ));
        _defaults.add(
                new ByteBeatExpression(
                        "simple",
                        "t*t/p0",
                        0.5,
                        50,
                        50,
                        50
                ));
    }
}
