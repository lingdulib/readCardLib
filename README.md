Support part of the Android POS device reader tools category, the following is the specific use of the method.

# Download
Gradle:

```java
implementation 'cn.com.yl.readcard:readcardutils:1.0.1'
implementation(name: 'fantaseedevicesdk_1935_201805241037', ext: 'aar')
```

> Set debug mode

Add in the application file:

```java
CardLogUtils.setDebug(true);
```

In the application file, turn on the card to complete the prompt tone:

```java
SoundTool.setIsSound(true);
```

> initializer

```java
private NfcCardUtils mNfcCardUtils;
 mNfcCardUtils=new NfcCardUtils(this, DeviceType.LANDIC10POS);
 //参数说明 1.射频卡扇区 2.射频卡块 3.磁条卡磁道块 4.射频卡密码 5.是否读取射频卡卡uuid
 mNfcCardUtils.setNfcCardModle(0, 0,new int[]{1，2，3},"FFFFFFFFFFFF",false);
```

> Card reading callback method

Add to the onResume method in activity:

```java
mNfcCardUtils.onResume();
        mNfcCardUtils.addCallBack(new NfcCardValueInterface() {
            @Override
            public void postNfcObj(NfcCardValueBean valueBean) {
			//射频卡回调
                mWorkTextView.append("读卡状态:"+valueBean.getResultCode());
                mWorkTextView.append("\n");
                mWorkTextView.append("读取内容:"+valueBean.getResultValue());
                mWorkTextView.append("\n");
                String value= CStringUtil.getELastString(valueBean.getResultValue());
                Log.e(TAG,"value:"+value);
            }

            @Override
            public void postCardObj(NfcCardValueBean valueBean) {
			//磁条卡回调
                mWorkTextMagView.append("读卡状态:"+valueBean.getResultCode());
                String[] resultArr=valueBean.getResultValueArr();
                for(String value : resultArr){
                    Log.e(TAG,"mag card code :"+value);
                    mWorkTextMagView.append("\n");
                    mWorkTextMagView.append("读取内容:"+value);
                    mWorkTextMagView.append("\n");
                }
            }

            @Override
            public void postUidService(Handler handler, String sn) {
                if(handler!=null){
                    Message msg = Message.obtain();
                    msg.what = NfcCardConstant.GET_NFC_CARD_PWD_SUCCESS;
                    handler.sendMessage(msg);
                    mWorkTextView.append("读卡uuid:"+sn);
                    mWorkTextView.append("\n");
                }
            }
        });
```

> Read the card information method and add:

Read RF card information

```java
mNfcCardUtils.readNfcNo();
```

Read the card information

```java
mNfcCardUtils.readMagNo();
```

> Release resources

Add to the onPause method in activity:

```java
mNfcCardUtils.onStop();
```

Add to the onDestroy method in activity:

```java
 mNfcCardUtils.destroyInstance();
 mNfcCardUtils=null;
```
