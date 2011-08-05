package com.megajoy.thirdpart.report.service.bean;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.annotation.PostConstruct;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.sol.util.log.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository
@Scope("prototype")
public class HttpConnectionManager {   
	  
    private HttpParams httpParams;
    private ThreadSafeClientConnManager connectionManager;
    
    private URI url;

    @Value("${http.CONNECT_TIMEOUT}")
    private int CONNECT_TIMEOUT = 10000;  

    @Value("${http.READ_TIMEOUT}")
    private int READ_TIMEOUT = 10000;  
  
    public HttpConnectionManager(String url) {
		try {
			this.url = new URI(url);
		} catch (URISyntaxException e) {
			Logger.SYS.error("错误的URL地址:" + url);
		}
	}
    
    @PostConstruct
    public void init() {  
        httpParams = new BasicHttpParams();
        
        // 设置读取超时时间   
        httpParams.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, READ_TIMEOUT);
        // 设置获取连接的最大等待时间   
        httpParams.setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECT_TIMEOUT);
        
        httpParams.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,HttpVersion.HTTP_1_1);
        httpParams.setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET,"UTF-8");

//        // 解析协议
//        SchemeRegistry schemeRegistry = new SchemeRegistry();
//        schemeRegistry.register(new Scheme(url.getProtocol(), url.getPort() == -1 ? 80 : url.getPort(), PlainSocketFactory.getSocketFactory()));
//        
//        connectionManager = new ThreadSafeClientConnManager(schemeRegistry);
//        
//        // 设置最大连接数   
//        connectionManager.setMaxTotal(MAX_TOTAL_CONNECTIONS);
    }  
  
    public HttpClient getHttpClient() {  
        return new DefaultHttpClient(connectionManager, httpParams);  
    }
    
    public boolean post(String xml) throws IOException {
    	HttpClient client = new DefaultHttpClient(httpParams);
    	HttpPost post = new HttpPost(url);
    	StringEntity entity = new StringEntity(xml, "text/xml", "UTF-8");
    	post.setEntity(entity);
		try {
			HttpResponse response = client.execute(post);
//			System.out.println("response:" + response.getStatusLine().getStatusCode());
			if(response.getStatusLine().getStatusCode() == 200)
				return true;
			else {
				post.abort();
				return false;
			}
		} catch (ClientProtocolException e) {
			post.abort();
			throw e;
		} catch (IOException e) {
			post.abort();
			throw e;
		}
    }
}  

