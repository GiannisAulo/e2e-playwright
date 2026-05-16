import { test, expect } from '../../fixtures/custom-fixture';
import { USERS, INVALID_CREDENTIALS } from '../../test-data/users-data';

test.describe('Login - E2E', () => {
    test.beforeEach(async ({ loginPage }) => {
        await loginPage.goto();
    });

    test('should login successfully with valid credentials', async ({ loginPage }) => {
        await loginPage.login(USERS.standard.username, USERS.standard.password);
        await loginPage.expectRedirectedToDashboard();
    });

    test('should show error with invalid password', async ({ loginPage }) => {
        const { username, password } = INVALID_CREDENTIALS.wrongPassword;
        await loginPage.login(username, password);
        await loginPage.expectErrorVisible();
    });

    test('should show error with unknown user', async ({ loginPage }) => {
        const { username, password } = INVALID_CREDENTIALS.unknownUser;
        await loginPage.login(username, password);
        await loginPage.expectErrorVisible();
    });

    test('should show error when fields are empty', async ({ loginPage }) => {
        await loginPage.login('', '');
        await loginPage.expectErrorVisible();
    });

    test('dashboard should load after login', async ({ loginPage, dashboardPage }) => {
        await loginPage.login(USERS.standard.username, USERS.standard.password);
        await dashboardPage.expectLoaded();
    });

    test('should redirect to login after logout', async ({ dashboardPage, authenticatedPage }) => {
        await dashboardPage.logout();
        await authenticatedPage.waitForURL(/\/login/);
    });
});
