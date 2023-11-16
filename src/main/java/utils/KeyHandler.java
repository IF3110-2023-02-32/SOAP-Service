package utils;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import utils.MessageUtil;
import model.APIKey;
import model.Logging;

public class KeyHandler implements SOAPHandler<SOAPMessageContext> {
    @Override
    public Set<QName> getHeaders() {
        return null;
    }

    @Override
    public void close(MessageContext context) {
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        return true;
    }

    @Override
    public boolean handleMessage(SOAPMessageContext context) {

        Boolean outbound = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        if (outbound) {

            return true;
        } else {

            boolean authorized = authorize(context);
            context.put("authorized", authorized);
            if (!authorized) {
                MessageUtil.clearSoapBody(context);
                MessageUtil.injectFaultToContext(context, "Client", "Unauthorized access");
                log(context, authorized);
            }
            return authorized;
        }
    }

    private boolean authorize(SOAPMessageContext smc) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, List<?>> httpHeaders = (Map<String, List<?>>) smc.get(MessageContext.HTTP_REQUEST_HEADERS);

            if (httpHeaders.containsKey("Authorization")) {
                String authHeader = (String) httpHeaders.get("Authorization").get(0);

                String token = authHeader.split(" ")[1];
                List<APIKey> apiKey = APIKey.findBy("key", token);
                if (apiKey.size() > 0) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void log(SOAPMessageContext smc, boolean authorized) {
        try {
            StringBuilder description = new StringBuilder();
            if (authorized) {
                description.append("Called ");
                QName operation = (QName) smc.get(MessageContext.WSDL_OPERATION);
                description.append(operation.getLocalPart());
                description.append(" with arguments: ");
                String attributes = smc.get("attributes").toString();
                description.append(attributes);
                System.out.println("attributes-a: " + attributes);
            } else {
                description.append("Unauthorized access tried to call ");
                QName operation = (QName) smc.get(MessageContext.WSDL_OPERATION);
                description.append(operation.getLocalPart());
                description.append(" with arguments: ");
                String attributes = MessageUtil.getAllAttributesFromContext(smc);
                description.append(attributes);
                System.out.println("attributes-na: " + attributes);
            }

            Logging log = new Logging(
                    0,
                    description.toString(),
                    MessageUtil.getIPFromContext(smc),
                    MessageUtil.getURLFromContext(smc),
                    new Timestamp(System.currentTimeMillis()));

            Logging.insert(log);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
