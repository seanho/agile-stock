package hk.reality.stock;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class BaseStockActivity extends ListActivity {
    public static final int DIALOG_ABOUT = 102;
    public static final int DIALOG_ERR_UNEXPECTED = 403;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.about:
            showDialog(DIALOG_ABOUT);
            return true;
        default:
        }
        return false;
    }
    

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DIALOG_ABOUT:
            AlertDialog aboutDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.about_title)
                .setMessage(R.string.msg_about)
                .setPositiveButton(R.string.ok_label, new OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(true)
                .create();
            return aboutDialog;
        case DIALOG_ERR_UNEXPECTED:
            AlertDialog unexpectedErrDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.msg_error_unexpect)
                .setMessage(R.string.msg_error_unexpect_details)
                .setPositiveButton(R.string.ok_label, new OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }                    
                })
                .setCancelable(true)
                .create();
            return unexpectedErrDialog;
        default:
        }
        return null;
    }
}
