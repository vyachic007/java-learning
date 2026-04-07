package by.slava_borisov.hoteladmin.service;

import by.slava_borisov.hoteladmin.dao.UserDao;
import by.slava_borisov.hoteladmin.model.User;
import by.slava_borisov.hoteladmin.util.Messages;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    @Test
    void loadUserByUsername() {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("password");

        when(userDao.findByUsername("admin")).thenReturn(Optional.of(user));

        UserDetails result = userDetailsService.loadUserByUsername("admin");

        assertNotNull(result);
        assertEquals("admin", result.getUsername());
        assertEquals("password", result.getPassword());

        verify(userDao).findByUsername("admin");
    }

    @Test
    void loadUserByUsernameShouldThrowExceptionWhenUserNotFound() {
        when(userDao.findByUsername("unknown")).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("unknown")
        );

        assertEquals(
                String.format(Messages.USER_NOT_FOUND, "unknown"),
                exception.getMessage()
        );

        verify(userDao).findByUsername("unknown");
    }
}