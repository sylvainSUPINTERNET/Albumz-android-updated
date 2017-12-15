package com.example.sylvain.albumz;

import android.app.FragmentTransaction;
import android.content.ClipData;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;


public class MainActivity extends FragmentActivity implements NavigationView.OnNavigationItemSelectedListener {


    public String current_user_name = null;
    public String current_user_email = null;


    protected FirebaseAuth mAuth;
    FirebaseUser currentUser;

    protected DatabaseReference Db;
    protected DatabaseReference Users;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();


    protected NavigationView navigationView;
    protected MenuItem itemAccount;



    protected TextView test_connected_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //test message welcom on connexion
        test_connected_message = findViewById(R.id.test_connected_message);



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.nav_account) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            redirectUpload();

        } else if (id == R.id.nav_gallery) {
            redirectListAlbum();

        } else if (id == R.id.nav_account) {
            // Check that the activity is using the layout version with
            // the fragment_container FrameLayout
            if (currentUser != null) {
                mAuth.getInstance().signOut();
                startActivity(getIntent());
            } else {
                redirectLogin();
            }
       
        } else if (id == R.id.nav_send) {
            if (currentUser != null)
            {
                redirectAlbumCreation();
            }
            else
            {
                redirectLogin();
            }

        }


            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);

            return true;
    }

    private void redirectListAlbum() {
        if (findViewById(R.id.fragment_container) != null) {
            // Create a new Fragment to be placed in the activity layout
            ListAlbumFragment listAlbumFragment = new ListAlbumFragment();
            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            listAlbumFragment.setArguments(getIntent().getExtras());
            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .replace(R.id.fragment_container, listAlbumFragment).commit();
        }
    }

    private void redirectUpload() {
        if (findViewById(R.id.fragment_container) != null) {
            // Create a new Fragment to be placed in the activity layout
            UploadFragment UploadFragment = new UploadFragment();
            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            UploadFragment.setArguments(getIntent().getExtras());
            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .replace(R.id.fragment_container, UploadFragment).commit();
        }
    }


    private void redirectAlbumCreation() {
        if (findViewById(R.id.fragment_container) != null) {
            // Create a new Fragment to be placed in the activity layout
            AlbumCreationFragment albumCreationFragment = new AlbumCreationFragment();
            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            albumCreationFragment.setArguments(getIntent().getExtras());
            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .replace(R.id.fragment_container, albumCreationFragment).commit();
        }
    }

    private void redirectLogin() {
        if (findViewById(R.id.fragment_container) != null) {
            // Create a new Fragment to be placed in the activity layout
            AccountFragment accountFragment = new AccountFragment();
            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            accountFragment.setArguments(getIntent().getExtras());
            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .replace(R.id.fragment_container, accountFragment).commit();
        }
    }

    public void redirectAlbumShow(Album album){
        if (findViewById(R.id.fragment_container) != null) {
            // Create a new Fragment to be placed in the activity layout
            AlbumShowFragment albumShowFragment = new AlbumShowFragment();
            Bundle args = new Bundle();
            args.putString("albumName",album.getAlbumName());
            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            albumShowFragment.setArguments(args);
            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .replace(R.id.fragment_container, albumShowFragment).commit();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        //DISPLAY DATA FROM CURRENT_USER
        if (currentUser == null) {
            //DISPLAY NOTHING no users
            Log.d("current_user", "USER IS NOT CONNECTED");
            navigationView.getMenu().getItem(0).setTitle("Sign / Sign in");

        } else {
            Log.d("current_user", currentUser.toString());
            Log.d("current_user", currentUser.getEmail());
            //test_connected_message.setText("Your email " + currentUser.getEmail() + " TODO, getDisplayName()");
            navigationView.getMenu().getItem(0).setTitle("Deconnexion");
        }

    }




}


