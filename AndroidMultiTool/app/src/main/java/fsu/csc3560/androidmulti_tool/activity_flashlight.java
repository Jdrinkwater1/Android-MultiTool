package fsu.csc3560.androidmulti_tool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;
import androidx.annotation.RequiresApi;
import static fsu.csc3560.androidmulti_tool.MainActivity.switchActivity;



public class activity_flashlight extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ToggleButton toggleFlashLightOnOff;
    private CameraManager cameraManager;
    private String getCameraID;
    private ImageView flashView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashlight);
        drawerLayout = findViewById(R.id.drawer_layout);
        flashView = findViewById((R.id.flashview));
        // Register the ToggleButton with specific ID
        toggleFlashLightOnOff = findViewById(R.id.toggle_flashlight);
        // cameraManager to interact with camera devices
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        // Exception is handled, because to check whether
        // the camera resource is being used by another
        // service or not.
        try {
            // O means back camera unit,
            // 1 means front camera unit
            getCameraID = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    // RequiresApi is set because, the devices which are
    // below API level 10 don't have the flash unit with
    // camera.
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void toggleFlashLight(View view) {
        if (toggleFlashLightOnOff.isChecked()) {
            // Exception is handled, because to check
            // whether the camera resource is being used by
            // another service or not.
            try {
                flashView.setImageResource(R.drawable.ic_baseline_flashlight_on_64);
                // true sets the torch in ON mode
                cameraManager.setTorchMode(getCameraID, true);

                // Inform the user about the flashlight
                // status using Toast message
                Toast.makeText(activity_flashlight.this, "Flashlight is turned ON", Toast.LENGTH_SHORT).show();
            } catch (CameraAccessException e) {
                // prints stack trace on standard error
                // output error stream
                e.printStackTrace();
            }
        } else {
            // Exception is handled, because to check
            // whether the camera resource is being used by
            // another service or not.
            try {
                flashView.setImageResource(R.drawable.ic_baseline_flashlight_off_64);
                // true sets the torch in OFF mode
                cameraManager.setTorchMode(getCameraID, false);

                // Inform the user about the flashlight
                // status using Toast message
                Toast.makeText(activity_flashlight.this, "Flashlight is turned OFF", Toast.LENGTH_SHORT).show();
            } catch (CameraAccessException e) {
                // prints stack trace on standard error
                // output error stream
                e.printStackTrace();
            }
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
        switchActivity(this,CompassActivity.class);
    }
    public void clickStop(View view)
    {
        switchActivity(this,activity_stopwatch.class);
    }
    public void clickFlash(View view)
    {
        recreate();
    }

    protected void onPause()
    {
        super.onPause();
        //close drawer
        drawerLayout.closeDrawer(GravityCompat.START);
    }
}