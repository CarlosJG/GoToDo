package net.carlosjg.gotodo;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterMenu  extends ArrayAdapter<String> {
Context context;
private ArrayList<String> TextValue = new ArrayList<String>();

public AdapterMenu(Context context, ArrayList<String> TextValue) {
    super(context, R.layout.single_item_navdrawer, TextValue);
    this.context = context;
    this.TextValue= TextValue;

}

 
@Override
public View getView(int position, View coverView, ViewGroup parent) {

    LayoutInflater inflater = (LayoutInflater) context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View rowView = inflater.inflate(R.layout.single_item_navdrawer,
            parent, false);

    ImageView iv = (ImageView)rowView.findViewById(R.id.icon_menu);
    
    switch (position) {
	case 0: 
        iv.setImageResource(R.drawable.ic_icon_menu_new);
    break;
	case 1: 
        iv.setImageResource(R.drawable.ic_menu_nextactions);
    break;
	case 2: 
        iv.setImageResource(R.drawable.ic_menu_process);
    break;
	case 3: 
        iv.setImageResource(R.drawable.ic_menu_calendar);
    break;
	case 4: 
        iv.setImageResource(R.drawable.ic_menu_projects);
    break;
	case 5: 
        iv.setImageResource(R.drawable.ic_menu_settings);
    break;
    }
    

    TextView text1 = (TextView)rowView.findViewById(R.id.text);
    text1.setText(TextValue.get(position));

    return rowView;

}

}