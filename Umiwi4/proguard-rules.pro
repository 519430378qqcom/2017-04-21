# This is a configuration file for ProGuard.
# http://proguard.sourceforge.net/index.html#manual/usage.html

-dontshrink

-flattenpackagehierarchy
-allowaccessmodification 
-printmapping map.txt 

-optimizationpasses 7 
-keepattributes Exceptions
-dontskipnonpubliclibraryclassmembers 
-ignorewarnings

-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose
-dontwarn
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

# Optimization is turned off by default. Dex does not like code run
# through the ProGuard optimize and preverify steps (and performs some
# of these optimizations on its own).
-dontoptimize
-dontpreverify
# Note that if you want to enable optimization, you cannot just
# include optimization flags in your own project configuration file;
# instead you will need to point to the
# "proguard-android-optimize.txt" file instead of this one from your
# project.properties file.

-keepattributes *Annotation*
-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService

# For native methods, see http://proguard.sourceforge.net/manual/examples.html#native
-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# keep setters in Views so that animations can still work.
# see http://proguard.sourceforge.net/manual/examples.html#beans
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

# We want to keep methods in Activity that could be used in the XML attribute onClick
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

# For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}


# keep source line
-keepattributes InnerClasses,SourceFile,LineNumberTable

-keepnames class * implements java.io.Serializable
-keepnames class * implements com.umiwi.ui.beans.BaseGsonBeans
# serialVersionUID remain unchanged
-keepclassmembers class * implements java.io.Serializable {
   static final long serialVersionUID;
   private static final java.io.ObjectStreamField[] serialPersistentFields;
   !static !transient <fields>;
   private void writeObject(java.io.ObjectOutputStream);
   private void readObject(java.io.ObjectInputStream);
   java.lang.Object writeReplace();
   java.lang.Object readResolve();
}

# The support library contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version.  We know about them, and they are safe.
-dontwarn android.support.**

-renamesourcefileattribute SourceFile

-keep class com.umiwi.ui.beans.** { *; }
-keep class com.umiwi.ui.managers.StatisticsUrl
-keep class com.umiwi.ui.http.parsers.**{*;}
-keep class com.umiwi.ui.model.**{*;}
-keep class com.umiwi.ui.view.**{*;}
-keep class com.umiwi.ui.zxing.**{*;}

-keep class com.umiwi.ui.event.WebToNativeEvent
-keepclassmembers class com.umiwi.ui.fragment.WebFragment$JSIntefaceWebToNative{ *; }

#okhttputils
-dontwarn com.zhy.http.**
-keep class com.zhy.http.**{*;}


#okhttp
-dontwarn okhttp3.**
-keep class okhttp3.**{*;}


#okio
-dontwarn okio.**
-keep class okio.**{*;}

#IM
-dontwarn com.netease.**
-dontwarn io.netty.**
-keep class com.netease.** {*;}
#如果 netty 使用的官方版本，它中间用到了反射，因此需要 keep。如果使用的是我们提供的版本，则不需要 keep
-keep class io.netty.** {*;}

##如果你使用全文检索插件，需要加入
#-dontwarn org.apache.lucene.**
#-keep class org.apache.lucene.** {*;}
