package dst.ass2.ws.impl;

import dst.ass2.ws.IGetStatsRequest;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Created by amra.
 */

public class GetStatsRequestAdapter extends XmlAdapter<String, IGetStatsRequest> {

    // ulazeci request u j.objekat
    @Override
    public IGetStatsRequest unmarshal(String v) throws Exception {
        GetStatsRequest request = new GetStatsRequest();
        request.setMaxStreamings(new Integer(v));
        return request;
    }

    // obrnuto, j.objekat u request/response
    @Override
    public String marshal(IGetStatsRequest v) throws Exception {
        return Integer.toString(v.getMaxStreamings());
    }
}
