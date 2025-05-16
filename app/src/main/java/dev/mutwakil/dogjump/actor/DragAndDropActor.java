package dev.mutwakil.dogjump.actor;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.Stage;
import dev.mutwakil.dogjump.screen.GameScreen;

public class DragAndDropActor extends BaseActor {
	public DragAndDropActor self;
	private float grabOffsetX;
	private float grabOffsetY;
	private DropTargetActor dropTarget;
	private boolean draggable;
	private float startPositionX;
	private float startPositionY;
	public boolean disabled = false;
	public boolean killing = false;
	private GameScreen game;
	protected int point =-2;

	public DragAndDropActor(float x, float y, Stage s,GameScreen game) {
		super(x, y, s);
		this.game = game;
		self = this;
		addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float offsetX, float offsetY, int pointer, int button) {
				if(disabled)return false;
				if(killing)return false;
				if(game.activePointer==-1){
				game.activePointer = pointer;
				point = pointer;
				self.grabOffsetX = offsetX;
				self.grabOffsetY = offsetY;
				self.startPositionX = self.getX();
				self.startPositionY = self.getY();
				self.toFront();
				self.addAction(Actions.scaleTo(1.1f, 1.1f, 0.25f));
				self.onDragStart();
				return true;
				}
				return false;
			}

			public void touchDragged(InputEvent event, float offsetX, float offsetY, int pointer) {
				if(disabled)return;
				if(killing)return;
				if(pointer!=game.activePointer)return;
				float deltaX = offsetX - self.grabOffsetX;
				float deltaY = offsetY - self.grabOffsetY;
				self.moveBy(deltaX, deltaY);
			}

			public void touchUp(InputEvent event, float offsetX, float offsetY, int pointer, int button) {
				if(disabled)return;
				if(killing)return;
				if(pointer==game.activePointer){
					game.activePointer=-1;
					point = -2;
				self.setDropTarget(null);
				// keep track of distance to closest object
				float closestDistance = Float.MAX_VALUE;
				for (BaseActor actor : BaseActor.getList(self.getStage(), DropTargetActor.class.getName())) {
					DropTargetActor target = (DropTargetActor) actor;
					if (target.isTargetable() && self.overlaps(target)) {
						float currentDistance = Vector2.dst(self.getX(), self.getY(), target.getX(), target.getY());
						// check if this target is even closer
						if (currentDistance < closestDistance) {
							self.setDropTarget(target);
							closestDistance = currentDistance;
						}
					}
				}
				self.addAction(Actions.scaleTo(1.00f, 1.00f,0.25f));
				self.onDrop();
				killing = false;
				}
			}
		});
		draggable = true;
	}

	public void act(float dt) {
		super.act(dt);
	}

	public boolean hasDropTarget() {
		return (dropTarget != null);
	}

	public void setDropTarget(DropTargetActor dt) {
		dropTarget = dt;
	}

	public DropTargetActor getDropTarget() {
		return dropTarget;
	}

	public void setDraggable(boolean d) {
		draggable = d;
	}

	public boolean isDraggable() {
		return draggable;
	}

	public void moveToActor(BaseActor other) {
		float x = other.getX() + (other.getWidth() - this.getWidth()) / 2;
		float y = other.getY() + (other.getHeight() - this.getHeight()) / 2;
		addAction(Actions.moveTo(x, y, 0.50f, Interpolation.pow3));
	}
	
	public void moveToActor(BaseActor other,float p) {
		float x = other.getX() + (other.getWidth() - this.getWidth()) / 2;
		float y = other.getY() + (other.getHeight() - this.getHeight()) / 2;
		addAction(Actions.moveTo(x, y, p, Interpolation.pow3));
	}

	public void moveToStart() {
		addAction(Actions.moveTo(startPositionX, startPositionY, 0.50f, Interpolation.pow3));
	}

	public void onDragStart() {
	}

	public void onDrop() {
	}

}
