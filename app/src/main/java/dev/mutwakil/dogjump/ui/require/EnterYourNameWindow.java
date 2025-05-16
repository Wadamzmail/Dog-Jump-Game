package dev.mutwakil.dogjump.ui.require;

import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisImage;
import dev.mutwakil.dogjump.game.Dj;
import dev.mutwakil.dogjump.game.PlayerData;
import dev.mutwakil.dogjump.ui.BaseAlertWindow;
import dev.mutwakil.dogjump.ui.MyLabel;
import dev.mutwakil.dogjump.ui.MyTextButton;
import dev.mutwakil.dogjump.ui.MyTextField;

public class EnterYourNameWindow extends BaseAlertWindow{
	private VisImage image;
	private MyTextField nameField;
	private MyTextButton confirm;
	private MyLabel enterYourName;
	private MyTextButton save;
	public EnterYourNameWindow(){
		super("");
		bg.clear();
		bg.add(table).width(Dj.width(0.98f,bg)).center();
		enterYourName = new MyLabel("أكتب إسمك");
		save = new MyTextButton("حفظ");
		enterYourName.setAlignment(Align.center);
		nameField = new MyTextField();
		nameField.setAlignment(Align.center);
		nameField.setText(PlayerData.get().name);
		save.setOnClickListener((event,x,y)->{
			PlayerData.get().name = nameField.getText().trim();
			PlayerData.save();
			hide();
			});
		table.add(enterYourName).pad(Dj.size(0.02f,this)).growX().row();
		table.add(nameField).width(Dj.size(0.6f,table)).pad(Dj.size(0.02f,this)).height(Dj.size(0.06f,this)).center().fillX().row();
		table.add(save).center().width(Dj.size(0.3f,table)).center();
	}
}