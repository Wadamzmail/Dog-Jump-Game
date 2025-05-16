package dev.mutwakil.dogjump.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisTextButton;
import dev.mutwakil.dogjump.game.SettingsManager;
import dev.mutwakil.dogjump.res.Res;

public class MyTextButton extends VisTextButton{
	private OnClick click;
	private Drawable originalBg,clickedBg;
	public MyTextButton(String s,MyTextButton.VisTextButtonStyle style){
		super(s,style);
		init();
		listener();
	}
	public MyTextButton(String s){
		super(s);
		MyTextButton.VisTextButtonStyle textButtonStyle = new MyTextButton.VisTextButtonStyle();
		textButtonStyle.up = Res.nineDrawable(Res.BTTN_UP);
		textButtonStyle.down = Res.nineDrawable(Res.BTTN_DOWN);
		textButtonStyle.font = Res.font;
		textButtonStyle.fontColor = Color.WHITE;
		setStyle(textButtonStyle);
		init();
		listener();
	}
	void init(){
		setTransform(true);
		getLabel().setAlignment(Align.center);
	}
	@Override
	public void setStyle(Button.ButtonStyle style) {
		originalBg = style.up;
		clickedBg = style.down;
		super.setStyle(style);	
	}
	private void listener(){
		addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event,float x,float y){	
				if(originalBg!=null&&clickedBg!=null){ 
				getStyle().up = clickedBg;		
				addAction(Actions.sequence(
				Actions.delay(0.11f),
				Actions.run(() -> getStyle().up = originalBg)
				));
				}
				if(SettingsManager.get().sfxVolume!=0f)Res.BUTTON_CLICK.play(SettingsManager.get().sfxVolume);
				if(click!=null)click.onClick(event,x,y);
			}
		});
	}
	public void setOnClickListener(OnClick click){
		this.click = click;
	}
}