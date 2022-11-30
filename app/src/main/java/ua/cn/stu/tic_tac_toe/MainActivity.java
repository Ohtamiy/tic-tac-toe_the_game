package ua.cn.stu.tic_tac_toe;

import ua.cn.stu.tic_tac_toe.fragments.GameFragment;
import ua.cn.stu.tic_tac_toe.fragments.LoginFragment;
import ua.cn.stu.tic_tac_toe.fragments.SplashFragment;
import ua.cn.stu.tic_tac_toe.navigator.Navigator;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements Navigator {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            SplashFragment splashFragment = new SplashFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .add(R.id.fragmentContainer, splashFragment)
                    .commit();
        }
    }

    @Override
    public void launchLoginFragment() {
        LoginFragment loginFragment = LoginFragment.newInstance();
        getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, loginFragment)
                    .commit();
    }

    @Override
    public void launchGameFragment(String value) {
        GameFragment fragment = GameFragment.newInstance(value);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void back() {
        onBackPressed();
    }
}