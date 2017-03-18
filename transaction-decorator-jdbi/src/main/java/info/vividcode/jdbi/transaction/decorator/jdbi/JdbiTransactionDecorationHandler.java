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

import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.TransactionCallback;
import org.skife.jdbi.v2.sqlobject.mixins.Transactional;
import org.skife.jdbi.v2.tweak.transactions.LocalTransactionHandler;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class JdbiTransactionDecorationHandler<T> implements InvocationHandler {

    private final T target;
    private final Class<? extends Annotation> targetAnnotation;
    private final Transactional<?> transactionalSqlObject;

    JdbiTransactionDecorationHandler(T target, Class<? extends Annotation> targetAnnotation, Transactional<?> transactionalSqlObject) {
        this.target = target;
        this.targetAnnotation = targetAnnotation;
        this.transactionalSqlObject = transactionalSqlObject;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Method targetMethod = target.getClass().getMethod(method.getName(), method.getParameterTypes());
        Annotation transactionalAnnotationsOrNull = targetMethod.getAnnotation(targetAnnotation);
        if (transactionalAnnotationsOrNull != null) {
            return inTransaction(() -> invokeTargetMethod(target, targetMethod, args));
        } else {
            return invokeTargetMethod(target, targetMethod, args);
        }
    }

    /**
     * This method is similar to {@link LocalTransactionHandler#inTransaction(Handle, TransactionCallback)} method.
     */
    private <E extends Throwable> Object inTransaction(Proc<E> proc) throws E {
        final Object returnValue;
        try {
            transactionalSqlObject.begin();
            returnValue = proc.exec();
            transactionalSqlObject.commit();
        } catch (Throwable error) {
            try {
                transactionalSqlObject.rollback();
            } catch (Throwable e) {
                error.addSuppressed(e);
            }
            throw error;
        }
        return returnValue;
    }

    private static Object invokeTargetMethod(Object target, Method method, Object[] args) throws Throwable {
        try {
            return method.invoke(target, args);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    private interface Proc<E extends Throwable> {
        Object exec() throws E;
    }

}
