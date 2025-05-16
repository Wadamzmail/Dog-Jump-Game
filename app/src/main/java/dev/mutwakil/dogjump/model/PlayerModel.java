package dev.mutwakil.dogjump.model;

import dev.mutwakil.dogjump.game.Dj;
import dev.mutwakil.dogjump.util.Utils;

public class PlayerModel {
	public String name="";
	public String uid="";
	
	public PlayerModel(){
		
	}
	
	public void validate(){
		if(name.equals("")){
			name="اللاعب";
			for(int i=0;i<3;++i){
				name+=Dj.getRandom(0,9);
			}
		}
		if(uid.equals("")){
			uid=Utils.generateUUID();
		}
	}
}