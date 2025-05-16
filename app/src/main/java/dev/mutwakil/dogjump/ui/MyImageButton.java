package dev.mutwakil.dogjump.ui;

import android.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.kotcrab.vis.ui.widget.VisImageButton;
import dev.mutwakil.dogjump.game.SettingsManager;
import dev.mutwakil.dogjump.res.Res;

 public class MyImageButton extends VisImageButton{
	 private OnClick click;
	 private Drawable originalBg,clickedBg;
	 public MyImageButton(MyImageButton.VisImageButtonStyle style){
		 super(style);
		 init();
		 listener();
	 }
	 public MyImageButton(Drawable s){
		 super(s);
		 MyImageButton.VisImageButtonStyle imageButtonStyle = new MyImageButton.VisImageButtonStyle();
		 imageButtonStyle.imageUp = s;
		 imageButtonStyle.up = Res.drawable(Res.BOX_UP);
		 imageButtonStyle.down = Res.drawable(Res.BOX_DOWN);
		 setStyle(imageButtonStyle);
		 init();
		 listener();
	 }
	 void init(){
		 setTransform(true);
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