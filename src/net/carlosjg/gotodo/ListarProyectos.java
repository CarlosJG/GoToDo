package net.carlosjg.gotodo;
import java.util.List;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
 
     
    public class ListarProyectos extends ListFragment implements LoaderManager.LoaderCallbacks<List<Project>> {
         
        AdapterListaProyectos mAdapter;
        //static Project itemBorrar;

		@Override public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            registerForContextMenu(getListView());
            
            final ImageView Info= (ImageView)getActivity().findViewById(R.id.infoApp);
    		Info.setVisibility(View.INVISIBLE);
             	           
            // Texto si no hay datos
            setEmptyText(getString(R.string.no_hay_datos));
 
            // crear un adaptador vacio que se usa despues 
            mAdapter = new AdapterListaProyectos(getActivity());
            setListAdapter(mAdapter);
 
            // Indicador de progreso de carga de datos
            setListShown(false);
 
            // Prepara el loader. Reconnecta con el exixtente, o crea uno nuevo             
            Bundle bundle = new Bundle();
            getLoaderManager().initLoader(0, bundle, this);           
        }
                    
        @Override
        public Loader<List<Project>> onCreateLoader(int arg0, Bundle arg1) {
            return new DataListLoaderProject(getActivity(),"null");
        }
 
        @Override
        public void onLoadFinished(Loader<List<Project>> arg0, List<Project> data) {
            mAdapter.setData(data);
            // Se muestra la lista
            if (isResumed()) {
                setListShown(true);
            } else {
                setListShownNoAnimation(true);
            }
        }
 
        @Override
        public void onLoaderReset(Loader<List<Project>> arg0) {
            mAdapter.setData(null);
        }
                  
        // Mostrar detalle de nota selecionada
        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            Project P=(Project) l.getItemAtPosition(position);       	
        	Bundle bundleNota =new Bundle(2);
        	bundleNota.putString("Project",P.getId().toString());
        	bundleNota.putString("Estado", getString(R.string.NEXT_ACTION));
			FragmentManager fragmentManager = getFragmentManager();
        	Fragment listaNotasProyecto = new ListarNotas();
        	listaNotasProyecto.setArguments(bundleNota);
			fragmentManager.beginTransaction()
					.addToBackStack("listado_notas_proyecto")
					.replace(R.id.content_frame, listaNotasProyecto)
					.commit();     	
        }
        
        // Recargar datos al volver al fragment desde detalle
        @Override 
        public void onResume(){
            super.onResume();
            ImageView Info= (ImageView)getActivity().findViewById(R.id.infoApp);
    		Info.setVisibility(View.INVISIBLE);
            Bundle bundle = new Bundle();
            getLoaderManager().restartLoader(0, bundle, ListarProyectos.this);
        }
        
    	@Override
    	public void onStop(){
    		getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
    		ImageView Info= (ImageView)getActivity().findViewById(R.id.infoApp);
    		Info.setVisibility(View.VISIBLE);
    		super.onStop();
    		
    		
    	}
        
//****** BORRAR EN ONCLICK TODO , se borra desde SETTINGS de momento
        
        // Menu contextual (longclick)
//        @Override
//        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
//            super.onCreateContextMenu(menu, v, menuInfo);
//            AdapterView.AdapterContextMenuInfo amenuInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;
//            itemBorrar = (Project) getListAdapter().getItem(amenuInfo.position);
//            showDialog(R.string.Eliminar_item,itemBorrar);          
//        }
 
        // Ventana confirmación borrar nota
//        public void showDialog(int titulo,Project itemBorrar){
//        FragmentTransaction ft = getFragmentManager().beginTransaction();
//        BorrarDialog newFragment = new BorrarDialog ();
//        Bundle args = new Bundle();
//        args.putInt("titulo", titulo);
//        newFragment.setArguments(args);
//        newFragment.setTargetFragment(this, 1234);
//        newFragment.show(ft, "dialog");
//        }
        
        // Recibir datos de DialogFragment y Borrar elemento pulsado

//        public void onActivityResult(int requestCode, int resultCode, Intent data) {
//            if (requestCode == 1234) {
//                String KEY = null;
//				String editText = data.getStringExtra(KEY);
//				if (editText.equals("yes"))	{
//					itemBorrar.delete();
//	                //Se recarga la lista
//	                Bundle bundle = new Bundle();
//	               // bundle.putString(Estado,miEstado);
//	                getLoaderManager().restartLoader(0, bundle, ListarProyectos.this);
//				}
//            }			
//        }
        
    }
     
    
