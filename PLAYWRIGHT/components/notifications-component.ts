import { Page, Locator, expect } from '@playwright/test';

export class NotificationsComponent {
  readonly page: Page;
  readonly toastContainer: Locator;
  readonly successToast: Locator;
  readonly errorToast: Locator;
  readonly warningToast: Locator;

  constructor(page: Page) {
    this.page = page;
    this.toastContainer = page.getByTestId('toast-container');
    this.successToast = page.getByTestId('toast-success');
    this.errorToast = page.getByTestId('toast-error');
    this.warningToast = page.getByTestId('toast-warning');
  }

  async expectSuccess(message?: string): Promise<void> {
    await expect(this.successToast).toBeVisible();
    if (message) {
      await expect(this.successToast).toContainText(message);
    }
  }

  async expectError(message?: string): Promise<void> {
    await expect(this.errorToast).toBeVisible();
    if (message) {
      await expect(this.errorToast).toContainText(message);
    }
  }

  async waitForToastToDisappear(): Promise<void> {
    await expect(this.toastContainer).toBeHidden({ timeout: 10_000 });
  }
}
