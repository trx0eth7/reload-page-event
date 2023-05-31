package ru.trx.reloadpageevent.screen.user;

import io.jmix.ui.Notifications;
import io.jmix.ui.model.CollectionLoader;
import io.jmix.ui.navigation.Route;
import io.jmix.ui.screen.LookupComponent;
import io.jmix.ui.screen.UiController;
import io.jmix.ui.screen.UiDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import ru.trx.reloadpageevent.entity.User;
import ru.trx.reloadpageevent.screen.refresh.RefreshableBrowse;

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