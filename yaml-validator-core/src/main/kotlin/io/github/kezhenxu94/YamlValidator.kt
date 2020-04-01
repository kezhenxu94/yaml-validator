/**
 * Copyright 2020 kezhenxu94
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.kezhenxu94

import io.github.kezhenxu94.exceptions.ValidateException
import io.github.kezhenxu94.validators.nn.NotNullValidator
import org.yaml.snakeyaml.Yaml
import java.io.InputStream

/**
 * A helper class to make it easy to construct a real [Validator] instance.
 */
class YamlValidator private constructor(private val builder: Builder) {
  private val validator = Yaml(RootConstructor).loadAs(builder.inputStream, Map::class.java)

  /**
   * @see [Validator.validate]
   */
  fun validate(toValidate: Any?) {
    return when {
      validator is Validator -> validator.validate(toValidate)
      toValidate is String   -> traverse(validator, Loader.loadAs(toValidate, Map::class.java))
      else                   -> traverse(validator, Loader.loadAs(Dumper.dump(toValidate), Map::class.java))
    }
  }

  private fun traverse(validator: Any, toValidate: Any?) {
    when (validator) {
      is Validator -> {
        if (!builder.ignoreMissing || validator is NotNullValidator || toValidate != null) {
          validator.validate(toValidate)
        }
      }

      is Map<*, *> -> {
        if (toValidate !is Map<*, *>) {
          throw ValidateException()
        }
        validator.forEach { (k, v) ->
          traverse(v!!, toValidate[k])
        }
      }

      is List<*>   -> {
        if (toValidate !is List<*>) {
          throw ValidateException()
        }
        validator.forEachIndexed { index, v ->
          traverse(v!!, toValidate[index])
        }
      }
    }
  }

  companion object {
    class Builder(
        internal val inputStream: InputStream,
        internal var ignoreMissing: Boolean = false
    ) {

      /**
       * Ignore the missing fields when validating, if the corresponding validator is [NotNullValidator], this option
       * takes no effect.
       */
      fun ignoreMissing() = this.also { ignoreMissing = true }

      /**
       * Build a real [Validator] instance from this [Builder].
       */
      fun build() = YamlValidator(this)
    }

    /**
     * Create a [Builder] from the YAML [InputStream].
     */
    fun from(inputStream: InputStream) = Builder(inputStream)

    /**
     * Create a [Builder] from the [yaml] text string.
     */
    fun from(yaml: String) = Builder(yaml.byteInputStream())
  }
}
