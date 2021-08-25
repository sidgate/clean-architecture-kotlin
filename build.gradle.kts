plugins {
    id ("io.spring.dependency-management") version "1.0.8.RELEASE"
    java
}

subprojects {

    group = "io.reflectoring.reviewapp"
    version = "0.0.1-SNAPSHOT"

    apply( plugin= "java")
    apply (plugin= "io.spring.dependency-management")
    apply (plugin= "java-library")

    repositories {
        jcenter()
    }

    dependencyManagement {
        imports {
            mavenBom("org.springframework.boot:spring-boot-dependencies:2.1.9.RELEASE")
        }
    }

    tasks {
        compileJava {
            sourceCompatibility = "1.8"
            targetCompatibility = "1.8"
        }
    }

}
