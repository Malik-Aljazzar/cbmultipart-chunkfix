package dev.malik.cbmultipartchunkfix;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(CBMultipartChunkFix.MODID)
public class CBMultipartChunkFix {
    public static final String MODID = "cbmultipart_chunkfix";
    public static final Logger LOGGER = LogUtils.getLogger();

    public CBMultipartChunkFix(IEventBus modEventBus, ModContainer modContainer) {
        LOGGER.info("CBMultipart Chunk Scheduler Fix loaded (see TheCBProject/CBMultipart#197)");
    }
}
