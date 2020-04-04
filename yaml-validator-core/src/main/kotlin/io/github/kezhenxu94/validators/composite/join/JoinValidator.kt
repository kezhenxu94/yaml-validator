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

package io.github.kezhenxu94.validators.composite.join

import io.github.kezhenxu94.Validatable
import io.github.kezhenxu94.annotations.TagProcessor
import io.github.kezhenxu94.exceptions.ValidateException
import io.github.kezhenxu94.validators.Referable

@TagProcessor(tags = ["!join"], construct = JoinConstruct::class)
internal class JoinValidator(private val expected: List<*>) : Validatable, Referable<String> {
  override var reference: String? = null

  override fun validate(any: Any?) {
    val expectedString = expected.joinToString("", transform = {
      when (it) {
        is Referable<*> -> it.reference.toString()
        else            -> it.toString()
      }
    })

    reference = expectedString

    if (expectedString != any) {
      throw ValidateException()
    }
  }
}