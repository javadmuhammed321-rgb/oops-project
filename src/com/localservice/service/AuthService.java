package com.localservice.service;

import com.localservice.dao.ProviderDAO;
import com.localservice.dao.UserDAO;
import com.localservice.model.Person;
import com.localservice.model.ServiceProvider;
import com.localservice.model.User;

/**
 * Authentication & registration service.
 * Uses polymorphic Person references where useful.
 */
public class AuthService {
    private final UserDAO userDAO = new UserDAO();
    private final ProviderDAO providerDAO = new ProviderDAO();

    public Person login(String email, String password) throws Exception {
        User user = userDAO.findByEmailAndPassword(email, password);
        if (user == null) return null;
        if ("PROVIDER".equals(user.getRole())) {
            ServiceProvider sp = providerDAO.findByUserId(user.getId());
            return sp != null ? sp : user;
        }
        return user;
    }

    public User registerCustomer(String name, String email, String password, String phone, String address) throws Exception {
        if (userDAO.findByEmail(email) != null) {
            throw new Exception("Email already registered");
        }
        User u = new User(0, name, email, password, phone, "CUSTOMER", address);
        userDAO.insert(u);
        return u;
    }

    public ServiceProvider registerProvider(String name, String email, String password, String phone,
                                            String businessName, String description) throws Exception {
        if (userDAO.findByEmail(email) != null) {
            throw new Exception("Email already registered");
        }
        User u = new User(0, name, email, password, phone, "PROVIDER", "");
        userDAO.insert(u);
        ServiceProvider sp = new ServiceProvider(u.getId(), name, email, password, phone,
                0, businessName, description, 0.0, true);
        providerDAO.insert(sp);
        return sp;
    }
}
