# iHealth Device Developer Documnentation
###### This document describes how to use the iHealth Device SDK to accomplish the major operation: Connection Device, Online Measurement, Offline Measurement and iHealth Device Management.

## How to use the iHealth SDK

iHealth Device SDK 支持通过USB, Bluetooth, BluetoothLe, Wifi, 音频接口和iHealth Device相互连接并进行数据交互。

### 支持设备的iHealth Device for Android

iHealth Bp3m 
iHealth Bp3l
iHealth Bp5
iHealth Bp7
iHealth Abi
iHealth Hs3
iHealth Hs4
iHealth Hs4s
iHealth Hs5
iHealth Am3
iHealth Am3s
iHealth Po3

### 相关配置

需要引入开发工具包iHealthLibrary.jar。其中BP ABI、BP3、BP5、BP7、BG1、BG5、HS3和HS5支持Android4.0及其以上版本；AM3、AM3S、HS4和PO3支持Android4.3及其以上版本和Android4.4的三星品牌手机。
具体配置如下图：

![box-model](https://github.com/iHealthDeviceLabs/iHealthDeviceLabs-Android/blob/master/public/ihealth_device_doc.png?raw=true)

### 用户申请流程

[请点击此链接](https://github.com/iHealthDeviceLabs/iHealthDeviceLabs-Android/blob/master/doc/Developer_Registration_Application_Instruction.md)


### SDK使用流程

##### 1. Initialization iHealth SDK.

```java
iHealthDevicesManager.getInstance().init(MainActivity.this);
```

##### 2. Register callback, and get a callback ID.

```java
/*
 * Register callback to the manager. This method will return a callback Id.
 */
int callbackId = iHealthDevicesManager.getInstance().registerClientCallback(iHealthDevicesCallback);
```

##### 4. Add callback filter.

```java

```

##### 3. Verify iHealth device user permission.

```java
iHealthDevicesManager.getInstance().sdkUserInAuthor(MainActivity.this, userName, clientId, clientSecret, callbackId);
```

```java
private iHealthDevicesCallback iHealthDevicesCallback = new iHealthDevicesCallback() {

    @Override
    public void onScanDevice(String mac, String deviceType) {
    }

    @Override
    public void onDeviceConnectionStateChange(String mac, String deviceType, int status) {
    }
    
    @Override
    public void onUserStatus(String username, int userStatus) {    	
    }

	@Override
    public void onDeviceNotify(String mac, String deviceType, String action, String message) {
    }
};
```


##### 4. Add callback filter.

```java
iHealthDevicesManager.getInstance().addCallbackFilterForAddress(clientCallbackId, ...);
```

```java
iHealthDevicesManager.getInstance().addCallbackFilterForDeviceType(clientCallbackId, ...);
```


##### 5. Discovery a iHealth device.

```java
int type = iHealthDevicesManager.DISCOVERY_BP5
iHealthDevicesManager.getInstance().startDiscovery(type);
```

##### 6. Connection a iHealth device.

```java
iHealthDevicesManager.getInstance().connectDevice(userName, mac);
```

##### 7. Get iHealth device controller.

```java

Am3Control am3Control = iHealthDevicesManager.getInstance().getAm3Control(mac);

Am3sControl am3sControl = iHealthDevicesManager.getInstance().getAm3sControl(mac);

Bg1Control bg1Control = iHealthDevicesManager.getInstance().getBg1Control(mac);

Bg5Control bg5Control = iHealthDevicesManager.getInstance().getBg5Control(mac);

Bp3mControl bp3mControl = iHealthDevicesManager.getInstance().getBp3mControl(mac);

Bp3lControl bp3lControl = iHealthDevicesManager.getInstance().getBp3lControl(mac);

Bp5Control bp5Control = iHealthDevicesManager.getInstance().getBp5Control(mac);

Bp7Control bp7Control = iHealthDevicesManager.getInstance().getBp7Control(mac);

Hs3Control hs3Control = iHealthDevicesManager.getInstance().getHs3Control(mac);

Hs4Control hs4Control = iHealthDevicesManager.getInstance().getHs4Control(mac);

Hs4sControl hs4sControl = iHealthDevicesManager.getInstance().getHs4sControl(mac);

Hs5Control hs5Control = iHealthDevicesManager.getInstance().getHs5Control(mac);

Po3Control po5Control = iHealthDevicesManager.getInstance().getPo3Control(mac);

```

## API Guide

[请点击此链接](https://github.com/iHealthDeviceLabs/iHealthDeviceLabs-Android/tree/master/api-docs)

## Examples

[请点击此链接](https://github.com/iHealthDeviceLabs/iHealthDeviceLabs-Android/blob/master/doc/Developer_Registration_Application_Instruction.md)

## Release Note

[请点击此链接](https://github.com/iHealthDeviceLabs/iHealthDeviceLabs-Android/blob/master/doc/Developer_Registration_Application_Instruction.md)

## FAQ
