package notepad;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.pronote.R;

public class NoteClassAdapter extends ArrayAdapter<NoteItem> {

	  int resource;

	 // public NoteClassAdapter(Context context,
	//                         int resource,
	 //                        List<NoteItem> items) {
	 //   super(context, resource, items);
	 //   this.resource = resource;
	 // }

	  private static class ViewHolder {
	        TextView text;
	        TextView date;
	    }

	    public NoteClassAdapter(Context context,int resource, ArrayList<NoteItem> items) {
	       super(context, resource, items);
	       this.resource=resource;
	    }
	
	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	  //  LinearLayout todoView;

		  ViewHolder viewholder;
	    NoteItem item = getItem(position);

	    String taskString = item.getTask();
	    Date createdDate = item.getCreated();
	    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
	    String dateString = sdf.format(createdDate);

	    if (convertView == null) {
	    //  todoView = new LinearLayout(getContext());
	    	viewholder = new ViewHolder();
	     // String inflater = Context.LAYOUT_INFLATER_SERVICE;
	     // LayoutInflater li;
	    //  li = (LayoutInflater)getContext().getSystemService(inflater);
	     // li.inflate(resource, todoView, true);
	    	
	    	LayoutInflater inflater = LayoutInflater.from(getContext());
	    	convertView = inflater.inflate(R.layout.notes_row, null);
	    	viewholder.date=(TextView) convertView.findViewById(R.id.date);
	    	viewholder.text=(TextView)convertView.findViewById(R.id.text1);
	    	convertView.setTag(viewholder);}
	    	else{
	    		viewholder=(ViewHolder)convertView.getTag();}
	    	viewholder.date.setText(taskString);
	    	viewholder.text.setText(dateString);
	    	return convertView;
	    	
	   // } else {
	  //    todoView = (LinearLayout) convertView;
	  //  }

	 //   TextView dateView = (TextView)todoView.findViewById(R.id.date);
	 //   TextView taskView = (TextView)todoView.findViewById(R.id.text1);

	 //   dateView.setText(dateString);
	 //   taskView.setText(taskString);

	 //   return todoView;
	    	
	    	
	  }
	}

