package dev.mutwakil.dogjump.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.VisUI;
import dev.mutwakil.dogjump.res.Res;

public class MySlider extends Slider {
	private OnChange change;
	public MySlider (float min, float max, float stepSize, boolean vertical) {
		super(min, max, stepSize, vertical, VisUI.getSkin());
		init();
	}
	
	public MySlider (float min, float max, float stepSize, boolean vertical, String styleName) {
		super(min, max, stepSize, vertical, VisUI.getSkin(), styleName);
		init();
	}
	
	public MySlider (float min, float max, float stepSize, boolean vertical, SliderStyle style) {
		super(min, max, stepSize, vertical, style);
		init();
	}

	void init(){
		SliderStyle style = new SliderStyle();
		style.background = Res.drawable(Res.SLIDER);
		style.knob = Res.drawable(Res.SLIDER_KNOB);
		style.knobDown = Res.drawable(Res.SLIDER_KNOB_DOWN);
		setStyle(style);
		addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(change!=null)change.changed(event,actor);
			}
			
		});
	}
	public void setOnChangeListener(OnChange change){
		this.change = change;
	}
}