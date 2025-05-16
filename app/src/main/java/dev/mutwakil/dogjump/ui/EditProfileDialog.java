package dev.mutwakil.dogjump.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisTextField;
import dev.mutwakil.dogjump.game.Dj;
import dev.mutwakil.dogjump.game.PlayerData;

public class EditProfileDialog extends BaseDialog{
	private VisImage image;
	private MyTextField nameField;
	private MyTextButton confirm;
	private MyLabel enterYourName;
	private MyTextButton save;
	public EditProfileDialog(){
		super("تعديل الملف الشخصي");
		enterYourName = new MyLabel("إسمك");
		enterYourName.setAlignment(Align.center);
		nameField = new MyTextField();
		save = new MyTextButton("حفظ");
		PlayerData.load();
		nameField.setText(PlayerData.get().name);
		save.setOnClickListener((event,x,y)->{
			PlayerData.load();
			PlayerData.get().name=nameField.getText().trim();
			PlayerData.save();
			hide();
		});
		table.add(nameField).pad(Dj.size(0.02f,this)).height(Dj.size(0.06f,this)).center().growX();
		table.add(enterYourName).pad(Dj.size(0.02f,this)).growX().row();
		table.add(save).width(width(0.4f)).height(Dj.size(0.06f,this)).padTop(Dj.size(0.025f)).padBottom(Dj.size(0.025f)).center().colspan(2);
	}
}