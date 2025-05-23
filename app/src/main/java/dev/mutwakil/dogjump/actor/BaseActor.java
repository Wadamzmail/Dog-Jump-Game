package dev.mutwakil.dogjump.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.TextureArray;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Intersector.MinimumTranslationVector;
import dev.mutwakil.dogjump.game.Dj;
import java.util.ArrayList;

public class BaseActor extends Actor {
	public Stage parent;
	private Animation<TextureRegion> animation;
	private float elapsedTime;
	private boolean animationPaused;
	private static Rectangle worldBounds;
	private Polygon boundaryPolygon;
	protected Vector2 velocityVec;
	private float maxSpeed;
	private float deceleration;
	private Vector2 accelerationVec;
	private float acceleration;

	public BaseActor(float x, float y, Stage s) {
		super();
		setPosition(x, y);
		s.addActor(this);
		animation = null;
		elapsedTime = 0;
		animationPaused = false;
		maxSpeed = 1000;
		deceleration = 0;
		accelerationVec = new Vector2(0, 0);
		acceleration = 0;
		velocityVec = new Vector2(0, 0);
		//setScale(Jigsaw.instance.scaleX,Jigsaw.instance.scaleY);
	}

	public void setAnimation(Animation<TextureRegion> anim) {
		animation = anim;
		TextureRegion tr = animation.getKeyFrame(0);
		float w = tr.getRegionWidth();
		float h = tr.getRegionHeight();
		setSize(w, h);
		setOrigin(w / 2, h / 2);
	}

	public void setAnimationPaused(boolean pause) {
		animationPaused = pause;
	}

	@Override
	public void act(float dt) {
		super.act(dt);
		if (!animationPaused)
			elapsedTime += dt;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		//Gdx.gl.glClearColor(0,0,0, 1);
		//Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.draw(batch, parentAlpha);
		// apply color tint effect

		Color c = getColor();
		batch.setColor(c.r, c.g, c.b, c.a);
		if (animation != null && isVisible())
			batch.draw(animation.getKeyFrame(elapsedTime), getX(), getY(), getOriginX(), getOriginY(), getWidth(),
					getHeight(), getScaleX(), getScaleY(), getRotation());
	}

	public Animation<TextureRegion> loadAnimationFromFiles(String[] fileNames, float frameDuration, boolean loop) {
		int fileCount = fileNames.length;
		Array<TextureRegion> textureArray = new Array<TextureRegion>();
		for (int n = 0; n < fileCount; n++) {
			String fileName = fileNames[n];
			Texture texture = new Texture(Gdx.files.internal(fileName));
			texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			textureArray.add(new TextureRegion(texture));
		}
		Animation<TextureRegion> anim = new Animation<TextureRegion>(frameDuration, textureArray);
		if (loop)
			anim.setPlayMode(Animation.PlayMode.LOOP);
		else
			anim.setPlayMode(Animation.PlayMode.NORMAL);
		if (animation == null)
			setAnimation(anim);
		return anim;
	}

	public Animation<TextureRegion> loadAnimationFromSheet(String fileName, int rows, int cols, float frameDuration,
			boolean loop) {
		Texture texture = new Texture(Gdx.files.internal(fileName), true);
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		int frameWidth = texture.getWidth() / cols;
		int frameHeight = texture.getHeight() / rows;
		TextureRegion[][] temp = TextureRegion.split(texture, frameWidth, frameHeight);
		Array<TextureRegion> textureArray = new Array<TextureRegion>();
		for (int r = 0; r < rows; r++)
			for (int c = 0; c < cols; c++)
				textureArray.add(temp[r][c]);
		Animation<TextureRegion> anim = new Animation<TextureRegion>(frameDuration, textureArray);
		if (loop)
			anim.setPlayMode(Animation.PlayMode.LOOP);
		else
			anim.setPlayMode(Animation.PlayMode.NORMAL);
		if (animation == null)
			setAnimation(anim);
		return anim;
	}

	public Animation<TextureRegion> loadTexture(String fileName) {
		String[] fileNames = new String[1];
		fileNames[0] = fileName;
		return loadAnimationFromFiles(fileNames, 1, true);
	}
	public Animation<TextureRegion> loadTexture(Texture texture){
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		Array<TextureRegion> textureArray = new Array<TextureRegion>();
		textureArray.add(new TextureRegion(texture));
	    Animation<TextureRegion> anim = new Animation<TextureRegion>(1,textureArray);
	    if (true)
	    anim.setPlayMode(Animation.PlayMode.LOOP);
	    else
	    anim.setPlayMode(Animation.PlayMode.NORMAL);
	    if (animation == null)
	    setAnimation(anim);
	    return anim;
	}

	public boolean isAnimationFinished() {
		return animation.isAnimationFinished(elapsedTime);
	}

	public static void setWorldBounds(float width, float height) {
		worldBounds = new Rectangle(0, 0, width, height);
	}

	public static void setWorldBounds(BaseActor ba) {
		setWorldBounds(ba.getWidth(), ba.getHeight());
	}

	public void boundToWorld() {
		// check left edge
		if (getX() < 0)
			setX(0);
		// check right edge
		if (getX() + getWidth() > worldBounds.width)
			setX(worldBounds.width - getWidth());
		// check bottom edge
		if (getY() < 0)
			setY(0);
		// check top edge
		if (getY() + getHeight() > worldBounds.height)
			setY(worldBounds.height - getHeight());
	}

	public void alignCamera() {
		Camera cam = this.getStage().getCamera();
		Viewport v = this.getStage().getViewport();
		// center camera on actor
		cam.position.set(this.getX() + this.getOriginX(), this.getY() + this.getOriginY(), 0);
		// bound camera to layout
		cam.position.x = MathUtils.clamp(cam.position.x, cam.viewportWidth / 2,
				worldBounds.width - cam.viewportWidth / 2);
		cam.position.y = MathUtils.clamp(cam.position.y, cam.viewportHeight / 2,
				worldBounds.height - cam.viewportHeight / 2);
		cam.update();
	}

