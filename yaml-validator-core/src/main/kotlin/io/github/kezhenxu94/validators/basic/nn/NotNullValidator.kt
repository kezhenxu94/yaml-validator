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

package io.github.kezhenxu94.validators.basic.nn

import io.github.kezhenxu94.Validatable
import io.github.kezhenxu94.annotations.TagProcessor
import io.github.kezhenxu94.exceptions.ValidateException
import io.github.kezhenxu94.validators.Referable

@TagProcessor(tags = ["!nn"], construct = NotNullConstruct::class)
internal class NotNullValidator : Validatable, Referable<Any> {
  override var reference: Any? = null

  override fun validate(any: Any?) {
    reference = any

    if (any == null) {
      throw ValidateException()
    }
  }
}