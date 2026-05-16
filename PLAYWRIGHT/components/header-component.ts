import { Page, Locator, expect } from '@playwright/test';

export class HeaderComponent {
  readonly page: Page;
  readonly logo: Locator;
  readonly navLinks: Locator;
  readonly userAvatar: Locator;

  constructor(page: Page) {
    this.page = page;
    this.logo = page.getByTestId('header-logo');
    this.navLinks = page.getByRole('navigation').getByRole('link');
    this.userAvatar = page.getByTestId('user-avatar');
  }

  async expectVisible(): Promise<void> {
    await expect(this.logo).toBeVisible();
  }

  async clickLogo(): Promise<void> {
    await this.logo.click();
  }

  async navigateTo(label: string): Promise<void> {
    await this.navLinks.filter({ hasText: label }).click();
  }
}
