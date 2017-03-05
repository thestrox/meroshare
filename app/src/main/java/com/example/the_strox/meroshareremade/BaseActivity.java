package com.example.the_strox.meroshareremade;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class BaseActivity extends AppCompatActivity {

    protected DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    protected AppBarLayout appBarLayout;
    protected NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        appBarLayout =(AppBarLayout) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

       navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    // This method will trigger on item Click of navigation menu
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.navItem1:

                                mDrawerLayout.closeDrawers();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                Intent intent1 = new Intent(getApplicationContext(), HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent1);
                                    }
                                }, 200);

                                break;
                            case R.id.navItem2:
                                mDrawerLayout.closeDrawers();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                Intent intent2 = new Intent(getApplicationContext(), LiveActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent2);
                                    }
                                }, 200);

                                break;
                            case R.id.navItem3:
                                mDrawerLayout.closeDrawers();

                                if(Common.user==1) {
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                    Intent intent3 = new Intent(getApplicationContext(), PortfolioActivity_main.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivity(intent3);
                                        }
                                    }, 200);
                                }
                                else{
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                    Intent intent3 = new Intent(getApplicationContext(), PortfolioActivity_login.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivity(intent3);
                                        }
                                    }, 200);
                                }

                                break;
                            case R.id.navItem4:
                                mDrawerLayout.closeDrawers();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                Intent intent4 = new Intent(getApplicationContext(), CalculatorActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent4);
                                    }
                                }, 200);
                                break;
                            case R.id.navItem5:
                                mDrawerLayout.closeDrawers();
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                Intent intent5 = new Intent(getApplicationContext(), ListedActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent5);
                                            }
                                        }, 200);
                                break;
                        }
                        return false;
                    }
                });
    }

}
