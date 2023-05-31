package ru.trx.reloadpageevent.screen.refresh;

import io.jmix.ui.event.UIRefreshEvent;
import io.jmix.ui.screen.StandardLookup;
import org.springframework.context.event.EventListener;

public abstract class RefreshableBrowse<T> extends StandardLookup<T> {
    protected abstract void refresh();

    @EventListener
    protected void onPageRefresh(UIRefreshEvent event) {
        refresh();
    }
}
