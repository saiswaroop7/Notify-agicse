package com.swaroop.sai.notify_agicse;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Sai on 26-07-2017.
 */

public class About extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    private static long back_pressed;
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mToggle;
    NavigationView nv;
    String userid;
    DatabaseReference ref;
    private FirebaseAuth firebaseAuth;

    @Override
    protected  void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        firebaseAuth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference("Users");
        mDrawerLayout = (DrawerLayout) findViewById(R.id.about);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.setDrawerListener(mToggle);
        mToggle.syncState();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String id = firebaseAuth.getInstance().getCurrentUser().getUid();
                userid = dataSnapshot.child(id).child("Name").getValue().toString();
                TextView textView = (TextView) findViewById(R.id.header_tv);
                textView.setText("Welcome, " + userid);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nv= (NavigationView) findViewById(R.id.nav_view);
        nv.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed(){
        DrawerLayout back= (DrawerLayout) findViewById(R.id.about);
        if (back.isDrawerOpen(GravityCompat.START)) {
            back.closeDrawer(GravityCompat.START);
        } else if (back_pressed + 2000 > System.currentTimeMillis()) {
            firebaseAuth.signOut();
            super.onBackPressed();
        }
        else
            Toast.makeText(getBaseContext(), "Press again to exit", Toast.LENGTH_SHORT).show();
        back_pressed = System.currentTimeMillis();
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item){
        int id=item.getItemId();
        displaySelectedScreen(id);

        return true;
    }


    private void displaySelectedScreen(int id){
        switch(id)
        {
            case R.id.nav_home:
                startActivity(new Intent(this, MainActivity.class));finish();
                break;
            case R.id.nav_tech:
                startActivity(new Intent(this, Tech.class));finish();
                break;
            case R.id.nav_nontech:
                startActivity(new Intent(this, Non_tech.class));finish();
                break;
            case R.id.nav_settings:
                startActivity(new Intent(this, Settings.class));
                finish();
                break;
            case R.id.nav_Dev:
                startActivity(new Intent(this, Dev.class));finish();
                break;

            case R.id.nav_logout:
                firebaseAuth.signOut();
                if (firebaseAuth.getCurrentUser() == null)
                {
                    startActivity(new Intent(this, Login.class));
                    finish();
                }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.about);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item))
        {
            return mToggle.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }
}
