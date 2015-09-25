package zhou.gank.io.ui.fragment

import android.app.Activity
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.KeyEvent
import android.view.View
import groovy.transform.CompileStatic
import zhou.gank.io.util.Notifier

@CompileStatic
class BaseFragment extends Fragment {

    Notifier notifier

    @Override
    void onAttach(Activity activity) {
        super.onAttach(activity)
        if (activity instanceof Notifier) {
            notifier = activity as Notifier
        }
    }

    protected void setSupportActionBar(Toolbar toolbar) {
        Activity activity = getActivity();
        if (activity instanceof AppCompatActivity) {
            AppCompatActivity appCompatActivity = activity as AppCompatActivity;
            appCompatActivity.setSupportActionBar(toolbar);
        }
    }

    protected ActionBar getSupportActionBar() {
        Activity activity = getActivity();
        if (activity instanceof AppCompatActivity) {
            AppCompatActivity appCompatActivity = activity as AppCompatActivity;
            return appCompatActivity.getSupportActionBar();
        }
        return null;
    }

    protected void noticeActivity(int noticeId) {
        notifier?.notice(noticeId)
    }

    protected void initView(View v) {

    }

    boolean onKeyDown(int keyCode, KeyEvent event) {
        return false
    }
}