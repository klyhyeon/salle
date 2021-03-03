package com.example.salle.application;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

//210303clear
public class LoginInterceptor implements HandlerInterceptor {
	
	//사용자 상호작용 logger에 LoginInterceptor 클래스를 저장
	private static final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);
	
	//Controller 동작 전 실행되는 Interceptor - 로그인 정보 제거
	//로그아웃 하지 않은 상태에선 로그인 정보 유지해도 괜찮은데 제거해야되나??
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

//        HttpSession session = request.getSession();
//        //기존의 로그인 정보 제거
//        if (session.getAttribute(login) != null) {
//        	logger.info("로그인 정보 제거");
//        	session.removeAttribute(login);
//        }
        return true;
    }

	//View로 가기 전 실행되는 Interceptor
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    	if (modelAndView != null) {
    	//recreate session if session doesn't exist, if exist, retrieve the session
    	HttpSession session = request.getSession(true);
    	//]
    	//get value Object key "login" from modelMap and modelAttributename "login" is added in Controller
    	Object loginInter = modelAndView.getModel().get("login");
    	Object messageAlert = modelAndView.getModel().get("messageAlert");
    			
    	if (loginInter != null) {
    		logger.info("새로운 로그인 성공");
    		session.setAttribute("login", loginInter);
    	}
    	
    	if (messageAlert != null) {
    		logger.info("메시지 성공");
    		session.setAttribute("messageAlert", messageAlert);
    	}
    } else {
    	
    }
    	

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

}
