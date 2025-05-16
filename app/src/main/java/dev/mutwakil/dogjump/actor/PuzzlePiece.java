package dev.mutwakil.dogjump.actor;

import android.os.Handler;
import android.os.Looper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import dev.mutwakil.dogjump.MainActivity;
import dev.mutwakil.dogjump.Turn;
import dev.mutwakil.dogjump.game.Dj;
import dev.mutwakil.dogjump.game.GameType;
import dev.mutwakil.dogjump.game.SettingsManager;
import dev.mutwakil.dogjump.model.Move;
import dev.mutwakil.dogjump.res.Res;
import dev.mutwakil.dogjump.screen.GameScreen;
import dev.mutwakil.lh.GameClient;
import dev.mutwakil.lh.GameServer;
import java.util.ArrayList;

public class PuzzlePiece extends DragAndDropActor {
	protected int row;
	protected int col;
	private PuzzleArea puzzleArea, prevArea;
	private Sound dragSound;
	private Sound dropSound;
	private PuzzleArea home;
	protected PuzzleArea[][] targets;
	protected int prevRow = 50, prevCol = 50;
	protected GameScreen game;
	protected Action coubleCoolDown;
	protected int id =-1;

	public PuzzlePiece(float x, float y, Stage s, GameScreen game) {
		super(x, y, s, game);
		this.game = game;
		this.targets = game.targets;
		//dragSound = Jigsaw.instance.resManager.DRAG_SOUND;
		//dropSound = Jigsaw.instance.resManager.DROP_SOUND;
	}

	public void setRow(int r) {
		if (prevRow == 50)
			prevRow = r;
		row = r;
	}

	public void setCol(int c) {
		if (prevCol == 50)
			prevCol = c;
		col = c;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}
	public void setId(int i){
		id = i;
	}
	public int getId(){
		return id;
	}

	public void setPuzzleArea(PuzzleArea pa) {
		puzzleArea = pa;
	}

	public void setHome(PuzzleArea h) {
		home = h;
	}

	public PuzzleArea getHome() {
		return home;
	}

	public Boolean hasHome() {
		return home != null;
	}

	public PuzzleArea getPuzzleArea() {
		return puzzleArea;
	}

	public void clearPuzzleArea() {
		puzzleArea = null;
	}

	public boolean hasPuzzleArea() {
		return puzzleArea != null;
	}

