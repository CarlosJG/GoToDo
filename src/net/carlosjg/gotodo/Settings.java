package net.carlosjg.gotodo;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class Settings extends Fragment {
	public View view;
	public Spinner nombreP;
	public Spinner nombreEt;
	public Project item;
	public Tags elem;
	public boolean borrarNotasProyecto=false;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		ImageView Info= (ImageView)getActivity().findViewById(R.id.infoApp);
		Info.setVisibility(View.INVISIBLE);
		
		view = inflater.inflate(R.layout.settings, 
                container, false);

	
		final Button btnCrearProyecto = (Button)view.findViewById(R.id.BtnCrearProyecto);
		final Button btnEditarProyecto = (Button)view.findViewById(R.id.BtnEditarProyecto);
		final Button btnBorrarProyecto = (Button)view.findViewById(R.id.BtnBorrarProyecto);
		final CheckBox borrarNotas = (CheckBox)view.findViewById(R.id.checkBorrarNotasProyectos);
		final Button btnCrearEtiqueta = (Button)view.findViewById(R.id.btnCrearEtiqueta);
		final Button btnEditarEtiqueta = (Button)view.findViewById(R.id.btnEditarEtiqueta);
		final Button btnBorrarEtiqueta = (Button)view.findViewById(R.id.btnBorrarEtiqueta);
		
		loadSpinnerP();
		loadSpinnerE();		
		
		btnCrearProyecto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {            	
            	setDialogProyecto(R.string.crearProyecto);
            }
		});
		
		btnEditarProyecto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	if (nombreP.getSelectedItem()!=null){            	
            		setDialogProyecto(R.string.editarProyecto);
            	}else Toast.makeText(getActivity(),getResources().getString(R.string.proyecto_sinProyectos), Toast.LENGTH_LONG).show();
            }
		});

		btnBorrarProyecto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {  
            	if (nombreP.getSelectedItem()!=null){            
	            	borrarNotasProyecto=borrarNotas.isEnabled();
	            	setDialogProyecto(R.string.borrarProyecto);
            	}else Toast.makeText(getActivity(),getResources().getString(R.string.proyecto_sinProyectos), Toast.LENGTH_LONG).show();
            }
		});
		
		btnCrearEtiqueta.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {           	
            	setDialogEtiqueta(R.string.crearEtiqueta);           	
            }
		});
		
		btnEditarEtiqueta.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	if (nombreEt.getSelectedItem()!=null){        
            	setDialogEtiqueta(R.string.editarEtiqueta); 
            	}else Toast.makeText(getActivity(),getResources().getString(R.string.sinEtiquetas), Toast.LENGTH_LONG).show();
            }
		});
		
		btnBorrarEtiqueta.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	if (nombreEt.getSelectedItem()!=null){        
            	setDialogEtiqueta(R.string.borrarEtiqueta);   
            	}else Toast.makeText(getActivity(),getResources().getString(R.string.etiqueta_errorborrar), Toast.LENGTH_LONG).show();
            }
		});
		return view;		
	}
	
    // Ventana dialogo Proyectos
    public void setDialogProyecto(int titulo){
	    FragmentTransaction ft = getFragmentManager().beginTransaction();
	    DialogCrudProyecto newFragment = new DialogCrudProyecto ();
	    Bundle args = new Bundle();
	    if (titulo==R.string.editarProyecto || titulo==R.string.borrarProyecto){
	    	item = (Project)nombreP.getSelectedItem();
	    	if (item!=null){
	    	args.putString("nombre", item.ProjNombre);
	    	args.putInt("prioridad", item.ProjPR); }
	    }
	    args.putInt("titulo", titulo);
	    newFragment.setArguments(args);
	    newFragment.setTargetFragment(this, 1235);
	    newFragment.show(ft, "dialog");
    }
    
    // Ventana dialogo Etiquetas
    public void setDialogEtiqueta(int titulo){
	    FragmentTransaction ft = getFragmentManager().beginTransaction();
	    DialogCrudEtiqueta newFragment = new DialogCrudEtiqueta ();
	    Bundle args = new Bundle();
	    if (titulo==R.string.editarEtiqueta || titulo==R.string.borrarEtiqueta){
	    	elem = (Tags)nombreEt.getSelectedItem();
	    	if (elem!=null) args.putString("nombre", elem.Tag);
	    }
	    args.putInt("titulo", titulo);
	    newFragment.setArguments(args);
	    newFragment.setTargetFragment(this, 1235);
	    newFragment.show(ft, "dialog");
    }
    
    // Recibir datos de DialogFragment y crea, borra o edita proyectos y etiquetas
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1235) {
        	String nombrePro = data.getStringExtra("nombre");
            int tituloRec= data.getIntExtra("titulo",0);
            int prioridad=0;   
            String prioridadP = data.getStringExtra("prioridad");
            try
            {
                 prioridad = Integer.parseInt(prioridadP);
            }
            catch (NumberFormatException e)
            {
                //Toast.makeText(getActivity(), getResources().getString(R.string.error), Toast.LENGTH_LONG).show();
            }            
            switch (tituloRec) {          
				
			case R.string.crearProyecto:	 
				if (nombrePro!="" && (prioridad<=3 && prioridad>=0)){
					crearProyecto(nombrePro,prioridad);						
				}
				else {Toast.makeText(getActivity(),getResources().getString(R.string.Proyecto_error), Toast.LENGTH_LONG).show();}
				break;
				
			case R.string.editarProyecto:        
				if (nombrePro!="" && (prioridad<=3 && prioridad>=0)){					
					editarProyecto(prioridad,nombrePro);
					Toast.makeText(getActivity(),getResources().getString(R.string.proyecto_editado), Toast.LENGTH_LONG).show();
				}
				else {Toast.makeText(getActivity(),getResources().getString(R.string.Proyecto_error), Toast.LENGTH_LONG).show();}
				break;
				
			case R.string.borrarProyecto:  
				borrarProyecto();
				break;
				
			case R.string.crearEtiqueta:  
				crearEtiqueta(nombrePro);
				break;
				
			case R.string.editarEtiqueta:  
				editarEtiqueta(nombrePro);
				break;
				
			case R.string.borrarEtiqueta:  
				borrarEtiqueta();
				break;
            }
            
            
  
        }
			
    }
     

	public void loadSpinnerP(){
	nombreP	 = (Spinner)view.findViewById(R.id.SpnProjectSet);
	List<Project>proyectosList= Project.listAll(Project.class);
	final ArrayAdapter<Project> spinner_adapter = new ArrayAdapter<Project>(getActivity(), 
			android.R.layout.simple_spinner_item, proyectosList);
	spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	nombreP.setAdapter(spinner_adapter);
	}
	
	public void loadSpinnerE(){
	nombreEt = (Spinner)view.findViewById(R.id.spnLabelSet);
	List<Tags> TagsList = Tags.listAll(Tags.class);
	ArrayAdapter<Tags> spinner_adapter1 = new ArrayAdapter<Tags>(getActivity(), 
	android.R.layout.simple_spinner_item, TagsList);
	spinner_adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	nombreEt.setAdapter(spinner_adapter1);
				
	}
	
	public void crearProyecto(String nombre, int prioridad){
		List<Project> sameProjects =Note.find(Project.class, "PROJ_NOMBRE=?", nombre);
		
		if(sameProjects.size()>0){
			Toast.makeText(getActivity(), getResources().getString(R.string.Proyecto_mismo_nombre), Toast.LENGTH_LONG).show();
			}
		else {
			Project P=new Project(getActivity(),nombre,prioridad);
			P.save();
			loadSpinnerP();
			Toast.makeText(getActivity(),getResources().getString(R.string.Proyecto_creado), Toast.LENGTH_LONG).show();
		}
	}
	
	public void editarProyecto(int prioridadNuevaP, String nombreNuevoP){
		Project P =(Project) nombreP.getSelectedItem();		
		if (P!=null){			
			Project P_edit = Project.findById(Project.class,P.getId());
			P_edit.ProjNombre=nombreNuevoP;
			P_edit.ProjPR=prioridadNuevaP;
			P_edit.save();			
		}else Toast.makeText(getActivity(),getResources().getString(R.string.proyecto_sinProyectos), Toast.LENGTH_LONG).show();
		loadSpinnerP();
	}
	
	public void borrarProyecto(){
		Project P =(Project) nombreP.getSelectedItem();		
		if (P!=null){			
			Project P_borrar = Project.findById(Project.class,P.getId());
			List<Note>notasDelProyectoBorrar=Note.find(Note.class, "PROJECT_ID=?", P_borrar.getId().toString());			
				for (Note item : notasDelProyectoBorrar){
					Note nota= Note.findById(Note.class, item.getId());
					if (borrarNotasProyecto){
					nota.delete();				
					}
					//Borrar referencias al proyecto en las notas
					else {
						nota.ProjectID=(Long) null;//TODO comprobar esto con la DB
						nota.save();
					}
			}
			P_borrar.delete();
			loadSpinnerP();
			Toast.makeText(getActivity(),getResources().getString(R.string.proyecto_borrado), Toast.LENGTH_LONG).show();
		}else Toast.makeText(getActivity(),getResources().getString(R.string.proyecto_errorborrar), Toast.LENGTH_LONG).show();
	}
	
	public void crearEtiqueta(String nombre){
		List<Tags> sameTags =Tags.find(Tags.class, "TAG=?", nombre);
		
		if(sameTags.size()>0){
			Toast.makeText(getActivity(), getResources().getString(R.string.etiqueta_mismo_nombre), Toast.LENGTH_LONG).show();
			}
		else {
			Tags T=new Tags(getActivity(),nombre);
			T.save();
			loadSpinnerE();
			Toast.makeText(getActivity(),getResources().getString(R.string.etiqueta_creada), Toast.LENGTH_LONG).show();
		}
		
	}
	
	public void editarEtiqueta(String nombreE){
		Tags T =(Tags) nombreEt.getSelectedItem();		
		if (T!=null){			
			Tags T_edit = Tags.findById(Tags.class,T.getId());
			T_edit.Tag=nombreE;
			T_edit.save();
			Toast.makeText(getActivity(),getResources().getString(R.string.etiqueta_creada), Toast.LENGTH_LONG).show();
		}else Toast.makeText(getActivity(),getResources().getString(R.string.sinEtiquetas), Toast.LENGTH_LONG).show();
		loadSpinnerE();
		
	}
	
	public void borrarEtiqueta(){
		Tags T =(Tags) nombreEt.getSelectedItem();		
		if (T!=null){			
			Tags T_borrar = Tags.findById(Tags.class,T.getId());
			T_borrar.delete();
			loadSpinnerE();
			Toast.makeText(getActivity(),getResources().getString(R.string.etiqueta_borrada), Toast.LENGTH_LONG).show();
		}else Toast.makeText(getActivity(),getResources().getString(R.string.etiqueta_errorborrar), Toast.LENGTH_LONG).show();
	}
	
	@Override
	public void onStop(){
		ImageView Info= (ImageView)getActivity().findViewById(R.id.infoApp);
		Info.setVisibility(View.VISIBLE);
		super.onStop();
		
		
	}
	
	@Override
	public void onResume(){
		ImageView Info= (ImageView)getActivity().findViewById(R.id.infoApp);
		Info.setVisibility(View.INVISIBLE);
		super.onResume();
	
	}

		
}
	


	
	
