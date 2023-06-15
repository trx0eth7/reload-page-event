package ru.trx.reloadpageevent.screen;

import io.jmix.ui.event.UIRefreshEvent;

/**
 * This interface defines custom refresh behavior for browser screen,
 * which is triggered by {@link UIRefreshEvent}
 */
public interface RefreshablePageScreen {

    /**
     * This method needs to custom reloading data logic
     */
    void refreshPage();
}
