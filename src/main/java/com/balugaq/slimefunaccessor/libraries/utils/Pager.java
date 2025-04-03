package com.balugaq.slimefunaccessor.libraries.utils;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A simple implementation of a pager for a list of elements.
 * <p>
 * Pages are numbered starting from 1.
 * 0 is used to indicate an empty or invalid page.
 *
 * @param <T> the type of elements in the list
 * @author balugaq
 */
@Getter
public class Pager<T> {
    private List<Container<T>> content = new CopyOnWriteArrayList<>();
    private int contentPerPage = 9;
    private int currentPage = 1;

    @Nonnull
    public Pager<T> setContentPerPage(int contentPerPage) {
        Preconditions.checkArgument(contentPerPage > 0, "contentPerPage must be greater than 0");
        this.contentPerPage = contentPerPage;

        int totalPage = getTotalPages();
        if (totalPage == 0) {
            this.currentPage = 0;
        } else {
            this.currentPage = Math.min(currentPage, totalPage);
        }
        return this;
    }

    @Nonnull
    private Pager<T> setContent(@Nonnull List<Container<T>> content) {
        this.content = new ArrayList<>(content);
        int totalPage = getTotalPages();
        if (totalPage == 0) {
            this.currentPage = 0;
        } else {
            this.currentPage = Math.min(currentPage, totalPage);
        }
        return this;
    }

    @Nonnull
    private Pager<T> setCurrentPage(int page) {
        int totalPage = getTotalPages();
        if (totalPage == 0) {
            if (page != 0) {
                Logger.warn("Invalid page number: " + page + " (total page: 0)");
                this.currentPage = Math.max(1, Math.min(page, getTotalPages()));
            }
        } else if (!isInbounds(page)) {
            Logger.warn("Invalid page number: " + page + " (total page: " + totalPage + ")");
            this.currentPage = Math.max(1, Math.min(page, getTotalPages()));
        } else {
            this.currentPage = page;
        }
        return this;
    }

    public boolean hasNext() {
        return currentPage < getTotalPages();
    }

    public boolean hasPrevious() {
        return currentPage > 1;
    }

    public int getTotalPages() {
        if (contentPerPage <= 0 || content.isEmpty()) {
            return 0;
        }
        return (int) Math.ceil((double) content.size() / contentPerPage);
    }

    @Nonnull
    public Pager<T> add(@Nonnull T element) {
        content.add(new Container<>(element));
        return this;
    }

    @Nonnull
    public Pager<T> add(@Nonnull Container<T> element) {
        content.add(element);
        return this;
    }

    @Nonnull
    public Pager<T> addAll(@Nonnull Collection<Container<T>> elements) {
        content.addAll(elements);
        return this;
    }

    @Nonnull
    public Pager<T> remove(@Nonnull T element) {
        return remove(new Container<>(element));
    }

    @Nonnull
    public Pager<T> remove(@Nonnull Container<T> element) {
        content.remove(element);
        return this;
    }

    @Nonnull
    public Pager<T> clear() {
        content.clear();
        return this;
    }

    public boolean isEmpty() {
        return content.isEmpty();
    }

    public boolean contains(@Nonnull T element) {
        return contains(new Container<>(element));
    }

    public boolean contains(@Nonnull Container<T> element) {
        return content.contains(element);
    }

    public int size() {
        return content.size();
    }

    public int getPageFromIndex(int index) {
        return (index / contentPerPage) + 1;
    }

    @Nonnull
    public List<Container<T>> next() {
        if (hasNext()) currentPage++;
        return getPage(currentPage);
    }

    @Nonnull
    public List<Container<T>> previous() {
        if (hasPrevious()) currentPage--;
        return getPage(currentPage);
    }

    @Nonnull
    public List<Container<T>> getCurrent() {
        return getPage(currentPage);
    }

    @Nonnull
    public List<Container<T>> getPageForward(int forwardPages) {
        return getPage(currentPage + forwardPages);
    }

    @Nonnull
    public List<Container<T>> getPageBackward(int backwardPages) {
        return getPage(currentPage - backwardPages);
    }

    @Nonnull
    public List<Container<T>> getPageLimited(int elementsLimit) {
        return getPageLimited(currentPage, elementsLimit);
    }

    @Nonnull
    public List<Container<T>> getPageLimited(int pageStart, int elementsLimit) {
        return getPageLimited(pageStart, elementsLimit, null);
    }

    @Nonnull
    public List<Container<T>> getPageLimited(int pageStart, int elementsLimit, @Nullable Function<Container<T>, Boolean> filter) {
        List<Container<T>> filtered = new ArrayList<>();
        for (int i = (pageStart - 1) * contentPerPage; i < content.size() && filtered.size() < elementsLimit; i++) {
            Container<T> container = content.get(i);
            if (filter == null || filter.apply(container)) {
                filtered.add(container);
            }
        }
        return filtered;
    }

    public List<Container<T>> getPage(int page) {
        if (!isInbounds(page)) {
            return Collections.emptyList();
        }
        int start = (page - 1) * contentPerPage;
        int end = Math.min(page * contentPerPage, content.size());
        return new ArrayList<>(content.subList(start, end));
    }

    public boolean isInbounds(int page) {
        return page >= 1 && page <= getTotalPages();
    }

    @EqualsAndHashCode
    @Getter
    public static class Container<T> {
        private final T data;
        @Setter
        private String tag = null;

        public Container(@Nonnull T data) {
            this.data = data;
        }
    }
}
