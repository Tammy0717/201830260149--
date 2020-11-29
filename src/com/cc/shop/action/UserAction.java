package com.cc.shop.action;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;



import com.cc.shop.pojo.BillItem;
import com.cc.shop.pojo.Message;
import com.cc.shop.pojo.User;
import com.cc.shop.service.MessageService;
import com.cc.shop.service.UserService;
import com.cc.shop.utils.PageBean;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 用户模块Action的类
 * 
 * 
 */
public class UserAction extends ActionSupport implements ModelDriven<User> {
	// 模型驱动使用的对象
	private User user = new User();

	public User getModel() {
		return user;
	}
	
	private Integer page;
	
	public String username;
	
	private MessageService messageService;

	// 接收验证码:
	private String checkcode;
	
	
	// 注入UserService
	private UserService userService;

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * 跳转到注册页面的执行方法
	 */
	public String registPage() {
		return "registPage";
	}
	/**
	 * 跳转到个人信息中心
	 */
	public String toinfomation() {
		return "toinfomation";
	}
	
	//去留言板
	public String tomessage(){
		PageBean<Message> pageBean = messageService.findByPage(page);
		ActionContext.getContext().getValueStack().set("pageBean", pageBean);
		return "tomessage";
	}
	
	/**
	 * 用户退出，清除session 返回到首页
	 */
	public String update() {
		userService.update(user);
		ServletActionContext.getRequest().getSession().invalidate();
		this.addActionMessage("修改成功！请重新登录!");
		return "quit";
	}
	
	public void checkName() throws Exception
	{
		System.out.println(username);
		User existUser = userService.findByUsername(username);
		// 获得response对象,项页面输出:
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		// 判断
		if (existUser != null) {
			// 查询到该用户:用户名已经存在
			response.getWriter().println("xxxxx");
		} else {
			// 没查询到该用户:用户名可以使用
			response.getWriter().println("x");
		}
	}
	
	public String checkcode() throws IOException {
		// 判断验证码程序:
		// 从session中获得验证码的随机值:
		String securityCode = (String) ServletActionContext.getRequest()
				.getSession().getAttribute("securityCode");
		
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		
		if (!checkcode.equalsIgnoreCase(securityCode)) {
			// 查询到该用户:用户名已经存在
			response.getWriter().println("<font color='red'>验证码输入错误!</font>");
		} else {
			// 没查询到该用户:用户名可以使用
			response.getWriter().println("<font color='green'>验证码正确</font>");
		}
		return NONE;
	}
	
	
	
	/**
	 * AJAX进行异步校验用户名的执行方法
	 * 
	 * @throws IOException
	 */
	
	public String findByName() throws IOException {
		
		// 调用Service进行查询:
		User existUser = userService.findByUsername(user.getUsername());
		// 获得response对象,项页面输出:
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		// 判断
		if (existUser != null) {
			// 查询到该用户:用户名已经存在
			response.getWriter().println("<font color='red'>用户名已经存在</font>");
		} else {
			// 没查询到该用户:用户名可以使用
			response.getWriter().println("<font color='green'>用户名可以使用</font>");
		}
		return NONE;
	}

	/**
	 * 用户注册的方法:
	 */
	public String regist() {
		// 判断验证码程序:
		// 从session中获得验证码的随机值:
		String securityCode = (String) ServletActionContext.getRequest()
				.getSession().getAttribute("securityCode");
		if(!checkcode.equalsIgnoreCase(securityCode)){
			this.addActionError("验证码输入错误!");
			return "checkcodeFail";
		}
		System.out.println("yonghu================="+user.getName());
		userService.save(user);
		this.addActionMessage("注册成功!请登录!");
		return "msg";
	}

	

	/**
	 * 跳转到登录页面
	 */
	public String loginPage() {
		return "loginPage";
	}

	/**
	 * 登录的方法
	 */
	public String login() {
		User existUser = userService.login(user);
		// 判断
		if (existUser == null) {
			// 登录失败
			this.addActionError("登录失败:用户名或密码错误!");
			return LOGIN;
		} else {
			// 登录成功
			// 将用户的信息存入到session中
			ServletActionContext.getRequest().getSession().setAttribute("existUser", existUser);
			// 页面跳转
			return "loginSuccess";
		}
	}
	
	/**
	 * 用户退出的方法
	 */
	public String quit(){
		// 销毁session
		ServletActionContext.getRequest().getSession().invalidate();
		return "quit";
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setCheckcode(String checkcode) {
		this.checkcode = checkcode;
	}

	public MessageService getMessageService() {
		return messageService;
	}

	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

}
