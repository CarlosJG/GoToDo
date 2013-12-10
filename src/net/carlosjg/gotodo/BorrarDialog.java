package net.carlosjg.gotodo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class BorrarDialog extends DialogFragment {


	
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	int titulo = getArguments().getInt("titulo");
        return new AlertDialog.Builder(getActivity())
            .setTitle(titulo)
            .setMessage(getResources().getString(R.string.confirmar_mensaje))
            .setNegativeButton(android.R.string.no, new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                  
                }
            })
            .setPositiveButton(android.R.string.yes,  new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                  sendResult(1234);  
                }
            })
            .create();
    }
    private void sendResult(int INT_CODE) {
    	String KEY = null;
        Intent i = new Intent();
        i.putExtra(KEY,"yes");
        getTargetFragment().onActivityResult(getTargetRequestCode(), INT_CODE, i);
    }
}