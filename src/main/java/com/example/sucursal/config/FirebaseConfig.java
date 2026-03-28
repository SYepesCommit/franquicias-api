package com.example.sucursal.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;

@Configuration
public class FirebaseConfig {

    @Bean
    public Firestore getFirestore() throws IOException {
        // Verificamos si ya existe una instancia para evitar el error "already exists"
        if (FirebaseApp.getApps().isEmpty()) {
            
            // 1. Intentamos leer la ruta desde una variable de entorno (Ideal para Render)
            // Si no existe, usamos el nombre por defecto "./service-account.json"
            String path = System.getenv("FIREBASE_CONFIG_PATH");
            if (path == null) path = "./service-account.json";

            try (FileInputStream serviceAccount = new FileInputStream(path)) {
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();

                FirebaseApp.initializeApp(options);
            } catch (IOException e) {
                System.err.println("CRÍTICO: No se pudo encontrar el archivo de credenciales en: " + path);
                throw e;
            }
        }
        return FirestoreClient.getFirestore();
    }
}