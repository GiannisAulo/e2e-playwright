import { Page, Locator, expect } from '@playwright/test';

export class ModalComponent {
  readonly page: Page;
  readonly modal: Locator;
  readonly title: Locator;
  readonly confirmButton: Locator;
  readonly cancelButton: Locator;
  readonly closeButton: Locator;

  constructor(page: Page) {
    this.page = page;
    this.modal = page.getByRole('dialog');
    this.title = this.modal.getByRole('heading');
    this.confirmButton = this.modal.getByRole('button', { name: /confirm|yes|ok/i });
    this.cancelButton = this.modal.getByRole('button', { name: /cancel|no/i });
    this.closeButton = this.modal.getByLabel('Close');
  }

  async expectVisible(title?: string): Promise<void> {
    await expect(this.modal).toBeVisible();
    if (title) {
      await expect(this.title).toHaveText(title);
    }
  }

  async confirm(): Promise<void> {
    await this.confirmButton.click();
    await expect(this.modal).toBeHidden();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
    await expect(this.modal).toBeHidden();
  }

  async close(): Promise<void> {
    await this.closeButton.click();
    await expect(this.modal).toBeHidden();
  }
}
