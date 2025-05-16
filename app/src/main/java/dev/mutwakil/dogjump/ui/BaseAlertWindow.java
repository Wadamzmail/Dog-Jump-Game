package dev.mutwakil.dogjump.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisDialog;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import dev.mutwakil.dogjump.game.Dj;
import dev.mutwakil.dogjump.res.Res;

public class BaseAlertWindow extends VisDialog {
	protected VisTable bg;
	protected VisTable table,titleTable;
	private VisLabel title;
	private float width,height,pw=0.12f,ph=0.1f;
	protected MyImageButton close;
	public BaseAlertWindow(String s){
		super("");
		reset();
		setFillParent(true);
		//setBackground((Drawable)null);
		//setBackground(Res.nineDrawable(Res.WINDOW_DIM));
		setBackground(VisUI.getSkin().getDrawable("dialogDim"));
		if(Dj.width(1f)<Dj.height(1f)){
			width = pw;
			height = ph;
			}else{
			width = ph;
			height = pw;
		}
		bg = new VisTable();
		table = new VisTable();
		titleTable = new VisTable();
		bg.defaults().center();
		titleTable.setBackground(Res.nineDrawable(Res.HIGHLIGHT));
		table.setBackground(Res.nineDrawable(Res.DTABLE_BG));
		VisImageButton.VisImageButtonStyle closeStyle = new VisImageButton.VisImageButtonStyle();
		closeStyle.imageUp = Res.drawable(Res.CLOSE);
		closeStyle.up = Res.drawable(Res.CANCEL_UP);
		closeStyle.down = Res.drawable(Res.CANCEL_DOWN);
	    close = new MyImageButton(closeStyle);
		close.setOnClickListener((event,x,y)->hide());
		titleTable.add().growX().center();
		titleTable.add(close).size(Dj.size(0.05f,this)).padRight(Dj.size(0.015f,this)).right().bottom();
		//bg.add(close).size(Dj.size(0.05f,this)).pad(Dj.size(0.015f,this)).right().top().row();
		bg.setBackground(Res.nineDrawable(Res.BACKGROUND));
		add(bg).width(Dj.size(0.6f,this));//.padLeft(Dj.width(width)).padRight(Dj.width(width)).padTop(Dj.height(height)).padBottom(Dj.height(height));
		bg.add(titleTable).width(Value.percentWidth(0.98f,bg)).height(Dj.size(0.07f,this)).align(Align.top).row();
		bg.add(table).width(Value.percentWidth(0.98f,bg)).center();
		table.padBottom(Dj.size(0.05f));
		table.padTop(Dj.size(0.05f));
		listener();
	}
	public BaseAlertWindow(){
		this("");
	}
	private void listener(){
		addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event,float x,float y){
				 
			}
		});
	}
	@Override
	public void pack() {
		if(getStage()!=null)getStage().act();
		Gdx.app.postRunnable(() -> {
			table.invalidateHierarchy();
			titleTable.invalidateHierarchy();
			bg.invalidateHierarchy();
		});
		titleTable.pack();
		table.pack();
		bg.pack();
		super.pack();
	}
	@Override
	public VisDialog show(Stage stage) {
		super.show(stage);
		stage.act();
		pack();
		toFront();
		return this;
	}
	protected Value width(float f){
		return Value.percentWidth(f,table);
	}
	protected Value height(float f){
		return Value.percentHeight(f,table);
	}
}