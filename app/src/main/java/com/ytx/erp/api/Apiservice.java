package com.ytx.erp.api;

/**
 * Author：KePeiKun
 * Time：2022/7/22
 * Description：Url地址
 */

public interface Apiservice {

//      public static String BASE_URL="https://chatshop.com.cn/";//正式
        public static String BASE_URL="http://192.168.2.39:8085/";//测试/


    /*充值协议*/
    public static String RECHARGE_URL= BASE_URL+"lgshare#/pages/wallet/recharge/recharge";

    /*结算规则*/
    public static String SETTLEMENT_RULES_URL= Apiservice.BASE_URL+"lgshare/#/pages/about_us/settle/settle";

    /*分享规则*/
    public static String SHARING_AGREEMENT_URL= Apiservice.BASE_URL+"lgshare/#/pages/about_us/sharing_agreement/sharing_agreement";

    /*联盟规则*/
    public static String BOUTIQUE_SERVICE_AGREEMENT_URL= Apiservice.BASE_URL+"lgshare/#/pages/about_us/boutique_service_agreement/boutique_service_agreement";

}
