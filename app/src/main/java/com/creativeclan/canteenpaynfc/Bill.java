package com.creativeclan.canteenpaynfc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class Bill extends AppCompatActivity {
    TextView n;
    TextView q;

    TextView n2;
    TextView q2;

    TextView n3;
    TextView q3;


    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        pref = getApplicationContext().getSharedPreferences("bill", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        n = (TextView) findViewById(R.id.n);q = (TextView) findViewById(R.id.q);

        n.setText(pref.getString("item1N",null));
        q.setText(pref.getString("item1Q",null));

        n2 = (TextView) findViewById(R.id.n2);q2 = (TextView) findViewById(R.id.q2);

        n2.setText(pref.getString("item2N",null));
        q2.setText(pref.getString("item2Q",null));

        n3 = (TextView) findViewById(R.id.n3);q3 = (TextView) findViewById(R.id.q3);

        n3.setText(pref.getString("item3N",null));
        q3.setText(pref.getString("item3Q",null));
        //make them null before exiting

        editor.clear();
        editor.commit();


    }

  // @Override
  //  public void onBackPressed(){
   //     Intent intent = new Intent(Bill.this,MainActivity.class);
   //     startActivity(intent);
   // }
}
