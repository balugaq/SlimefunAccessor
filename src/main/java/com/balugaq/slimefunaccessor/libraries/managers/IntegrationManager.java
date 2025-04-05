package com.balugaq.slimefunaccessor.libraries.managers;

import com.balugaq.jeg.api.groups.SearchGroup;
import com.balugaq.jeg.api.objects.enums.FilterType;
import com.balugaq.jeg.implementation.JustEnoughGuide;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.libraries.dough.collections.Pair;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class IntegrationManager extends Manager {
    @Getter
    private static boolean JEGLoaded;
    @Getter
    private static Boolean pinyin;
    public IntegrationManager(@Nonnull JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void load() {
        JEGLoaded = Bukkit.getPluginManager().isPluginEnabled("JustEnoughGuide");
    }

    @Override
    public void unload() {
        JEGLoaded = false;
    }

    public static boolean isSearchApplicable(@Nonnull Player player, @Nonnull SlimefunItem slimefunItem, @Nonnull String searchTerm) {
        if (!JEGLoaded) {
            return false;
        }

        if (pinyin == null) {
            pinyin = JustEnoughGuide.getConfigManager().isPinyinSearch();
        }

        Pair<String, Map<FilterType, String>> parsedSearchTerm = parseSearchTerm(searchTerm);
        String actualSearchTerm = parsedSearchTerm.getFirstValue();
        if (actualSearchTerm == null || actualSearchTerm.isBlank()) {
            return true;
        }

        Map<FilterType, String> filters = parsedSearchTerm.getSecondValue();
        if (filters != null && !filters.isEmpty()) {
            Set<SlimefunItem> item = new HashSet<>();
            item.add(slimefunItem);
            for (Map.Entry<FilterType, String> filter : filters.entrySet()) {
                item = SearchGroup.filterItems(player, filter.getKey(), filter.getValue(), pinyin, item);
                if (item.isEmpty()) {
                    return false;
                }
            }
        }

        return SearchGroup.isSearchFilterApplicable(slimefunItem, actualSearchTerm, pinyin);
    }

    @Nonnull
    public static Pair<String, Map<FilterType, String>> parseSearchTerm(@Nonnull String searchTerm) {
        StringBuilder actualSearchTermBuilder = new StringBuilder();
        String[] split = searchTerm.split(" ");
        Map<FilterType, String> filters = new HashMap<>();

        for (String s : split) {
            boolean isFilter = false;

            for (FilterType filterType : FilterType.values()) {
                if (s.startsWith(filterType.getFlag()) && s.length() > filterType.getFlag().length()) {
                    isFilter = true;
                    String filterValue = s.substring(filterType.getFlag().length());
                    filters.put(filterType, filterValue);
                    break;
                }
            }

            if (!isFilter) {
                actualSearchTermBuilder.append(s).append(" ");
            }
        }

        String actualSearchTerm = actualSearchTermBuilder.toString().trim();

        for (FilterType filterType : FilterType.values()) {
            String flag = filterType.getFlag();
            actualSearchTerm = actualSearchTerm.replaceAll(Pattern.quote(flag), Matcher.quoteReplacement(flag));
        }

        return new Pair<>(actualSearchTerm.toLowerCase(), filters);
    }
}
