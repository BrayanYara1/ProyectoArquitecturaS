# Reglas de ProGuard optimizadas para Salud Activa

# 1. Modelos de Datos (GSON)
# Es vital mantener los nombres de los campos en las clases de datos porque
# GSON los usa para la serialización/deserialización con la API.
-keep class com.example.gestionturnosapp.data.** { *; }

# 2. Componentes de Arquitectura
# Mantener Activities y Fragments para que el sistema y Navigation los puedan instanciar
-keep public class * extends android.app.Activity
-keep public class * extends androidx.fragment.app.Fragment
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider

# Mantener constructores de ViewModels para ViewModelProvider
-keepclassmembers class * extends androidx.lifecycle.ViewModel {
    public <init>(...);
}

# 3. ViewBinding
-keep class com.example.gestionturnosapp.databinding.** { *; }

# 4. Networking (Retrofit/OkHttp)
# Se mantienen las firmas de métodos y anotaciones necesarias para la reflexión
-keepattributes Signature, Exceptions, *Annotation*, EnclosingMethod, InnerClasses
-keep class retrofit2.** { *; }
-keep class okhttp3.** { *; }
-dontwarn retrofit2.**
-dontwarn okhttp3.**
-dontwarn okio.**

# MANTENER INTERFACES DE API (Fundamental para Retrofit)
-keep interface com.example.gestionturnosapp.data.remote.** { *; }
# Mantener tipos genéricos en los métodos de la API
-keepclassmembers interface com.example.gestionturnosapp.data.remote.** {
    <methods>;
}

# 5. Firebase & Crashlytics
# Reglas necesarias para reportes de errores precisos
-keepattributes SourceFile, LineNumberTable
-keep public class com.google.firebase.provider.FirebaseInitProvider
-keep class com.google.firebase.** { *; }

# 6. Seguridad (SQLCipher y EncryptedSharedPreferences)
-keep class androidx.security.crypto.** { *; }
-keep class net.zetetic.database.** { *; }
-keep class net.zetetic.database.sqlcipher.** { *; }
-dontwarn net.zetetic.**

# 7. Hilt / Dagger
-keep class **_HiltModules* { *; }
-keep class **_HiltAndroidApp* { *; }
-keep class **_HiltActivity* { *; }
-keep class **_HiltFragment* { *; }
-keep class **_HiltViewModel* { *; }
-keep @dagger.hilt.android.lifecycle.HiltViewModel class *
-keep @dagger.hilt.EntryPoint class *
-keep class dagger.hilt.android.internal.managers.** { *; }

# 8. Splash Screen y UI Libraries
-keep class androidx.core.splashscreen.** { *; }
-dontwarn androidx.core.splashscreen.**
-keep class com.facebook.shimmer.** { *; }
-keep class com.airbnb.lottie.** { *; }

# 9. Room y SQLite
-keep class * extends androidx.room.RoomDatabase
-keep class * extends androidx.room.Entity
-keep class * extends androidx.room.Dao
-keep class androidx.sqlite.db.** { *; }
-keep class androidx.room.paging.** { *; }
-dontwarn androidx.room.**
-dontwarn androidx.sqlite.db.**
