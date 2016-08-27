package th.ac.tni.studentaffairs.estudentloantni.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import th.ac.tni.studentaffairs.estudentloantni.R;
import th.ac.tni.studentaffairs.estudentloantni.dao.DataModelWebContent;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.MyViewHolder> {

    private ArrayList<DataModelWebContent> topicList = new ArrayList<DataModelWebContent>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, date;

        public MyViewHolder(View view) {
            super(view);
            date = (TextView) view.findViewById(R.id.txtDate);
            title = (TextView) view.findViewById(R.id.txtTitle);
        }
    }

    public TopicAdapter(ArrayList<DataModelWebContent> topicList) {
        this.topicList = topicList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listview_item_web_content, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DataModelWebContent topic = topicList.get(position);
        holder.title.setText(topic.getTitle());
        holder.date.setText(topic.getDate());
    }

    @Override
    public int getItemCount() {
        return topicList.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        topicList.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(ArrayList<DataModelWebContent> list) {
        topicList.addAll(list);
        notifyDataSetChanged();
    }
}
