package com.balugaq.slimefunaccessor.libraries.utils;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import io.github.thebusybiscuit.slimefun4.libraries.dough.collections.Pair;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nonnull;
import javax.xml.stream.Location;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
public class Pager<T> {
    public List<Pair<T, Descriptor<T>>> content = new ArrayList<>();
    public int contentPerPage = 9;
    public int currentPage = 1;

    @Nonnull
    public Pager<T> setContentPerPage(int contentPerPage) {
        Preconditions.checkArgument(contentPerPage > 0, "contentPerPage must be greater than 0");
        this.contentPerPage = contentPerPage;

        int totalPage = getTotalPage();
        if (totalPage == 0) {
            this.currentPage = 0;
        } else {
            this.currentPage = Math.min(currentPage, totalPage);
        }
        return this;
    }

    @Nonnull
    public Pager<T> setContent(@Nonnull List<Pair<T, Descriptor<T>>> content) {
        this.content = new ArrayList<>(content);
        int totalPage = getTotalPage();
        if (totalPage == 0) {
            this.currentPage = 0;
        } else {
            this.currentPage = Math.min(currentPage, totalPage);
        }
        return this;
    }

    @Nonnull
    public Pager<T> setCurrentPage(int page) {
        int totalPage = getTotalPage();
        if (totalPage == 0) {
            if (page != 0) {
                Logger.warn("Invalid page number: " + page + " (total page: 0)");
            }
        } else if (page < 1 || page > totalPage) {
            Logger.warn("Invalid page number: " + page + " (total page: " + totalPage + ")");
        }
        this.currentPage = page;
        return this;
    }

    public boolean hasNext() {
        return currentPage < getTotalPage();
    }

    public boolean hasPrevious() {
        return currentPage > 1;
    }

    public int getTotalPage() {
        if (contentPerPage <= 0 || content.isEmpty()) {
            return 0;
        }
        return (int) Math.ceil((double) content.size() / contentPerPage);
    }

    @Nonnull
    public Pager<T> add(@Nonnull T element) {
        content.add(new Pair<>(element, new Descriptor<>(element)));
        return this;
    }

    @Nonnull
    public Pager<T> add(@Nonnull Pair<T, Descriptor<T>> element) {
        content.add(element);
        return this;
    }

    @Nonnull
    public Pager<T> addAll(@Nonnull Collection<Pair<T, Descriptor<T>>> elements) {
        content.addAll(elements);
        return this;
    }

    @Nonnull
    public Pager<T> remove(@Nonnull T element) {
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
        return content.contains(element);
    }

    public int size() {
        return content.size();
    }

    public int getPageFromIndex(int index) {
        return (index / contentPerPage) + 1;
    }

    @Nonnull
    public List<Pair<T, Descriptor<T>>> next() {
        if (hasNext()) currentPage++;
        int start = (currentPage - 1) * contentPerPage;
        int end = Math.min(currentPage * contentPerPage, content.size());
        return new ArrayList<>(content.subList(start, end));
    }

    @Nonnull
    public List<Pair<T, Descriptor<T>>> previous() {
        if (hasPrevious()) currentPage--;
        int start = (currentPage - 1) * contentPerPage;
        int end = Math.min(currentPage * contentPerPage, content.size());
        return new ArrayList<>(content.subList(start, end));
    }

    @Nonnull
    public Pager<T> turnToPageWhere(@Nonnull Function<Pair<T, Descriptor<T>>, Boolean> condition) {
        int batchSize = 1000;  // 分批处理
        for (int i = 0; i < content.size(); i += batchSize) {
            int end = Math.min(i + batchSize, content.size());
            List<Pair<T, Descriptor<T>>> batch = content.subList(i, end);
            for (Pair<T, Descriptor<T>> item : batch) {
                if (condition.apply(item)) {
                    setCurrentPage(getPageFromIndex(i));
                    return this;
                }
            }
        }
        setCurrentPage(1);
        return this;
    }

    @Getter
    public static class Descriptor<T> {
        private final T item;
        @Setter
        private String tag = null;

        public Descriptor(@Nonnull T item) {
            this.item = item;
        }

        public Descriptor(@Nonnull T item, @Nonnull String tag) {
            this.item = item;
            this.tag = tag;
        }
    }
}
