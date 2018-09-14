package fantansee.cn.ling.yu.sdk.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

import java.security.InvalidParameterException;

import cn.betatown.readcardframework.ReadCardHelper;
import cn.betatown.readcardframework.common.DeviceType;
import cn.betatown.readcardframework.common.ReadCallBack;
import cn.betatown.readcardframework.common.ReadMessage;
import cn.fantasee.landipos.LandiDeviceService;
import fantansee.cn.ling.yu.sdk.bean.NfcCardModel;
import fantansee.cn.ling.yu.sdk.bean.NfcCardValueBean;
import fantansee.cn.ling.yu.sdk.constant.NfcCardConstant;
import fantansee.cn.ling.yu.sdk.listener.NfcCardValueInterface;
import fantansee.cn.ling.yu.sdk.model.NfcCardKeyModel;

/**
 * nfc 卡号读取
 *
 * @author Yu L.
 */
public class NfcCardUtils {

    private static final String TAG = NfcCardUtils.class.getSimpleName();
    private Context mContext;
    private ProgressDialog mProgressDialog;
    private ReadCardHelper mReadCardHelper;
    private Toast mToast;
    private NfcCardValueInterface mNfcCardValueInterface;
    private NfcCardKeyModel mGetNfcCardKeyBusiness;
    private NfcCardModel mNfcCardModel;

    private NfcCardUtils() {
    }

