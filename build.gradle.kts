plugins {
    id("java")
}

group = "example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.springframework.boot:spring-boot-starter-web:3.5.0")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf:3.5.0")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client:3.5.0")
    implementation("org.springframework.boot:spring-boot-starter-security:3.5.0")
    implementation("org.springframework.boot:spring-boot-devtools:3.5.0")
    implementation("org.projectlombok:lombok:1.18.38")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.5.0")
    implementation("org.postgresql:postgresql:42.7.5")
}
tasks.test {
    useJUnitPlatform()
}

tasks.register("add") {
    println("Checking if 'name' property exists: ${project.hasProperty("name")}")
    val entityName = if (project.hasProperty("name")) {
        val name = project.property("name").toString().trim()
        println("Raw value from -Pname: $name")
        name.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    } else {
        throw GradleException("Please provide -Pname=EntityName")
    }
    println("Final entityName: $entityName")

    val baseDir = "src/main/java/example"
    val files = listOf(
        "models/${entityName}.java" to "package example.models;\n\npublic class ${entityName} {\n    // TODO: Add fields\n}",
        "repositories/${entityName}Repository.java" to "package example.repositories;\n\nimport org.springframework.data.jpa.repository.JpaRepository;\nimport example.models.${entityName};\n\npublic interface ${entityName}Repository extends JpaRepository<${entityName}, Long> {\n}",
        "services/${entityName}Service.java" to "package example.services;\n\npublic interface ${entityName}Service {\n    // TODO: Add service methods\n}",
        "services/impl/${entityName}ServiceImpl.java" to "package example.services.impl;\n\nimport example.services.${entityName}Service;\nimport org.springframework.stereotype.Service;\n\n@Service\npublic class ${entityName}ServiceImpl implements ${entityName}Service {\n    // TODO: Implement service methods\n}",
        "controllers/${entityName}Controller.java" to "package example.controllers;\n\nimport org.springframework.web.bind.annotation.*;\n\n@RestController\n@RequestMapping(\"/${entityName.toLowerCase()}\")\npublic class ${entityName}Controller {\n    // TODO: Add endpoints\n}"
    )
    doLast {
        files.forEach { (relativePath, content) ->
            val file = file("$baseDir/$relativePath")
            file.parentFile.mkdirs()
            if (!file.exists()) {
                file.writeText(content)
                println("Created: $baseDir/$relativePath")
            } else {
                println("Skipped (already exists): $baseDir/$relativePath")
            }
        }
    }
}
