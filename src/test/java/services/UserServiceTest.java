package services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.ryjan.telegram.config.RedisConfig;
import org.ryjan.telegram.interfaces.repos.jpa.UserRepository;
import org.ryjan.telegram.kafka.UserProducer;
import org.ryjan.telegram.model.users.User;
import org.ryjan.telegram.services.UserService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RedisTemplate<String, User> userRedisTemplate;

    @Mock
    private ValueOperations<String, User> valueOperations;

    @Mock
    private UserProducer userProducer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(userRedisTemplate.opsForValue()).thenReturn(valueOperations);

        when(userProducer.findUser(anyLong())).thenReturn(CompletableFuture.completedFuture(null));
    }

    @Test
    void testCreateUser_UserDoesNotExist() {
        User user = new User(1L, "testUser");

        when(valueOperations.get(anyString())).thenReturn(null);

        User result = userService.createUser(user);

        assertNotNull(result);
        assertEquals(user, result);

        verify(valueOperations, times(2)).get(anyString());
        verify(userProducer, times(1)).sendUser(user);
    }

    @Test
    void testCreateUser_UserAlreadyExists() {
        User user = new User(1L, "testUser");

        when(valueOperations.get(anyString())).thenReturn(user);

        User result = userService.createUser(user);

        assertNotNull(result);
        assertEquals(user, result);

        verify(valueOperations, times(2)).get(anyString());
        verify(userProducer, never()).sendUser(any());
    }

    @Test
    void testFindUser_FromRedis() {
        String redisKey = RedisConfig.USER_CACHE_KEY;
        Long userId = 1L;
        User user = new User(userId, "testUser");

        when(valueOperations.get(redisKey + userId)).thenReturn(user);

        User result = userService.findUser(userId);

        assertNotNull(result);
        assertEquals(user, result);
        verify(valueOperations, times(1)).get(redisKey + userId);
        verify(userProducer, never()).findUser(anyLong());
    }

    @Test
    void testFindUser_FromKafka() {
        Long userId = 1L;
        User user = new User(userId, "testUser");

        when(valueOperations.get(anyString())).thenReturn(null).thenReturn(user);
        when(userProducer.findUser(userId)).thenReturn(CompletableFuture.completedFuture(null));

        User result = userService.findUser(userId);

        assertNotNull(result);
        assertEquals(user, result);

        verify(valueOperations, times(2)).get(anyString());
        verify(userProducer, times(1)).findUser(userId);
    }
}
