package quickstart.service.account;


import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.security.utils.Digests;
import org.springside.modules.utils.Clock;
import org.springside.modules.utils.Encodes;
import quickstart.dao.TaskMapperDao;
import quickstart.dao.UserMapperDao;
import quickstart.entity.User;
import quickstart.service.ServiceException;

import java.util.List;
import quickstart.service.account.ShiroDbRealm.*;

/**
 * @author Administrator
 * @create 2018-01-11
 */
@Component
@Transactional
public class AccountService {

    public static final String HASH_ALGORITHM = "SHA-1";
    public static final int HASH_INTERATIONS = 1024;
    private static final int SALT_SIZE = 8;

    private static Logger logger = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    private UserMapperDao userMapperDao;

    @Autowired
    private TaskMapperDao taskMapperDao;

    private Clock clock = Clock.DEFAULT;

    public List<User> getAllUser() {
        return (List<User>) userMapperDao.findAllUser();
    }

    public User getUser(Long id) { return userMapperDao.findOne(id); }

    public User findUserByLoginName(String loginName){ return userMapperDao.selectUserByLoginName(loginName); }

    @Transactional
    public void registerUser(User user) {
        entryptPassword(user);
        user.setRoles("user");
        user.setRegisterDate(clock.getCurrentDate());

        userMapperDao.save(user);
    }

    @Transactional
    public void updateUser(User user) {
        if (StringUtils.isNotBlank(user.getPlainPassword())) {
            entryptPassword(user);
        }
        userMapperDao.update(user);
    }

    @Transactional
    public void deleteUser(Long id) {

        if (isSupervisor(id)) {

            logger.warn("操作员{}尝试删除超级管理员用户", getCurrentUserName());
            throw new ServiceException("不能删除超级管理员用户");
        }

        userMapperDao.deleteById(id);
        taskMapperDao.deleteByUserId(id);
    }

    /**
     * 判断是否超级管理员.
     */
    private boolean isSupervisor(Long id) {

        return id == 1;
    }

    /**
     * 取出Shiro中的当前用户LoginName.
     */
    private String getCurrentUserName() {
        ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        return user.loginName;
    }

    /**
     * 设定安全的密码，生成随机的salt并经过1024次 sha-1 hash
     */
    private void entryptPassword(User user) {
        byte[] salt = Digests.generateSalt(SALT_SIZE);
        user.setSalt(Encodes.encodeHex(salt));

        byte[] hashPassword = Digests.sha1(user.getPlainPassword().getBytes(), salt, HASH_INTERATIONS);
        user.setPassword(Encodes.encodeHex(hashPassword));
    }

    //@Autowired
    public void setUserMapperDao(UserMapperDao userMapperDao){ this.userMapperDao = userMapperDao; }

    //@Autowired
    public void setTaskMapperDao(TaskMapperDao taskMapperDao){ this.userMapperDao = userMapperDao; }

    public void setClock(Clock clock) {
        this.clock = clock;
    }

}
