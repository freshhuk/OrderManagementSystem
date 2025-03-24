package org.whiletrue.ordermanagementsystem.Services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.whiletrue.ordermanagementsystem.Domain.Entity.User;
import org.whiletrue.ordermanagementsystem.Repository.UserRepository;

import java.util.Optional;

/**
 * Service class responsible for user-related operations such as retrieving and deleting users.
 * This service acts as a middle layer between the controller and the repository, allowing
 * business logic to be applied if necessary before interacting with the database.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    /**
     * Constructs a UserService with the given UserRepository.
     *
     * @param userRepository The repository used to access User data.
     */
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param id The ID of the user to retrieve.
     * @return An {@link Optional} containing the user if found, or empty if not found.
     */
    public Optional<User> getUser(Long id) {
        logger.info("Attempting to retrieve user with ID: {}", id);
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            logger.info("User found with ID: {}", id);
        } else {
            logger.warn("User not found with ID: {}", id);
        }
        return user;
    }

    /**
     * Deletes a user from the system by their unique identifier.
     * <p>
     * Note: This operation is irreversible. Make sure to handle related
     * data (e.g., orders, authentication) before calling this method.
     *
     * @param id The ID of the user to delete.
     */
    public void deleteUser(Long id) {
        logger.info("Deleting user with ID: {}", id);
        userRepository.deleteById(id);
        logger.info("User with ID {} has been deleted", id);
    }
}
