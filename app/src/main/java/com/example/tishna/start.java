package com.example.tishna;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.UUID;


public class start extends AppCompatActivity {
    Bundle info;
    String address = null;
    int daily_limit;
    ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket=null;
    private boolean isBtConnected = false;
    int stop=0;
    Button connect,disconnect,start,end;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    TextView used;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        info = getIntent().getExtras();
        address=info.getString("key");
        daily_limit=info.getInt("daily_limit");
        System.out.println(daily_limit);


        connect=(Button)findViewById(R.id.connect);
        disconnect=(Button)findViewById(R.id.disconnect);
        start=(Button)findViewById(R.id.start);
        end=(Button)findViewById(R.id.end);
        used=(TextView)findViewById(R.id.used);

        new task().execute();

    }

    public class task extends AsyncTask<Void,Void,Void> {

        private boolean ConnectSuccess=true;
        task() {

        }
        @Override
        protected void onPreExecute(){
            progress= ProgressDialog.show(start.this,"Connecting","Plase Wait !!!");
        }


        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {

                if (btSocket == null || !isBtConnected)
                {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    System.out.println(address);
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);
                    System.out.println("reached");//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            }
            else
            {
                msg("Connected.");
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }



    public void connect(View view) {
        Intent intent = new Intent(start.this,bluetooth.class);
        startActivity(intent);

    }

    public void disconnect(View view) {
        if (btSocket!=null) //If the btSocket is busy
        {
            try
            {
                btSocket.close(); //close connection
            }
            catch (IOException e)
            { msg("Error");}
        }
        finish(); //return to the first layout




    }
    public class downloading extends AsyncTask<Void, Void,Integer> {

        downloading(){

        }
        @Override
        protected Integer doInBackground(Void... voids) {
            double val = 0.0;
            try {

                if (btSocket != null) {

                    stop = 0;
                    btSocket.getOutputStream().write("W".toString().getBytes());
                    InputStream inputStream = btSocket.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String s = null;
                    Double d = Double.valueOf(daily_limit);
                    d = d * 1000;


                        while (val < d && stop == 0) {
                            s = reader.readLine();
                            val = Float.parseFloat(s);
                            System.out.println(val);
                        }
                        reader.close();
                        inputStream.close();





                }


            } catch (IOException e) {
                e.printStackTrace();
            }


            int val1 = (int) val;
            return val1;
        }
        @Override
        protected void onPostExecute(Integer d) {
            super.onPostExecute(d);
            System.out.println(d);
            used.setText(String.valueOf(d));
            daily_limit=daily_limit-(d/1000);
            try {
                btSocket.getOutputStream().write("O".toString().getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    public void start(View view) {
        new downloading().execute();
    }

    public void end(View view) {
        stop = 1;

    }
    public void reset(View view) {
        try {
            btSocket.getOutputStream().write("S".toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void bathing(View view) {
        try {
            btSocket.getOutputStream().write("B".toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }
}
