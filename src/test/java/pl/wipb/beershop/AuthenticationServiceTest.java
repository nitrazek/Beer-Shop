package pl.wipb.beershop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.wipb.beershop.dao.interfaces.AccountDao;
import pl.wipb.beershop.models.Account;
import pl.wipb.beershop.models.utils.AccountRole;
import pl.wipb.beershop.services.AuthenticationService;
import pl.wipb.beershop.utils.RequestParsers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {
    @Mock
    private AccountDao accountDao;
    @Mock
    private RequestParsers parsers;
    @InjectMocks
    private AuthenticationService authenticationService;

    private Map<String, String[]> parameterMap;
    private Map<String, String> fieldToError;

    @BeforeEach
    public void setUp() {
        parameterMap = new HashMap<>();
        fieldToError = new HashMap<>();
    }

    @Test
    public void testHandleLoginSuccessful() {
        Account account = new Account("user", "password", "email@test.com", AccountRole.CLIENT);
        when(parsers.parseLoginParams(parameterMap, fieldToError)).thenReturn(account);
        when(accountDao.findByLogin("user")).thenReturn(Optional.of(account));

        Account result = authenticationService.handleLogin(parameterMap, fieldToError);

        assertEquals(account, result);
        assertTrue(fieldToError.isEmpty());
    }

    @Test
    public void testHandleLoginInvalidLogin() {
        Account account = new Account("user", "password", "email@test.com", AccountRole.CLIENT);
        when(parsers.parseLoginParams(parameterMap, fieldToError)).thenReturn(account);
        when(accountDao.findByLogin("user")).thenReturn(Optional.empty());

        Account result = authenticationService.handleLogin(parameterMap, fieldToError);

        assertEquals(account, result);
        assertEquals("Konto o podanej nazwie nie istnieje.", fieldToError.get("login"));
    }

    @Test
    public void testHandleLoginInvalidPassword() {
        Account account = new Account("user", "password1", "email@test.com", AccountRole.CLIENT);
        Account accountFromDb = new Account("user", "password2", "email", AccountRole.CLIENT);
        when(parsers.parseLoginParams(parameterMap, fieldToError)).thenReturn(account);
        when(accountDao.findByLogin("user")).thenReturn(Optional.of(accountFromDb));

        Account result = authenticationService.handleLogin(parameterMap, fieldToError);

        assertEquals(account, result);
        assertEquals("Podano złe hasło.", fieldToError.get("password"));
    }

    @Test
    public void testHandleRegisterSuccessful() {
        Account account = new Account("user", "password", "email@test.com", AccountRole.CLIENT);
        when(parsers.parseRegisterParams(parameterMap, fieldToError)).thenReturn(account);
        when(accountDao.findByLogin("user")).thenReturn(Optional.empty());
        when(accountDao.findByEmail("email@test.com")).thenReturn(Optional.empty());

        Account result = authenticationService.handleRegister(parameterMap, fieldToError);

        verify(accountDao, times(1)).saveOrUpdate(account);
        assertEquals(account, result);
        assertTrue(fieldToError.isEmpty());
    }

    @Test
    public void testHandleRegisterExistingLogin() {
        Account account = new Account("user", "password", "email@test.com", AccountRole.CLIENT);
        when(parsers.parseRegisterParams(parameterMap, fieldToError)).thenReturn(account);
        when(accountDao.findByLogin("user")).thenReturn(Optional.of(account));

        Account result = authenticationService.handleRegister(parameterMap, fieldToError);

        assertEquals(account, result);
        assertEquals("Konto o podanym loginie już istnieje.", fieldToError.get("account"));
    }

    @Test
    public void testHandleRegisterExistingEmail() {
        Account account = new Account("user", "password", "email@test.com", AccountRole.CLIENT);
        when(parsers.parseRegisterParams(parameterMap, fieldToError)).thenReturn(account);
        when(accountDao.findByLogin(account.getLogin())).thenReturn(Optional.empty());
        when(accountDao.findByEmail("email@test.com")).thenReturn(Optional.of(account));

        Account result = authenticationService.handleRegister(parameterMap, fieldToError);

        assertEquals(account, result);
        assertEquals("Konto o podanym emailu już istnieje.", fieldToError.get("account"));
    }

    @Test
    public void testVerifyAccount() {
        Account account = new Account("user", "password", "email@test.com", AccountRole.CLIENT);
        when(accountDao.findByLogin("user")).thenReturn(Optional.of(account));

        assertTrue(authenticationService.verifyAccount("user"));
        assertFalse(authenticationService.verifyAccount(null));
        assertFalse(authenticationService.verifyAccount("nonExistentUser"));
    }

    @Test
    public void testVerifySeller() {
        Account sellerAccount = new Account("user", "password", "email@test.com", AccountRole.SELLER);
        when(accountDao.findByLogin("user")).thenReturn(Optional.of(sellerAccount));

        assertTrue(authenticationService.verifySeller("user"));
        assertFalse(authenticationService.verifySeller(null));
        assertFalse(authenticationService.verifySeller("nonExistentUser"));
    }

    @Test
    public void testVerifyAdmin() {
        Account adminAccount = new Account("user", "password", "email@test.com", AccountRole.ADMIN);
        when(accountDao.findByLogin("user")).thenReturn(Optional.of(adminAccount));

        assertTrue(authenticationService.verifyAdmin("user"));
        assertFalse(authenticationService.verifyAdmin(null));
        assertFalse(authenticationService.verifyAdmin("nonExistentUser"));
    }
}
