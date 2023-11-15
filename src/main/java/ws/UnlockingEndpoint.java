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
            @WebParam(name = "dashboard_id") Integer dashboard_id) {

        String[] columns = { "socmed_id", "dashboard_id" };
        String[] values = { socmed_id.toString(), dashboard_id.toString() };
        List<Unlocking> listOfUnlocking = Unlocking.findBy(columns, values);
        if (listOfUnlocking.size() > 0) {
            return;
        }
        Unlocking unlocking = new Unlocking(
                socmed_id,
                dashboard_id,
                "AUTO_GENERATED");

        String url = System.getenv("DASHBOARD_REST_URL") + "/public/admin/email";
        String email = new Fetch(url).method("GET").send();
        Unlocking.insert(unlocking);
        System.out.println("Unlocking request notification sent to " + email);
        EmailUtil.sendEmail(email, "New Unlocking Request on Dashboard",
                "Please check your inbox to approve or reject the Unlocking");
    }

    @WebMethod
    public void acceptUnlocking(
            @WebParam(name = "socmed_id") Integer socmed_id,
            @WebParam(name = "dashboard_id") Integer dashboard_id) {
        Unlocking unlocking = new Unlocking(
                socmed_id,
                dashboard_id,
                "AUTO_GENERATED");
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
    }

    @WebMethod
    public void rejectUnlocking(
            @WebParam(name = "socmed_id") Integer socmed_id,
            @WebParam(name = "dashboard_id") Integer dashboard_id) {
        Unlocking unlocking = new Unlocking(
                socmed_id,
                dashboard_id,
                "AUTO_GENERATED");
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
            @WebParam(name = "dashboard_id") Integer dashboard_id) {
        String[] fields = { "socmed_id", "dashboard_id" };
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
