/**
 * Copyright 2020 yaml-validator
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

package io.github.kezhenxu94.validators.basic.eq

import io.github.kezhenxu94.annotations.TagProcessor
import io.github.kezhenxu94.core.Context
import io.github.kezhenxu94.core.Referable
import io.github.kezhenxu94.core.Validatable
import io.github.kezhenxu94.exceptions.ValidateException
import org.yaml.snakeyaml.nodes.ScalarNode

@TagProcessor(tags = ["!eq"], construct = EqualConstruct::class)
internal open class EqualValidator(override val context: Context) : Validatable, Referable<Any> {
    override var reference: Any? = null

    override fun validate(candidate: Any?) {
        try {
            val expected = (context.node as ScalarNode).value

            if (reference == null) {
                validateAnchor(expected, candidate)
            } else {
                validateAlias(candidate)
            }
        } finally {
            reference = candidate
        }
    }

    private fun validateAlias(any: Any?) {
        if (reference?.toString() != any?.toString()) {
            throw ValidateException(context, reference, any)
        }
    }

    private fun validateAnchor(expected: String?, any: Any?) {
        if (expected != any?.toString()) {
            throw ValidateException(context, expected, any)
        }
    }
}
