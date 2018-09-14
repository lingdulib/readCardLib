package fantansee.cn.ling.yu.sdk.model;

import android.os.Handler;

import fantansee.cn.ling.yu.sdk.listener.NfcCardValueInterface;

/**
 * 获取卡片密钥key
 *
 * @author Yu L.
 */
public class NfcCardKeyModel {

    private Handler handler;
    public static String GET_NFC_CARD_URL_SERVER = "";

    public NfcCardKeyModel(Handler handler) {
        this.handler = handler;
    }

    /**
     * 获取密钥
     *
     * @param sn
     */
    public void getKey(String sn, NfcCardValueInterface nfcCardValueInterface) {
        if (nfcCardValueInterface != null) {
            nfcCardValueInterface.postUidService(handler, sn);
        }
    }

}
