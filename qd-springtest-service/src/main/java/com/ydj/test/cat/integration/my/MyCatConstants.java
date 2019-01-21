package com.ydj.test.cat.integration.my;

import com.dianping.cat.CatConstants;

/**
 *
 * @author : Ares.yi
 * @createTime : 2019-1-19
 * @version : 1.0
 * @description :
 *
 */
public class MyCatConstants extends CatConstants {

    public  static final String CROSS_CONSUMER ="PigeonCall";

    public static final String CROSS_SERVER = "PigeonService";
    
    public static final String PROVIDER_APPLICATION_NAME="serverApplicationName";
    
    public static final String CONSUMER_CALL_SERVER="PigeonCall.server";
    
    public static final String CONSUMER_CALL_APP="PigeonCall.app";
    
    public static final String CONSUMER_CALL_PORT="PigeonCall.port";
    
    public static final String PROVIDER_CALL_SERVER="PigeonService.client";
    
    public static final String PROVIDER_CALL_APP="PigeonService.app";

    public static final String FORK_MESSAGE_ID="m_forkedMessageId";

    public static final String FORK_ROOT_MESSAGE_ID="m_rootMessageId";

    public static final String FORK_PARENT_MESSAGE_ID="m_parentMessageId";



    /**
     * cat http header 常量
     */
    public static final String CAT_HTTP_HEADER_ROOT_MESSAGE_ID = "X-CAT-ROOT-MESSAGE-ID";
    public static final String CAT_HTTP_HEADER_PARENT_MESSAGE_ID = "X-CAT-ROOT-PARENT-ID";
    public static final String CAT_HTTP_HEADER_CHILD_MESSAGE_ID = "X-CAT-ROOT-CHILD-ID";

    public static final String CAT_HTTP_HEADER_TRACE_MODE = "X-CAT-TRACE-MODE";

    public static final String APPLICATION_KEY = "CONSUMER-APP-NAME";

}