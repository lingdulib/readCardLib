package fantansee.cn.ling.yu.sdk.utils;

import android.text.TextUtils;

/**
 * 字节转换
 *
 * @author Yu L.
 */
public class CStringUtil {

    public static String getReverUidString(String uid) {
        StringBuilder builder = new StringBuilder();
        for (int i = uid.length(); i >= 0; i -= 2) {
            if (i - 2 >= 0) {
                String _code = uid.substring((i - 2), i);
                builder.append(_code);
            }
        }
        return builder.toString();
    }

    public static String getUidString(String result) {
        if (TextUtils.getTrimmedLength(result) >= 12) {
            result = result.substring(4, 12);
        }
        return result;
    }

    public static String getELastString(String result) {

        if (TextUtils.isEmpty(result)) {
            result = "";
        } else {
            int _dot = result.indexOf("E");
            if (_dot != -1) {
                result = result.substring(_dot + 1);
            }
        }
        return result;
    }

    public static String[] getDotString(String result) {
        if (TextUtils.isEmpty(result))
            return new String[]{};
        return result.split(",");
    }
}
