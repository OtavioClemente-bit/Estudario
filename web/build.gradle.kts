plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("org.jetbrains.compose")
}

kotlin {
    js {
        outputModuleName.set("estudario")
        browser {
            commonWebpackConfig { outputFileName = "estudario.js" }
            // Testes da lógica de dados rodam no Node (sem navegador).
            testTask { enabled = false }
        }
        nodejs()
        binaries.executable()
    }

    compilerOptions {
        optIn.add("kotlin.time.ExperimentalTime")
    }

    sourceSets {
        jsTest.dependencies {
            implementation(kotlin("test"))
        }
        jsMain.dependencies {
            implementation(project(":shared"))
            implementation(compose.runtime)
            implementation(compose.html.core)
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
        }
    }
}

// Chaves públicas (as mesmas que vão no APK): vêm do ~/.gradle/gradle.properties ou, no deploy,
// de variáveis de ambiente. Nada secreto entra aqui.
val webConfigDir = layout.buildDirectory.dir("generated/webConfig")
val generateWebConfig by tasks.registering {
    val url = providers.gradleProperty("estudario.supabase.url").filter { it.isNotBlank() }.orElse(providers.environmentVariable("ESTUDARIO_SUPABASE_URL")).orElse("")
    val key = providers.gradleProperty("estudario.supabase.publishableKey").filter { it.isNotBlank() }.orElse(providers.environmentVariable("ESTUDARIO_SUPABASE_PUBLISHABLE_KEY")).orElse("")
    val google = providers.gradleProperty("estudario.google.webClientId").filter { it.isNotBlank() }.orElse(providers.environmentVariable("ESTUDARIO_GOOGLE_WEB_CLIENT_ID")).orElse("")
    inputs.property("url", url)
    inputs.property("key", key)
    inputs.property("google", google)
    outputs.dir(webConfigDir)
    doLast {
        val forbidden = listOf("service_role", "sb_secret_", "sk-")
        check(forbidden.none { key.get().contains(it) }) { "Só a chave publicável do Supabase pode ir para o app web." }
        fun lit(value: String) = "\"" + value.replace("\\", "\\\\").replace("\"", "\\\"") + "\""
        val file = webConfigDir.get().file("br/com/estudario/web/WebConfig.kt").asFile
        file.parentFile.mkdirs()
        file.writeText(
            """
            |package br.com.estudario.web
            |
            |object WebConfig {
            |    const val SUPABASE_URL = ${lit(url.get().trim().trimEnd('/'))}
            |    const val SUPABASE_KEY = ${lit(key.get().trim())}
            |    const val GOOGLE_CLIENT_ID = ${lit(google.get().trim())}
            |}
            |""".trimMargin(),
        )
    }
}
kotlin.sourceSets.named("jsMain") { kotlin.srcDir(generateWebConfig) }
