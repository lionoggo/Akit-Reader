FastApp
---------
FastApp是一个轻量级急速开发框架，基于Fragment+RxJava+Retrofit 2.0+Glide+Realm构建，采用Material Design设计风格，帮助开发者快速构建轻量级应用。当然，由于本项目是为了说明博客中提到的一些问题，因此早期并未让其复杂化。另外，为了让它富有生机，给它起名为“师父说”.

本项目基于Material Design+RxJava+Retrofit 2.0+Glide+Realm构建了一个良好MVC架构的客户端模型，对新手友好度更高。


本项目仍在不断的完善阶段，欢迎及时向我反馈[ISSUES][1]，[email][2]。如果对你有帮助欢迎点个star、fork.另外也欢迎欢迎关注[代码之道，编程之法][3]

---------------

Preview
-------------

![此处输入图片的描述][4]

![此处输入图片的描述][5]


![此处输入图片的描述][6]

![此处输入图片的描述][7]

--------------------

Download
----------
(Android 5.0 or above)
[点击下载最新版][8]


---------------

Point
--------------------


### 为什么采用多Fragment构建应用？
相比Activity，Fragment稍显复杂。谈起Fragment的时候，很多开发者直接摆手，然后告诉你这玩意坑太多,比如说调试比较困难，无法有效的实现业务逻辑和View的解耦，偶尔的NullPointerException问题等等。但这些问题都不是阻止我们使用Fragment的原因。其实深究下来，你会发现这并不是Fragment自身的问题，而更多的是由于Fragment稍显复杂，导致很多开发者没有耐心去深究它，再加上一些开发者喜欢直接copy网上的代码，不加以思考的就应用在实际项目中，后果可想而知。有很多人也提出过通过自定义View的方式取代Fragment，当然这也是可行的，但是对于大部分开发者来说，这好像没有必要（你不是在加班，就是在吃饭...你懂的）

那么这里我为什么要采用多Fragment构建基础框架呢？主要有有两方面的原因：
>1.相比创建Actvity，Fragment要显得更加轻量级。尤其是在同一个Activity中实现多种布局的时候，无需新建Activity，另外，由于Fragment可以被缓存，因此在某些场景下会有更流畅的体验。
2. Fragment能让你更容易的适配手机和平板。如果你的应用需要支持这两种类型的设备，那么使用Fragment会帮你减少很多适配问题。
3. 最大程度上的实现布局复用，更好的实现模块化。很多情况下，你会发现：你在用玩“积木”的方法拼装应用。

而你所付出的代价仅仅需要花费点事件研究它。在FastApp当中，我们已经为你了一部分工作，在大多数情况下，它工作良好。你需要做的就是关注界面的实现和业务逻辑。

### 为什么采用RxJava+Retrofit？
在11年左右的时候，没有太多网络库供你选择，所以在一些比较老的项目当中还会看到我们使用HttpUrlConnection或者HttpClient来自行封装成的请求库。5年的时间，让Android整个生态环境更加晚上，越来越多人参与到Android的开发工作，也诞生了很多非常优秀的网络库，如async-http-client,volley，OkHttpClient,Retrofit等等，这些库各有利弊，但是就目前看来相对比较通用又受大家欢迎的莫过于volley，OkHttpclent和Retrofit这三者了。而Retrofit是通过包装OKHttpClient而来，所以说两者在底层并无多大区别，而volley则是google前几年推出的。在我看来，在你的应用无特殊要求的情况下，三者之一都能满足需求，而且在出现问题的时候能快速的从周围开发者得到反馈。

这里我选择了Retrofit来做出基础网络请求，另外也应时的结合RxJava来帮助大家更好的学习。

>补：目前国内很多大神也在这些项目智商二次封装了很多使用的库，比如NoHttp，xUtils，OkHttpUtils，OkGo等，也有很多一些从头开发的，如HttpLite等。但无论你选择哪一个，本质并无变化。不过，如果你想深入底层学习，开始时请尽可能的不要使用，封装程度太高，学到的越少。后期有经验之后，自己尝试造轮子呗。


