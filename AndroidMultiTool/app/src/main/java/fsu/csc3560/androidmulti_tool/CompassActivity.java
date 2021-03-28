package fsu.csc3560.androidmulti_tool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.View;

public class CompassActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);

        drawerLayout = findViewById(R.id.drawer_layout);
    }

    public void clickMenu(View view)
    {
        MainActivity.toggleDrawer(drawerLayout);
    }
    public void clickBubble(View view)
    {
        //redirect to home
        MainActivity.switchActivity(this,MainActivity.class);
    }

    public void clickGForce(View view)
    {
        MainActivity.switchActivity(this,GForceActivity.class);
    }
    public void clickCompass(View view)
    {
        recreate();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        //close drawer
        drawerLayout.closeDrawer(GravityCompat.START);
    }


}