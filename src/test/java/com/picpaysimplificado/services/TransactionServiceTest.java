package com.picpaysimplificado.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.picpaysimplificado.domain.dtos.TransactionDTO;
import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.domain.user.UserType;
import com.picpaysimplificado.repositories.TransactionRepository;


public class TransactionServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private TransactionRepository repository;
 
    @Mock
    private AuthorizationService authService;

    @Mock
    private NotificationService notificationService;

    @Autowired
    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Should create a transaction")
    void testCreateTransactionCase1() throws Exception {
        User sender = new User(1L,"Maria", "Souza", "999999901","maria@example.com","123456", new BigDecimal(10), UserType.COMMON);
        User receiver = new User(2L,"Joao", "Souza", "999999902","Joao@example.com","123456", new BigDecimal(10), UserType.COMMON);

        when(userService.findUserById(1L)).thenReturn(sender);
        when(userService.findUserById(2L)).thenReturn(receiver);
        when(authService.authorizeTransaction(any(), any())).thenReturn(true);

        TransactionDTO request = new TransactionDTO(new BigDecimal(10),1L,2L);
        transactionService.createTransaction(request);

        verify(repository,times(1)).save(any());

        sender.setBalance(new BigDecimal(0));
        verify(userService,times(1)).saveUser(sender);

        receiver.setBalance(new BigDecimal(20));
        verify(userService,times(1)).saveUser(receiver);

        verify(notificationService,times(1)).sendNotification(sender, "Transação realizada com sucesso");
        verify(notificationService,times(1)).sendNotification(receiver, "Transação recebida com sucesso");





    }



    @Test
    @DisplayName("Should throw exception when transaction is not authorized")
    void testCreateTransactionCase2() throws Exception {
        
        User sender = new User(1L,"Maria", "Souza", "999999901","maria@example.com","123456", new BigDecimal(10), UserType.COMMON);
        User receiver = new User(2L,"Joao", "Souza", "999999902","Joao@example.com","123456", new BigDecimal(10), UserType.COMMON);

        when(userService.findUserById(1L)).thenReturn(sender);
        when(userService.findUserById(2L)).thenReturn(receiver);
        when(authService.authorizeTransaction(any(), any())).thenReturn(false);

        Exception thrown = Assertions.assertThrows(Exception.class, () -> {
            TransactionDTO request = new TransactionDTO(new BigDecimal(10),1L,2L);
            transactionService.createTransaction(request);
        });
        
        Assertions.assertEquals("Transação não autorizada", thrown.getMessage());
    }
}
