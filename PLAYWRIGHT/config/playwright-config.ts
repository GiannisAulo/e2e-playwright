import type { PlaywrightTestOptions } from '@playwright/test';

export const sharedUseConfig: Partial<PlaywrightTestOptions> = {
  ignoreHTTPSErrors: true,
  actionTimeout: 15_000,
  navigationTimeout: 30_000,
};
