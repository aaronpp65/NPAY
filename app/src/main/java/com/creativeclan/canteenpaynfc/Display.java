package com.creativeclan.canteenpaynfc;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

public class Display extends AppCompatActivity {
    Dbhelper mydb;
    Button checkout;


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
    private MyHandler mHandler;
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

        setContentView(R.layout.activity_display);
        mydb=new Dbhelper(this);
        final Button check = (Button)findViewById(R.id.checkout);
        check.setVisibility(View.INVISIBLE);

        Bundle b = getIntent().getExtras();
        int value = 0; // or other values
        if(b != null)
            value = b.getInt("key");


        String totval=Integer.toString(value);
        final int finvalue=value;
        Log.i("val",totval);
        TextView amt=(TextView) findViewById(R.id.total);
        amt.setText(totval);

        final TextView roll=(TextView)findViewById(R.id.roll);
        final TextView name=(TextView)findViewById(R.id.name);
        final TextView credit=(TextView)findViewById(R.id.credit);
        checkout=(Button)findViewById(R.id.button2);

        mHandler = new MyHandler(this);

        display = (TextView) findViewById(R.id.textView1);

        final String id=display.getText().toString();
//Extract it and add ' '



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
                    }
                }
            }
        });
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(Display.this,"Toast Message",Toast.LENGTH_SHORT).show();
                display.setText(display.getText().toString().replaceAll("(\\$*)(#*)", "").trim());

                String sid="'"+display.getText().toString().replaceAll("(\\$*)(#*)", "").trim()+"'";
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

                    int newcredit=Integer.parseInt(res.getString(3))-finvalue;

                    if(newcredit<=0){
                        new AlertDialog.Builder(Display.this)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("Oops!!")
                                .setMessage("It seems that your balance is Zero :(")
                                .setPositiveButton("Okay",null)
                                .show();
                    }
                    else {
                        String fintotval = Integer.toString(newcredit);
                        credit.setText(fintotval);
                    }
                }

                  check.setVisibility(View.VISIBLE);
            }
        });

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sid="'"+display.getText().toString().replaceAll("(\\$*)(#*)", "").trim()+"'";
                int fintot = Integer.parseInt(credit.getText().toString());

                   mydb.up(sid,fintot);
                Toast.makeText(Display.this,"Success",Toast.LENGTH_LONG).show();
               Intent intent = new Intent(Display.this,Bill.class);
              startActivity(intent);



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
        private final WeakReference<Display> mActivity;

        public MyHandler(Display activity) {
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
