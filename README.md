# When browser is refreshed how do you know about that

**Purpose**

required to reload data when the user did a page reload

**How to do it by Jmix**

Jmix event: io.jmix.ui.event.UIRefreshEvent

How could use it, code example:

```groovy
public abstract class RefreshableBrowse<T> extends StandardLookup<T> {

    protected abstract void refresh();

    @EventListener
    protected void onPageRefresh(UIRefreshEvent event) {
        refresh();
    }
} 

@UiController("rpe_User.browse")
@UiDescriptor("user-browse.xml")
@LookupComponent("usersTable")
@Route("users")
public class UserBrowse extends RefreshableBrowse<User> {

    @Autowired
    protected CollectionLoader<User> usersDl;

    @Autowired
    protected Notifications notifications;

    @Override
    protected void refresh() {
        usersDl.load();

        notifications.create(Notifications.NotificationType.HUMANIZED)
                .withCaption("User data was reloaded")
                .withPosition(Notifications.Position.BOTTOM_RIGHT)
                .withHideDelayMs(3000)
                .show();
    }
}

```

**Approach for all Lookup screens**

By default, all lookup screens support data reloading

To support specific refreshable behaviour, it's necessary:
1. To implement `com.ats.psz.ui.screen.RefreshablePageScreen` contract in any screen
2. To implement `refreshPage()` method, e.g. loading data `processInstancesDl.load()`
 
```groovy
@EventListener
    public void onRefresh(UIRefreshEvent event) {
        Collection<Screen> dialogScreens = screens.getOpenedScreens().getDialogScreens();
        Collection<Screen> currentBreadcrumbs = screens.getOpenedScreens().getCurrentBreadcrumbs();

        Screen activeScreen = lastScreen(dialogScreens);

        if (activeScreen == null && !currentBreadcrumbs.isEmpty()) {
            activeScreen = currentBreadcrumbs.iterator().next();
        }

        if (activeScreen instanceof RefreshablePageScreen) {
            log.info("Custom refreshes {} screen", activeScreen.getId());
            ((RefreshablePageScreen) activeScreen).refreshPage();
        } else if (activeScreen instanceof LookupScreen) {
            log.info("Reloads all dataloader to {} screen", activeScreen.getId());
            UiControllerUtils.getScreenData(activeScreen).loadAll();
        }
    }
```
NOTE: use carefully
