export const TIMEOUTS = {
  short: 5_000,
  medium: 15_000,
  long: 30_000,
  navigation: 60_000,
} as const;

export const ROUTES = {
  login: '/login',
  dashboard: '/dashboard',
  users: '/users',
  profile: '/profile',
} as const;

export const STORAGE_KEYS = {
  authToken: 'authToken',
  userSession: 'userSession',
} as const;
