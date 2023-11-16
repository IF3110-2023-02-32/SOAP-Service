package utils;

import java.sql.Timestamp;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import utils.MessageUtil;
import model.Logging;

public class LoggingHandler implements SOAPHandler<SOAPMessageContext> {
    @Override
    public Set<QName> getHeaders() {
        return null;
    }

    @Override
    public void close(MessageContext context) {
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        boolean authorized = (boolean) context.get("authorized");
        log(context, authorized);
        return true;
    }

    @Override
    public boolean handleMessage(SOAPMessageContext context) {

        Boolean outbound = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        if (outbound) {
            boolean authorized = (boolean) context.get("authorized");
            log(context, authorized);
        } else {
            context.put("attributes", MessageUtil.getAllAttributesFromContext(context));
        }
        return true;
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
            } else {
                description.append("Unauthorized access tried to call ");
                QName operation = (QName) smc.get(MessageContext.WSDL_OPERATION);
                description.append(operation.getLocalPart());
                description.append(" with arguments: ");
                String attributes = MessageUtil.getAllAttributesFromContext(smc);
                description.append(attributes);
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
