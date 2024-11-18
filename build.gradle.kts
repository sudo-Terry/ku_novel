plugins {
    id("java")
    id("org.springframework.boot") version "2.7.6"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
}

group = "com.example.greatarchitecture"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation("com.google.code.gson:gson:2.8.9")
    implementation("org.xerial:sqlite-jdbc:3.36.0.3")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation ("org.projectlombok:lombok")
    implementation ("org.slf4j:slf4j-api:1.7.30")
    implementation ("org.hibernate:hibernate-core:5.6.10.Final")
    annotationProcessor ("org.projectlombok:lombok:1.18.28")

}

tasks.test {
    useJUnitPlatform()
}

springBoot {
    mainClass.set("com.example.ku_novel.server.ServerMain")
}