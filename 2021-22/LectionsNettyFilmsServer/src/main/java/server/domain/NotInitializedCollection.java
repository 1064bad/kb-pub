package server.domain;

import lombok.EqualsAndHashCode;

import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

@EqualsAndHashCode
public class NotInitializedCollection<T> implements Collection<T> {

    private RuntimeException t() {
        return new RuntimeException("trying to use not initialized collection");
    }

    @Override
    public int size() {
        throw t();
    }

    @Override
    public boolean isEmpty() {
        throw t();
    }

    @Override
    public boolean contains(Object o) {
        throw t();
    }

    @Override
    public Iterator<T> iterator() {
        throw t();
    }

    @Override
    public Object[] toArray() {
        throw t();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        throw t();
    }

    @Override
    public boolean add(T t) {
        throw t();
    }

    @Override
    public boolean remove(Object o) {
        throw t();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw t();
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        throw t();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw t();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw t();
    }

    @Override
    public void clear() {
        throw t();
    }

    @Override
    public <T1> T1[] toArray(IntFunction<T1[]> generator) {
        throw t();
    }

    @Override
    public boolean removeIf(Predicate<? super T> filter) {
        throw t();
    }

    @Override
    public Spliterator<T> spliterator() {
        throw t();
    }

    @Override
    public Stream<T> stream() {
        throw t();
    }

    @Override
    public Stream<T> parallelStream() {
        throw t();
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        throw t();
    }
}