	public void updateX(float x) {
		/*	if (x < 0) {
				this.setScaleX((float) Math.negateExact((int) getScaleX()));
			} else if (x > 0) {
				this.setScaleX(getScaleX());
			}
			*/
	}

	public void setBoundaryRectangle() {
		float w = getWidth();
		float h = getHeight();
		float[] vertices = { 0, 0, w, 0, w, h, 0, h };
		boundaryPolygon = new Polygon(vertices);
	}

	public void setBoundaryPolygon(int numSides) {
		float w = getWidth();
		float h = getHeight();
		float[] vertices = new float[2 * numSides];
		for (int i = 0; i < numSides; i++) {
			float angle = i * 6.28f / numSides;
			// x-coordinate
			vertices[2 * i] = w / 2 * MathUtils.cos(angle) + w / 2;
			// y-coordinate
			vertices[2 * i + 1] = h / 2 * MathUtils.sin(angle) + h / 2;
		}
		boundaryPolygon = new Polygon(vertices);
	}

	public Polygon getBoundaryPolygon() {
		boundaryPolygon.setPosition(getX(), getY());
		boundaryPolygon.setOrigin(getOriginX(), getOriginY());
		boundaryPolygon.setRotation(getRotation());
		boundaryPolygon.setScale(getScaleX(), getScaleY());
		return boundaryPolygon;
	}

	public boolean overlaps(BaseActor other) {
		Polygon poly1 = this.getBoundaryPolygon();
		Polygon poly2 = other.getBoundaryPolygon();
		// initial test to improve performance
		if (!poly1.getBoundingRectangle().overlaps(poly2.getBoundingRectangle()))
			return false;
		return Intersector.overlapConvexPolygons(poly1, poly2);
	}

	public void centerAtPosition(float x, float y) {
		setPosition(x - getWidth() / 2, y - getHeight() / 2);
	}

	public void centerAtActor(BaseActor other) {
		centerAtPosition(other.getX() + other.getWidth() / 2, other.getY() + other.getHeight() / 2);
	}

	public void setOpacity(float opacity) {
		this.getColor().a = opacity;
	}

	public Vector2 preventOverlap(BaseActor other) {
		Polygon poly1 = this.getBoundaryPolygon();
		Polygon poly2 = other.getBoundaryPolygon();
		// initial test to improve performance
		if (!poly1.getBoundingRectangle().overlaps(poly2.getBoundingRectangle()))
			return null;
		MinimumTranslationVector mtv = new MinimumTranslationVector();
		boolean polygonOverlap = Intersector.overlapConvexPolygons(poly1, poly2, mtv);
		if (!polygonOverlap)
			return null;
		this.moveBy(mtv.normal.x * mtv.depth, mtv.normal.y * mtv.depth);
		return mtv.normal;
	}

	public static ArrayList<BaseActor> getList(Stage stage, String className) {
		ArrayList<BaseActor> list = new ArrayList<BaseActor>();
		Class theClass = null;
		try {
			theClass = Class.forName(className);
		} catch (Exception error) {
			error.printStackTrace();

		}
		for (Actor a : stage.getActors()) {
			if (theClass.isInstance(a))
				list.add((BaseActor) a);
		}
		return list;
	}

	public static int count(Stage stage, String className) {
		return getList(stage, className).size();
	}

	public void wrapAroundWorld() {
		if (getX() + getWidth() < 0)
			setX(worldBounds.width);
		if (getX() > worldBounds.width)
			setX(-getWidth());
		if (getY() + getHeight() < 0)
			setY(worldBounds.height);
		if (getY() > worldBounds.height)
			setY(-getHeight());
	}

	public void bounceOff(BaseActor other) {
		Vector2 v = this.preventOverlap(other);
		if (Math.abs(v.x) >= Math.abs(v.y)) //horizontal bounce
			this.velocityVec.x *= -1;
		else // vertical bounce
			this.velocityVec.y *= -1;
	}

	public void setSpeed(float speed) {
		// if length is zero, then assume motion angle is zero degrees
		if (velocityVec.len() == 0)
			velocityVec.set(speed, 0);
		else
			velocityVec.setLength(speed);
	}

	public float getSpeed() {
		return velocityVec.len();
	}

	public void setMotionAngle(float angle) {
		velocityVec.setAngle(angle);
	}

	public float getMotionAngle() {
		return velocityVec.angle();
	}

	public boolean isMoving() {
		return (getSpeed() > 0);
	}

	public void setAcceleration(float acc) {
		acceleration = acc;
	}

	public void accelerateAtAngle(float angle) {
		accelerationVec.add(new Vector2(acceleration, 0).setAngle(angle));
	}

	public void accelerateForward() {
		accelerateAtAngle(getRotation());
	}

	public void setMaxSpeed(float ms) {
		maxSpeed = ms;
	}

	public void setDeceleration(float dec) {
		deceleration = dec;
	}

	public void applyPhysics(float dt) {
		// apply acceleration
		velocityVec.add(accelerationVec.x * dt, accelerationVec.y * dt);
		float speed = getSpeed();
		// decrease speed (decelerate) when not accelerating
		if (accelerationVec.len() == 0)
			speed -= deceleration * dt;
		// keep speed within set bounds
		speed = MathUtils.clamp(speed, 0, maxSpeed);
		// update velocity
		setSpeed(speed);
		// apply velocity
		moveBy(velocityVec.x * dt, velocityVec.y * dt);
		// reset acceleration
		accelerationVec.set(0, 0);
	}

}