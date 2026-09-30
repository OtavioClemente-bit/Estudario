import com.android.build.api.dsl.ApplicationExtension
import java.util.Base64
import java.util.Properties
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("org.jetbrains.kotlin.kapt")
}

fun String.toBuildConfigLiteral(): String =
    "\"${replace("\\", "\\\\").replace("\"", "\\\"").replace("\r", "\\r").replace("\n", "\\n")}\""

fun requireClientSafeSupabaseValue(name: String, value: String) {
    val normalized = value.lowercase()
    check(listOf("service_role", "service-role", "servicerole", "service role", "openai", "sk-", "sb_secret_").none(normalized::contains)) {
        "$name must contain only client-safe Supabase configuration."
    }
}

fun isClientSafeSupabasePublishableKey(value: String): Boolean {
    if (Regex("sb_publishable_[A-Za-z0-9_-]{16,}").matches(value)) return true
    val segments = value.split('.')
    if (segments.size != 3 || segments.any { !Regex("[A-Za-z0-9_-]+").matches(it) }) return false
    val payload = runCatching {
        Base64.getUrlDecoder().decode(segments[1]).toString(Charsets.UTF_8)
    }.getOrNull() ?: return false
    val role = Regex("\\\"role\\\"\\s*:\\s*\\\"([^\\\"]+)\\\"")
        .find(payload)
        ?.groupValues
        ?.getOrNull(1)
    val ref = Regex("\\\"ref\\\"\\s*:\\s*\\\"([^\\\"]+)\\\"")
        .find(payload)
        ?.groupValues
        ?.getOrNull(1)
    return role == "anon" && !ref.isNullOrBlank()
}

val supabaseUrl = providers.gradleProperty("estudario.supabase.url").orNull.orEmpty().trim()
val supabasePublishableKey = providers.gradleProperty("estudario.supabase.publishableKey").orNull.orEmpty().trim()
check(supabaseUrl.isEmpty() == supabasePublishableKey.isEmpty()) {
    "Both client-safe Supabase URL and publishable key must be supplied together; the configuration gate remains closed."
}
requireClientSafeSupabaseValue("Supabase URL", supabaseUrl)
requireClientSafeSupabaseValue("Supabase publishable key", supabasePublishableKey)
check(supabasePublishableKey.isEmpty() || isClientSafeSupabasePublishableKey(supabasePublishableKey)) {
    "Supabase publishable key must match the client-safe publishable or legacy anon shape."
}
val forbiddenAndroidSecretProperty = listOf(
    "OPENAI_API_KEY",
    "openai_api_key",
    "openai.apiKey",
    "SUPABASE_SERVICE_ROLE_KEY",
    "supabase.serviceRoleKey",
    "supabase_service_role_key",
).firstOrNull { providers.gradleProperty(it).orNull?.isNotBlank() == true }
check(forbiddenAndroidSecretProperty == null) {
    "Server-only service-role/OpenAI properties are not allowed in Android configuration."
}

// ID do cliente OAuth "Aplicativo da Web" do Google Cloud, usado no login com Google (não é segredo).
val googleWebClientId = providers.gradleProperty("estudario.google.webClientId").orNull.orEmpty().trim()

// Número do projeto do Google Cloud vinculado ao app no Play Console (não é segredo).
val playIntegrityCloudProjectNumber = providers.gradleProperty("estudario.playIntegrity.cloudProjectNumber").orNull.orEmpty().trim()
check(playIntegrityCloudProjectNumber.isEmpty() || playIntegrityCloudProjectNumber.all(Char::isDigit)) {
    "estudario.playIntegrity.cloudProjectNumber must contain only digits."
}

val signingProperties = Properties()
val propertiesFile = rootProject.file("keystore.properties")
if (propertiesFile.exists()) {
    propertiesFile.inputStream().use { signingProperties.load(it) }
}

