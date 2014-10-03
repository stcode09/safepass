package utilities.safepassbeta;

import android.app.ProgressDialog;
import android.content.Context;

import java.util.concurrent.Callable;

public class LoadingDialog {

    public static void show(Context context, final Callable<Integer> myFunction, String title, String msg) {
        final ProgressDialog ringProgressDialog = ProgressDialog.show(context, title, msg, true);
        ringProgressDialog.setCancelable(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    myFunction.call();
                } catch (Exception e) {
                    System.out.println("Caught exception: " + e);
                }
                ringProgressDialog.dismiss();
            }
        }).start();
    }

}
