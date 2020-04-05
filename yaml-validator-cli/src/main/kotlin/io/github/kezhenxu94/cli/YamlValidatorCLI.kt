@file:Suppress("LocalVariableName")

package io.github.kezhenxu94.cli

import io.github.kezhenxu94.YamlValidator
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default
import kotlinx.cli.required
import java.io.FileInputStream

/**
 * [YamlValidatorCLI] is a CLI wrapper for yaml-validator.
 */
class YamlValidatorCLI {

  companion object {
    /**
     * [main] entrance of the CLI tool.
     */
    @JvmStatic
    fun main(args: Array<String>) {
      val parser = ArgParser("yv")

      val rules by parser.option(ArgType.String, shortName = "r", description = "rules file").required()

      val data by parser.option(ArgType.String, shortName = "f", description = "yaml file to validate").required()

      val `ignore-missing` by parser.option(ArgType.Boolean, description = "ignore missing fields").default(false)

      val `disable-reference` by parser.option(ArgType.Boolean, description = "disable references").default(false)

      parser.parse(args)

      YamlValidator.from(FileInputStream(rules)).apply {
        if (`ignore-missing`) ignoreMissing() else Unit
        if (`disable-reference`) disableReference() else Unit
      }.build().validate(FileInputStream(data))
    }
  }
}
