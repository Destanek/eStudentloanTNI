package th.ac.tni.studentaffairs.estudentloantni.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import th.ac.tni.studentaffairs.estudentloantni.fragment.ChangePasswordFragment;
import th.ac.tni.studentaffairs.estudentloantni.fragment.DocumentFragment;
import th.ac.tni.studentaffairs.estudentloantni.fragment.EditAccountFragment;
import th.ac.tni.studentaffairs.estudentloantni.fragment.LoginFragment;
import th.ac.tni.studentaffairs.estudentloantni.adapter.PageAdapter;
import th.ac.tni.studentaffairs.estudentloantni.R;
import th.ac.tni.studentaffairs.estudentloantni.manager.RegistrationService;
import th.ac.tni.studentaffairs.estudentloantni.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,
                R.layout.activity_main);
        Firebase.setAndroidContext(this);
        Intent i = new Intent(this, RegistrationService.class);
        startService(i);
        initInstance();
    }

    private void initInstance() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.toolbar_title);
        setSupportActionBar(toolbar);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.news_tap));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.document_tap));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PageAdapter adapter = new PageAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0) {
                    toolbar.setBackgroundColor(0xff5D7E7D);
                    tabLayout.setBackgroundColor(0xff5D7E7D);
                } else if (tab.getPosition() == 1) {
                    toolbar.setBackgroundColor(0xff5d899d);
                    tabLayout.setBackgroundColor(0xff5d899d);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        final View nav_header = binding.navigationView.getHeaderView(0);
        final TextView user = (TextView) nav_header.findViewById(R.id.username);
        final TextView email = (TextView) nav_header.findViewById(R.id.email);
        final CircleImageView photo = (CircleImageView) nav_header.findViewById(R.id.profile_image);

        final Firebase ref = new Firebase("https://sizzling-torch-4935.firebaseio.com/");
        ref.addAuthStateListener(new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                if (authData != null) {
                    // user is logged in
//                    getSupportFragmentManager().beginTransaction().replace(R.id.tap_fragment_2,new DocumentFragment()).commit();
                    binding.navigationView.getMenu().clear();
                    binding.navigationView.inflateMenu(R.menu.drawer_login);
                    String uid = authData.getUid();
                    ref.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Map<String, String> newPost = (Map<String, String>) dataSnapshot.getValue();
                            String name = newPost.get("firstname") + " " + newPost.get("lastname");
                            user.setText(name);
                            email.setText(newPost.get("email"));

                            Log.d("Moo", "Name: " + name + " Email : " + newPost.get("email"));
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                    photo.setImageResource(R.drawable.ic_user_login);
                } else {
                    // user is not logged in
//                    getSupportFragmentManager().beginTransaction().replace(R.id.tap_fragment_2,new LoginFragment()).commit();
                    binding.navigationView.getMenu().clear();
                    binding.navigationView.inflateMenu(R.menu.drawer);
                    user.setText(" ");
                    email.setText(" ");
                    photo.setImageResource(R.drawable.ic_user_logout);
                }
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        binding.navigationView.getMenu().findItem(R.id.drawer_home).setChecked(true);
                        break;
                    case 1:
                        binding.navigationView.getMenu().findItem(R.id.drawer_home).setChecked(false);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        binding.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Checking if the item is in checked state or not, if not make it in checked state
//                if(menuItem.isChecked())
//                    menuItem.setChecked(false);
//                else
//                    menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {

                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.drawer_help:
                        Toast.makeText(getApplicationContext(), R.string.help_selected, Toast.LENGTH_SHORT).show();
                        return true;
                    // For rest of the options we just show a toast on click
                    case R.id.drawer_home:
                        viewPager.setCurrentItem(0);
                        Toast.makeText(getApplicationContext(), R.string.home_selected, Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.drawer_login:
                        viewPager.setCurrentItem(1);
                        getSupportFragmentManager().beginTransaction().replace(R.id.tap_fragment_2, new LoginFragment()).addToBackStack(MainActivity.class.getName()).commit();
                        Toast.makeText(getApplicationContext(), R.string.login_selected, Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.drawer_logout:
                        ref.unauth();
                        viewPager.setCurrentItem(1);
                        getSupportFragmentManager().beginTransaction().replace(R.id.tap_fragment_2, new LoginFragment()).addToBackStack(MainActivity.class.getName()).commit();
                        Toast.makeText(getApplicationContext(), R.string.logout_selected, Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.drawer_pass:
                        viewPager.setCurrentItem(1);
                        getSupportFragmentManager().beginTransaction().replace(R.id.tap_fragment_2, new ChangePasswordFragment()).addToBackStack(DocumentFragment.class.getName()).commit();
                        Toast.makeText(getApplicationContext(), R.string.change_password_selected, Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.drawer_edit:
                        viewPager.setCurrentItem(1);
                        getSupportFragmentManager().beginTransaction().replace(R.id.tap_fragment_2, new EditAccountFragment()).addToBackStack(DocumentFragment.class.getName()).commit();
                        Toast.makeText(getApplicationContext(), R.string.edit_account_selected, Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.drawer_settings:
                        Toast.makeText(getApplicationContext(), R.string.setting_selected, Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        Toast.makeText(getApplicationContext(), R.string.wrong_selected, Toast.LENGTH_SHORT).show();
                        return true;

                }
            }
        });

        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}


