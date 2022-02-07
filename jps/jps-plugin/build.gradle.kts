plugins {
    kotlin("jvm")
    id("jps-compatible")
}

val compilerModules: Array<String> by rootProject.extra

val generateTests by generator("org.jetbrains.kotlin.jps.GenerateJpsPluginTestsKt") {
    javaLauncher.set(
        javaToolchains.launcherFor {
            languageVersion.set(JavaLanguageVersion.of(11))
        }
    )
}

dependencies {
    compile(project(":kotlin-build-common"))
    compile(project(":core:descriptors"))
    compile(project(":core:descriptors.jvm"))
    compile(project(":kotlin-compiler-runner"))
    compile(project(":daemon-common"))
    compile(project(":daemon-common-new"))
    compile(projectRuntimeJar(":kotlin-daemon-client"))
    compile(projectRuntimeJar(":kotlin-daemon"))
    testImplementation(projectTests(":generators:test-generator")) // TODO FIX ME
    testCompile(projectTests(":generators:test-generator"))
    compile(project(":compiler:frontend.java"))
    compile(project(":js:js.frontend"))
    compile(projectRuntimeJar(":kotlin-preloader"))
    compile(project(":jps:jps-common"))
    compileOnly("org.jetbrains.intellij.deps:asm-all:9.1")
    compileOnly(intellijDep()) {
        includeJars("jdom", "trove4j", "jps-model", "platform-api", "util", rootProject = rootProject)
    }
    compileOnly(jpsStandalone()) { includeJars("jps-builders", "jps-builders-6") }
    testCompileOnly(project(":kotlin-reflect-api"))
    testCompile(project(":compiler:incremental-compilation-impl"))
    testCompile(projectTests(":compiler:tests-common"))
    testCompile(projectTests(":compiler:incremental-compilation-impl"))
    testCompile(commonDep("junit:junit"))
    testCompile(project(":kotlin-test:kotlin-test-jvm"))
    testCompile(projectTests(":kotlin-build-common"))
    testApi(projectTests(":compiler:test-infrastructure-utils"))
    testCompileOnly(jpsStandalone()) { includeJars("jps-builders", "jps-builders-6") }
    Ide.IJ {
        testCompile(intellijDep("devkit"))
    }

    testCompile(intellijDep())

    testCompile(jpsBuildTest())
    compilerModules.forEach {
        testRuntime(project(it))
    }

    testRuntimeOnly(intellijPluginDep("java"))

    testRuntimeOnly(toolsJar())
    testRuntime(project(":kotlin-reflect"))
    testRuntime(project(":kotlin-script-runtime"))
}

sourceSets {
    "main" {
        projectDefault()
        resources.srcDir("resources-en")
    }
    "test" {
        Ide.IJ {
            java.srcDirs("jps-tests/test")
        }
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

tasks.compileJava {
    sourceCompatibility = "1.8"
    targetCompatibility = "1.8"
}

tasks.compileKotlin {
    kotlinOptions.jvmTarget = "1.8"
    sourceCompatibility = "1.8"
    targetCompatibility = "1.8"
}

projectTest(parallel = true) {
    // do not replace with compile/runtime dependency,
    // because it forces Intellij reindexing after each compiler change
    dependsOn(":kotlin-compiler:dist")
    dependsOn(":kotlin-stdlib-js-ir:packFullRuntimeKLib")
    workingDir = rootDir
}

testsJar {}
