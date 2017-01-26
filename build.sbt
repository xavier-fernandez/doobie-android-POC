name := "doobie_android"

enablePlugins(AndroidApp)

version := "0.0.1"

resolvers += "jcenter" at "http://jcenter.bintray.com"
resolvers += "jitpack.io" at "https://jitpack.io"

javacOptions ++= Seq("-source", "1.7", "-target", "1.7")
scalaVersion := "2.11.8"
proguardCache in Android := Seq.empty
platformTarget in Android := "android-24"
scalacOptions in Compile += "-feature"

updateCheck in Android := {} // disable update check

proguardOptions in Android ++= Seq(
  "-ignorewarnings",
  "-dontobfuscate",
  "-dontoptimize",
  "-keepattributes Signature",
  "-printseeds target/seeds.txt",
  "-printusage target/usage.txt",
  "-dontwarn scala.collection.**",
  "-dontwarn org.scaloid.**",
  "-keep class scala.Dynamic",
  "-keep class scala.concurrent.ExecutionContext",
  "-keep class com.fortysevendeg.mvessel.AndroidDriver",
  "-keep class slick.driver.SQLiteDriver",
  "-dontwarn rx.internal.util.**",
  "-dontwarn sun.misc.Unsafe"
)

proguardScala in Android := true

useProguard in Android := true

dexMulti in Android := true

dexMinimizeMain in Android := true

dexMainClasses in Android := Seq(
  "com/example/app/MultidexApplication.class",
  "android/support/multidex/BuildConfig.class",
  "android/support/multidex/MultiDex$V14.class",
  "android/support/multidex/MultiDex$V19.class",
  "android/support/multidex/MultiDex$V4.class",
  "android/support/multidex/MultiDex.class",
  "android/support/multidex/MultiDexApplication.class",
  "android/support/multidex/MultiDexExtractor$1.class",
  "android/support/multidex/MultiDexExtractor.class",
  "android/support/multidex/ZipUtil$CentralDirectory.class",
  "android/support/multidex/ZipUtil.class"
)

dexMaxHeap in Android := "2048m"

packagingOptions in Android := PackagingOptions(
  excludes = Seq(
    "META-INF/MANIFEST.MF",
    "META-INF/LICENSE.txt",
    "META-INF/LICENSE",
    "META-INF/NOTICE.txt",
    "META-INF/NOTICE"
  )
)

libraryDependencies ++= Seq(
  aar("org.macroid" %% "macroid" % "2.0"),
  "org.typelevel" %% "cats" % "0.9.0",
  "com.fortysevendeg" % "macroid-extras_2.11" % "0.3",
  "com.android.support" % "support-v4" % "24.1.1",
  "com.android.support" % "design" % "24.1.1",
  "com.android.support" % "multidex" % "1.0.0",
  "com.android.support" % "appcompat-v7" % "24.1.1",
  "org.tpolecat" %% "doobie-core-cats" % "0.4.1",
  "com.fortysevendeg" %% "mvessel-android" % "0.1",
  "org.slf4j" % "slf4j-nop" % "1.7.21",
  "org.joda" % "joda-convert" % "1.8.1",
  "io.circe" %% "circe-core" % "0.7.0",
  "io.circe" %% "circe-generic" % "0.7.0",
  "io.circe" %% "circe-parser" % "0.7.0"
)

run <<= run in Android
packageRelease <<= packageRelease in Android
install <<= install in Android

proguardScala in Android := true
useProguard in Android := true

// Tests //////////////////////////////

libraryDependencies ++= Seq(
  "org.apache.maven" % "maven-ant-tasks" % "2.1.3" % "test",
  "org.robolectric" % "shadows-multidex" % "3.1.4" % "test",
  "org.robolectric" % "robolectric" % "3.1.4" % "test",
  "com.novocode" % "junit-interface" % "0.11" % "test",
  "com.geteit" %% "robotest" % "0.12" % "test",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)

scalacOptions in Test ++= Seq("-Yrangepos")
parallelExecution in Test := false
fork in Test := true
javaOptions in Test ++= Seq("-XX:MaxPermSize=2048M", "-XX:+CMSClassUnloadingEnabled")

// without this, @Config throws an exception,
unmanagedClasspath in Test ++= (bootClasspath in Android).value
