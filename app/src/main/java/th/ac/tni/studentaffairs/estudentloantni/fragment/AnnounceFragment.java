package th.ac.tni.studentaffairs.estudentloantni.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import th.ac.tni.studentaffairs.estudentloantni.DividerItemDecoration;
import th.ac.tni.studentaffairs.estudentloantni.R;
import th.ac.tni.studentaffairs.estudentloantni.adapter.AdapterWebContent;
import th.ac.tni.studentaffairs.estudentloantni.adapter.TopicAdapter;
import th.ac.tni.studentaffairs.estudentloantni.dao.DataModelWebContent;
import th.ac.tni.studentaffairs.estudentloantni.databinding.FragmentAnnounceBinding;

public class AnnounceFragment extends Fragment {

    private FragmentAnnounceBinding binding;

    private ArrayList<DataModelWebContent> listWeb;
    private AdapterWebContent adapterListWeb;
    private TopicAdapter mTopicAdapter;
    private RecyclerView recyclerView;

    long childCount;
    int childNum;

    android.app.AlertDialog spotDialog;

    DatabaseReference mDatabase,mTopicRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_announce,
                container,
                false);
        View rootView = binding.getRoot();
        initialization();
        return rootView;
    }

    private void checkData() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        Gson gson = new Gson();
        String json = sharedPrefs.getString("Announce Data", null);
        if(json!=null){
            Type type = new TypeToken<ArrayList<DataModelWebContent>>() {}.getType();
            listWeb = gson.fromJson(json, type);
        }
        else{
            if(haveNetworkConnection()){
                Title t2 = new Title();
                t2.execute();
            }
        }
    }

    private void initialization() {

        listWeb = new ArrayList<DataModelWebContent>();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mTopicRef = mDatabase.child("news");
        recyclerView = binding.recycleView;

        checkData();

        mTopicAdapter = new TopicAdapter(listWeb);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        binding.recycleView.setLayoutManager(linearLayoutManager);
        binding.recycleView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        binding.recycleView.setAdapter(mTopicAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                DataModelWebContent topicList = listWeb.get(position);

                Bundle b = new Bundle();
                b.putString("url", topicList.getLink());
                FragmentWebview f = new FragmentWebview();
                f.setArguments(b);

                getFragmentManager().beginTransaction().add(R.id.tap_fragment_1, f).hide(AnnounceFragment.this).addToBackStack(AnnounceFragment.class.getName()).commit();

            }

            @Override
            public void onLongClick(View view, int position) {
                DataModelWebContent topicList = listWeb.get(position);

                Bundle b = new Bundle();
                b.putString("url", topicList.getLink());
                FragmentWebview f = new FragmentWebview();
                f.setArguments(b);

                getFragmentManager().beginTransaction().add(R.id.tap_fragment_1, f).hide(AnnounceFragment.this).addToBackStack(AnnounceFragment.class.getName()).commit();

            }
        }));

//        checkData();
//        adapterListWeb = new AdapterWebContent(getActivity(), listWeb);
//        binding.lvWeb.setAdapter(adapterListWeb);
//        binding.lvWeb.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String getLink = adapterListWeb.getItem(position).getLink();
//                Log.d("myListView", "onItemClick: " + getLink);
//
//                Bundle b = new Bundle();
//                b.putString("url", getLink);
//                FragmentWebview f = new FragmentWebview();
//                f.setArguments(b);
//
//                getFragmentManager().beginTransaction().add(R.id.tap_fragment_1, f).hide(AnnounceFragment.this).addToBackStack(AnnounceFragment.class.getName()).commit();
//
//            }
//        });
        binding.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });

    }

    private void refreshContent(){

        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                if(haveNetworkConnection()) {
                    mTopicAdapter.clear();
                    Title t2 = new Title();
                    t2.execute();
                }
                mTopicAdapter.addAll(listWeb);
                binding.swipeContainer.setRefreshing(false);
            }
        }, 3000);

    }

    public void storeData(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(listWeb);
        editor.putString("Announce Data", json);
        editor.commit();
        Log.d("cacheData", ": Success");
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                haveConnectedWifi = true;
                Toast.makeText(getContext(), activeNetwork.getTypeName(), Toast.LENGTH_SHORT).show();
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                haveConnectedMobile = true;
            }
        } else {
            Toast.makeText(getContext(), "No Internet Connection!!", Toast.LENGTH_SHORT).show();
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    private class Title extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            spotDialog = new SpotsDialog(getActivity(),R.style.CustomDialog);
            spotDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                mTopicRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        childCount = dataSnapshot.getChildrenCount();
                        Log.d("ChildCount", "onChildAdded: " + childCount);
                        childNum = (int) childCount;
                        for(DataSnapshot dst : dataSnapshot.getChildren()){
                            Map<String, String> newPost = (Map<String, String>) dst.getValue();
                            listWeb.add(new DataModelWebContent(newPost.get("message"), newPost.get("link"),newPost.get("date"),newPost.get("type")));
                        }
                        Log.d("GetData", "onDataChange: " + listWeb.size());
                        spotDialog.dismiss();
                        mTopicAdapter.notifyDataSetChanged();
                        storeData();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

//                // Connect to the web site
//                org.jsoup.nodes.Document document = Jsoup.connect("http://studentaffairs.tni.ac.th/home/?cat=7").get();
//                // Get the html document title
//                Elements Date = document.select("time.published");
//                for (Element div : Date) {
//                    listDate.add(div.text());
//                }
//                Elements Content1 = document.select("div:has(h2.entry-title) .entry-title a");
//                int i=0;
//                for (Element div : Content1) {
//                    listWeb.add(new DataModelWebContent(div.attr("title"), div.attr("href"),listDate.get(i)));
//                    i++;
//                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
//            adapterListWeb.notifyDataSetChanged();
        }
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private AnnounceFragment.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final AnnounceFragment.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildLayoutPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildLayoutPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

}
