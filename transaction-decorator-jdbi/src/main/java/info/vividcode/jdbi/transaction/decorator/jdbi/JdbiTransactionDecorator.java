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

package info.vividcode.jdbi.transaction.decorator.jdbi;

import info.vividcode.jdbi.transaction.decorator.api.TransactionDecorator;
import org.skife.jdbi.v2.sqlobject.mixins.Transactional;

import java.lang.annotation.Annotation;
import java.lang.reflect.Proxy;

public class JdbiTransactionDecorator<S extends Transactional<S>> implements TransactionDecorator {

    private final S transactionalSqlObject;

    public JdbiTransactionDecorator(S transactionalSqlObject) {
        this.transactionalSqlObject = transactionalSqlObject;
    }

    @Override
    public <T, C extends T> T decorate(Class<T> clazz, Class<? extends Annotation> targetAnnotation, C impl) {
        JdbiTransactionDecorationHandler<T> handler = new JdbiTransactionDecorationHandler<>(impl, targetAnnotation, transactionalSqlObject);
        return clazz.cast(Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{ clazz }, handler));
    }

}
