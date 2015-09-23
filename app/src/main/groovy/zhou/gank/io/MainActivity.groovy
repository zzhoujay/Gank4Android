package zhou.gank.io

import android.content.Intent
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v7.app.AppCompatActivity
import android.view.View
import groovy.transform.CompileStatic
import zhou.gank.io.ui.activity.HomeActivity
import zhou.gank.io.util.NetworkKit

@CompileStatic
class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main);

//        NetworkKit.time(2015, 9, 23, { println(it) })

        NetworkKit.random("Android", 10, { println(it) })

//        NetworkKit.type("iOS", 10, 2, { println(it) })
    }

    public void Open(View view) {
        startActivity(new Intent(this, HomeActivity.class));
    }
}