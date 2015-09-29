package zhou.gank.io.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertController
import android.support.v7.app.AlertDialog
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.widget.TextView;
import groovy.transform.CompileStatic
import zhou.gank.io.R
import zhou.gank.io.comment.Config

@CompileStatic
public class InfoDialog extends DialogFragment {

    String title
    CharSequence content

    @Override
    void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        Bundle b = getArguments()
        title = b?.getString(Config.Static.TITLE)
        content = b?.getCharSequence(Config.Static.CONTENT)
    }

    @Override
    Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
        TextView tv = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_text, null) as TextView
        tv.setText(content)
        tv.setMovementMethod(LinkMovementMethod.getInstance())
        builder.setTitle(title).setView(tv).setPositiveButton(R.string.confirm, null)
        AlertDialog dialog = builder.create()
        return dialog
    }

    static InfoDialog newInstance(String title, CharSequence content) {
        InfoDialog dialog = new InfoDialog()
        Bundle bundle = new Bundle()
        bundle.putString(Config.Static.TITLE, title)
        bundle.putCharSequence(Config.Static.CONTENT, content)
        dialog.setArguments(bundle)
        return dialog
    }
}