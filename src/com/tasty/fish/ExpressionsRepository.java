package com.tasty.fish;

import com.tasty.fish.domain.ByteBeatExpression;

import java.util.ArrayList;
import java.util.List;

public class ExpressionsRepository implements IExpressionsRepository{

    private final ArrayList<ByteBeatExpression> _expressions;
    private ByteBeatExpression _active;
    private final ArrayList<IExpressionsRepositoryListener> _listeners;

    public ExpressionsRepository() {
        _expressions = new ArrayList<ByteBeatExpression>();
        _listeners = new ArrayList<IExpressionsRepositoryListener>();
        initializeExpressions();
    }

    private static String[] s_predefinedTitles = { "bleullama-fun", "harism",
            "tangent128", "miiro", "xpansive", "tejeez" };
    private static String[] s_predefinedExpressions = {
            "((t%(p1*777))|(p3*t))&((0xFF*p2))-t",
            "(((p1*t)>>1%(p2*128))+20)*3*t>>14*t>>(p3*18)",
            "t*(((t>>9)&(p3*10))|(((p2*t)>>11)&24)^((t>>10)&15&((p1*t)>>15)))",
            "(p1*t)*5&((p2*t)>>7)|(p3*t*3)&(t*4>>10)",
            "(((p1*t)*((p2*t)>>8|t>>9)&(p3*46)&t>>8))^(t&t>>13|t>>6)",
            "((p1*t)*((p2*t)>>5|t>>8))>>((p3*t)>>16)"};

    private void initializeExpressions(){
        for (int i = 0; i < s_predefinedExpressions.length; ++i)
            addNewExpression(s_predefinedTitles[i],
                    s_predefinedExpressions[i]);

        addNewExpression("custom", "t");
        _active = getExpressions().get(0);
    }

    private boolean addNewExpression(String title, String exp) {
        ByteBeatExpression e = new ByteBeatExpression(title, exp, 0.5f, 1f, 1f, 1f);
        _expressions.add(e);
        return true;
    }

    @Override
    public void setIExpressionsRepositoryListener(IExpressionsRepositoryListener listener) {
        _listeners.add(listener);
    }

    @Override
    public List<ByteBeatExpression> getExpressions() {
        return _expressions;
    }

    public ByteBeatExpression getActive() {
        return _active;
    }

    @Override
    public void setActiveExpression(int position) {
        _active = _expressions.get(position);
        for(IExpressionsRepositoryListener l: _listeners)
            l.OnActiveExpressionChanged();
    }
}
