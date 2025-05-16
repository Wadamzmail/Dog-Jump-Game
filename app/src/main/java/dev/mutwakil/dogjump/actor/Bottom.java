package dev.mutwakil.dogjump.actor;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import dev.mutwakil.dogjump.game.Dj;
import dev.mutwakil.dogjump.model.Move;
import dev.mutwakil.dogjump.res.Res;
import dev.mutwakil.dogjump.screen.GameScreen;
import dev.mutwakil.lh.GameClient;
import dev.mutwakil.lh.GameServer;

public class Bottom extends PuzzlePiece {
	public Bottom(float x,float y,Stage s,GameScreen t){
		super(x,y,s,t);
		//loadTexture("images/red01.png");
		loadTexture(Res.BOTTOM);
	}
	
	@Override
	protected void initOnDrag() {
		super.initOnDrag();
		targetable(Top.class,true);
	}
	@Override
	protected void initOnDropStart() {
		super.initOnDropStart();
		targetable(Top.class,false);
	}
	@Override
	protected void initOnDropEnd() {
		super.initOnDropEnd();
		if(game.isServer()){
			GameServer server =Dj.instance.server;
			Move move = new Move();
			move.dogId = getId();
			move.tC = getPuzzleArea().getCol();
			move.tR = getPuzzleArea().getRow();
			server.sendTCP(move);
		}
	}
	
}