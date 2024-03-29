package fsu.csc3560.androidmulti_tool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import static fsu.csc3560.androidmulti_tool.MainActivity.switchActivity;

public class CompassActivity extends AppCompatActivity implements SensorEventListener {
    DrawerLayout drawerLayout;
    private SensorManager sensorManager;
    private Sensor sensor;
    private Sensor rotation,accel,magnetmeter;
    private ImageView compassImage;
    private TextView degrees;
    int direction;
    float[] rMat = new float[9];
    float[] orientation = new float[9];
    private float[] lastAccel = new float[3];
    private float[] lastMag  = new float[3];
    private boolean haveSensor = false, haveSensor2 = false;
    private boolean lastAccelset = false;
    private boolean lastMagSet = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        drawerLayout = findViewById(R.id.drawer_layout);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        compassImage = (ImageView) findViewById(R.id.compass);
        degrees = (TextView) findViewById(R.id.degrees);

        start();
    }
    //startup the sensors needed and check the sensors that we're going to use
    private void start()
    {
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)==null)
        {
            //if the user doesnt have this sensor throw the dialogue box warning
            if (sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)==null || sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)==null)
            {
                noSensorWarning();
            }
            else
            {
                accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                accel = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
                haveSensor = sensorManager.registerListener(this,accel,SensorManager.SENSOR_DELAY_UI);
                haveSensor = sensorManager.registerListener(this,magnetmeter,SensorManager.SENSOR_DELAY_UI);
            }
        }
        else
        {
            rotation = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
            haveSensor=sensorManager.registerListener(this,rotation,SensorManager.SENSOR_DELAY_UI);
        }

    }
    public void ClickMenu(View view)
    {
        MainActivity.toggleDrawer(drawerLayout);
    }
    public void ClickBubble(View view)
    {
        //redirect to home
        switchActivity(this,MainActivity.class);
    }

    public void clickCompass(View view)
    {
        recreate();
    }
    public void noSensorWarning()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Your Device does not support this.");
        alert.setCancelable(false);
        alert.setNegativeButton("Close",null);
    }
    public void clickFlash(View view)
    {
        switchActivity(this,activity_flashlight.class);
    }
    public void clickStop(View view)
    {
        switchActivity(this,activity_stopwatch.class);
    }
    //stop the sensors
    public void stop()
    {
        if(haveSensor && haveSensor2)
        {
            sensorManager.unregisterListener(this,accel);
            sensorManager.unregisterListener(this,magnetmeter);
        }
        else
        {
            if(haveSensor)
            {
                sensorManager.unregisterListener(this,rotation);
            }
        }
    }
    //everytime the sensors data changes run this, if the either sensor changes it will calculate the degrees using either the rotation vector accelerometer, or the magnetic field and accelerometer
    public void onSensorChanged(SensorEvent event)
    {
        if(event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR)
        {
            SensorManager.getRotationMatrixFromVector(rMat,event.values);
            direction = (int) ((Math.toDegrees(SensorManager.getOrientation(rMat,orientation)[0])+360)%360);    //index 0 is the direction will return direction in degrees 0-360
        }
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            System.arraycopy(event.values,0,lastAccel,0,event.values.length);
            lastAccelset=true;
        }
        else
        if(event.sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD)
        {
            System.arraycopy(event.values,0,lastMag,0,event.values.length);
            lastMagSet=true;
        }
        if(lastMagSet && lastAccelset)
        {
            SensorManager.getRotationMatrix(rMat,null,lastAccel,lastMag);
            SensorManager.getOrientation(rMat,orientation);
            direction = (int) ((Math.toDegrees(SensorManager.getOrientation(rMat,orientation)[0])+360)%360);    //index 0 is the direction will return direction in degrees 0-360
        }
        direction = Math.round(direction);
        compassImage.setRotation(-direction);

        degrees.setText(direction+"°");

    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    //on pause stop the sensors
    @Override
    protected void onPause()
    {
        super.onPause();
        //stop sensors
        stop();
        //close drawer
        drawerLayout.closeDrawer(GravityCompat.START);
    }
    protected void onResume()
    {
        super.onResume();
        start();
    }


}