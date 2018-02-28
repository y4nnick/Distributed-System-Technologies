package dst.ass2.ws.impl;

import dst.ass2.ws.IGetStatsResponse;
import dst.ass2.ws.dto.StatisticsDTO;

/**
 * Created by amra.
 */
public class GetStatsResponse implements IGetStatsResponse {

    private StatisticsDTO statisticsDTO;

    public GetStatsResponse (StatisticsDTO statisticsDTO) {
        this.statisticsDTO = statisticsDTO;
    }

    @Override
    public StatisticsDTO getStatistics() {

            return statisticsDTO;
    }
}

