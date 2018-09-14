package fantansee.cn.ling.yu.sdk.constant;

/**
 * @author Yu L.
 */
public interface NfcCardConstant {

    String INVID_PASSWORD_LENGTH = "密码格式错误，请检查.";
    String INVID_DEVICE_TYPE_PARAMTER = "不支持的设备.";
    String DIALOG_MSG_VALUE = "读取卡信息...";
    String DIALOG_MSG_CANCEL = "取消";
    String READ_CARD_INFO_ERROR_MSG = "请设置卡片信息.";
    int GET_NFC_CARD_PWD_SUCCESS = 0;
    int GET_NFC_CARD_PWD_ERROR = -1;
    String CARD_CONTROL_VALUE = "FF078069FFFFFFFFFFFF";
    int CARD_CONTROL_BLOCK = 3;
}
