package dev.mutwakil.dogjump.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public interface OnChange {
	public void changed(ChangeListener.ChangeEvent event, Actor actor);
}