package fantansee.cn.ling.yu.sdk.bean;

/**
 * nfc 卡片值信息
 *
 * @author Yu L.
 */
public class NfcCardValueBean {

    private String resultCode = "";
    private String resultValue = "";
    private String[] resultValueArr;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultValue() {
        return resultValue;
    }

    public void setResultValue(String resultValue) {
        this.resultValue = resultValue;
    }

    public String[] getResultValueArr() {
        return resultValueArr;
    }

    public void setResultValueArr(String[] resultValueArr) {
        this.resultValueArr = resultValueArr;
    }
}
