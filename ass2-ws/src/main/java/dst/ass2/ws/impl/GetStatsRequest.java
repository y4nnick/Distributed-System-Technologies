package dst.ass2.ws.impl;

import dst.ass2.ws.IGetStatsRequest;

/**
 * Created by amra.
 */
public class GetStatsRequest implements IGetStatsRequest {

    private int maxStreamings;

    public GetStatsRequest(){

    }

    @Override
    public int getMaxStreamings() {
        return maxStreamings;
    }

    @Override
    public void setMaxStreamings(int maxStreamings) {
        this.maxStreamings = maxStreamings;

    }
}
