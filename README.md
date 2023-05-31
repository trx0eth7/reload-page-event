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
