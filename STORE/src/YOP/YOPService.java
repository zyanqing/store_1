package YOP;

import domain.Order;

import java.io.IOException;
import java.util.Map;

public interface YOPService {

    public String createOrder(String orderId, String orderAmount, String notifyUrl,String redirectUrl,String goodsParamExt) throws Exception;

    public String paymentOrderByCashier(String token,String userNo) throws Exception;

    public String paymentOrderByAPI(String token,String userNo) throws Exception;

    public Map<String,String> decrypt(String response) throws Exception;

    public Map<String, String> refund(Order order) throws IOException;


}