### 为什么采用Glide？
目前网络加载框架也是繁多，目前常用的有以下几种：ImageLoader（2011年），Picasso（2013年），Glide（2012年），Fresco（2015年）四种。其中ImageLoader出现的最早也应用的最为广泛。早期出现的ImageLoader首要关注的是如何尽快的加载图片，然后需要自己动手处理图片防止内存溢出。后面，大家觉得很烦啊，于是一些即注重加载速度，又减少内存溢出的网络加载框架就出现了，就像后三种。（我在一些群里曾听到一些开发者说“ImageLoader不行啊，经常造成内存溢出啊，Fresco就不会，所以ImageLoader已经过时了”之类的话，虽然我个人能力也比较一般，但是当遇到问题的时候我首先想的是“我是不是忘记处理什么？是不是我的能力有问题，而不是质疑框架，毕竟框架是死的，人是活的”）



这些加载库各有优缺点，需要自行调研之后根据业务选择，这里我选择了加载速度较快，体积更小的Glide作为网络加载库。


**这里我们对ImageLoader，Glide、Picasso、Fresco做个简单的对比：**
**ImageLoader**虽然已经停止维护了，但是仍然比较好用，图片加载不多的场景下推荐使用。

**Glide**在**Picasso**基础上二次开发而来，沿袭了Picasso以往的简洁风格，并做了大量优化。
|Glide Vs Picasso|Glide|Picasso|
|----|-----|-------|
|图片格式|默认RGB_565，支持动态图|默认RGB_ARGB_8888，不支持动态图|
|内存占用|默认情况下比Picasso小一半|
|磁盘缓存|根据ImageView的大小来缓存相应的图片|只缓存原始尺寸的图片|
|包体积|比Picasso大|
|加载速度|大部分情况下比Picasso快|

**Fresco**重点关注图片加载导致的内存溢出问题。和传统做法不一致，Fresco将图片资源放在虚拟机管理内存之外的Native堆当中，因此该部分内存不占用App内存。但是由于虚拟机无法管理该区域，因此Fresco自行实现了图片资源的回收机制。由于是放在Native堆当中的，因此这部分是用C++来写的，因此阅读难度相对较大，另外Fresco这个库也相对较大，会增加2M左右的体积。在这个流量不值钱的年代，增加的这点体积好像也算不了什么？

***总结，如果你喜欢偷懒，又不擅长处理内存溢出问题，而且你的应用大量使用图片的场景下，推荐使用Fresco。其他场景下，优先选择Glide吧。***


### 为什么使用Realm？
对Android开发者而言，Sqlite再熟悉不过了，但是你会发现Sqlite是面向结构式的语言而并非面向对象式的。而Realm则是一种面向对象的数据库，因此你无须再编写sql语句就可以将对象存储到数据库当中。如果你的项目是一个新项目，而你又是面向对象的坚定主义者，那么使用Realm是你不二的选择。如果你是在改造老项目，而且项目又比较大，请不要使用Realm,否则会成为你的噩梦.

关于Realm更多信息，直接参考官网即可。

--------------

Version
---------

###V 0.0.1

1.提交第一版

--------------------




  [1]: https://github.com/closedevice/FastApp/issues
  [2]: closedevice@gmail.com
  [3]: http://blog.csdn.net/dd864140130
  [4]: https://raw.githubusercontent.com/closedevice/FastApp/master/screenshots/appstart.png
  [5]: https://raw.githubusercontent.com/closedevice/FastApp/master/screenshots/gan.png
  [6]: https://raw.githubusercontent.com/closedevice/FastApp/master/screenshots/wx.png
  [7]: https://raw.githubusercontent.com/closedevice/FastApp/master/screenshots/settingspng.png
  [8]: http://fir.im/l7ta