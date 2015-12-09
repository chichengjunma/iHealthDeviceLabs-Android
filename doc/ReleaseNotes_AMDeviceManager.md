# Release notes for AM Device Manager

## Introducation

The release notes for AM Device Manager provide an overview of the release and document.

## How to use

### Get AM device battery

1. Initializing AM control method.
2. Call control.queryAMState() in which you want to get battery.
3. If success,you can get an action of AM device battery message in callback method.

### Check alarm information

1. Initializing AM control method.
2. Call control.checkAlarmClock(id) in which you want to check.
3. If success,you can get an action of the alarm message in callback method.

### Delete alarm

1. Initializing AM control method.
2. Call control.deleteAlarmClock(id) in which you want to delete.
3. If you get a action without message,delete the alarm is success.

## AM Device Manager

Refactoring the AM Device Manager.

### old version

![Registration1](https://github.com/iHealthDeviceLabs/iHealthDeviceLabs-Android/blob/master/public/AM_DeviceManager_old.png?raw=true)

### new version

![Registration1](https://github.com/iHealthDeviceLabs/iHealthDeviceLabs-Android/blob/master/public/AM_DeviceManager_new.png?raw=true)