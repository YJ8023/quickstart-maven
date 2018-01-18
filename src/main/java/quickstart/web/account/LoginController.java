package quickstart.web.account;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Administrator
 * @create 2018-01-10
 */

@Controller
@RequestMapping(value = "login")
public class LoginController {

    @RequestMapping(method = RequestMethod.GET)
    public String toLogin(){
        return "account/login";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String fail(@RequestParam(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM) String userName
            , Model model) {

        model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, userName);

        System.out.println("username: " + userName);

        return "account/login";
    }
}
