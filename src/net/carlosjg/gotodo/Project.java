package net.carlosjg.gotodo;

import android.content.Context;

import com.orm.SugarRecord;

public class Project extends SugarRecord<Project>{
	
	
	public String ProjNombre;
	public int ProjPR;
	
	
	public Project(Context ctx) {
		super(ctx);

	}
	
	public Project(Context ctx,String proNombre, int projPR) {
		super(ctx);
		this.ProjNombre= proNombre;
		this.ProjPR= projPR;

	}
	
	//metodo para construir mediante objeto
	public void add(Project project) {	
		this.ProjNombre= project.ProjNombre;
		this.ProjPR= project.ProjPR;

	}
	
	
	//sobrecritura del metodo toString para inflar los Spinners
	@Override
	public String toString() {
		return ProjNombre;
	}
	
	

}
