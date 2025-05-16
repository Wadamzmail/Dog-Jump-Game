package dev.mutwakil.dogjump.actor;

import com.badlogic.gdx.scenes.scene2d.Stage;

public class HoliderArea extends DropTargetActor {
	public HoliderArea(float x, float y, Stage s) {
		super(x, y, s);
		loadTexture("border.png"); 
	}
}