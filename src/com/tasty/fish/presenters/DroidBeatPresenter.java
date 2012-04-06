package com.tasty.fish.presenters;

import java.util.ArrayList;

import com.tasty.fish.domain.ByteBeatExpression;
import com.tasty.fish.interfaces.IDroidBeatView;
import com.tasty.fish.utils.AndroidAudioDevice;
import com.tasty.fish.utils.FastParse;

public class DroidBeatPresenter {
	
	private IDroidBeatView _view;
	private boolean _die;
	private ArrayList<ByteBeatExpression> _exps = null;
	private ByteBeatExpression _exp = null;
	
	public DroidBeatPresenter(IDroidBeatView view){
		_view = view;
		_exps = new ArrayList<ByteBeatExpression>();
	}
	
	public void stopAudioThread(){
		_die = true;
	}
	
	private void updateView(){
		_view.updateSeekerSpeedPostion(_exp.getSpeed());
		_view.updateSeekerPostion(0,_exp.getArgs(0));
		_view.updateSeekerPostion(1,_exp.getArgs(1));
		_view.updateSeekerPostion(2,_exp.getArgs(2));
	}
	
	public void setActiveExpression(int id){
		_exp = _exps.get(id);
		updateView();
	}
	
	public boolean addNewExpression(String title, String exp){
		ByteBeatExpression e = new ByteBeatExpression(title, exp, 0.5f, 1f, 1f, 1f);
		if(!e.tryParse()) return false;
		_exps.add(e);
		return true;
	}
	
	public void startAudioThread() {
		_die = false;
		new Thread(new Runnable() {
			public void run() {
				AndroidAudioDevice audio = new AndroidAudioDevice();
				byte samples[] = new byte[1024];
				while (true) {
					
					for (int i = 0; i < samples.length; i++)
						samples[i] = _exp.getNext();

					audio.writeSamples(samples);

					_view.displayBuffer(samples, _exp.getTime());
					_view.updateT(_exp.getTime());

					if (_die) {
						audio.stop();
						return;
					}
				}
			}
		}).start();
	}

	public void updateTimeScale(float inc) {
		_exp.updateTimeScale(inc);
	}

	public void updateArgument(int i, float x) {
		_exp.updateArgument(i,x);
	}

	public void resetTime() {
		_exp.resetTime();
	}

	public void resetArgs() {
		_exp.updateTimeScale(0.5f);
		_exp.updateArgument(0, 1f);
		_exp.updateArgument(1, 1f);
		_exp.updateArgument(2, 1f);
		updateView();
	}
	
	public ArrayList<ByteBeatExpression> getExpressions(){
		return _exps;
	}
}