extensions.configure<ApplicationExtension> {
    namespace = "br.com.estudario"
    compileSdk = 36

    defaultConfig {
        applicationId = "br.com.estudario"
        minSdk = 26
        targetSdk = 36
        versionCode = 27
        versionName = "3.2.2"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
        buildConfigField("String", "SUPABASE_URL", supabaseUrl.toBuildConfigLiteral())
        buildConfigField("String", "SUPABASE_PUBLISHABLE_KEY", supabasePublishableKey.toBuildConfigLiteral())
        buildConfigField("String", "GOOGLE_WEB_CLIENT_ID", googleWebClientId.toBuildConfigLiteral())
        buildConfigField("long", "PLAY_INTEGRITY_CLOUD_PROJECT_NUMBER", "${playIntegrityCloudProjectNumber.ifEmpty { "0" }}L")
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    sourceSets.getByName("androidTest").assets.directories.add("$projectDir/schemas")

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    signingConfigs {
        create("release") {
            storeFile = signingProperties.getProperty("storeFile")?.let { rootProject.file(it) }
            storePassword = signingProperties.getProperty("storePassword")
            keyAlias = signingProperties.getProperty("keyAlias")
            keyPassword = signingProperties.getProperty("keyPassword")
        }
    }

    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
        }
        // "Estudário Teste": instala ao lado da versão da Play Store (outro pacote e outro nome),
        // para testar no celular sem desinstalar e sem perder os dados do app oficial.
        create("preview") {
            initWith(getByName("debug"))
            applicationIdSuffix = ".teste"
            resValue("string", "app_name", "Estudário Teste")
            matchingFallbacks += listOf("debug")
        }
    }

    packaging.resources.excludes += setOf("/META-INF/{AL2.0,LGPL2.1}")
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

dependencies {
    implementation(platform("androidx.compose:compose-bom:2025.08.01"))
    androidTestImplementation(platform("androidx.compose:compose-bom:2025.08.01"))

    implementation("androidx.core:core-ktx:1.17.0")
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("androidx.activity:activity-compose:1.10.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.9.2")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.9.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.2")
    implementation("androidx.navigation:navigation-compose:2.9.3")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    debugImplementation("androidx.compose.ui:ui-tooling")

    implementation("androidx.room:room-runtime:2.7.2")
    implementation("androidx.room:room-ktx:2.7.2")
    kapt("androidx.room:room-compiler:2.7.2")
    implementation("androidx.datastore:datastore-preferences:1.1.7")
    implementation("androidx.work:work-runtime-ktx:2.11.2")

    // Jetpack Glance (Widgets)
    implementation("androidx.glance:glance-appwidget:1.1.1")
    implementation("androidx.glance:glance-material3:1.1.1")

    // Conta Google + pasta privada do app no Drive (o backup fala com a API REST por HTTPS,
    // sem a biblioteca cliente inteira do Drive).
    implementation("com.google.android.gms:play-services-auth:21.3.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.10.2")
    implementation("com.google.android.play:integrity:1.4.0")
    // Login com a conta Google do celular (Credential Manager).
    implementation("androidx.credentials:credentials:1.3.0")
    implementation("androidx.credentials:credentials-play-services-auth:1.3.0")
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")
    // Texto do PDF do edital lido no próprio celular, para a IA receber só o conteúdo programático.
    implementation("com.tom-roush:pdfbox-android:2.0.27.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")

    // Leitura de estudo: Markdown completo (tabelas, listas, código) e fórmulas LaTeX.
    implementation("io.noties.markwon:core:4.6.2")
    implementation("io.noties.markwon:ext-tables:4.6.2")
    implementation("io.noties.markwon:ext-strikethrough:4.6.2")
    implementation("io.noties.markwon:ext-latex:4.6.2")
    implementation("io.noties.markwon:inline-parser:4.6.2")

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")
    testImplementation("org.json:json:20250517")
    androidTestImplementation("androidx.test.ext:junit:1.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")
    androidTestImplementation("androidx.room:room-testing:2.7.2")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}

kapt {
    correctErrorTypes = true
    arguments { arg("room.schemaLocation", "$projectDir/schemas") }
}
