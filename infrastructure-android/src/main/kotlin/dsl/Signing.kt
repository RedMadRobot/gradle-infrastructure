package dsl

import com.android.build.api.dsl.SigningConfig
import org.gradle.api.Incubating
import java.io.File
import java.util.*

/**
 * Applies signature configuration from the specified [propertiesFile].
 *
 * The file content should have the following format:
 * ```
 * store_file=[relative path to keystore file]
 * store_password=[keystore password]
 * key_alias=[the alias of the needed key]
 * key_password=[the password for the specified alias]
 * ```
 */
@Incubating
public fun SigningConfig.fromProperties(propertiesFile: File) {
    val directory = propertiesFile.parentFile
    val properties = Properties()
    propertiesFile.inputStream().use(properties::load)

    storeFile = File(directory, properties.getProperty("store_file"))
    storePassword = properties.getProperty("store_password")
    keyAlias = properties.getProperty("key_alias")
    keyPassword = properties.getProperty("key_password")
}
