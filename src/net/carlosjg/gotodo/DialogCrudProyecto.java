package net.carlosjg.gotodo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class DialogCrudProyecto extends DialogFragment {

	private EditText nombreProyecto;
	private EditText prioridadProyecto;
	private int Titulo;

	
	public DialogCrudProyecto() {
		// Constructor vacio lo requiere DialogFragment
	}
		 
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		int titulo= getArguments().getInt("titulo");
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    LayoutInflater inflater = getActivity().getLayoutInflater();
	    View v= inflater.inflate(R.layout.projectdialog, null);
	    nombreProyecto= (EditText)v.findViewById(R.id.edtNombreProyecto);
	    prioridadProyecto= (EditText)v.findViewById(R.id.edtPrioridadProyecto);
	    builder.setTitle(titulo);
		 if (titulo==R.string.editarProyecto || titulo==R.string.borrarProyecto){
			 String nombre = getArguments().getString("nombre");
			 int prioridad = getArguments().getInt("prioridad");
			 nombreProyecto.setText(nombre);
			 prioridadProyecto.setText(""+prioridad);
			 if (titulo==R.string.borrarProyecto){
				 nombreProyecto.setKeyListener(null);
				 prioridadProyecto.setKeyListener(null);	 
			 }
		  }
		 Titulo=titulo;
	    builder.setView(v).setPositiveButton("OK", new DialogInterface.OnClickListener(){
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	            	   sendResult(1235, Titulo, nombreProyecto.getText().toString(),prioridadProyecto.getText().toString());
	                  
	               }
	           }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	                  
	               }
	           });      
	    return builder.create();
	}
       
	// Comunicación de datos a fragment anterior
    private void sendResult(int INT_CODE, int titulo, String nombre, String prioridad) {
        Intent i = new Intent();
        i.putExtra("nombre", nombre);
        i.putExtra("prioridad", prioridad);
        i.putExtra("titulo", titulo);
        getTargetFragment().onActivityResult(getTargetRequestCode(), INT_CODE, i);
    }
}
