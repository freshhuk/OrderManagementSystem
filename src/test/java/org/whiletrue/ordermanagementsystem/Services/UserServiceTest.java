package org.whiletrue.ordermanagementsystem.Services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.whiletrue.ordermanagementsystem.Domain.Entity.User;
import org.whiletrue.ordermanagementsystem.Repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUser_userExists_returnsUser() {
        // Arrange
        Long userId = 1L;
        User mockUser = User.builder().id(userId).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        // Act
        Optional<User> result = userService.getUser(userId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(userId, result.get().getId());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void getUser_userDoesNotExist_returnsEmptyOptional() {
        // Arrange
        Long userId = 999L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userService.getUser(userId);

        // Assert
        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void deleteUser_executesDeletion() {
        // Arrange
        Long userId = 1L;

        // Act
        userService.deleteUser(userId);

        // Assert
        verify(userRepository, times(1)).deleteById(userId);
    }
}