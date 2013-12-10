package net.carlosjg.gotodo;


import android.content.Context;

import com.orm.SugarRecord;



public class Note extends SugarRecord<Note>{
	String Titulo;
    String Texto;
    String Foto;
    String Sonido;
    String URL;
    String Archivo;
    long FechaEntrada;
    long FechaEstimada;
    long FechaRealizada;
    int Prioridad;
    String Estado;
    
    
    //Relación con tabla projectos (clase Project) mediante SugarORM
    long ProjectID;
    
 
    public Note(Context ctx){
        super(ctx);
    }
    
    public Note(Context ctx, String titulo, String texto, String foto, String sonido,
    		String uRL, String archivo,  long fechaEntrada, long fechaEstimada, 
    		long fechaRealizada,int prioridad,String estado,
    		long projectID)
    {
    	
        super(ctx);
        this.Titulo = titulo;
        this.Texto = texto;
        this.Foto = foto;
        this.Sonido = sonido;
        this.URL = uRL;
        this.Archivo = archivo;
        this.FechaEntrada = fechaEntrada;
        this.FechaEstimada = fechaEstimada;
        this.FechaRealizada = fechaRealizada;
        this.Prioridad = prioridad;
        this.Estado = estado;
        this.ProjectID = projectID;

    }
    
    //metodo para construir mediante objeto
	public void add(Note note) {
		
		this.Titulo=note.Titulo;
        this.Texto = note.Texto;
        this.Foto = note.Foto;
        this.Sonido = note.Sonido;
        this.URL = note.URL;
        this.Archivo = note.Archivo;
        this.FechaEntrada = note.FechaEntrada;
        this.FechaEstimada = note.FechaEstimada;
        this.FechaRealizada = note.FechaRealizada;
        this.Prioridad = note.Prioridad;
        this.Estado = note.Estado;
        this.ProjectID = note.ProjectID;

        

	}

    
}