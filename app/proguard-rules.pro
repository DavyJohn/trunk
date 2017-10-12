# 指定代码的压缩级别
-optimizationpasses 5

# 包明不混合大小写
-dontusemixedcaseclassnames

# 不去忽略非公共的库类
-dontskipnonpubliclibraryclasses

 # 优化不优化输入的类文件
-dontoptimize

 # 预校验
-dontpreverify

 # 混淆时是否记录日志
-verbose

 # 混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

# 保护注解
-keepattributes *Annotation*

# 保持哪些类不被混淆
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keep public class com.android.Context


-keep public class com.jakewharton.*
-keep public class com.squareup.picasso.*
-keep class android.net.SSLCertificateSocketFactory
-keep public class com.nineoldandroids.*
-keep public class com.daimajia.slider.*
-dontwarn com.squareup.okhttp.**
-keep public class com.squareup.okhttp3.*{*;}
-keep public class com.google.code.gson.*
-keep public class dmax.dialog.**{*;}
-keep public class cn.qqtheme.framework.*
-keep public class com.avast.*
-keep public class com.orhanobut.*
-keep public class com.bartoszlipinski.recyclerviewheader.*
-keep public class com.zhy.*
-keep public class com.huxq17.xrefreshview.*
-keep public class com.kejiwen.securitykeyboard.*
-keep public class com.lsjwzh.*
-keep public class com.zhailr.caipiao.widget.*
-keep public class com.zhailr.caipiao.model.response.*{*;}
-keep public class com.zhailr.caipiao.utils.Constant
-keep public class dmax.dialog.*{*;}
# 如果有引用v4包可以添加下面这行
-keep public class android.support.**



# 忽略警告
-ignorewarning

#记录生成的日志数据,gradle build时在本项目根目录输出 

# apk 包内所有 class 的内部结构
-dump class_files.txt
# 未混淆的类和成员
-printseeds seeds.txt
# 列出从 apk 中删除的代码
-printusage unused.txt
# 混淆前后的映射
-printmapping mapping.txt

#记录生成的日志数据，gradle build时 在本项目根目录输出-end


#####混淆保护自己项目的部分代码以及引用的第三方jar包library

#-libraryjars libs/umeng-analytics-v5.2.4.jar

#三星应用市场需要添加:sdk-v1.0.0.jar,look-v1.0.1.jar
#-libraryjars libs/sdk-v1.0.0.jar
#-libraryjars libs/look-v1.0.1.jar

#如果不想混淆 keep 掉
-keep class com.lippi.recorder.iirfilterdesigner.** {*; }
#友盟
-keep class com.umeng.**{*;}
#项目特殊处理代码

#忽略警告
-dontwarn com.lippi.recorder.utils**
#保留一个完整的包
-keep class com.lippi.recorder.utils.** {
    *;
 }

-keep class  com.lippi.recorder.utils.AudioRecorder{*;}


#如果引用了v4或者v7包
-dontwarn android.support.**

#混淆保护自己项目的部分代码以及引用的第三方jar包library-end

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

#保持 native 方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

#保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

#保持自定义控件类不被混淆
-keepclassmembers class * extends android.support.v7.app.AppCompatActivity {
   public void *(android.view.View);
}

#保持 Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

#保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable

#保持 Serializable 不被混淆并且enum 类也不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}


#保持枚举 enum 类不被混淆 如果混淆报错，建议直接使用上面的 -keepclassmembers class * implements java.io.Serializable即可
#-keepclassmembers enum * {
#  public static **[] values();
#  public static ** valueOf(java.lang.String);
#

-keepclassmembers class * {
    public void *ButtonClicked(android.view.View);
}

#不混淆资源类
-keep class **.R$* { *; }

#避免混淆泛型 如果混淆报错建议关掉
#–keepattributes Signature

#移除log 测试了下没有用还是建议自己定义一个开关控制是否输出日志
#-assumenosideeffects class android.util.Log {
#    public static boolean isLoggable(java.lang.String, int);
#    public static int v(...);
#    public static int i(...);
#    public static int w(...);
#    public static int d(...);
#    public static int e(...);
#

#如果用用到Gson解析包的，直接添加下面这几行就能成功混淆，不然会报错。
#gson
#-libraryjars libs/gson-2.2.2.jar
-keepattributes Signature
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-libraryjars libs/commons-logging-1.1.1.jar    #忽略jar包
-dontwarn org.apache.**                        #不警告此包
-keep class org.apache.** {*;}                 #保留此包下代码不进行混淆
 -keepattributes Signature
-keepattributes *Annotation*

#不混淆butterknife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}
-dontwarn android.net.**
-keep class android.net.SSLCertificateSocketFactory{*;}

#gson
# -libraryjars libs/gson-2.2.2.jar
-keepattributes EnclosingMethod
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.JsonObject { *; }
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }
#okhttp
-dontwarn okhttp3.**
-keep class okhttp3.**{*;}
-keep interface okhttp3.**{*;}
#okio
-dontwarn okio.**
-keep class okio.**{*;}
-keep interface okio.**{*;}

#pgy
#-libraryjars libs/pgyer_sdk_x.x.jar
#-dontwarn com.pgyersdk.**
#-keep class com.pgyersdk.** { *; }

#jp
-dontoptimize
-dontpreverify

-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
