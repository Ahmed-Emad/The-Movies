package io.zarda.moviesapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import io.zarda.moviesapp.R;
import io.zarda.moviesapp.fragments.DetailsFragment;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if (savedInstanceState == null) {
            DetailsFragment fragment = new DetailsFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.details_container, fragment)
                    .commit();
            getSupportActionBar().setTitle("Movie Details");
        }
    }

}
