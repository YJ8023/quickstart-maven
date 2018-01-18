package quickstart.web.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import quickstart.entity.User;
import quickstart.service.account.AccountService;

import javax.validation.Valid;

/**
 * 注册用户
 *
 * @author Administrator
 * @create 2018-01-17
 */

@Controller
@RequestMapping(value = "/register")
public class RegisterController {

    @Autowired
    private AccountService accountService;

    @RequestMapping(method = RequestMethod.GET)
    public String registerForm(){

        return "account/register";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String register(@Valid User user, RedirectAttributes redirectAttributes){

        accountService.registerUser(user);
        redirectAttributes.addFlashAttribute("username", user.getLoginName());
        return "redirect:/login";
    }

    /**
     * Ajax请求校验loginName是否唯一
     */
    @RequestMapping(value = "checkLoginName")
    @ResponseBody
    public String checkLoginName(@RequestParam("loginName")String loginName){

        if(accountService.findUserByLoginName(loginName) == null){

            return "true";
        } else{

            return "false";
        }
    }

}
