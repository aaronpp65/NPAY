package com.creativeclan.canteenpaynfc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity
{
 Display disp = new Display();

    public void sub(View view)
    {
        switch (view.getId())
        {
            case R.id.button01:
            {
                TextView textView02 = (TextView) findViewById(R.id.textView02);
                int qty=Integer.parseInt(textView02.getText().toString());
                if(qty>0)
                qty--;
                String quantity = String.valueOf(qty);
                textView02.setText(quantity);
            }break;

            case R.id.button11:
            {
                TextView textView12 = (TextView) findViewById(R.id.textView12);
                int qty=Integer.parseInt(textView12.getText().toString());
                if(qty>0)
                qty--;
                String quantity = String.valueOf(qty);
                textView12.setText(quantity);
            }break;
            case R.id.button21:
            {
                TextView textView22 = (TextView) findViewById(R.id.textView22);
                int qty=Integer.parseInt(textView22.getText().toString());
                if(qty>0)
                qty--;
                String quantity = String.valueOf(qty);
                textView22.setText(quantity);
            }break;
        }

    }



    public void add(View view)
     {
         switch (view.getId())
         {
             case R.id.button03:
             {

                 TextView textView02 = (TextView) findViewById(R.id.textView02);
                 int qty=Integer.parseInt(textView02.getText().toString());
                 qty++;
                 String quantity = String.valueOf(qty);
                 textView02.setText(quantity);


             }
             break;
             case R.id.button13:
             {

                 TextView textView12 = (TextView) findViewById(R.id.textView12);
                 int qty=Integer.parseInt(textView12.getText().toString());
                 qty++;
                 String quantity = String.valueOf(qty);

                 textView12.setText(quantity);
             }break;
             case R.id.button23:
             {

                 TextView textView22 = (TextView) findViewById(R.id.textView22);
                 int qty=Integer.parseInt(textView22.getText().toString());
                 qty++;
                 String quantity = String.valueOf(qty);

                 textView22.setText(quantity);
             }break;
         }
     }

     public  void checkout(View view)
     {
         SharedPreferences pref = getApplicationContext().getSharedPreferences("bill", MODE_PRIVATE);
         SharedPreferences.Editor editor = pref.edit();





         int tot=0;
         CheckBox checkBox00 =(CheckBox) findViewById(R.id.checkBox00);
         CheckBox checkBox10 =(CheckBox) findViewById(R.id.checkBox10);
         CheckBox checkBox20 =(CheckBox) findViewById(R.id.checkBox20);


         if(checkBox00.isChecked()){
             TextView textView02 = (TextView) findViewById(R.id.textView02);
             int qty = Integer.parseInt(textView02.getText().toString());
             tot += (qty * 8);

             editor.putString("item1Q",textView02.getText().toString());
             editor.putString("item1N",checkBox00.getText().toString());
             Log.i("n",checkBox00.getText().toString());

         }
         if(checkBox10.isChecked()){
             TextView textView12 = (TextView) findViewById(R.id.textView12);
             int qty = Integer.parseInt(textView12.getText().toString());
             tot += (qty * 8);

             editor.putString("item2Q",textView12.getText().toString());
             editor.putString("item2N",checkBox10.getText().toString());
         }
         if(checkBox20.isChecked()){
             TextView textView22 = (TextView) findViewById(R.id.textView22);
             int qty = Integer.parseInt(textView22.getText().toString());
             tot += (qty * 8);

            editor.putString("item3Q",textView22.getText().toString());
            editor.putString("item3N",checkBox20.getText().toString());
         }

         editor.apply();

         String amount=Integer.toString(tot);
         Log.i("amount",amount);
         //disp.total(tot);
         if(tot==0)
         {
             new AlertDialog.Builder(this)
                     .setIcon(android.R.drawable.ic_dialog_alert)
                     .setTitle("Oops!!")
                     .setMessage("It seems that you haven't selected any food item.Make use of the checkbox for the same")
                     .setPositiveButton("Okay",null)
                     .show();

         }
         else
         {


             Intent intent = new Intent(MainActivity.this, Display.class);
             Bundle b = new Bundle();
             b.putInt("key", tot); //Your id
             intent.putExtras(b); //Put your id to your next Intent
             startActivity(intent);
            // finish();
             //Toast.makeText(MainActivity.this, amount, Toast.LENGTH_LONG).show();
         }

     }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.item1:
                Intent intent = new Intent(MainActivity.this, LoginN.class);
                startActivity(intent);
           // case R.id.item2:
              //  Toast.makeText(MainActivity.this,"Wohooo",Toast.LENGTH_LONG).show();


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



/*
       try {
            // Open your local db as the input stream
            InputStream myInput = getBaseContext().getAssets().open("Cantpay.db");

            // Path to the just created empty db
            String outFileName = "/data/data/com.creativeclan.canteenpaynfc/databases/Cantpay.db";

            OutputStream myOutput = new FileOutputStream(outFileName);

            // transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0)
            {
                myOutput.write(buffer, 0, length);
            }

            // Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();
        }
        catch (Exception e)
        {
            Log.e("error", e.toString());
        }
*/




    }
}
