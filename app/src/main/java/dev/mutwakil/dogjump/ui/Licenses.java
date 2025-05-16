package dev.mutwakil.dogjump.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTable;
import dev.mutwakil.dogjump.MainActivity;
import dev.mutwakil.dogjump.game.Dj;
import dev.mutwakil.dogjump.res.Res;

public class Licenses extends BaseDialog {
	private VisScrollPane scrollPane;
	private VisTable t;
	private MyLabel l;
	
	public Licenses() {
		super("LICENSES");
		clear();
		
		add(bg).width(Dj.size(1f, this));
		
		t = new VisTable();
		l = new MyLabel("Loading Licenses...");
		l.setWrap(true);
		l.getStyle().font = Res.CAIRO_SEMIBOLD;
		t.add(l).growY().width(Dj.size(0.9f, this)).pad(10).row();
		
		scrollPane = new VisScrollPane(t);
		scrollPane.setVariableSizeKnobs(true);
		scrollPane.setScrollingDisabled(true, false);
		scrollPane.setScrollbarsVisible(true);
		scrollPane.setFadeScrollBars(true);
		add(scrollPane).grow().pad(20);
		
		new Thread(() -> {
			String str = Gdx.files.internal("LICENSE.txt").readString();
			Gdx.app.postRunnable(() -> {
				l.setText(str);
				t.invalidateHierarchy();
				pack();  
			});
		}).start();
	}
}