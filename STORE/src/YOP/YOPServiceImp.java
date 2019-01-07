package YOP;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.yeepay.g3.sdk.yop.encrypt.CertTypeEnum;
import com.yeepay.g3.sdk.yop.encrypt.DigitalEnvelopeDTO;
import com.yeepay.g3.sdk.yop.utils.DigitalEnvelopeUtils;
import com.yeepay.g3.sdk.yop.utils.InternalConfig;
import domain.Order;
import utils.YeepayService;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class YOPServiceImp implements YOPService {


    @Override
    public String createOrder(String orderId, String orderAmount, String notifyUrl, String redirectUrl, String goodsParamExt) throws Exception {

        Date time = new Date();
        String strDateFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        String formatTime = sdf.format(time);

        Map<String, String> params = new HashMap<>();
        params.put("orderId", orderId);
        params.put("orderAmount", orderAmount);
        params.put("timeoutExpress", "150");
        params.put("requestDate", formatTime);
        params.put("redirectUrl", redirectUrl);
        params.put("notifyUrl", notifyUrl);
        params.put("goodsParamExt", goodsParamExt);
//        params.put("paymentParamExt", paymentParamExt);
//        params.put("industryParamExt", industryParamExt);
//        params.put("memo", memo);
//        params.put("riskParamExt", riskParamExt);
//        params.put("csUrl", csUrl);
        params.put("fundProcessType", "DELAY_SETTLE");
//        params.put("divideDetail", divideDetail);
//        params.put("divideNotifyUrl", divideNotifyUrl);

        String uri = YeepayService.getUrl(YeepayService.TRADEORDER_URL);
        Map<String, String> result = YeepayService.requestYOP(params, uri, YeepayService.TRADEORDER);

        System.out.println(result);

        return result.get("token");
    }

    @Override
    public String paymentOrderByCashier(String token,String userNo) throws Exception {


        String parentMerchantNo = YeepayService.getParentMerchantNo();
        String merchantNo = YeepayService.getMerchantNo();
//        String ext = "{\"appId\":\""+appId+"\",\"openId\":\""+openId+"\",\"clientId\":\""+clientId+"\"}";

        Map<String,String> params = new HashMap<String,String>();
        params.put("parentMerchantNo", parentMerchantNo);
        params.put("merchantNo", merchantNo);
        params.put("token", token);
        params.put("timestamp", String.valueOf(System.currentTimeMillis()));
        params.put("directPayType", "");
        params.put("cardType", "");
        params.put("userNo", userNo);
        params.put("userType", "USER_ID");
//        params.put("ext", ext);


        return YeepayService.getUrl(params);
    }

    public Map<String, String> refund(Order order) throws IOException {

        Map<String, String> params = new HashMap<>();
        params.put("orderId", order.getOid());
        params.put("uniqueOrderNo", order.getUniqueOrderNo());
        params.put("refundRequestId", order.getOid() + order.getUniqueOrderNo());
        params.put("refundAmount", String.valueOf(order.getTotal()));
        params.put("description", "退款");
        params.put("memo", "退款");
        params.put("notifyUrl", "http://119.23.41.164:8080/store/OrderServlet?method=refundCallback");
        params.put("accountDivided", "");
//        System.out.println("description="+description);

        Map<String, String> result = new HashMap<>();
        String uri = YeepayService.getUrl(YeepayService.REFUND_URL);
        result = YeepayService.requestYOP(params, uri, YeepayService.REFUND);

        return result;

    }

    @Override
    public String paymentOrderByAPI(String token, String userNo) throws Exception {

        Map<String,String> params = new HashMap<String,String>();

        params.put("token", token);
        params.put("payTool", "SCCANPAY");
        params.put("payType", "WECHAT");
//        params.put("userNo", userNo);
//        params.put("userType", userType);
//        params.put("appId", appId);
//        params.put("openId", openId);
//        params.put("payEmpowerNo", payEmpowerNo);
//        params.put("merchantTerminalId", merchantTerminalId);
//        params.put("merchantStoreNo", merchantStoreNo);
        params.put("userIp", "172.19.24.222");
        params.put("version", "1.0");

        String uri = YeepayService.getUrl(YeepayService.APICASHIER_URI);
        Map<String, String> result = YeepayService.requestYOP(params, uri, YeepayService.APICASHIER);
         System.out.println(result);

        return null;
    }

    //测试异步回调
    @Override
    public Map<String,String> decrypt(String response) throws Exception{
        //String response="qNjNqLTYaSK98W9F04PghhPishrb_Z6u4g6oADsKxc_pwmajFQJZQnhVH5PeKrjf7SyvYr0VmZ2qZ66ev2PZohf0rSbkrTdV8yaN4MSvt4jhv2qBAaB39N9tDzr2avJ4uLZQ_QNvGG2cp4HJAp9h6Xa5LNkSqJrnxqzHQWzWDDIylNKirNeXp99qfip5yQYRaHE9B8CiJ8FaoY44d8J9udzdJPyABjpYbbEtGV6ad7xcmdE6EsXq6pC5JEbl8DDa1RJuwVA_ZFWABW7wJRgqwmJg_eQgiJ83C8HMg8nL0TVk7wp8hPUXLseOWbq021a7TDddn9mC8_i7l85BIvGVxA$6NFh3P1wfzZhMMwce5FxengavYXb_woW7KGriA2slwq5DNKu7ydSjQ1YuBiicFyxVHdtclXNptwJPCT8lGWKSxVRa9GWyljqGeG7nHNLaqTv65h5WGzidSlB1Z6HUN655Mf8ZtoVqs7p6kMotEbjDcBNT7KQBYJlrPJS70v2A1fco-9jzl4Ptaf0JFGCxwQv9erUM72KAbsZBWAKxGE9mjhlOB6H5z3Sb2qbVN9Sf-OYI9TZd_wQrnYH16OrYqFIIbzEHFmZLfYV5bwXu682W8Ewk4RJ0MSAL8-I-cdMMP7gUFweBpK2EOZTRyvkUBb5-i-qt9stRxlThfzTIgLeHOyI17WE_bAq7_84wl5iAbBlUxUdT87ArKFwyBVXzygwQUDvNlafzo6uYYQQd-keExyiMOj3YDTfTvvOPjjWxLbv65h5WGzidSlB1Z6HUN65tl6_6BIOnFDRpuCdhV4aDQtDszcvOWKpTar8RTCIduq0h2Ukk2B6UpSCb_2lPPAcTe8JM0LDr5yz0aM8zPBJdbHo_AoY1X4_aJ2CojGOMXUvMIbj6dViu9dQoBQHzv6krCL40XxBs3NB8qsuAfDj3lrpMDCnr6pRZpE20mfNw67c_Vh2K9miFTtwC0Cfp7SOlc2LPOiA-mGx-wdmwUM4cgl7GIiyX1nafzqF4gSvNn1phKf0IcnXtXfRTEFeHIAgllNxaPb-YfsQ_slWLmMbnJxDQFnRkFNiITC4P1ZyPZKdjEGyfHOHiRpgV8nywudTfCB_hDh0PVRyijcXXfnWncEvBz8k80XM4JBWIvO-DqyUVCzDoyv7-yCdb3a2B-ehTHNel7QGNu-U6iIrTiUlk236CYqxCKCdH79g0cPiHez1kL3rgeWds0nlK_XYyD29Sa0l3WEBcrdK38Szh_pEV1K0AYgh_zOTv3J3VV4ssNG736AebkCSK3-HJAr88O_i952OFXsMpUsLuNoI8QQMjxQeiWNkEAg2IEBIx-Bs1TJbApZwD8X_LZh1SYXxOLjqJppz3CZ7ZPSkOAQuyciB4CBxn-q5OYE4fcD8fbuYzEs$AES$SHA256";
//        String response="iZLsNqljI0S4Edin9NctaLxZZpF7coaX5GOB-jpYynO8Wyv2ELzZHMOStZm0vIZ-DQnX8m2E6LNv3AN2Z_k2-MGgFKAvFq2_BIM-JF3vSiaUkvW0YVyvJUSNBkv9v_JsFYDVsP5h4zbtZQbbB6Uavj53_LWWYYGriWl2qkyHQbxEqszDPgayGo45oeCoxVLZW8QWyClzKRIAuhAFp2IP7NtWhl7lFln_CO_g6KK6_QbUqYACUtxhfSznbXYfLPGtgc5uQIfUtSpJqWOx_zUd3-qEuFl27Z4vjSmoN93YlNDeDx6gMKJlKaPKuJp7aGX9ptj3w8PPN11TWEIpbd0t5A$sKGZIndTkBKi9kp8MAo-BVxqEk0UN_dlmmr5I3diLRmfqBs4E3LurNHBzhW1iZ9LO5sm5oP87r6-s7sYzZdmPtDayoLF7KkZaDlYyxuTRnFYUXo28dDR293fFuaTPSFfqH3omysxy2ncZyQPo2yky50RcMDxhLr2nwX714gnmk-cJBQ8MA6P87PfNgLoF5rPopFaj-PTXJlM6_coX2yo_6JDhG8TrB0Ig3ntLzA9kKOR_1vbCPizv0FtVaNKyfrxlUwabD-eIlxBXhmnt10NTuTcM18OFvUFTHxxBrR0dMEUJww0MvudKJEjVZwwh2Hmv4NOVAPk4c5zeVhiHTQHWG4JQmCxvpG-mltxbv-66dVbNi45julQN7AcrbCJwKChj6amyXVQUNkGkmQr53iCDFBG4GtxneKOQ9mIap27YtPihemFcuBF6vfii4m9bBpXvorIqGie4kDT_8U_IHAwByUHyTBZBYx6gmbDHPhqD4_pkSRKzs6lAN1z1A1fY-WCbEwCNMEPxr_r7-5yumrlXSrG4uiR_1wBt8vBTlv2ludl1rRKWVFxN5jaQQp0MahJIq1AssuORDF3o1fx0gwwf-gjkxkUfz39VNWW2vXPG4dEBFJgj8zgyUHrOxNsfTMT_6Mak77Qgej2d8fnh5DqNNSMrt8L4LRLC4gV98GiRjf8BfJXDgHhY3Wsskxr3EBLE1sKoV7bST5H9Fl0t3l0OKkHbHQyuVh7iYawyZwvpEXnHesbZr_ZaBieQfjUKHcvA0L0B_exFcWPDm8oQt01WZ9zP8a86Lc82l-Cll1gk0gwjtDw3cQZ5T20BdDm3TLfCRqWA9oEBprBpaHij6HJo0XB5Zw_V1YmDP1PSm9tQamXiwrNoP7FoI7JzOzVkq8YRXTaFlFDJBk8n8pRrlNVggui5mScWVPsmg-l2MZgR93IaVrETKzZTWfFlIy2Uebhs9WOfDgU7WgL9jUaZ28P2VzXs6r9EQgkK_D8iLKFVEE8vnMfLkm-41AWLFHMzl24XUkKKAzcwZaasJYeG_Ob-aGcnAOQSulMLPT0gymFvOOG6keM7zxxzie86usakW4Gpqziyv64VZ7HugfEvPE5C09KW7Ln4Tgbtagh1_x67iEEJKCCPuZhuUvkdMQ9OpYAhmCBkGaWCUFsNIqKO9_59NpYKny8xHklk1XXCWNsQCeE6VXXyWsrZywR4TgalLR6$AES$SHA256";

            //开始解密
            Map<String,String> jsonMap  = new HashMap<>();
            DigitalEnvelopeDTO dto = new DigitalEnvelopeDTO();
            dto.setCipherText(response);
            //InternalConfig internalConfig = InternalConfig.Factory.getInternalConfig();
            PrivateKey privateKey = InternalConfig.getISVPrivateKey(CertTypeEnum.RSA2048);
            System.out.println("privateKey: "+privateKey);
            PublicKey publicKey = InternalConfig.getYopPublicKey(CertTypeEnum.RSA2048);
            System.out.println("publicKey: "+publicKey);

            dto = DigitalEnvelopeUtils.decrypt(dto, privateKey, publicKey);
            System.out.println("解密结果:"+dto.getPlainText());
            jsonMap = parseResponse(dto.getPlainText());
            return jsonMap;
    }

    public static Map<String, String> parseResponse(String response){

        Map<String,String> jsonMap  = new HashMap<>();
        jsonMap	= JSON.parseObject(response,
                new TypeReference<TreeMap<String,String>>() {});

        return jsonMap;
    }
}
