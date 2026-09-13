package com.localservice.service;

import com.localservice.dao.ProviderDAO;
import com.localservice.dao.ServiceDAO;
import com.localservice.model.Service;
import com.localservice.model.ServiceProvider;

import java.util.List;

public class CatalogService {
    private final ServiceDAO serviceDAO = new ServiceDAO();
    private final ProviderDAO providerDAO = new ProviderDAO();

    public List<Service> availableServices() throws Exception {
        return serviceDAO.findAllActive();
    }

    public List<Service> byCategory(String category) throws Exception {
        return serviceDAO.findByCategory(category);
    }

    public List<Service> emergencyServices() throws Exception {
        return serviceDAO.findEmergencyServices();
    }

    public List<Service> byProvider(int providerId) throws Exception {
        return serviceDAO.findByProvider(providerId);
    }

    public Service getService(int id) throws Exception {
        return serviceDAO.findById(id);
    }

    public List<ServiceProvider> availableProviders() throws Exception {
        return providerDAO.findAvailable();
    }

    public List<ServiceProvider> allProviders() throws Exception {
        return providerDAO.findAll();
    }

    public int addServiceForProvider(int providerId, String name, String description,
                                     String category, double price, int duration) throws Exception {
        Service s = new Service(0, providerId, name, description, category, price, duration, true);
        return serviceDAO.insert(s);
    }
}
