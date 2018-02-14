package th.co.dest.anek.studentloan.fragment;

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
import java.util.List;

import dmax.dialog.SpotsDialog;
import th.co.dest.anek.studentloan.DividerItemDecoration;
import th.co.dest.anek.studentloan.R;
import th.co.dest.anek.studentloan.adapter.TopicAdapter;
import th.co.dest.anek.studentloan.dao.NewDao;
import th.co.dest.anek.studentloan.databinding.FragmentAnnounceBinding;

public class AnnounceFragment extends Fragment {

    private FragmentAnnounceBinding binding;

    private ArrayList<NewDao> listWeb;
    private TopicAdapter mTopicAdapter;

    android.app.AlertDialog spotDialog;

    DatabaseReference mDatabase, mTopicRef;

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
        if (json != null) {
            Type type = new TypeToken<ArrayList<NewDao>>() {
            }.getType();
            listWeb = gson.fromJson(json, type);
        } else {
            if (haveNetworkConnection()) {
                Title t2 = new Title();
                t2.execute();
            }
        }
    }

    private void initialization() {

        listWeb = new ArrayList<NewDao>();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mTopicRef = mDatabase.child("news");

        RecyclerView recyclerView = binding.recycleView;

        checkData();

        mTopicAdapter = new TopicAdapter(listWeb);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        binding.recycleView.setLayoutManager(linearLayoutManager);
        binding.recycleView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        binding.recycleView.setAdapter(mTopicAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                openWebView(position);
            }

            @Override
            public void onLongClick(View view, int position) {
                openWebView(position);
            }
        }));

        binding.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });

    }

    public void openWebView(int position){
        NewDao topicList = listWeb.get(position);
        if(topicList.getLink()==null){
            Toast.makeText(getContext(), R.string.no_data,Toast.LENGTH_SHORT).show();
        }
        else{
            Bundle b = new Bundle();
            b.putString("url", topicList.getLink());
            FragmentWebview f = new FragmentWebview();
            f.setArguments(b);
            getFragmentManager().beginTransaction().add(R.id.tap_fragment_1, f).hide(AnnounceFragment.this).addToBackStack(AnnounceFragment.class.getName()).commit();
        }

    }

    private void refreshContent() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (haveNetworkConnection()) {
                    mTopicAdapter.clear();
                    Title t2 = new Title();
                    t2.execute();
                }
                mTopicAdapter.addAll(listWeb);
                binding.swipeContainer.setRefreshing(false);
            }
        }, 3000);

    }

    public void storeData() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(listWeb);
        editor.putString("Announce Data", json);
        editor.commit();
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                haveConnectedWifi = true;
//                Toast.makeText(getContext(), activeNetwork.getTypeName(), Toast.LENGTH_SHORT).show();
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

            spotDialog = new SpotsDialog(getActivity(), R.style.CustomDialog);
            spotDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                ValueEventListener postListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // init list for store
                        List<NewDao> listNew = new ArrayList<NewDao>();
                        // get data from firebase
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            NewDao dao = data.getValue(NewDao.class);
                            // bind data to list
                            listNew.add(dao);
                        }
                        // loop end to start
                        int countList = listNew.size();
                        for (int i = countList - 1; i >= 0; i--) {
                            // bind to listWeb
                            listWeb.add(listNew.get(i));
                            spotDialog.dismiss();
                            mTopicAdapter.notifyDataSetChanged();
                            storeData();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                mTopicRef.addValueEventListener(postListener);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
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
