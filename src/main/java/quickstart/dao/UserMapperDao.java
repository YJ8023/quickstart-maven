package quickstart.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import quickstart.entity.User;

import java.util.List;

@Repository
public interface UserMapperDao{

    User selectUserByLoginName(String userName);

    List<User> findAllUser();

    User findOne(Long id);

    void deleteById(Long id);

    void update(User user);

    void save(User user);
}
