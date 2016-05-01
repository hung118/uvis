package gov.utah.dts.det.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

/**
 * UMD login interceptor class - checks sessiion varible user. If it's null
 * (user did not login,) forward to index.jsp (UMD protected file). Otherwise,
 * invoke next interceptor normally. This interceptor should resolve session
 * time out issue.
 * 
 * @author HNGUYEN
 * 
 */
public class UMDLoginInterceptor extends MethodFilterInterceptor {

	private static final long serialVersionUID = 1L;

	public UMDLoginInterceptor() {
		
	}
	
	@Override
	protected String doIntercept(ActionInvocation invocation) throws Exception {
		
		/* grabs the action
		Object action = invocation.getAction();
        if (action instanceof ValidationAware){
            before(invocation, (ValidationAware) action);
        }*/
		
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession(true);

		if (session.getAttribute("user") == null) {
			if (request.getParameter("login_attempt") != null) { // user is attempting to login right now, let it pass.
				return invocation.invoke();
			} else { // user did not login, forward to global result (index.jsp).
				return "umdLogin";
			}
		} else {
			return invocation.invoke();
		}

	}

}
