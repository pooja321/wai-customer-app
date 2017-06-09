package customer.thewaiapp.com.Utility;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import java.util.Objects;

/**
 * Created by DELL on 4/14/2017.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        final String status = NetworkUtil.getConnectivityStatusString(context);
        if (Objects.equals(status, "Internet connection not available")) {
//            Intent intentone = new Intent(context.getApplicationContext(), InternetCheck.class);
//            intentone.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intentone);
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("No Internet Connection")
                    .setMessage("It looks like your internet connection is off. Please turn it " +
                            "on and try again")
                    .setCancelable(false)
                    .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String value = NetworkUtil.getConnectivityStatusString(context);
                            if (Objects.equals(value, "Internet connection not available")) {
                                builder.show();
                            }
                            else {
                                dialog.dismiss();
                            }

                        }
                    }).setIcon(android.R.drawable.ic_dialog_alert).show();

        }

    }
}
