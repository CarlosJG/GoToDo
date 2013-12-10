package net.carlosjg.gotodo;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CalendarView extends Fragment{
	private View view;
	public GregorianCalendar month, itemmonth;
    public CalendarAdapter adapter;
    public Handler handler;// se usa para los eventos y el punto
    public ArrayList<String> items;
                                                                   
    ArrayList<String> event;
    LinearLayout rLayout;
    ArrayList<String> date;
    ArrayList<String> desc;
    
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {view = inflater.inflate(R.layout.calendar, 
                  container, false); 
			
			Create(savedInstanceState);
		
	      return view;
	}
    
    public void Create(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Locale.setDefault(Locale.US);

            rLayout = (LinearLayout) view.findViewById(R.id.text);
            month = (GregorianCalendar) GregorianCalendar.getInstance();
            itemmonth = (GregorianCalendar) month.clone();

            items = new ArrayList<String>();

            adapter = new CalendarAdapter(getActivity(), month);

            GridView gridview = (GridView) view.findViewById(R.id.gridview);
            gridview.setAdapter(adapter);

            handler = new Handler();
            handler.post(calendarUpdater);

            TextView title = (TextView) view.findViewById(R.id.title);
            title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));

            RelativeLayout previous = (RelativeLayout) view.findViewById(R.id.previous);

            previous.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                            setPreviousMonth();
                            refreshCalendar();
                    }
            });

            RelativeLayout next = (RelativeLayout) view .findViewById(R.id.next);
            next.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                            setNextMonth();
                            refreshCalendar();

                    }
            });

            gridview.setOnItemClickListener(new OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                            // Borrar vista anterior si la hay
                            if (((LinearLayout) rLayout).getChildCount() > 0) {
                                    ((LinearLayout) rLayout).removeAllViews();
                            }
                            desc = new ArrayList<String>();
                            date = new ArrayList<String>();
                            ((CalendarAdapter) parent.getAdapter()).setSelected(v);
                            String selectedGridDate = CalendarAdapter.dayString
                                            .get(position);
                            String[] separatedTime = selectedGridDate.split("-");
                            String gridvalueString = separatedTime[2].replaceFirst("^0*",
                                            "");// solo ultima parte de la fecha; 2 de 2012-12-02.
                            int gridvalue = Integer.parseInt(gridvalueString);
                            // mes previo o anterior
                            if ((gridvalue > 10) && (position < 8)) {
                                    setPreviousMonth();
                                    refreshCalendar();
                            } else if ((gridvalue < 7) && (position > 28)) {
                                    setNextMonth();
                                    refreshCalendar();
                            }
                            ((CalendarAdapter) parent.getAdapter()).setSelected(v);

                            for (int i = 0; i < CalendarUtility.startDates.size(); i++) {
                                    if (CalendarUtility.startDates.get(i).equals(selectedGridDate)) {
                                            desc.add(CalendarUtility.nameOfEvent.get(i));
                                    }
                            }

                            if (desc.size() > 0) {
                                    for (int i = 0; i < desc.size(); i++) {
                                            TextView rowTextView = new TextView(getActivity());
                                            rowTextView.setText(getResources().getString(R.string.evento) + desc.get(i));
                                            rowTextView.setTextColor(Color.BLACK);
                                            rowTextView.setTextSize(12);
                                            rLayout.addView(rowTextView);
                                    }

                            }

                            desc = null;

                    }

            });
    }

    protected void setNextMonth() {
            if (month.get(GregorianCalendar.MONTH) == month
                            .getActualMaximum(GregorianCalendar.MONTH)) {
                    month.set((month.get(GregorianCalendar.YEAR) + 1),
                                    month.getActualMinimum(GregorianCalendar.MONTH), 1);
            } else {
                    month.set(GregorianCalendar.MONTH,
                                    month.get(GregorianCalendar.MONTH) + 1);
            }

    }

    protected void setPreviousMonth() {
            if (month.get(GregorianCalendar.MONTH) == month
                            .getActualMinimum(GregorianCalendar.MONTH)) {
                    month.set((month.get(GregorianCalendar.YEAR) - 1),
                                    month.getActualMaximum(GregorianCalendar.MONTH), 1);
            } else {
                    month.set(GregorianCalendar.MONTH,
                                    month.get(GregorianCalendar.MONTH) - 1);
            }

    }

    protected void showToast(String string) {
            Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();

    }

    public void refreshCalendar() {
            TextView title = (TextView) getActivity().findViewById(R.id.title);

            adapter.refreshDays();
            adapter.notifyDataSetChanged();
            handler.post(calendarUpdater); 

            title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
    }

    public Runnable calendarUpdater = new Runnable() {

            @Override
            public void run() {
                    items.clear();

                    // Imprimir eventos
                    event = CalendarUtility.readCalendarEvent(getActivity());
                    for (int i = 0; i < CalendarUtility.startDates.size(); i++) {
                            itemmonth.add(GregorianCalendar.DATE, 1);
                            items.add(CalendarUtility.startDates.get(i).toString());
                    }
                    adapter.setItems(items);
                    adapter.notifyDataSetChanged();
            }
    };
    
	@Override
	public void onStop(){
		ImageView Info= (ImageView)getActivity().findViewById(R.id.infoApp);
		Info.setVisibility(View.VISIBLE);
		super.onStop();
		
		
	}

}
