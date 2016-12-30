package customer.thewaiapp.com.payment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import customer.thewaiapp.com.R;

/**
 * Created by keviv on 02/09/2016.
 */
public class ExitAlertDialogFragment extends DialogFragment {

    public interface ExitOrderListener {
        void ExitOrder(Boolean exit);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        final ExitOrderListener listener = (ExitOrderListener) getActivity();
        View view=inflater.inflate(R.layout.fragment_exit_order_booking, null);
        builder.setMessage("Do you really want to cancel? All saved data will be lost");
//        builder.setView(view);

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.ExitOrder(false);
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.ExitOrder(true);
            }
        });

        Dialog dialog=builder.create();
        return dialog;
    }
    @Override
    public void onStart() {
        super.onStart();
        ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent));
        ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorAccent));
    }
}
