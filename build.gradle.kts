plugins {
    id("java")
}

group = "com.example.greatarchitecture"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("com.google.code.gson:gson:2.8.9")
    implementation("org.xerial:sqlite-jdbc:3.43.2.0")
}

tasks.test {
    useJUnitPlatform()
}