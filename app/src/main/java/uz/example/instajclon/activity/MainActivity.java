package uz.example.instajclon.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import uz.example.instajclon.R;
import uz.example.instajclon.adapter.ViewPagerAdapter;
import uz.example.instajclon.fragment.FavoriteFragment;
import uz.example.instajclon.fragment.HomeFragment;
import uz.example.instajclon.fragment.ProfileFragment;
import uz.example.instajclon.fragment.SearchFragment;
import uz.example.instajclon.fragment.UploadFragment;

/**
 * Contains view pager with 5 fragments in MainActivity,
 * and pages can be controlled by BottomNavigationView
 */
public class MainActivity extends BaseActivity implements HomeFragment.HomeListener,UploadFragment.UploadListener {
    String TAG = MainActivity.class.getSimpleName();
    ViewPager viewPager;
    BottomNavigationView bottomNavigationView; 
    int index = 0;

    HomeFragment homeFragment;
    UploadFragment uploadFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        viewPager = findViewById(R.id.viewPager);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_home : {
                        viewPager.setCurrentItem(0);
                        break;
                    }
                    case R.id.navigation_search : {
                        viewPager.setCurrentItem(1);
                        break;
                    }
                    case R.id.navigation_upload : {
                        viewPager.setCurrentItem(2);
                        break;
                    }
                    case R.id.navigation_favorite : {
                        viewPager.setCurrentItem(3);
                        break;
                    }
                    case R.id.navigation_profile : {
                        viewPager.setCurrentItem(4);
                        break;
                    }
                }
                return true;
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                index = position;
                bottomNavigationView.getMenu().getItem(index).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        homeFragment = new HomeFragment();
        uploadFragment = new UploadFragment();
        setupViewPager(viewPager);
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(homeFragment);
        adapter.addFragment(new SearchFragment());
        adapter.addFragment(uploadFragment);
        adapter.addFragment(new FavoriteFragment());
        adapter.addFragment(new ProfileFragment());
        viewPager.setAdapter(adapter);
    }

    @Override
    public void scrollToUpload() {
        index = 2;
        scrollByIndex(index);
    }

    @Override
    public void scrollToHome() {
        index = 0;
        scrollByIndex(index);
    }

    private void scrollByIndex(int index) {
        viewPager.setCurrentItem(index);
        bottomNavigationView.getMenu().getItem(index).setChecked(true);
    }
}