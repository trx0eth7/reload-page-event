package ru.trx.reloadpageevent.screen.main;

import io.jmix.ui.ScreenTools;
import io.jmix.ui.Screens;
import io.jmix.ui.component.AppWorkArea;
import io.jmix.ui.component.Button;
import io.jmix.ui.component.Window;
import io.jmix.ui.component.mainwindow.Drawer;
import io.jmix.ui.event.UIRefreshEvent;
import io.jmix.ui.icon.JmixIcon;
import io.jmix.ui.navigation.Route;
import io.jmix.ui.screen.LookupScreen;
import io.jmix.ui.screen.Screen;
import io.jmix.ui.screen.Subscribe;
import io.jmix.ui.screen.UiController;
import io.jmix.ui.screen.UiControllerUtils;
import io.jmix.ui.screen.UiDescriptor;
import io.jmix.ui.sys.UiComponentsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import ru.trx.reloadpageevent.screen.RefreshablePageScreen;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

@UiController("rpe_MainScreen")
@UiDescriptor("main-screen.xml")
@Route(path = "main", root = true)
public class MainScreen extends Screen implements Window.HasWorkArea {
    private static final Logger log = LoggerFactory.getLogger(MainScreen.class);

    @Autowired
    private ScreenTools screenTools;

    @Autowired
    private AppWorkArea workArea;

    @Autowired
    private Drawer drawer;

    @Autowired
    private Button collapseDrawerButton;

    @Autowired
    private Screens screens;


    @Override
    public AppWorkArea getWorkArea() {
        return workArea;
    }

    @Subscribe("collapseDrawerButton")
    private void onCollapseDrawerButtonClick(Button.ClickEvent event) {
        drawer.toggle();
        if (drawer.isCollapsed()) {
            collapseDrawerButton.setIconFromSet(JmixIcon.CHEVRON_RIGHT);
        } else {
            collapseDrawerButton.setIconFromSet(JmixIcon.CHEVRON_LEFT);
        }
    }

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        screenTools.openDefaultScreen(
                UiControllerUtils.getScreenContext(this).getScreens());

        screenTools.handleRedirect();
    }

    /**
     * Refresh only active screen for current user session
     * <p>
     * NOTE: Subscribers to this event are initialized
     * at the stage of creating composite components {@link UiComponentsImpl#initCompositeComponent},
     * so it's a reasonable decision to subscribe on this event here
     *
     * @param event client reload page event
     */
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

    @Nullable
    private Screen lastScreen(Collection<Screen> screens) {
        if (screens.isEmpty()) {
            return null;
        }

        if (screens instanceof List) {
            return ((List<Screen>) screens).get(screens.size() - 1);
        }

        Screen last = null;

        for (Screen screen : screens) {
            last = screen;
        }

        return last;
    }
}
