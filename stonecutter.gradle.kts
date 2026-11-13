plugins {
    id("dev.kikugie.stonecutter")
    id("net.fabricmc.fabric-loom") version "1.17-SNAPSHOT" apply false
    id("net.fabricmc.fabric-loom-remap") version "1.17-SNAPSHOT" apply false
}
stonecutter active "26.2"

gradle.projectsEvaluated {
    val auditTasks = project.subprojects
        .mapNotNull { sub -> sub.tasks.findByName("runClientMixinAudit") }
        .sortedBy { it.path }

    fun runDirectoryOf(task: Task): File? = (task.project.extensions
        .findByName("loom") as? net.fabricmc.loom.api.LoomGradleExtensionAPI)
        ?.runConfigs
        ?.findByName("clientMixinAudit")
        ?.runDirectory?.get()?.asFile

    for (task in auditTasks) {
        val runDirectory = runDirectoryOf(task) ?: continue
        for (other in auditTasks) {
            if (other !== task && runDirectoryOf(other) == runDirectory) {
                task.mustRunAfter("${other.project.path}:downloadAssets")
            }
        }
    }

    auditTasks.zipWithNext { previous, next -> next.mustRunAfter(previous) }
}

tasks.register("buildAndGather") {
    group = "build"
    dependsOn(project.subprojects.map { it.tasks.named("build") })
    doFirst {
        println("Gathering builds")
        val buildLibs: (Project) -> java.nio.file.Path = { p ->
            p.layout.buildDirectory
                .dir("libs")
                .get()
                .asFile
                .toPath()
        }
        project.delete(project.fileTree(buildLibs(rootProject)) { include("*") })
        project.subprojects.forEach { subproject ->
            project.copy {
                from(buildLibs(subproject)) {
                    include("*.jar")
                    exclude("*-dev.jar", "*-sources.jar", "*-shadow.jar", "*-javadoc.jar")
                }
                into(buildLibs(rootProject))
                duplicatesStrategy = DuplicatesStrategy.INCLUDE
            }
        }
    }
}