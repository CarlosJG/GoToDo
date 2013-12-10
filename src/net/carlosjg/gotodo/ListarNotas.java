package net.carlosjg.gotodo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
 
     
    public class ListarNotas extends ListFragment implements LoaderManager.LoaderCallbacks<List<Note>> {
         
        AdapterListaNotas mAdapter;
        String miEstado;
        String miProyecto;
        static Note itemBorrar;
        
		@Override public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            registerForContextMenu(getListView());
            
            final ImageView Info= (ImageView)getActivity().findViewById(R.id.infoApp);
    		Info.setVisibility(View.INVISIBLE);
    		
			
            
            miEstado = getArguments().getString("Estado");
            miProyecto = getArguments().getString("Project");
            
            if (miEstado!="null")getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        	
            //Añadir spinner a la actionBar
            ActionBar bar = getActivity().getActionBar();          
            
            //Obtener la lista de acciones GTD del array XML
            Resources res =getResources();
            String[] opcionesGTD = res.getStringArray(R.array.EstadosGTD);
            
			//Pasarla a un tipo compatible con el adapter
			List<String> list = new ArrayList<String>();
			list = Arrays.asList(opcionesGTD);
			ArrayList<String> arrayList = new ArrayList<String>(list);
			
            //Enlazar adapter, lista y añadir listener
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),  
                  android.R.layout.simple_spinner_dropdown_item, arrayList);
            bar.setListNavigationCallbacks(adapter, navigationListener);
            
            // Texto si no hay datos
            setEmptyText(getString(R.string.no_hay_datos));
 
            // crear un adaptador vacio que se usa despues 
            mAdapter = new AdapterListaNotas(getActivity());
            setListAdapter(mAdapter);
 
            // Indicador de progreso de carga de datos
            setListShown(false);
 
            // Prepara el loader. Reconnecta con el exixtente, o crea uno nuevo             
            //Valor del Spinner EstadosGTD
            //llamada a OncreateLoader, se envia String en un Bundle con el valor
            Bundle bundle = new Bundle();
            //bundle.putString("Project", miProyecto);
            //bundle.putString("Estado",miEstado);//TODO el bundle no admite valor "null" como string lo convierte a null
            getLoaderManager().initLoader(0, bundle, this);
        }
                    
        @Override
        public Loader<List<Note>> onCreateLoader(int arg0, Bundle arg1) {
            return new DataListLoaderNote(getActivity(),miEstado,miProyecto);
        }
 
        @Override
        public void onLoadFinished(Loader<List<Note>> arg0, List<Note> data) {
            mAdapter.setData(data);
            // Se muestra la lista
            if (isResumed()) {
                setListShown(true);
            } else {
                setListShownNoAnimation(true);
            }
        }
 
        @Override
        public void onLoaderReset(Loader<List<Note>> arg0) {
            mAdapter.setData(null);
        }
        
           
        //Mostrar detalle de nota selecionada
        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            Note notaEditar=(Note) l.getItemAtPosition(position);       	
        	Bundle bundleNota =new Bundle(1);
        	bundleNota.putString("IdNota",notaEditar.getId().toString());
			FragmentManager fragmentManager = getFragmentManager();
        	Fragment crudNota = new ControllerNota();
        	crudNota.setArguments(bundleNota);

			fragmentManager.beginTransaction()
					.addToBackStack("detalle_nota")
					.replace(R.id.content_frame, crudNota)
					.commit();     	
        }
        
        // Menu contextual (longclick)
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
            super.onCreateContextMenu(menu, v, menuInfo);
            AdapterView.AdapterContextMenuInfo amenuInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;
            itemBorrar = (Note) getListAdapter().getItem(amenuInfo.position);
            showDialog(R.string.Eliminar_item,itemBorrar);
            
        }
 
        // Ventana confirmación borrar nota
        public void showDialog(int titulo,Note itemBorrar){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        BorrarDialog newFragment = new BorrarDialog ();
        Bundle args = new Bundle();
        args.putInt("titulo", titulo);
        newFragment.setArguments(args);
        newFragment.setTargetFragment(this, 1234);
        newFragment.show(ft, "dialog");
        }
        
        // Recibir datos de DialogFragment y Borrar elemento pulsado
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode == 1234) {
                String KEY = null;
				String editText = data.getStringExtra(KEY);
				if (editText.equals("yes"))	{
					itemBorrar.delete();
	                //Se recarga la lista
	                Bundle bundle = new Bundle();
	                bundle.putString("Estado",miEstado);
	                getLoaderManager().restartLoader(0, bundle, ListarNotas.this);
				}
            }
				
        }
        
        // Listener para el Spiner GTD en la actionBar
        ActionBar.OnNavigationListener navigationListener = new OnNavigationListener() {
            @Override
            public boolean onNavigationItemSelected(int itemPosition, long itemId) {
            	Resources res =getResources();
                String[] opcionesGTD = res.getStringArray(R.array.EstadosGTD);
                Bundle bundle = new Bundle();
            	miEstado=opcionesGTD[itemPosition];
                bundle.putString("Estado",miEstado);
                
            	//Se reinicia el loader con el nuevo valor del Spinner (string Estado)
                getLoaderManager().restartLoader(0, bundle, ListarNotas.this);
                return false;
            }
        };
        
        // Recargar datos al volver al fragment desde detalle
        @Override 
        public void onResume(){
            super.onResume();
            final ImageView Info= (ImageView)getActivity().findViewById(R.id.infoApp);
    		Info.setVisibility(View.INVISIBLE);
            Bundle bundle = new Bundle();
            bundle.putString("Estado",miEstado);
            getLoaderManager().restartLoader(0, bundle, ListarNotas.this);
        }
        
        @Override
    	public void onStop(){
        	getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
    		final ImageView Info= (ImageView)getActivity().findViewById(R.id.infoApp);
    		Info.setVisibility(View.VISIBLE);
    		super.onStop();
    		
    		
    	}
        

    }
     
    
