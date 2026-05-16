import { test as base, Page } from '@playwright/test';
import { LoginPage } from '../pages/login-page';
import { DashboardPage } from '../pages/dashboard-page';
import { HeaderComponent } from '../components/header-component';
import { ModalComponent } from '../components/modal-component';
import { NotificationsComponent } from '../components/notifications-component';
import { USERS } from '../test-data/users-data';

type PageFixtures = {
    loginPage: LoginPage;
    dashboardPage: DashboardPage;
    header: HeaderComponent;
    modal: ModalComponent;
    notifications: NotificationsComponent;
    authenticatedPage: Page;
};

export const test = base.extend<PageFixtures>({
    loginPage: async ({ page }, use) => {
        await use(new LoginPage(page));
    },

    dashboardPage: async ({ page }, use) => {
        await use(new DashboardPage(page));
    },

    header: async ({ page }, use) => {
        await use(new HeaderComponent(page));
    },

    modal: async ({ page }, use) => {
        await use(new ModalComponent(page));
    },

    notifications: async ({ page }, use) => {
        await use(new NotificationsComponent(page));
    },

    authenticatedPage: async ({ page }, use) => {
        const loginPage = new LoginPage(page);
        await loginPage.goto();
        await loginPage.login(USERS.standard.username, USERS.standard.password);
        await loginPage.expectRedirectedToDashboard();
        await use(page);
    },
});

export { expect } from '@playwright/test';
