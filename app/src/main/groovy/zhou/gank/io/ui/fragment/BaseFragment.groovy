package zhou.gank.io.ui.fragment

import android.app.Activity
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import groovy.transform.CompileStatic

@CompileStatic
class BaseFragment extends Fragment{

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

}