package quickstart.web.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import quickstart.entity.User;
import quickstart.service.account.AccountService;

import javax.validation.Valid;
import java.util.List;

/**

 * @author Administrator
 * @create 2018-01-17
 */
@Controller
@RequestMapping(value = "/admin/user")
public class UserAdminController {

    @Autowired
    private AccountService accountService;

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model){

        List<User> users = accountService.getAllUser();
        model.addAttribute("users", users);

        return "account/adminUserList";
    }

    /**
     * @PathVariable：是用来获得请求url中的动态参数的
     */
    @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", accountService.getUser(id));
        return "account/adminUserForm";
    }

    /**
     * 更新用户
     * RedirectAttributes：用于重定向之后还能带参数跳转
     * @Valid：进行数据验证
     */
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute("user")User user, RedirectAttributes redirectAttributes){

        accountService.updateUser(user);
        redirectAttributes.addFlashAttribute("message", "更新用户" + user.getLoginName() + "成功");
        return "redirect:/admin/user";
    }

    /**
     * 删除用户
     * RedirectAttributes：用于重定向之后还能带参数跳转
     * @PathVariable：是用来获得请求url中的动态参数的
     */
    @RequestMapping(value = "/delete/{id}")
    public String delete(@PathVariable("id")Long id, RedirectAttributes redirectAttributes){

        User user = accountService.getUser(id);
        accountService.deleteUser(id);
        redirectAttributes.addFlashAttribute("message", "删除用户" + user.getLoginName() + "成功");
        return "redirect:/admin/user";
    }

    /**
     * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,
     * 先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
     * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
     * @ModelAttribute注释的方法会在此controller每个方法执行前被执行
     * @RequestParam：获取参数
     */
    @ModelAttribute
    public void getUser(@RequestParam(value = "id", defaultValue = "-1") Long id, Model model) {
        if (id != -1) {
            model.addAttribute("user", accountService.getUser(id));
        }
    }
}
