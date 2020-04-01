package blockchaincode.provider;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.sunsheen.jfids.das.core.annotation.Bean;
import com.sunsheen.jfids.das.core.annotation.Provider;
import com.sunsheen.jfids.das.core.web.socket.DasWebSocket;
import com.sunsheen.jfids.das.core.web.socket.DasWebSocketSample;
/**
 * 该类为websocket接口服务。
 * @author WangSong
 *
 */
@Bean("com.sunsheen.jfids.das.core.test.WebsocketListenerImpl")
@ServerEndpoint("/websocket/{biz}")//访问路径。如:ws://IP地址:端口号/websocket/demo
@Provider(DasWebSocket.class)
public class BlockChaincodeWebsocket extends DasWebSocketSample{
    /**
     * 
     * 接收来自前端的数据并可立即返回数据。
     * 注解	@OnMessage必须。
     * @param message
     * @param pathParam
     */
	@OnMessage
	public void onMessage(String message,@PathParam("biz") String pathParam) {
		 System.out.println("onMessage:" + message);
		 System.out.println("pathParam:" + pathParam);
		 //调用父类的发送数据方法
		 sendMessage(message+"-------");
    	 //session.getAsyncRemote().sendText(message+"-------");
	}
	/**
	 * 创建连接时触发该方法。
	 * 如果子类实现了方法，需要首先调用父类的对应方法。
	 * 注解@OnOpen必须
	 */
	@Override
	@OnOpen
	public void onOpen(Session session) {
		super.onOpen(session);//初始化seesion
		System.out.println("onOpen");
	    System.out.println(session.getRequestParameterMap());
	}
    /**
     * 关闭连接时触发该方法。
     * 如果子类实现了方法，需要首先调用父类的对应方法。
     * 注解@OnClose必须
     */
	@Override
	@OnClose
	public void onClose(Session session) {
		super.onClose(session);
		System.out.println("onClose");
		
	}
    /**
     * 发生异常时触发该方法。
     * 如果子类实现了方法，需要首先调用父类的对应方法。
     * 注解@OnError必须
     */
	@Override
	@OnError
	public void onError(Throwable error,Session session) {
		super.onError(error,session);
	}
}
