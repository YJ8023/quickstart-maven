package quickstart.web.task;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import quickstart.entity.Task;
import quickstart.entity.User;
import quickstart.service.task.TaskService;
import org.springside.modules.web.Servlets;
import quickstart.service.account.ShiroDbRealm.ShiroUser;

import javax.servlet.ServletRequest;
import javax.validation.Valid;
import java.util.Map;

/**
 * @author Administrator
 * @create 2018-01-12
 */
@Controller
@RequestMapping(value = "task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    private static final String PAGE_SIZE = "2";

    private static Map<String, String> sortTypes = Maps.newLinkedHashMap();
    static {
        sortTypes.put("auto", "自动");
        sortTypes.put("title", "标题");
    }

    /**
     * 分页查询
     */
    @RequestMapping(method = RequestMethod.GET)
    public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber
            , @RequestParam(value = "page.size",defaultValue = PAGE_SIZE) int pageSize
            , @RequestParam(value = "sortType", defaultValue = "auto") String sortType
            , Model model, ServletRequest request){

        Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
        Long userId = getCurrentUserId();

        PageInfo<Task> tasks = taskService.queryTaskByPag(userId, pageNumber, pageSize);

        model.addAttribute("tasks", tasks);
        model.addAttribute("sortType", sortType);
        model.addAttribute("sortTypes", sortTypes);
        model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));

        return "task/taskList";
    }

    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String createForm(Model model) {
        model.addAttribute("task", new Task());
        model.addAttribute("action", "create");
        return "task/taskForm";
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String create(@Valid Task newTask, RedirectAttributes redirectAttributes) {

        User user = new User();
        user.setId(getCurrentUserId());

        System.out.println(user.getId());

        newTask.setUser(user);

        taskService.saveTask(newTask);
        redirectAttributes.addFlashAttribute("message", "创建任务成功");
        return "redirect:/task/";
    }

    @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("task", taskService.getTask(id));
        model.addAttribute("action", "update");
        return "task/taskForm";
    }

    //@ModelAttribute
    //被@ModelAttribute注释的方法会在此controller每个方法执行前被执行
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute("task") Task task, RedirectAttributes redirectAttributes) {
        taskService.saveTask(task);
        redirectAttributes.addFlashAttribute("message", "更新任务成功");
        return "redirect:/task/";
    }

    //@PathVariable是用来获得请求url中的动态参数的
    @RequestMapping(value = "delete/{id}")
    public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        taskService.deleteTask(id);
        redirectAttributes.addFlashAttribute("message", "删除任务成功");
        return "redirect:/task/";
    }

    /**
     * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出Task对象,再把Form提交的内容绑定到该对象上。
     * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
     */
    @ModelAttribute
    public void getTask(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
        if (id != -1) {
            model.addAttribute("task", taskService.getTask(id));
        }
    }

    /**
     * 取出Shiro中的当前用户Id.
     */
    private Long getCurrentUserId() {
        ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();

        return user.id;
    }
}
