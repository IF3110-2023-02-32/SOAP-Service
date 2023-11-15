package utils;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPFault;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.URI;

public class MessageUtil {
    public static String getIPFromContext(MessageContext messageContext) {
        try {
            Class<?> clazz = messageContext.get("com.sun.xml.internal.ws.http.exchange").getClass();
            Method method = clazz.getDeclaredMethod("getRemoteAddress");
            method.setAccessible(true);

            InetSocketAddress addr = (InetSocketAddress) method
                    .invoke(messageContext.get("com.sun.xml.internal.ws.http.exchange"));
            return addr.getAddress().getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getURLFromContext(MessageContext messageContext) {
        try {
            Class<?> clazz = messageContext.get("com.sun.xml.internal.ws.http.exchange").getClass();
            Method method = clazz.getDeclaredMethod("getRequestURI");
            method.setAccessible(true);

            URI uri = (URI) method.invoke(messageContext.get("com.sun.xml.internal.ws.http.exchange"));
            return uri.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getAllAttributesFromContext(MessageContext messageContext) {
        try {
            SOAPMessageContext smc = (SOAPMessageContext) messageContext;
            SOAPBody soapBody = smc.getMessage().getSOAPBody();
            return String
                    .join(
                            ", ",
                            soapBody
                                    .getTextContent()
                                    .replaceFirst("^\\s++", "")
                                    .replaceAll("(\\s*)", " ")
                                    .split(" "))
                    .trim().replaceFirst(", ", "");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void injectFaultToContext(MessageContext messageContext, String faultCode, String faultString) {
        try {
            SOAPMessageContext smc = (SOAPMessageContext) messageContext;
            SOAPBody soapBody = smc.getMessage().getSOAPBody();
            SOAPFault soapFault = soapBody.addFault();
            soapFault.setFaultCode(faultCode);
            soapFault.setFaultString(faultString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void clearSoapBody(SOAPMessageContext smc) {
        try {
            SOAPBody body = smc.getMessage().getSOAPBody();
            body.removeContents();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
