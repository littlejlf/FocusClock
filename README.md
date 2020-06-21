# FocusClock

**一款能够让你在专注工作时不受手机干扰的软件。**

**功能包括：**

* **用户可通过seekBar滑动来选择专注时长（120分钟以内），随后开始计时，在计时期间，只允许访问本app，当用户访问其他app将被强制拉回。**

* **可选择打开关闭环境音（雨声）,计时时环境音自动打开。当用户在计时时选择放弃，将弹出弹窗警告和警告提示音。**

* **保存用户的专注时长至数据库中。**

* **可通过back键返回而不会导致计时中断（Actiivty销毁），计时期间允许访问本app其他activity而音乐和计时功能正常进行。**

    ![add image](https://github.com/cloudmusiccc/TimeMaster/raw/master/showImage/nice.png)
## 运行环境*

**安卓实体机：android 9及以下**

**安卓虚拟机：android 10.0 (Q)及以下**：

确定build.gradle文件添加了依赖项

```xml
implementation 'org.litepal.android:core:1.4.1'
```

确定manifest文件添加了用户权限

```xml
  <uses-permission
        android:name="android.permission.PACKAGE_USAGE_STATS"
        tools:ignore="ProtectedPermissions" />
```

注意在本app所在的activity设置启动模式为singleTask

```xml
  android:launchMode="singleTask"
```

**Android 10 为了减少app对于用户不必要的影响，对用户权限作了更为严格的限制，对于app从后台启动activity进行了更高级别的限制，因此本项目通过Service启动Activity的功能在Android 10版本下失效。**

**这意味着在Android 10的运行环境下本app将无法强制将用户拉回 ！**

**这意味着在Android 10的运行环境下本app将无法强制将用户拉回 ！**

**这意味着在Android 10的运行环境下本app将无法强制将用户拉回 ！**

  ![add image](https://github.com/cloudmusiccc/TimeMaster/raw/master/showImage/change.png)




**有关文档如下：**https://developer.android.com/guide/components/activities/background-starts



## 设计流程：

  ![add image](https://github.com/cloudmusiccc/TimeMaster/raw/master/showImage/flow.png)

## 各方法介绍：

| **MainActivity**           |                                                              |
| -------------------------- | ------------------------------------------------------------ |
| **函数名**                 | **功能简介**                                                 |
| **bindViews**              | **通过seekBar的触发事件动态显示用户选取的时长**              |
| **upDataStudyTime**        | **计算当前用户的专注时长，并显示在textView中**               |
| **isAccessGranted**        | **判断当前用户是否开启了该app所需权限ACTION_USAGE_ACCESS_SETTINGS，若未开启，则跳转至权限开启页面** |
| **startMusicService**      | **开启服务：MusicService，环境音（雨声）开始播放**           |
| **stopMusicService**       | **关闭服务：MusicService,环境音（雨声）停止播放**            |
| **startAlarmMusicService** | **开启服务：AlarmMusicService,警告音播放**                   |
| **stopAlarmMusicService**  | **关闭服务:AlarmMusicService,警告音关闭**                    |
| **showQuitEvent**          | **显示退出弹窗警告**                                         |
| **startMyService**         | **开启服务:MyService，监视用户是否打开了其他app，若有，将被强制拉回** |
| **stopMyService**          | **关闭服务：MyService**                                      |
| **IsMyserviceRunning**     | **判断MusicService是否运行，与播放音乐有关**                 |
| **updateCountDownText**    | **计算当前剩余分钟和剩余秒，并显示在textView**               |
| **startTimer**             | **开始计时，建立一个倒计时器线程，以每秒为一个间隔进行计时，并每秒调用updateCountDownText刷新当前剩余时间界面，计时结束需更新数据库，更新页面显示** |
| **pauseTimer**             | **停止计时，调用cancel()方法销毁计时器线程，更新页面显示**   |



| **testActivity**                                             |
| ------------------------------------------------------------ |
| **当用户打开其他app，将被强制拉回，但经过多次实验发现无法拉回TimeMasterActivity，故创建一个testActivity作为中间跳转。用户将被强制拉回此活动，onCreate方法执行时会发出弹窗，“上课期间禁止玩手机”，用户只能点击确定，随后进入TimeMasterActivity** |

 

| **MyService**      |                                                              |
| ------------------ | ------------------------------------------------------------ |
| **task**           | **建立一个任务，获取系统当前时间time，并查询time之前10秒到time这段时间最新使用的应用，并作为topPackageName打印至控制台。随后判断topPackageName是否为DeepClass所在包名，若不是，则强制拉回** |
| **onStartCommand** | **建立一个定时器timer,每隔1秒执行一次任务task**              |
| **onDestory**      | **销毁任务，销毁定时器线程**                                 |

 

| **MusicService**                                             |
| ------------------------------------------------------------ |
| **建立控件MediaPlayer播放环境音（雨声），设置循环播放：true** |

 

| **AlarmMusicService**                                      |
| ---------------------------------------------------------- |
| **建立控件MediaPlayer播放放弃警告音，设置循环播放：false** |



##### -------------------------------------------------------该项目仅用于学习---------------------------------------------------------------------------

