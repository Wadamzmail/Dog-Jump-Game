package dev.mutwakil.dogjump.screen;

import android.opengl.GLES20;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.StringBuilder;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import deep.richi.libgdx.rtl.RtlController;
import dev.mutwakil.dogjump.MainActivity;
import dev.mutwakil.dogjump.Room;
import dev.mutwakil.dogjump.Turn;
import dev.mutwakil.dogjump.actor.Bottom;
import dev.mutwakil.dogjump.actor.PuzzleArea;
import dev.mutwakil.dogjump.actor.PuzzlePiece;
import dev.mutwakil.dogjump.actor.Top;
import dev.mutwakil.dogjump.game.Dj;
import dev.mutwakil.dogjump.game.GameType;
import dev.mutwakil.dogjump.game.PlayerData;
import dev.mutwakil.dogjump.model.Move;
import dev.mutwakil.dogjump.model.PlayerModel;
import dev.mutwakil.dogjump.res.Res;
import dev.mutwakil.dogjump.script.Onigais;
import dev.mutwakil.dogjump.script.Ping;
import dev.mutwakil.dogjump.ui.EditProfileDialog;
import dev.mutwakil.dogjump.ui.MenuDialog;
import dev.mutwakil.dogjump.ui.MyImageButton;
import dev.mutwakil.dogjump.game.CameraShake;
import dev.mutwakil.dogjump.ui.MyTextField;
import dev.mutwakil.dogjump.ui.ingame.GameEndLocalHost;
import dev.mutwakil.dogjump.ui.ingame.GameEndTable;
import dev.mutwakil.dogjump.ui.ingame.RespondReCreateRequest;
import dev.mutwakil.dogjump.ui.ingame.SendReCreateRequest;
import dev.mutwakil.dogjump.ui.require.EnterYourNameWindow;
import dev.mutwakil.dogjump.util.Utils;
import dev.mutwakil.lh.GameClient;
import dev.mutwakil.lh.OnReseved;
import dev.mutwakil.lh.ServerState;
import java.util.ArrayList;
import java.util.HashMap;

public class GameScreen extends ScreenAdapter {
	public Dj game;
	private Stage gameStage,uiStage;
	public VisTable uiTable,gameTable,bottomTable,topTable,topArea,bottomArea;
	private Viewport viewport;
	private int thisWidth,thisHeight;
	public PuzzleArea[][] targets= new PuzzleArea[5][5];
	public ArrayList<Top> tops = new ArrayList<Top>();
	public ArrayList<Bottom> bottoms = new ArrayList<Bottom>();
	public PuzzlePiece couble;
	public  VisLabel debugging,topTurnLabel,bottomTurnLabel,topDonkeysLabel,bottomDonkeysLabel;
	private VisLabel.LabelStyle turnLabelStyle,donkeysLabelStyle;
	private VisImage bg;
	public int activePointer=-1;
	public CameraShake shake;
	public Turn currentTurn = Turn.BOTTOM;
	public int donkeysTop=0,donkeysBottom=0;
	public GameType gType;
	public HashMap<Integer,Bottom> boIds = new HashMap<Integer,Bottom>();
	public HashMap<Integer,Top>toIds = new HashMap<Integer,Top>();
	public PlayerModel opponent = new PlayerModel();
	public Onigais myOnigai = Onigais.NONE;
	public SendReCreateRequest reCreateRequest;
	public RespondReCreateRequest respondReCreateRequest;
	public Ping ping = new Ping();
	public Timer.Task pingTask;
	public GameEndTable gameEndTable;
	public GameEndLocalHost gameEndLocalHost;
	public boolean isEnded = false;
	
	public GameScreen(GameType gType){
		this.game = Dj.instance;
		this.gType = gType;
	}

