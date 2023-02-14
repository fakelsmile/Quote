package com.fakelsmile.quote.service.services;

import com.fakelsmile.quote.service.errors.UserAlreadyExistException;
import com.fakelsmile.quote.service.errors.UserNotFoundException;
import com.fakelsmile.quote.service.models.entity.UserEntity;
import com.fakelsmile.quote.service.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    private UserEntity user;
    private UserEntity savedUser;

    @BeforeEach
    public void setup() {
        user = UserEntity.builder()
                .id(1L)
                .name("Zaph")
                .email("Zaph@gmail.com")
                .password("qwerty123")
                .build();

        savedUser = UserEntity.builder()
                .id(1L)
                .name("Zaph")
                .email("Zaph@gmail.com")
                .password("qwerty123")
                .build();
    }

    @AfterEach
    public void turnDown() {
        verifyNoMoreInteractions(userRepository);
    }

    @DisplayName("JUnit test for saveUser method")
    @Test
    public void saveUserTest() throws UserAlreadyExistException {
        when(userRepository.findByNameOrEmail(user.getName(), user.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(savedUser);
        UserEntity actual = userService.saveUser(user);

        assertEquals(savedUser, actual);
        verify(userRepository).save(user);
        verify(userRepository).findByNameOrEmail(user.getName(), user.getEmail());
    }

    @DisplayName("JUnit test for getUser method")
    @Test
    public void getUserTest() throws UserNotFoundException {
        when(userRepository.findById(1L)).thenReturn(Optional.of(savedUser));
        UserEntity actual = userService.getUser(savedUser.getId());

        assertEquals(savedUser, actual);
        verify(userRepository).findById(1L);
    }

    @DisplayName("JUnit test for getUser method with error")
    @Test
    public void getUserWithErrorTest() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.getUser(savedUser.getId()));

        verify(userRepository).findById(1L);
    }
}
