package com.nested.conf;

import com.nested.entity.Snippet;
import com.nested.repository.SnippetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * Created by elizbor on 01.03.2017.
 */
@Component
public class AppLoader implements ApplicationListener<ContextRefreshedEvent> {

    private SnippetRepository snippetRepository;

    @Autowired
    public AppLoader(SnippetRepository snippetRepository) {
        this.snippetRepository = snippetRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        Snippet snippet1 = new Snippet();
        snippet1.setTextDE("1");
        snippet1.setTextEN("12");
        snippet1.setIndent(0);
        snippet1.setCollapsed(false);
        snippet1.setOrderSnippet(0);

        Snippet snippet2 = new Snippet();
        snippet2.setTextDE("2");
        snippet2.setTextEN("22");
        snippet2.setIndent(1);
        snippet2.setCollapsed(false);
        snippet2.setOrderSnippet(1);

        Snippet snippet3 = new Snippet();
        snippet3.setTextDE("3");
        snippet3.setTextEN("32");
        snippet3.setIndent(2);
        snippet3.setCollapsed(false);
        snippet3.setOrderSnippet(2);

        Snippet snippet4 = new Snippet();
        snippet4.setTextDE("4");
        snippet4.setTextEN("42");
        snippet4.setIndent(1);
        snippet4.setCollapsed(false);
        snippet4.setOrderSnippet(3);

        Snippet snippet5 = new Snippet();
        snippet5.setTextDE("5");
        snippet5.setTextEN("52");
        snippet5.setIndent(0);
        snippet5.setCollapsed(false);
        snippet5.setOrderSnippet(4);

        Snippet snippet6 = new Snippet();
        snippet6.setTextDE("Content or form, or nothing here. Whatever you want.Content or form, or nothing here. Whatever you want.Content or form, or nothing here. Whatever you want.");
        snippet6.setTextEN("Content or form, or nothing here. Whatever you want.Content or form, or nothing here. Whatever you want.Content or form, or nothing here. Whatever you want.");
        snippet6.setIndent(0);
        snippet6.setCollapsed(false);
        snippet6.setOrderSnippet(5);

        snippetRepository.save(snippet1);
        snippetRepository.save(snippet2);
        snippetRepository.save(snippet3);
        snippetRepository.save(snippet4);
        snippetRepository.save(snippet5);
        snippetRepository.save(snippet6);
    }
}
