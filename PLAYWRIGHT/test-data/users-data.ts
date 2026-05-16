export interface UserCredentials {
  username: string;
  password: string;
  email?: string;
  role: 'admin' | 'standard' | 'locked';
}

export const USERS: Record<string, UserCredentials> = {
  admin: {
    username: 'admin_user',
    password: 'Admin@12345',
    email: 'admin@example.com',
    role: 'admin',
  },
  standard: {
    username: 'standard_user',
    password: 'User@12345',
    email: 'user@example.com',
    role: 'standard',
  },
  locked: {
    username: 'locked_user',
    password: 'Locked@12345',
    email: 'locked@example.com',
    role: 'locked',
  },
};

export const INVALID_CREDENTIALS = {
  wrongPassword: { username: 'standard_user', password: 'WrongPassword' },
  unknownUser: { username: 'ghost_user', password: 'GhostPass@1' },
  emptyFields: { username: '', password: '' },
};
