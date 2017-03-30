package kg.app.kuba.homework_maptopoint;

import android.app.Activity;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;

/**
 * Created by admin on 30.03.2017.
 */

public class ToastUtility {

    private Activity activity;

    public ToastUtility(Activity activity){this.activity = activity;}

    public static void showShortMessega(MapsActivity activity, String messega){
        Toast.makeText(activity, messega, Toast.LENGTH_LONG).show();
    }
    public static void showLongMessega(Activity activity, String messega){
        Toast.makeText(activity, messega, Toast.LENGTH_SHORT).show();
    }
}
