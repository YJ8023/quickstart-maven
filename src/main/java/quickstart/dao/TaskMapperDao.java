package quickstart.dao;

import com.github.pagehelper.PageInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import quickstart.entity.Task;

import java.util.List;

@Repository
public interface TaskMapperDao extends PagingAndSortingRepository<Task, Long>, JpaSpecificationExecutor<Task> {

    Page<Task> findByUserId(Long id, Pageable pageRequest);

    void deleteByUserId(Long userId);

    Task selectTask(Long id);

    void insertTask(Task task);

    List<Task> selectAllTask();

    List<Task> selectTaskByUserId(Long userId);

    void deleteTask(Long id);

    void updateTask(Task entity);
}
