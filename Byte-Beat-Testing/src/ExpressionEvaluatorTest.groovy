import com.tasty.fish.domain.implementation.ByteBeatExpression
import com.tasty.fish.domain.implementation.ExpressionEvaluator

class ExpressionEvaluatorTest extends GroovyTestCase{
    def p1 = 1;
    def p2 = 1;
    def p3 = 1;

    void DoABunchOfTimes(actual, expecting, fixedPointOperators){
        println expecting

        def eval = new ExpressionEvaluator();
        eval.setExpression(new ByteBeatExpression("Hello",(String)expecting,1,p1,p2,p3));

        def average = 0;
        def reps = 100000;
        print "["
        for (i in 1..reps){
            def start = System.nanoTime();
            byte expectingSample = eval.getNextSample();
            def end = System.nanoTime();
            average += end - start
            byte actualSample = (byte)actual((long)i);
            assert actualSample == expectingSample, "Failed assertion on sample " + i
        }
        average = (average/reps) / 1000000000
        def freq = ((1/average)/1000)
        println "]"
        println average + " sec period"
        println  freq + " kHz"
        println "============================================"
    }

    void testAdd(){
        def equ = { t -> t + t }
        DoABunchOfTimes(equ, "t+t", true)
    }

    void testMult(){
        def equ = { t -> (long)t * (long)t }
        DoABunchOfTimes(equ, "t*t", true)
    }

    void test_bleullama(){
        def equ = { t -> ((t%((int)p1*777))|((int)p3*t))&((0xFF*(int)p2))-t }
        DoABunchOfTimes(equ , "((t%(p1*777))|(p3*t))&((0xFF*p2))-t", true)
    }

    void test_tarism(){
        def equ = { t ->      (((p1*t)>>1%(p2*128))+20)*3*t>>14*t>>(p3*18)}
        DoABunchOfTimes(equ ,"(((p1*t)>>1%(p2*128))+20)*3*t>>14*t>>(p3*18)", true)
    }

    void test_tangent128(){
        def equ = { t -> t*(((t>>9)&(p3*10))|(((p2*t)>>11)&24)^((t>>10)&15&((p1*t)>>15)))}
        DoABunchOfTimes(equ , "t*(((t>>9)&(p3*10))|(((p2*t)>>11)&24)^((t>>10)&15&((p1*t)>>15)))", true)
    }

    void test_miiro(){
        def equ = { t -> (p1*t)*5&((p2*t)>>7)|(p3*t*3)&(t*4>>10)}
        DoABunchOfTimes(equ , "(p1*t)*5&((p2*t)>>7)|(p3*t*3)&(t*4>>10)", true)
    }

    void test_xpansive(){
        def equ = { t -> (((p1*t)*((p2*t)>>8|t>>9)&(p3*46)&t>>8))^(t&t>>13|t>>6)}
        DoABunchOfTimes(equ , "(((p1*t)*((p2*t)>>8|t>>9)&(p3*46)&t>>8))^(t&t>>13|t>>6)", true)
    }

    void test_tejeez(){
        def equ = { t -> ((p1*t)*((p2*t)>>5|t>>8))>>((p3*t)>>16)}
        DoABunchOfTimes(equ , "((p1*t)*((p2*t)>>5|t>>8))>>((p3*t)>>16)", true)
    }
}
