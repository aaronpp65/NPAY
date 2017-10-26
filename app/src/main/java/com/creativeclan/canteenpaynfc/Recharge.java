package com.creativeclan.canteenpaynfc;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.Set;

public class Recharge extends AppCompatActivity {
   Dbhelper mydb;
    int oldCredit;
    /*
     * Notifications from UsbService will be received here.
     */
    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case UsbService.ACTION_USB_PERMISSION_GRANTED: // USB PERMISSION GRANTED
                    Toast.makeText(context, "USB Ready", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_PERMISSION_NOT_GRANTED: // USB PERMISSION NOT GRANTED
                    Toast.makeText(context, "USB Permission not granted", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_NO_USB: // NO USB CONNECTED
                    Toast.makeText(context, "No USB connected", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_DISCONNECTED: // USB DISCONNECTED
                    Toast.makeText(context, "USB disconnected", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_NOT_SUPPORTED: // USB NOT SUPPORTED
                    Toast.makeText(context, "USB device not supported", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    private UsbService usbService;
    private TextView display;
    private EditText editText;
    private Recharge.MyHandler mHandler;
    private final ServiceConnection usbConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName arg0, IBinder arg1) {
            usbService = ((UsbService.UsbBinder) arg1).getService();
            usbService.setHandler(mHandler);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            usbService = null;
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);

        mydb=new Dbhelper(this);

        Button next = (Button) findViewById(R.id.next);
        final Button recharge = (Button) findViewById(R.id.recharge);
        final TextView roll=(TextView)findViewById(R.id.roll);
        final TextView name=(TextView)findViewById(R.id.name);
        final TextView credit=(TextView)findViewById(R.id.credit);
        final EditText newCredit = (EditText) findViewById(R.id.newCredit);

        mHandler = new Recharge.MyHandler(this);
        display = (TextView) findViewById(R.id.textView1);

        final String id=display.getText().toString();

        recharge.setVisibility(View.INVISIBLE);
        newCredit.setVisibility(View.INVISIBLE);

        editText = (EditText) findViewById(R.id.editText1);
        Button sendButton = (Button) findViewById(R.id.buttonSend);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editText.getText().toString().equals("")) {
                    String data = editText.getText().toString();
                    if (usbService != null) { // if UsbService was correctly binded, Send data
                        display.append(data);
                        usbService.write(data.getBytes());
                      //  Toast.makeText(Recharge.this,"Toast Message",Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });

      /*  if(display.getText().toString()!=null) {
            final Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {

                    //Toast.makeText(Display.this,"Toast Message",Toast.LENGTH_SHORT).show();
                    display.setText(display.getText().toString().replaceAll("(\\$*)(#*)", "").trim());
                    String sid="'"+display.getText().toString().replaceAll("(\\$*)(#*)", "").trim()+"'";
                 //   String sid = "'0429CCDA0D2980'";
                    Cursor res = mydb.getdata(sid);

                    if (res.getCount() == 0) {
                        showmsg("Error", "Opps,its seems you are not in our databse.Are you sure you study here??");
                        return;
                    }

                    StringBuffer buffer = new StringBuffer();

                    while (res.moveToNext()) {
                        buffer.append("Id:" + res.getString(0) + "\n");
                        buffer.append("roll:" + res.getString(1) + "\n");
                        buffer.append("name:" + res.getString(2) + "\n");
                        buffer.append("credit:" + res.getString(3) + "\n");

                        display.setText(res.getString(0));
                        roll.setText(res.getString(1));
                        name.setText(res.getString(2));
                        credit.setText(res.getString(3));

                        oldCredit = Integer.parseInt(res.getString(3));

                    }

                    recharge.setVisibility(View.VISIBLE);
                    newCredit.setVisibility(View.VISIBLE);


                    handler.postDelayed(this, 1000);
                }
            };
            handler.post(runnable);
        }*/

       next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(Display.this,"Toast Message",Toast.LENGTH_SHORT).show();
                display.setText(display.getText().toString().replaceAll("(\\$*)(#*)", "").trim());

                String sid="'"+display.getText().toString().replaceAll("(\\$*)(#*)", "").trim()+"'";

             //  String sid="'0429CCDA0D2980'";
                Cursor res = mydb.getdata(sid);

                if(res.getCount()==0)
                {
                    showmsg("Error","Opps,its seems you are not in our databse.Are you sure you study here??");
                    return;
                }

                StringBuffer buffer = new StringBuffer();

                while (res.moveToNext())
                {
                    buffer.append("Id:"+res.getString(0)+"\n");
                    buffer.append("roll:"+res.getString(1)+"\n");
                    buffer.append("name:"+res.getString(2)+"\n");
                    buffer.append("credit:"+res.getString(3)+"\n");

                    display.setText(res.getString(0));
                    roll.setText(res.getString(1));
                    name.setText(res.getString(2));
                    credit.setText(res.getString(3));

                     oldCredit= Integer.parseInt(res.getString(3));

                }

                recharge.setVisibility(View.VISIBLE);
                newCredit.setVisibility(View.VISIBLE);
            }
        });

        recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newcredit = Integer.parseInt(newCredit.getText().toString());

               // String sid="'0429CCDA0D2980'";
                String sid="'"+display.getText().toString().replaceAll("(\\$*)(#*)", "").trim()+"'";
                mydb.up(sid,newcredit+oldCredit);
                Toast.makeText(Recharge.this,"Success",Toast.LENGTH_LONG).show();
            }
        });

    }
    public void showmsg(String title,String msg)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.show();
    }
    @Override
    public void onResume() {
        super.onResume();
        setFilters();  // Start listening notifications from UsbService
        startService(UsbService.class, usbConnection, null); // Start UsbService(if it was not started before) and Bind it
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(mUsbReceiver);
        unbindService(usbConnection);
    }

   /* @Override
    public void onBackPressed(){
        Intent intent = new Intent(Recharge.this,MainActivity.class);
        startActivity(intent);
    }*/

    private void startService(Class<?> service, ServiceConnection serviceConnection, Bundle extras) {
        if (!UsbService.SERVICE_CONNECTED) {
            Intent startService = new Intent(this, service);
            if (extras != null && !extras.isEmpty()) {
                Set<String> keys = extras.keySet();
                for (String key : keys) {
                    String extra = extras.getString(key);
                    startService.putExtra(key, extra);
                }
            }
            startService(startService);
        }
        Intent bindingIntent = new Intent(this, service);
        bindService(bindingIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void setFilters() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbService.ACTION_USB_PERMISSION_GRANTED);
        filter.addAction(UsbService.ACTION_NO_USB);
        filter.addAction(UsbService.ACTION_USB_DISCONNECTED);
        filter.addAction(UsbService.ACTION_USB_NOT_SUPPORTED);
        filter.addAction(UsbService.ACTION_USB_PERMISSION_NOT_GRANTED);
        registerReceiver(mUsbReceiver, filter);
    }

    /*
     * This handler will be passed to UsbService. Data received from serial port is displayed through this handler
     */
    private static class MyHandler extends Handler {
        private final WeakReference<Recharge> mActivity;

        public MyHandler(Recharge activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UsbService.MESSAGE_FROM_SERIAL_PORT:
                    String data = (String) msg.obj;
                    mActivity.get().display.append(data);
                    break;
            }
        }
    }

}
