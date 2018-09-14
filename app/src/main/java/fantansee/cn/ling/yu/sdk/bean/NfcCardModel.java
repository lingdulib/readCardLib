package fantansee.cn.ling.yu.sdk.bean;

/**
 * 设置rfid卡读取信息
 *
 * @author Yu L.
 */
public class NfcCardModel {

    private String cardPwd = "";
    private int sectorIndex = 0;
    private int blockIndex = 0;
    private String uid = "";
    private boolean isOpenServer = false;
    private boolean isVerityPwd = false;
    private int[] trackNoArrs;
    private String defaultPwd = "FFFFFFFFFFFF";
    private String resultType = "";
    private String writeContent = "";

    public String getCardPwd() {
        return cardPwd;
    }

    public void setCardPwd(String cardPwd) {
        this.cardPwd = cardPwd;
    }

    public int getSectorIndex() {
        return sectorIndex;
    }

    public void setSectorIndex(int sectorIndex) {
        this.sectorIndex = sectorIndex;
    }

    public int getBlockIndex() {
        return blockIndex;
    }

    public void setBlockIndex(int blockIndex) {
        this.blockIndex = blockIndex;
    }

    public boolean isOpenServer() {
        return isOpenServer;
    }

    public void setOpenServer(boolean openServer) {
        isOpenServer = openServer;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDefaultPwd() {
        return defaultPwd;
    }

    public boolean isVerityPwd() {
        return isVerityPwd;
    }

    public void setVerityPwd(boolean verityPwd) {
        isVerityPwd = verityPwd;
    }

    public int[] getTrackNoArrs() {
        return trackNoArrs;
    }

    public void setTrackNoArrs(int[] trackNoArrs) {
        this.trackNoArrs = trackNoArrs;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getWriteContent() {
        return writeContent;
    }

    public void setWriteContent(String writeContent) {
        this.writeContent = writeContent;
    }
}
