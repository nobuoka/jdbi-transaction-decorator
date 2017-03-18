/*
 * Copyright 2017 Nobuoka Yu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package info.vividcode.jdbi.transaction.decorator.api;

import java.lang.annotation.Annotation;

public interface TransactionDecorator {

    /**
     * This method decorates an object so that annotated methods in a decorated object will execute in transaction.
     *
     * @param clazz Interface of decoration target.
     * @param targetAnnotation Methods which annotated by this annotation will execute in transaction.
     * @param impl Decoration target.
     * @param <T> Interface type of decoration target. This type is also return type of this method.
     * @param <C> Actual type of decoration target. {@link C} must extends {@link T}.
     * @return Decorated object. Annotated methods will execute in transaction.
     */
    <T, C extends T> T decorate(Class<T> clazz, Class<? extends Annotation> targetAnnotation, C impl);

}
