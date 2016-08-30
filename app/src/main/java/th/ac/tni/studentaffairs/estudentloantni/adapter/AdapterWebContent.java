package th.ac.tni.studentaffairs.estudentloantni.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import th.ac.tni.studentaffairs.estudentloantni.dao.NewDao;
import th.ac.tni.studentaffairs.estudentloantni.R;

public class AdapterWebContent extends ArrayAdapter<NewDao> {
    public AdapterWebContent(Context context, ArrayList<NewDao> webContent) {
        super(context, 0, webContent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_item_web_content, parent, false);
        }

        NewDao data = getItem(position);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
        txtTitle.setText(data.getTitle());

        TextView txtDate = (TextView) convertView.findViewById(R.id.txtDate);
        txtDate.setText(data.getDate());

        return convertView;
    }
}
