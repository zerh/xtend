package xtend;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by eliezer on 10/29/17.
 */

public class Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Xtend.map(this, bundle);
    }
}