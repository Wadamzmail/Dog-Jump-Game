package dev.mutwakil.dogjump.actor;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import dev.mutwakil.dogjump.game.Dj;
import dev.mutwakil.dogjump.model.Move;
import dev.mutwakil.dogjump.res.Res;
import dev.mutwakil.dogjump.screen.GameScreen;
import dev.mutwakil.lh.GameClient;

public class Top extends PuzzlePiece{
	public Top(float x,float y,Stage s,GameScreen t){
		super(x,y,s,t);
		//loadTexture("images/greenn.png");	
		loadTexture(Res.TOP);
	}
	@Override
	protected void initOnDrag() {
		super.initOnDrag();
		targetable(Bottom.class,true);
	}
	@Override
	protected void initOnDropStart() {
		super.initOnDropStart();
		targetable(Bottom.class,false);
	}
	@Override
	protected void initOnDropEnd() {
		super.initOnDropEnd();
		if(game.isClient()){
			GameClient client =Dj.instance.client;
			 Move move = new Move();
			 move.dogId = getId();
			 move.tC = getPuzzleArea().getCol();
			 move.tR = getPuzzleArea().getRow();
			 client.sendTCP(move);
		}
	}
	 
}