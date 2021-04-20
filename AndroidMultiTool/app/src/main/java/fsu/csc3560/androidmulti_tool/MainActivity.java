package fsu.csc3560.androidmulti_tool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.animation.ObjectAnimator;
import android.app.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.hardware.Sensor;

import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sensor;
    private Sensor orientation,rotation;
    private TextView pitchDegrees,rollDegrees;
    private ImageView level,pitchBubble;
    private float[] orientationArr = new float[3];
    private ObjectAnimator animation1,animation2;
    float[] rotationMatrix = new float[9];
    double pitchOrient,rollOrient;
    private boolean haveSensor = false, haveSensor2 = false;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        drawerLayout = findViewById(R.id.drawer_layout);
        pitchDegrees = findViewById(R.id.pitchView);
        rollDegrees = findViewById(R.id.rollView);
        level = findViewById(R.id.rollBubble);
        pitchBubble = findViewById(R.id.pitchBubble);
        start();
    }

    private void start()
    {
        if(sensorManager.getDefaultSensor(sensor.TYPE_ROTATION_VECTOR)!= null)
        {
            rotation = sensorManager.getDefaultSensor(sensor.TYPE_ROTATION_VECTOR);
            haveSensor=sensorManager.registerListener(this,rotation,SensorManager.SENSOR_DELAY_UI);
        }
        else
        {
            noSensorWarning();
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


    public void noSensorWarning()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Your Device does not support this.");
        alert.setCancelable(false);
        alert.setNegativeButton("Close",null);
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

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        if(event.sensor.getType()==Sensor.TYPE_ROTATION_VECTOR)
        {
            SensorManager.getRotationMatrixFromVector(rotationMatrix,event.values);
            SensorManager.getOrientation(rotationMatrix,orientationArr);
            pitchOrient = Math.toDegrees(orientationArr[1]);
            pitchOrient = Math.round(pitchOrient);
            rollOrient = Math.toDegrees(orientationArr[2]);
            rollOrient = Math.round(rollOrient);
            if(rollOrient > 62)
            {
                animation1 = ObjectAnimator.ofFloat(level,"translationX", (float) 186);
            }
            else if(rollOrient < -68 )
            {
                animation1 = ObjectAnimator.ofFloat(level,"translationX", (float) -210);
            }
            else
                {
                animation1 = ObjectAnimator.ofFloat(level, "translationX", (float) rollOrient * 3);
            }
            animation1.setDuration(0);
            animation1.start();
            if(pitchOrient <= -69)
            {
                animation2 = ObjectAnimator.ofFloat(pitchBubble,"translationY", (float) -207);
            }
            else if(pitchOrient>=77)
            {
                animation2 = ObjectAnimator.ofFloat(pitchBubble,"translationY", (float) 231);
            }
            else
            {
                animation2 = ObjectAnimator.ofFloat(pitchBubble,"translationY", (float) pitchOrient*3);
            }

            animation2.setDuration(0);
            animation2.start();
            pitchDegrees.setText("Pitch:"+pitchOrient+"°");
            rollDegrees.setText("Roll:"+rollOrient+"°");
        }
    }
    public void stop()
    {
        if(haveSensor)
        {
            sensorManager.unregisterListener(this,rotation);
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}