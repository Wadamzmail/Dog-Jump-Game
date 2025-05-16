package dev.mutwakil.dogjump.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSlider;
import dev.mutwakil.dogjump.game.Dj;
import dev.mutwakil.dogjump.game.SettingsManager;
import dev.mutwakil.dogjump.res.Res;

public class SettingsDialog extends BaseDialog {
	private MySlider sfxSlider,musicSlider;
	private VisLabel  sfxLabel,musicLabel;
	public SettingsDialog(String s){
		super(s);
		init();
	}
	public SettingsDialog(){
		this("الإعدادات");
	}
	void init(){
		
		VisLabel.LabelStyle style = new VisLabel.LabelStyle();
		style.font = Res.font;
		style.fontColor = Res.DEEP_ORANGE;
		sfxSlider = new MySlider(0.0f,1f,0.01f,false);
		musicSlider = new MySlider(0.0f,1f,0.01f,false);
		sfxLabel = new VisLabel("المؤثرات الصوتية",style);
		musicLabel = new VisLabel("الموسيقى",style);
		sfxLabel.setAlignment(Align.center);
		musicLabel.setAlignment(Align.center);
		table.add().padTop(Dj.size(0.1f)).row();
		table.add(sfxSlider).pad(Dj.size(0.02f,this)).growX();
		table.add(sfxLabel).pad(Dj.size(0.02f,this)).growX().row();
		table.add(musicSlider).pad(Dj.size(0.02f,this)).growX();
		table.add(musicLabel).pad(Dj.size(0.02f,this)).growX().row();
		table.add().padBottom(Dj.size(0.1f)).row();
		load();
		listeners();
	}
	void load(){
		SettingsManager.load();
		sfxSlider.setValue(SettingsManager.get().sfxVolume);
		musicSlider.setValue(SettingsManager.get().musicVolume);
	}
	void listeners(){
		sfxSlider.setOnChangeListener((event,actor)->{
			SettingsManager.get().sfxVolume=sfxSlider.getValue();
			SettingsManager.save();
			});
		musicSlider.setOnChangeListener((event,actor)->{
			SettingsManager.get().musicVolume=musicSlider.getValue();
			SettingsManager.save();
			});
	}
}