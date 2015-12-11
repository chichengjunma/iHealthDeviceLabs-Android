# iHealth Device Developer Documnentation
This document describes how to use the iHealth Device SDK to accomplish the major operation: Connection Device, Online Measurement, Offline Measurement and iHealth Device Management.

### Support iHealth Device for Android

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

### Configure

Need to introduce the development kit iHealthLibrary.jar. Which are ABI BP3, BP5, BP7, BG1, BG5, BP, HS3, Android4.0 and HS5 support AM3 and its version; AM3S, HS4, and PO3 support Android4.3 and the above version and Android4.4 Samsung brand mobile phone.

Specific configuration as shown below:


Need to introduce the development kit iHealthLibrary.jar. 
Which are ABI BP3, BP5, BP7, BG1, BG5, BP, HS3, Android4.0 and HS5 support AM3 and its version; 
AM3S, HS4, and PO3 support Android4.3 and the above version and Android4.4 Samsung brand mobile phone.
detail config follow：

![box-model](https://github.com/iHealthDeviceLabs/iHealthDeviceLabs-Android/blob/master/public/ihealth_device_doc.png?raw=true)

### How to apply for SDK permissions

[Click this link](https://github.com/iHealthDeviceLabs/iHealthDeviceLabs-Android/blob/master/doc/Developer_Registration_Application_Instruction.md)


### How to use the iHealth SDK

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

##### 3. Add callback filter.

```java
iHealthDevicesManager.getInstance().addCallbackFilterForAddress(clientCallbackId, ...);
```

```java
iHealthDevicesManager.getInstance().addCallbackFilterForDeviceType(clientCallbackId, ...);
```

##### 4. Verify iHealth device user permission.

```java
iHealthDevicesManager.getInstance().sdkUserInAuthor(MainActivity.this, userName, clientId, clientSecret, callbackId);
```

```java
private iHealthDevicesCallback iHealthDevicesCallback = new iHealthDevicesCallback() {
    
    @Override
    public void onUserStatus(String username, int userStatus) {    	
    }

};
```

##### 5. Discovery a iHealth device.

```java
int type = iHealthDevicesManager.DISCOVERY_BP5
iHealthDevicesManager.getInstance().startDiscovery(type);
```

```java
private iHealthDevicesCallback iHealthDevicesCallback = new iHealthDevicesCallback() {

    @Override
    public void onScanDevice(String mac, String deviceType) {
    }

};
```

##### 6. Connection a iHealth device.

```java
iHealthDevicesManager.getInstance().connectDevice(userName, mac);
```

```java
private iHealthDevicesCallback iHealthDevicesCallback = new iHealthDevicesCallback() {

    @Override
    public void onDeviceConnectionStateChange(String mac, String deviceType, int status) {
    }
};
```

##### 7. Get iHealth device controller.

```java
/*
* Get Am3 device controller
*/
Am3Control am3Control = iHealthDevicesManager.getInstance().getAm3Control(mac);

/*
* Get Am3s device controller
*/
Am3sControl am3sControl = iHealthDevicesManager.getInstance().getAm3sControl(mac);

/*
* Get Bg1 device controller
*/
Bg1Control bg1Control = iHealthDevicesManager.getInstance().getBg1Control(mac);

/*
* Get Bg5 device controller
*/
Bg5Control bg5Control = iHealthDevicesManager.getInstance().getBg5Control(mac);

/*
* Get Bp3m device controller
*/
Bp3mControl bp3mControl = iHealthDevicesManager.getInstance().getBp3mControl(mac);

/*
* Get Bp3l device controller
*/
Bp3lControl bp3lControl = iHealthDevicesManager.getInstance().getBp3lControl(mac);

/*
* Get Bp5 device controller
*/
Bp5Control bp5Control = iHealthDevicesManager.getInstance().getBp5Control(mac);

/*
* Get Bp7 device controller
*/
Bp7Control bp7Control = iHealthDevicesManager.getInstance().getBp7Control(mac);

/*
* Get Hs3 device controller
*/
Hs3Control hs3Control = iHealthDevicesManager.getInstance().getHs3Control(mac);

/*
* Get Hs4 device controller
*/
Hs4Control hs4Control = iHealthDevicesManager.getInstance().getHs4Control(mac);

/*
* Get Hs4s device controller
*/
Hs4sControl hs4sControl = iHealthDevicesManager.getInstance().getHs4sControl(mac);

/*
* Get Hs5 device controller
*/
Hs5Control hs5Control = iHealthDevicesManager.getInstance().getHs5Control(mac);

/*
* Get Po3 device controller
*/
Po3Control po3Control = iHealthDevicesManager.getInstance().getPo3Control(mac);

```

## API Guide

[Click this link](https://github.com/iHealthDeviceLabs/iHealthDeviceLabs-Android/tree/master/api-docs)

## Examples

[Click this link](https://github.com/iHealthDeviceLabs/iHealthDeviceLabs-Android/tree/master/examples)

## Release Note

[Click this link](https://github.com/iHealthDeviceLabs/iHealthDeviceLabs-Android/blob/master/doc/ReleaseNotes_DeviceManager.md)

## FAQ
