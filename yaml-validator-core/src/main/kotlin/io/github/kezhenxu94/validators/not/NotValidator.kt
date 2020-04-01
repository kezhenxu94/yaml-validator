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

package io.github.kezhenxu94.validators.not

import io.github.kezhenxu94.annotations.Validator
import io.github.kezhenxu94.exceptions.ValidateException

@Validator(prefixes = ["!not."], construct = NotConstruct::class)
internal class NotValidator(private val validator: io.github.kezhenxu94.Validator) : io.github.kezhenxu94.Validator {
  @Throws(ValidateException::class)
  override fun validate(any: Any?) {
    try {
      validator.validate(any)
    } catch (_: ValidateException) {
      return
    }
    throw ValidateException()
  }
}
