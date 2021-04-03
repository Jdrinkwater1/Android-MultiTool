package fsu.csc3560.androidmulti_tool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.Matrix;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
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

    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        compassImage = (ImageView) findViewById(R.id.compass);
        degrees = (TextView) findViewById(R.id.degrees);
        start();
    }

    private void start()
    {
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)==null)
        {
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

    //the follow menu opens the menu on click
    public void ClickMenu(View view)
    {
        toggleDrawer(drawerLayout);
    }

    public static void toggleDrawer(DrawerLayout drawerLayout)
    {
        //if drawer is open close it
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        //else open the drawer
        else
            drawerLayout.openDrawer(GravityCompat.START);
    }

    public void ClickBubble(View view)
            //recreate activity?
    {
        recreate();
    }

    public void clickCompass(View view)
    {
        switchActivity(this,CompassActivity.class);
    }


    public void clickGForce(View view)
    {
        switchActivity(this,GForceActivity.class);
    }



    public static void switchActivity(Activity activity,Class aClass)
            //make intent
    {
        Intent intent = new Intent(activity,aClass);
        //set flags not sure what this does 3/28
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //start activity
        activity.startActivity(intent);
    }

    public void noSensorWarning()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Your Device does not support this.");
        alert.setCancelable(false);
        alert.setNegativeButton("Close",null);
    }

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
    @Override
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

        String where = "NO";
        if(direction >= 350 || direction<=10)
            where = "N";
        if(direction < 350 || direction>280)
            where = "NW";
        if(direction <= 280 || direction>260)
            where = "W";
        if(direction <= 260 || direction>190)
            where = "SW";
        if(direction <= 190 || direction>170)
            where = "S";
        if(direction <= 170 || direction>100)
            where = "SE";
        if(direction <= 100 || direction>80)
            where = "E";
        if(direction <= 80|| direction>10)
            where = "NE";

        degrees.setText(direction+"°"+where);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

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