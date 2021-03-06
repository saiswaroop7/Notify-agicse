package com.swaroop.sai.notify_agicse;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * Created by Sai on 13-09-2017.
 */

public class Settings extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static long back_pressed;
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mToggle;
    NavigationView nv;
    String ed1, ed2, id, userid, pass, repass;
    private EditText email, remail, edpass, edrepass;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    AlertDialog alertDialog;
    private DatabaseReference databaseReference, ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.settings);
        ref = FirebaseDatabase.getInstance().getReference("Users");
        alertDialog = new AlertDialog.Builder(Settings.this).create();
        alertDialog.setTitle("Reset Successful!");
        alertDialog.setMessage("All the event's statuses have been reset");
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
        nv = (NavigationView) findViewById(R.id.nav_view);
        nv.setNavigationItemSelectedListener(this);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReferenceFromUrl("https://notify-agicse.firebaseio.com/Users");
        Button b1 = (Button) findViewById(R.id.b_updatemail);
        email = (EditText) findViewById(R.id.ed_mail);
        remail = (EditText) findViewById(R.id.ed_remail);
        edpass = (EditText) findViewById(R.id.ed_pass);
        edrepass = (EditText) findViewById(R.id.ed_repass);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verify();
                if (!verify()) {
                    Toast.makeText(getApplicationContext(), "Update Error. Please try Again", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else {
                    Success();
                }
            }
        });
        Button b2 = (Button) findViewById(R.id.b_updatepass);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verify_pass();
                if (!verify_pass()) {
                    Toast.makeText(getApplicationContext(), "Verification Error. Please try Again", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else {
                    Success_pass();
                }
            }
        });
        Button b3 = (Button) findViewById(R.id.b_reseteve);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                databaseReference.child(id).child("event1").setValue("no");
                databaseReference.child(id).child("event2").setValue("no");
                databaseReference.child(id).child("event3").setValue("no");
                databaseReference.child(id).child("event4").setValue("no");
                databaseReference.child(id).child("event5").setValue("no");
                databaseReference.child(id).child("event6").setValue("no");
                databaseReference.child(id).child("event7").setValue("no");
                databaseReference.child(id).child("event8").setValue("no");
                confirm();
            }
        });
    }

    public void confirm() {
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public boolean verify_pass() {
        pass = edpass.getText().toString().trim();
        repass = edrepass.getText().toString().trim();
        boolean valid = true;
        if (pass.isEmpty() || edpass.length() < 4 || edpass.length() > 15) {
            edpass.setError("Password length must be 4-15 characters");
            valid = false;
        } else {
            edpass.setError(null);
        }
        if (!pass.equals(repass)) {
            edrepass.setError("Password does not match");
            valid = false;
        } else {
            edpass.setError(null);
        }
        return valid;
    }

    public boolean verify() {
        ed1 = email.getText().toString().trim();
        ed2 = remail.getText().toString().trim();

        boolean valid = true;
        if (ed1.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(ed1).matches()) {
            email.setError("Please enter a valid email address");
            valid = false;
        } else {
            email.setError(null);
        }
        if (!ed1.equals(ed2)) {
            remail.setError("Email does not match");
            valid = false;
        } else {
            remail.setError(null);
        }

        return valid;
    }

    public void Success() {
        progressDialog.setMessage("Updating Email");
        progressDialog.show();
        id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.updateEmail(ed1)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            databaseReference.child(id).child("Email").setValue(ed1);
                            Toast.makeText(getApplicationContext(), "User email address updated.", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        } else {
                            Toast.makeText(getApplicationContext(), "Update Error. Please try again", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    public void Success_pass() {
        progressDialog.setMessage("Updating Password");
        progressDialog.show();
        final String id2 = firebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseUser user2 = firebaseAuth.getInstance().getCurrentUser();
        user2.updatePassword(pass)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            databaseReference.child(id2).child("Password").setValue(pass);
                            Toast.makeText(getApplicationContext(), "Password updated.", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        } else {
                            Toast.makeText(getApplicationContext(), "Update Error. Please try again", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout back = (DrawerLayout) findViewById(R.id.settings);
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
            case R.id.nav_tech:
                startActivity(new Intent(this, Tech.class));
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
            case R.id.nav_logout:
                firebaseAuth.signOut();
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(this, Login.class));
                    finish();
                }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.settings);
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