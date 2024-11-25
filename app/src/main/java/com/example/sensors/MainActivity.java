package com.example.sensors;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    SensorManager sensorManager;
    private static final int POLL_INTERVAL = 500;
    private Handler hdr = new Handler();
    private PowerManager.WakeLock wl;
    SensorInfo sensor_info = new SensorInfo();
    Boolean shown_dialog = false;
    private static final int shake_threshold = 15;

    private final Runnable pollTask = new Runnable() {
        public void run() {
            showDialog();
            hdr.postDelayed(pollTask, POLL_INTERVAL);
        }
    };

    @SuppressLint("InvalidWakeLockTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "Sensors Info");

        Glide.with(this)
                .asGif()
                .load(R.drawable.cd)
                .into((ImageView) findViewById(R.id.gifImageView));
    }//end onCreate

    public void onAccuracyChanged(Sensor sensor, int accuracy){
        // TO DO
    }//end onAccuracyChanged

    public void onSensorChanged(SensorEvent event){
        int type = event.sensor.getType();

        if (type == Sensor.TYPE_ACCELEROMETER) {
            sensor_info.accX=event.values[0];
            sensor_info.accY=event.values[1];
            sensor_info.accZ=event.values[2];
        }
        if (type == Sensor.TYPE_GRAVITY) {
            sensor_info.graX=event.values[0];
            sensor_info.graY=event.values[1];
            sensor_info.graZ=event.values[2];
        }
        if (type == Sensor.TYPE_GYROSCOPE) {
            sensor_info.gyrX=event.values[0];
            sensor_info.gyrY=event.values[1];
            sensor_info.gyrZ=event.values[2];
        }
        if (type == Sensor.TYPE_LIGHT) {
            sensor_info.light=event.values[0];
        }
        if (type == Sensor.TYPE_LINEAR_ACCELERATION) {
            sensor_info.laccX=event.values[0];
            sensor_info.laccY=event.values[1];
            sensor_info.laccZ=event.values[2];
        }
        if (type == Sensor.TYPE_MAGNETIC_FIELD) {
            sensor_info.magX=event.values[0];
            sensor_info.magY=event.values[1];
            sensor_info.magZ=event.values[2];
        }
        if (type == Sensor.TYPE_ORIENTATION) {
            sensor_info.orX=event.values[0];
            sensor_info.orY=event.values[1];
            sensor_info.orZ=event.values[2];
        }
        if (type == Sensor.TYPE_PROXIMITY) {
            sensor_info.proximity=event.values[0];
        }
        if (type == Sensor.TYPE_ROTATION_VECTOR) {
            sensor_info.rotX=event.values[0];
            sensor_info.rotY=event.values[1];
            sensor_info.rotZ=event.values[2];
        }
    }//end onSensorChanged

    public void showDialog() {
        if ((Math.abs(sensor_info.accX) > shake_threshold) ||
                (Math.abs(sensor_info.accY) > shake_threshold) ||
                (Math.abs(sensor_info.accZ) > shake_threshold)) {
            if (!shown_dialog) {
                shown_dialog = true;
                final AlertDialog.Builder viewDialog = new AlertDialog.Builder(this);
                viewDialog.setIcon(android.R.drawable.btn_star_big_on);
                viewDialog.setTitle("คำทำนายเซียมซี");

                // Generate random number between 1 and 28
                Random random = new Random();
                int randomNumber = random.nextInt(28) + 1;

                // Get the corresponding message from resources
                String[] messages = getResources().getStringArray(R.array.random_messages);
                String message = messages[randomNumber - 1];

                viewDialog.setMessage("ใบที่ " + randomNumber + "\n\n" + message);
                viewDialog.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                shown_dialog = false;
                            }
                        });
                viewDialog.show();
            }
        }//end if
    }//end showDialog

    private final BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            int health= intent.getIntExtra(BatteryManager.EXTRA_HEALTH,0);
            String health_text = (health == 2) ? "GOOD" : "NOT GOOD";
            int level= intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0);
            int plugged= intent.getIntExtra(BatteryManager.EXTRA_PLUGGED,0);
            String plugged_text = (plugged == 2) ? "USB" : (plugged == 1) ? "AC" : "UNPLUGGED";
            boolean present= intent.getExtras().getBoolean(BatteryManager.EXTRA_PRESENT);
            int scale= intent.getIntExtra(BatteryManager.EXTRA_SCALE,0);
            int status= intent.getIntExtra(BatteryManager.EXTRA_STATUS,0);
            String status_text = (status == 3) ? "DISCHARGING" : (status == 2) ? "CHARGING" : "FULL";
            String technology= intent.getExtras().getString(BatteryManager.EXTRA_TECHNOLOGY);
            int temperature= intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,0);
            int voltage= intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE,0);
        }//end onReceive
    };

    @SuppressLint("WakelockTimeout")
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE),
                SensorManager.SENSOR_DELAY_NORMAL);

        this.registerReceiver(this.batteryInfoReceiver,	new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        if (!wl.isHeld()) {
            wl.acquire();
        }
        hdr.postDelayed(pollTask, POLL_INTERVAL);
    }//end onResume

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);

        this.unregisterReceiver(this.batteryInfoReceiver);

        if (wl.isHeld()) {
            wl.release();
        }
        hdr.removeCallbacks(pollTask);
    }//end onPause

    static class SensorInfo{
        float accX, accY, accZ;
        float graX, graY, graZ;
        float gyrX, gyrY, gyrZ;
        float light;
        float laccX, laccY, laccZ;
        float magX, magY, magZ;
        float orX, orY, orZ;
        float proximity;
        float rotX, rotY, rotZ;
    }//end class SensorInfo
}//end MainActivity