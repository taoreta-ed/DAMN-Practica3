package com.example.damn_practica3

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var fileAdapter: FileAdapter // Necesitarás crear este adaptador
    private val STORAGE_PERMISSION_REQUEST_CODE = 100

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permiso concedido, puedes proceder a listar archivos
            Toast.makeText(this, "Permiso de almacenamiento concedido", Toast.LENGTH_SHORT).show()
            listFilesInDirectory(Environment.getExternalStorageDirectory())
        } else {
            // Permiso denegado, informa al usuario y maneja la situación
            Toast.makeText(this, "Permiso de almacenamiento denegado. No se pueden explorar archivos.", Toast.LENGTH_LONG).show()
            // Podrías deshabilitar funcionalidades o mostrar un mensaje de error
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerViewFiles)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Verificar y solicitar permisos al inicio
        if (checkStoragePermissions()) {
            listFilesInDirectory(Environment.getExternalStorageDirectory())
        } else {
            requestStoragePermissions()
        }
    }

    private fun checkStoragePermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestStoragePermissions() {
        // Para Android 6.0 (API 23) y superiores, necesitamos solicitar permisos en tiempo de ejecución.
        // Android 8 está en este rango.
        requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        // También puedes solicitar ambos permisos en un arreglo si lo deseas,
        // pero RequestPermission() es para un solo permiso.
        // Para múltiples: ActivityResultContracts.RequestMultiplePermissions()
    }

    // Esta función listará los archivos en un directorio dado
    private fun listFilesInDirectory(directory: File) {
        if (!directory.exists()) {
            Toast.makeText(this, "El directorio no existe: ${directory.absolutePath}", Toast.LENGTH_SHORT).show()
            return
        }

        if (!directory.canRead()) {
            Toast.makeText(this, "No se puede leer el directorio: ${directory.absolutePath}", Toast.LENGTH_SHORT).show()
            return
        }

        val files = directory.listFiles()
        if (files == null) {
            Toast.makeText(this, "No se pudieron obtener los archivos del directorio: ${directory.absolutePath}", Toast.LENGTH_SHORT).show()
            return
        }

        // Aquí pasarías la lista de archivos a tu adaptador de RecyclerView
        fileAdapter = FileAdapter(files.toList()) { file ->
            // Manejar clic en archivo/carpeta
            if (file.isDirectory) {
                listFilesInDirectory(file) // Navegar a la carpeta
            } else {
                // Abrir archivo (implementar lógica de apertura/visualización)
                Toast.makeText(this, "Archivo seleccionado: ${file.name}", Toast.LENGTH_SHORT).show()
            }
        }
        recyclerView.adapter = fileAdapter
    }
}