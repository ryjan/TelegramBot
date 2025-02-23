package org.ryjan.telegram.commands.users.admin.adminpanel;

import org.ryjan.telegram.model.users.User;
import org.ryjan.telegram.services.ServiceBuilder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AdminPanelService extends ServiceBuilder {

    public void sendAdminRewards(Long administratorId) {
        User administrator = userService.findUser(administratorId);
        administrator.getBanks().addGems(new BigDecimal("1.5"));
        userService.save(administrator);
    }
}
