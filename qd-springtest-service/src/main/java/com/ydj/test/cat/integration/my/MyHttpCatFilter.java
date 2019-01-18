package com.ydj.test.cat.integration.my;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import com.dianping.cat.servlet.CatFilter;
import com.dianping.cat.util.UrlParser;
import org.apache.commons.lang.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MyHttpCatFilter extends CatFilter implements Filter {

    private static final ThreadLocal<Cat.Context> CAT_CONTEXT = new ThreadLocal<>();


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            chain.doFilter(request, response);
            return;
        }

        HttpServletRequest req = (HttpServletRequest) request;

        //若Header中有context相关属性，则生成调用链
        if(null != req.getHeader(MyCatConstants.CAT_HTTP_HEADER_ROOT_MESSAGE_ID)){
            Transaction transaction = Cat.newTransaction(MyCatConstants.CROSS_SERVER,this.getRequestURI(req));
            try {
                Cat.Context context = this.getContext(req);
                this.createProviderCross(req);
                Cat.logRemoteCallServer(context);
            } catch (Exception e) {
                e.printStackTrace();
                Cat.logError(e);
                transaction.setStatus(e);
            } finally {
                transaction.complete();
                CAT_CONTEXT.remove();
            }
        }

       super.doFilter(request,response,chain);
    }



    private String getRequestURI(HttpServletRequest req) {
        return UrlParser.format(req.getRequestURI());
    }

    private static class HTTPCatContext implements Cat.Context{

        private Map<String,String> properties = new HashMap<>();

        @Override
        public void addProperty(String key, String value) {
            properties.put(key,value);
        }

        @Override
        public String getProperty(String key) {
            return properties.get(key);
        }
    }


    private Cat.Context getContext(HttpServletRequest req){
        Cat.Context context = CAT_CONTEXT.get();
        if(context == null){
            context = new HTTPCatContext();
            context.addProperty(Cat.Context.ROOT,req.getHeader(MyCatConstants.CAT_HTTP_HEADER_ROOT_MESSAGE_ID));
            context.addProperty(Cat.Context.PARENT,req.getHeader(MyCatConstants.CAT_HTTP_HEADER_PARENT_MESSAGE_ID));
            context.addProperty(Cat.Context.CHILD,req.getHeader(MyCatConstants.CAT_HTTP_HEADER_CHILD_MESSAGE_ID));
            CAT_CONTEXT.set(context);
        }
        return context;
    }

    private void createProviderCross(HttpServletRequest req){
        String consumerAppName = req.getHeader(MyCatConstants.APPLICATION_KEY);
        if(StringUtils.isEmpty(consumerAppName)){
            consumerAppName= req.getRemoteHost()+":"+ req.getRemotePort();
        }
        Cat.logEvent(MyCatConstants.PROVIDER_CALL_APP, consumerAppName);
        Cat.logEvent(MyCatConstants.PROVIDER_CALL_SERVER, req.getRemoteHost());
    }


}