	@Override
	public void show() {
		super.show();
		viewport = new ScreenViewport();
		viewport.setScreenWidth(Gdx.graphics.getWidth());
		viewport.setScreenHeight(Gdx.graphics.getHeight());
		gameStage = new Stage(viewport,game.batch);
		uiStage = new Stage(viewport,game.batch);
		game.input.addProcessor(uiStage);
		game.input.addProcessor(gameStage);
		uiTable = new VisTable();
		gameTable = new VisTable();
		uiTable.setFillParent(true);
		gameTable.setFillParent(true);
		//uiTable.debug();
		bg = new VisImage();
		bg.setDrawable(Res.drawable(Res.BG));
		bg.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		gameStage.addActor(bg);
		gameStage.addActor(gameTable);
		topTable = new VisTable();
		bottomTable = new VisTable();
		gameTable.add(topTable).grow().padBottom(Dj.width(0.05f)).row();
		gameTable.add(bottomTable).grow().padTop(Dj.width(0.05f)).bottom();
		gameTable.pack();
		uiStage.addActor(uiTable);
		debugging = new VisLabel();
		debugging.setWrap(true);
		shake = new CameraShake((OrthographicCamera)viewport.getCamera());
			if(isClient()){
				//viewport.getCamera().up.set(0,-1,0);
				//viewport.getCamera().direction.set(0,0,-1);
				//viewport.getCamera().update();
				initClient();
				}
		if(isServer()){
			initServer();
		}
		Gdx.app.postRunnable(()->create());
		initUi();
		initDialogs();
		initOnigais();
	}
	public void initServer(){
		game.server.setListeners(new OnReseved(){
			@Override
			public void onReserved(Object object) {
				if(opponent==null||opponent.name.equals("")){
					game.server.sendTCP(Onigais.WONNA_YOUR_DATA);
				}
				if(object instanceof Move){
					Top tp = toIds.get(((Move)object).dogId);
					if(tp!=null)tp.netMove((Move)object);
					/*tops.forEach(top->{
						if(top.getId()==((Move)object).dogId){
							top.netMove((Move)object);
						}
					});*/
				}
				if(object instanceof PlayerModel){
					opponent = (PlayerModel)object;
					if(currentTurn==Turn.TOP){
						topTurnLabel.setText(turn(opponentName()));
						bottomTurnLabel.setText("");
					}
					if(pingTask==null)
					pingTask = Timer.schedule(new Timer.Task() {
						@Override
						public void run() {
							if(game.server!=null)game.server.sendTCP(ping);
						}
					}, 0, 1);
					
				}
				if(object instanceof Onigais){
					Onigais onigai = (Onigais)object;
					if(myOnigai==Onigais.RECREATE_PLEASE){
						switch(onigai){
							case RECREATE_ACCEPTED:{
								if(isEnded)
								 newMatch();
								else		
								 reCreate();
								myOnigai= Onigais.NONE;
								reCreateRequest.hide();
								gameEndLocalHost.hide();
								break;
							}
							case RECREATE_REJECTED:{
								myOnigai=Onigais.NONE;
								reCreateRequest.hide();
								break;
							}
						}
				    }else if(onigai==Onigais.RECREATE_PLEASE){
						respondReCreateRequest.show(uiStage);
					}else if(onigai==Onigais.RECREATE_CANCELED){
							myOnigai= Onigais.NONE;
							respondReCreateRequest.hide();
					}
					if(onigai==Onigais.WONNA_YOUR_DATA){
						PlayerData.load();
						game.server.sendTCP(PlayerData.get());
					}
				}
			}
			
		},new ServerState(){
			@Override
			public void created(String _ip) {
			}

			@Override
			public void closed(String _ip) {
			}

			@Override
			public void connected() {
				MainActivity.toast("Connected");
			}

			@Override
			public void disconnected() {
				MainActivity.toast("Disconnected");
				disConnected();
			}
			
		});
	}
	public void disConnected(){
		Gdx.app.postRunnable(()->{
		if(pingTask!=null)pingTask.cancel();
		if(isServer()&&game.server!=null)game.server.dispose();
		if(isClient()&&game.client!=null)game.client.dispose();
		game.server=null;
		game.client=null;
		game.setScreen(new MenuScreen());
		});
	}
	public void initClient(){
		if(!game.ip.equals("")){
			game.client = new GameClient(game.ip,new OnReseved(){	
				@Override
				public void onReserved(Object object) {
					if(opponent==null||opponent.name.equals("")){
						game.client.sendTCP(Onigais.WONNA_YOUR_DATA);
					}
					if(object instanceof Move){
						Bottom bm = boIds.get(((Move)object).dogId);
						if(bm!=null)bm.netMove((Move)object);
						/*bottoms.forEach(bottom->{
							if(bottom.getId()==((Move)object).dogId){
								bottom.netMove((Move)object);
							}
						});*/
					}
					if(object instanceof PlayerModel){
						opponent = (PlayerModel)object;
						if(currentTurn==Turn.BOTTOM){
						topTurnLabel.setText(turn(opponentName()));
						bottomTurnLabel.setText("");
						}
					}
					if(object instanceof Onigais){
						Onigais onigai = (Onigais)object;
						if(myOnigai==Onigais.RECREATE_PLEASE){
							switch(onigai){
								case RECREATE_ACCEPTED:{
									if(isEnded)
									 newMatch();
									else
									 reCreate();
									myOnigai= Onigais.NONE;
									reCreateRequest.hide();
									gameEndLocalHost.hide();
									break;
								}
								case RECREATE_REJECTED:{
									myOnigai=Onigais.NONE;
									reCreateRequest.hide();
									break;
								}
							}
						}else if(onigai==Onigais.RECREATE_PLEASE){
							respondReCreateRequest.show(uiStage);
					    }else if(onigai==Onigais.RECREATE_CANCELED){
							myOnigai= Onigais.NONE;
							respondReCreateRequest.hide();
						}
						if(onigai==Onigais.WONNA_YOUR_DATA){
							PlayerData.load();
							game.client.sendTCP(PlayerData.get());
						}
					}
				}
				
				},new GameClient.OnConnectionStateChanged(){
				
				@Override
				public void onConnected() {
					MainActivity.toast("connected");
				}
				
				
				@Override
				public void onDisconnected() {
					MainActivity.toast("disconnected");
					disConnected();
				}
				
				
				@Override
				public void onFailed(Exception e) {
					e.printStackTrace();
					MainActivity.toast("Failed :"+e);
				}
				
			});
		}
	}
	private void initOnigais(){
		reCreateRequest = new SendReCreateRequest(this);
		respondReCreateRequest = new RespondReCreateRequest(this);
		
	}
	private void initDialogs(){
		gameEndTable = new GameEndTable(this);
		gameEndLocalHost = new GameEndLocalHost(this);
	}
	private void initUi(){
		turnLabelStyle = new VisLabel.LabelStyle();
		turnLabelStyle.font= Res.turnFont;
		turnLabelStyle.fontColor = Color.WHITE;
		donkeysLabelStyle = new VisLabel.LabelStyle();
		donkeysLabelStyle.font= Res.font;
		donkeysLabelStyle.fontColor = Color.WHITE;
		//outSide Top Table
		Group topAreaGroup = new Group();
		topArea = new VisTable();
		topArea.setBackground(VisUI.getSkin().getDrawable("dialogDim"));
		topArea.setTransform(true);
		topArea.setWidth(Dj.width(1f));
		topArea.setHeight(Dj.height(0.13f));
		
		topArea.setOrigin(topArea.getWidth() / 2f, topArea.getHeight() / 2f);
		
		if(gType==GameType.TABLE)topArea.setRotation(180);
		
		topAreaGroup.setSize(topArea.getWidth(), topArea.getHeight());
		topAreaGroup.addActor(topArea);
		
		uiTable.add(topAreaGroup).top().row();
		//inside top table
		topDonkeysLabel = new VisLabel();
		topDonkeysLabel.setStyle(donkeysLabelStyle);
		topDonkeysLabel.setAlignment(Align.center);
		topDonkeysLabel.setWrap(true);
		topTurnLabel = new VisLabel();
		topTurnLabel.setStyle(turnLabelStyle);
		topTurnLabel.setAlignment(Align.center);
		topArea.add(topDonkeysLabel).grow().center();
		topArea.add(topTurnLabel).grow().center();
		//Space between top and bottom tables
		uiTable.add().expand().fill().height(Value.percentHeight(0.65f,uiTable)).row();
		//add menu button
		VisImageButton.VisImageButtonStyle menuStyle = new VisImageButton.VisImageButtonStyle();
		menuStyle.imageUp = Res.drawable(Res.MENU);
		menuStyle.up = Res.drawable(Res.BOX_UP);
		menuStyle.down = Res.drawable(Res.BOX_DOWN);
		MyImageButton menu = new MyImageButton(menuStyle);
		menu.setOnClickListener((event,x,y)->{
			 new MenuDialog(this).show(uiStage);
			 //respondReCreateRequest.show(uiStage);
			 //uiStage.addActor(new MenuDialog(this));
			 /*bottoms.clear();
			 gType = GameType.LOCALHOST;
			 game.room=Room.CLIENT;
			 gameEndLocalHost.show(uiStage);*/
		});
		uiTable.add(menu).size(size(0.07f,uiTable)).row();
		//outSide Bottom Table
		bottomArea = new VisTable();
		bottomArea.setBackground(topArea.getBackground());
		uiTable.add(bottomArea).grow().bottom().row();
		//inside bottom table	
		bottomDonkeysLabel = new VisLabel();
		bottomDonkeysLabel.setStyle(donkeysLabelStyle);
		bottomDonkeysLabel.setAlignment(Align.center);
		bottomDonkeysLabel.setWrap(true);
		bottomTurnLabel = new VisLabel();
		bottomTurnLabel.setStyle(turnLabelStyle);
		bottomTurnLabel.setAlignment(Align.center);
		bottomArea.add(bottomDonkeysLabel).grow().center();
		bottomArea.add(bottomTurnLabel).grow().center();
		uiTable.pack();
		//topArea.setWidth(Value.percentWidth(1f).get(uiTable));
		//topArea.setHeight(Value.percentHeight(0.15f).get(uiTable));
		
	}
	public StringBuilder donkeysText(int i){							
		String str = "عدد الحمير : ";	
		return RtlController.getInstance().getRtl(str+i,true);
	}
	public StringBuilder turn(String name){
		String str = "دور ";
		return RtlController.getInstance().getRtl(str+name,true);
	}
	public String opponentName(){
		return opponent!=null?(opponent.name!=null?opponent.name:""):"";
	}
	public void reCreate(){
		toIds.clear();
		boIds.clear();
		tops.forEach(top->{
			top.clearListeners();
			top.clearPuzzleArea();
			top.clearActions();
			top.clear();
			top.remove();
		});
		bottoms.forEach(bottom->{
			bottom.clearListeners();
			bottom.clearPuzzleArea();
			bottom.clearActions();
			bottom.clear();
		});
		for(int r=0;r<5;++r){
			for(int c=0;c<5;++c){
				PuzzleArea target = targets[c][r];
				target.clear();
				target.remove();
			}
		}
		tops.clear();
		bottoms.clear();
		bottomTable.clear();
		topTable.clear();
		couble=null;
		donkeysBottom = 0;
		donkeysTop = 0;
		create();
	}
	public void newMatch(){
		toIds.clear();
		boIds.clear();
		tops.forEach(top->{
			top.clearListeners();
			top.clearPuzzleArea();
			top.clearActions();
			top.clear();
			top.remove();
		});
		bottoms.forEach(bottom->{
			bottom.clearListeners();
			bottom.clearPuzzleArea();
			bottom.clearActions();
			bottom.clear();
		});
		for(int r=0;r<5;++r){
			for(int c=0;c<5;++c){
				PuzzleArea target = targets[c][r];
				target.clear();
				target.remove();
			}
		}
		tops.clear();
		bottoms.clear();
		bottomTable.clear();
		topTable.clear();
		couble=null;
		create();
	}
	private void create(){
		thisWidth = Gdx.graphics.getWidth();
		thisHeight = Gdx.graphics.getHeight();
		int numberRows =5,numberCols=5;
		int fieldWidth = (int)size(0.90f);
		int fieldHeight = (int)size(0.90f);
		final int pieceWidth = fieldWidth / numberCols;
		final int pieceHeight = fieldHeight / numberRows;
		boolean isClient = gType==GameType.LOCALHOST&&game.room==Room.CLIENT;
		//isClient=true; //for Debugging
		for (int r =0; r<numberRows; r++) {
			for (int c = 0; c<numberCols; c++) {
				int marginX = (thisWidth - fieldWidth) / 2;
				int marginY = (thisHeight - fieldHeight) / 2;
				int areaX = isClient?(0 + marginX) + pieceWidth * (numberCols-1-c):(0 + marginX) + pieceWidth * c;
				int areaY = isClient?((thisHeight + (thisHeight / 65 - (thisHeight / 70))) - marginY - pieceHeight)
				- pieceHeight * (numberRows-1-r):((thisHeight + (thisHeight / 65 - (thisHeight / 70))) - marginY - pieceHeight)
				- pieceHeight * r;
				PuzzleArea pa = new PuzzleArea(areaX, areaY, gameStage,targets);
				pa.setSize(pieceWidth, pieceHeight);
				pa.setBoundaryRectangle();
				pa.setRow(r);
				pa.setCol(c);
				pa.setId((c*r)+Utils.getRandomNumber());
				targets[c][r]= pa;
				//add Top Dogs
				boolean top1 = r<3;
				boolean top2 = r==2&&c<=2;
				boolean bottom1 = r>1;
				boolean bottom2 = r==2&&c>=2;
				if(top1){
					boolean con = false;
					if(top2)con = true;
					if(!con){
					Top top = new Top(areaX,areaY,gameStage,this);
					top.setSize(pieceWidth-pieceWidth*0.3f,pieceHeight-pieceHeight*0.3f);
					top.setBoundaryRectangle();
					top.setRow(r);
					top.setCol(c);
					top.moveToActor(pa);
					top.setId(1000+r*numberCols+c);
					pa.setDog(top);
					top.setPuzzleArea(pa);
					pa.setTargetable(false);
					tops.add(top);
					toIds.put(top.getId(),top);
	                }
				}
				//add Bottom dogs
				if(bottom1){
					boolean con = false;
					if(bottom2)con = true;
					if(!con){
					Bottom bottom = new Bottom(areaX,areaY,gameStage,this);
					bottom.setSize(pieceWidth-pieceWidth*0.3f,pieceHeight-pieceHeight*0.3f);
					bottom.setBoundaryRectangle();
					bottom.setRow(r);
					bottom.setCol(c);
					bottom.moveToActor(pa);
					bottom.setId(2000+r*numberCols+c);
					pa.setDog(bottom);
					bottom.setPuzzleArea(pa);
					pa.setTargetable(false);
					bottoms.add(bottom);
					boIds.put(bottom.getId(),bottom);
					}
				}
			}
		}	
		topTable.toFront();
		bottomTable.toFront(); 
		moveTurn(currentTurn);
		isEnded = false;
		topDonkeysLabel.setText(donkeysText(donkeysTop));
		bottomDonkeysLabel.setText(donkeysText(donkeysBottom));
	}
	public boolean isClient(){
		return gType==GameType.LOCALHOST&&game.room==Room.CLIENT;
	}
	public boolean isServer(){
		return gType==GameType.LOCALHOST&&game.room==Room.SERVER;
	}
	public void moveTurn(Turn turn){
		currentTurn = turn;
		switch (turn){
			case TOP:{
			if(gType==GameType.TABLE){	
			bottoms.forEach(bottom->bottom.disabled=true);	
			tops.forEach(top->top.disabled=false);
			topTurnLabel.setText("دورك");
			bottomTurnLabel.setText("");
			greenBottom(false);
			return;
			}	
			if(isClient()){
				tops.forEach(top->top.disabled=false);
				bottoms.forEach(bottom->bottom.disabled=true);
				topTurnLabel.setText("");
				bottomTurnLabel.setText("دورك");
				greenBottom(true);
			}else if(isServer()){
				tops.forEach(top->top.disabled=true);
				bottoms.forEach(bottom->bottom.disabled=true);
			    topTurnLabel.setText(turn(opponentName()));
				bottomTurnLabel.setText("");
				greenBottom(false);
			}
		    	break;
			}
			case BOTTOM:{
			if(gType==GameType.TABLE){
			tops.forEach(top->top.disabled=true);
			bottoms.forEach(bottom->bottom.disabled=false);	
			topTurnLabel.setText("");
			bottomTurnLabel.setText("دورك");
			greenBottom(true);
			return;
			}
			 if(isClient()){
				 tops.forEach(top->top.disabled=true);
				 bottoms.forEach(bottom->bottom.disabled=true);				
				  topTurnLabel.setText(turn(opponentName()));
				 bottomTurnLabel.setText("");
				 greenBottom(false);
			 }else if(isServer()){
				 tops.forEach(top->top.disabled=true);
				 bottoms.forEach(bottom->bottom.disabled=false);
				 topTurnLabel.setText("");
				 bottomTurnLabel.setText("دورك");
				 greenBottom(true);
			 }
				break;
			}
		}
	}
	public void greenBottom(boolean t){
		if(t){
			bottomArea.setBackground(Res.nineDrawable(Res.TABLE_BG_GREEN));
			topArea.setBackground(Res.nineDrawable(Res.TABLE_BG));
		}else{
			topArea.setBackground(Res.nineDrawable(Res.TABLE_BG_GREEN));
			bottomArea.setBackground(Res.nineDrawable(Res.TABLE_BG));
		}
	}
	public void checkWin(){
		//MainActivity.toast(tops.size()+"\n"+bottoms.size());
		if(tops.size()==0){
			if(donkeysBottom==0){
				donkeysTop++;
			}else if(donkeysBottom>0){
				donkeysBottom--;
			}
			endGame();
		}
		if(bottoms.size()==0){
			if(donkeysTop==0){
				donkeysBottom++;
			}else if(donkeysTop>0){
				donkeysTop--;
			}
			endGame();
		}
	}
	public void endGame(){
		isEnded = true;
		Timer.schedule(new Timer.Task() {
			@Override
			public void run() {
				moveTurn(currentTurn == Turn.BOTTOM ? Turn.TOP : Turn.BOTTOM);
			}
		}, 0.19f);
		//MainActivity.toast("v1");
		if(gType==GameType.TABLE){
			gameEndTable.show(uiStage);
			//MainActivity.toast("c1");
		}
		if(gType==GameType.LOCALHOST){
			gameEndLocalHost.show(uiStage);
			//MainActivity.toast("c2");
		}
	}
	public float size(float f){
		return Gdx.graphics.getWidth()<Gdx.graphics.getHeight()?Dj.width(f):Dj.height(f);
	}
	public Value size(float f,Actor actor){
		return Gdx.graphics.getWidth()<Gdx.graphics.getHeight()?Value.percentWidth(f,actor):Value.percentHeight(f,actor);
	}
	@Override
	public void hide() {
		super.hide();
		disConnected();
		game.ip ="";
		game.input.removeProcessor(gameStage);
		game.input.removeProcessor(uiStage);
	}
	@Override
	public void render(float delta) {
		super.render(delta);
		Gdx.gl.glClearColor(0.5f,0.5f,0.5f,1f);
		Gdx.gl.glClear(GLES20.GL_COLOR_BUFFER_BIT);
		game.batch.setProjectionMatrix(viewport.getCamera().combined);
		gameStage.act(delta);
		uiStage.act(delta);
		gameStage.draw();
		shake.update(delta);
		uiStage.draw();
	}
	@Override
	public void dispose() {
		super.dispose();
		gameStage.dispose();
		uiStage.dispose();
	}
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		viewport.update(width,height);
	}
}