package karl.codes.java;

import com.google.common.collect.AbstractIterator;

import java.util.Iterator;

/**
 * Created by karl on 10/20/2015.
 */
public final class ClassAncestry<T> implements Iterable<Class<? extends T>> {
    private final Class<? extends T> startClass;
    private final Class<T> rootClass;

    public ClassAncestry(Class<? extends T> startClass, Class<T> rootClass) {
        this.startClass = startClass;
        this.rootClass = rootClass;
    }

    @Override
    public Iterator<Class<? extends T>> iterator() {
        return new ClassAncestryIterator<>(startClass, rootClass);
    }

    private static final class ClassAncestryIterator<T> extends AbstractIterator<Class<? extends T>> {
        private Class<?> current;
        private final Class<T> rootClass;

        public ClassAncestryIterator(Class<? extends T> startClass, Class<T> rootClass) {
            this.current = startClass;
            this.rootClass = rootClass;
        }

        @Override
        protected Class<? extends T> computeNext() {
            Class<?> next = current;

            if (current == null) {
                return endOfData();
            } else if (current.equals(rootClass)) {
                // TODO a bit ugly, there's got to be a more elegant way
                current = null;
            } else {
                current = current.getSuperclass();
            }

            return (Class<? extends T>) next;
        }
    }

}
