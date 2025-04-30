plugins {
    id("java")
}

group = "com.minecraft.minepay"
version = "1.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_21

dependencies {
    annotationProcessor("org.projectlombok:lombok:1.18.32")

    compileOnly("org.projectlombok:lombok:1.18.32")
    compileOnly("com.google.code.gson:gson:2.10.1")
    compileOnly("com.google.guava:guava:33.2.0-jre")
    compileOnly("com.mojang:authlib:3.16.29")

    implementation("com.zaxxer:HikariCP:5.1.0")
    implementation("org.jetbrains:annotations:24.1.0")
    implementation("com.googlecode.json-simple:json-simple:1.1.1")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.test {
    useJUnitPlatform()
}