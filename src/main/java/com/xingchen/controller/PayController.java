package com.xingchen.controller;

/**
 * @author Li
 * @Date 2022/7/13 19:42
 */

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.xingchen.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Controller
@CrossOrigin
@Api(tags={"支付"})
public class PayController {
    @Autowired
    UserService userService;

    private final String APP_ID = "2021000119683765";
    private final String APP_PRIVATE_KEY ="MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCY0rPFS+1zL/UFZO68V1zrD9REbkw4K7qo6aM5XbVBtximb8o1r4kPNrY4SZ1YMD8McrMX1PxDvPo7WxhXIkMdBfZjXCj4oizJGARbQQEkwURQtmD5l8LKMM2B4fp2dqPlhQPJIZgIRl5+QkZk//jeygjx711NDyHqRa3G05dygO0SmrT1heOcCKSHpGM3JbkWYipcHoJNPMxcuMTlQR5U/35FpNejoLtKejBnJsRgOkhJmFCaj89tx5irhv3TB5nqrVtKEAccyaeo8cMdiNEISgeGXIreIc9JC6cQTtaf4yv5xu3L7ESMC5/PPLzLO8rZp1gzu1LwqGr2tOy1bUlHAgMBAAECggEAb8G2AVQTIycw8W7sPwNMSgHbYueeCX3BFQE2gh8LDDGPbzdVEol5m4yRGPyMeg9eLAY8/fKdQBQUkg9970p49eKEWgjtVVWTlsNffDQ/V9eV08hrhlr68c1am97bwIHmPVRWfmF4wqRjOcXRoM6n+pSYkgRPoKiuTpoCCOJ3YB3SvmuwuM8HPhdx1IsmQZH7bW7IL5/3+a+Azi208itiLk7x39AhEvkhtNxiO30N5ZjgYYW7jylvJXvEAnLdTREdSj7DexwyvdUbWu3PLaTaaoWrhBEc5iiWef3nPcNpZC4JxVO/dKoYRIi5GM9/BCSR3vV4ZM8O7N3LuuLUOz4l4QKBgQDNnkW3wfCFz82lwLxGYtLqEDzKWzHjgScT9AJ/dd68L1XLDQtw8eBGWIROcDlvk7H7rloR9kip9C433ZQKgApPQgXfwY5Slszz/eada9DGBXGQj74a2D3HGjHBpy5eNPiwvRMCQSf8AJVVy/rJnz8Qx4+ZPB3C837B8CNnIZfO3wKBgQC+RMSyT18jdjz7knU7iiC4h0haiBueGA4vpS0CHw5ZFCsoSGkF7mjXPaDvalHHAkZwFT5z3D3W+GypRxoQEqdCKJApqIlSsp9ABeL/24LcroMuVz6YETpxyGBhKnd9MfeZvalZrBx3upSAxCuntUSJ71rOtnOGlpGX4uI/j1AamQKBgGSft+6dcl+JulILaBfp9N2m5JkC1/9GDSGuoCpQPJhLcejUkWzMcbz7RIQ+V11LIibQxDNWG2lQne2o+fHJh0ISRV2jE0tHkTU2Y81WHe1KbLEaJyZr/I+bOg5TClYABlQ3sz2/NRoWDv7Vu/0/eVV008lhqg7lqnquUiT+Q5RxAoGBAIg+FdrYmvd5oxFBPlD0B5oaxOSOsbfy4bWcQS5TrqJ6nqyprQpiPRBjQOou53/pUSbRD3dHLcDRAn30O+Vmdf4ZraEm3a4p2X6uuG0dcgczrxtvvZKGQ6NWKC1ri0RKk1GzTaC4bWoOYGgUEG52ji13Bz8dxnuJNZfdDsUUifeBAoGAE0DXp9iWZJ9+dZ7Al9Hq1IsEFU3KpvgdvnryaXpXacKZZUHpZS23QUgIZYcy81bhW+lNvlJ6lKVps8D+jcln4l95geEGAkQWT7EpMRwTvA6Rl8+6OqvQRpgJipgSd9XLM58lsUwFaMWWPsaQRN48NXaLHcRxqKjpdrA99KyaVBI=";
    private final String CHARSET = "UTF-8";
    private final String ALIPAY_PUBLIC_KEY ="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAj/QiwvFEuqr6hCtRKQE/tpR8IAaNeMwp2myqGSPsP6DH8M8a37OcSdXECeT3aqJgfwxNoG08JuOpVBlpL5bx1AnR+FDQ8o8lWtscICdU7cT2UthfBuEke6dmDO/FsLt0s3cy5vgDFv51lJf0derJ90mwAQP0ZBrCICuV7dwkf4NM64icI/ehu/V4MLTN5e1F+ofIryNWxKvEN6xpZTWBXIOZvpae8YPsrC5PoqS7U7zjotzZEcxFVVvkNvHaFJyRd7KtoY/Ud3Fs7Ykq4FU5v93rTIcByZ8sB3T6n3INoCsvbm2fSqKEY9ad1o7dvn4s4gtsdOYEs4St7ub36Uw9mwIDAQAB";
    private final String GATEWAY_URL ="https://openapi.alipaydev.com/gateway.do";
    private final String FORMAT = "JSON";
    private final String SIGN_TYPE = "RSA2";
    private final String NOTIFY_URL = "http://127.0.0.1/notifyUrl";
    private final String RETURN_URL = "http://110.40.211.224:8080/xingchen/returnUrl";

    @GetMapping("alipay")
    public void alipay(HttpServletResponse httpResponse,Double orderPrice,Integer id) throws IOException {


        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, APP_ID, APP_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE);
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setReturnUrl(RETURN_URL);
        request.setNotifyUrl(NOTIFY_URL);
        String out_trade_no = id+"_"+UUID.randomUUID();
        String total_amount = String.valueOf(orderPrice);
        String subject ="T部落开通vip";
        String body = "";
        request.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                + "\"total_amount\":\""+ total_amount +"\","
                + "\"subject\":\""+ subject +"\","
                + "\"body\":\""+ body +"\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        String form = "";
        try {
            form = alipayClient.pageExecute(request).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        httpResponse.setContentType("text/html;charset=" + CHARSET);
        httpResponse.getWriter().write(form);
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();
    }

    @RequestMapping(value = "/returnUrl", method = RequestMethod.GET)
    public String returnUrl(HttpServletRequest request, HttpServletResponse response)
            throws IOException, AlipayApiException {
        System.out.println("=================================同步回调=====================================");
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name =  iter.next();
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            valueStr = new String(valueStr.getBytes("utf-8"), "utf-8");
            params.put(name, valueStr);
        }

        System.out.println(params);
        boolean signVerified = AlipaySignature.rsaCheckV1(params, ALIPAY_PUBLIC_KEY, CHARSET, SIGN_TYPE); // 调用SDK验证签名
        if(signVerified){
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
            String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
            String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"), "UTF-8");

            System.out.println("商户订单号="+out_trade_no);
            System.out.println("支付宝交易号="+trade_no);
            System.out.println("付款金额="+total_amount);
            String[] str=out_trade_no.split("_");
            userService.setVip(Integer.parseInt(str[0]),Double.parseDouble(total_amount));
            return "redirect:http://110.40.211.224:3500/public/index_index";
        }else{
            return "redirect:http://110.40.211.224:3500/public/index_index";
        }
    }
}
