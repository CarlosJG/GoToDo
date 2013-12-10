
package net.carlosjg.gotodo;

import android.content.Context;

import com.orm.SugarRecord;

public class Tags extends SugarRecord<Tags>{
	
	String Tag;

	
	public Tags(Context ctx) {
		super(ctx);

	}
	
	public Tags(Context ctx,String Tag) {
		super(ctx);
		this.Tag= Tag;


	}
	
	//metodo para construir mediante objeto
	public void add(Tags Tag) {		
		this.Tag= Tag.Tag;

	}
	//sobrecritura del metodo toString para inflar los Spinners
	@Override
	public String toString(){
		return Tag;
	}

}