package com.example.demo.service;

import com.example.demo.dto.res.SuggestItemRes;
import com.example.demo.entity.Article;
import com.example.demo.entity.Campaign;
import com.example.demo.entity.Event;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.CampaignRepository;
import com.example.demo.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class SearchSuggestService {
    private static final int MIN_QUERY_LEN = 1;

    private final CampaignRepository campaignRepository;
    private final ArticleRepository articleRepository;
    private final EventRepository eventRepository;

    @Transactional(readOnly = true)
    public List<SuggestItemRes> suggest(String q, int limit) {
        if (q == null || q.trim().length() < MIN_QUERY_LEN) {
            return List.of();
        }
        String term = q.trim();
        int cap = Math.min(Math.max(limit, 1), 30);

        List<SuggestItemRes> staticPages = staticPageSuggestions(term);
        List<SuggestItemRes> merged = new ArrayList<>(staticPages);
        if (merged.size() >= cap) {
            return merged.subList(0, cap);
        }

        int remaining = cap - merged.size();
        int perType = Math.max(1, (remaining + 2) / 3);

        List<SuggestItemRes> campaigns = campaignRepository
                .searchByKeyword(term, PageRequest.of(0, perType))
                .getContent()
                .stream()
                .map(this::toCampaignSuggest)
                .toList();

        List<SuggestItemRes> articles = articleRepository
                .searchByKeyword(term, PageRequest.of(0, perType))
                .getContent()
                .stream()
                .map(this::toArticleSuggest)
                .toList();

        List<SuggestItemRes> events = eventRepository
                .searchByKeyword(term, PageRequest.of(0, perType))
                .getContent()
                .stream()
                .map(this::toEventSuggest)
                .toList();

        merged.addAll(roundRobinMerge(List.of(campaigns, articles, events), remaining));
        return merged.subList(0, Math.min(merged.size(), cap));
    }

    private List<SuggestItemRes> staticPageSuggestions(String term) {
        String lower = term.toLowerCase(Locale.ROOT);
        List<StaticPage> defs = List.of(
                new StaticPage("Home", "Back to homepage", "/", "home", "homepage", "start"),
                new StaticPage("About us", "About the organization", "/about", "about", "intro", "organization", "who we are"),
                new StaticPage("Contact", "Form & information", "/contact", "contact", "reach", "support"),
                new StaticPage("Sponsors", "Sponsoring partners", "/sponsors", "sponsor", "sponsors", "funding"),
                new StaticPage("Partners", "Partner signup", "/partners", "partner", "partners", "collaborate"),
                new StaticPage("Partner login", "Partner portal", "/partner-login", "partner login", "sign in", "portal"),
                new StaticPage("Donate", "Support our fund", "/donate", "donate", "donation", "give"),
                new StaticPage("News & stories", "Latest articles", "/news", "news", "stories", "articles", "blog"),
                new StaticPage("Campaigns", "Campaign list", "/campaign", "campaign", "campaigns", "projects"),
                new StaticPage("Events", "Event calendar", "/events", "event", "events", "activities")
        );
        return defs.stream()
                .filter(d -> d.matches(lower))
                .map(d -> new SuggestItemRes("page", null, d.title, d.hint, d.path))
                .limit(6)
                .toList();
    }

    private record StaticPage(String title, String hint, String path, String... keys) {
        boolean matches(String lower) {
            for (String k : keys) {
                String key = k.toLowerCase(Locale.ROOT);
                if (key.contains(lower) || lower.contains(key)) {
                    return true;
                }
            }
            return false;
        }
    }

    private SuggestItemRes toCampaignSuggest(Campaign c) {
        String title = c.getTitle() != null ? c.getTitle() : "";
        String hint = c.getLocation() != null && !c.getLocation().isBlank() ? c.getLocation() : "Campaign";
        return new SuggestItemRes("campaign", c.getId(), title, hint, null);
    }

    private SuggestItemRes toArticleSuggest(Article a) {
        String title = a.getTitle() != null ? a.getTitle() : "";
        String hint = a.getCategory() != null && a.getCategory().getName() != null
                ? a.getCategory().getName()
                : "Article";
        return new SuggestItemRes("article", a.getId(), title, hint, null);
    }

    private SuggestItemRes toEventSuggest(Event e) {
        String title = e.getTitle() != null ? e.getTitle() : "";
        String hint = e.getLocation() != null && !e.getLocation().isBlank() ? e.getLocation() : "Event";
        return new SuggestItemRes("event", e.getId(), title, hint, null);
    }

    private static List<SuggestItemRes> roundRobinMerge(List<List<SuggestItemRes>> lists, int max) {
        List<SuggestItemRes> out = new ArrayList<>();
        int row = 0;
        while (out.size() < max) {
            boolean added = false;
            for (List<SuggestItemRes> list : lists) {
                if (row < list.size()) {
                    out.add(list.get(row));
                    added = true;
                    if (out.size() >= max) {
                        return out;
                    }
                }
            }
            if (!added) {
                break;
            }
            row++;
        }
        return out;
    }
}
