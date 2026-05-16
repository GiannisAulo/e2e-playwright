import { Page, Locator, expect } from '@playwright/test';
import { ROUTES } from '../utils/constants';

export class DashboardPage {
    readonly page: Page;
    readonly heading: Locator;
    readonly userMenu: Locator;
    readonly logoutButton: Locator;

    constructor(page: Page) {
        this.page = page;
        this.heading = page.getByRole('heading', { name: /dashboard/i });
        this.userMenu = page.getByTestId('user-menu');
        this.logoutButton = page.getByRole('button', { name: /logout|sign out/i });
    }

    async goto(): Promise<void> {
        await this.page.goto(ROUTES.dashboard);
    }

    async expectLoaded(): Promise<void> {
        await expect(this.heading).toBeVisible();
        await expect(this.page).toHaveURL(new RegExp(ROUTES.dashboard));
    }

    async logout(): Promise<void> {
        await this.userMenu.click();
        await this.logoutButton.click();
    }
}
