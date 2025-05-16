package dev.mutwakil.dogjump.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.kotcrab.vis.ui.widget.VisTextField;
import dev.mutwakil.dogjump.res.Res;

public class MyTextField extends VisTextField{
	public MyTextField(){
		super();
		VisTextFieldStyle style = new VisTextFieldStyle();
		style.font = Res.font;
		style.fontColor = Color.BLACK;
		style.background = Res.nineDrawable(Res.TF_BG);
		style.selection = Res.drawable(Res.TF_SELECTION);
		style.cursor = Res.drawable(Res.TF_CURSOR);
		setStyle(style);
	}
}