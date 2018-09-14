package fantansee.cn.ling.yu.sdk.listener;

import android.os.Handler;

import fantansee.cn.ling.yu.sdk.bean.NfcCardValueBean;

/**
 * nfc 卡片
 *
 * @author Yu L.
 */
public interface NfcCardValueInterface {

    void postNfcObj(NfcCardValueBean valueBean);

    void postCardObj(NfcCardValueBean valueBean);

    void postUidService(Handler handler, String sn);
}
