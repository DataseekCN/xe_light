package com.dataseek.xe.intercepter;

import com.alibaba.fastjson.JSONObject;
import com.dataseek.xe.dao.IUserDao;
import com.dataseek.xe.entity.UserInfo;
import com.dataseek.xe.util.DataUtil;
import com.dataseek.xe.util.XeConsts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    protected static Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);

    @Autowired
    private IUserDao userDao;

    //存放鉴权信息的Header名称，默认是session_id
    private String httpHeaderName = "session_id";

    //鉴权失败后返回的HTTP错误码，默认为401
    private int unauthorizedErrorCode = HttpServletResponse.SC_UNAUTHORIZED;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        String token = request.getHeader(httpHeaderName);
        boolean reStatus = true;

        if (!DataUtil.isEmpty(token)) {
            //根据token查询userid
            UserInfo qryDto = new UserInfo();
            qryDto.setSessionId(token);
            List<UserInfo> tmpList = userDao.qryUser(qryDto);

            if (tmpList != null && !tmpList.isEmpty()) {
                UserInfo userInfo = tmpList.get(0);
                //判断现在时间
                long expDate = DataUtil.transDateToValue(userInfo.getExpDate());
                long subValue = System.currentTimeMillis() - expDate;
                //超时
                if (subValue >= 0) {
                    reStatus = false;
                }
            }
            //token关联user不存在
            else {
                reStatus = false;
            }
        }

        if(!reStatus) {
            JSONObject jsonObject = new JSONObject();
            PrintWriter out = null;
            try {
                response.setStatus(unauthorizedErrorCode);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setCharacterEncoding("UTF-8");

                jsonObject.put("status", XeConsts.RESPONSE_STATUS_FAILURE);
                jsonObject.put("error_message", "session illegal.");
                out = response.getWriter();
                out.println(jsonObject);
                return false;
            }
            catch (Exception e) {
                logger.error(e.toString(), e);
            }
            finally {
                if (null != out) {
                    out.flush();
                    out.close();
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }





}
