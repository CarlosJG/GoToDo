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

public class DialogCrudEtiqueta extends DialogFragment {

	private EditText nombreEtiqueta;
	private int Titulo;

	
	public DialogCrudEtiqueta() {
		// Empty constructor required for DialogFragment
	}
		 
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		int titulo= getArguments().getInt("titulo");
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    LayoutInflater inflater = getActivity().getLayoutInflater();
	    View v= inflater.inflate(R.layout.labeldialog, null);
	    nombreEtiqueta= (EditText)v.findViewById(R.id.edtNombreEtiqueta);
	    builder.setTitle(titulo);
	    Titulo=titulo;
		 if (titulo==R.string.editarEtiqueta || titulo==R.string.borrarEtiqueta){
			 String nombre = getArguments().getString("nombre");
			 nombreEtiqueta.setText(nombre);
			 if (titulo==R.string.borrarEtiqueta){
				 nombreEtiqueta.setKeyListener(null); 
			 }
		  }
		 Titulo=titulo;
	    builder.setView(v).setPositiveButton("OK", new DialogInterface.OnClickListener(){
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	            	   sendResult(1235, Titulo, nombreEtiqueta.getText().toString());	                  
	               }
	           }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	                  
	               }
	           });      
	    return builder.create();
	}
       
	// Comunicación de datos a fragment padre
    private void sendResult(int INT_CODE, int titulo, String nombre) {
        Intent i = new Intent();
        i.putExtra("nombre", nombre);
        i.putExtra("titulo", titulo);
        getTargetFragment().onActivityResult(getTargetRequestCode(), INT_CODE, i);
    }
}