    public NfcCardUtils(Context context, DeviceType deviceType) {
        if (deviceType != DeviceType.LANDIAPOSA8 && deviceType != DeviceType.LANDIC10POS)
            throw new InvalidParameterException(NfcCardConstant.INVID_DEVICE_TYPE_PARAMTER);
        mContext = context;
        mReadCardHelper = new ReadCardHelper(mContext, deviceType.getText());
        mReadCardHelper.setDeviceType(deviceType);
        mProgressDialog = new ProgressDialog(mContext,
                ThemeUtils.getDialogTheme());
        mProgressDialog.setMessage(NfcCardConstant.DIALOG_MSG_VALUE);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, NfcCardConstant.DIALOG_MSG_CANCEL,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        stopNfc();
                    }
                });
        mGetNfcCardKeyBusiness = new NfcCardKeyModel(new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                int _id = msg.what;
                switch (_id) {
                    case NfcCardConstant.GET_NFC_CARD_PWD_SUCCESS: {
                        String code = (String) msg.obj;
                        if (!TextUtils.isEmpty(code)) {
                            invidParamter(code);
                            mNfcCardModel.setCardPwd(code);
                            int sectorIndex = mNfcCardModel.getSectorIndex();
                            int blockIndex = mNfcCardModel.getBlockIndex();
                            String keyPwd = mNfcCardModel.getCardPwd();
                            String cardType = mNfcCardModel.getResultType();
                            mReadCardHelper.readCardInfo(ReadCardHelper.EXECUTE_TYPE_READ, sectorIndex, blockIndex, keyPwd, cardType, new ReadCallBack() {
                                @Override
                                public void onCallBack(ReadMessage readMessage) {
                                    closeNfc();
                                    if (readMessage != null) {
                                        String code = readMessage.getResultCode();
                                        String result = readMessage.getResultMessage();
                                        if (TextUtils.equals(code, ReadCardHelper.RESULT_SUCCESS)) {
                                            if (TextUtils.isEmpty(result)) {
                                                if (SoundTool.isIsSound())
                                                    SoundTool.getMySound(mContext).playMusic("error");
                                            } else {
                                                if (SoundTool.isIsSound())
                                                    SoundTool.getMySound(mContext).playMusic("success");
                                                if (mNfcCardValueInterface != null) {
                                                    NfcCardValueBean valueBean = new NfcCardValueBean();
                                                    valueBean.setResultCode(code);
                                                    valueBean.setResultValue(result);
                                                    mNfcCardValueInterface.postNfcObj(valueBean);
                                                }
                                            }
                                        } else {
                                            if (SoundTool.isIsSound())
                                                SoundTool.getMySound(mContext).playMusic("error");
                                        }
                                    }
                                }
                            });
                        }
                    }
                    break;
                    case NfcCardConstant.GET_NFC_CARD_PWD_ERROR: {
                        Bundle bundle = msg.getData();
                        if (bundle != null) {
                            String _errMsg = bundle.getString("msg", "读卡失败.");
                            Toast.makeText(mContext, _errMsg, Toast.LENGTH_LONG).show();
                        }
                        if (mProgressDialog != null) {
                            mProgressDialog.dismiss();
                        }
                        stopNfc();
                    }
                    break;
                }
            }
        });
    }

    public void setNfcCardModle(int sectorIndex, int blockIndex, int[] trackArrs, String pwd, boolean isOpen) {
        if (mNfcCardModel == null) {
            mNfcCardModel = new NfcCardModel();
        }
        invidParamter(pwd);
        mNfcCardModel.setSectorIndex(sectorIndex);
        mNfcCardModel.setBlockIndex(blockIndex);
        mNfcCardModel.setCardPwd(pwd);
        mNfcCardModel.setTrackNoArrs(trackArrs);
        mNfcCardModel.setOpenServer(isOpen);
    }

    public void setWriteContent(String content) {
        if (mNfcCardModel == null) {
            mNfcCardModel = new NfcCardModel();
        }
        mNfcCardModel.setWriteContent(content);
    }

    private void stopNfc() {
        closeNfc();
    }

    public void closeNfc() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
        if (mReadCardHelper != null)
            mReadCardHelper.cancel();
    }

    public void readNfcNo() {
        mNfcCardModel.setVerityPwd(true);
        if (mNfcCardModel == null) {
            showTipMessage(NfcCardConstant.READ_CARD_INFO_ERROR_MSG);
        }
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
        final int sectorIndex = mNfcCardModel.getSectorIndex();
        final int blockIndex = mNfcCardModel.getBlockIndex();
        String key = mNfcCardModel.getDefaultPwd();
        if (mNfcCardModel.isVerityPwd()) {
            key = mNfcCardModel.getCardPwd();
        }
        final String keyPwd = key;
        mReadCardHelper.readCardInfo(ReadCardHelper.EXECUTE_TYPE_UUID, sectorIndex, blockIndex, key, "", new ReadCallBack() {
            @Override
            public void onCallBack(ReadMessage readMessage) {
                if (readMessage != null) {
                    final String resultCode = readMessage.getResultCode();
                    if (TextUtils.equals(resultCode, ReadCardHelper.RESULT_SUCCESS)) {
                        String uid = readMessage.getResultMessage();
                        if (mNfcCardModel.isOpenServer()) {
                            mNfcCardModel.setResultType(readMessage.getResultType());
                            uid = CStringUtil.getUidString(uid);
                            CardLogUtils.printELog(TAG, uid);
                            if (TextUtils.getTrimmedLength(uid) == 8) {
                                mNfcCardModel.setUid(uid);
                                mGetNfcCardKeyBusiness.getKey(uid, mNfcCardValueInterface);
                            } else {
                                stopNfc();
                            }
                        } else {
                            mReadCardHelper.readCardInfo(ReadCardHelper.EXECUTE_TYPE_READ, sectorIndex, blockIndex, keyPwd, readMessage.getResultType(), new ReadCallBack() {
                                @Override
                                public void onCallBack(ReadMessage readMessage) {
                                    closeNfc();
                                    if (readMessage != null) {
                                        String code = readMessage.getResultCode();
                                        String result = readMessage.getResultMessage();
                                        if (TextUtils.equals(code, ReadCardHelper.RESULT_SUCCESS)) {
                                            if (TextUtils.isEmpty(result)) {
                                                if (SoundTool.isIsSound())
                                                    SoundTool.getMySound(mContext).playMusic("error");
                                            } else {
                                                if (SoundTool.isIsSound())
                                                    SoundTool.getMySound(mContext).playMusic("success");
                                                if (mNfcCardValueInterface != null) {
                                                    NfcCardValueBean valueBean = new NfcCardValueBean();
                                                    valueBean.setResultCode(code);
                                                    valueBean.setResultValue(result);
                                                    mNfcCardValueInterface.postNfcObj(valueBean);
                                                }
                                            }
                                        } else {
                                            if (SoundTool.isIsSound())
                                                SoundTool.getMySound(mContext).playMusic("error");
                                        }
                                    }
                                }
                            });
                        }
                    } else {
                        if (SoundTool.isIsSound())
                            SoundTool.getMySound(mContext).playMusic("error");
                        closeNfc();
                    }
                }
            }
        });
    }

    public void readMagNo() {
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
        int[] trackNos = mNfcCardModel.getTrackNoArrs();
        if (trackNos == null || trackNos.length == 0) {
            showTipMessage(NfcCardConstant.READ_CARD_INFO_ERROR_MSG);
            return;
        }
        mReadCardHelper.readMagCardInfo(trackNos.clone(), false, new ReadCallBack() {
            @Override
            public void onCallBack(ReadMessage readMessage) {
                closeNfc();
                if (readMessage != null) {
                    String _resultCode = readMessage.getResultCode();
                    String _msg = readMessage.getResultMessage();
                    if (TextUtils.equals(_resultCode, ReadCardHelper.RESULT_FAILED)) {
                        if (SoundTool.isIsSound())
                            SoundTool.getMySound(mContext).playMusic("error");
                    } else if (TextUtils.equals(_resultCode, ReadCardHelper.RESULT_SUCCESS)) {
                        if (TextUtils.isEmpty(_msg)) {
                            if (SoundTool.isIsSound())
                                SoundTool.getMySound(mContext).playMusic("error");
                        } else {
                            if (SoundTool.isIsSound())
                                SoundTool.getMySound(mContext).playMusic("success");
                        }
                    }
                    if (mNfcCardValueInterface != null) {
                        NfcCardValueBean valueBean = new NfcCardValueBean();
                        valueBean.setResultCode(_resultCode);
                        CardLogUtils.printELog(TAG, "card value:" + _msg);
                        valueBean.setResultValueArr(CStringUtil.getDotString(_msg));
                        mNfcCardValueInterface.postCardObj(valueBean);
                    }
                }
            }
        });
    }

    public void writeNfcCard() {
        mNfcCardModel.setVerityPwd(true);
        if (mNfcCardModel == null) {
            showTipMessage(NfcCardConstant.READ_CARD_INFO_ERROR_MSG);
        }
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
        final int sectorIndex = mNfcCardModel.getSectorIndex();
        final int blockIndex = mNfcCardModel.getBlockIndex();
        String key = mNfcCardModel.getDefaultPwd();
        if (mNfcCardModel.isVerityPwd()) {
            key = mNfcCardModel.getCardPwd();
        }
        mReadCardHelper.writeCardInfo(sectorIndex, blockIndex, key, mNfcCardModel.getWriteContent(), new ReadCallBack() {
            @Override
            public void onCallBack(ReadMessage readMessage) {
                closeNfc();
                if (readMessage != null) {
                    String code = readMessage.getResultCode();
                    String result = readMessage.getResultMessage();
                    if (TextUtils.equals(code, ReadCardHelper.RESULT_SUCCESS)) {
                        if (TextUtils.isEmpty(result)) {
                            if (SoundTool.isIsSound())
                                SoundTool.getMySound(mContext).playMusic("error");
                        } else {
                            if (SoundTool.isIsSound())
                                SoundTool.getMySound(mContext).playMusic("success");
                        }
                    } else {
                        if (SoundTool.isIsSound())
                            SoundTool.getMySound(mContext).playMusic("error");
                    }
                    if (mNfcCardValueInterface != null) {
                        NfcCardValueBean valueBean = new NfcCardValueBean();
                        valueBean.setResultCode(code);
                        valueBean.setResultValue(result);
                        mNfcCardValueInterface.postNfcObj(valueBean);
                    }
                }
            }
        });
    }

    public void changeNfcCardBlockPwd() {
        mNfcCardModel.setVerityPwd(true);
        if (mNfcCardModel == null) {
            showTipMessage(NfcCardConstant.READ_CARD_INFO_ERROR_MSG);
        }
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
        int sectorIndex = mNfcCardModel.getSectorIndex();
        int blockIndex = mNfcCardModel.getBlockIndex();
        if (blockIndex != NfcCardConstant.CARD_CONTROL_BLOCK) {
            blockIndex = NfcCardConstant.CARD_CONTROL_BLOCK;
        }
        String key = mNfcCardModel.getDefaultPwd();
        if (mNfcCardModel.isVerityPwd()) {
            key = mNfcCardModel.getCardPwd();
        }
        String value = mNfcCardModel.getWriteContent() + NfcCardConstant.CARD_CONTROL_VALUE;
        mReadCardHelper.writeCardInfo(sectorIndex, blockIndex, key, value, new ReadCallBack() {
            @Override
            public void onCallBack(ReadMessage readMessage) {
                closeNfc();
                if (readMessage != null) {
                    String code = readMessage.getResultCode();
                    String result = readMessage.getResultMessage();
                    if (TextUtils.equals(code, ReadCardHelper.RESULT_SUCCESS)) {
                        if (TextUtils.isEmpty(result)) {
                            if (SoundTool.isIsSound())
                                SoundTool.getMySound(mContext).playMusic("error");
                        } else {
                            if (SoundTool.isIsSound())
                                SoundTool.getMySound(mContext).playMusic("success");
                        }
                    } else {
                        if (SoundTool.isIsSound())
                            SoundTool.getMySound(mContext).playMusic("error");
                    }
                    if (mNfcCardValueInterface != null) {
                        NfcCardValueBean valueBean = new NfcCardValueBean();
                        valueBean.setResultCode(code);
                        valueBean.setResultValue(result);
                        mNfcCardValueInterface.postNfcObj(valueBean);
                    }
                }
            }
        });
    }

    private void showTipMessage(String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
        }
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.setText(msg);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }

    public void setTitle(String title) {
        if (TextUtils.isEmpty(title))
            return;
        if (mProgressDialog != null)
            mProgressDialog.setTitle(title);
    }

    public void setMessage(String message) {
        if (TextUtils.isEmpty(message))
            return;
        if (mProgressDialog != null)
            mProgressDialog.setMessage(message);
    }

    public void addCallBack(NfcCardValueInterface valueInterface) {
        mNfcCardValueInterface = valueInterface;
    }

    public void onResume() {
        if (mContext != null)
            LandiDeviceService.login(mContext);
    }

    public void onStop() {
        LandiDeviceService.logout();
    }

    private void invidParamter(String pwd) {
        if (TextUtils.getTrimmedLength(pwd) != 12) {
            throw new InvalidParameterException(NfcCardConstant.INVID_PASSWORD_LENGTH);
        }
    }

    public void destroyInstance() {
        mReadCardHelper = null;
        mContext = null;
    }
}
