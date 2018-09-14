package fantansee.cn.ling.yu.sdk.utils;

import android.app.AlertDialog;
import android.os.Build;

/**
 * @author Yu L.
 */
public class ThemeUtils {

    private ThemeUtils() {

    }

    public static int getDialogTheme() {
        int theme;
        if (Build.VERSION.SDK_INT >= 21) {
            theme = android.R.style.Theme_Material_Light_Dialog_Alert;
        } else {
            theme = AlertDialog.THEME_HOLO_LIGHT;
        }
        return theme;
    }

}
