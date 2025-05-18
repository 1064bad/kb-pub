plugins {
    application
    war
}

group = "org.example"
version = ""

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.26")
    annotationProcessor("org.projectlombok:lombok:1.18.26")
    implementation("org.slf4j:slf4j-api:2.0.6")
    implementation("org.slf4j:slf4j-simple:2.0.6")
    implementation("org.simplejavamail:simple-java-mail:7.9.1")
    implementation("org.telegram:telegrambots:6.5.0")
    implementation("org.postgresql:postgresql:42.6.0")
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.0.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.14.2")
    implementation("org.apache.logging.log4j:log4j-core:2.20.0")
    implementation("org.apache.logging.log4j:log4j-api:2.20.0")
    implementation("com.zaxxer:HikariCP:5.0.1")

    implementation("org.reflections:reflections:0.10.2")


    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    testImplementation("org.mockito:mockito-core:5.11.0")
    testImplementation("org.assertj:assertj-core:3.25.3")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

application {
    mainClass.set("library.App")
}

tasks.getByName<JavaExec>("run") {
    standardInput = System.`in`
}

tasks.war {
    webAppDirectory.set(file("src/main/library"))
}