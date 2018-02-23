# MusicDNA
一款汉化版的musicdna项目
- 2018/2/13开始新建项目
- 2/14 创建首页,
```
1.欢迎页可以考虑做欢迎页或者附带广告的延时操作，待完善；
2.android.intent.category.LAUNCHER与android.intent.category.DEFAULT的差异；
```
- 2/15 首页的引导页
```
1.不同deawable-v21/v24的不同之处(hdpi/mdpi你知道，但是了解不深，待加强！)
```
- 2/16 今天写好本地音乐布局+列表显示(大年初一，没写代码，今天补上)
```
1.对于fragment的生命周期在深入体会下，要记住！
2.关于图片缓存的可以参考本代码进行深度学习整理；
3.com.squareup.leakcanary-android-no-op的库的学习；
```
- 2/19 播放界面
```
对于歌曲播放本页面功能实现比较繁琐，必须一一整理。
1.歌词类：Lyrics Parcelable序列化,许久都不熟悉，要去整理的！
2.播放界面左右滑动切换歌曲，
3.本地检索歌词/保存歌词，网络下载歌词保存本地，
4.音乐播放服务里：MediaSessionManager/MediaSession/MediaController等不熟悉的类别，PendingIntent
5.通知栏显示音乐的布局得做调整，可以模仿网易云音乐；
6.mMediaPlayer的监听方法：
setOnPreparedListener/setOnCompletionListener/setOnBufferingUpdateListener/setOnErrorListener/setOnInfoListener等
7.GestureDetector类的学习/ RecyclerView.OnItemTouchListener
```
2/20 均衡器界面功能/设置里面各功能
2/21 喜欢/最近文件夹/保存dna
```
```
2/22 今天完成,所有的功能必须结束。
```
```
2/23 一天时间——准备简历；

2/24 Service/BroadcastReceiver
```
1.项目中2个广播，1个服务；
2.PhoneStateListener：什么监听方法？
PhoneStateListener是给三方app监听通信状态变化的方法
3.service里面的方法以及与界面的通讯；
```
## 后续回公司把一些没有的功能根据需要补充完成，如果有可能，可以再继续做多次开发，得看时间。
功能：
1.是否可以从外部加入下载网络音乐，与网络视频并播放？
2.删除歌曲
3.头部最近播放的歌曲专辑图片不显示，待处理 2/23
