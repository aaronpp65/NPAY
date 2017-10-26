package com.creativeclan.canteenpaynfc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    EditText  user;
    EditText pass;
    Button  login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
       user = (EditText)findViewById(R.id.user);
        pass = (EditText)findViewById(R.id.pass);
       login = (Button) findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(user.getText().toString().equals("pp") && pass.getText().toString().equals("pp")) {

                 Intent intent = new Intent(Login.this,Recharge.class);
                    startActivity(intent);

                    user.getText().clear();
                    pass.getText().clear();
                }

                  else
                {
                    Toast.makeText(Login.this,"Error",Toast.LENGTH_LONG).show();}

                Log.i("h",user.getText().toString());
                Log.i("h",pass.getText().toString());
            }


        });




    }
}
