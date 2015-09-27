package zhou.gank.io

import android.content.Intent
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v7.app.AppCompatActivity
import android.view.View
import groovy.transform.CompileStatic
import zhou.gank.io.ui.activity.HomeActivity
import zhou.gank.io.ui.activity.TabActivity

@CompileStatic
class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        App.setTheme(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main);
    }

    public void Open(View view) {
        startActivity(new Intent(this, HomeActivity.class));
    }
}