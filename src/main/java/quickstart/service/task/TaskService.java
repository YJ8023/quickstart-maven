package quickstart.service.task;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;
import org.springside.modules.persistence.SearchFilter.Operator;
import quickstart.dao.TaskMapperDao;
import quickstart.entity.Task;

import java.util.List;
import java.util.Map;

/**
 * taskService
 *
 * @author Administrator
 * @create 2018-01-12
 */
@Component
@Transactional
public class TaskService {

    @Autowired
    private TaskMapperDao taskMapperDao;

    public Task getTask(Long id){

        return taskMapperDao.selectTask(id);
    }

    public void saveTask(Task entity){

        if (entity.getId() != null){

            taskMapperDao.updateTask(entity);
        } else {

            taskMapperDao.insertTask(entity);
        }
    }

    public void deleteTask(Long id){

        taskMapperDao.deleteTask(id);
    }

    public List<Task> getAllTask(){

        return taskMapperDao.selectAllTask();
    }

    /**
     * 分页查询
     */
    public PageInfo<Task> queryTaskByPag(Long userId, Integer pageNo, Integer pageSize){

        pageNo = pageNo == null?1:pageNo;
        pageSize = pageSize == null?2:pageSize;
        PageHelper.startPage(pageNo,pageSize);

        return new PageInfo<Task>(taskMapperDao.selectTaskByUserId(userId));
    }

    /**
     * 创建动态查询条件组合
     */
    private Specification<Task> buildSpecification(Long userId, Map<String, Object> searchParams){

        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        filters.put("user.id", new SearchFilter("user.id", Operator.EQ, userId));
        Specification<Task> spec = DynamicSpecifications.bySearchFilter(filters.values(), Task.class);

        return spec;
    }
}


