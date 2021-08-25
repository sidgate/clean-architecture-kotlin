dependencies {
    implementation (project(":common"))
    implementation (project(":buckpal-application"))
    
    implementation ("org.springframework.boot:spring-boot-starter-web")
    
    compileOnly ("org.projectlombok:lombok")
    annotationProcessor ("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude (group= "junit") // excluding junit 4
    }
    testImplementation ("org.junit.jupiter:junit-jupiter-engine:5.0.1")
    testImplementation ("org.mockito:mockito-junit-jupiter:2.23.0")
    testImplementation ("com.tngtech.archunit:archunit:0.9.3")
    testImplementation ("de.adesso:junit-insights:1.1.0")
    testImplementation ("org.junit.platform:junit-platform-launcher:1.4.2")
}

tasks {
    test {
        useJUnitPlatform()
        systemProperty ("de.adesso.junitinsights.enabled", "true")
    }
}
