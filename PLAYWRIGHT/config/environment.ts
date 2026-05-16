export type Environment = 'local' | 'staging' | 'production';

export const ENV: Environment = (process.env.ENV as Environment) || 'local';

export const ENV_URLS: Record<Environment, string> = {
  local: 'http://localhost:3000',
  staging: 'https://staging.yourapp.com',
  production: 'https://yourapp.com',
};

export const API_URLS: Record<Environment, string> = {
  local: 'http://localhost:3000/api',
  staging: 'https://staging.yourapp.com/api',
  production: 'https://yourapp.com/api',
};
