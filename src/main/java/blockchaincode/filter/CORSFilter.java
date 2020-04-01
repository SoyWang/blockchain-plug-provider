//package blockchaincode.filter;
//
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.HashSet;
//import java.util.Set;
//
//import javax.servlet.Filter;
//import javax.servlet.FilterChain;
//import javax.servlet.FilterConfig;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
///**
// * 解决跨域访问 
// * @author WangSong
// *
// */
//
//@WebFilter("/*")
//public class CORSFilter implements Filter {
//    public CORSFilter() {
//    }
//
//    public void destroy() {
//    }
//
//    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
//        System.out.println("=========================================");
//    	//设置跨域请求
//        HttpServletRequest request = (HttpServletRequest) req;
//        HttpServletResponse response = (HttpServletResponse) res;
//        String[] allowDomains = {
//        		"http://gateway.das.sunsheen.cn/api/rest/das/block-chaincode/add",
//        		"http://gateway.das.sunsheen.cn/api/rest/das/block-chaincode/update",
//        		"http://gateway.das.sunsheen.cn/api/rest/das/block-chaincode/history",
//        		"http://gateway.das.sunsheen.cn/api/rest/das/block-chaincode/block-info"
//        		};
//        Set allowOrigins = new HashSet(Arrays.asList(allowDomains));
//        String originHeads = request.getHeader("Origin");
////        if(allowOrigins.contains(originHeads)){
////            response.setHeader("Access-Control-Allow-Origin", originHeads);
//        	response.setHeader("Access-Control-Allow-Origin", "*");
//            response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE,HEAD,PUT,PATCH");
//            response.setHeader("Access-Control-Max-Age", "36000");
//            response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept,Authorization,authorization");
//            response.setHeader("Access-Control-Allow-Credentials","true");
////        }
//        chain.doFilter(req, response);
//    }
//
//    public void init(FilterConfig fConfig) throws ServletException {
//    }
//}
