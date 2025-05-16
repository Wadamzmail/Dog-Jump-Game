package dev.mutwakil.dogjump.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisLabel;
import dev.mutwakil.dogjump.res.Res;

public class MyLabel extends VisLabel{
    public MyLabel(String txt,LabelStyle style){
		super(txt,style);
	}
	public MyLabel(String txt){
		setText(txt);
		init();
	}
    void init(){
		LabelStyle style = new LabelStyle();
		style.font = Res.font;
		style.fontColor = Color.BLACK;
		setStyle(style);
		setAlignment(Align.center);
	}
}