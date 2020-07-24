package com.example.tishna;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
//import android.widget.AdapterView.OnClickListener()

import android.widget.TextView;
import android.content.Intent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;
public class bluetooth extends AppCompatActivity {
    Button btnPaired;
    ListView deviceList;
    BluetoothAdapter myBluetooth=BluetoothAdapter.getDefaultAdapter();
    Set <BluetoothDevice> pairedDevices=null;
    Bundle info;
    int daily_limit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        btnPaired=(Button)findViewById(R.id.button);
        deviceList=(ListView)findViewById(R.id.deviceList);

        info=getIntent().getExtras();
        daily_limit = info.getInt("daily_limit");


        if (myBluetooth==null){
            Toast.makeText(bluetooth.this, "Bluetooth Device Not Available", Toast.LENGTH_LONG).show();
        }
        else{
            if (myBluetooth.isEnabled()){

            }
            else{
                Intent turnOn= new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(turnOn,1);
            }
        }
    }



    public void paired(View view) {
        if (myBluetooth!=null &&myBluetooth.isEnabled()){
            pairedDevices=myBluetooth.getBondedDevices();}

        ArrayList list = new ArrayList();

        if(pairedDevices.size()>0){
            for( BluetoothDevice bt : pairedDevices){
                list.add(bt.getName()+"\n"+bt.getAddress());
            }
        }
        else{
            System.out.println("reached");
            Toast.makeText(this,"No Bluetooth Devices Found",Toast.LENGTH_SHORT).show();
        }
        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,list);
        deviceList.setAdapter(adapter);
        deviceList.setOnItemClickListener(myListClickListener);
    }

    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener()
    {
        public void onItemClick (AdapterView<?> av, View v, int arg2, long arg3)
        {
            // Get the device MAC address, the last 17 chars in the View
            String infos = ((TextView) v).getText().toString();
            String address = infos.substring(infos.length() - 17);
            // Make an intent to start next activity.

            info.putString("key",address);
            Intent i = new Intent(bluetooth.this, start.class);
            i.putExtras(info);
            //Change the activity.
            startActivity(i);

        }
    };



}
