package io.zarda.moviesapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import io.zarda.moviesapp.R;
import io.zarda.moviesapp.fragments.DetailsFragment;
import io.zarda.moviesapp.fragments.MoviesFragment;

public class MainActivity extends AppCompatActivity {

    public static boolean TWO_PANE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.details_container) != null) {
            TWO_PANE = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.details_container, new DetailsFragment(), MoviesFragment.DETAILS_FRAGMENT_TAG)
                        .commit();
            }
        } else {
            TWO_PANE = false;
        }
    }

}
