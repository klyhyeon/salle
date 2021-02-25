package com.example.salle.controller;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebSocketConnectionRestController {
	
//	@Autowired
//	private ActiveUserManager activeSessionManager;
//	
//	@PostMapping("/rest/user-connect")
//	public String userConnect(HttpServletRequest request,
//			@ModelAttribute("username") String senderName) {
//		String remoteAddr = "";
//		if (request != null) {
//			remoteAddr = request.getHeader("Remote_Addr");
//			
//			if (StringUtils.isEmpty(remoteAddr)) {
//				remoteAddr = request.getHeader("X-FORWARDED-FOR");
//				
//				if (remoteAddr == null || "".equals(remoteAddr)) {
//					remoteAddr = request.getRemoteAddr(); 
//				}
//			}
//		}
//		
//		activeSessionManager.add(senderName, remoteAddr);
//		return remoteAddr;
//	}
	
//	@PostMapping("/rest/user-disconncet")
//	public String userDisconnect(@ModelAttribute("userName") String userName) {
//		activeSessionManager.remove(userName);
//		return "disconnected";
//	}
	
//	@GetMapping("/rest/active-users-except/{userName}")
//	public Set<String> getActiveUsersExceptCurrentUser(@PathVariable String userName) {
//		return activeSessionManager.getActiveUsersExceptCurrentUser(userName);
//	}
}
