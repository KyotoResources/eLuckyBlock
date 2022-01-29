package it.zS0bye.eLuckyBlock.hooks;

import com.plotsquared.core.PlotAPI;
import com.plotsquared.core.location.Location;
import com.plotsquared.core.player.PlotPlayer;

import java.util.UUID;

public class HPlotSquaredAPI {

    private final PlotAPI api;
    private final UUID uuid;

    public HPlotSquaredAPI(final UUID uuid) {
        this.api = new PlotAPI();
        this.uuid = uuid;
    }

    public boolean checkPlot() {
        PlotPlayer<?> player = this.api.wrapPlayer(uuid);
        if(player == null)
            return false;
        Location loc = player.getLocation();
        if(loc.isPlotArea() &&
                loc.getOwnedPlot().isOwner(uuid)) {
            return true;
        }else return !loc.isPlotArea();
    }

}
