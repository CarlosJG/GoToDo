
package net.carlosjg.gotodo;

import android.content.Context;

import com.orm.SugarRecord;

public class Note_Tags extends SugarRecord<Note_Tags>{
	
	   //Relación con tablas Note y Tags mediante SugarORM
    Long note_id;
    Long tag_id;
    
    //TODO Probar esto:
    //Note note
    //Tags tag

	
	public Note_Tags(Context ctx) {
		super(ctx);

	}
	
	public Note_Tags(Context ctx, Long note_id,Long tag_id) {
		super(ctx);
		this.note_id= note_id;
		this.tag_id=tag_id;


	}
	
	//metodo para construir mediante objeto
	public void add(Note_Tags Note_Tag) {		
		this.note_id= Note_Tag.note_id;
		this.tag_id= Note_Tag.tag_id;
	}
	
	

}