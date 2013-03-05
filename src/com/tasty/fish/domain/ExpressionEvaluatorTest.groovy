package com.tasty.fish.domain

class ExpressionEvaluatorTest extends GroovyTestCase{
    void DoABunchOfTimes(actual, expecting){
        println expecting
        def eval = new ExpressionEvaluator();
        eval.setExpression(new ByteBeatExpression("Hello",(String)expecting,1,1,1,1));
        def average = 0;
        for (i in 1..1000){
            def start = System.nanoTime();
            def sample = eval.getNextSample();
            def end = System.nanoTime();
            average += end - start
            print sample + " "
            assert (byte)actual(i) == sample
        }
        average = (average/1000) / 1000000000
        def freq = ((1/average)/1000)
        println average + " sec period"
        println  freq + " kHz"
    }

    void testAdd(){
        def equ = { t -> t + t }
        DoABunchOfTimes(equ, "t+t")
    }

    void testMult(){
        def equ = { t -> t * t }
        DoABunchOfTimes(equ, "t*t")
    }

    void testComplex(){
        def equ = { t -> t*t>>5&t }
        DoABunchOfTimes(equ, "t*t>>5&t")
    }

    void testComplex2(){
        def equ = { t -> (t*t>>5&t)/999 }
        DoABunchOfTimes(equ, "t*t>>5&t)/999")
    }
}
