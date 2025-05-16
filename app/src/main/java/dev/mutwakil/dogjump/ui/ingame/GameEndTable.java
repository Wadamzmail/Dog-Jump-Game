package dev.mutwakil.dogjump.ui.ingame;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.utils.StringBuilder;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisDialog;
import com.kotcrab.vis.ui.widget.VisTable;
import deep.richi.libgdx.rtl.RtlController;
import dev.mutwakil.dogjump.game.Dj;
import dev.mutwakil.dogjump.res.Res;
import dev.mutwakil.dogjump.screen.GameScreen;
import dev.mutwakil.dogjump.ui.BaseAlertWindow;
import dev.mutwakil.dogjump.ui.MyLabel;
import dev.mutwakil.dogjump.ui.MyTextButton;

public class GameEndTable extends BaseAlertWindow{
	private GameScreen gs;
	private MyLabel topWin,bottomWin,topNote,bottomNote;
	private MyTextButton reCreate;
	private Group topGroup;
	private VisTable topTable;
	String win = "لقد فزت";
	String lose = "خسارة";
	String good = "كانت لعبة ملحمية";
	String bad = "لديك %1$s حمار عليك إرجاعه في المرة القادمة";
	public GameEndTable(GameScreen gs){
		super();
		this.gs = gs;
		setBackground(VisUI.getSkin().getDrawable("dialogDim"));
		clear();
		add(bg).width(Dj.size(0.75f,this));
		bg.clear();
		topTable = new VisTable();
		topGroup = new Group();
		topTable.setTransform(true);
		topGroup.addActor(topTable);
		bg.add(table).width(Dj.width(0.98f,bg)).center();
		topWin = new MyLabel("");
		bottomWin = new MyLabel("");
		topNote = new MyLabel("");
		bottomNote = new MyLabel("");
		topWin.getStyle().font=Res.titleFont;
		bottomWin.getStyle().font=Res.titleFont;
		topWin.setStyle(topWin.getStyle());
		bottomWin.setStyle(bottomWin.getStyle());
		reCreate = new MyTextButton("البدأ");
		reCreate.setOnClickListener((event,x,y)->{
			gs.newMatch();
			hide();
		});
		topTable.add(topWin).row();
		topTable.add(topNote).padTop(Dj.size(0.05f)).row();
		topTable.pack();
		table.add(topGroup).size(Dj.width(1f,topTable),Dj.height(1f,topTable)).padBottom(Dj.size(0.05f)).padTop(Dj.size(0.05f)).row();
		topTable.setOrigin(Dj.width(1f,topTable).get()/2,Dj.height(1f,topTable).get()/2);
		topTable.setRotation(180);
		table.addSeparator(false).row();
		table.add(bottomWin).padTop(Dj.size(0.05f)).row();
		table.add(bottomNote).row();
		table.add(reCreate).width(Dj.width(0.4f,table)).padTop(Dj.size(0.05f)).center();
	}
	StringBuilder bad(int c){
	    return RtlController.getInstance().getRtl(String.format(bad,c),true);
	}
	@Override
	public VisDialog show(Stage stage) {
		Res.playSfx(Res.WIN);
		if(gs.tops.size()==0){
			topWin.getStyle().fontColor=Res.DEEP_RED;
			bottomWin.getStyle().fontColor=Res.DEEP_GREEN;
			topWin.setText(lose);
			topNote.setText(bad(gs.donkeysTop));
			bottomWin.setText(win);
			bottomNote.setText(good);
		}else if(gs.bottoms.size()==0){
			topWin.getStyle().fontColor=Res.DEEP_GREEN;
			bottomWin.getStyle().fontColor=Res.DEEP_RED;
			bottomWin.setText(lose);
			bottomNote.setText(bad(gs.donkeysBottom));
			topWin.setText(win);
			topNote.setText(good);
		}
		return super.show(stage);
	}
}