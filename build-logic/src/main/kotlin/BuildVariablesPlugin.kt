import com.mooncloak.kodetools.kenv.Kenv
import com.mooncloak.kodetools.kenv.dotenv
import com.mooncloak.kodetools.kenv.properties
import com.mooncloak.kodetools.kenv.store.getBooleanOrNull
import com.mooncloak.kodetools.kenv.store.getStringOrNull
import org.gradle.api.Plugin
import org.gradle.api.Project

abstract class BuildVariablesPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        projectBuildVariables[target.name] = BuildVariables(
            kenv = Kenv {
                system()

                properties(file = target.rootProject.layout.projectDirectory.file("app.properties").asFile)

                val envFile = target.rootProject.layout.projectDirectory.file(".env").asFile

                if (envFile.exists()) {
                    dotenv(file = envFile)
                }
            }
        )
    }
}

class BuildVariables internal constructor(
    private val kenv: Kenv
) {

    val group: String
        get() = kenv["group"].value

    val version: String
        get() = kenv["version"].value

    val versionCode: Int
        get() = getCommitCount()

    val cdnPublishAccessKey: String?
        get() = runCatching { kenv.getStringOrNull("mooncloakCdnAccessKey") }.getOrNull()

    val cdnPublishSecretKey: String?
        get() = runCatching { kenv.getStringOrNull("mooncloakCdnSecretKey") }.getOrNull()

    val publishAsLatest: Boolean
        get() = runCatching { kenv.getBooleanOrNull("publishAsLatest") }.getOrNull() ?: true
}

val Project.buildVariables: BuildVariables
    get() {
        var variables = projectBuildVariables[this.name]

        if (variables == null) {
            this.logger.warn("The '${BuildVariablesPlugin::class.simpleName}' was not applied to project with name '${this.name}'. Attempting to load root project build variables.")
        }

        if (this != this.rootProject) {
            variables = projectBuildVariables[this.rootProject.name]
        }

        return variables
            ?: error("Failed to load required build variables. Make sure the '${BuildVariablesPlugin::class.simpleName}' is applied to the project.")
    }

private val projectBuildVariables = mutableMapOf<String, BuildVariables>()
