package customer.thewaiapp.com;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by KRISHNA on 09-12-2016.
 */

public class SpinnerAdaptor extends BaseAdapter {

    private List<String> listdata;
    private Activity activity;
    private LayoutInflater layoutInflater;

    public SpinnerAdaptor(List<String> listdata, Activity activity) {
        this.listdata = listdata;
        this.activity = activity;
        this.layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return listdata.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (convertView == null)
            view = layoutInflater.inflate(R.layout.spinner_item, parent, false);
        TextView tv = (TextView) view.findViewById(R.id.textView9);
        tv.setText(listdata.get(position));
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
//        View view = super.getDropDownView(position, convertView, parent);
//        LinearLayout l1 = (LinearLayout) view;
        View view = convertView;
        if (convertView == null)
            view = layoutInflater.inflate(R.layout.spinner_dropdown, parent, false);
        TextView tv = (TextView) view.findViewById(R.id.textviewDropdown);
        tv.setText(listdata.get(position));
        return view;
    }
}
