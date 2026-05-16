import { USERS, UserCredentials } from '../test-data/users-data';

export function pickUser(role: 'admin' | 'standard' | 'locked'): UserCredentials {
  return USERS[role];
}
