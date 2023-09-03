package ru.job4j.interview.service;

import java.util.Objects;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class TrackState<L, R> {
    private final L previous;
    private final R next;

    public TrackState(L previous, R next) {
        this.previous = previous;
        this.next = next;
    }

    public L getPrevious() {
        return previous;
    }

    public R getNext() {
        return next;
    }

    public boolean last() {
        return this.previous != null && this.next == null;
    }

    public boolean noTracks() {
        return this.previous == null && this.next == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TrackState<?, ?> couple = (TrackState<?, ?>) o;

        if (previous != null ? !previous.equals(couple.previous) : couple.previous != null) return false;
        return next != null ? next.equals(couple.next) : couple.next == null;
    }

    @Override
    public int hashCode() {
        int result = previous != null ? previous.hashCode() : 0;
        result = 31 * result + (next != null ? next.hashCode() : 0);
        return result;
    }

    public boolean isNull() {
        return Objects.isNull(previous) && Objects.isNull(next);
    }
}
