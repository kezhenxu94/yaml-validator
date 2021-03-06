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

package io.github.kezhenxu94

import io.github.kezhenxu94.annotations.TagProcessor
import org.reflections.Reflections
import org.yaml.snakeyaml.constructor.Construct
import org.yaml.snakeyaml.constructor.Constructor
import org.yaml.snakeyaml.nodes.Tag

/**
 * The root `Constructor` in the validation, which can be used to recursively construct POJOs.
 */
class RootConstructor(
    tagClasses: MutableSet<Class<*>> = mutableSetOf(),
    tagPackage: MutableSet<String> = mutableSetOf()
) : Constructor() {
    init {
        registerPrefix(Package::class.java.`package`.name)

        tagPackage.forEach { registerPrefix(it) }

        tagClasses.forEach { registerClass(it) }
    }

    private fun registerPrefix(prefix: String) {
        Reflections(prefix).getTypesAnnotatedWith(TagProcessor::class.java).forEach { registerClass(it) }
    }

    private fun registerClass(klass: Class<*>) {
        klass.getAnnotation(TagProcessor::class.java).apply {
            val instance = try {
                construct.java.getDeclaredConstructor(RootConstructor::class.java).newInstance(this@RootConstructor)
            } catch (_: Throwable) {
                construct.java.newInstance()
            }

            tags.forEach {
                yamlConstructors[Tag(it)] = instance
            }

            prefixes.forEach {
                yamlMultiConstructors[it] = instance
            }
        }
    }

    /**
     * The `yamlConstructors` of of `Constructor`.
     */
    val constructs: Map<Tag, Construct> get() = yamlConstructors
}
