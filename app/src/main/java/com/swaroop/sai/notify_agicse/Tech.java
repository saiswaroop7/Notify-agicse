package com.swaroop.sai.notify_agicse;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static java.lang.Thread.sleep;

/**
 * Created by Sai on 26-07-2017.
 */

public class Tech extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static long back_pressed;
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mToggle;
    NavigationView nv;
    String id, userid;
    boolean isImageFitToScreen;
    TextView tv1, tv2, tv3, tv4, info_event1, info_event2, info_event3, info_event4;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference, info, events, ref;
    private StorageReference ev1, ev2, ev3, ev4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tech);
        ref = FirebaseDatabase.getInstance().getReference("Users");
        tv1 = (TextView) findViewById(R.id.tv_event1);
        tv2 = (TextView) findViewById(R.id.tv_event2);
        tv3 = (TextView) findViewById(R.id.tv_event3);
        tv4 = (TextView) findViewById(R.id.tv_event4);
        info_event1 = (TextView) findViewById(R.id.info_event1);
        info_event2 = (TextView) findViewById(R.id.info_event2);
        info_event3 = (TextView) findViewById(R.id.info_event3);
        info_event4 = (TextView) findViewById(R.id.info_event4);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
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
        databaseReference = firebaseDatabase.getReferenceFromUrl("https://notify-agicse.firebaseio.com/Events");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String eve1 = dataSnapshot.child("Technical").child("Event1").getValue().toString();
                if(!eve1.equals("none"))
                {
                tv1.setText(eve1 + " :");
                }
                    if(eve1.equals("none")){
                    View b = findViewById(R.id.b_event1);
                    b.setVisibility(View.GONE);
                }}
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String eve2 = dataSnapshot.child("Technical").child("Event2").getValue().toString();
                tv2.setText(eve2 + " :");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String eve3 = dataSnapshot.child("Technical").child("Event3").getValue().toString();
                tv3.setText(eve3 + " :");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String eve4 = dataSnapshot.child("Technical").child("Event4").getValue().toString();
                tv4.setText(eve4 + " :");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        info = firebaseDatabase.getReferenceFromUrl("https://notify-agicse.firebaseio.com/Events_info");
        info.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String eve1_info = dataSnapshot.child("Event1").getValue().toString();
                info_event1.setText(eve1_info);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        info.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String eve2_info = dataSnapshot.child("Event2").getValue().toString();
                info_event2.setText(eve2_info);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        info.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String eve3_info = dataSnapshot.child("Event3").getValue().toString();
                info_event3.setText(eve3_info);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        info.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String eve4_info = dataSnapshot.child("Event4").getValue().toString();
                info_event4.setText(eve4_info);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ev1 = FirebaseStorage.getInstance().getReferenceFromUrl("gs://notify-agicse.appspot.com/Technical/event1.jpg");
        ev2 = FirebaseStorage.getInstance().getReferenceFromUrl("gs://notify-agicse.appspot.com/Technical/event2.jpg");
        ev3 = FirebaseStorage.getInstance().getReferenceFromUrl("gs://notify-agicse.appspot.com/Technical/event3.jpg");
        ev4 = FirebaseStorage.getInstance().getReferenceFromUrl("gs://notify-agicse.appspot.com/Technical/event4.jpg");
        ImageView e1 = (ImageView) findViewById(R.id.event1_view);
        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(ev1)
                .placeholder(R.drawable.photoshop)
                .into(e1);
        ImageView e2 = (ImageView) findViewById(R.id.event2_view);
        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(ev2)
                .placeholder(R.drawable.photoshop)
                .into(e2);
        ImageView e3 = (ImageView) findViewById(R.id.event3_view);
        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(ev3)
                .placeholder(R.drawable.photoshop)
                .into(e3);
        ImageView e4 = (ImageView) findViewById(R.id.event4_view);
        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(ev4)
                .placeholder(R.drawable.photoshop)
                .into(e4);
        events = firebaseDatabase.getReferenceFromUrl("https://notify-agicse.firebaseio.com/Users");
        Button b1 = (Button) findViewById(R.id.b_event1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                events.child(id).child("event1").setValue("yes");
                Toast.makeText(getApplicationContext(), "You will be Notified", Toast.LENGTH_LONG).show();
            }
        });
        Button b2 = (Button) findViewById(R.id.b_event2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                events.child(id).child("event2").setValue("yes");
                Toast.makeText(getApplicationContext(), "You will be Notified", Toast.LENGTH_LONG).show();
            }
        });
        Button b3 = (Button) findViewById(R.id.b_event3);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                events.child(id).child("event3").setValue("yes");
                Toast.makeText(getApplicationContext(), "You will be Notified", Toast.LENGTH_LONG).show();
            }
        });
        Button b4 = (Button) findViewById(R.id.b_event4);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                events.child(id).child("event4").setValue("yes");
                Toast.makeText(getApplicationContext(), "You will be Notified", Toast.LENGTH_LONG).show();
            }
        });
        mDrawerLayout = (DrawerLayout) findViewById(R.id.tech);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.setDrawerListener(mToggle);
        mToggle.syncState();
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nv = (NavigationView) findViewById(R.id.nav_view);
        nv.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout back = (DrawerLayout) findViewById(R.id.tech);
        if (back.isDrawerOpen(GravityCompat.START)) {
            back.closeDrawer(GravityCompat.START);
        } else if (back_pressed + 2000 > System.currentTimeMillis()) {
            firebaseAuth.signOut();
            super.onBackPressed();
        } else
            Toast.makeText(getBaseContext(), "Press again to exit", Toast.LENGTH_SHORT).show();
        back_pressed = System.currentTimeMillis();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        displaySelectedScreen(id);

        return true;
    }


    private void displaySelectedScreen(int id) {
        switch (id) {

            case R.id.nav_home:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            case R.id.nav_nontech:
                startActivity(new Intent(this, Non_tech.class));
                finish();
                break;
            case R.id.nav_about:
                startActivity(new Intent(this, About.class));
                finish();
                break;
            case R.id.nav_Dev:
                startActivity(new Intent(this, Dev.class));
                finish();
                break;
            case R.id.nav_settings:
                startActivity(new Intent(this, Settings.class));
                finish();
                break;
            case R.id.nav_logout:
                firebaseAuth.signOut();
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(this, Login.class));
                    finish();
                }

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.tech);
        drawer.closeDrawer(GravityCompat.START);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return mToggle.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }
}

