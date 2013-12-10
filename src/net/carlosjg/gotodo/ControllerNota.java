package net.carlosjg.gotodo;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ControllerNota extends Fragment {
	private View view;
	public Note note;
	public String myFormat = "dd/MM/yy";
    public SimpleDateFormat sdf = new SimpleDateFormat(myFormat,Locale.US);
    public boolean error;
	public String fechaR = "";
	public static String imagePath;
	public File destination;
	public Uri audioUri;
	public EditText Titulo;
	public EditText Texto;
	public Spinner Proyecto;
	public Spinner Etiquetas;
	public Spinner EstadoGTD;
	public TextView LabelGTD;
	public EditText FechaEstimada;
	public EditText Prioridad;
	public Button btnFoto;
	public Button btnSonido;
	public Button btnGuardarNota;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {view = inflater.inflate(R.layout.controller_nota, 
                  container, false);
		getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
			
		ImageView Info= (ImageView)getActivity().findViewById(R.id.infoApp);
		Info.setVisibility(View.INVISIBLE);
		Titulo= (EditText)view.findViewById(R.id.edtTitulo);
		Texto= (EditText)view.findViewById(R.id.edtTexto);
		Proyecto= (Spinner)view.findViewById(R.id.spiProyecto);
		Etiquetas= (Spinner)view.findViewById(R.id.spiEtiquetas);
		EstadoGTD= (Spinner)view.findViewById(R.id.spiGTD);
		LabelGTD = (TextView)view.findViewById(R.id.txtGTD);
		FechaEstimada=(EditText)view.findViewById(R.id.edtFecha);
		Prioridad=(EditText)view.findViewById(R.id.etxtPrioridad);
		btnFoto = (Button)view.findViewById(R.id.btnFoto);
		btnSonido = (Button)view.findViewById(R.id.btnURL);
		btnGuardarNota = (Button)view.findViewById(R.id.btnGuardarNota);
	       
	    String IdNota = getArguments().getString("IdNota");
	      
	    if (IdNota==null){
	    	  nuevaNota();
	    }
	    else editarNota(IdNota);
		
	    return view;
	}
	
	public void nuevaNota (){
		
		note= new Note(getActivity());				

		//No hay spinner GTD al crear nota
		LabelGTD.setVisibility(View.INVISIBLE);
		EstadoGTD.setVisibility(View.INVISIBLE);
		
		// Prioridad
		Prioridad.setText("0");//por defecto
	    
	    // Fecha Estimada
	    final Calendar myCalendar = Calendar.getInstance();
	    final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
	        @Override
	        public void onDateSet(DatePicker view, int year, int monthOfYear,
	                int dayOfMonth) {
	            myCalendar.set(Calendar.YEAR, year);
	            myCalendar.set(Calendar.MONTH, monthOfYear);
	            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
	            String fechaR=sdf.format(myCalendar.getTimeInMillis());
	            FechaEstimada.setText(fechaR);
	            //Guardar fecha Requerida en objeto 
	            note.FechaEstimada=myCalendar.getTimeInMillis();
	        }
	    };
	    FechaEstimada.setOnClickListener(new OnClickListener() {

	            @Override
	            public void onClick(View v) {
	                new DatePickerDialog(getActivity(), date, myCalendar
	                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
	                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
	            }            
	        });
	    
	    
	    
		// ****Acciones click en FOTO
		btnFoto.setOnClickListener(new OnClickListener() {
             @Override
             public void onClick(View v) {
            	 String time=String.valueOf(myCalendar.getTimeInMillis());           	 
            	 String name = "gtd"+time;
                  destination = new File(Environment
                          .getExternalStorageDirectory(), name + ".jpg");

                  Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                  intent.putExtra(MediaStore.EXTRA_OUTPUT,
                          Uri.fromFile(destination));
                  startActivityForResult(intent, 1);
            	 
             }
        });
	    
		// ****Acciones click en SONIDO
		btnSonido.setOnClickListener(new OnClickListener() {
             @Override
             public void onClick(View v) {
//            	 
//            	 String time=String.valueOf(myCalendar.getTimeInMillis());           	 
//            	 String name = "gtd"+time;
//                  destination = new File(Environment
//                          .getExternalStorageDirectory(), name + ".m4a");
//
//                  Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
//                  intent.putExtra(MediaStore.EXTRA_OUTPUT,
//                          Uri.fromFile(destination));
//                  startActivityForResult(intent, 2);
            	 
            	 
            	  
            	 Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);                  
                 startActivityForResult(intent, 2);
            	 
             }
        });


	    
		// ****Acciones click en guardar
		btnGuardarNota.setOnClickListener(new OnClickListener() {
             @Override
             public void onClick(View v) {
            	 
            	 note.Titulo = Titulo.getText().toString();
                 if (note.Titulo.equals("")){
                	 error = true;
                     Toast.makeText(getActivity(),R.string.error_titulo, Toast.LENGTH_LONG).show();		                		 
                 }else error=false;                
                 note.Texto = Texto.getText().toString();
                 
                 //Guardar Fecha actual en FechaEntrada
                 String tmp = sdf.format(new Date());
                 Date date = null;
				try {
					date = sdf.parse(tmp);
				} catch (ParseException e) {
					 e.printStackTrace();
				}               
                 note.FechaEntrada  = date.getTime();
                 
                 // Obtener valor spinner proyecto
                 Project P = (Project) Proyecto.getSelectedItem();
                 if (P!=null){                
                 note.ProjectID =P.getId(); 
                 }
                 
                // Guardar prioridad en objeto
                 String Pr =Prioridad.getText().toString();
                 if (Pr.equals("0") || Pr.equals("1") || Pr.equals("2") ||Pr.equals("3")){
	         			
                	 note.Prioridad =Integer.parseInt(Pr);
                	 error=false;
         		}
         		else {
         			error=true;
         			Toast.makeText(getActivity(),R.string.error_prioridad , Toast.LENGTH_LONG).show();
         		}                                
             
                 
                 //grabar datos                 
                 if (!error){
                	//Spinner Etiquetas (hace falta la id de nota)
	                 Note_Tags note_tags=new Note_Tags(getActivity());
	                 Tags tag = (Tags) Etiquetas.getSelectedItem();
	                 if (tag!=null){
	                 note_tags.tag_id=tag.getId();                
	                 note_tags.note_id=note.getId();
	                 note_tags.save();
	                 }
	                 note.save();
	                 
	                 if (note.FechaEstimada>0)saveCalendar(note);
	                 
	                 Toast.makeText(getActivity(),R.string.nota_grabada, Toast.LENGTH_LONG).show();
	                 //Cerrar y volver a la activity anterior
	                 android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
	                 fragmentManager.popBackStackImmediate();
                 };
             }
         });
						
		
		//****** Carga de datos a SPINNERS
		
		//Spinner Proyecto				
				List<Project>proyectosList= Project.listAll(Project.class);
				//Creamos el adaptador
				ArrayAdapter<Project> spinner_adapter = new ArrayAdapter<Project>(getActivity(), 
						android.R.layout.simple_spinner_item, proyectosList);
				//Añadimos el layout para el menú y se lo damos al spinner
				spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				Proyecto.setAdapter(spinner_adapter);				
				
	   //Spinner Etiquetas
				List<Tags> TagsList = Tags.listAll(Tags.class);
				//Creamos el adaptador
				ArrayAdapter<Tags> spinner_adapter1 = new ArrayAdapter<Tags>(getActivity(), 
						android.R.layout.simple_spinner_item, TagsList);
				//Añadimos el layout para el menú y se lo damos al spinner
				spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				Etiquetas.setAdapter(spinner_adapter1);
	}
			
	public void editarNota (String id){
		
		// Recupera nota seleccionada
		List<Note> listNote=Note.find(Note.class,"ID = ?",id);
		note=listNote.get(0);		
		
		// Valores de la nota antes de editar, titulo y texto
		Titulo.setText(note.Titulo);
		Texto.setText(note.Texto);
		
		// cargar Spinner Proyecto				
		List<Project>proyectosList= Project.listAll(Project.class);
		ArrayAdapter<Project> spinner_adapter = new ArrayAdapter<Project>(getActivity(), 
				android.R.layout.simple_spinner_item, proyectosList);
		spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Proyecto.setAdapter(spinner_adapter);
				
		// Valor anterior Spinner Proyecto
		List<Project> listProj=Project.find(Project.class,"ID = ?", String.valueOf(note.ProjectID));
		int Position=0;
		if (listProj.size()>0){
			Project Proj = listProj.get(0);
			for (int x=0 ; x<proyectosList.size();x++){
				if (proyectosList.get(x).ProjNombre.equals(Proj.ProjNombre)) Position=x;
			}
		}
 		Proyecto.setSelection(Position);
		
		// Valor anterior Prioridad
		if(note.Prioridad<4 || note.Prioridad>0){
		Prioridad.setText(String.valueOf(note.Prioridad));
		}
		else Prioridad.setText("0");
	    
	    //Valor anterior de Fecha Estimada
		if (note.FechaEstimada!=0){
		    fechaR=sdf.format(note.FechaEstimada);
			FechaEstimada.setText(fechaR);
		}
	    
	    // DatePicker para selecionar nueva Fecha 
	    final Calendar myCalendar = Calendar.getInstance();
	    
	    final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

	        @Override
	        public void onDateSet(DatePicker view, int year, int monthOfYear,
	                int dayOfMonth) {
	            myCalendar.set(Calendar.YEAR, year);
	            myCalendar.set(Calendar.MONTH, monthOfYear);
	            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
	            FechaEstimada.setText(sdf.format(myCalendar.getTimeInMillis()));
	        }

	    };

	    FechaEstimada.setOnClickListener(new OnClickListener() {

	            @Override
	            public void onClick(View v) {
	                new DatePickerDialog(getActivity(), date, myCalendar
	                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
	                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
	            }
	        });

		
			// Spinner EstadoGTD		
				Resources res =getResources();
				final String[] opcionesGTD = res.getStringArray(R.array.EstadosGTD);
				List<String> list = new ArrayList<String>();
				list = Arrays.asList(opcionesGTD);
				ArrayList<String> arrayList = new ArrayList<String>(list);			 
				EstadoGTD.setAdapter(new ArrayAdapter<String>(getActivity(), 
						android.R.layout.simple_spinner_item, arrayList));
				
			// Valor anterior Spinner EstadoGTD
				Position=0;
				if (list.size()>0){
					for (int x=0 ; x<list.size();x++){
						if (list.get(x).equals(note.Estado)) Position=x;
					}
				}
		 		EstadoGTD.setSelection(Position);
								
			// Spinner Etiquetas				
				List<Tags> TagsList = Tags.listAll(Tags.class);

				ArrayAdapter<Tags> spinner_adapter1 = new ArrayAdapter<Tags>(getActivity(), 
						android.R.layout.simple_spinner_item, TagsList);
				spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				Etiquetas.setAdapter(spinner_adapter1);
				
			// Valor anterior Spinner Etiquetas TODO (actualmente implementa solo la última etiqueta grabada de cada nota)
				Position=0;
				//recupera una lista con los Ids de las etiquetas que tienen la id de la nota:
				List<Note_Tags> TagsIdsNota = Note_Tags.find(Note_Tags.class,"NOTEID = ?",note.getId().toString());				
				//comprueba si hay alguna y si es así en que posición está;
				if (TagsIdsNota.size()>0){
					//recupera una lista con los nombres (etiquetas) de las ids anteriores:
					List<Tags> NombreTags = Tags.find(Tags.class, "ID = ?", TagsIdsNota.get((TagsIdsNota.size()-1)).tag_id.toString());
					String EtiquetaSel=NombreTags.get(0).Tag;
					String TempEtiq;
					for (int x=0 ; x<TagsList.size();x++){
						TempEtiq=TagsList.get(x).Tag;
						if (TempEtiq.equals(EtiquetaSel)) Position=x;
					}
				}
				// coloca el spinner en la posicion leida o en cero si no hay etiqueta:
		 		Etiquetas.setSelection(Position);
		 		
		 		
				// ****Acciones click en FOTO
				btnFoto.setOnClickListener(new OnClickListener() {
		             @Override
		             public void onClick(View v) {
		            	 if (note.Foto!=null){
			            	 Intent intent = new Intent();
			            	 intent.setAction(Intent.ACTION_VIEW);
			            	 intent.setDataAndType(Uri.parse("file://" + note.Foto), "image/*");
			            	 startActivity(intent);
		            	 }
		            	 else
		            	 {
		            		 String time=String.valueOf(myCalendar.getTimeInMillis());           	 
		                	 String name = "gtd"+time;
		                     destination = new File(Environment
		                             .getExternalStorageDirectory(), name + ".jpg");

		                     Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		                     intent.putExtra(MediaStore.EXTRA_OUTPUT,
		                             Uri.fromFile(destination));
		                     startActivityForResult(intent, 1);
		            	 }
		            	 
		             }
		        });
				
				// ****Acciones click en SONIDO
				btnSonido.setOnClickListener(new OnClickListener() {
		             @Override
		             public void onClick(View v) {
		            	 if (note.Sonido!=null){            		 
		            		 MediaPlayer mediaPlayer = MediaPlayer.create(getActivity(),Uri.parse(note.Sonido));
		            		  mediaPlayer.setOnCompletionListener(completionListener);
		            		  mediaPlayer.start();
		            		 }
		            	 else{		            	 
			            	 Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);                  
			                 startActivityForResult(intent, 2);
		            	 }
		            	 
		             }
		        });

												
			// ****Acciones al click en guardar
				btnGuardarNota.setOnClickListener(new OnClickListener() {
		             @Override
		             public void onClick(View v) {
		            	 
		             //Leer valores nuevos
		                 note.Titulo = Titulo.getText().toString();
		                 if (note.Titulo.equals("")){
		                	 error = true;
		                     Toast.makeText(getActivity(),R.string.error_titulo, Toast.LENGTH_LONG).show();		                		 
		                 }else error=false;
		                 note.Texto = Texto.getText().toString();
			            
			    	 // Valor de Fecha Estimada
		                boolean cambioFecha=true;
		    		    String fechaR2=FechaEstimada.getText().toString();
			    		if (fechaR.equals(fechaR2)) cambioFecha=false;
		                 
		             // Leer Spinner Proyecto actualizado
		                 Project P = (Project) Proyecto.getSelectedItem();
		                 if (P!=null){                
		                 note.ProjectID =P.getId();
		                 }
		                 
		             // Leer Spinner GTD
		                 note.Estado=EstadoGTD.getSelectedItem().toString();
		               	 if (note.Estado.equals(getString(R.string.DOIT))){
		                     String tmp = sdf.format(new Date());
		                     Date date = null;
		    				try {
		    					date = sdf.parse(tmp);
		    				} catch (ParseException e) {
		    					 e.printStackTrace();
		    				}               
		                     note.FechaRealizada  = date.getTime();
		               	 }
		                 
		             // Leer Prioridad actualizada
		                 String Pr =Prioridad.getText().toString();
		                 
		                 if (Pr.equals("0") || Pr.equals("1") || Pr.equals("2") ||Pr.equals("3")){
		         			
		                	 note.Prioridad =Integer.parseInt(Pr);
		                	 error=false;
		         		}
		         		else {
		         			error=true;
		         			Toast.makeText(getActivity(),R.string.error_prioridad , Toast.LENGTH_LONG).show();
		         		}
  	                               
		                 
		             // Grabar datos
		                 if (!error){
			                 note.save();
			                 if (cambioFecha)saveCalendar(note);//TODO borrar evento anterior en calendar
			                 
				             //Spinner Etiquetas (hace falta la id de nota antes de grabar en el Note_Tags)
			                 Note_Tags note_tags=new Note_Tags(getActivity());
			                 Tags tag = (Tags) Etiquetas.getSelectedItem();
			                 if (tag!=null){
			                 note_tags.tag_id=tag.getId();                
			                 note_tags.note_id=note.getId();
			                 note_tags.save();
			                 } 
			                 Toast.makeText(getActivity(),R.string.nota_grabada, Toast.LENGTH_LONG).show();
			                 //Cerrar y volver a la activity(fragment) anterior
			                 android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
			                 fragmentManager.popBackStackImmediate();
		                 }    
		             }
		         });

	}
	// Recoger datos de vuelta de los intents (racord audio y camera)
	@SuppressWarnings("static-access")
	@Override
	public void onActivityResult(int requestCode, int resultCode,
	        Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);

	    switch (requestCode) {

	    case 1:
	        if (resultCode == getActivity().RESULT_OK) {

	            BitmapFactory.Options options = new BitmapFactory.Options();
	            options.inSampleSize = 4;
	            String imagePath = destination.getAbsolutePath();	            
	            note.Foto=imagePath;	            
	            Toast.makeText(getActivity(),getResources().getString(R.string.fotoGrabada),Toast.LENGTH_LONG).show();
	        }

	    break;
	        
	    case 2:
	    	if ((resultCode == getActivity().RESULT_OK)){
	    		audioUri = data.getData();
	    		note.Sonido=audioUri.toString();	    	
	    		Toast.makeText(getActivity(),getResources().getString(R.string.sonidoGrabado),Toast.LENGTH_LONG).show();
	    	}
	    	
	    break;
	        
//	    case 3:
//		    if (resultCode == getActivity().RESULT_OK) {
//
//		            //TODO elegir foto de Galeria (Proxima versión)
//		    }
//
//		    break;
//
	        }
	    }
	
	// Play audio note
    MediaPlayer.OnCompletionListener completionListener
    = new MediaPlayer.OnCompletionListener(){
           		 @Override
           		 public void onCompletion(MediaPlayer arg0) {
           		  // TODO botones para reproducir y grabar (proxima versión)
           		  //buttonRecord.setEnabled(true);
           		  //buttonPlay.setEnabled(true);
           		 }};

	
	// Sincronizacion con Google Calendar (o aplicacion del sistema elegida API 14)
	public void saveCalendar(Note note){
		Intent intent = new Intent(Intent.ACTION_EDIT);
		intent.setType("vnd.android.cursor.item/event");
		intent.putExtra(Events.TITLE, note.Titulo);
		intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,note.FechaEstimada);
		intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,note.FechaEstimada);
		intent.putExtra(Events.ALL_DAY, false);// periodicidad
		intent.putExtra(Events.DESCRIPTION,note.Texto);
		startActivity(intent);
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
 