	public boolean isCorrectlyPlaced() {
		return hasPuzzleArea() && this.getRow() == puzzleArea.getRow() && this.getCol() == puzzleArea.getCol();
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

	protected void initOnDrag() {

	}

	protected void initOnDropStart() {

	}

	protected void initOnDropEnd() {
		if (Math.abs(col - prevCol) == 2) {
			int middleCol = (col + prevCol) / 2;
			if (targets[middleCol][row] != null) {
				PuzzlePiece middlePiece = targets[middleCol][row].getDog();
				if (middlePiece != null && !middlePiece.getClass().getName().equals(getClass().getName())) {
					killing = true;
					game.couble = this;
					moveToActor(puzzleArea, 0.1f);
					middlePiece.kill();
					//if(game.gType==GameType.LOCALHOST)addAction(Actions.sequence(Actions.delay(0.19f), Actions.run(() ->game.checkWin())));
					if(game.activePointer!=-1&&point!=-2){
						if(game.activePointer==point){
							game.activePointer=-1;
							point=-2;
						}
					}
					canCouple();
					return;
				}
			}
		}

		if (Math.abs(row - prevRow) == 2) {
			int middleRow = (row + prevRow) / 2;
			if (targets[col][middleRow] != null) {
				PuzzlePiece middlePiece = targets[col][middleRow].getDog();
				if (middlePiece != null && !middlePiece.getClass().getName().equals(getClass().getName())) {
					killing = true;
					game.couble = this;
					moveToActor(puzzleArea, 0.1f);
					middlePiece.kill();
					//if(game.gType==GameType.LOCALHOST)addAction(Actions.sequence(Actions.delay(0.19f), Actions.run(() ->game.checkWin())));
					if(game.activePointer!=-1&&point!=-2){
						if(game.activePointer==point){
							game.activePointer=-1;
							point=-2;
						}
					}
					canCouple();
					return;
				}
			}
		}
		
		moveToActor(puzzleArea);
		if (prevArea != puzzleArea){
			game.moveTurn(this instanceof Bottom ? Turn.TOP : Turn.BOTTOM);
			Res.MOVE.play(1f);
		}
	}

	private void canCouple() {
		 addAction(Actions.sequence(Actions.delay(0.16f), Actions.run(() -> {
			 game.moveTurn(this instanceof Bottom ? Turn.TOP : Turn.BOTTOM);
		if (this instanceof Bottom) {
             if(checkCouple(Top.class)){
				if(game.gType==GameType.TABLE||game.isServer())game.couble.disabled=false;
				if(game.isClient())game.couble.disabled= true; 
				if(coubleCoolDown==null){
					coubleCoolDown = coubleCoolDown();
					addAction(coubleCoolDown);
					}else{
					removeAction(coubleCoolDown);
					coubleCoolDown = null;
				}
				return;
			 }
		} else if (this instanceof Top) {
			if(checkCouple(Bottom.class)){
				if(game.gType==GameType.TABLE||game.isClient())game.couble.disabled=false; 
				if(game.isServer())game.couble.disabled=true;
				if(coubleCoolDown==null){
				coubleCoolDown = coubleCoolDown();
			    addAction(coubleCoolDown);	
				}else{
				removeAction(coubleCoolDown);
				coubleCoolDown = null;
				}
				return;
			}
		}
		game.couble=null;
		})));
	}

	public void kill() {
		if(SettingsManager.get().sfxVolume!=0f)Res.EXPLOSION.play(SettingsManager.get().sfxVolume);
		addAction(Actions.sequence(Actions.delay(0.15f), Actions.run(() -> {
			game.shake.shake(0.8f, 15f);
			disabled = true;
			if(game.activePointer!=-1&&point!=-2){
				if(game.activePointer==point){
					game.activePointer=-1;
					point=-2;
				}
			}
			puzzleArea.setDog(null);
			if (this instanceof Top) {
				game.tops.remove(this);
				//MainActivity.toast("Top😵");
				game.bottomTable.add(this).size(this.getWidth() / 2);
				game.bottomTable.pack();
			} else if (this instanceof Bottom) {
                 game.bottoms.remove(this);
				//MainActivity.toast("Bottom🤧");
				game.topTable.add(this).size(this.getWidth() / 2);
				game.topTable.pack();
			}
			game.checkWin();
		})));
		
	}
	
	private Action coubleCoolDown(){
		return Actions.sequence(Actions.delay(2f),Actions.run(()->{
			if(game.couble!=null){
			  if(game.couble==this){
				game.couble.disabled=true;
				game.couble=null;
				if(point!=-2){
				game.activePointer=-1;
				point=-2;
				}
				self.addAction(Actions.scaleTo(1.00f, 1.00f,0.25f));
				moveToActor(puzzleArea);
				coubleCoolDown = null;
				}
			}
		}));
	}

	@Override
	public void onDragStart() {
		if (disabled)
			return;
		//dragSound.play(1f);
		//home.setTargetable(true);
		if (hasPuzzleArea()) {
			PuzzleArea pa = getPuzzleArea();
			pa.setTargetable(true);
			//clearPuzzleArea();
		}
		initOnDrag();
	}
   public void netMove(Move move){
	 //  MainActivity.toast(getCol()+" "+getRow());
	   PuzzleArea pa = game.targets[move.tC][move.tR];   
	  // if(!pa.hasDog()){
	   puzzleArea.setTargetable(false);
	   puzzleArea.setDog(null);
	   setDropTarget(pa);
	   prevArea = puzzleArea;
	   setPuzzleArea(pa);
	   setCol(pa.getCol());
	   setRow(pa.getRow());
	   pa.setDog(this);
	   initOnDropEnd();
	   pa.setTargetable(false);	   
	   prevCol = col;
	   prevRow = row;
	   toFront();
	  /* }else {
		   puzzleArea.setTargetable(true);
		   moveToActor(puzzleArea);
		   puzzleArea.setTargetable(false);
		   if(game.isServer()){
			   GameServer server =Dj.instance.server;
			   Move movee = new Move();
			   movee.dogId = getId();
			   movee.tC = getPuzzleArea().getCol();
			   movee.tR = getPuzzleArea().getRow();
			   server.sendTCP(movee);
		   }
		   if(game.isClient()){
			   GameClient client =Dj.instance.client;
			   Move movee = new Move();
			   movee.dogId = getId();
			   movee.tC = getPuzzleArea().getCol();
			   movee.tR = getPuzzleArea().getRow();
			   client.sendTCP(movee);
		   }
		   
	   }*/
   }
	@Override
	public void onDrop() {
		//MainActivity.toast(getCol()+" "+getRow());
		if (disabled)
			return;
		//dropSound.play(1f);
		initOnDropStart();
		if (hasDropTarget()) {
			if (getDropTarget() instanceof PuzzleArea) {
				puzzleArea.setDog(null);
				puzzleArea.setTargetable(false);
				prevArea = puzzleArea;
				PuzzleArea pa = (PuzzleArea) getDropTarget();
				//moveToActoe(pa);
				setPuzzleArea(pa);
				setCol(pa.getCol());
				setRow(pa.getRow());
				pa.setDog(this);
				initOnDropEnd();
				pa.setTargetable(false);		
				prevCol = col;
				prevRow = row;
			} else {
				/*PuzzleArea ha = (PuzzleArea) getDropTarget();
				if (ha == home)
					moveToActor(ha);
				else
					moveToActor(home);
					*/
			}
		} else {
			puzzleArea.setTargetable(true);
			moveToActor(puzzleArea);
			puzzleArea.setTargetable(false);
        
		}
		//home.setTargetable(false);
	}

	protected void targetable(Class<?> c, boolean t) {
		if (hasLeftArea()) {
			PuzzleArea pa = getLeftArea();
			if (pa.hasDog()) {
				PuzzlePiece dog = pa.getDog();
				if (c.isInstance(dog)) {
					if (dog.hasLeftArea()) {
						if (!dog.getLeftArea().hasDog()) {
							dog.getLeftArea().setTargetable(t);
						}
					}
				} else {
					if (dog.hasLeftArea()) {
						if (!dog.getLeftArea().hasDog()) {
							dog.getLeftArea().setTargetable(false);
						}
					}
				}
			} else {
				pa.setTargetable(t);
			}
		}
		if (hasRightArea()) {
			PuzzleArea pa = getRightArea();
			if (pa.hasDog()) {
				PuzzlePiece dog = pa.getDog();
				if (c.isInstance(dog)) {
					if (dog.hasRightArea()) {
						if (!dog.getRightArea().hasDog()) {
							dog.getRightArea().setTargetable(t);
						}
					}
				} else {
					if (dog.hasRightArea()) {
						if (!dog.getRightArea().hasDog()) {
							dog.getRightArea().setTargetable(false);
						}
					}
				}
			} else {
				pa.setTargetable(t);
			}
		}
		if (hasTopArea()) {
			PuzzleArea pa = getTopArea();
			if (pa.hasDog()) {
				PuzzlePiece dog = pa.getDog();
				if (c.isInstance(dog)) {
					if (dog.hasTopArea()) {
						if (!dog.getTopArea().hasDog()) {
							dog.getTopArea().setTargetable(t);
						}
					}
				} else {
					if (dog.hasTopArea()) {
						if (!dog.getTopArea().hasDog()) {
							dog.getTopArea().setTargetable(false);
						}
					}
				}
			} else {
				pa.setTargetable(t);
			}
		}
		if (hasBottomArea()) {
			PuzzleArea pa = getBottomArea();
			if (pa.hasDog()) {
				PuzzlePiece dog = pa.getDog();
				if (c.isInstance(dog)) {
					if (dog.hasBottomArea()) {
						if (!dog.getBottomArea().hasDog()) {
							dog.getBottomArea().setTargetable(t);
						}
					}
				} else {
					if (dog.hasBottomArea()) {
						if (!dog.getBottomArea().hasDog()) {
							dog.getBottomArea().setTargetable(false);
						}
					}
				}
			} else {
				pa.setTargetable(t);
			}
		}
	}

	protected boolean checkCouple(Class<?> c) {
		if (hasLeftArea()) {
			PuzzleArea pa = getLeftArea();
			if (pa.hasDog()) {
				PuzzlePiece dog = pa.getDog();
				if (c.isInstance(dog)) {
					if (dog.hasLeftArea()) {
						if (!dog.getLeftArea().hasDog()) {
							return true;
						}
					}
				}
			}
		}
		if (hasRightArea()) {
			PuzzleArea pa = getRightArea();
			if (pa.hasDog()) {
				PuzzlePiece dog = pa.getDog();
				if (c.isInstance(dog)) {
					if (dog.hasRightArea()) {
						if (!dog.getRightArea().hasDog()) {
							return true;
						}
					}
				}
			}
		}
		if (hasTopArea()) {
			PuzzleArea pa = getTopArea();
			if (pa.hasDog()) {
				PuzzlePiece dog = pa.getDog();
				if (c.isInstance(dog)) {
					if (dog.hasTopArea()) {
						if (!dog.getTopArea().hasDog()) {
							return true;
						}
					}
				} 
			}
		}
		if (hasBottomArea()) {
			PuzzleArea pa = getBottomArea();
			if (pa.hasDog()) {
				PuzzlePiece dog = pa.getDog();
				if (c.isInstance(dog)) {
					if (dog.hasBottomArea()) {
						if (!dog.getBottomArea().hasDog()) {
							return true;
						}
					}
				} 
			}
		}
		return false;
	}
}