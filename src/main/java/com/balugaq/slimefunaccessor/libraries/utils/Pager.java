package com.balugaq.slimefunaccessor.libraries.utils;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.xzavier0722.mc.plugin.slimefun4.storage.util.StorageCacheUtils;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private List<Container<T>> content = new ArrayList<>();
    @Setter
    private boolean dirty = true;
    private int contentPerPage = 9;
    private int currentPage = 1;

    @CanIgnoreReturnValue
    @Nonnull
    public Pager<T> setContentPerPage(final int contentPerPage) {
        Preconditions.checkArgument(contentPerPage > 0, "contentPerPage must be greater than 0");
        this.contentPerPage = contentPerPage;

        int totalPage = getTotalPages();
        this.currentPage = Math.min(currentPage, totalPage);
        setDirty(true);
        return this;
    }

    @CanIgnoreReturnValue
    @Nonnull
    private Pager<T> setContent(@Nonnull final List<Container<T>> content) {
        this.content = new ArrayList<>(content);
        int totalPage = getTotalPages();
        this.currentPage = Math.min(currentPage, totalPage);
        setDirty(true);
        return this;
    }

    @CanIgnoreReturnValue
    @Nonnull
    private Pager<T> setCurrentPage(final int page) {
        final int totalPage = getTotalPages();
        if (isOutbounds(page)) {
            Logger.warn("Invalid page number: " + page + " (total page: " + totalPage + ")");
            this.currentPage = Math.max(1, Math.min(page, getTotalPages()));
        } else {
            this.currentPage = page;
        }
        setDirty(true);
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
            return 1;
        }
        return Math.max(1, (int) Math.ceil((double) content.size() / contentPerPage));
    }

    @CanIgnoreReturnValue
    @Nonnull
    public Pager<T> add(@Nonnull final T element) {
        return add(new Container<>(element));
    }

    @CanIgnoreReturnValue
    @Nonnull
    public Pager<T> add(@Nonnull final Container<T> element) {
        content.add(element);
        setDirty(true);
        return this;
    }

    @CanIgnoreReturnValue
    @Nonnull
    public Pager<T> addAll(@Nonnull final Collection<Container<T>> elements) {
        content.addAll(elements);
        setDirty(true);
        return this;
    }

    @CanIgnoreReturnValue
    @Nonnull
    public Pager<T> remove(@Nonnull final T element) {
        return remove(new Container<>(element));
    }

    @CanIgnoreReturnValue
    @Nonnull
    public Pager<T> remove(@Nonnull final Container<T> element) {
        content.remove(element);
        setDirty(true);
        return this;
    }

    @CanIgnoreReturnValue
    @Nonnull
    public Pager<T> clear() {
        content.clear();
        setDirty(true);
        return this;
    }

    public boolean isEmpty() {
        return content.isEmpty();
    }

    public boolean contains(@Nonnull final T element) {
        return contains(new Container<>(element));
    }

    public boolean contains(@Nonnull final Container<T> element) {
        return content.contains(element);
    }

    public int size() {
        return content.size();
    }

    public int getPageFromIndex(final int index) {
        return (index / contentPerPage) + 1;
    }

    @CanIgnoreReturnValue
    @Nonnull
    public List<Container<T>> next() {
        if (hasNext()) currentPage++;
        setDirty(true);
        return getPage(currentPage);
    }

    @CanIgnoreReturnValue
    @Nonnull
    public List<Container<T>> previous() {
        if (hasPrevious()) currentPage--;
        setDirty(true);
        return getPage(currentPage);
    }

    @Nonnull
    public List<Container<T>> getCurrent() {
        return getPage(currentPage);
    }

    @Nonnull
    public List<Container<T>> getPageForward(final int forwardPages) {
        return getPage(currentPage + forwardPages);
    }

    @Nonnull
    public List<Container<T>> getPageBackward(final int backwardPages) {
        return getPage(currentPage - backwardPages);
    }

    @Nonnull
    public List<Container<T>> getPageLimited(final int elementsLimit) {
        return getPageLimited(currentPage, elementsLimit);
    }

    @Nonnull
    public List<Container<T>> getPageLimited(final int pageStart, final int elementsLimit) {
        return getPageLimited(pageStart, elementsLimit, null);
    }

    @Nonnull
    public List<Container<T>> getPageLimited(final int pageStart, final int elementsLimit, @Nullable final Function<Container<T>, Boolean> filter) {
        List<Container<T>> filtered = new ArrayList<>();
        for (int i = (pageStart - 1) * contentPerPage; i < content.size() && filtered.size() < elementsLimit; i++) {
            Container<T> container = content.get(i);
            if (filter == null || filter.apply(container)) {
                filtered.add(container);
            }
        }
        return filtered;
    }

    public List<Container<T>> getPage(final int page) {
        if (isOutbounds(page)) {
            return Collections.emptyList();
        }
        int start = (page - 1) * contentPerPage;
        int end = Math.min(page * contentPerPage, content.size());
        return new ArrayList<>(content.subList(start, end));
    }

    public boolean isOutbounds(final int page) {
        return page < 1 || page > getTotalPages();
    }

    @EqualsAndHashCode
    @Getter
    public static class Container<T> {
        public static final String NOTHING = "无标签";
        private final T data;
        private final Set<String> tags = new HashSet<>();
        @Setter
        private SlimefunItem slimefunItem = null;
        @Setter
        private ItemStack itemOnFrame = null;

        public Container(@Nonnull final T data) {
            this.data = data;
            if (data instanceof Location location) {
                this.slimefunItem = StorageCacheUtils.getSfItem(location);
            }
        }

        @Nonnull
        public String getTag() {
            return tags.stream().findFirst().orElse(NOTHING);
        }

        public void setTag(@Nonnull final String tag) {
            tags.clear();
            tags.add(tag);
        }

        public void addTag(@Nonnull final String tag) {
            tags.add(tag);
        }

        public void clearTags() {
            tags.clear();
        }
    }
}
