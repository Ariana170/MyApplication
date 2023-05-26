package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileC extends AppCompatActivity {

    GoogleSignInOptions gso;
    int t;
    GoogleSignInClient gsc;
    Button googlebtn;
    EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_c);

        username = findViewById(R.id.username);
        String nume = username.getText().toString();
        password = findViewById(R.id.password);

        googlebtn = findViewById(R.id.google);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);

        googlebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn(); //am deja login
            }
        });

        //TextView username = (TextView) findViewById(R.id.username);
        //TextView password = (TextView) findViewById(R.id.password);
        //Button loginbtn = (Button) findViewById(R.id.button);
        //loginbtn.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        if(username.getText().toString().equals("admin") && password.getText().toString().equals("admin") ){
        //            Toast.makeText(ProfileC.this,"LOGIN SUCCESSFUL",Toast.LENGTH_SHORT).show();
        //        }
        //        else Toast.makeText(ProfileC.this,"LOGIN FAILED",Toast.LENGTH_SHORT).show();

//            }
//        });


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView);
        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnItemSelectedListener(item ->{
            switch (item.getItemId()){
                case R.id.home:
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    overridePendingTransition(R.anim.slide_in_from_right,R.anim.slide_out_from_right);
                    finish();
                    return true;
                case R.id.profile:
                    return true;
                case R.id.mVideos:
                    startActivity(new Intent(getApplicationContext(),Music_Videos.class));
                    overridePendingTransition(R.anim.slide_in_from_right,R.anim.slide_out_from_right);
                    finish();
                    return true;
            }
            return false;
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        //verific daca user-ul este logat
        SessionManagement sessionManagement = new SessionManagement(ProfileC.this);
        int userID = sessionManagement.getSession();

        if(userID != -1){
            moveToProfileLogIn();
        }else {

        }
    }

    public void login(View view){

        User user = new User(12,"Ana");

        SessionManagement sessionManagement = new SessionManagement(ProfileC.this);
        sessionManagement.saveSession(user);

        moveToProfileLogIn();
    }

    private void moveToProfileLogIn(){
        Intent intent = new Intent(ProfileC.this,ProfileLogIn.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    void signIn(){
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent,1000);
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == 1000){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try{
                task.getResult(ApiException.class);
                moveToProfileLogIn();
            }catch (ApiException e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}