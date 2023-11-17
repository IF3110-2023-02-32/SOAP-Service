package ws;

import java.util.List;
import java.util.ArrayList;

import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;

import model.Unlocking;
import utils.Fetch;
import utils.EmailUtil;

import org.json.JSONObject;
import org.json.JSONArray;

@WebService
@HandlerChain(file = "log_and_auth.xml")
public class UnlockingEndpoint {
    @WebMethod
    public void requestUnlocking(
            @WebParam(name = "socmed_id") Integer socmed_id,
            @WebParam(name = "link_code") String link_code) {

        // String[] columns = { "socmed_id", "dashboard_id", "link_code" };
        // String[] values = { socmed_id.toString(), null, link_code };
        // List<Unlocking> listOfUnlocking = Unlocking.findBy(columns, values);
        // if (listOfUnlocking.size() > 0) {
        //     return;
        // }
        Unlocking unlocking = new Unlocking(
                socmed_id,
                null,
                link_code);
        Unlocking.insert(unlocking);
    }

    @WebMethod
    @WebResult(name = "statusCode")
    public int verifyUnlocking(
            @WebParam(name = "socmed_id") Integer socmed_id,
            @WebParam(name = "dashboard_id") Integer dashboard_id,
            @WebParam(name = "link_code") String link_code) {

        String[] fields = { "link_code" };
        String[] values = { link_code };
        List<Unlocking> matchedResult = Unlocking.findBy(fields, values);
        if (matchedResult == null) {
            return 0;
        } else {
            Unlocking unlocking = new Unlocking(
                    socmed_id,
                    dashboard_id,
                    link_code);
            Unlocking.update(unlocking);
            boolean callbackSuccess = this.unlockingCallback(
                    new ArrayList<Unlocking>() {
                        {
                            add(unlocking);
                        }
                    });
            if (callbackSuccess) {
                System.out.println("Callback success");
            } else {
                System.out.println("Callback failed");
            }
            return 1;
        }
    }

    @WebMethod
    @WebResult(name = "unlocking")
    public List<Unlocking> getUnlocking() {
        return (List<Unlocking>) Unlocking.findAll();
    }

    @WebMethod
    @WebResult(name = "unlocking")
    public Unlocking getSingleUnlocking(
            @WebParam(name = "socmed_id") Integer socmed_id,
            @WebParam(name = "dashboard_id") Integer dashboard_id,
            @WebParam(name = "link_code") String link_code) {
        String[] fields = { "socmed_id", "dashboard_id", "link_code" };
        String[] values = { socmed_id.toString(), dashboard_id.toString() };
        return Unlocking.findBy(fields, values).get(0);
    }

    @WebMethod
    @WebResult(name = "unlocking")
    public List<Unlocking> getUnlockingBySocmedID(
            @WebParam(name = "socmed_id") Integer socmed_id) {
        String[] fields = { "socmed_id" };
        String[] values = { socmed_id.toString() };
        return Unlocking.findBy(fields, values);
    }

    @WebMethod
    @WebResult(name = "unlocking")
    public List<Unlocking> getUnlockingByLinkCode(
            @WebParam(name = "link_code") String link_code) {
        String[] fields = { "link_code" };
        String[] values = { link_code };
        return Unlocking.findBy(fields, values);
    }

    private boolean unlockingCallback(List<Unlocking> listOfUnlocking) {
        JSONArray request = new JSONArray();
        for (Unlocking unlocking : listOfUnlocking) {
            JSONObject obj = new JSONObject();
            obj.put("socmed_id", unlocking.socmed_id);
            obj.put("dashboard_id", unlocking.dashboard_id);
            obj.put("link_code", unlocking.link_code);
            request.put(obj);
        }
        String url = System.getenv("SOCMED_APP_URL") + "/api/callback/unlocking";

        String[] header = {
                "Content-Type: text/plain",
                "Accept: application/json"
        };
        String body = request.toString();

        String response = new Fetch(url).method("PUT").headers(header).body(body).send();

        JSONObject json = new JSONObject(response);
        return json.getString("message").equals("Unlocking updated");
    }
}
