支持android A8 POS 射频卡,磁条卡读取，写入的一组公共方法.

# 引入项目
Gradle:

```java
implementation 'cn.com.yl.readcard:readcardutils:1.0.1'
implementation(name: 'fantaseedevicesdk_1935_201805241037', ext: 'aar')
```

> 设置debug模式和声音提示音

 1.在application文件中，开启debug模式

```java
CardLogUtils.setDebug(true);
```

 2.在application文件中,打开或者关闭读卡，写卡提示音 


```java
SoundTool.setIsSound(true);
```

> 初始化调用

```java
private NfcCardUtils mNfcCardUtils;
 mNfcCardUtils=new NfcCardUtils(this, DeviceType.LANDIC10POS);
 //参数说明 1.射频卡扇区 2.射频卡块 3.磁条卡磁道块 4.射频卡密码 5.是否读取射频卡卡uuid
 mNfcCardUtils.setNfcCardModle(0, 0,new int[]{1，2，3},"FFFFFFFFFFFF",false);
```

> 读卡回调使用说明

在Activity的onResume中增加方法:

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

> 射频卡读取方法使用

```java
mNfcCardUtils.readNfcNo();
```

> 磁条卡读取方法使用

```java
mNfcCardUtils.readMagNo();
```

> 释放资源

在activity的onPause方法中调用:

```java
mNfcCardUtils.onStop();
```

在activity的onDestroy方法中调用:

```java
 mNfcCardUtils.destroyInstance();
 mNfcCardUtils=null;
```
