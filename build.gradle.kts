plugins {
    id("org.springframework.boot") version "2.4.4"
    id ("io.spring.dependency-management") version "1.0.11.RELEASE"
    java
    kotlin("jvm") version "1.4.32"
    kotlin("plugin.spring") version "1.4.32"
    kotlin("plugin.allopen") version "1.4.32"
    kotlin("kapt") version "1.4.32"
}

repositories {
    mavenCentral()
}
subprojects {

    group = "io.reflectoring.reviewapp"
    version = "0.0.1-SNAPSHOT"

    apply (plugin= "java")
    apply (plugin= "io.spring.dependency-management")
    apply(plugin="org.springframework.boot")
    apply (plugin= "java-library")
    apply (plugin= "kotlin")


    repositories {
        jcenter()
        mavenCentral()
    }


    tasks {
        compileJava {
            sourceCompatibility = "1.8"
            targetCompatibility = "1.8"
        }

        bootJar {
            enabled = false
        }

        jar {
            enabled = true
        }
    }

}

tasks {
    bootJar {
        enabled = false
    }
}
