import { Page, Locator, expect } from '@playwright/test';
import { ROUTES } from '../utils/constants';

export class LoginPage {
    readonly page: Page;
    readonly usernameInput: Locator;
    readonly passwordInput: Locator;
    readonly loginButton: Locator;
    readonly errorMessage: Locator;

    constructor(page: Page) {
        this.page = page;
        this.usernameInput = page.getByTestId('username');
        this.passwordInput = page.getByTestId('password');
        this.loginButton = page.getByRole('button', { name: /login|sign in/i });
        this.errorMessage = page.getByTestId('error-message');
    }

    async goto(): Promise<void> {
        await this.page.goto(ROUTES.login);
    }

    async login(username: string, password: string): Promise<void> {
        await this.usernameInput.fill(username);
        await this.passwordInput.fill(password);
        await this.loginButton.click();
    }

    async expectErrorVisible(message?: string): Promise<void> {
        await expect(this.errorMessage).toBeVisible();
        if (message) {
            await expect(this.errorMessage).toContainText(message);
        }
    }

    async expectRedirectedToDashboard(): Promise<void> {
        await this.page.waitForURL(`**${ROUTES.dashboard}`);
    }
}
