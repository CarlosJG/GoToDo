
package net.carlosjg.gotodo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.ActionBar;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MainActivity extends ActionBarActivity {

    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;
    
    private CharSequence tituloSeccion;  
    private CharSequence tituloApp;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);
        
        //MENU: Pasar array del XML a Arraylist para poder traducir
        Resources res =getResources();
		final String[] opcionesMenu = res.getStringArray(R.array.opcionesMenu);
		List<String> list = new ArrayList<String>();
		list = Arrays.asList(opcionesMenu);
		ArrayList<String> arrayList = new ArrayList<String>(list);		
        AdapterMenu adClass = new AdapterMenu(MainActivity.this,arrayList);

        drawerList.setAdapter(adClass);
        
		drawerList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				FragmentManager fragmentManager = getSupportFragmentManager();
				fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
				Bundle EstadoGTD = new Bundle(1);
				//final ImageView Info= (ImageView) findViewById(R.id.infoApp);

				switch (position) {
					case 0://Crear nota
				        						
						EstadoGTD.putString("IdNota",null);
						 Fragment crudNota = new ControllerNota();
						 crudNota.setArguments(EstadoGTD);
							getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);							
							fragmentManager.beginTransaction()
									.addToBackStack("crearNota")
									.replace(R.id.content_frame, crudNota)
									.commit();
						break;
						
					case 1://Proximas acciones
						
						EstadoGTD.putString("Estado", getString(R.string.NEXT_ACTION));
						EstadoGTD.putString("Project","null");
						ListarNotas proximasNotas = new ListarNotas();
						proximasNotas.setArguments(EstadoGTD);
						getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
						fragmentManager.beginTransaction()
									.addToBackStack("procesarNotas")
									.replace(R.id.content_frame, proximasNotas)
									.commit();
						break;
						
					case 2://Bandeja de Entrada
						
						EstadoGTD.putString("Estado","null");
						//este null es string xq se utiliza para la busqueda con el ORM
						EstadoGTD.putString("Project","null");
						ListarNotas procesarNotas = new ListarNotas();
						procesarNotas.setArguments(EstadoGTD);
						getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
						fragmentManager.beginTransaction()
									.addToBackStack("bandejaEntrada")
									.replace(R.id.content_frame, procesarNotas)
									.commit();
						break;
					
					case 3://Calendario
					
						CalendarView calendario = new CalendarView();
						calendario.setArguments(EstadoGTD);
						getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
						fragmentManager.beginTransaction()
									.addToBackStack("calendar")
									.replace(R.id.content_frame, calendario)
									.commit();
						break;
					
					case 4://Listado Proyectos

						EstadoGTD.putString("Estado","null");
						//este null es string xq se utiliza para la busqueda con el ORM
						EstadoGTD.putString("Project","null");
						ListarProyectos listadoProyectos = new ListarProyectos();
						listadoProyectos.setArguments(EstadoGTD);
						getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
						fragmentManager.beginTransaction()
									.addToBackStack("listadoProyectos")
									.replace(R.id.content_frame, listadoProyectos)
									.commit();
						break;
						
					case 5://Ajustes

						Fragment ajustes = new Settings();
						getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
						fragmentManager.beginTransaction()
									.addToBackStack("ajustes")
									.replace(R.id.content_frame, ajustes)
									.commit();
						break;
				}



				drawerList.setItemChecked(position, true);

				tituloSeccion = opcionesMenu[position];
				getSupportActionBar().setTitle(tituloSeccion);

				drawerLayout.closeDrawer(drawerList);
			}
		});

		
		
		tituloSeccion = getTitle();
		tituloApp = getTitle();

		drawerToggle = new ActionBarDrawerToggle(this, 
				drawerLayout,
				R.drawable.ic_navigation_drawer, 
				R.string.drawer_open,
				R.string.drawer_close) {

			public void onDrawerClosed(View view) {
				getSupportActionBar().setTitle(tituloSeccion);
				ActivityCompat.invalidateOptionsMenu(MainActivity.this);

			}

			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle(tituloApp);
				ActivityCompat.invalidateOptionsMenu(MainActivity.this);
				//Oculta Spinner GTD de la actionbar
				getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
				
			}
		};

		drawerLayout.setDrawerListener(drawerToggle);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (drawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		switch(item.getItemId())
		{
			case R.id.action_settings:
				FragmentManager fragmentManager = getSupportFragmentManager();
				fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
				Fragment ajustes = new Settings();
				getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
				fragmentManager.beginTransaction()
							.addToBackStack("ajustes")
							.replace(R.id.content_frame, ajustes)
							.commit();
				break;
			case R.id.action_search:
				//Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show(); TODO proxima version
				break;
			default:
				return super.onOptionsItemSelected(item);
		}

		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		boolean menuAbierto = drawerLayout.isDrawerOpen(drawerList);

		if(menuAbierto)
			menu.findItem(R.id.action_search).setVisible(false);
		else
			menu.findItem(R.id.action_search).setVisible(true);

		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		drawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		drawerToggle.onConfigurationChanged(newConfig);
	}
	

}