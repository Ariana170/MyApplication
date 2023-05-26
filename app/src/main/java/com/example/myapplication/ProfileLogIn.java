package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import java.io.ByteArrayOutputStream;
import java.util.BitSet;

public class ProfileLogIn extends AppCompatActivity {

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    TextView name, email;
    private final int gallery_req_code = 1000;
    ImageView picture;
    DataBase DB;
    String nameDB;
    Bitmap imageDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_log_in);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct!=null){
            String personName = acct.getDisplayName();
            String persEmail = acct.getEmail();

            name.setText(personName);
            email.setText(persEmail);
        }

        picture = findViewById(R.id.img_gallery);
        Button addpicture = findViewById(R.id.button_gallery);
        Button savepicture = findViewById(R.id.button_save);
        DB = new DataBase(this);



        addpicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iGallery = new Intent(Intent.ACTION_PICK);
                iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGallery,gallery_req_code);
            }
        });
        int x = 0;
        savepicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = String.format("img%d", x);
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.music);
                ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArray);

                byte[] img = byteArray.toByteArray();

                boolean insert = DB.insertdata(name, img);
                if(insert == true){
                    Toast.makeText(ProfileLogIn.this,"DATA SAVED",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ProfileLogIn.this,"DATA NOT SAVED",Toast.LENGTH_SHORT).show();
                }

                imageDB = DB.getImage(name);
                picture.setImageBitmap(imageDB);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK){
            if(requestCode==gallery_req_code){

                picture.setImageURI(data.getData());
            }
        }
    }

    public void logout(View view){
        SessionManagement sessionManagement = new SessionManagement(ProfileLogIn.this);
        sessionManagement.removeSession();

        moveToLogin();
    }
    private void moveToLogin(){
        Intent intent = new Intent(ProfileLogIn.this, ProfileC.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}