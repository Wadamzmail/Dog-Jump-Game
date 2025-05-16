package dev.mutwakil.dogjump.actor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import dev.mutwakil.dogjump.MainActivity;
import dev.mutwakil.dogjump.res.Res;

public class PuzzleArea extends DropTargetActor {
	private int row;
	private int col;
	private Animation<TextureRegion> bg;
	private PuzzlePiece dog;
	private PuzzleArea[][]targets;
	private int id=-1;

	public PuzzleArea(float x, float y, Stage s, PuzzleArea[][]targets) {
		super(x, y, s);
		this.targets = targets;
		//loadTexture("images/target.png");
		//setAnimation(bg);
		loadTexture(Res.TARGET);
	}

	public void setRow(int r) {
		row = r;
	}

	public void setCol(int c) {
		col = c;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}
	public void setId(int id){
		this.id = id;
	}
	public int getId(){
		return id;
	}
	public PuzzleArea getRightArea() {
		return targets[getCol() + 1][getRow()];
	}
	
	public PuzzleArea getLeftArea() {
		return targets[getCol() - 1][getRow()];
	}
	
	public PuzzleArea getTopArea() {
		return targets[getCol()][getRow() - 1];
	}
	
	public PuzzleArea getBottomArea() {
		return targets[getCol()][getRow() + 1];
	}
	
	public boolean hasLeftArea() {
		if (getCol() - 1 < 0)
		return false;
		return targets[getCol() - 1][getRow()] != null;
	}
	
	public boolean hasRightArea() {
		if (getCol() + 1 > 4)
		return false;
		return targets[getCol() + 1][getRow()] != null;
	}
	
	public boolean hasTopArea() {
		if (getRow() - 1 < 0)
		return false;
		try {
			return targets[getCol()][getRow() - 1] != null;
			} catch (Exception e) {
			
			MainActivity.toast(e.toString());
		}
		return false;
	}
	
	public boolean hasBottomArea() {
		if (getRow() + 1 > 4)
		return false;
		return targets[getCol()][getRow() + 1] != null;
	}
	
	public void setDog(PuzzlePiece dog){
		this.dog = dog;
	}
	public PuzzlePiece getDog(){
		return dog;
	}
	public boolean hasDog(){
		return dog!=null;
	}
}