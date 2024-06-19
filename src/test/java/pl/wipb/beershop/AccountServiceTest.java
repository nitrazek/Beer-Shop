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
import pl.wipb.beershop.services.AccountService;
import pl.wipb.beershop.utils.AccountFilterOptions;
import pl.wipb.beershop.utils.RequestParsers;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @Mock
    private AccountDao accountDao;
    @Mock
    private RequestParsers parsers;
    @InjectMocks
    private AccountService accountService;

    private Map<String, String[]> parameterMap;
    private Map<String, String> fieldToError;

    @BeforeEach
    public void setUp() {
        parameterMap = new HashMap<>();
        fieldToError = new HashMap<>();
    }

    @Test
    void testGetAccount_ExistingId() {
        Long accountId = 1L;
        Account account = new Account("user", "password", "email@test.com");
        when(accountDao.findById(accountId)).thenReturn(Optional.of(account));

        Account result = accountService.getAccount(accountId);

        assertEquals(account, result);
        verify(accountDao, times(1)).findById(accountId);
    }

    @Test
    void testGetAccount_NonExistingId() {
        Long accountId = 1L;
        when(accountDao.findById(accountId)).thenReturn(Optional.empty());

        Account result = accountService.getAccount(accountId);

        assertNull(result);
        verify(accountDao, times(1)).findById(accountId);
    }

    @Test
    void testGetAllAccounts() {
        List<Account> expectedAccounts = Arrays.asList(
                new Account("user1", "password1", "email1@test.com"),
                new Account("user2", "password2", "email2@test.com")
        );
        when(accountDao.findAll()).thenReturn(expectedAccounts);

        List<Account> result = accountService.getAllAccounts();

        assertEquals(expectedAccounts.size(), result.size());
        assertEquals(expectedAccounts, result);
        verify(accountDao, times(1)).findAll();
    }

    @Test
    void testGetFilteredAccountList() {
        AccountFilterOptions filterOptions = new AccountFilterOptions("user", "email1@test.com", false, false, true);

        when(parsers.parseAccountFilterParams(parameterMap, fieldToError)).thenReturn(filterOptions);
        when(accountDao.findAll()).thenReturn(Arrays.asList(
                new Account("user1", "password1", "email1@test.com"),
                new Account("user2", "password2", "email2@test.com")
        ));

        List<Account> result = accountService.getFilteredAccountList(parameterMap, fieldToError);

        assertEquals(1, result.size());
        assertEquals("user1", result.get(0).getLogin());
        verify(parsers, times(1)).parseAccountFilterParams(parameterMap, fieldToError);
        verify(accountDao, times(1)).findAll();
    }

    @Test
    void testAddOrEditAccount() {
        Account account = new Account("user", "password", "email@test.com");
        when(parsers.parseAddEditAccountParams(parameterMap, fieldToError)).thenReturn(account);

        accountService.addOrEditAccount(parameterMap, fieldToError);

        verify(accountDao, times(1)).saveOrUpdate(account);
    }

    @Test
    void testDeleteAccount_ExistingId() {
        Long accountId = 1L;
        Account account = new Account("user", "password", "email@test.com");
        when(accountDao.findById(accountId)).thenReturn(Optional.of(account));

        accountService.deleteAccount(accountId, new HashMap<>());

        verify(accountDao, times(1)).delete(any(Account.class));
    }

    @Test
    void testDeleteAccount_NonExistingId() {
        Long accountId = 999L;
        when(accountDao.findById(accountId)).thenReturn(Optional.empty());

        accountService.deleteAccount(accountId, fieldToError);

        assertTrue(fieldToError.containsKey("param"));
        assertEquals("Account o podanym id nie istnieje.", fieldToError.get("param"));
        verify(accountDao, never()).delete(any(Account.class));
    }
}
