package com.picpaysimplificado.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.picpaysimplificado.domain.dtos.UserDTO;
import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.domain.user.UserType;
import com.picpaysimplificado.repositories.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    public void validateTransaction(User sender, BigDecimal amount) throws Exception{
        if (sender.getUsertype() == UserType.MERCHANT) {
            throw new Exception("Usuário do tipo lojista não pode realizar transações");    
        }

        if (sender.getBalance().compareTo(amount) < 0) {
            throw new Exception("Saldo insuficiente");
        }
    }

    public User findUserById(Long id) throws Exception{
        return this.repository.findById(id).orElseThrow(() -> new Exception("Usuário não encontrado"));

    }

    public User createUser(UserDTO data) {
        User newUser = new User(data);
        this.saveUser(newUser);
        return newUser;
    }
    
    public List<User> getAllUsers(){
        return this.repository.findAll();
    }

    public void saveUser(User user) {
        this.repository.save(user);
    }
    
}
