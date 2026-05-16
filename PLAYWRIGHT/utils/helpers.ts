import { Page } from '@playwright/test';

export async function waitForNetworkIdle(page: Page, timeout = 5000): Promise<void> {
  await page.waitForLoadState('networkidle', { timeout });
}

export function generateRandomEmail(): string {
  const timestamp = Date.now();
  return `test.user.${timestamp}@example.com`;
}

export function generateRandomString(length = 8): string {
  return Math.random().toString(36).substring(2, 2 + length);
}

export async function clearAndType(page: Page, selector: string, text: string): Promise<void> {
  await page.fill(selector, '');
  await page.fill(selector, text);
}

export async function retryAction<T>(
  action: () => Promise<T>,
  retries = 3,
  delay = 500
): Promise<T> {
  for (let i = 0; i < retries; i++) {
    try {
      return await action();
    } catch (error) {
      if (i === retries - 1) throw error;
      await new Promise((resolve) => setTimeout(resolve, delay));
    }
  }
  throw new Error('Retry limit reached');
}
