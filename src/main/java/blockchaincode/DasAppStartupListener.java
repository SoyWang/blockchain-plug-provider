//package blockchaincode;
//
//import javax.servlet.ServletContext;
//
//import com.sunsheen.jfids.das.core.DasApplication;
//import com.sunsheen.jfids.das.core.annotation.DasBootApplication;
///**
// * 当与核格开发工程（非核格Maven工程）结合时，使用这种方式启动应用。
// * @author WangSong
// *
// */
////继承接口SystemStartupListener和声明@Listener可以实现服务器启动的自启动
//@Listener
//@DasBootApplication
//public class DasAppStartupListener implements SystemStartupListener {
//	@Override
//	public void init(final ServletContext context) {
//		//使用不同的类加载器，将外部HKDAS的依赖库引用到核格工程中，核格工程与HKDAS应用之间互相隔离，不能互操作。
//		//DasApplicationAdapter.run(DasAppStartupListener.class);
//		/*这种方式启动可以和核格工程深入结合。即seam组件和HKDAS的组件可以互操作。可以在HKDAS的java类中使用DBSession等对象。
//		但必须把hearken-das-sdks.jar包放入web工程的lib目录下.
//		*/
//		DasApplication.run(DasAppStartupListener.class);
//	}
//}