package dev.mutwakil.dogjump.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisWindow;
import dev.mutwakil.dogjump.game.Dj;
import dev.mutwakil.dogjump.res.Res;

public class MyWindow extends VisWindow {
	protected VisWindow.WindowStyle windowStyle;
	protected Table titleTable,table,bg;
	public MyWindow(String s){
		super(s);
		windowStyle = new VisWindow.WindowStyle();
		windowStyle.background = Res.nineDrawable(Res.BACKGROUND);
		windowStyle.stageBackground = VisUI.getSkin().getDrawable("dialogDim");
		windowStyle.titleFont = Res.titleFont;
		windowStyle.titleFontColor = Color.WHITE;
		titleTable = getTitleTable();
		titleTable.setBackground(Res.nineDrawable(Res.TITLE_BG));
		setStyle(windowStyle);
		table = new Table();
		//table.setBackground(Res.nineDrawable(Res.TITLE_BG));
		table.setFillParent(true);
		add(table).grow();
		
	}
	public MyWindow(){
		this("");
		
	}
	@Override
	public void pack() {
		titleTable.setWidth(width(1.01f).get());
		titleTable.setHeight(Dj.size(0.15f));
		super.pack();
		
	}
	protected Value width(float f){
		return Value.percentWidth(f,table);
	}
	protected Value height(float f){
		return Value.percentHeight(f,table);
	}
}