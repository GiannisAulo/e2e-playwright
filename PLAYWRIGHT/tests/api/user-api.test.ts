import { test, expect } from '@playwright/test';
import { API_BASE_URL } from '../../utils/env';
import { USERS } from '../../test-data/users-data';

test.describe('User API', () => {
    let authToken: string;

    test.beforeAll(async ({ request }) => {
        const response = await request.post(`${API_BASE_URL}/auth/login`, {
            data: {
                username: USERS.standard.username,
                password: USERS.standard.password,
            },
        });
        expect(response.ok()).toBeTruthy();
        const body = await response.json();
        authToken = body.token;
    });

    test('GET /users - should return user list', async ({ request }) => {
        const response = await request.get(`${API_BASE_URL}/users`, {
            headers: { Authorization: `Bearer ${authToken}` },
        });
        expect(response.ok()).toBeTruthy();
        const users = await response.json();
        expect(Array.isArray(users)).toBeTruthy();
    });

    test('GET /users/:id - should return a single user', async ({ request }) => {
        const response = await request.get(`${API_BASE_URL}/users/1`, {
            headers: { Authorization: `Bearer ${authToken}` },
        });
        expect(response.status()).toBe(200);
        const user = await response.json();
        expect(user).toHaveProperty('id');
        expect(user).toHaveProperty('username');
    });

    test('POST /users - should create a new user', async ({ request }) => {
        const response = await request.post(`${API_BASE_URL}/users`, {
            headers: { Authorization: `Bearer ${authToken}` },
            data: {
                username: `testuser_${Date.now()}`,
                email: `test_${Date.now()}@example.com`,
                role: 'standard',
            },
        });
        expect([200, 201]).toContain(response.status());
    });

    test('should return 401 without auth token', async ({ request }) => {
        const response = await request.get(`${API_BASE_URL}/users`);
        expect(response.status()).toBe(401);
    });
});
