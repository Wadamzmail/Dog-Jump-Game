package dev.mutwakil.dogjump.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

public class CameraShake {
	private float duration, intensity;
	private float timeLeft;
	private OrthographicCamera camera;
	private Vector3 originalPosition;
	
	public CameraShake(OrthographicCamera camera) {
		this.camera = camera;
		this.originalPosition = new Vector3(camera.position);
	}
	
	public void shake(float duration, float intensity) {
		this.duration = duration;
		this.intensity = intensity;
		this.timeLeft = duration;
	}
	
	public void update(float delta) {
		if (timeLeft > 0) {
			timeLeft -= delta;
			float currentIntensity = intensity * (timeLeft / duration);
			float offsetX = (float)(Math.random() - 0.5f) * 2 * currentIntensity;
			float offsetY = (float)(Math.random() - 0.5f) * 2 * currentIntensity;
			
			camera.position.set(originalPosition.x + offsetX, originalPosition.y + offsetY, originalPosition.z);
			camera.update();
			} else {
			camera.position.set(originalPosition);
		}
	}
}