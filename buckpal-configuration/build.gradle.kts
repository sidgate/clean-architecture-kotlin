plugins{
    kotlin("plugin.allopen")
    kotlin("plugin.spring")
}
dependencies {

    implementation (project(":common"))
    implementation (project(":buckpal-application"))
    implementation (project(":adapters:buckpal-persistence"))
    implementation (project(":adapters:buckpal-web"))
    implementation ("org.springframework.boot:spring-boot-starter-web")
    runtimeOnly ("com.h2database:h2")
    annotationProcessor ("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude (group = "junit") // excluding junit 4
    }
    testImplementation ("org.junit.jupiter:junit-jupiter-engine:5.0.1")
    testImplementation ("org.junit.platform:junit-platform-launcher:1.4.2")
    testImplementation ("org.mockito:mockito-junit-jupiter:2.23.0")
    testImplementation ("com.tngtech.archunit:archunit:0.9.3")
    testImplementation ("de.adesso:junit-insights:1.1.0")
    testImplementation ("com.h2database:h2")

}

tasks {
    test {
        useJUnitPlatform()
        systemProperty ("de.adesso.junitinsights.enabled", "true")
    }

    bootJar {
        enabled = true
    }

    allOpen {
        annotations("org.springframework.boot.autoconfigure.SpringBootApplication",
        "org.springframework.context.annotation.Configuration")
    }
}

