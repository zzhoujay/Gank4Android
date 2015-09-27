package zhou.gank.io.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.design.widget.CoordinatorLayout
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.fourmob.datetimepicker.date.DatePickerDialog
import groovy.transform.CompileStatic
import zhou.gank.io.App
import zhou.gank.io.R
import zhou.gank.io.comment.Config
import zhou.gank.io.ui.fragment.DailyFragment
import zhou.gank.io.util.Notifier
import zhou.gank.io.util.TimeKit

@CompileStatic
public class DailyActivity extends AppCompatActivity implements Notifier {

    CoordinatorLayout coordinatorLayout;
    Fragment currFragment
    int year, month, day

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        App.setTheme(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compat)

        def i = getIntent()
        def times = TimeKit.getTime()
        year = i.getIntExtra(Config.Static.YEAR, times[0])
        month = i.getIntExtra(Config.Static.MONTH, times[1])
        day = i.getIntExtra(Config.Static.DAY, times[2])

        add(DailyFragment.newInstance(year, month, day))
    }

    def add(Fragment f) {
        getSupportFragmentManager().beginTransaction().add(R.id.container, f).commit();
        this.currFragment = f;
    }

    def replace(Fragment f) {
        if (currFragment == f) {
            return
        }
        coordinatorLayout.removeAllViews()
        getSupportFragmentManager().beginTransaction().remove(currFragment).add(R.id.container, f).commit()
        this.currFragment = f
    }

    @Override
    void notice(int noticeId) {
        switch (noticeId) {
            case Config.Action.FINISH:
                finish()
                break
            case Config.Action.CHANGE_DATE:
                DatePickerDialog dialog = DatePickerDialog.newInstance({ datePickerDialog, year, month, day ->
                    Intent i = new Intent(this, DailyActivity.class)
                    i.putExtra(Config.Static.YEAR, year as int)
                    i.putExtra(Config.Static.MONTH, (month as int) + 1)
                    i.putExtra(Config.Static.DAY, day as int)
                    startActivity(i)
                }, year, month - 1, day)
                dialog.setVibrate(false)
                dialog.show(getSupportFragmentManager(), "time")
//                DatePickerDialog dialog = new DatePickerDialog(this, { picker, year, month, day ->
//                    Intent i = new Intent(this, DailyActivity.class)
//                    i.putExtra(Config.Static.YEAR, year as int)
//                    i.putExtra(Config.Static.MONTH, (month as int) + 1)
//                    i.putExtra(Config.Static.DAY, day as int)
//                    startActivity(i)
//                }, year, month - 1, day)
//                dialog.show()
//                DatePickerDialog dialog = DatePickerDialog.newInstance(this, times[0], times[1] - 1, times[2])
//                dialog.vibrate(false)
//                dialog.show(getFragmentManager(), "date")
                break
        }
    }
}