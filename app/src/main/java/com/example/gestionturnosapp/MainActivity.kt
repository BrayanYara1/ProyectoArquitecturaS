package com.example.gestionturnosapp

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            android.util.Log.d("MainActivity", "Permiso de notificaciones concedido")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // Habilitar diseño de borde a borde para un estilo moderno
        androidx.core.view.WindowCompat.setDecorFitsSystemWindows(window, false)

        // Inicializar el Splash Screen antes de super.onCreate()
        try {
            installSplashScreen()
        } catch (e: Exception) {
            android.util.Log.e("MainActivity", "Error installing splash screen", e)
        }
        
        super.onCreate(savedInstanceState)
        
        // Bloquear capturas si el modo privacidad está activo
        if (com.example.gestionturnosapp.data.local.PreferenceManager.isPrivacyModeEnabled(this)) {
            window.setFlags(
                android.view.WindowManager.LayoutParams.FLAG_SECURE,
                android.view.WindowManager.LayoutParams.FLAG_SECURE
            )
        }

        try {
            setContentView(R.layout.activity_main)
            
            val navHostFragment = supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment) as? NavHostFragment
            
            if (navHostFragment == null) {
                android.util.Log.e("MainActivity", "NavHostFragment not found!")
                return
            }

            val navController = navHostFragment.navController
            val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
            
            bottomNav?.setupWithNavController(navController)
            navController.addOnDestinationChangedListener { _, destination, _ ->
                bottomNav?.visibility = when (destination.id) {
                    R.id.homeFragment, 
                    R.id.turnosListFragment, 
                    R.id.userProfileFragment, 
                    R.id.especialidadesFragment -> View.VISIBLE
                    else -> View.GONE
                }
            }
            
            intent?.let { handleIntent(it) }
            
        } catch (t: Throwable) {
            android.util.Log.e("MainActivity", "Fatal error in onCreate", t)
        }
    }

    override fun onStart() {
        super.onStart()
        checkNotificationPermission()
    }

    override fun onNewIntent(intent: android.content.Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: android.content.Intent) {
        val navigateTo = intent.getStringExtra("NAVIGATE_TO")
        val type = intent.getStringExtra("TYPE")
        
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as? NavHostFragment
        val navController = navHostFragment?.navController

        when {
            (navigateTo == "TURNO_DETAIL") || (type == "TURNO") -> {
                val turnoId = intent.getStringExtra("TURNO_ID")
                val bundle = Bundle().apply {
                    putString("TURNO_ID", turnoId)
                    putBoolean("FROM_NOTIFICATION", true)
                }
                
                window.decorView.post {
                    try {
                        navController?.navigate(R.id.turnoDetailFragment, bundle)
                    } catch (e: Exception) {
                        android.util.Log.e("MainActivity", "Navigation to detail failed", e)
                    }
                }
            }
            navigateTo == "SOLICITAR_TURNO" -> {
                window.decorView.post {
                    try {
                        navController?.navigate(R.id.solicitarTurnoFragment)
                    } catch (e: Exception) {
                        android.util.Log.e("MainActivity", "Navigation to schedule failed", e)
                    }
                }
            }
        }
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (androidx.core.content.ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != android.content.pm.PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}
