package fsu.csc3560.androidmulti_tool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);


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

    @Override
    protected void onPause()
    {
        super.onPause();
        //close drawer
        drawerLayout.closeDrawer(GravityCompat.START);
    }









}