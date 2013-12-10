package net.carlosjg.gotodo;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
 
public class AdapterListaProyectos extends ArrayAdapter<Project> {
    private final LayoutInflater mInflater;
 
    public AdapterListaProyectos(Context context) {
        super(context, android.R.layout.simple_list_item_2);
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
 
    public void setData(List<Project> data) {
        clear();
        if (data != null) {
            for (Project appEntry : data) {
                add(appEntry);
            }
        }
    }
 
    @Override public View getView(int position, View convertView, ViewGroup parent) {
        View view;
 
        if (convertView == null) {
            view = mInflater.inflate(R.layout.single_item_list, parent, false);
        } else {
            view = convertView;
        }
 
        Project item = getItem(position);
        
        //set icono prioridad        
        Drawable iconPrioridad=view.getResources().getDrawable(R.drawable.ic_priority_grey);
        
        switch(item.ProjPR) {
        case 1:
        	iconPrioridad=view.getResources().getDrawable(R.drawable.ic_priority_green);
            break;
        case 2:
        	iconPrioridad=view.getResources().getDrawable(R.drawable.ic_priority_orange);
            break;
        case 3:
        	iconPrioridad=view.getResources().getDrawable(R.drawable.ic_priority_red);
            break;
        default:
        	iconPrioridad=view.getResources().getDrawable(R.drawable.ic_priority_grey);
    }

        ((ImageView)view.findViewById(R.id.icon_list)).setImageDrawable(iconPrioridad);
        //set Titulo nota
        ((TextView)view.findViewById(R.id.note_titulo)).setText(item.ProjNombre);

        //Set nombre proyecto
//        if (item.ProjectID >0){
//        	List<Project> Proyecto =Note.find(Project.class, "ID = ?",String.valueOf(item.ProjectID)); 
//        	//Se crea lista solo para ver el count (ver estress de la BD aqui) 
//        	//Esperando consulta al desarrollador del ORM en GitHub
//        	List<Note> countP=Note.find(Note.class,"PROJECT_ID = ?",String.valueOf(item.ProjectID));
//        	int countPr=countP.size();	        	
//	        ((TextView)view.findViewById(R.id.note_project)).setText(Proyecto.get(0).ProjNombre+"("+String.valueOf(countPr)+")");
//	        ((TextView)view.findViewById(R.id.note_project)).setBackgroundColor(Color.GRAY);
//	        ((TextView)view.findViewById(R.id.note_project)).setTextColor(Color.WHITE);
//
//        }
        
        return view;
    }
} 